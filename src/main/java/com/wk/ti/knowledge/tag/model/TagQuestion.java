package com.wk.ti.knowledge.tag.model;

import com.wk.ti.question.model.BaseEntity;
import com.wk.ti.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "question_tag",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"question_id", "knowledge_tag_id"})},
        schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagQuestion extends BaseEntity {
    @EmbeddedId
    private TagQuestionKey tagQuestionKey;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "knowledge_tag_id")
    private Tag tag;
}
