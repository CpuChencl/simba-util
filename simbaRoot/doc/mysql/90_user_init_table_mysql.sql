/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/19 星期三 13:56:03                       */
/*==============================================================*/


drop table if exists project;

drop table if exists smartUser;

drop table if exists thirdSystemUser;

drop table if exists userGroup;

/*==============================================================*/
/* Table: userGroup                                             */
/*==============================================================*/
create table userGroup
(
   id                   bigint not null auto_increment,
   name                 varchar(64) comment '分组名称',
   description          varchar(256) comment '分组描述',
   status               int comment '分组状态',
   type                 int comment '分组类型',
   creater              varchar(64) comment '分组创建者',
   createTime           datetime,
   primary key (id)
);

alter table userGroup comment '用户分组';


/*==============================================================*/
/* Table: project                                               */
/*==============================================================*/
create table project
(
   id                   int not null auto_increment,
   code                 varchar(64) not null comment '编码',
   name                 varchar(64) not null comment '名称',
   projectKey           varchar(128) not null comment '加密密钥',
   loginSuccessUrl      varchar(256) comment '登录成功后的回调地址',
   forgetBackUrl        varchar(256) comment '忘记密码后的回调url地址',
   createTime           datetime not null comment '创建时间',
   createUserAccount    varchar(64) not null comment '创建者账号',
   primary key (id),
   unique key AK_Key_Code (code)
);

alter table project comment '项目';

/*==============================================================*/
/* Table: smartUser                                             */
/*==============================================================*/
create table smartUser
(
   id                   bigint not null auto_increment,
   account              varchar(128) not null comment '账号',
   name                 varchar(128) not null comment '名称',
   email                varchar(128) comment '邮箱',
   telNo                varchar(32) comment '手机号',
   password             varchar(128) comment '密码',
   createTime           datetime not null comment '注册时间',
   status               tinyint comment '状态',
   primary key (id),
   unique key AK_Key_Account (account)
);

alter table smartUser comment '用户';

/*==============================================================*/
/* Table: thirdSystemUser                                       */
/*==============================================================*/
create table thirdSystemUser
(
   id                   bigint not null auto_increment,
   userId               bigint not null comment '用户ID',
   thirdSystem          varchar(64) not null comment '第三方系统',
   thirdSystemUserId    varchar(128) not null comment '第三方系统用户ID标识',
   primary key (id),
   key AK_Key_UserId (userId),
   key AK_Key_ThirdUserId (thirdSystemUserId, thirdSystem)
);

alter table thirdSystemUser comment '第三方系统用户';

