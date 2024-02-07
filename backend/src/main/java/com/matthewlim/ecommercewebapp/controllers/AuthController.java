package com.matthewlim.ecommercewebapp.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.matthewlim.ecommercewebapp.services.TokenService;
import com.matthewlim.ecommercewebapp.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

	Logger logger = LogManager.getLogger(UserService.class);
	
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	private final RestTemplate restTemplate;

	@Value("${CLIENT_ID}")
	private String clientId;
	
	@Value("${CLIENT_SECRET}")
	private String clientSecret;
	
	@Value("${REDIRECT_URI}")
	private String redirectUri;
	
	@PostMapping("/login")
	public ResponseEntity<String> formLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
		logger.info("Login request received with username " + username + " and password " + password);
		
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			logger.info("Form login authenticated using UsernamePasswordAuthenticationToken - Username: " + username + ", Password: " + password);
			
			String jwtToken = tokenService.generateToken(authentication);
			logger.info("JWT generated by token service: " + jwtToken);
			
			return ResponseEntity.ok(jwtToken);
		} catch (BadCredentialsException ex) {
			logger.error("Invalid credentials for user " + username);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}	
	}
	
    @GetMapping("/oauth2/token")
    public ResponseEntity<String> exchangeAccessCodeForJwt(@RequestParam("code") String code) {
	    logger.info("Exchanging authorization code " + code + " for access token...");
    	
	    // Prepare the request URL with query parameters
        String tokenUrl = "https://github.com/login/oauth/access_token";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("code", code)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("grant_type", "authorization_code");

        // Make a POST request to the authorization server to exchange the authorization code for an access token
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.toUriString(), null, String.class);
        logger.info("Making POST request to " + builder.toUriString());

        // Extract the access token from the response
        String accessToken = responseEntity.getBody();
        logger.info("Received access token " + accessToken);

        // Use the access token to authenticate and generate a JWT
        logger.info("Exchanging access token for JWT");
        Authentication authentication = new BearerTokenAuthenticationToken(accessToken);
        String jwtToken = tokenService.generateToken(authentication);
        logger.info("JWT generated by token service: " + jwtToken);

        // Return the JWT to the client
        return ResponseEntity.ok(jwtToken);
    }
}