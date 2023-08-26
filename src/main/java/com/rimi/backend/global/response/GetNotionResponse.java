package com.rimi.backend.global.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetNotionResponse {
    public String notionLink;

    public static GetNotionResponse create(String notionLink) {
        return new GetNotionResponse(notionLink);
    }
}
