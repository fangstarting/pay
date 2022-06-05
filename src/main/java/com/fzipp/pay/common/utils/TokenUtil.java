package com.fzipp.pay.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 口令工具类：采用阿里的java-jwt的实现口令的生产与验证
 * @author 24k
 */
public class TokenUtil {

	/** 口令时限  60分钟 */
	private static final long EXPIRE_TIME = 60 * 60 * 1000;
	/** 密钥 */
	private static final String TOKEN_SECRET = "fzipp_24k";
	/**
	 * 生成签名: 60分钟过期
	 * @param username 用户名
	 * @param password 密码
	 * @return 生产口令
	 */
	public static String sign(String username,String password) {

		try {

			//设置过期时间
			Date date = new Date(System.currentTimeMillis()+ EXPIRE_TIME);
			//私钥加密算法
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			//设置头部信息
			Map<String,Object> header = new HashMap<>(2);
			header.put("type", "JWT");
			header.put("alg", "HS256");
			//返回Token字符串
//			String token = JWT.create()
//					.withClaim("loginName", username)
//					.withClaim("loginPwd", password)
//					// jwt唯一id
//					.withJWTId(username)
//					// 主题
//					.withSubject("")
//					// 签发的目标
//					.withAudience("")
//					// 签名时间
//					.withIssuedAt(new Date())
//					// token过期时间
//					.withExpiresAt(date)
//					// 签名
//					.sign(algorithm);
			String token = JWT.create()
					.withHeader(header)
					.withClaim("id", username)
					.withClaim("pwd", password)
					.withExpiresAt(date)
					.sign(algorithm);
			return token;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 检验Token是否正确
	 * @param **token** 口令
	 * @return true 口令正确
	 */
	public static boolean verify(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			JWTVerifier build = JWT.require(algorithm).build();
			build.verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
