package com.fzipp.pay.common.utils;

import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.entity.Account;
import com.fzipp.pay.service.AccountService;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token工具类
 * @author 24k
 */
@Component
public class JwtUtil {

    /**
     * 口令时限  60分钟
     */
    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    /**
     * 加密密钥
     */
    private static final String TOKEN_KEY = "24k";

    /**
     * 生成token
     *
     * @param id       账户id：accountId
     * @param userName 账户名：username
     * @return
     */
    public static String createToken(String id, String userName) {
        Map<String, Object> header = new HashMap();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //setID:用户ID
        //setExpiration:token过期时间  当前时间+有效时间
        //setSubject:用户名
        //setIssuedAt:token创建时间
        //signWith:加密方式
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setSubject(userName)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, TOKEN_KEY);
        return builder.compact();
    }

    @Autowired
    private AccountService accountService;

    /**
     * 验证token是否有效
     *
     * @param token 请求头中携带的token
     * @return token验证结果  2-token过期；1-token认证通过；0-token认证失败
     */
    public int verify(String token) {
        Claims claims;
        try {
            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
            claims = Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return 2;
        }
        //从token中获取用户id，查询该Id的账户是否合法，合法则token验证通过
        String id = claims.getId();
        Account account = accountService.getById(id);
        if (account != null && SysProp.ACCOUNT_STATUS_OK.equals(account.getStatus())) {
            return 1;
        } else {
            return 0;
        }
    }
}
