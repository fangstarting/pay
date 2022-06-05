package com.fzipp.pay.common.constant;

/**
 * @ClassName LogNotes
 * @Description 日志记录
 * @Author 24k
 * @Date 2022/1/2 19:23
 * @Version 1.0
 */
public class LogNotes {

    public static final String UP_USER(Integer opUserId, Integer userId) {
        return "内容:修改用户信息;操作工号:" + opUserId + ",被修改UserId:" + userId;
    }

    public static final String UP_AUDIT(Integer opUserId, Integer auditId, Integer audType) {
        String typeName = null;
        if (audType == 1) typeName = "激活";
        if (audType == 2) typeName = "请假";
        if (audType == 3) typeName = "加班";
        return "内容:修改审批信息,类型:" + typeName + ";操作工号:" + opUserId + ",被修改AuditId:" + auditId;
    }

    public static final String UP_PASSWORD(Integer userId, Integer accountId) {
        return "内容:修改账户密码;操作工号:" + userId + ",被修改账户Id:" + accountId;
    }

    public static final String ADD_MESS_001(String matter, String realName, Integer userId) {
        return matter + "        ----来自工号为" + userId + "的" + realName;
    }

    public static final String SEND_BUTCH_PAY(String content,Integer opUserId){
        return "内容:进行工资批量发放操作;操作工号:" + opUserId + ";消息:" + content;
    }

    public static final String MESS_SYS_UP_PRO_TITLE = "用户头像修改通知";

    public static final String MESS_SYS_UP_PRO_MATTER(String realName, String sex) {
        String call = "尊敬的" + realName;
        if ("男".equals(sex)) {
            call += "先生：";
        } else if ("女".equals(sex)) {
            call += "女士：";
        } else {
            call += "：";
        }
        return call+"恭喜您上传并修改用户头像成功，感谢您对本平台的使用与支持！祝您生活愉快，工作顺利！        ----来自系统通知";
    }

}
