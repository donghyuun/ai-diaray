package com.example.springbackend.Service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ImageGeneratorService {

    @Value("${kakao-app-key}")
    private String kakao_app_key;
    private final RestTemplate restTemplate;
    public ImageGeneratorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @ResponseBody
    public void getAiImage(String content) throws Exception {
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
    }
}
