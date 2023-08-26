package com.rimi.backend.domain.advice.domain.repository;

import com.rimi.backend.domain.advice.domain.entity.QandA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QandARepository extends JpaRepository<QandA, Long> {
}
