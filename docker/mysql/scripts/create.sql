create database order_service character set utf8mb4 collate utf8mb4_unicode_ci;

use order_service;

create table if not exists orders
(
    id       bigint auto_increment primary key,
    price    decimal(19, 2) not null,
    status   integer,
    user_id  varchar(255),
    created  datetime(6),
    modified datetime(6)
);
