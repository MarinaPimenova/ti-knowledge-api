package com.wk.ti.project.model;

import com.wk.ti.question.model.BaseEntity;
import com.wk.ti.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "project_question",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"question_id", "project_id"})},
        schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectQuestion extends BaseEntity {
    @EmbeddedId
    private ProjectQuestionKey projectQuestionKey;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;
}
