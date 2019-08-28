package com.appsdeveloperblog.app.ws.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	final void testGenerateUserId() {
		String userId = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);

		assertNotNull(userId);
		assertNotNull(userId2);
		assertTrue(userId.length() == 30);
		assertTrue(!userId.equalsIgnoreCase(userId2));
	}

	@Test
	// @Disabled //ignore the test
	void testHasTokenNotExpired() {
		String token = utils.generateEmailVerificationToken("g5hg656jk845gh4yh");
		assertNotNull(token);
		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
	}

	@Test
	final void testHasTokenExpired() {
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJneXM5QGdtYWlsLmNvbSIsImV4cCI6MTU2NTI3OTQzM30.jEGuamkk7bmBj6xU2PcygdeFAaKZbn-uLn0atpC8D2IlKEBsqlPjHN7ZMZK8BOpKGpzRhl8BCKy0kA8-IpSa-Q";
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		assertTrue(hasTokenExpired);
	}
}