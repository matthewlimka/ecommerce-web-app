package com.matthewlim.ecommercewebapp.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.matthewlim.ecommercewebapp.services.UserService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserService userService;
	private final RsaKeyProperties rsaKeyProperties;
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		return new InMemoryUserDetailsManager(User
				.withUsername("user")
				.password(passwordEncoder().encode("password"))
				.roles("USER")
				.build());
	}	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder
				.withPublicKey(rsaKeyProperties.publicKey())
				.build();
	}
	
	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey
				.Builder(rsaKeyProperties.publicKey())
				.privateKey(rsaKeyProperties.privateKey())
				.build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        		.csrf(csrf -> csrf.disable())
        		.authorizeHttpRequests(authorize -> authorize
        			.requestMatchers("/", "/error", "/login", "/oauth2/token").permitAll()
        			.requestMatchers("/api/v1/**").authenticated()
        			.anyRequest().authenticated()
        		)
        		.oauth2Login(withDefaults())
        		.formLogin(form -> form.disable())
        		.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint()))
        		.oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
        		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder
			.authenticationProvider(daoAuthenticationProvider())
			.authenticationProvider(jwtAuthenticationProvider());
		
		return authenticationManagerBuilder.build();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;
	}
	
	@Bean
	public JwtAuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider(jwtDecoder());
	}
	
	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                	.allowedOrigins("http://localhost:3000")
                	.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                	.allowCredentials(true);
            
                registry.addMapping("/login")
                	.allowedOrigins("http://localhost:3000")
                	.allowedMethods("POST")
                	.allowCredentials(true);
                
                registry.addMapping("/oauth2/token")
                	.allowedOrigins("http://localhost:3000")
                	.allowedMethods("GET", "OPTIONS")
                	.allowCredentials(true);
                
                registry.addMapping("/")
            		.allowedOrigins("http://localhost:3000")
            		.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
            		.allowCredentials(true);
            }
        };
    }
}