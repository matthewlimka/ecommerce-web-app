package com.matthewlim.ecommercewebapp.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matthewlim.ecommercewebapp.exceptions.AddressNotFoundException;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.AddressService;

@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@GetMapping
	public List<Address> getAddresses(@RequestParam(required = false) String streetAddress, @RequestParam(required = false) String city, @RequestParam(required = false) String state, @RequestParam(required = false) String postalCode, @RequestParam(required = false) String country, @RequestParam(required = false) User user) {
		if ( streetAddress != null ) {
			return addressService.findByStreetAddress(streetAddress);
		} else if ( city != null ) {
			return addressService.findByCity(city);
		} else if ( state != null ) {
			return addressService.findByState(state);
		} else if ( postalCode != null ) {
			return addressService.findByPostalCode(postalCode);
		} else if ( country != null ) {
			return addressService.findByCountry(country);
		} else if ( user != null ) {
			return new ArrayList<Address>(Arrays.asList(addressService.findByUser(user)));
		} else {
			return addressService.findAllAddresses();	
		}
	}

	@GetMapping("/{addressId}")
	public Address getAddress(@PathVariable Long addressId) throws AddressNotFoundException {
		return addressService.findByAddressId(addressId);
	}
	
	@PostMapping
	public ResponseEntity<Address> createAddress(@RequestBody Address address) {
		Address savedAddress = addressService.addAddress(address);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{addressId}")
				.buildAndExpand(address.getAddressId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedAddress);
	}
	
	@PutMapping("/{addressId}")
	public Address updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
		return addressService.updateAddress(addressId, updatedAddress);
	}
	
	@PatchMapping("/{addressId}")
	public Address partialUpdateAddress(@PathVariable Long addressId, @RequestBody Map<String, Object> fields) {
		return addressService.partialUpdateAddress(addressId, fields);
	}
	
	@DeleteMapping("/{addressId}")
	public void deleteAddress(@PathVariable Long addressId) {
		addressService.deleteAddress(addressId);
	}
}
