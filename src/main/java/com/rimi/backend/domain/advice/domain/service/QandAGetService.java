package com.rimi.backend.domain.advice.domain.service;

import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.repository.QandARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QandAGetService {
    private final QandARepository qandARepository;

    public Optional<QandA> getQandA(String email, Integer step, Integer percent) {
        return qandARepository.findByEmailAndStepAndPercent(email, step, percent);
    }
}
