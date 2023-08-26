package com.rimi.backend.domain.advice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NextStepRequest {

    @NotBlank(message = "question 을 입력해주세요.")
    @Size(min = 1, max = 300, message = "question 은 최소 1개, 최대 300개의 문자만 입력 가능합니다.")
    private String question;

    @NotBlank(message = "answer 을 입력해주세요.")
    @Size(min = 1, max = 300, message = "answer 은 최소 1개, 최대 300개의 문자만 입력 가능합니다.")
    private String answer;

    @Size(max = 500, message = "advice 은 최소 1개, 최대 500개의 문자만 입력 가능합니다.")
    private String advice;

    @NotBlank(message = "email 을 입력해주세요.")
    private String email;

    @NotNull(message = "step 을 입력해주세요.(1,2,3,4)")
    private Integer step;

    @NotNull(message = "percent 을 입력해주세요.(50,75,100)")
    private Integer percent;
}
