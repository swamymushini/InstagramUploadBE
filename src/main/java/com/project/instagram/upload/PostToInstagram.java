package com.project.instagram.upload;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.project.instagram.upload.model.Post;

@Component
public class PostToInstagram implements RequestHandler<Post, String> {

	private AddTextOnJpegService addTextOnJpegService;
	private S3UploaderService s3UploaderService;
	private InstaUploadService instaUploadService;

	@Autowired
	public void setAddTextOnJpegService(AddTextOnJpegService addTextOnJpegService) {
		this.addTextOnJpegService = addTextOnJpegService;
	}

	@Autowired
	public void setS3UploaderService(S3UploaderService s3UploaderService) {
		this.s3UploaderService = s3UploaderService;
	}

	@Autowired
	public void setInstaUploadService(InstaUploadService instaUploadService) {
		this.instaUploadService = instaUploadService;
	}

	@Override
	public String handleRequest(Post input, Context context) {
		System.out.println(input.getImageText() + " " + input.getHeading());
		instantiateDependencies();
		try {

			BufferedImage imagePath = addTextOnJpegService.addTextToTemplate(input.getHeading(), input.getImageText());
			String imageUrl = s3UploaderService.uploadToBucket(imagePath);
			instaUploadService.uploadFile(imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return e.getMessage();
		}

		return "Uploaded Successfully";
	}

	private void instantiateDependencies() {
		// Manually create the dependencies here
		addTextOnJpegService = new AddTextOnJpegService();
		s3UploaderService = new S3UploaderService();
		instaUploadService = new InstaUploadService();
	}
}
