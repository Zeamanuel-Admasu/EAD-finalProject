package com.restaurant.table_reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.restaurant.table_reservation.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;

  public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
    this.customUserDetailsService = customUserDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(authenticationProvider())
        .build();
  }

  @Bean
  public CustomUsernamePasswordAuthenticationFilter customFilter(AuthenticationManager authenticationManager) {
    CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter();
    customFilter.setAuthenticationManager(authenticationManager); // Set the AuthenticationManager
    return customFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/", "/home", "/css/", "/js/","/images/", "/signup", "/login/admin", "/login/user")
        .permitAll().requestMatchers("/admin/").hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER")
        .requestMatchers("/reservation", "/payment", "/confirmation").hasAnyRole("USER", "ADMIN").anyRequest()
        .authenticated())
            .formLogin(form -> form.loginPage("/login/user") // Default login page (will redirect
                                          // if admin)
            .loginProcessingUrl("/perform_login") // Single login processing URL
            .successHandler(customAuthenticationSuccessHandler()) // Handles both user/admin redirection
            .failureUrl("/login/user?error=true") // Show errors on the user login page by default
            .permitAll())
        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
            .deleteCookies("JSESSIONID").permitAll());

    return http.build();
  }

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return (request, response, authentication) -> {
      // Get the roles of the authenticated user
      boolean isAdmin = authentication.getAuthorities().stream()
          .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

      String redirectUrl = isAdmin ? "/admin/dashboard" : "/user/dashboard";
      response.sendRedirect(redirectUrl);
    };
  }

