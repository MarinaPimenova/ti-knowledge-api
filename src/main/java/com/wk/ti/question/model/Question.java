package com.wk.ti.question.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "question", schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "question_id_seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "question_id_seq_gen", sequenceName = "knowledge.question_id_seq_gen", allocationSize = 1)
    private Long id;
    @Column(name = "question_level_id")
    private Long questionLevelId;
    @Column(name = "question")
    private String question;
    @Column(name = "short_answer")
    private String shortAnswer;
    @Column(name = "detailed_answer")
    private String detailedAnswer;

}