CREATE TABLE IF NOT EXISTS knowledge.question_code_example
(
    id                      bigserial PRIMARY KEY,
    code_example_id       bigint NOT NULL,
    question_id       bigint NOT NULL,

    created_by              varchar(256) NOT NULL,
    created_date            timestamptz NOT NULL,
    updated_by              varchar(256),
    modified_date           timestamptz,

    CONSTRAINT fk_question_code_example
        FOREIGN KEY(code_example_id)
            REFERENCES knowledge.code_example(id),

    CONSTRAINT fk_question
        FOREIGN KEY(question_id)
            REFERENCES knowledge.question(id),
    CONSTRAINT uk_question_code_example
        UNIQUE (code_example_id, question_id)
);
