CREATE TABLE IF NOT EXISTS knowledge.resource
(
    id                  bigserial PRIMARY KEY,
    description         varchar(1024),
    resource_url        varchar(2048),

    created_by          varchar(256) NOT NULL,
    created_date        timestamptz NOT NULL,
    updated_by          varchar(256),
    modified_date       timestamptz,

    CONSTRAINT chk_resource
        CHECK
            (
            description IS NOT NULL
                OR
            resource_url IS NOT NULL
            )
);