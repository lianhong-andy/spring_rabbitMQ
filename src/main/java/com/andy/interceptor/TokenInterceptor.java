package com.andy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 拦截器，用于验签
 */
@Component("tokenInterceptor")
public class TokenInterceptor extends HandlerInterceptorAdapter {
    Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
    @Value("${${lvmama_sign_secret}}")
    private String sign_secret;

    @Value("${IP_ADDRESS}")
    private String ipAddress;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = getIpAddr(request);
        if(ip!=null && ip.contains(ipAddress)){
            return true;
        }
        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL = " + requestURL.toString());
        System.out.println("requestURI = " + requestURI);
        String sign = request.getParameter("sign");
        String appkey = request.getParameter("appkey");
        String t = request.getParameter("t");
        String data = request.getParameter("data");

        StringBuilder content = new StringBuilder();
        content.append("【sign = "+sign+",appkey = "+appkey+",t = "+t+",data = "+data+"】");
        // 3个参数必须都传
        if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(appkey) || StringUtils.isEmpty(t)){
            logger.info("【无效请求-参数为空】IP = " + ip + "，入参：" + content.toString());
            throw new Exception( "缺少参数，数据不完整");
        }
        // 获取HTTP请求的所有键值对，并升序排列
        List<String> params = Collections.list(request.getParameterNames());
        Collections.sort(params);
        return true;
    }

    /**
     * 获取客户端访问的具体IP
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
