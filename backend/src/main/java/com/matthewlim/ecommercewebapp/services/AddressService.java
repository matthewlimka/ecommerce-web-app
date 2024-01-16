package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.exceptions.AddressNotFoundException;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.repositories.AddressRepository;

@Service
public class AddressService {

	Logger logger = LogManager.getLogger(AddressService.class);
	
	@Autowired
	private AddressRepository addressRepo;
	
	public Address findByAddressId(Long addressId) {
		Address address = addressRepo.findById(addressId)
				.orElseThrow(() -> {
					logger.error("No address with address ID " + addressId + " found");
					return new AddressNotFoundException("No address with address ID " + addressId + " found");
				});
		
		logger.info("Successfully found address with address ID " + addressId);
		return address;
	}
	
	public List<Address> findByStreet(String street) {
		List<Address> addresses = addressRepo.findByStreet(street);
				
		logger.info("Successfully found " + addresses.size() + " addresses with street " + street);
		return addresses;
	}
	
	public List<Address> findByCity(String city) {
		List<Address> addresses = addressRepo.findByCity(city);
				
		logger.info("Successfully found " + addresses.size() + " addresses with city " + city);
		return addresses;
	}
	
	public List<Address> findByState(String state) {
		List<Address> addresses = addressRepo.findByState(state);
				
		logger.info("Successfully found " + addresses.size() + " addresses with state " + state);
		return addresses;
	}
	
	public List<Address> findByPostalCode(String postalCode) {
		List<Address> addresses = addressRepo.findByPostalCode(postalCode);
				
		logger.info("Successfully found " + addresses.size() + " addresses with postal code " + postalCode);
		return addresses;
	}
	
	public List<Address> findByCountry(String country) {
		List<Address> addresses = addressRepo.findByCountry(country);
				
		logger.info("Successfully found " + addresses.size() + " addresses with country " + country);
		return addresses;
	}
	
	public List<Address> findAllAddresses() {
		List<Address> addressList = addressRepo.findAll();
		
		logger.info("Successfully found " + addressList.size() + " addresses");
		return addressList;
	}
	
	public Address addAddress(Address address) {
		logger.info("Successfully registered new address with address ID " + address.getAddressId());
		return addressRepo.save(address);
	}
	
	public Address updateAddress(Long addressId, Address updatedAddress) {
		Address existingAddress = addressRepo.findById(addressId)
				.orElseThrow(() -> {
					logger.error("No address with address ID " + addressId + " found");
					return new AddressNotFoundException("No address with address ID " + addressId + " found");
				});

		existingAddress.setStreet(updatedAddress.getStreet());
		existingAddress.setCity(updatedAddress.getCity());
		existingAddress.setState(updatedAddress.getState());
		existingAddress.setPostalCode(updatedAddress.getPostalCode());
		existingAddress.setCountry(updatedAddress.getCountry());
		
		logger.info("Successfully updated address with address ID " + addressId);
		return addressRepo.save(existingAddress);
	}
	
	public Address partialUpdateAddress(Long addressId, Map<String, Object> fields) {
		Address existingAddress = addressRepo.findById(addressId)
				.orElseThrow(() -> {
					logger.error("No address with address ID " + addressId + " found");
					return new AddressNotFoundException("No address with address ID " + addressId + " found");
				});
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Address.class, key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, existingAddress, value);
		});
		
		logger.info("Successfully updated address with address ID " + addressId);
		return addressRepo.save(existingAddress);
	}
	
	public void deleteAddress(Long addressId) {
		Address existingAddress = addressRepo.findById(addressId)
				.orElseThrow(() -> {
					logger.error("No user with address ID " + addressId + " found");
					return new AddressNotFoundException("No address with address ID " + addressId + " found");
				});
		
		logger.info("Successfully deleted address with address ID " + addressId);
		addressRepo.delete(existingAddress);
	}	
}
