package com.phh.config;

import com.phh.constants.MsgConst;
import com.phh.utils.WebUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常处理
 *
 * @author phh
 * @date 2019/5/16
 */
@ControllerAdvice
public class WebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);

    /**
     * 未认证
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler({UnauthenticatedException.class})
    public String authenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        logger.error("authenticationException", e);
        if (WebUtil.isAjax(request)) {
            // 输出JSON
            Map<String, Object> map = new HashMap<>(2);
            map.put(MsgConst.NOT_LOGIN, "未登录");
            WebUtil.writeJson(response, map);
            return null;
        } else {
            return "redirect:/login";
        }
    }

    /**
     * 无权限
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public String authorizationException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) {
        logger.error("authorizationException", e);
        if (WebUtil.isAjax(request)) {
            // 输出JSON
            Map<String, Object> map = new HashMap<>(2);
            map.put(MsgConst.NO_ACCESS, "无权限");
            WebUtil.writeJson(response, map);
            return null;
        } else {
            return "redirect:/unauthorized";
        }
    }


    /**
     * 全局异常(系统异常)
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    public String exceptionException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error("requestURI:", request.getRequestURI());
        logger.error("exceptionException:", e);
        if (WebUtil.isAjax(request)) {
            // 输出JSON
            Map<String, Object> map = new HashMap<>(2);
            map.put("9999", Objects.toString(e.getMessage(), "系统异常"));
            WebUtil.writeJson(response, map);
            return null;
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("code", "9999");
            session.setAttribute("msg", Objects.toString(e.getMessage(), "系统异常"));
            return "redirect:/error";
        }
    }

}
