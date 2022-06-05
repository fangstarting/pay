package com.fzipp.pay.common.constant;


public class Messages {

    public static final String API_500 = "接口调用出现异常错误！";

    public static final String API_403 = "权限认证未通过！";

    public static final String PARAMS_EX = "参数错误！";

    /**
     * 权限认证失败消息
     */
    public static final String MESSAGE_NOT_POWER = "安全认证失败，请重新登录！";

    /**
     * 邮箱验证Title
     */
    public static final String EMAIL_VERIFY_TITLE = "你的一次性代码";

    public static final String MESSAGE_NOT_EMAIL_01 = "输入的邮箱地址无效！";

    public static final String MESSAGE_SEND_VERIFY_Y = "验证码发送成功！";

    public static final String MESSAGE_SEND_VERIFY_N = "验证码发送失败！";

    public static final String UPLOAD_PRO_NULL = "文件有误！";

    public static final String UPLOAD_PRO_TYPE_NO = "文件格式有误！";

    public static final String UPLOAD_PRO_Y = "上传成功！";

    public static final String UPLOAD_PRO_N = "上传失败！";

    /**
     * 系统配置项message
     */
    public static final String REMOVE_CONFIGS_EX = "系统默认配置项目您无权执行删除操作！";

    public static final String UPDATE_CONFIGS_EX = "系统默认配置项目您无权执行修改操作！";

    public static final String DEL_REF_EX = "数据已被引用无法直接删除，请删除引用数据后重试！";

    /**
     * 薪资发放
     */
    public static final String Pay_MAIL_TITLE = "Pay薪资发放通知";
}
