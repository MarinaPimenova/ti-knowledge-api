package com.wk.ti.project.model;

import com.wk.ti.question.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "project", schema = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(generator = "project_id_seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "project_id_seq_gen", sequenceName = "knowledge.project_id_seq",
            allocationSize = 10)
    private Long id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_lead")
    private String projectLead;
}
