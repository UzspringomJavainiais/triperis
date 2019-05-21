package com.javainiaisuzspringom.tripperis.utils.journal;

import com.javainiaisuzspringom.tripperis.utils.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView)
        throws Exception {
        System.out.println("Date: " + DateUtils.now().toString() +
                            "\nType: " +  request.getMethod() +
                            "\nMethod:" + request.getRequestURI());
    }

    /**
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("afterCompletion() is invoked");
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("afterConcurrentHandlingStarted() is invoked");
    }
    */
}
