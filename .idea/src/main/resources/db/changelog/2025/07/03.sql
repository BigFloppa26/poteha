--liquibase formatted sql

--changeset migration:202507_03_1
alter table app_feature add column required boolean;

--changeset migration:202507_03_2
update app_feature set required = true;
alter table app_feature alter column required set not null;

--changeset migration:202507_03_3
update app_feature set id = 'PWD_CHANGE' where id = 'CHANGE_PASSWORD';

--changeset migration:202507_03_4
update app_feature set id = 'PWD_RESET' where id = 'RESET_PASSWORD';

--changeset migration:202507_03_5
update app_feature set id = 'USR_CREATE' where id = 'USER_CREATED';

--changeset migration:202507_03_6
update app_user set enabled = true where enabled = false;
