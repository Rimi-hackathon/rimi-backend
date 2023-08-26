package com.rimi.backend.global.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CreateAssistantService {

    @Value("${gpt.apiKey}")
    private String OPENAI_API_KEY;

    @Value("${gpt.apiUrl}")
    private String OPENAI_API_URL;

    private WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl(OPENAI_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();
    }

    public Flux<String> createAssistant(String system, String user) throws JsonProcessingException  {
        JSONObject payload = buildPayload(system, user, null);
        return sendRequestAndParseResponse(payload);
    }

    public Flux<String> createAssistantWithAssistantResponse(String system, String user, String assistant) {
        JSONObject payload = buildPayload(system, user, assistant);
        return sendRequestAndParseResponse(payload);
    }

    private JSONObject buildPayload(String system, String user, String assistant) {
        JSONObject payload = new JSONObject();
        JSONArray messageList = new JSONArray();

        messageList.put(buildMessage("system", system));
        messageList.put(buildMessage("user", user));
//        if (assistant != null && !assistant.isEmpty()) {
//            messageList.put(buildMessage("assistant", assistant));
//        }

        payload.put("model", "gpt-3.5-turbo-16k-0613");
        payload.put("messages", messageList);
        payload.put("stream", true);

        return payload;
    }

    private JSONObject buildMessage(String role, String content) {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private HttpPost buildPostRequest(StringEntity inputEntity) {
        HttpPost post = new HttpPost(OPENAI_API_URL);
        post.setEntity(inputEntity);
        post.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
        post.setHeader("Content-Type", "application/json");
        return post;
    }

    private Flux<String> sendRequestAndParseResponse(JSONObject payload) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload.toString())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }
}
