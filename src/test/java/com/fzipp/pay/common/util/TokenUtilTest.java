package com.fzipp.pay.common.util;

import com.fzipp.pay.common.utils.TokenUtil;
import org.junit.jupiter.api.Test;

/**
 * @ClassName TokenUtilTest
 * @Description
 * @Author 24k
 * @Date 2021/12/26 21:51
 * @Version 1.0
 */
public class TokenUtilTest {

    @Test
    public void getToken(){
        String token = TokenUtil.sign("fengfang", "123456");
        System.out.println(token);

        boolean verify = TokenUtil.verify(token);
        System.out.println(verify);
    }
}
