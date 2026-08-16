package com.wk.ti.question.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wk.ti.code.example.model.CodeExampleDto;
import com.wk.ti.knowledge.tag.model.TagDto;
import com.wk.ti.project.model.ProjectDto;
import com.wk.ti.qlevel.model.QuestionLevelDto;
import com.wk.ti.resource.model.ResourceDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Immutable
@Entity
@Table(name = "question_projection", schema = "knowledge")
public class QuestionDetails {

    @Id
    private Long id;

    private String question;

    @Column(name = "short_answer")
    private String shortAnswer;

    @Column(name = "detailed_answer")
    private String detailedAnswer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "question_level", columnDefinition = "jsonb")
    private QuestionLevelDto questionLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "code_example", columnDefinition = "jsonb")
    private CodeExampleDto codeExample;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<TagDto> tags;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ResourceDto> resources;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ProjectDto> projects;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnore
    public static QuestionDetails of(Long id, QuestionDetails q) {
        return q.toBuilder()
                .id(id)
                .build();
    }
}