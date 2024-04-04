package com.matthewlim.ecommercewebapp.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
@ConfigurationPropertiesBinding
public class PrivateKeyConverter implements Converter<String, RSAPrivateKey> {

	private final ResourceLoader resourceLoader;
	
	@Override
	public RSAPrivateKey convert(String privateKeyLocation) {
		try {
			String privateKeyContent = new String(resourceLoader.getResource(privateKeyLocation).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			return RsaKeyConverters.pkcs8().convert(new ByteArrayInputStream(privateKeyContent.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to load or convert private key", e);
		}
	}
}