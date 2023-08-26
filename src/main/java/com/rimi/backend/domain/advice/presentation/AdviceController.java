package com.rimi.backend.domain.advice.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rimi.backend.domain.advice.application.dto.request.AdviceRequest;
import com.rimi.backend.domain.advice.application.dto.request.NextStepRequest;
import com.rimi.backend.domain.advice.application.service.AdviceUserCase;
import com.rimi.backend.domain.advice.application.service.NextStepUserCase;
import com.rimi.backend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

import static com.rimi.backend.domain.advice.presentation.constant.AdviceResponseMessage.CREATE_Q_A_SUCCESS;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdviceController {
    private final AdviceUserCase adviceUserCase;
    private final NextStepUserCase nextStepUserCase;

    /**
     * 조언 생성하기
     * [POST] api/advice
     * 입력: String question, String answer
     * 출력: String advice
     */
    @PostMapping(value = "advice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createAdvice(@Valid @RequestBody AdviceRequest adviceRequest) {
        try {
            return adviceUserCase.createAdvice(adviceRequest);
        } catch (JsonProcessingException je) {
            log.error(je.getMessage());
            return Flux.empty();
        }
    }

    /**
     * 다음 단계로 넘어가는 경우
     * [POST] api/next
     * 입력: String question, String answer, String googleIdToken
     */
    @PostMapping("/next")
    public ResponseEntity<SuccessResponse> goNext(@Valid @RequestBody NextStepRequest nextStepRequest) {
        this.nextStepUserCase.goNext(nextStepRequest);
        return ResponseEntity.ok(SuccessResponse.create(CREATE_Q_A_SUCCESS.getMessage()));
    }
}
