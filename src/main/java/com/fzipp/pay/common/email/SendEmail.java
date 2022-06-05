package com.fzipp.pay.common.email;

import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.redis.cache.RedisClient;
import com.fzipp.pay.entity.child.EmailVerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.File;

@Component
public class SendEmail {

    @Autowired
    private HttpSession session;

    /**
     * 发件人昵称
     */
    @Value("${spring.mail.nickname}")
    private String nickname;

    /**
     * 发送方账户信息
     */
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 普通邮件
     *
     * @param toEmail
     * @return
     */
    public ResultEmail commonEmail(ToEmail toEmail) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(nickname + '<' + from + '>');
        //谁要接收
        message.setTo(toEmail.getTos());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(toEmail.getContent());
        try {
            mailSender.send(message);
            return new ResultEmail(toEmail, true, "发送普通邮件成功");
        } catch (MailException e) {
            e.printStackTrace();
            return new ResultEmail(toEmail, false, "发送普通邮件失败");
        }
    }

    /**
     * Html邮件
     *
     * @param toEmail
     * @return
     * @throws MessagingException
     */
    public ResultEmail htmlEmail(ToEmail toEmail) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(nickname + '<' + from + '>');
            //谁要接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            helper.setText(toEmail.getContent(), true);
            mailSender.send(message);
            return new ResultEmail(toEmail, true, "发送HTML邮件成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResultEmail(toEmail, false, "发送HTML邮件失败");
        }
    }

    /**
     * 静态资源邮件
     *
     * @param toEmail
     * @param multipartFile
     * @param resId
     * @return
     */
    public ResultEmail staticEmail(ToEmail toEmail, MultipartFile multipartFile, String resId) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(nickname + '<' + from + '>');
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            //邮件内容拼接
            String content =
                    "<html><body><img width='250px' src=\'cid:" + resId + "\'>" + toEmail.getContent()
                            + "</body></html>";
            helper.setText(content, true);
            //蒋 multpartfile 转为file
            File multipartFileToFile = MultipartFileToFile(multipartFile);
            FileSystemResource res = new FileSystemResource(multipartFileToFile);
            //添加内联资源，一个id对应一个资源，最终通过id来找到该资源
            helper.addInline(resId, res);
            mailSender.send(message);
            return new ResultEmail(toEmail, true, "嵌入静态资源的邮件已经发送");
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResultEmail(toEmail, false, "嵌入静态资源的邮件发送失败");
        }
    }

    /**
     * 附件邮件发送
     *
     * @param toEmail
     * @param multipartFile
     * @return
     */
    public ResultEmail enclosureEmail(ToEmail toEmail, MultipartFile multipartFile) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(nickname + '<' + from + '>');
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            helper.setText(toEmail.getContent(), true);
            File multipartFileToFile = MultipartFileToFile(multipartFile);
            FileSystemResource file = new FileSystemResource(multipartFileToFile);
            String filename = file.getFilename();
            //添加附件
            helper.addAttachment(filename, file);
            mailSender.send(message);
            return new ResultEmail(toEmail, true, "附件邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResultEmail(toEmail, false, "附件邮件发送失败");
        }
    }

    /**
     * 附件列表邮件发送
     *
     * @param toEmail
     * @param multipartFiles
     * @return
     */
    public ResultEmail enclosureListEmail(ToEmail toEmail, MultipartFile[] multipartFiles) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(nickname + '<' + from + '>');
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            helper.setText(toEmail.getContent(), true);
            if (multipartFiles != null & multipartFiles.length > 0) {
                for (MultipartFile multipartFile : multipartFiles) {
                    File multipartFileToFile = MultipartFileToFile(multipartFile);
                    FileSystemResource file = new FileSystemResource(multipartFileToFile);
                    String filename = file.getFilename();
                    //添加附件
                    helper.addAttachment(filename, file);
                }
            }
            mailSender.send(message);
            return new ResultEmail(toEmail, true, "附件邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResultEmail(toEmail, false, "附件邮件发送失败");
        }
    }

    /**
     * 客户端邮箱验证码校验
     *
     * @param email 客户Email
     * @param verifyCode 客户校验码
     * @return
     */
    public Boolean isVerifyCode(String email, String verifyCode) {
        if (email == null || verifyCode == null) return false;
        String sessionEmail = (String) session.getAttribute(SysProp.VERIFY_EMAIL_KEY);
        String code = (String) session.getAttribute(SysProp.VERIFY_CODE_KEY);
        if (sessionEmail.equalsIgnoreCase(email) && verifyCode.equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }

    @Autowired
    private RedisClient redisClient;

    /**
     * Redis 邮箱验证 -->>生产环境配置方案
     * @param id
     * @param email
     * @param verifyCode
     * @return
     */
    public Boolean isVerifyCode(String id,String email, String verifyCode) {
        if (id==null||email == null || verifyCode == null) return false;
        EmailVerInfo verInfo = (EmailVerInfo) redisClient.get(id);
        String verEmail = verInfo.getEmail();
        String verCode = verInfo.getCode();
        if (verEmail.equalsIgnoreCase(email) && verifyCode.equalsIgnoreCase(verCode)) {
            return true;
        }
        return false;
    }


    /**
     * 文件类型转换
     *
     * @param multiFile
     * @return
     */
    private File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
