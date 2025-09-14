--liquibase formatted sql

--changeset migration:202506_04_1
alter table app_confirm add column attempts int;

--changeset migration:202506_04_2
update app_confirm set attempts = 3 where attempts is null;
alter table app_confirm alter column attempts set not null;
--rollback alter table app_confirm alter column attempts drop not null;