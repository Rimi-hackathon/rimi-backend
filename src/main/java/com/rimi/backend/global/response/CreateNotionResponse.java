package com.rimi.backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CreateNotionResponse {
    private Boolean isSuccess;
    private String notionUrl;

    public static CreateNotionResponse create(String url) {
        return new CreateNotionResponse(true, url);
    }

    public static CreateNotionResponse create() {
        return new CreateNotionResponse(false, null);
    }
}
