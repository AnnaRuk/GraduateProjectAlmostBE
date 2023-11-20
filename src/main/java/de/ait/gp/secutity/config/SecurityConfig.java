package de.ait.gp.secutity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig {

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(UserDetailsService userDetailsServiceImpl,
                                                         PasswordEncoder passwordEncoder,
                                                         AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

        httpSecurity.authorizeRequests()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/register/**").permitAll()
                .antMatchers("/api/users/confirm/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/kindergartens/**").permitAll()
                .antMatchers("/api/users/profile/controlKindergarten").hasAnyAuthority("MANAGER")
                .antMatchers("/api/users/profile/requests").hasAnyAuthority("USER", "MANAGER")
                .antMatchers("/api/users/profile/dialogues").hasAnyAuthority("USER", "MANAGER")
                .antMatchers("/api/users/profile/favorites").hasAuthority("USER")
                .antMatchers("/api/users/profile/children").hasAuthority("USER")
                .antMatchers("/api/files").permitAll()
                .antMatchers("/api/users/profile").hasAnyAuthority("USER", "MANAGER");


        httpSecurity.exceptionHandling()
                .defaultAuthenticationEntryPointFor(SecurityExceptionHandlers.AUTHENTICATION_ENTRY_POINT,
                        new AntPathRequestMatcher("/api/**"))
                .accessDeniedHandler(SecurityExceptionHandlers.ACCESS_DENIED_HANDLER);

        httpSecurity
                .formLogin()
                .loginProcessingUrl("/api/login")
                .successHandler(SecurityExceptionHandlers.AUTHENTICATION_SUCCESS_HANDLER)
                .failureHandler(SecurityExceptionHandlers.AUTHENTICATION_FAILURE_HANDLER);

        httpSecurity
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(SecurityExceptionHandlers.LOGOUT_SUCCESS_HANDLER);

        return httpSecurity.build();

    }

}
