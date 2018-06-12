drop table if exists opinionFeedback;

/*==============================================================*/
/* Table: opinionFeedback                                       */
/*==============================================================*/
create table opinionFeedback
(
   id                   int not null auto_increment,
   userId               int not null,
   title                varchar(128),
   text                 text,
   createTime           datetime not null,
   primary key (id)
);
drop table if exists FAQ;

/*==============================================================*/
/* Table: FAQ                                                   */
/*==============================================================*/
create table FAQ
(
   id                   int not null auto_increment,
   title                varchar(128),
   text                 text,
   type                 int not null,
   createTime           datetime not null,
   primary key (id)
);
drop table if exists FAQType;

/*==============================================================*/
/* Table: FAQType                                               */
/*==============================================================*/
create table FAQType
(
   id                   int not null auto_increment,
   type                 varchar(32) not null,
   primary key (id)
);
drop table if exists bugFeedback;

/*==============================================================*/
/* Table: bugFeedback                                           */
/*==============================================================*/
create table bugFeedback
(
   id                   int not null auto_increment,
   userId               int not null,
   title                varchar(128),
   text                 text,
   img                  text,
   createTime           datetime not null,
   primary key (id)
);
