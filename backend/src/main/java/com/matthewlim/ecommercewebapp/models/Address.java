package com.matthewlim.ecommercewebapp.models;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	
	private String streetAddress;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public Address(String streetAddress, String city, String state, String postalCode, String country, User user) {
		super();
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.user = user;
	}
}