package com.rimi.backend.notion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@RestController
public class NotionController {
    @Autowired()
    private NotionService notionService;

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${notion.apiKey}")
    private String API_KEY;

    @Value("${notion.apiUrl}")
    private String API_URL;

    // TODO: we need to decide whether to hold the payload on the server or the client.
    @GetMapping("/createNotionPage")
    public void createNotionPage() {

        String json = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Notion-Version", "2022-02-22");
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // HTTP POST 요청을 보냅니다.
        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.PATCH, requestEntity, String.class);
        System.out.println("responseEntity = " + responseEntity);

        // HTTP 응답 결과를 확인합니다.
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Notion page creation success!");
        } else {
            System.out.println("Notion page creation failure.");
        }
    }
}
