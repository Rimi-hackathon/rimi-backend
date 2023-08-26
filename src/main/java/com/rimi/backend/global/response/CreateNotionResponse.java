package com.rimi.backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CreateNotionResponse {
    private String notionUrl;

    public static CreateNotionResponse create(String url) {
        return new CreateNotionResponse(url);
    }

    public static CreateNotionResponse create() {
        return new CreateNotionResponse(null);
    }
}
