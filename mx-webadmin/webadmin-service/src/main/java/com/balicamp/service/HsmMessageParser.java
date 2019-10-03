package com.balicamp.service;

import java.util.HashMap;

public interface HsmMessageParser {

	public String parse(String message, boolean html, boolean isRequest);

	public HashMap parseToHashMap(String message, boolean isRequest);
}