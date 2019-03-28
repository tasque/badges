package org.badges.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@Configuration
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {


    @Autowired
    private LdapUserDetailsContextMapper ldapUserDetailsContextMapper;


    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>()
                .configure(builder);
        builder.authenticationProvider(activeDirectoryLdapAuthenticationProvider());

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*").anonymous()
                .antMatchers("/api/**").authenticated()
                .and().formLogin().loginPage("/login-page").loginProcessingUrl("/login").permitAll()
                .and().sessionManagement().maximumSessions(1);
    }

    @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
                "mara.local", "ldap://ad-auth.mara.local");

        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setUserDetailsContextMapper(ldapUserDetailsContextMapper);

        return provider;
    }


    // AuthenticationEntryPoint, AccessDeniedHandler, Filter
}
