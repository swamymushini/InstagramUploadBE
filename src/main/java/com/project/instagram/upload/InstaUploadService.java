package com.project.instagram.upload;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstaUploadService {

	String apiUrl = "https://graph.facebook.com/v16.0/17841459115117239/media";

	public void uploadFile(String imageUrl) throws Exception {

		String accessToken = "EAAWwCHwDz0UBABB1G5eFbRjjHyJHOVP9qjPjO6mGLaEpam6nHTx1ZBZCtGNPIfSSvvJn9SP3uA7ZA2QJJXZCQsqmZBSt6wE8rw22mdbyVAZBqryu1P1TY54f0MynLwteusPC3FHcMdW9ZCGE5aCkcuBvZCT1E6bZAXpwEBTRZCQOWDy72uCUXPb8El";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("image_url", imageUrl);
		requestBody.add("access_token", accessToken);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

		String id = null;
		if (response.getStatusCode().is2xxSuccessful()) {
			String responseBody = response.getBody();
			id = responseBody.substring(responseBody.indexOf("\"id\":") + 6, responseBody.indexOf("}"));
			System.out.println("ID: " + id);
		} else {
			System.out.println("Media upload failed. Error response: " + response.getBody());
			throw new Exception("Failed to Post the image in container" + response.getBody());
		}

		apiUrl = "https://graph.facebook.com/v16.0/17841459115117239/media_publish";
		requestBody = new LinkedMultiValueMap<>();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getBody());
		String id2 = jsonNode.get("id").asText();

		requestBody.add("creation_id", id2);
		requestBody.add("access_token", accessToken);

		requestEntity = new HttpEntity<>(requestBody, headers);
		response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
		String responseText = response.getBody();

		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Response: " + responseText);
		} else {
			throw new Exception("Failed to Post the container " + responseText);
		}

	}
}
