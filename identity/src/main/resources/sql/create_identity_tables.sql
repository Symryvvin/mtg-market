create table account
(
    id         bigserial     not null
        constraint account_pk
            primary key,
    login      varchar(50)   not null,
    password   varchar(1000) not null,
    is_blocked bool default false,
    role       varchar(50)   not null
);

create
    unique index account_login_uindex
    on account (login);

create table profile
(
    account_id int not null,
    full_name  varchar(256),
    email      varchar(256),
    phone      int8
);

create table address
(
    profile_id int,
    post_index int,
    settlement varchar(256),
    street     varchar(256),
    building   varchar(256),
    apartment  varchar(256)
);

