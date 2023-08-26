package com.rimi.backend.domain.advice.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdviceResponseMessage {
    CREATE_ADVICE_SUCCESS("조언을 생성했습니다.");
    private final String message;
}
