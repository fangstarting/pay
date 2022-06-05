package com.fzipp.pay.common.util;

import com.fzipp.pay.common.utils.SubstringUtil;
import org.junit.jupiter.api.Test;

/**
 * @ClassName SubstringUtilTest
 * @Description
 * @Author 24k
 * @Date 2021/12/27 10:05
 * @Version 1.0
 */
public class SubstringUtilTest {

    @Test
    public void test1(){
        String sexByCard = SubstringUtil.getSexByCard("610302199708294511");
        System.out.println(sexByCard);
    }
}
