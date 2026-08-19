-- _003_2_create_tag_table.sql

-- Notice that tag only needs to be unique within a category,
-- so the same tag name could exist in different contexts if needed.

CREATE TABLE IF NOT EXISTS knowledge.tag
(
    id            bigserial PRIMARY KEY,

    category_id   bigint       NOT NULL,

    tag           varchar(128) NOT NULL,

    description   varchar(1024),

    created_by    varchar(256) NOT NULL,
    created_date  timestamptz  NOT NULL,
    updated_by    varchar(256),
    modified_date timestamptz,

    CONSTRAINT fk_tag_category FOREIGN KEY (category_id) REFERENCES knowledge.category (id),

    CONSTRAINT uk_tag UNIQUE (category_id, tag)
);