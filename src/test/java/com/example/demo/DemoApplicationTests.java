package com.example.demo;

import com.example.demo.user.UserController;
import com.example.demo.user.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserController user;

	@Test
	void contextLoads() {
		UserDTO dto = new UserDTO();
		dto.setName("Tayfun");
		dto.setSurname("Ilo");
		dto.setCreditLimit(200000.0);
		dto.setUsedCreditLimit(100000.0);
		user.saveUser(dto);
	}

}
