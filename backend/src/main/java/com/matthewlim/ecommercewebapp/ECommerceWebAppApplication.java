package com.matthewlim.ecommercewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.matthewlim.ecommercewebapp.auth.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ECommerceWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceWebAppApplication.class, args);
	}

}