create table components
(
    id       bigserial
        primary key,
    name     varchar(50) not null,
    category varchar(20) not null,
    price    numeric     not null,
    quantity bigint      not null
);

alter table components
    owner to postgres;

create table models
(
    id   bigserial
        primary key,
    name varchar(100) not null
);

alter table models
    owner to postgres;

create table model_structures
(
    id           bigserial
        primary key,
    model_id     bigint not null
        references models
            on delete cascade,
    component_id bigint not null
        references components
            on delete cascade,
    quantity     bigint not null
);

alter table model_structures
    owner to postgres;

create table roles
(
    id   bigserial
        primary key,
    name varchar(20) not null
);

alter table roles
    owner to postgres;

create table users
(
    id            bigserial
        primary key,
    role_id       bigint       not null
        references roles
            on delete cascade,
    username      varchar(50)  not null,
    password_hash varchar(255) not null,
    full_name     varchar(100) not null
);

alter table users
    owner to postgres;

create table production_logs
(
    id       bigserial
        primary key,
    model_id bigint                   not null
        references models
            on delete cascade,
    user_id  bigint                   not null
        references users
            on delete cascade,
    date     timestamp with time zone not null
);

alter table production_logs
    owner to postgres;

