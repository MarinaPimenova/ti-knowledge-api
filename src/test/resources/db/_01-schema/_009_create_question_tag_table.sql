CREATE TABLE IF NOT EXISTS knowledge.question_tag
(
    id                      bigserial PRIMARY KEY,
    knowledge_tag_id       bigint NOT NULL,
    question_id       bigint NOT NULL,

    created_by              varchar(256) NOT NULL,
    created_date            timestamptz NOT NULL,
    updated_by              varchar(256),
    modified_date           timestamptz,

    CONSTRAINT fk_question_tag
        FOREIGN KEY(knowledge_tag_id)
            REFERENCES knowledge.tag(id),

    CONSTRAINT fk_question
        FOREIGN KEY(question_id)
            REFERENCES knowledge.question(id)
);

ALTER TABLE knowledge.question_tag
    ADD CONSTRAINT uk_question_tag
        UNIQUE (knowledge_tag_id, question_id);