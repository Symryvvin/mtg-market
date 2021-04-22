create table card
(
    id           varchar(36)  not null,
    oracle_id    varchar(36)  not null,
    name         varchar(300) not null,
    printed_name varchar(300),
    set_code     varchar(10)  not null,
    lang         varchar(10)  not null
);

create
    unique index CARD_ID_UINDEX
    on card (id);

alter table card
    add constraint CARD_PK
        primary key (id);