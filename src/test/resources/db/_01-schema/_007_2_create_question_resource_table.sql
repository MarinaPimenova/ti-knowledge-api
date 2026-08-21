CREATE TABLE IF NOT EXISTS knowledge.question_resource
(
    id                      bigserial PRIMARY KEY,
    resource_id       bigint NOT NULL,
    question_id       bigint NOT NULL,

    created_by              varchar(256) NOT NULL,
    created_date            timestamptz NOT NULL,
    updated_by              varchar(256),
    modified_date           timestamptz,

    CONSTRAINT fk_question_resource
        FOREIGN KEY(resource_id)
            REFERENCES knowledge.resource(id),

    CONSTRAINT fk_question
        FOREIGN KEY(question_id)
            REFERENCES knowledge.question(id),
    CONSTRAINT uk_question_resource
        UNIQUE (resource_id, question_id)
);
