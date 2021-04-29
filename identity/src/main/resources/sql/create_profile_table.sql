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