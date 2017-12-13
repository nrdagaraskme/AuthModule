package com.locus.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

/**
 * Created by naveen on 09/12/17.
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return createSuccessAuthentication(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication createSuccessAuthentication(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        return new PreAuthenticatedAuthenticationToken(principal, principal.getPassword(), authorities);
    }
}
