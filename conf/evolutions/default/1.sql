# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table entry (
  id                        integer not null,
  title                     varchar(255) not null,
  message                   varchar(255) not null,
  constraint pk_entry primary key (id))
;

create sequence entry_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists entry;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists entry_seq;

