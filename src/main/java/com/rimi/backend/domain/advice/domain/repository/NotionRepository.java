package com.rimi.backend.domain.advice.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rimi.backend.domain.advice.domain.entity.NotionResult;

public interface NotionRepository extends JpaRepository<NotionResult, Long> {
    public NotionResult findByGoogleIdToken(String googleIdToken);
}
