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



