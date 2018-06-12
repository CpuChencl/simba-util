/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/9/7 星期四 16:58:00                        */
/*==============================================================*/


drop table if exists project;

drop table if exists projectUser;

drop table if exists urlData;

/*==============================================================*/
/* Table: project                                               */
/*==============================================================*/
create table project
(
   id                   int not null auto_increment,
   code                 varchar(128) not null,
   name                 varchar(128) not null,
   primary key (id),
   unique key AK_Key_Code (code)
);

alter table project comment '项目';

/*==============================================================*/
/* Table: projectUser                                           */
/*==============================================================*/
create table projectUser
(
   id                   int not null auto_increment,
   account              varchar(128) not null,
   projectId            int not null,
   type                 tinyint not null,
   primary key (id)
);

alter table projectUser comment '项目用户关系表';

/*==============================================================*/
/* Table: urlData                                               */
/*==============================================================*/
create table urlData
(
   id                   int not null auto_increment,
   projectId            int not null,
   url                  varchar(128) not null,
   data                 text not null,
   description          varchar(128),
   primary key (id)
);

alter table urlData comment 'url地址对应的数据表';

