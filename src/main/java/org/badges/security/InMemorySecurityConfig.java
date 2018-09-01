package org.badges.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


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

        auth.inMemoryAuthentication()
                .withUser("ram").password("ram123").roles("USER")
                .and().withUser("ravan").password("ravan123").roles("ADMIN")
                .and().withUser("kans").password("kans123").roles("USER")
        ;
    }
}
