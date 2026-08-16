package com.wk.ti.knowledge.tag.model;

import com.wk.ti.question.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "tag", schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(generator = "tag_id_seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "tag_id_seq_gen",
            sequenceName = "knowledge.tag_id_seq", allocationSize = 10)
    private Long id;
    @Column(name = "category_id")
    private Long knowledgeCategoryId;
    private String tag;
    private String description;
}
