package com.rimi.backend.domain.advice.application.service;

import com.rimi.backend.domain.advice.application.dto.request.AdviceRequest;
import com.rimi.backend.domain.advice.application.dto.response.AdviceResponse;
import com.rimi.backend.global.gpt.service.CreateAssistantService;
import com.rimi.backend.global.gpt.service.GetSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdviceUserCase {
    private final CreateAssistantService createAssistantService;
    private final GetSystemService getSystemService;

    public AdviceResponse createAdvice(AdviceRequest adviceRequest) {
        String system = getSystemService.getSystemContent("getAdvice");
        String user = "question: " + adviceRequest.getQuestion() + "\nanswer: " + adviceRequest.getAnswer();
        return new AdviceResponse(createAssistantService.createAssistant(system, user));
    }


}

