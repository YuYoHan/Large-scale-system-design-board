show databases;

use article;

show tables;

desc article;

select *
from article;
select count(article_id)
from article;

# 일반적인 페이징 조회
select *
from article
where board_id = 1
order by created_at desc
limit 30 offset 90;
# 페이징 조회 Query Plan 확인
explain
select *
from article
where board_id = 1
order by created_at desc
limit 30 offset 90;

# 인덱싱
create index idx_board_id_article_id on article (board_id asc, article_id desc);

# article_id로 조회
select *
from article
where board_id = 1
order by article_id desc
limit 30 offset 90;
explain
select *
from article
where board_id = 1
order by article_id desc
limit 30 offset 90;

# 50,000 페이지 조회
select *
from article
where board_id = 1
order by article_id desc
limit 30 offset 1499970;
explain
select *
from article
where board_id = 1
order by article_id desc
limit 30 offset 1499970;

# Covering Index 방법 사용
select board_id, article_id
from article
where board_id = 1
order by article_id desc
limit 30 offset 1499970;
explain
select board_id, article_id
from article
where board_id = 1
order by article_id desc
limit 30 offset 1499970;

# 추출된 30건의 article_id로 Clustered Index 접근
select *
from (select article_id
      from article
      where board_id = 1
      order by article_id desc
      limit 30 offset 1499970) t
         left join article on t.article_id = article.article_id;
explain
select *
from (select article_id
      from article
      where board_id = 1
      order by article_id desc
      limit 30 offset 1499970) t
         left join article on t.article_id = article.article_id;


select *
from (select article_id
      from article
      where board_id = 1
      order by article_id desc
      limit 30 offset 8999970) t
         left join article on t.article_id = article.article_id;

select count(*) from article where board_id = 1;

select count(*) from (
    select article_id from article where board_id = 1 limit 300301
                     ) t;


create table board_article_count
(
    board_id      bigint not null primary key,
    article_count bigint not null
);

create table outbox
(
    outbox_id  bigint        not null primary key,
    shard_key  bigint        not null,
    event_type varchar(100)  not null,
    payload    varchar(5000) not null,
    created_at datetime      not null
);

create index idx_shard_key_created_at on outbox (shard_key asc, created_at asc);