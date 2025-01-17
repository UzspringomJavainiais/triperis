package com.javainiaisuzspringom.tripperis.utils.journal;

import com.javainiaisuzspringom.tripperis.domain.AccessLog;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.repositories.AccessLogRepository;
import com.javainiaisuzspringom.tripperis.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AccessLogRepository accessLogRepository;

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                @Nullable Exception ex) throws Exception {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccessLog log = new AccessLog();
        log.setType(request.getMethod());
        log.setAction(request.getRequestURI());
        log.setDate(DateUtils.now());
        log.setIpAddress(request.getRemoteAddr());
        log.setRequestorInfo(request.getHeader("User-Agent"));

        if (obj instanceof Account) {
            Account account = (Account) obj;

            StringBuilder roles = new StringBuilder();

            for (Role role : account.getRoles())
                roles.append(role.getId()).append(";");

            log.setRoles(roles.toString().isEmpty() ? null : roles.toString());

            log.setAccount(account);
        }

        accessLogRepository.save(log);
    }
}
