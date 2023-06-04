package com.project.instagram.upload.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestClass {

	public static void main(String[] args) throws Exception {
		String json = "{\r\n" + "\"fulfillment\" : {\r\n" + "\"notify_customer\" : \"true\",\r\n"
				+ "\"location_id\" : \"9120023730003\",\r\n"
				+ "\"tracking_urls\" : [ \"https:\\\\/\\\\/shipping.xyz\\\\/track.php?num=123456789\" ],\r\n"
				+ "\"tracking_company\" : \"Shipping Company Name\",\r\n"
				+ "\"tracking_numbers\" : [ \"123456789\" ]\r\n" + "}\r\n" + "}";
		ObjectMapper objectMapper = new ObjectMapper();
		String writeValueAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		System.out.println(writeValueAsString);
	}
}
