--liquibase formatted sql

--changeset migration:202507_05_1
create table app_task
(
    id     varchar primary key,
    locked timestamp(6) not null
);

--changeset migration:202507_05_2
update app_feature set flag = false;

--changeset migration:202507_05_3
update app_feature set flag = true;

--changeset migration:202507_05_4
create index idx_notification_created on app_notification (created desc);

--changeset migration:202507_05_5
create index idx_confirm_modified on app_confirm (modified desc);