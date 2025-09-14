--liquibase formatted sql

--changeset migration:202506_02_1
alter table app_auth alter column last_use type timestamp(6) using last_use::timestamp(6);

--changeset migration:202506_02_2
create table app_feature
(
    id   varchar primary key,
    flag boolean not null
);

--changeset migration:202506_02_3
create table app_notification
(
    id        uuid primary key,
    created   timestamp(6),
    modified  timestamp(6),
    status    varchar,
    type      varchar,
    recipient varchar,
    header    varchar,
    body      text
);

--changeset migration:202506_02_4
create table app_confirm
(
    id              uuid primary key,
    ext_id          uuid,
    ext_type        varchar,
    created         timestamp(6),
    modified        timestamp(6),
    confirmed       boolean not null,
    notification_id uuid,
    attributes      json,
    code            varchar,
    url             varchar
);

--changeset migration:202506_02_5
insert into app_feature (id, flag)
VALUES ('RESET_PASSWORD', true),
       ('CHANGE_PASSWORD', true);