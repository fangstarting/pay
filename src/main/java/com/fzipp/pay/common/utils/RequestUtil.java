package com.fzipp.pay.common.utils;

import com.fzipp.pay.common.redis.cache.RedisClient;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.entity.child.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class RequestUtil {

    @Autowired
    private HttpServletRequest request;

//    @Autowired
//    private HttpSession httpSession;

    @Autowired
    private RedisClient redisClient;


    //登录成功 记录账户信息
//        request.getSession().setAttribute("username",username);
//        request.getSession().setAttribute("accountId",account.getAccountId());
//        request.getSession().setAttribute("userId",user.getUserId());
//        request.getSession().setAttribute("status",1);
//        request.getSession().setAttribute("user",user);

    private UserLogin getUserLogin(){
        String token = request.getHeader("Authorization");
        if (token != null && !"".equals(token)) {
            token = token.split(" ")[1];
        }
        return (UserLogin) redisClient.get(token);
    }

    public User getUser(){
        return this.getUserLogin().getUser();
    }

    public String getUsername(){
        return this.getUserLogin().getUsername();//此初realname实则为username
    }

    public Integer getUserId(){
        return this.getUserLogin().getUser().getUserId();
    }

    public Integer getAccountId(){
        return this.getUserLogin().getAccountId();
    }

    public Integer getLoginStatus(){
        return this.getUserLogin().getLoginStatus();
    }

//    public User getUser(){
//        return (User) request.getSession().getAttribute("user");
//    }
//
//    public String getUsername(){
//        return (String) request.getSession().getAttribute("username");
//    }
//
//    public Integer getUserId(){
//        return (Integer) request.getSession().getAttribute("userId");
//    }
//
//    public Integer getAccountId(){
//        return (Integer) request.getSession().getAttribute("accountId");
//    }
//
//    public Integer getLoginStatus(){
//        return (Integer) request.getSession().getAttribute("status");
//    }

}
