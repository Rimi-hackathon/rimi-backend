package com.rimi.backend.domain.advice.application.service;

import com.rimi.backend.domain.advice.application.dto.request.NextStepRequest;
import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.service.QandAGetService;
import com.rimi.backend.domain.advice.domain.service.QandASaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NextStepUserCase {

    private final QandASaveService qandASaveService;
    private final QandAGetService qandAGetService;

    public void goNext(NextStepRequest nextStepRequest) {
        Optional<QandA> qandAResponse = qandAGetService.getQandA(nextStepRequest.getEmail(), nextStepRequest.getStep(), nextStepRequest.getPercent());
        QandA qandA;
        if (qandAResponse.isPresent()) {
            qandA=qandAResponse.get();
            qandA.updateQandA(nextStepRequest.getQuestion(), nextStepRequest.getAnswer(), nextStepRequest.getAdvice());

        }else {
            qandA = QandA.createQandA(nextStepRequest);
        }
        qandASaveService.saveQandA(qandA);
    }
}
