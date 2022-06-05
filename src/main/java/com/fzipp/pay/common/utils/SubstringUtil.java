package com.fzipp.pay.common.utils;

/**
 * @ClassName SubstringUtil
 * @Description 字符串处理工具类
 * @Author 24k
 * @Date 2021/12/27 9:42
 * @Version 1.0
 */
public class SubstringUtil {

    /**
     * 截取身份证>生日
     * @param card
     * @return
     */
    public static String getBirthByCard(String card){
        String birth = card.substring(6, 14);
        return birth;
    }

    /**
     * 截取身份证>性别
     * @param card
     * @return
     */
    public static String getSexByCard(String card){//610302199708294512
        Integer sexI = Integer.parseInt(card.substring(16,17));
        String sex;
        if(sexI%2==0){
            sex = "女";
        }else {
            sex = "男";
        }
        return sex;
    }
}
