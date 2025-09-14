--liquibase formatted sql

--changeset migration:202507_02_1
alter table app_comment add column user_id uuid;
alter table app_comment alter column user_id set not null;