drop table if exists posters;
create table IF NOT EXISTS posters
(id bigint
, provider_id bigint
, email varchar
, first_name varchar
, last_name varchar
, description varchar
, practitioner varchar
, foreground varchar
, background varchar
, from_date date
, to_date date
, pdf varchar);

drop table if exists providers;
create table IF NOT EXISTS providers
(id bigint
, logo varchar
, title varchar);

drop table if exists likes;
create table IF NOT EXISTS likes
(id bigint
, poster_id bigint
, like_ varchar);


drop table if exists beloved;
create table IF NOT EXISTS beloved
(id bigint
, poster_id bigint
, beloved varchar);

insert into providers (id, logo, title) values(nextval('poster_seq'), 'medstar-logo.png', 'Med Star Washington');
insert into providers (id, logo, title) values(nextval('poster_seq'), 'inova-logo.png', 'INOVA');
insert into providers (id, logo, title) values(nextval('poster_seq'), 'georgewashington-logo.png', 'George Washington University');
insert into providers (id, logo, title) values(nextval('poster_seq'), 'sibley-logo.png', 'Sibley Memorial Hospital');
insert into providers (id, logo, title) values(nextval('poster_seq'), 'providence-hospital-logo.png', 'Providence Hospital');
insert into providers (id, logo, title) values(nextval('poster_seq'), 'childrensnational-logo.png', 'Childrens National Medical Center');
