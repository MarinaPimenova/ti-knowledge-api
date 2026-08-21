package com.wk.ti.code.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CodeExampleQuestionKey {
    @Column(name = "question_id")
    Long questionId;
    @Column(name = "code_example_id")
    Long codeExampleId;
}
