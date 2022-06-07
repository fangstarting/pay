package com.fzipp.pay.ddos;

import com.fzipp.pay.common.ddos.DdosBean;
import com.fzipp.pay.params.ddos.DdosStartParam;
import org.junit.Test;

/**
 * @program pay
 * @description
 * @Author FFang
 * @Create 2022-06-07 05:27
 */
public class DdosTest {
    public static void main(String[] args) {
        DdosStartParam d = new DdosStartParam();
        d.setConcurrent(3);
        d.setInterval(1000L);
        DdosBean.startDDOS(d);
        try {
            Thread.sleep(5000);
            DdosBean.ddosObj().stop();
            System.out.println("stop");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void t1(){
        long a = 1000/120;
        System.out.println(a);
    }

}
