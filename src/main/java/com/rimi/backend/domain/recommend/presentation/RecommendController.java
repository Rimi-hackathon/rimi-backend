package com.rimi.backend.domain.recommend.presentation;

import com.rimi.backend.domain.recommend.RecommendDto;
import com.rimi.backend.domain.recommend.application.service.NextQuestionUserCase;
import com.rimi.backend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.rimi.backend.domain.advice.presentation.constant.AdviceResponseMessage.CREATE_Q_A_SUCCESS;

@RestController
@RequestMapping("/recommend")
@AllArgsConstructor
public class RecommendController {

    private final NextQuestionUserCase nextQuestionUserCase;

    @PostMapping("/next")
    public ResponseEntity<SuccessResponse<String>> goNext(@Valid @RequestBody RecommendDto recommendDto) {
        return ResponseEntity.ok(SuccessResponse.create(CREATE_Q_A_SUCCESS.getMessage(), nextQuestionUserCase.findNextQuestions(recommendDto.getEmail())));
    }
}
