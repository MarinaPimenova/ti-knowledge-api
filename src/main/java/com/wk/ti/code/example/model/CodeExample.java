package com.wk.ti.code.example.model;

import com.wk.ti.question.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "code_example", schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeExample extends BaseEntity {
    @Id
    @GeneratedValue(generator = "code_example_id_seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "code_example_id_seq_gen", sequenceName = "knowledge.code_example_id_seq",
            allocationSize = 10)
    private Long id;

    private String language;

    @Column(name = "source_code")
    private String source_code;
}
