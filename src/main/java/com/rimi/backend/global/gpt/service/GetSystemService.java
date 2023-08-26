package com.rimi.backend.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSystemService {

    private final String systemContentFormat = "You're {}. \n" +
            "We're going to give you some questions and user's answers, and you must use them to {}. \n" +
            "You must use korean to answer. Say clearly.\n";

    public String getSystemContent(String type) {
        switch (type) {
            case "getAdvice":
                return systemContentFormat.format(
                        "a critic",
                        "point out how to enrich the answer quality",
                        "Answer in 300 ~ 400 characters."
                );
            case "findNextQuestion":
                return systemContentFormat.format(
                        "an interviewer",
                        "choose appropriate question to better understand the user's experience",
                        "Based on your understanding of the situation so far, please chooose one question according to following list. \n" +
                                "1. 당신이 가슴 뛰었던 경험을 말해주세요. \n" +
                                "2. 당신이 가장 후회하고 있는 경험을 말해주세요. \n" +
                                "3. 당신이 가장 열정적이었던 경험을 말해주세요. \n"
                );
            case "getNotionInput":
                return systemContentFormat.format(
                        "a personal branding consultant",
                        "answer appropriately",
                        "Based on your understanding of the situation so far, please fill out following questions.\n" +
                                "질문1: 답변을 하고 있는 사람을 긍정적으로 표현해줘. 답변1: \n" +
                                "질문2: 답변을 하고 있는 사람을 긍정적으로 상세하게 표현해줘. 답변2: \n" +
                                "질문3: 답변을 하고 있는 사람이 중요하게 생각하는 가치를 적어줘. 답변3: \n" +
                                "질문4: 답변을 하고 있는 사람이 가진 강점 및 스킬을 단답으로 여러개 적어줘. 답변4: \n" +
                                "질문5: 답변을 하고 있는 사람이 가진 경험을 단답으로 여러개 적어줘. 답변5:"
                );
            default:
                return "Unknown type";
        }
    }
}
