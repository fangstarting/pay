package com.fzipp.pay.common.util;

import com.fzipp.pay.common.utils.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName DatetimeTest
 * @Description 时间测试
 * @Author 24k
 * @Date 2021/12/27 9:15
 * @Version 1.0
 */
public class DatetimeTest {

    private Date datetime;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Test
    public void test1(){
        this.setDatetime(new Date());
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        System.out.println(this.getDatetime());
        System.out.println(sqlDate);
        Date d1 = DateUtil.getSqlDate();
        System.out.println(d1);
    }
    @Test
    public void test2(){
        java.sql.Date sqlInDate = DateUtil.getSqlInDate("19970829");
        System.out.println(sqlInDate);
    }

    @Test
    public void test3(){
        Date utilInDate = DateUtil.getUtilInDate("20211229");
        System.out.println(utilInDate);
    }
    @Test
    public void test4(){
        java.sql.Date sqlInYear = DateUtil.getSqlInYear("2021");
        System.out.println(sqlInYear.toLocalDate().getYear());
    }
    @Test
    public void test5(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        try {
            Date date1 = sdf.parse(DateUtil.WORK_UPTIME);
            Date date2 = sdf.parse("08:45:00");
            Integer flag = date1.compareTo(date2);
            System.out.println(flag);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        boolean flag1 = false;
        boolean flag2 = false;
        try {
            Date date1 = sdf.parse("16:30:00");
            Date date2 = sdf.parse(DateUtil.getTime());
            Date date3 = sdf.parse("21:00:00");
            flag1 = date1.compareTo(date2) >= 0? false:true;
            flag2 = date3.compareTo(date2) >= 0? true:false;
            System.out.println(flag1 && flag2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
