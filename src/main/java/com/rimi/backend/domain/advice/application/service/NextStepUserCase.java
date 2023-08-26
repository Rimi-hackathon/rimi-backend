package com.rimi.backend.domain.advice.application.service;

import com.rimi.backend.domain.advice.application.dto.request.NextStepRequest;
import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.service.QandASaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NextStepUserCase {

    private final QandASaveService qandASaveService;

    public void goNext(NextStepRequest nextStepRequest){
        QandA qandA=QandA.createQandA(nextStepRequest);
        qandASaveService.saveQandA(qandA);
    }
}
