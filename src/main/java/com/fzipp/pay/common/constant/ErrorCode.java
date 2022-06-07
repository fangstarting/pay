package com.fzipp.pay.common.constant;

/**
 * @ClassName ErrorCode
 * @Description 常量标识码
 * @Author 24k
 * @Date 2021/12/26 17:49
 * @Version 1.0
 */
public class ErrorCode {

    /**
     * 调用接口正常
     */
    public static final Integer CORRECT = 1;

    /**
     * 调用接口错误
     */
    public static final Integer FAULT = 0;

    /**
     * 警告消息
     */
    public static final Integer WARNING = 2;

    /**
     * 需要后续操作的验证 例如：覆盖操作
     */
    public static final Integer LATER_ACTION = 10;

    /**
     * 权限认证失败
     */
    public static final Integer NOT_POWER = 403;

    /**
     * 错误代码
     */
    public static final String TITLE = "errorcode";

    /**
     * 业务逻辑返回码
     * 100业务正确/正常
     * 101业务错误/异常
     * @param b
     * @return
     */
    public static final String VERIFY_BIZ(boolean b){
        return b?"100":"101";
    }

    /**
     * 参数错误
     */
    public static final String VERIFY_PARAMS_ERROR = "105";

    /**
     * 状态验证
     * 110启用
     * 111停用
     * @param b
     * @return
     */
    public static final String VERIFY_STATUS(boolean b){return b?"110":"111";}

    /**
     * 激活验证 120激活 121未激活
     * @param b
     * @return
     */
    public static final String VERIFY_ACTIVE(boolean b){return b?"120":"121";}
}
