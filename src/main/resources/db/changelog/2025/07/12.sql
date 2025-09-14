--liquibase formatted sql

--changeset migration:202507_12_1
alter table app_support_request add column description text;