--liquibase formatted sql

--changeset migration:202507_11_1
create table app_support_request
(
    id        uuid primary key,
    created   timestamp(6),
    modified  timestamp(6),
    author_id uuid,
    status    varchar,
    title     varchar
);

--changeset migration:202507_11_2
create index idx_support_request_created on app_support_request (created desc);

--changeset migration:202507_11_3
create index idx_support_request_author on app_support_request (author_id);

--changeset migration:202507_11_4
create table app_support_discuss
(
    id           uuid primary key,
    created      timestamp(6),
    modified     timestamp(6),
    from_user_id uuid,
    request_id   uuid,
    message      text,
    read         boolean not null
);

--changeset migration:202507_11_5
create index idx_support_discuss_created on app_support_discuss (created desc);

--changeset migration:202507_11_6
create index idx_support_discuss_request on app_support_discuss (request_id);