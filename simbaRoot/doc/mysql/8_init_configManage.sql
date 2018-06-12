/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/1/31 ÐÇÆÚÈý 8:48:39                        */
/*==============================================================*/


drop table if exists applicationUser;

drop table if exists property;

drop table if exists propertyTemplate;

/*==============================================================*/
/* Table: applicationUser                                       */
/*==============================================================*/
create table applicationUser
(
   id                   int not null auto_increment,
   userId               int not null,
   applicationId        int not null,
   userType             tinyint,
   primary key (id)
);

/*==============================================================*/
/* Table: property                                              */
/*==============================================================*/
create table property
(
   id                   int not null auto_increment,
   name                 varchar(128),
   templateId           int not null,
   dev                  text,
   prod                 text,
   test                 text,
   createtime           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: propertyTemplate*/
/*==============================================================*/
create table propertyTemplate
(
   id                   int not null auto_increment,
   name                 varchar(64) not null,
   description          varchar(128),
   template             text,
   createtime           datetime not null,
   primary key (id)
);

