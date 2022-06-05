package com.fzipp.pay.common.util;

import com.fzipp.pay.common.utils.RandomStrUtil;
import org.junit.Test;

public class RandomStrUtilsTest {

    @Test
    public void test_001(){
        String randomCode = RandomStrUtil.getRandomCode(6);
        System.out.println(randomCode);
        String randomNumCode = RandomStrUtil.getRandomNumCode(6);
        System.out.println(randomNumCode);
    }
}
