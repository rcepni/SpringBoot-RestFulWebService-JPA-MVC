package com.appsdeveloperblog.app.ws.io.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	static boolean recordsCreated=false;
	@BeforeEach
	void setUp() throws Exception {
		if(!recordsCreated)
		createRecords();

	}

@Test
void testGetVerifiedUsers() {
Pageable pageableRequest=PageRequest.of(1, 1);
Page<UserEntity> page=	userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
assertNotNull(page);
		
List<UserEntity> userEntities=page.getContent();
assertNotNull(userEntities);
//assertTrue(userEntities.size()==1);
	}

	@Test
	final void testFindUserByFirstName() {
		String firstName="Adam";
	List<UserEntity> users=	userRepository.findUserByFirstName(firstName);
	assertNotNull(users);
	assertTrue(users.size()==2);
	
	UserEntity user=users.get(0);
	assertTrue(user.getFirstName().equals(firstName));
	}
	

	@Test
	final void testFindUserByLastName() {
		String lastName="Abraham";
	List<UserEntity> users=	userRepository.findUserByLastName(lastName);
	assertNotNull(users);
	assertTrue(users.size()==2);
	
	UserEntity user=users.get(0);
	assertTrue(user.getLastName().equals(lastName));
	}
	
	@Test
	final void testfindUsersByKeyword() {
		String keyword="Abr";
	List<UserEntity> users=	userRepository.findUsersByKeyword(keyword);
	assertNotNull(users);
	assertTrue(users.size()==2);
	
	UserEntity user=users.get(0);
	assertTrue(user.getLastName().contains(keyword) ||
			   user.getFirstName().contains(keyword));
	}
	@Test
	final void testFindUserFirstNameAndLastNameByKeyword() {
		String keyword="Abr";
		List<Object[]> users=	userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size()==2);
		
		Object[] user=users.get(0);
		String userFirstName=String.valueOf(user[0]);
		String userLastName=String.valueOf(user[1]);
		assertNotNull(userFirstName);
		assertNotNull(userLastName);
		System.out.println("\nFirstName ="+userFirstName);
		System.out.println("\nLastName ="+userLastName+"\n");
	}
	
	@Test
	final void  updateUserEmailVerificationStatus() {
		
boolean newEmailVerificationStatus=false;
userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1s43fik");	
UserEntity storedUserDetails=userRepository.findByuserId("1s43fik");
boolean storedEmailVerificationStatus= storedUserDetails.getEmailVerificationStatus();
assertTrue(storedEmailVerificationStatus==newEmailVerificationStatus);

	}
	
	private void  createRecords() {
		UserEntity	userEntity=new UserEntity();
		userEntity.setFirstName("Adam");
		userEntity.setLastName("Abraham");
		userEntity.setUserId("1s43fik");
		userEntity.setEncryptedPassword("xxx");
		userEntity.setEmail("hwy@gmail.com");
		userEntity.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity=new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("tu6ucxle");
		addressEntity.setCity("vancouver");
		addressEntity.setCountry("Canada");
		addressEntity.setPostalCode("AEDFG");
		addressEntity.setStreetName("123 street address");
		
		List<AddressEntity> addresses=new ArrayList<>();
		addresses.add(addressEntity);
		userEntity.setAddresses(addresses);
		
		userRepository.save(userEntity);
		
		UserEntity	userEntity2=new UserEntity();
		userEntity2.setFirstName("Adam");
		userEntity2.setLastName("Abraham");
		userEntity2.setUserId("1s43fityb6k");
		userEntity2.setEncryptedPassword("xxx");
		userEntity2.setEmail("hwy@gmail.com");
		userEntity2.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity2=new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("tu6ucxut5le");
		addressEntity2.setCity("vancouver");
		addressEntity2.setCountry("Canada");
		addressEntity2.setPostalCode("AEDFG");
		addressEntity2.setStreetName("123 street address");
		
		List<AddressEntity> addresses2=new ArrayList<>();
		addresses2.add(addressEntity2);
		userEntity2.setAddresses(addresses2);
		
		userRepository.save(userEntity2);
		
		recordsCreated=true;
	}
	
}
