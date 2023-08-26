package com.rimi.backend.global.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatGptResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    @JsonProperty("choices")
    private List<Choice> choices;
    private Usage usage;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}
