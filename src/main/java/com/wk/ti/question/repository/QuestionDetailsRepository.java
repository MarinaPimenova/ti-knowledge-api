package com.wk.ti.question.repository;

import com.wk.ti.question.model.QuestionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionDetailsRepository
        extends JpaRepository<QuestionDetails, Long> {
}