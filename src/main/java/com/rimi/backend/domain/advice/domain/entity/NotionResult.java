package com.rimi.backend.domain.advice.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Entity
public class NotionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    public String email;
    @Getter
    public String url;

    public static NotionResult createNotionResult(String email, String url) {
        return NotionResult.builder()
                .email(email)
                .url(url)
                .build();
    }
}