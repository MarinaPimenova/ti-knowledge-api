package com.wk.ti.qlevel.repository;

import com.wk.ti.qlevel.model.QuestionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionLevelRepository extends JpaRepository<QuestionLevel, Long> {
}
