package com.dnd.dotchi.infra.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Component
public class S3FileUploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) {
        final String saveFileName = parseSaveFileName(multipartFile);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, saveFileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return amazonS3.getUrl(bucket, saveFileName).toString();
    }

    private String parseSaveFileName(final MultipartFile image) {
        final String imageName = image.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(imageName);
        final String fileBaseName = UUID.randomUUID().toString().substring(0, 8);

        return fileBaseName + "_" + System.currentTimeMillis() + "." + extension;
    }

}
