package com.rimi.backend.domain.recommend.application.service;

import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.repository.QandARepository;
import com.rimi.backend.domain.advice.domain.service.QandAGetService;
import com.rimi.backend.global.gpt.service.CreateAssistantServiceBlock;
import com.rimi.backend.global.gpt.service.GetSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NextQuestionUserCase {
    private final QandARepository qandARepository;
    private final CreateAssistantServiceBlock createAssistantServiceBlock;
    private final GetSystemService getSystemService;

    public String findNextQuestions(String email){
        List<QandA> qandAList=qandARepository.findAllByEmail(email);
        String fullText="";
        for(QandA qandA:qandAList){
            fullText+="chatgpt: "+qandA.getQuestion()+"\n"+"OOO: "+qandA.getAnswer()+"\n";
        }
        String system=getSystemService.getNextQuestion(fullText);
        String user="";
        return createAssistantServiceBlock.createAssistant(system,user);
    }
}
