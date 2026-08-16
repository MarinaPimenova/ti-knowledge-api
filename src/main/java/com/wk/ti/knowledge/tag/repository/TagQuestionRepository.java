package com.wk.ti.knowledge.tag.repository;

import com.wk.ti.knowledge.tag.model.TagQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagQuestionRepository extends JpaRepository<TagQuestion, Long> {
}
