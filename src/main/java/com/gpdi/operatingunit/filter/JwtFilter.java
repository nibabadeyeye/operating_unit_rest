package com.gpdi.operatingunit.filter;

import com.gpdi.operatingunit.config.JwtToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 自定义过滤器
 * @Author: Lxq
 * @Date: 2019/9/6 12:35
 */
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 添加该类的静态对象
     */
    public static JwtFilter jwtFilterUtils;

    public JwtFilter() {
    }

    /**
     * 用PostConstruct修饰init方法，并在init方法中对其赋值
     */
    @PostConstruct
    public void init() {
        jwtFilterUtils = this;
        jwtFilterUtils.redisTemplate = this.redisTemplate;
    }

    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            //判断请求的请求头是否带上 "token"
            if (isLoginAttempt(request, response)) {
                //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
                executeLogin(request, response);
                return true;

            }
            //如果请求头不存在 Token，没有权限访问
            responseError(response, null);
            return false;
        } catch (Exception e) {
            //token 错误/无效
            responseError(response, e.getMessage());
            return false;
        }

    }


    /**
     * 判断用户是否想要登入。
     * 检测 redis 里面是否包含 Token
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String token = getToken(request);
        return token != null;
    }

    /**
     * 执行登录
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String token = getToken(request);
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * token 失效
     */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            Map<String, Object> map = new HashMap<>();
            map.put("Date", new Date());
            map.put("status", 401);
            map.put("data", null);
            map.put("msg", message == null ? "Unauthorized" : message);
            httpResponse.getWriter().print(map);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * redis 刷新时间策略
     */
    private static String getToken(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        if (token != null) {
            // 判断缓存中的token是否过期
            String tokens = jwtFilterUtils.redisTemplate.opsForValue().get(token);
            if (tokens != null) {
                //刷新时间
                jwtFilterUtils.redisTemplate.opsForValue().set(tokens.toString(), token, 2, TimeUnit.HOURS);
                return tokens;
            } else {
                // 缓存时间到,跳转到登录
                return null;
            }
        }
        return null;
    }
}
