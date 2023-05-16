package com.project.instagram.upload;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class AddTextOnJpegService {

	public BufferedImage addTextToTemplate(String heading, String text) throws Exception {
		String imagePath = "test.jpg";
		ClassPathResource resource = new ClassPathResource(imagePath);
		InputStream inputStream = resource.getInputStream();
		BufferedImage image = ImageIO.read(inputStream);

		System.out.println("Creating Image");
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		String fontPath = "bellaboo.ttf";
		Font font = loadFont(fontPath, 60);

		int boxX = 105;
		int boxY = 360;
		int boxWidth = 865;
		int boxHeight = 578;

		String hashtag = "#" + heading.toLowerCase().replace(" ", "_");
		addtTextIntoBox(hashtag, graphics, font, 170, 160, 700, 80, true);

		addtTextIntoBox(text, graphics, font, boxX, boxY, boxWidth, boxHeight, false);

		setDPI(image, 1000);

		System.out.println("Text added to the image successfully!");

		return image;
	}

	private void addtTextIntoBox(String text, Graphics2D graphics, Font font, int boxX, int boxY, int boxWidth,
			int boxHeight, boolean heading) {
System.out.println();
		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		int lineHeight = fontMetrics.getHeight();

		String[] lines = splitTextIntoLines(text, fontMetrics, boxWidth);

		int totalTextHeight = lines.length * lineHeight;

		if (totalTextHeight > boxHeight) {
			throw new IllegalArgumentException("Text is too large to fit within the box.");
		}
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);

		int textY = boxY + fontMetrics.getAscent() + (!heading ? ((boxHeight - totalTextHeight) / 2) : 0);

		for (String line : lines) {
			int textX = boxX + (!heading ? ((boxWidth - fontMetrics.stringWidth(line)) / 2) : 0);
			graphics.drawString(line, textX, textY);
			textY += lineHeight;
		}
	}

	private static void setDPI(BufferedImage image, int dpi) throws Exception {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();

		ImageWriteParam writeParam = writer.getDefaultWriteParam();
		ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
		IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

		double dotsPerMilli = dpi / 25.4;
		IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
		horiz.setAttribute("value", Double.toString(dotsPerMilli));
		IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
		vert.setAttribute("value", Double.toString(dotsPerMilli));
		IIOMetadataNode dim = new IIOMetadataNode("Dimension");
		dim.appendChild(horiz);
		dim.appendChild(vert);
		IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
		root.appendChild(dim);
		metadata.mergeTree("javax_imageio_1.0", root);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
		writer.setOutput(ios);
		writer.write(metadata, new IIOImage(image, null, metadata), writeParam);

		ios.close();
		writer.dispose();
	}

	private Font loadFont(String fontPath, float size) throws Exception {
		ClassPathResource resource = new ClassPathResource(fontPath);
		InputStream inputStream = resource.getInputStream();
		Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		return font.deriveFont(size);
	}

	private String[] splitTextIntoLines(String text, FontMetrics fontMetrics, int maxWidth) {
		StringBuilder sb = new StringBuilder();
		String[] words = text.split("\\s+");
		int lineWidth = 0;
		int spaceWidth = fontMetrics.stringWidth(" ");
		java.util.List<String> lines = new java.util.ArrayList<>();

		for (String word : words) {
			int wordWidth = fontMetrics.stringWidth(word);

			if (lineWidth + wordWidth <= maxWidth) {
				sb.append(word).append(" ");
				lineWidth += wordWidth + spaceWidth;
			} else {
				lines.add(sb.toString().trim());
				sb.setLength(0);
				sb.append(word).append(" ");
				lineWidth = wordWidth + spaceWidth;
			}
		}

		if (sb.length() > 0) {
			lines.add(sb.toString().trim());
		}

		return lines.toArray(new String[0]);
	}
}
