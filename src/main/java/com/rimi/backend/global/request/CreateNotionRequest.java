package com.rimi.backend.global.request;

import lombok.Getter;

public class CreateNotionRequest {
    @Getter
    private String snsUrl;
    @Getter
    private String personalSiteUrl;
    @Getter
    private String email;
    @Getter
    private String name;
}
