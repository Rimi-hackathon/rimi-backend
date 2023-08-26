package com.rimi.backend.domain.advice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NextStepRequest {

    @NotBlank(message = "question 을 입력해주세요.")
    private String question;

    @NotBlank(message = "answer 을 입력해주세요.")
    private String answer;

    @NotBlank(message = "advice 을 입력해주세요.")
    private String advice;

    @NotBlank(message = "googleIdToken 을 입력해주세요.")
    private String googleIdToken;
}
