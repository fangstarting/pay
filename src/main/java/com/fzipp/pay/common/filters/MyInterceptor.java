package com.fzipp.pay.common.filters;

import com.alibaba.fastjson.JSON;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.common.utils.TokenUtil;
import com.fzipp.pay.results.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyInterceptor.class);

    @Autowired
    private RequestUtil requestUtil;

    //在HandlerMapping获取handler之后，调用handler之前调用该方法，可以对request,response和handler进行操作
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info("进入preHandle拦截器>>>");
        Integer loginStatus = null;
        try {
            loginStatus = requestUtil.getLoginStatus();
        } catch (Exception e) {
            this.noVerify(response);
            LOGGER.info("preHandle拦截器>>>已拦截此次请求>>登录状态："+loginStatus);
            return false;
        }
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            LOGGER.info("请求方式：OPTIONS>>放行");
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token != null && !"".equals(token)) {
            token = token.split(" ")[1];
            LOGGER.info("Token令牌："+token);
        }
        boolean verify = TokenUtil.verify(token);
        if (!(verify && SysProp.LOGIN_STATIC_OK.equals(loginStatus))) {
            this.noVerify(response);
            LOGGER.info("preHandle拦截器>>>已拦截此次请求>>Token验证:"+verify+",登录状态："+loginStatus);
            return false;
        }
        return true;
    }

    //在调用handler之后，视图渲染之前调用，可以对中request,response,handler,modelAndView进行操作
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //在视图渲染之后，返回客户端之前调用，可以对request,response,handler以及handler产生的异常信息ex进行操作
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void noVerify(HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Result result = new Result();
        result.setError(ErrorCode.NOT_POWER);
        result.setMessage(Messages.MESSAGE_NOT_POWER);
        try {//可能存在Cookie无法携带从而丢失Http对象
            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            LOGGER.error("preHandle拦截器>>>存在Cookie无法携带而导致丢失Http对象");
        }
    }
}
