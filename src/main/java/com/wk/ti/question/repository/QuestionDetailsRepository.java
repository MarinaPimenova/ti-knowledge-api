package com.wk.ti.question.repository;

import com.wk.ti.question.model.QuestionDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDetailsRepository
        extends JpaRepository<QuestionDetails, Long> {

    List<QuestionDetails> findAllByOrderByUpdatedDateDesc(Pageable pageable);

    @Query(value = """
            select *
            from knowledge.question_projection qp
            where qp.created_by = case
                when :user is null then 'SYSTEM'
                else :user
            end
            limit :questionLimit
            """, nativeQuery = true)
    List<QuestionDetails> findAllByOrderByUpdatedDateDesc(
            @Param("questionLimit") Integer limit, @Param("user") String user);
}