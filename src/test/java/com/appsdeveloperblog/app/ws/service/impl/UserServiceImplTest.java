package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.AmazonSES;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	AmazonSES amazonSES;
	
String userId="23dfsdffgghgsdgdg";
String encryptedPassword="74hsffsfbdfljd";
UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Adam");
		userEntity.setLastName("LastName");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("fkf7r7ydhd65wfg");
		userEntity.setAddresses(getAddressesEntity());
	
	}

	@Test
	final void testGetUser() {
	
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");
		assertNotNull(userDto);
		assertEquals("Adam", userDto.getFirstName());
	}

	@Test
	final void testGetUser_UsernameNotFoundException() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, ()-> {
			userService.getUser("test@test.com");
		}
		);
	}
	@Test
	final void testCreateUser_CreateUserServiceException() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDto userDto=new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Adam");
		userDto.setLastName("LastName");
		userDto.setPassword("234560");
		userDto.setEmail("test@test.com");
		
		assertThrows(UserServiceException.class, ()-> {
			userService.createUser(userDto);
		}
		);
	}
	@Test
final void 	testCreateUserMethod() {
    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(utils.generateAddressId(anyInt())).thenReturn("fgkh688fghhhkj");	
    when(utils.generateUserId(anyInt())).thenReturn(userId);
	when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
	when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
	Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
	
//	AddressDTO addressDto=new 	AddressDTO();
//	addressDto.setType("shipping");
//	addressDto.setCity("McLean");
//	addressDto.setCountry("US");;
//	addressDto.setPostalCode("1232435");
//	addressDto.setStreetName("123 Some street name");
//	
//	AddressDTO billingAddressDto=new 	AddressDTO();
//	billingAddressDto.setType("billing");
//	billingAddressDto.setCity("McLean");
//	billingAddressDto.setCountry("US");;
//	billingAddressDto.setPostalCode("123875");
//	billingAddressDto.setStreetName("1Some street name");
//	
//	List<AddressDTO> addresses=new ArrayList<>();
//	addresses.add(addressDto);
//	addresses.add(billingAddressDto);
//	
	UserDto userDto=new UserDto();
	userDto.setAddresses(getAddressesDto());
	userDto.setFirstName("Adam");
	userDto.setLastName("LastName");
	userDto.setPassword("234560");
	userDto.setEmail("test@test.com");
	
	UserDto storedUserDetails=userService.createUser(userDto);
	
	assertNotNull(storedUserDetails);
	assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
	assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
	assertEquals(userEntity.getEmail(), storedUserDetails.getEmail());
	assertNotNull(storedUserDetails.getUserId());
	assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
	verify(utils,times(2)).generateAddressId(30);
	verify(bCryptPasswordEncoder,times(1)).encode("234560");
	verify(userRepository,times(1)).save(any(UserEntity.class));
	
	}
		private List<AddressDTO> getAddressesDto(){
		AddressDTO addressDto=new 	AddressDTO();
		addressDto.setType("shipping");
		addressDto.setCity("McLean");
		addressDto.setCountry("US");;
		addressDto.setPostalCode("1232435");
		addressDto.setStreetName("123 Some street name");
		
		AddressDTO billingAddressDto=new 	AddressDTO();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("McLean");
		billingAddressDto.setCountry("US");;
		billingAddressDto.setPostalCode("123875");
		billingAddressDto.setStreetName("1Some street name");
		
		List<AddressDTO> addresses=new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);
		return addresses;
	}
	private List<AddressEntity> getAddressesEntity(){
		List<AddressDTO> addresses=getAddressesDto();
		
		
		Type listType=new TypeToken<List<AddressEntity>>() {}.getType();
		return new ModelMapper().map(addresses, listType);
	}
	
	
}