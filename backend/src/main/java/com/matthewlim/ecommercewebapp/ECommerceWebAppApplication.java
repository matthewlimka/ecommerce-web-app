package com.matthewlim.ecommercewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.matthewlim.ecommercewebapp.auth.AppConfig;
import com.matthewlim.ecommercewebapp.auth.RsaKeyProperties;

@SpringBootApplication
@Import(AppConfig.class)
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ECommerceWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceWebAppApplication.class, args);
	}
}