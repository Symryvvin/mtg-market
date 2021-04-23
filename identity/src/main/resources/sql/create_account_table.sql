CREATE TYPE role AS ENUM ('CLIENT', 'TRADER', 'MANAGER');

create table account
(
    id         bigserial     not null
        constraint account_pk
            primary key,
    login      varchar(50)   not null,
    password   varchar(1000) not null,
    is_blocked bool default false,
    role       role          not null
);

create
    unique index account_login_uindex
    on account (login);



