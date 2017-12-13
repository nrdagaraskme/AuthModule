package com.locus.success;

import com.locus.security.TokenCreationService;
import com.locus.security.User;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by dhruv on 15/07/16.
 */
public class AuthenticationSuccessFilter implements Filter {

    private final TokenCreationService tokenCreationService;

    public AuthenticationSuccessFilter(TokenCreationService tokenCreationService) {
        this.tokenCreationService = tokenCreationService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                ((HttpServletResponse) response).setHeader("auth-token", tokenCreationService.getSignedToken((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
            }
            chain.doFilter(request, response);
        } catch (JOSEException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
