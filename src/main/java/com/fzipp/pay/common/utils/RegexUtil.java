package com.fzipp.pay.common.utils;

import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 邮箱正则
     * @param email
     * @return
     */
    public static Boolean isValidEmail(String email){
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }
}
