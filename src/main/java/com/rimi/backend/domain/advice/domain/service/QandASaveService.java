package com.rimi.backend.domain.advice.domain.service;

import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.repository.QandARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QandASaveService {
    private final QandARepository qandARepository;
    public void saveQandA(QandA qandA){
        qandARepository.save(qandA);
    }
}
