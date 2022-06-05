package com.fzipp.pay.common.utils;

import com.fzipp.pay.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	// 静态方法，便于作为工具类
	/**
	 * @param plainText 加密的字符串
	 * @return 加密后的字符串
	 */
	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPass(String password,String username){
		return getMd5(password+username);
	}

	/*public static void main(String[] args) {
		// 测试
		System.out.println(MD5Util.getMd5("123456789"));
	}*/

}
