package com.fzipp.pay;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimeUnitTests {

    @Test
    public void test_001(){
        TimeUnit days = TimeUnit.DAYS;
        long time = new Date().getTime();
        long l = TimeUnit.DAYS.toHours(1000);
        Date date = new Date(l);
        System.out.println(date);
        System.out.println(TimeUnit.DAYS.toDays(time));
        log.debug(time+"");
        log.debug(l+"");

    }

}
