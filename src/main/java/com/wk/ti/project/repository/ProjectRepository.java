package com.wk.ti.project.repository;

import com.wk.ti.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = """
            select count(p.id)
            from knowledge.project p
            where p.created_by = case
                when :user is null then 'SYSTEM'
                else :user
            end
            """, nativeQuery = true)
    Long projectCount(@Param("user") String user);

    @Query(value = """
            select *
            from knowledge.project p
            where p.created_by = case
                when :user is null then 'SYSTEM'
                else :user
            end
            """, nativeQuery = true)
    List<Project> findAllProjects(@Param("user") String user);
}
