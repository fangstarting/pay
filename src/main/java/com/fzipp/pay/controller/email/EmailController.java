package com.fzipp.pay.controller.email;

import com.alibaba.fastjson.JSON;
import com.fzipp.pay.common.constant.CommonMethod;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.email.ResultEmail;
import com.fzipp.pay.common.email.SendEmail;
import com.fzipp.pay.common.email.ToEmail;
import com.fzipp.pay.common.redis.cache.RedisClient;
import com.fzipp.pay.common.utils.RandomStrUtil;
import com.fzipp.pay.common.utils.RegexUtil;
import com.fzipp.pay.entity.child.EmailVerInfo;
import com.fzipp.pay.results.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.UUID;


@RestController
@RequestMapping("/email")
public class EmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private SendEmail sendEmail;

//    @Autowired
//    private HttpSession session;

    @Autowired
    private RedisClient redisClient;

    /**
     * 邮箱校验：验证码发送<br>
     * 将Email与code存储到session中 时长5分钟  //TODO 生产环境->>存储至redis
     *
     * @param email
     * @return
     */
    @RequestMapping(value = "/verify/send", method = RequestMethod.GET)
    public Result verifySendEmail(@RequestParam(value = "email", required = false) String email) {
        Result result = new Result();
        try {
            if (!RegexUtil.isValidEmail(email)) {
                result.setError(ErrorCode.FAULT);
                result.setMessage(Messages.MESSAGE_NOT_EMAIL_01);
                return result;
            }
            String verifyCode = RandomStrUtil.getRandomCode(6);
            //TODO   生产环境->>存储至redis
//            session.setMaxInactiveInterval(SysProp.VERIFY_CODE_MAX_TIME);
//            session.setAttribute(SysProp.VERIFY_EMAIL_KEY, email);
//            session.setAttribute(SysProp.VERIFY_CODE_KEY, verifyCode);

            String id = UUID.randomUUID().toString();
            EmailVerInfo emailVerInfo = new EmailVerInfo(id,email,verifyCode);
            redisClient.set(id,emailVerInfo,SysProp.VERIFY_CODE_MAX_TIME);


            ToEmail toEmail = CommonMethod.getVerifyToEmail(email, verifyCode);
            sendEmail.htmlEmail(toEmail);
//            LOGGER.info("邮箱验证发送成功>>>SessionID:"+session.getId()+">>>"+JSON.toJSONString(toEmail));
            LOGGER.info("邮箱验证发送成功>>>id:"+id+">>>"+JSON.toJSONString(toEmail));
            result.setError(ErrorCode.CORRECT);
            result.setData(id);
            result.setSign(true);
            result.setMessage(Messages.MESSAGE_SEND_VERIFY_Y);
        } catch (Exception e) {
            result.setMessage(Messages.MESSAGE_SEND_VERIFY_N);
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 邮箱校验：验证码校验
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/verify/check", method = RequestMethod.GET)
    public Result verifyCheckEmail(String email,@RequestParam(value = "code", required = false) String code) {
        Result result = new Result();
        try {
            Boolean verifyCode = sendEmail.isVerifyCode(email,code);
            result.setError(ErrorCode.CORRECT);
            result.setSign(verifyCode);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/common")
    public String commonEmail() {
        ToEmail toEmail = new ToEmail();
        String[] tos = {"121582384@qq.com"};
        toEmail.setTos(tos);
        toEmail.setSubject("Test510");
        toEmail.setContent("test......");
        ResultEmail resultEmail = sendEmail.commonEmail(toEmail);
        return resultEmail.getMessage();
    }


    @GetMapping("/html")
    public String htmlEmail() {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h1>这是Html格式邮件!,不信你看邮件，我字体比一般字体还要大</h1>\n" +
                "</body>\n" +
                "</html>";
        String[] tos = {"121582384@qq.com"};
        String title = "Html邮件";
        ToEmail toEmail = new ToEmail(tos, title, content);
        ResultEmail resultEmail = null;
        resultEmail = sendEmail.htmlEmail(toEmail);
        return resultEmail.getMessage();
    }

    @PostMapping("/image")
    public ResultEmail imageEmail(@RequestParam(value = "file") MultipartFile multipartFile, String resId) {
        ToEmail toEmail = new ToEmail();
        String[] tos = {"121582384@qq.com"};
        toEmail.setTos(tos);
        toEmail.setSubject("Test-image");
        toEmail.setContent("test image......");
        return sendEmail.staticEmail(toEmail, multipartFile, resId);
    }

    @PostMapping("/enclosure")
    public ResultEmail enclosureEmail(@RequestParam(value = "file") MultipartFile multipartFile) {
        ToEmail toEmail = new ToEmail();
        String[] tos = {"121582384@qq.com"};
        toEmail.setTos(tos);
        toEmail.setSubject("Test-enclosure");
        toEmail.setContent("test enclosure......");
        return sendEmail.enclosureEmail(toEmail, multipartFile);
    }

}
