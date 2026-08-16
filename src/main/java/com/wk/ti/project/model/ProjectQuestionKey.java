package com.wk.ti.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ProjectQuestionKey implements Serializable {
    @Column(name = "question_id")
    Long questionId;
    @Column(name = "project_id")
    Long projectId;
}
