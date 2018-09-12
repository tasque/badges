package org.badges.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


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
                .antMatchers("/login*").anonymous()
                .antMatchers("/api/**").authenticated()
                .and().formLogin().loginPage("/login-page").loginProcessingUrl("/login").permitAll()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager() {
            private final Map<String, UserPrincipal> users = new HashMap();

            @Override
            public void createUser(UserDetails user) {
                super.createUser(user);
                users.put(user.getUsername(), (UserPrincipal) user);
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return users.computeIfAbsent(username, key -> {
                    throw new UsernameNotFoundException(username);
                });
            }
        };
        userDetailsService.createUser(new UserPrincipal(1L, "ram", "ram123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        userDetailsService.createUser(new UserPrincipal(4L, "ravan", "ravan123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(
                new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        String username = authentication.getName();
                        String password = authentication.getCredentials().toString();

                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (userDetails.getPassword().equals(password)) {
                            return new UsernamePasswordAuthenticationToken(userDetails, password);
                        }
                        throw new BadCredentialsException("Bad creds " + username + " " + password);
                    }

                    @Override
                    public boolean supports(Class<?> aClass) {
                        return auth.equals(UsernamePasswordAuthenticationToken.class);
                    }
                }
        );
    }

}
