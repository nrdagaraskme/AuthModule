package com.locus.security;

import com.nimbusds.jose.JOSEException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

/**
 * Created by naveen on 09/12/17.
 */
public class TokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final TokenService tokenService;
    private final TokenCreationService tokenCreationService;

    @Autowired
    public TokenAuthenticationFilter(TokenService tokenService, TokenCreationService tokenCreationService) {
        this.tokenService = tokenService;
        this.tokenCreationService = tokenCreationService;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String authToken = getTokenFromHeader(request);

        if (StringUtils.isBlank(authToken)) {
            return null;
        }

        User user;
        try {
            user = tokenService.extractAndValidateUsernameFromToken(authToken);
            return user;
        } catch (JOSEException | InvalidKeySpecException | NoSuchAlgorithmException | AuthenticationException | ParseException e) {
            return null;
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "";
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("auth-token");
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        return null;
    }

}
