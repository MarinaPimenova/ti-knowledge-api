package com.wk.ti.code.example.model;

import com.wk.ti.question.model.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_code_example",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"question_id", "code_example_id"})},
        schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeExampleQuestion {
    @EmbeddedId
    private CodeExampleQuestionKey codeExampleQuestionKey;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @MapsId("codeExampleId")
    @JoinColumn(name = "code_example_id")
    private CodeExample codeExample;
}
