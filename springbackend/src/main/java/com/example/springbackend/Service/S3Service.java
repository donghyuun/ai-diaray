package com.example.springbackend.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3Client;
    public S3Service(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }
    public String uploadImageFromUrlToS3(String imageUrl) throws IOException {
        System.out.println("in S3Service, imageUrl: " + imageUrl);
        // 이미지 URL에서 이미지 다운로드
        InputStream inputStream = new URL(imageUrl).openStream();
        // S3에 저장할 고유한 키 생성(파일명으로 사용)
        String key = UUID.randomUUID().toString();
        // 이미지 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/webp");//karlo 가 반환하는 이미지는 webp 형식임
        // S3에 파일 업로드
        amazonS3Client.putObject("dall-e-bucket", key, inputStream, metadata);

        // S3에 파일 업로드된 객체의 URL을 가져온다
        String objectUrl = amazonS3Client.getUrl("dall-e-bucket", key).toString();
        System.out.println("Object URL from S3: " + objectUrl);
        return objectUrl;
    }
}
