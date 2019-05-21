package com.javainiaisuzspringom.tripperis.utils.journal;

import com.javainiaisuzspringom.tripperis.domain.AccessLog;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.repositories.AccessLogRepository;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import com.javainiaisuzspringom.tripperis.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView)
        throws Exception {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof Account) {
            Account account = (Account) obj;



            AccessLog log = new AccessLog();
            log.setType(request.getMethod());
            log.setAccount(account);
            log.setAction(request.getRequestURI());
            log.setDate(DateUtils.now());
            log.setAccount(account);

            accessLogRepository.save(log);
        }
    }
}
