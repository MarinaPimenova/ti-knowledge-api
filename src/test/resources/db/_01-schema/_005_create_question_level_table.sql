CREATE TABLE IF NOT EXISTS knowledge.question_level
(
    id              bigserial PRIMARY KEY,

    code            varchar(10) NOT NULL,

    description     varchar(256),

    created_by      varchar(256) NOT NULL,
    created_date    timestamptz NOT NULL,
    updated_by      varchar(256),
    modified_date   timestamptz,

    CONSTRAINT uk_question_level
        UNIQUE(code)
);