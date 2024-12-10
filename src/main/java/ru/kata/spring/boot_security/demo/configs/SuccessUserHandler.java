package ru.kata.spring.boot_security.demo.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(SuccessUserHandler.class);

    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        List<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).stream().toList();
        logger.info("User authenticated with roles: {}", roles);
        if (roles.contains("ROLE_USER") && !request.getRequestURI().equals("/user")) {
            logger.info("Redirecting to /user");
            response.sendRedirect("/user");
        } else {
            logger.info("Redirecting to /");
            response.sendRedirect("/");
        }
    }
}