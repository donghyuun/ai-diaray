package com.example.springbackend.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ImageGeneratorService {

    @Value("${kakao-app-key}")
    private String kakao_app_key;
    private final RestTemplate restTemplate;
    private final S3Service s3Service;
    public ImageGeneratorService(RestTemplate restTemplate, S3Service s3Service) {
        this.restTemplate = restTemplate;
        this.s3Service = s3Service;
    }

    public String getAiImageUrl(String content) throws Exception {
        String imgUrl = "";

        // Request 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", kakao_app_key);
        System.out.println("headers: " + headers);
        // Request 바디 설정
        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", "draw this with oil paint style: " + content);
        requestBody.put("negative_prompt", "ugly face, cropped, scary, sordid");
        String serializedRequestBody = requestBody.toString(); //생성한 객체를 직렬화함
        // Request Entity 설정
        HttpEntity entity = new HttpEntity(serializedRequestBody, headers);
        System.out.println("entity: " + entity);

        // API 호출
        String url = "https://api.kakaobrain.com/v2/inference/karlo/t2i";
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Response Body 출력
        System.out.println("getAiImage(): " + response.getBody());
        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree((JsonParser) response.getBody());
        String resString = response.getBody().toString();

        // images 배열에서 이미지 url 추출
        JsonNode rootNode = mapper.readTree(resString);
        JsonNode imagesNode = rootNode.get("images");
        if (imagesNode.isArray() && imagesNode.size() > 0) {
            JsonNode firstImageNode = imagesNode.get(0);
            JsonNode imageUrlNode = firstImageNode.get("image");
            if (imageUrlNode != null) {
                imgUrl = imageUrlNode.asText();
            }
        }
        return imgUrl;
    }
}
