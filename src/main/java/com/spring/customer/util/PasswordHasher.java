package com.spring.customer.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

	public static String generateHash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	public static void main(String[] args) {
		System.out.println(generateHash("ashima"));
	}

}
