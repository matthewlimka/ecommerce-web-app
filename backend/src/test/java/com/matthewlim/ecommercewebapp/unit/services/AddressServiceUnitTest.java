package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.AddressRepository;
import com.matthewlim.ecommercewebapp.services.AddressService;

@ExtendWith(MockitoExtension.class)
public class AddressServiceUnitTest {

	@Mock
	private AddressRepository addressRepository;
	
	@InjectMocks
	private AddressService addressService;
	
	@Test
	public void testFindAllAddresses() {
		List<Address> addressList = new ArrayList<Address>(Arrays.asList(new Address(), new Address()));		
		
		when(addressRepository.findAll()).thenReturn(addressList);
		List<Address> result = addressService.findAllAddresses();
		
		assertNotNull(result);
		assertThat(result).hasSize(addressList.size()).containsExactlyElementsOf(addressList);
	}
	
	@Test
	public void testFindByAddressId() {
		Long addressId = 1L;
		Address address = new Address();
		address.setAddressId(addressId);
		
		when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
		Address result = addressService.findByAddressId(addressId);
		
		assertNotNull(result);
		assertEquals(addressId, result.getAddressId());
	}
	
	@Test
	public void testFindByStreet() {
		String street = "street";
		Address address = new Address();
		address.setStreet(street);
		List<Address> addressList = new ArrayList<Address>(Arrays.asList(address));
		
		when(addressRepository.findByStreet(street)).thenReturn(addressList);
		List<Address> result = addressService.findByStreet(street);
		
		assertNotNull(result);
		assertThat(result).hasSize(addressList.size()).containsExactlyElementsOf(addressList);
	}
	
	@Test
	public void testFindByCity() {
		String city = "city";
		Address address = new Address();
		address.setCity(city);
		List<Address> addressList = new ArrayList<Address>(Arrays.asList(address));
		
		when(addressRepository.findByCity(city)).thenReturn(addressList);
		List<Address> result = addressService.findByCity(city);
		
		assertNotNull(result);
		assertThat(result).hasSize(addressList.size()).containsExactlyElementsOf(addressList);
	}
	
	@Test
	public void testFindByState() {
		String state = "state";
		Address address = new Address();
		address.setState(state);
		List<Address> addressList = new ArrayList<Address>(Arrays.asList(address));
		
		when(addressRepository.findByState(state)).thenReturn(addressList);
		List<Address> result = addressService.findByState(state);
		
		assertNotNull(result);
		assertThat(result).hasSize(addressList.size()).containsExactlyElementsOf(addressList);
	}
	
	@Test
	public void testFindByPostalCode() {
		String postalCode = "postalCode";
		Address address = new Address();
		address.setPostalCode(postalCode);
		List<Address> addressList = new ArrayList<Address>(Arrays.asList(address));
		
		when(addressRepository.findByPostalCode(postalCode)).thenReturn(addressList);
		List<Address> result = addressService.findByPostalCode(postalCode);
		
		assertNotNull(result);
		assertThat(result).hasSize(addressList.size()).containsExactlyElementsOf(addressList);
	}
	
	@Test
	public void testFindByCountry() {
		String country = "country";
		Address address = new Address();
		address.setCountry(country);
		List<Address> addressList = new ArrayList<Address>(Arrays.asList(address));
		
		when(addressRepository.findByCountry(country)).thenReturn(addressList);
		List<Address> result = addressService.findByCountry(country);
		
		assertNotNull(result);
		assertThat(result).hasSize(addressList.size()).containsExactlyElementsOf(addressList);
	}
	
	@Test
	public void testFindByUser() {
		User user = new User();
		Address address = new Address();
		address.setUser(user);
		
		when(addressRepository.findByUser(user)).thenReturn(Optional.of(address));
		Address result = addressService.findByUser(user);
		
		assertNotNull(result);
		assertEquals(user, result.getUser());
	}
	
	@Test
	public void testAddAddress() {
		Address address = new Address();
		
		when(addressRepository.save(address)).thenReturn(address);
		Address result = addressService.addAddress(address);
		
		assertNotNull(result);
		verify(addressRepository, times(1)).save(address);
	}
	
	@Test
	public void testUpdateAddress() {
		Long addressId = 1L;
		Address existingAddress = new Address();
		existingAddress.setAddressId(addressId);
		
		Address updatedAddress = new Address();
		updatedAddress.setStreet("updatedStreet");
		
		when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
		when(addressRepository.save(existingAddress)).thenReturn(updatedAddress);
		Address result = addressService.updateAddress(addressId, updatedAddress);
		
		assertNotNull(result);
		assertEquals(updatedAddress.getStreet(), result.getStreet());
	}
	
	@Test
	public void testPartialUpdateAddress() {
		Long addressId = 1L;
		Address existingAddress = new Address();
		existingAddress.setAddressId(addressId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("street", "Hollywood Street");
		fieldsToUpdate.put("city", "Los Angeles");
		
		when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
		when(addressRepository.save(existingAddress)).thenReturn(existingAddress);
		Address result = addressService.partialUpdateAddress(addressId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("street"), result.getStreet());
		assertEquals(fieldsToUpdate.get("city"), result.getCity());
	}
	
	@Test
	public void testDeleteAddress() {
		Long addressId = 1L;
		Address address = new Address();
		address.setAddressId(addressId);
		
		when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
		addressService.deleteAddress(addressId);
		
		verify(addressRepository, times(1)).delete(address);
	}
}