package com.wk.ti.question.repository;

import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings({"SqlResolve", "SqlSignature"})
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
            select qdp.id,
                   qdp.tag,
                   qdp.question,
                   qdp.shortAnswer,
                   qdp.resourceUrl,
                   qdp.description,
                   qdp.projectName
            from knowledge.question_dashboard_projection qdp
            """, nativeQuery = true)
    List<QuestionProjection> findAllQuestion();

    @Modifying
    @Query(value = """
            call knowledge.delete_question(:questionId, :user)
            """, nativeQuery = true)
    void remove(
            @Param("questionId") Long id,
            @Param("user") String user);
}
