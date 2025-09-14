--liquibase formatted sql

--changeset migration:202507_10_1
alter table app_feature add column key varchar;

--changeset migration:202507_10_2
update app_feature set key = id;
alter table app_feature alter column key set not null;
--rollback alter table app_feature alter column key drop not null;

--changeset migration:202507_10_3
update app_feature set id = uuid_in(md5(random()::text || random()::text)::cstring);
alter table app_feature alter column id type uuid using id::uuid;

--changeset migration:202507_10_4
create unique index idx_feature_key on app_feature (key);

--changeset migration:202507_10_5
alter table app_feature add column created timestamp(6);

--changeset migration:202507_10_6
alter table app_feature add column modified timestamp(6);