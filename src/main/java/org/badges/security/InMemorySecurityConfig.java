package org.badges.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


@Configuration
@EnableWebSecurity
//@ConditionalOnProperty("security.enabled")
public class InMemorySecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**")
                .authenticated()
                .and().formLogin().loginPage("/login-page").loginProcessingUrl("/login").permitAll()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> conf = auth
                .inMemoryAuthentication();
        conf.getUserDetailsService().createUser(new UserPrincipal(1L, "ram", "ram123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        conf.getUserDetailsService().createUser(new UserPrincipal(4L, "ravan", "ravan123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));

    }

}
