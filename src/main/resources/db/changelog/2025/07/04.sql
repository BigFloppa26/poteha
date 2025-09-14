--liquibase formatted sql

--changeset migration:202507_04_1
alter table app_confirm add column expires_in bigint;

--changeset migration:202507_04_2
update app_confirm set expires_in = extract(epoch from modified)::bigint where expires_in is null;
alter table app_confirm alter column expires_in set not null;
--rollback alter table app_confirm alter column expires_in drop not null;