package com.rimi.backend.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSystemService {

    private final String systemContentFormat = "You're {} to {}\n"+
            "We're going to give you some questions and user's answers, and you must use them to {}.\n"+
            "Give people who don't know what to do with {} how-to's and concrete examples, and advise them to {}.\n"+
            "Use friendly, easy-to-read UX writing for a better user experience.\n"+
            "Use Korean to answer.\n";

    public String getSystemContent(String type) {
        switch (type) {
            case "getAdvice":
                return systemContentFormat.format(
                        "a facilitator", "help user make good answer",
                        "improve the quality of user's answers",
                        "answering", "make more informative answers"
                );
            case "findNextQuestion":
                return systemContentFormat.format(
                        "an interviewer", "ask a question",
                        "choose next question to understand user",
                        "answering", "make more informative answers"
                );
            case "getNotionInput":
                return systemContentFormat.format(
                        "a personal branding consultant and UX writer", "help user's self-branding",
                        "improve the quality of user's answers",
                        "answering", "make more informative answers"
                );
            default:
                return "Unknown type";
        }
    }
}
