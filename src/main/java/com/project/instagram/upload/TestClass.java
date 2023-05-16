package com.project.instagram.upload;

import java.awt.image.BufferedImage;

public class TestClass {
	public static void main(String[] args) throws Exception {
		AddTextOnJpegService obj1 = new AddTextOnJpegService();
		BufferedImage imagePath = obj1.addTextToTemplate("Truncate feelings",
				"I must be a database query because I can't stop searching for you. You're the SQL statement that completes my SELECT * FROM");
		S3UploaderService s3Upload = new S3UploaderService();
		String imageUrl = s3Upload.uploadToBucket(imagePath);
		InstaUploadService instagramMediaUpload = new InstaUploadService();
		instagramMediaUpload.uploadFile(imageUrl);
	}
}
