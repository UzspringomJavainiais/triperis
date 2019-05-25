package com.javainiaisuzspringom.tripperis.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtTokenFilter extends GenericFilterBean {

    private static List<String> urisToSkip = Arrays.asList("/api/auth/", "/auth/");

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
                                                                                                    ServletException {

        HttpServletRequest httpServletRequest = ((HttpServletRequest) req);
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println("do filter: " + requestURI);

        if (shouldSkipRequest(requestURI)) {
            System.out.println("not validating uri: " + requestURI);
        } else {
            System.out.println("validating uri: " + requestURI);
            String token = jwtTokenProvider.resolveToken(httpServletRequest);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(req, res);
    }

    private Boolean shouldSkipRequest(String requestURI) {
        return urisToSkip.stream().anyMatch(requestURI::startsWith);
    }
}