package com.restaurant.table_reservation.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      boolean isAdminPage = Boolean.parseBoolean(request.getParameter("isAdminPage"));

      // Authenticate the user
      Authentication authentication = this.getAuthenticationManager()
              .authenticate(new UsernamePasswordAuthenticationToken(username, password));

      // Check role against the page context
      boolean isAdmin = authentication.getAuthorities().stream()
              .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

      // Enforce role-based context
      if (isAdminPage && !isAdmin) {
          throw new AuthenticationException("Access denied: Admin credentials required") {};
      } else if (!isAdminPage && isAdmin) {
          throw new AuthenticationException("Access denied: User credentials required") {};
      }

      return authentication;
  }



}




