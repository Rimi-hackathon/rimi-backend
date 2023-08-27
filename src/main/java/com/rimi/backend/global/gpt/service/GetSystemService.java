package com.rimi.backend.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSystemService {

    private String systemContentFormat = "You're {}. \n" +
            "We're going to give you some questions and user's answers, and you must use them to {}. \n" +
            "You must use korean to answer and also with honorific.\n";

    private final String[] notionQuestions = { "Q1: OOO님의 특징이 잘 드러나도록 아주 짧게 한 문장으로 표현해줘. A1: 저는 ",
            "Q2: 업무 스타일에 대해 디테일하게 설명해줘. A2: \n",
            "Q3: 보유한 역량에 대해 디테일하게 설명해줘. A3: \n",
            "Q4: 앞으로의 성장 가능성에 대해 디테일하게 설명해줘. A4: \n",
            "Q5: 강점 및 스킬들을 적어주고, 이거를 어떻게 활용할 수 있을지 적어줘. 신뢰도가 높은 순으로 정렬해. A5: \n",
            "Q6: OOO님의 경험 중에서 특별하고 유의미한 경험들을 선별해줘. A6: \n",
            "Q7: 최종적으로 OOO님이 가치를 높일 수 있는 전략들을 5개 추천해줘. A7: \n" };

    public String getSystemContent(String type) {
        switch (type) {
            case "getAdvice":
                return "You're critic and also a helper" +
                        "We're going to give you some questions and user's answers, and you must use them to" +
                        "answer in 400 ~ 450 characters." + "You must use korean to answer and also with honorific.";
            case "getNotionInput":
                return "You're critic and also a helper" + "We're going to give you some questions and user's answers, and you must use them to" +
                        "respond according to the context of the conversation" +"answer in 400 ~ 450 characters." + "You must use korean to answer and also with honorific."+
                        "Based on your understanding of the situation so far, answer following questions.\n" +
                        "Separate the answers for each question with the $ symbol as a delimiter.\n" +
                        "Q1: OOO님의 특징이 잘 드러나도록 한 문장으로 표현해줘. A1: 저는 " +
                        "Q2: 업무 스타일에 대해 디테일하게 설명해줘. A2: \n" +
                        "Q3: 보유한 역량에 대해 디테일하게 설명해줘. A3: \n" +
                        "Q4: 앞으로의 성장 가능성에 대해 디테일하게 설명해줘. A4: \n" +
                        "Q5: 강점 및 스킬들을 적어주고, 이거를 어떻게 활용할 수 있을지 적어줘. 신뢰도가 높은 순으로 정렬해. A5: \n" +
                        "Q6: OOO님의 경험 중에서 특별하고 유의미한 경험들을 선별해줘. A6: \n" +
                        "Q7: 최종적으로 OOO님이 가치를 높일 수 있는 전략들을 5개 추천해줘. A7: \n";
            default:
                return "Unknown type";
        }
    }

    public String getNextQuestion(String input) {
        return "You're critic and also a helper" + "an interviewer" + "We're going to give you some questions and user's answers, and you must use them to" +
                " find good questions that can help better understand the user" +"answer in 400 ~ 450 characters." + "You must use korean to answer and also with honorific."+
                " I'm going to explain the dialog history, what questions would you like to ask to better understand the user in the future?" +
                "---" + input + "\n---";
    }

    public String getNotionQuestion(int index) {
        return notionQuestions[index - 1];
    }
}
