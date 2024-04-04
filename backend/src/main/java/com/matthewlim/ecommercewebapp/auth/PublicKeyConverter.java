package com.matthewlim.ecommercewebapp.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
@ConfigurationPropertiesBinding
public class PublicKeyConverter implements Converter<String, RSAPublicKey> {
	
	private final ResourceLoader resourceLoader;
	
	@Override
	public RSAPublicKey convert(String publicKeyLocation) {
        try {
        	String publicKeyContent = new String(resourceLoader.getResource(publicKeyLocation).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return RsaKeyConverters.x509().convert(new ByteArrayInputStream(publicKeyContent.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load or convert public key", e);
        }
	}
}