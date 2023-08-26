package com.rimi.backend.domain.advice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rimi.backend.domain.advice.application.dto.request.AdviceRequest;
import com.rimi.backend.domain.advice.application.dto.response.AdviceResponse;
import com.rimi.backend.global.gpt.service.CreateAssistantService;
import com.rimi.backend.global.gpt.service.GetSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdviceUserCase {

    private final CreateAssistantService createAssistantService;
    private final GetSystemService getSystemService;

    public Flux<String> createAdvice(AdviceRequest adviceRequest) throws JsonProcessingException {
        String system = getSystemService.getSystemContent("getAdvice");
        String user = "chatgpt: " + adviceRequest.getQuestion() + "\nOOO: " + adviceRequest.getAnswer();

        return createAssistantService.createAssistant(system, user);
    }
}
