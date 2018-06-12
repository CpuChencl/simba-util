/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/1/16 11:31:39                           */
/*==============================================================*/


drop table if exists msgBlacklist;

drop table if exists dayAmount;

drop table if exists msgProject;

drop table if exists shortMessage;

drop table if exists msgTemplate;

/*==============================================================*/
/* Table: msgBlacklist*/
/*==============================================================*/
create table msgBlacklist
(
   id                   int not null auto_increment,
   mobile               varchar(32) not null comment '手机号',
   createTime           datetime not null,
   primary key (id),
   unique key AK_key_mobile (mobile)
);

alter table msgBlacklist comment '异常名单，暂时用于拉黑反复发短信的';

/*==============================================================*/
/* Table: dayAmount                                             */
/*==============================================================*/
create table dayAmount
(
   id                   bigint not null auto_increment,
   dayDate              date,
   amount               int not null,
   projectId            int,
   primary key (id)
);

alter table dayAmount comment '每天的发送量';

/*==============================================================*/
/* Table: msgProject*/
/*==============================================================*/
create table msgProject
(
   id                   int not null auto_increment,
   name                 varchar(64) comment '项目名称',
   projectKey           varchar(128) not null,
   ip                   text,
   threshold            float comment '报警阈值',
   limitNum             int comment '每天短信数量限制',
   email                varchar(1024) comment '邮箱提醒',
   mobile               varchar(1024) comment '短信提醒',
   primary key (id)
);

alter table msgProject comment '项目';

/*==============================================================*/
/* Table: shortMessage                                          */
/*==============================================================*/
create table shortMessage
(
   id                   bigint not null auto_increment comment 'ID',
   templateId           varchar(64) not null comment '模板Id',
   value                varchar(512) comment '模板中的值',
   mobile               varchar(32) not null comment '手机号码',
   sendDate             datetime not null comment '发送时间',
   projectId            int not null comment '来自项目',
   status               tinyint comment '发送状态',
   platform             varchar(32) comment '发送的平台',
   messageId            varchar(64) comment '调用SMS发送接口返回的短信ID',
   primary key (id)
);

alter table shortMessage comment '短信';

/*==============================================================*/
/* Table: msgTemplate;*/
/*==============================================================*/
create table msgTemplate
(
   id                   int not null auto_increment,
   name                 varchar(64) comment '模板名称',
   content              varchar(1024) comment '模板内容',
   createTime           datetime not null comment '模板创建时间',
   statusAli            tinyint,
   statusJpush          tinyint comment '审核状态',
   aliTemplateId        varchar(64),
   jpushTemplateId      varchar(64),
   selfId               varchar(64),
   primary key (id)
);

alter table msgTemplate comment '模板';

