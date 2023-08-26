package com.rimi.backend.domain.advice.domain.entity;

import com.rimi.backend.domain.advice.application.dto.request.NextStepRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class QandA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_a_id")
    private Long QandAid;
    private String googleIdToken;
    private Integer step; //1, 2, 3
    private Integer percent; //50%, 75%
    @Column(nullable = false, length = 300)
    private String question;
    @Column(nullable = false, length = 300)
    private String answer;
    @Column(nullable = false, length = 500)
    private String advice;

    public static QandA createQandA(NextStepRequest nextStepRequest) {
        return QandA.builder()
                .googleIdToken(nextStepRequest.getGoogleIdToken())
                .question(nextStepRequest.getQuestion())
                .advice(nextStepRequest.getAdvice())
                .answer(nextStepRequest.getAnswer())
                .step(nextStepRequest.getStep())
                .percent(nextStepRequest.getPercent())
                .build();
    }
}
