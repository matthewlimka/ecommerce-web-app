package com.matthewlim.ecommercewebapp.auth;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.matthewlim.ecommercewebapp.exceptions.UserNotFoundException;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                Jwt jwtToken = jwtDecoder.decode(jwt);
                String subject = jwtToken.getSubject();
                System.out.println("JWT subject: " + subject);
                
                // Create an Authentication object and set it in SecurityContext
                Authentication authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT");
                return;
            }
        } else if (isLoginRequest(request)) {
        	if (isAuthenticationRequestValid(request, response)) {
        		filterChain.doFilter(request, response);
        	}
        	return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isLoginRequest(HttpServletRequest request) {
    	return "/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod());
    }
    
    private boolean isAuthenticationRequestValid(HttpServletRequest request, HttpServletResponse response) {
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	return isValid(username, password, response);
    }
    
    private boolean isValid(String username, String password, HttpServletResponse response) {
    	try {
    		User user = userService.findByUsername(username);
    		
    		if (user != null) {
    			String validPassword = user.getPassword();
    			
    			if (passwordEncoder.matches(password, validPassword)) {
    				return true;
    			} else {
    				logger.info("Invalid password for user: " + username);
    				try {
    					response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid password");    					
    				} catch (IOException ioException) {
    		    		logger.error(ioException);
    		    	}
    				return false;
    			}
    		} else {

    		}
    	} catch (UserNotFoundException userNotFoundException) {
    		logger.error(userNotFoundException);
    		logger.info("Invalid username: " + username);
    		try {
    			response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid username");    			
    		} catch (IOException ioException) {
    			logger.error(ioException);
    		}
			return false;
    	} 
    	return false;
    }
}