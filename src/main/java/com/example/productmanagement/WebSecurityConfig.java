package com.example.productmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/").hasAnyAuthority("USER", "EDITOR", "ADMIN")
        .antMatchers("/new").hasAnyAuthority("ADMIN")
        .antMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
        .antMatchers("/delete/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET).hasAnyAuthority("USER", "EDITOR", "ADMIN")
        .antMatchers(HttpMethod.GET, "/index.html").hasAnyAuthority("USER", "EDITOR", "ADMIN")
        .antMatchers(HttpMethod.GET, "/get/**").hasAnyAuthority("USER", "EDITOR", "ADMIN")
        .antMatchers(HttpMethod.POST).hasAnyAuthority("EDITOR", "ADMIN")
        .antMatchers(HttpMethod.PUT).hasAnyAuthority("EDITOR", "ADMIN")
        .antMatchers(HttpMethod.DELETE).hasAuthority("ADMIN")
        .anyRequest().authenticated()
        .and()
        .formLogin().permitAll()
        .and()
        .logout().permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/403");

    http.csrf().disable();
  }

}
