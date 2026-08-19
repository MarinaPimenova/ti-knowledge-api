CREATE TABLE IF NOT EXISTS knowledge.project_question
(
    id                      bigserial PRIMARY KEY,
    project_id       bigint NOT NULL,
    question_id       bigint NOT NULL,

    created_by              varchar(256) NOT NULL,
    created_date            timestamptz NOT NULL,
    updated_by              varchar(256),
    modified_date           timestamptz,


    CONSTRAINT fk_question_project
        FOREIGN KEY(project_id)
            REFERENCES knowledge.project(id),

    CONSTRAINT fk_question
        FOREIGN KEY(question_id)
            REFERENCES knowledge.question(id)
);

ALTER TABLE knowledge.project_question
    ADD CONSTRAINT uk_project_question
        UNIQUE (project_id, question_id);