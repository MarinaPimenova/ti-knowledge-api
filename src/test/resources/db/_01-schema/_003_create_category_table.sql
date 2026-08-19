CREATE TABLE IF NOT EXISTS knowledge.category
(
    id              bigserial PRIMARY KEY,

    category_name   varchar(128) NOT NULL,

    description     varchar(1024),

    created_by      varchar(256) NOT NULL,
    created_date    timestamptz NOT NULL,
    updated_by      varchar(256),
    modified_date   timestamptz,

    CONSTRAINT uk_knowledge_category_name
        UNIQUE (category_name)
);