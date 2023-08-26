package com.rimi.backend.domain.advice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdviceRequest {

    @NotBlank(message = "question 을 입력해주세요.")
    private String question;

    @NotBlank(message = "answer 을 입력해주세요.")
    @Size(min = 1, max = 300, message="answer 은 최소 20개, 최대 300개의 문자만 입력 가능합니다.")
    private String answer;

}
