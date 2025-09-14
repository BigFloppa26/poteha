--liquibase formatted sql

--changeset migration:202507_01_1
create table app_boat
(
    id            uuid primary key,
    created       timestamp(6),
    modified      timestamp(6),
    enabled       boolean not null,
    latitude      float   not null,
    longitude     float   not null,
    name          varchar not null,
    type          varchar not null,
    brand         varchar,
    description   text,
    owner_id      uuid    not null,
    width         integer,
    height        integer,
    length        integer,
    design        varchar,
    material      varchar,
    max_load      integer,
    capacity      integer,
    build_year    integer,
    retrofit_year integer
);

--changeset migration:202507_01_2
create table app_comment
(
    id       uuid primary key,
    created  timestamp(6),
    modified timestamp(6),
    boat_id  uuid not null,
    comment  text
);

--changeset migration:202507_01_3
create table app_option
(
    id       uuid primary key,
    created  timestamp(6),
    modified timestamp(6),
    name     varchar
);

--changeset migration:202507_01_4
create table app_boat_option
(
    option_id uuid    not null,
    boat_id   uuid    not null,
    include   boolean not null,
    price     float,
    primary key (option_id, boat_id)
);

--changeset migration:202507_01_5
create index idx_boat_option_include on app_boat_option (include);

--changeset migration:202507_01_6
create index idx_boat_enabled on app_boat (enabled);

--changeset migration:202507_01_7
create index idx_comment on app_comment (boat_id);

--changeset migration:202507_01_8
create index idx_boat_name on app_boat (name);

--changeset migration:202507_01_9
create index idx_boat_type on app_boat (type);


