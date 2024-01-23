package com.matthewlim.ecommercewebapp.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matthewlim.ecommercewebapp.auth.LoginRequest;
import com.matthewlim.ecommercewebapp.services.TokenService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class AuthController {

	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<String> formLogin(@RequestBody LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
		String token = tokenService.generateToken(authentication);
		
		return ResponseEntity.ok(token);
	}
	
	@GetMapping("/login/oauth2")
	public ResponseEntity<String> oauth2Login(Principal principal) {
//		Authentication authentication = authenticationManager.authenticate(new OAuth2AuthenticationToken());
//		String token = tokenService.generateToken(authentication);
		
//		return ResponseEntity.ok(token);
		return null;
	}
	
	@GetMapping("/home")
	public String homePage(Principal principal) {
		if (principal != null) {
			return "Welcome home " + principal.getName() + "!";
		} else {
			return "Welcome home stranger!";
		}
	}

	@GetMapping("/")
	public String defaultHome() {
		return "This is the default home page";
	}
	
	@GetMapping("/secured")
	public String secureHome() {
		return "This is the secured home page";
	}
}