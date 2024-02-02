package com.matthewlim.ecommercewebapp.models;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matthewlim.ecommercewebapp.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(unique = true)
	private String username;

	private String password;

	@Column(unique = true)
	private String email;

	private String firstName;
	private String lastName;

	@Enumerated(EnumType.STRING)
	private Role role;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders;

	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Address address;

	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Cart cart;

	public User(String username, String password, String email, String firstName, String lastName, Role role, List<Order> orders,
			Address address, Cart cart) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.orders = orders;
		this.address = address;
		this.cart = cart;
	}
}
