package org.badges.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Configuration
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>()
                .configure(builder);
        builder.authenticationProvider(activeDirectoryLdapAuthenticationProvider());

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                    .antMatchers("/*").permitAll()
                    .anyRequest().authenticated()
                    .and()
//                .httpBasic()
//                    .authenticationEntryPoint(new AuthenticationErrorsHandler())
//                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(new AuthenticationErrorsHandler())
                    .and()
                .formLogin()
                    .and()
                .addFilterBefore(new AuthenticationErrorsHandler(), BasicAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
                "some domain", "ldap://localhost/");

        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);

        return provider;
    }

    // AuthenticationEntryPoint, AccessDeniedHandler, Filter
}
