package org.vaadin.jchristophe.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.vaadin.jchristophe.views.login.LoginView;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    public static final String LOGOUT_URL = "/logout";
    public static final String LOGOUT_SUCCESS_URL = "/";

    private final UserDetailsService userDetailsService;

    /**
     * userDetailsService is required for the Remember Me
     * https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/remember-me.html
     * @param userDetailsService
     */
    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class, LOGOUT_SUCCESS_URL);
        String privateSecretKeyToChange = "JKJDSKLDJdJSisdjsdfjmkdjdfkljkJKLjlk";
        http.rememberMe().key(privateSecretKeyToChange).tokenValiditySeconds(7200).userDetailsService(this.userDetailsService);
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL, "GET"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/images/*.png");
    }
}
