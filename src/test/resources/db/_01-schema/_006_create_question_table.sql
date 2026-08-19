CREATE TABLE IF NOT EXISTS knowledge.question
(
    id                bigserial PRIMARY KEY,

    question_level_id bigint       NOT NULL,

    question          text         NOT NULL,
    short_answer      text,
    detailed_answer   text,
    created_by        varchar(256) NOT NULL,
    created_date      timestamptz  NOT NULL,
    updated_by        varchar(256),
    modified_date     timestamptz,

    CONSTRAINT fk_question_level FOREIGN KEY (question_level_id) REFERENCES knowledge.question_level (id)
);