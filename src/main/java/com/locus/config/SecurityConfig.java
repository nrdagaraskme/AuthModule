package com.locus.config;

import com.locus.security.TokenAuthenticationFilter;
import com.locus.security.TokenAuthenticationProvider;
import com.locus.security.TokenCreationService;
import com.locus.security.TokenService;
import com.locus.success.AuthenticationSuccessFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenCreationService tokenCreationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenService, tokenCreationService);
        tokenAuthenticationFilter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
        tokenAuthenticationFilter.setAuthenticationManager( authenticationManagerBean());
        http
                .csrf()
                .disable()
                .logout()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(tokenAuthenticationFilter)
                .addFilterAfter(new AuthenticationSuccessFilter(tokenCreationService), TokenAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new TokenAuthenticationProvider());
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedHeaders("auth-token").exposedHeaders("auth-token");
            }
        };
    }
}

