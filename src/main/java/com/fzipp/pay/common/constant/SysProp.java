package com.fzipp.pay.common.constant;

/**
 * @ClassName SysProp
 * @Description 系统属性参数
 * @Author 24k
 * @Date 2022/1/2 19:57
 * @Version 1.0
 */
public class SysProp {

    /**
     * 通用状态码：YES
     */
    public static final Integer COMMON_STATUS_OK = 1;
    /**
     * 通用状态码：NO
     */
    public static final Integer COMMON_STATUS_NO = 0;
    /**
     * 默认操作用户id
     */
    public static final Integer DEFAULT_OP_USERID = 1;

    /**
     * 默认用户头像
     */
    public static final String DEFAULT_USER_PROFILE = "default.png";

    /**
     * 账户状态启用
     */
    public static final Integer ACCOUNT_STATUS_OK = 1;

    /**
     * 账户状态禁用
     */
    public static final Integer ACCOUNT_STATUS_NO = 0;

    /**
     * 消息状态：未读
     */
    public static final Integer MESS_STATUS_NO = 0;

    /**
     * 消息状态：已读
     */
    public static final Integer MESS_STATUS_OK = 1;

    /**
     * 系统通知：否
     */
    public static final Integer MESS_IS_SYSTEM_NO = 0;

    /**
     * 系统通知：是
     */
    public static final Integer MESS_IS_SYSTEM_YES = 1;

    /**
     * 头像文件路径
     */
    public static final String PROFILE_PATH = "profile//";

    /**
     * 头像文件类型
     */
    public static final String PROFILE_TYPE_JPEG = "image/jpeg";

    public static final String PROFILE_TYPE_PNG = "image/png";

    public static final String HTTP_TOP = "http://";

    /**
     * 角色
     */
    public static final Integer ROLE_MANAGE_KEY = 1;

    /**
     * 文件类型
     */
    public static final String FILE_TYPE_EXCEL_1 = ".xlsx";
    public static final String FILE_TYPE_EXCEL_2 = ".xls";

    /**
     * 半角小数点
     */
    public static final String DECIMAL_POINT = ".";

    /**
     * 空字符串
     */
    public static final String NULL_CHAR = "";

    /**
     * 邮箱key
     */
    public static final String VERIFY_EMAIL_KEY = "verifyEmail";

    /**
     * 验证码key
     */
    public static final String VERIFY_CODE_KEY = "verifyCodeKey";

    /**
     * 验证码有效时长 5分钟
     */
    public static final Integer VERIFY_CODE_MAX_TIME = 5*60;

    /**
     * 登陆状态有效
     */
    public static final Integer LOGIN_STATIC_OK = 1;

    /**
     * 考勤状态正常
     */
    public static final Integer CHECK_STATUS_OK = 0;

    /**
     * 考勤状态异常
     */
    public static final Integer CHECK_STATUS_ON = 1;

    /**
     * 薪资配置类型ADD
     */
    public static final String PAY_CONFIG_TYPE_ADD = "增加";
    /**
     * 薪资配置类型SUB
     */
    public static final String PAY_CONFIG_TYPE_SUB = "减少";

    /**
     * 缺勤类型表 请假类型ID
     */
    public static final Integer ABTYPE_LEAVE_ID_2 = 2;

}
