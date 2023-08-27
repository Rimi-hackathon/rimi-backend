package com.rimi.backend.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class CreateAssistantServiceBlock {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${gpt.apiKey}")
    private String OPENAI_API_KEY;

    public String createAssistant(String system, String user) {
        JSONObject payload = buildPayload(system, user, null);

        StringEntity inputEntity = new StringEntity(payload.toString(), ContentType.APPLICATION_JSON);
        HttpPost post = buildPostRequest(inputEntity);

        return sendRequestAndParseResponse(post);
    }

    public String createAssistantWithAssistantResponse(String system, String user, String assistant) {
        JSONObject payload = buildPayload(system, user, assistant);

        StringEntity inputEntity = new StringEntity(payload.toString(), ContentType.APPLICATION_JSON);
        HttpPost post = buildPostRequest(inputEntity);

        return sendRequestAndParseResponse(post);
    }

    private JSONObject buildPayload(String system, String user, String assistant) {
        JSONObject payload = new JSONObject();
        JSONArray messageList = new JSONArray();

        messageList.put(buildMessage("system", system));
        messageList.put(buildMessage("user", user));
        if (assistant != null && !assistant.isEmpty()) {
            messageList.put(buildMessage("assistant", assistant));
        }

        payload.put("model", "gpt-3.5-turbo-16k-0613");
        payload.put("messages", messageList);
        payload.put("temperature", 0);

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

    private String sendRequestAndParseResponse(HttpPost post) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            HttpEntity resEntity = response.getEntity();
            String resJsonString = new String(resEntity.getContent().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject resJson = new JSONObject(resJsonString);

            if (resJson.has("error")) {
                return "Error: " + resJson.getString("error");
            }

            JSONArray responseArray = resJson.getJSONArray("choices");
            if (responseArray.length() > 0) {
                JSONObject firstChoice = responseArray.getJSONObject(0);
                JSONObject messageObj = firstChoice.getJSONObject("message");
                if ("assistant".equals(messageObj.getString("role"))) {
                    String result = messageObj.getString("content");
                    // colon 이후의 메시지만 추출
                    String[] parts = result.split(":", 2);
                    return parts.length > 1 ? parts[1].trim() : result;
                }
            }

            return "No assistant response found.";

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
