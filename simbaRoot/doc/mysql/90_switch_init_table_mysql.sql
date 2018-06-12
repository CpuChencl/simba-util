/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/10 星期一 14:14:54                       */
/*==============================================================*/


drop table if exists receiveDealType;

drop table if exists receiveMsg;



/*==============================================================*/
/* Table: receiveDealType                                       */
/*==============================================================*/
create table receiveDealType
(
   id                   int not null auto_increment,
   beanId               varchar(128) not null comment '处理这个类型的Bean的Id，都必须实现同一个接口，并注入成Spring Bean',
   sync                 tinyint not null comment '是否同步处理(0:异步，1：同步)',
   name                 varchar(128) comment '名称',
   description          varchar(128) comment '描述',
   ext                  varchar(512) comment '扩展属性',
   primary key (id),
   unique key AK_Key_Name (name)
);

alter table receiveDealType comment '处理接收消息类型';

/*==============================================================*/
/* Table: receiveMsg                                            */
/*==============================================================*/
create table receiveMsg
(
   id                   bigint not null auto_increment,
   type                 varchar(128) not null comment '类型',
   source               varchar(128) not null comment '源',
   message              text not null comment '内容',
   createTime           datetime not null comment '时间',
   primary key (id)
);

alter table receiveMsg comment '接收到的消息';


