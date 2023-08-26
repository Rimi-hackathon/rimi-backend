package com.rimi.backend.global.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateNotionRequest {
    @Getter
    private String snsUrl;
    @Getter
    private String personalSiteUrl;
    @Getter
    private String email;
    @Getter
    private String name;

    public static CreateNotionRequest create(String snsUrl, String personalSiteUrl, String email, String name) {
        return new CreateNotionRequest(snsUrl, personalSiteUrl, email, name);
    }
}
