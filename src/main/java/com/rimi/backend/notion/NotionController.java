package com.rimi.backend.notion;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.repository.QandARepository;
import com.rimi.backend.global.gpt.service.CreateAssistantService;
import com.rimi.backend.global.gpt.service.GetSystemService;
import com.rimi.backend.global.request.CreateNotionRequest;
import com.rimi.backend.global.request.GetNotionRequest;
import com.rimi.backend.global.response.CreateNotionResponse;
import com.rimi.backend.global.response.GetNotionResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@CrossOrigin
@RestController
@RequestMapping("/api")

public class NotionController {
    @Autowired()
    private NotionService notionService;

    @Autowired()
    private GetSystemService systemService;

    @Autowired()
    private CreateAssistantService createAssistantService;

    @Autowired
    private QandARepository qandARepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${notion.apiKey}")
    private String API_KEY;

    @Value("${notion.apiUrl}")
    private String API_URL;

    // TODO: we need to decide whether to hold the payload on the server or the
    // client.
    @PostMapping("/createNotionPage")
    public ResponseEntity<CreateNotionResponse> createNotionPage(@RequestBody CreateNotionRequest req) {
        try {
            String promptBase = systemService.getSystemContent("getNotionInput");

            List<QandA> qandAList = qandARepository.findAll();
            String user = "";
            String assistant = "";

            for (QandA qandA : qandAList) {
                user += "question " + qandA.getQandAid() + ": " + qandA.getQuestion() + "\nanswer: " + qandA.getAnswer()
                        + "\n";
                assistant += "question " + qandA.getQandAid() + ": " + qandA.getQuestion() + "\nadvice to the user: "
                        + qandA.getAdvice() + "\n";
            }

            String gptResponse = createAssistantService.createAssistantBlock(promptBase, user, assistant);

            String json = notionService.buildPayload(req, gptResponse);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Notion-Version", "2022-02-22");
            headers.set("Authorization", "Bearer " + API_KEY);

            HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            // HTTP POST 요청을 보냅니다.
            ResponseEntity<String> notionResponseEntity = restTemplate.exchange(API_URL, HttpMethod.POST,
                    requestEntity,
                    String.class);
            System.out.println("notionResponseEntity = " + notionResponseEntity);
            notionResponseEntity.getBody();

            // HTTP 응답 결과를 확인합니다.
            if (notionResponseEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("Notion page creation success!");
                return ResponseEntity.ok(CreateNotionResponse.create(notionResponseEntity.getBody()));
            } else {
                System.out.println("Notion page creation failure on API request.");
                return ResponseEntity.internalServerError().body(CreateNotionResponse.create());
            }
        } catch (Exception e) {
            System.out.println("Notion page creation failure.");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(CreateNotionResponse.create());
        }
    }

    @GetMapping("/getNotionPage")
    public ResponseEntity<GetNotionResponse> getNotionPage(@RequestBody GetNotionRequest req) {
        return ResponseEntity.ok(notionLink);
    }
}
