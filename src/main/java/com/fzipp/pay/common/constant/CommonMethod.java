package com.fzipp.pay.common.constant;

import com.fzipp.pay.common.email.ToEmail;

/**
 * 业务通用静态方法
 */
public class CommonMethod {

    /**
     * 邮箱验证：Html
     * @param email 邮箱地址
     * @param verifyCode 验证码
     * @return ToEmail
     */
    public static final ToEmail getVerifyToEmail(String email, String verifyCode) {
        String[] tos = {email};
        String content =
                "<html>" +
                        "<body>" +
                        "<p>"+email+"，你好!</p>" +
                        "<p>我们已收到你要求获得 Pay 帐户所用的一次性代码的申请。</p>" +
                        "<p>你的一次性代码为: <strong>"+verifyCode+"</strong></p>" +
                        "<p>代码有效时长<strong>5分钟</strong>，请勿重复获取。</p>" +
                        "<p>如果你没有请求此代码，可放心忽略这封电子邮件。别人可能错误地键入了你的电子邮件地址。</p>" +
                        "<hr />"+
                        "<p><em>谢谢!</br>Pay 帐户团队</em></p>" +
                        "</body>" +
                        "</html>";
        return new ToEmail(tos,Messages.EMAIL_VERIFY_TITLE,content);
    }

    /**
     * 薪资发放通知
     * @param email
     * @param realname 姓名
     * @param date yyyy-MM
     * @param idealpay 应发工资
     * @param factpay 实发工资
     * @return
     */
    public static final ToEmail getPayToEmail(String email,String realname,String date,String idealpay,String factpay){
        String[] tos = {email};
        String subject = Messages.Pay_MAIL_TITLE;
        String content =
                "<html>" +
                        "<body>" +
                        "<p>"+email+"，"+realname+"你好!</p>" +
                        "<p>您的<strong>"+date+"</strong>月份工资已发放，请注意查收。</p>" +
                        "<p>应发工资: <strong>"+idealpay+"</strong>元</p>" +
                        "<p>实发工资: <strong>"+factpay+"</strong>元</p>" +
                        "<p>感谢您的辛勤付出，祝您工作顺利，身体康健！</p>" +
                        "<hr />"+
                        "<p><em>谢谢!</br>Pay 薪资中心</em></p>" +
                        "</body>" +
                        "</html>";
        return new ToEmail(tos,subject,content);
    }

}
