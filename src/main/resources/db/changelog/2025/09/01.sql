--liquibase formatted sql

--changeset migration:202509_01_1
alter table app_auth add column user_agent varchar;

--changeset migration:202509_01_2
alter table app_user add column password_attempts int;