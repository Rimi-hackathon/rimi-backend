package com.rimi.backend.domain.advice.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rimi.backend.domain.advice.application.dto.request.AdviceRequest;
import com.rimi.backend.domain.advice.application.dto.response.AdviceResponse;
import com.rimi.backend.domain.advice.application.service.AdviceUserCase;
import com.rimi.backend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

import static com.rimi.backend.domain.advice.presentation.constant.AdviceResponseMessage.CREATE_ADVICE_SUCCESS;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdviceController {
    private final AdviceUserCase adviceUserCase;

    /**
     * 조언 생성하기
     * [POST] api/advice
     * 입력: String question, String answer
     * 출력: String advice
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "advice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createAdvice(@Valid @RequestBody AdviceRequest adviceRequest) {
        try {
            return adviceUserCase.createAdvice(adviceRequest);
        } catch (JsonProcessingException je) {
            log.error(je.getMessage());
            return Flux.empty();
        }
    }

}
