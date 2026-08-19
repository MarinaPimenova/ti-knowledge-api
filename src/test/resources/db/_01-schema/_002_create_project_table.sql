CREATE TABLE IF NOT EXISTS knowledge.project
(

    id            bigserial     not null primary key,

    project_name  varchar(1024) not null,
    project_lead  varchar(256)  not null,

    created_by    varchar(256)  not null,
    created_date  timestamptz   not null,
    updated_by    varchar(256),
    modified_date timestamptz
);