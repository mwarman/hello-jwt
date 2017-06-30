package com.leanstacks.hellojwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leanstacks.hellojwt.security.JWTAuthenticationFilter;
import com.leanstacks.hellojwt.security.JWTLoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //@formatter:off
        
        http
          .csrf().disable()
          
          .authorizeRequests()
          
              .antMatchers("/").permitAll()
              .antMatchers(HttpMethod.POST, "/login").permitAll()
              .anyRequest().authenticated()
              
              .and()
              
              // Filter Requests to Authentication Endpoint
              .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
                      UsernamePasswordAuthenticationFilter.class)
              
              // Filter All Other Requests to Verify Authentication
              .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        //@formatter:on

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // Create a simple Authentication Manager with an In-Memory source
        auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");

    }

}
