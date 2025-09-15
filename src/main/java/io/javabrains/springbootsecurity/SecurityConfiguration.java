package io.javabrains.springbootsecurity;

import io.javabrains.springbootsecurity.handler.AuthEntryPoint;
import io.javabrains.springbootsecurity.handler.AuthFailureHandler;
import io.javabrains.springbootsecurity.handler.AuthSuccessHandler;
import io.javabrains.springbootsecurity.provider.FileAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    FileAuthenticationProvider fileAuthenticationProvider;


    @Autowired
    AuthSuccessHandler successHandler;

    @Autowired
    AuthFailureHandler failureHandler;

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    AuthEntryPoint basicEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(fileAuthenticationProvider);
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                //.antMatchers("/admin").hasRole("ADMIN")
               // .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .and()
                .csrf().disable()
                .sessionManagement()
                .enableSessionUrlRewriting(false)
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .sessionFixation()
                .newSession()
                .and().httpBasic()
                .authenticationEntryPoint(basicEntryPoint)
                .and().formLogin()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        List<String> matchers = new ArrayList<>(
                Arrays.asList("/health", "/api/user-locations"
                        ));
        web.ignoring()
                .antMatchers(matchers.toArray(new String[matchers.size()]));
    }
}
