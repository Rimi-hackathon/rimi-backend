package com.rimi.backend.global.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CreateNotionRequest {
    @Getter
    private String snsUrl;
    @Getter
    private String personalSiteUrl;
    @Getter
    private String email;
    @Getter
    private String name;
    @Getter
    private String googleIdToken;
}
