package com.wk.ti.project.repository;

import com.wk.ti.project.model.ProjectQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectQuestionRepository  extends JpaRepository<ProjectQuestion, Long> {
}
