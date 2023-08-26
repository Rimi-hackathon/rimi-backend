package com.rimi.backend.domain.advice.presentation;

import com.rimi.backend.domain.advice.application.dto.request.AdviceRequest;
import com.rimi.backend.domain.advice.application.service.AdviceUserCase;
import com.rimi.backend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.rimi.backend.domain.advice.presentation.constant.AdviceResponseMessage.CREATE_ADVICE_SUCCESS;

@RestController
@AllArgsConstructor
@RequestMapping("/api/advice")
public class AdviceController {
    private final AdviceUserCase adviceUserCase;

    /**
     * 조언 생성하기
     * [POST] api/advice
     * 입력: String question, String answer
     * 출력: String advice
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> createAdvice(@Valid @RequestBody AdviceRequest adviceRequest) {
        return ResponseEntity.ok(SuccessResponse.create(CREATE_ADVICE_SUCCESS.getMessage(), adviceUserCase.createAdvice(adviceRequest)));
    }

}
