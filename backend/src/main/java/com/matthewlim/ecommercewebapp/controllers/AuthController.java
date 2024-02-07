package com.matthewlim.ecommercewebapp.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.matthewlim.ecommercewebapp.services.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	private final RestTemplate restTemplate;

	@Value("${CLIENT_ID}")
	private String clientId;
	
	@Value("${CLIENT_SECRET}")
	private String clientSecret;
	
	@Value("${REDIRECT_URI")
	private String redirectUri;
	
	@PostMapping("/login")
	public ResponseEntity<String> formLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
		System.out.println("Login request received with username " + username + " and password " + password);
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		System.out.println("Form login authenticated using UsernamePasswordAuthenticationToken - Username: " + username + ", Password: " + password);
		String jwtToken = tokenService.generateToken(authentication);
		System.out.println("JWT generated by token service: " + jwtToken);
		
		return ResponseEntity.ok(jwtToken);
	}
	
//    @GetMapping("/oauth2/token")
//    public ResponseEntity<String> exchangeAccessCodeForJwt(@RequestParam("code") String code) {
//	    System.out.println("Exchanging authorization code " + code + " for access token...");
//    	
//        // Prepare the request to exchange authorization code for access token
//        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//        requestBody.add("client_id", clientId);
//        requestBody.add("client_secret", clientSecret);
//        requestBody.add("code", code);
//        requestBody.add("redirect_uri", redirectUri);
//        requestBody.add("grant_type", "authorization_code");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        
//        // Make a POST request to the authorization server to exchange the authorization code for an access token
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://github.com/login/oauth/access_token", requestEntity, String.class);
//
//        // Extract the access token from the response
//        String accessToken = extractAccessToken(responseEntity.getBody());
//        System.out.println("Received access token " + accessToken);
//        System.out.println("Exchanging access token " + accessToken + " for JWT");
//
//        // Use the access token to authenticate and generate a JWT
//        Authentication authentication = authenticateWithAccessToken(accessToken);
//        String jwtToken = tokenService.generateToken(authentication);
//        System.out.println("JWT generated by token service: " + jwtToken);
//
//        // Return the JWT to the client
//        return ResponseEntity.ok(jwtToken);
//    }
    
    @GetMapping("/oauth2/token")
    public ResponseEntity<String> exchangeAccessTokenForJwt(ResponseEntity<String> response) {
    	String accessToken = extractAccessToken(response.getBody());
        System.out.println("Received access token " + accessToken);
        System.out.println("Exchanging access token " + accessToken + " for JWT");

        // Use the access token to authenticate and generate a JWT
        Authentication authentication = authenticateWithAccessToken(accessToken);
        String jwtToken = tokenService.generateToken(authentication);
        System.out.println("JWT generated by token service: " + jwtToken);
        
    	return ResponseEntity.ok(jwtToken);
    }
    
    private String extractAccessToken(String responseBody) {
        // Implement logic to extract the access token from the response body (e.g., using JSON parsing)
        // Example: return extractedAccessToken;
    	return responseBody;
    }

    private Authentication authenticateWithAccessToken(String accessToken) {
        // Implement logic to authenticate the user using the access token
        // Example: return authenticatedAuthentication;
    	return new BearerTokenAuthenticationToken(accessToken);
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