package com.wk.ti.question.repository;

import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionProjection;
import com.wk.ti.question.model.QuestionTagCountResponse;
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
                   qdp.tags,
                   qdp.question,
                   qdp.shortAnswer,
                   qdp.resources,
                   qdp.projectName
            from knowledge.find_questions_by_pattern(:pattern) qdp
            where qdp.createdby = case
                when :user is null then 'SYSTEM'
                else :user
            end
            """, nativeQuery = true)
    List<QuestionProjection> findByPattern(
            @Param("pattern") String pattern,
            @Param("user") String user);

    @Query(value = """
            select qdp.id,
                   qdp.tags,
                   qdp.question,
                   qdp.shortAnswer,
                   qdp.resources,
                   qdp.projectName
            from knowledge.question_dashboard_projection qdp
            where qdp.createdby = case
                when :user is null then 'SYSTEM'
                else :user
            end
            """, nativeQuery = true)
    List<QuestionProjection> findAllQuestion(@Param("user") String user);

    @Modifying
    @Query(value = """
            call knowledge.delete_question(:id, :user)
            """, nativeQuery = true)
    void remove(
            @Param("id") Long id,
            @Param("user") String user);

    @Query(value = """
            select qat.tag,
                   qat.question_count
            from knowledge.questions_aggregated_by_tags qat
            """, nativeQuery = true)
    List<QuestionTagCountResponse> countByTags();

    @Query(value = """
            select count(q.id)
            from knowledge.question q
            where q.created_by = case
                when :user is null then 'SYSTEM'
                else :user
            end
            """, nativeQuery = true)
    Long questionCount(@Param("user") String user);
}
