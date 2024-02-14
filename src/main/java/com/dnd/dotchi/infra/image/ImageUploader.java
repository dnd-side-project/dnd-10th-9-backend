package com.dnd.dotchi.infra.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUploader {

	public static final String SYSTEM_PATH = System.getProperty("user.dir");
	public static final String SLASH = File.separator;

	private final String url;
	private final String uploadDirPath;

	public ImageUploader(
		@Value("${image.upload_url}") final String url,
		@Value("${image.upload_directory}") final String uploadDirPath
	) {
		this.url = url;
		this.uploadDirPath = uploadDirPath;
	}

	public String upload(MultipartFile image) {

		String fileSavePath = SYSTEM_PATH + SLASH + uploadDirPath;
		makeDirectory(fileSavePath);

		String saveFileName = parseSaveFileName(image);
		File uploadPath = new File(fileSavePath, saveFileName);

		transferFile(image, uploadPath);
		return url + File.separator + uploadDirPath + File.separator + saveFileName;
	}

	private void makeDirectory(String fileSavePath) {
		File directory = new File(fileSavePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private String parseSaveFileName(MultipartFile image) {
		String imageName = image.getOriginalFilename();
		String extension = StringUtils.getFilenameExtension(imageName);
		String fileBaseName = UUID.randomUUID().toString().substring(0, 8);

		return fileBaseName + "_" + System.currentTimeMillis() + "." + extension;
	}

	private void transferFile(MultipartFile image, File uploadPath) {
		try {
			image.transferTo(uploadPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
