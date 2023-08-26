package com.rimi.backend.domain.advice.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdviceResponseMessage {
    CREATE_ADVICE_SUCCESS("조언을 생성했습니다."),
    CREATE_Q_A_SUCCESS("다음 단계로 넘어갑니다");
    private final String message;
}
