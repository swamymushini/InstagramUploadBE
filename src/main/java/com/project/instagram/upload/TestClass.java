package com.project.instagram.upload;

import java.awt.image.BufferedImage;

public class TestClass {
	public static void main(String[] args) throws Exception {
		AddTextOnJpegService obj1 = new AddTextOnJpegService();
		BufferedImage imagePath = obj1.addTextToTemplate("Test captions",
				"Test captions");
		S3UploaderService s3Upload = new S3UploaderService();
		String imageUrl = s3Upload.uploadToBucket(imagePath);
		InstaUploadService instagramMediaUpload = new InstaUploadService();
		instagramMediaUpload.uploadFile(imageUrl);
	}
}
