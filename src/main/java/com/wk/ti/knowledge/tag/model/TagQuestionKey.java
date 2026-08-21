package com.wk.ti.knowledge.tag.model;

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
public class TagQuestionKey implements Serializable {
    @Column(name = "question_id")
    Long questionId;
    @Column(name = "knowledge_tag_id")
    Long tagId;
}
