package com.fzipp.pay.controller.captcha;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fzipp.pay.common.redis.cache.RedisClient;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {

    @Resource
    private RedisClient redisClient;

    @GetMapping("/gain")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        specCaptcha.setFont(Captcha.FONT_1);
        String id = UUID.randomUUID().toString();
        response.setHeader("id", id);
        CaptchaUtil.out(specCaptcha, request, response);
        String verCode = specCaptcha.text().toLowerCase();
        //不设置过期
//        redisClient.set(id, verCode);
        //设置5分钟过期
        redisClient.set(id,verCode,300);
    }

    @PostMapping(value = "/check")
    public boolean check(@RequestBody String info) {
        JSONObject jsonObject = JSON.parseObject(info);
        //获取传过来的id 和 code
        String id = jsonObject.getString("id");
        String code = jsonObject.getString("code");
        if (id == null || "".equals(id) || code == null || "".equals(code)) return false;
        //获取redis里面存的code
        Object obj = redisClient.get(id);
        if (obj == null) return false;
        //比较输入的code和redis的code
//        log.error("id："+id+"验证码："+obj.toString());
        boolean flag = code.equalsIgnoreCase(obj.toString());
        //匹配成功就删除redis存储
        if (flag) redisClient.delete(id);
        return flag;
    }
}
