package com.fzipp.pay.common.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrossFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossFilter.class);

    /**
     * Default constructor.
     */
    public CrossFilter() {
        LOGGER.info("跨域过滤器构建>>>");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("进入跨域过滤器doFilter>>>");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String origin = "*";
        if(request.getHeader("Origin") != null) {
            origin = request.getHeader("Origin");
        }
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        if(request.getMethod().toLowerCase().equals("options")) {
            //开启口令，允许自定义头
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");//这里“token”是我要传到后台的内容key
            response.setHeader("Access-Control-Expose-Headers", "*");
            return;
        }
        chain.doFilter(req, res);
    }


//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        LOGGER.info("进入跨域过滤器doFilter>>>");
////        response.setCharacterEncoding("utf-8");
////        response.setContentType("text/json;charset=utf-8");
//        /** 设置响应头允许ajax跨域访问 **/
//        HttpServletResponse rp=(HttpServletResponse)response;
//        HttpServletRequest req=(HttpServletRequest) request;
//
////        if (req.getMethod().equals("OPTIONS")) {
////            rp.setStatus(HttpServletResponse.SC_OK);
////            chain.doFilter(request, response);
////            return;
////        }
//
////        rp.setCharacterEncoding("utf-8");
////        rp.setContentType("text/json;charset=utf-8");
////        req.setCharacterEncoding("utf-8");
//        //跨域访问
//        String origin = "*";
//        if(req.getHeader("Origin") != null) {
//            origin = req.getHeader("Origin");
//        }
//        /** 使用CORS协议允许Response进行跨域 */
//        //允许跨域的主机地址
////        rp.setHeader("Access-Control-Allow-Origin", origin);
//		rp.setHeader("Access-Control-Allow-Origin", "*");
//        //允许跨域的请求方法
////		rp.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
//        rp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, GET");
////        rp.setHeader("Access-Control-Allow-Methods", "*");
//        //跨域的缓存时间
//        rp.setHeader("Access-Control-MAX-Age", "3600");//跨域访问时间
//        //允许跨域请求头
////        rp.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type");/** requested -> request*/
////		rp.setHeader("Access-Control-Allow-Headers", "x-request-with,Content-Type");
////        rp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        rp.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, Content-Type, Accept");
////		rp.setHeader("Access-Control-Allow-Headers", "*");
//        //是否携带Cookie
//        rp.setHeader("Access-Control-Allow-Credentials", "true");
//
//        chain.doFilter(request, response);
//    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        LOGGER.info("跨域过滤器销毁>>>");
    }
}
