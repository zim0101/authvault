package com.zim0101.authvault.configuration;

import com.zim0101.authvault.service.security.auth.FormBasedAuthenticationFailureHandler;
import com.zim0101.authvault.service.security.auth.FormBasedAuthenticationHandler;
import com.zim0101.authvault.service.security.oauth2.OAuthAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final FormBasedAuthenticationHandler formBasedAuthenticationHandler;
    private final FormBasedAuthenticationFailureHandler formBasedAuthenticationFailureHandler;
    private final OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    public SecurityConfiguration(UserDetailsService userDetailsService,
                                 FormBasedAuthenticationHandler formBasedAuthenticationHandler,
                                 FormBasedAuthenticationFailureHandler formBasedAuthenticationFailureHandler,
                                 OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.formBasedAuthenticationHandler = formBasedAuthenticationHandler;
        this.formBasedAuthenticationFailureHandler = formBasedAuthenticationFailureHandler;
        this.oAuthAuthenticationSuccessHandler = oAuthAuthenticationSuccessHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/assets/**", "/register/**", "/oauth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(formBasedAuthenticationHandler)
                        .failureHandler(formBasedAuthenticationFailureHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(oAuthAuthenticationSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                )
        ;
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}