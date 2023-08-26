package com.rimi.backend.domain.advice.domain.repository;

import com.rimi.backend.domain.advice.domain.entity.QandA;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QandARepository extends JpaRepository<QandA, Long> {
    // query all by googleIdToken
    List<QandA> findAllByEmail(String Email);

    Optional<QandA> findByEmailAndStepAndPercent(String email, Integer step, Integer percent);
}
