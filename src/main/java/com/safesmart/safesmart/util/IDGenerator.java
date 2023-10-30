package com.safesmart.safesmart.util;

import java.util.UUID;

public class IDGenerator {

	public static String generate() {
		UUID uuid = UUID.randomUUID();
		System.out.println("UUID=" + uuid.toString());
		return uuid.toString();
	}
}
