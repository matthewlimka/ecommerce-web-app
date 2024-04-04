package com.matthewlim.ecommercewebapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class AppConfig {

	@Autowired
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	@Bean
	public PublicKeyConverter publicKeyConverter() {
		return new PublicKeyConverter(resourceLoader);
	}
	
	@Bean
	public PrivateKeyConverter privateKeyConverter() {
		return new PrivateKeyConverter(resourceLoader);
	}
}