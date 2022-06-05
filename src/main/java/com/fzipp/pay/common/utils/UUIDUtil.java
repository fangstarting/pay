package com.fzipp.pay.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class UUIDUtil {
	/**
	 * UUID插入时间+name
	 * @param username
	 * @return
	 */
	public static String getUUIDN(Object username) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateStr = simpleDateFormat.format(new Date());
		String uuid = dateStr+UUID.randomUUID().toString().replace("-", "")+"name"+username;
		return uuid;
	}
	/**
	 * UUID插入时间
	 * @return
	 */
	public static String getUUID() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateStr = simpleDateFormat.format(new Date());
		String uuid = dateStr+UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
	/**
	 * 获取编号
	 * @param id
	 * @return
	 */
	public static String getUUIDT(Object id) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = simpleDateFormat.format(new Date());
		String uuid = "T"+dateStr+id;
		return uuid;
	}
}
