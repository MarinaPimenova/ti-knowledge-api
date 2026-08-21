package com.wk.ti.code.example.repository;

import com.wk.ti.code.example.model.CodeExampleQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeExampleQuestionRepository extends JpaRepository<CodeExampleQuestion, Long> {
}
