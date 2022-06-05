package com.fzipp.pay.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * 获取当前日期
	 * @return "yyyy-MM-dd"
	 */
	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = simpleDateFormat.format(new Date());
		return dateStr;
	}

	/**
	 * 获取当前日期
	 * @return "yyyyMMddHHmmss"
	 */
	public static String getDateOfYS() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = simpleDateFormat.format(new Date());
		return dateStr;
	}

	/**
	 * 获取当前日期时间
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String getDatetime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetimeStr = simpleDateFormat.format(new Date());
		return datetimeStr;
	}

	/**
	 *
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 开始时间<结束时间: 1;开始时间==结束时间: 0;否则: -1
	 */
	public static int compareToDate(Date start,Date end){
		return end.compareTo(start);
	}

	/**
	 * 获取当前时间
	 * @return "HH:mm:ss"
	 */
	public static String getTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String timeStr = simpleDateFormat.format(new Date());
		return timeStr;
	}

	/**
	 * 获取当前SqlDate日期时间
	 * @return
	 */
	public static java.sql.Date getSqlDate(){
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		return sqlDate;
	}

	/**
	 * 获取指定日期SqlDate日期
	 * @param sDate *"yyyyMMdd"*
	 * @return
	 */
	public static java.sql.Date getSqlInDate(String sDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date parse = null;
		try {
			parse = simpleDateFormat.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlInDate = new java.sql.Date(parse.getTime());
		return sqlInDate;
	}

	public static java.sql.Date getSqlInYear(String sDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		Date parse = null;
		try {
			parse = simpleDateFormat.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlInDate = new java.sql.Date(parse.getTime());
		return sqlInDate;
	}

	/**
	 * java.Util.Date ->> yyyy-MM-dd(String)
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}

	/**
	 * 获取指定日期UtilDate日期
	 * @param sDate *"yyyyMMdd"*
	 * @return
	 */
	public static java.util.Date getUtilInDate(String sDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date parse = null;
		try {
			parse = simpleDateFormat.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.util.Date utilInDate = new Date(parse.getTime());
		return utilInDate;
	}

	/**
	 * 获取指定日期与类型的java.util.Date对象
	 * @param dateStr 时间字符串
	 * @param format 类型 例如yyyyMMdd
	 * @return
	 */
	public static Date getDate(String dateStr,String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.parse(dateStr);
	}

	/**
	 * 获取指定年月中月的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getMonthLastDay(int year,int month){
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 获取指定日期与类型的日期字符串
	 * @param date 时间
	 * @param format 类型 例如>> "yyyyMMddHHmmss"
	 * @return
	 */
	public static String getStrDateFormat(Date date,String format){
		return new SimpleDateFormat(format).format(date);
	}


	/**
	 * 上班打卡开始时间
	 */
	public static final String WORK_UPTIME_START = "07:00:00";
//	public static final String WORK_UPTIME_START = "01:00:00";
	/**
	 * 上班打卡标准时间
	 */
	public static final String WORK_UPTIME = "08:30:00";
	/**
	 * 上班打卡结束时间
	 */
	public static final String WORK_UPTIME_END = "09:30:00";
//	public static final String WORK_UPTIME_END = "23:00:00";
	/**
	 * 下班打卡 开始时间
	 */
	public static final String WORK_DOWNTIME_START = "17:00:00";
//public static final String WORK_DOWNTIME_START = "01:00:00";
	/**
	 * 下班打卡 标准时间
	 */
	public static final String WORK_DOWNTIME = "17:30:00";
	/**
	 * 下班打卡 结束时间
	 */
	public static final String WORK_DOWNTIME_END = "23:30:00";

	/**
	 * 迟到判断
	 * @return *正常0  *迟到1
	 */
	public static int ifLate() {
		int flag = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date1 = sdf.parse(DateUtil.WORK_UPTIME);
			Date date2 = sdf.parse(DateUtil.getTime());
			flag = date1.compareTo(date2) >= 0? 0:1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 是否在上班打卡时间内
	 * @return
	 */
	public static boolean ifUpTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		boolean flag1 = false;
		boolean flag2 = false;
		try {
			Date date1 = sdf.parse(DateUtil.WORK_UPTIME_START);
			Date date2 = sdf.parse(DateUtil.getTime());
			Date date3 = sdf.parse(DateUtil.WORK_UPTIME_END);
			flag1 = date1.compareTo(date2) >= 0? false:true;
			flag2 = date3.compareTo(date2) >= 0? true:false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag1&&flag2;
	}

	/**
	 * 是否在下班打卡时间内
	 * @return
	 */
	public static boolean ifDownTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		boolean flag1 = false;
		boolean flag2 = false;
		try {
			Date date1 = sdf.parse(DateUtil.WORK_DOWNTIME_START);
			Date date2 = sdf.parse(DateUtil.getTime());
			Date date3 = sdf.parse(DateUtil.WORK_DOWNTIME_END);
			flag1 = date1.compareTo(date2) >= 0? false:true;
			flag2 = date3.compareTo(date2) >= 0? true:false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag1&&flag2;
	}
	/**
	 * 早退判断
	 * @return *正常0  *早退1
	 */
	public static int ifEarly() {
		int flag = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date1 = sdf.parse(DateUtil.WORK_DOWNTIME);
			Date date2 = sdf.parse(DateUtil.getTime());
			flag = date1.compareTo(date2) <= 0? 0:1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
