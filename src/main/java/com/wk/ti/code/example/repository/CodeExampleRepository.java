package com.wk.ti.code.example.repository;

import com.wk.ti.code.example.model.CodeExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CodeExampleRepository extends JpaRepository<CodeExample, Long> {
    @Modifying
    @Query(value = """
            delete from knowledge.code_example where id = :codeId
            """, nativeQuery = true)
    void remove(@Param("codeId") Long id);
}
