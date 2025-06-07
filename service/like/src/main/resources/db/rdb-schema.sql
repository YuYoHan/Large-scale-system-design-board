create database article_like;
use article_like;

create table article_like (
    article_like_id bigint not null primary key,
    article_id bigint not null,
    user_id bigint not null,
    created_at datetime not null
);

create unique index idx_article_id_user_id on article_like(article_id asc, user_id asc);

create database test_db;
use test_db;

create table lock_test (
    id bigint not null primary key ,
    content varchar(100) not null
);

insert into lock_test values (1234, 'test');
start transaction;
update lock_test
set content='test2'
where id=1234;
select * from performance_schema.data_locks;

use article_like;
select * from lock_test where id=1234;



create table article_like_count (
    article_id bigint not null primary key,
    like_count bigint not null,
    version bigint not null
);

create table outbox (
    outbox_id bigint not null primary key,
    shard_key bigint not null,
    event_type varchar(100) not null,
    payload varchar(5000) not null,
    created_at datetime not null
);

create index idx_shard_key_created_at on outbox(shard_key asc, created_at asc);