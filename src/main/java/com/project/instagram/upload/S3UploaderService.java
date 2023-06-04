package com.project.instagram.upload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3UploaderService {

	public String uploadToBucket(BufferedImage image) throws IOException {
		System.out.println("Uploading image to bucket...");
		String bucketName = "instagramuploadedposts";
		String key = "upload" + System.currentTimeMillis();

		S3Client s3Client = S3Client.builder().region(Region.US_EAST_1)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", outputStream);
		byte[] fileBytes = outputStream.toByteArray();

		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key)
				.contentType("image/jpeg").build();

		s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

		String publicUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
		System.out.println("Image uploaded to S3 successfully!");
		return publicUrl;
	}

}
