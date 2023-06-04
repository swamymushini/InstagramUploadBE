package com.project.instagram.upload;

	import java.io.IOException;

public class VideoCreationExample {

    public static void main(String[] args) {
        // Set the input image path and output video path
        String imagePath = "/path/to/5259393.jpg";
        String videoPath = "/path/to/output.mp4";

        // Set the video duration per image (in seconds)
        int imageDuration = 3;

        // Set the target video width and height
        int videoWidth = 1080;
        int videoHeight = 1920;

        // Set the frame rate for the video
        int frameRate = 1;

        // Set the command to create the video
        String[] command = {
                "ffmpeg",
                "-loop", "1",
                "-i", imagePath,
                "-c:v", "libx264",
                "-t", String.valueOf(imageDuration),
                "-pix_fmt", "yuv420p",
                "-vf", "scale=" + videoWidth + ":" + videoHeight + ",setsar=1:1",
                "-r", String.valueOf(frameRate),
                "-y", videoPath
        };

        // Execute the FFmpeg command
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            process.waitFor();
            System.out.println("Video creation complete!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
