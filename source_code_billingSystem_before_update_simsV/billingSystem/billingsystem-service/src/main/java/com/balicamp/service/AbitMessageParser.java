package com.balicamp.service;

import java.util.HashMap;

public interface AbitMessageParser {

	public String parse(String message, boolean html);

	public HashMap<String, String> parseToHashMap(String message);
}