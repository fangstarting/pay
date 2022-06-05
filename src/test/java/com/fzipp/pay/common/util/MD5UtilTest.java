package com.fzipp.pay.common.util;

import com.fzipp.pay.common.utils.MD5Util;
import org.junit.jupiter.api.Test;

/**
 * @ClassName MD5UtilTest
 * @Description
 * @Author 24k
 * @Date 2021/12/28 11:33
 * @Version 1.0
 */
public class MD5UtilTest {

    @Test
    public void getPassByMd5(){
        String s = "b9d11b3be25f5a1a7dc8ca04cd310b28";
        String md5 = MD5Util.getMd5(s);
        System.out.println(md5);
    }
}
