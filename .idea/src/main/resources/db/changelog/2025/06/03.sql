--liquibase formatted sql

--changeset migration:202506_03_1
alter table app_confirm drop column ext_id;

--changeset migration:202506_03_2
alter table app_confirm drop column ext_type;

--changeset migration:202506_03_3
alter table app_confirm drop column attributes;

--changeset migration:202506_03_4
alter table app_confirm add column payload json;

--changeset migration:202506_03_5
insert into app_feature (id, flag) values ('USER_CREATED', true);