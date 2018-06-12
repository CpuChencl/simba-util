/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/2/5 星期一 10:35:03                        */
/*==============================================================*/


drop table if exists realTimeMessage;

/*==============================================================*/
/* Table: realTimeMessage                                       */
/*==============================================================*/
create table realTimeMessage
(
   id                   bigint not null auto_increment,
   userId               int not null comment '用户ID',
   message              text not null comment '内容',
   createTime           datetime not null comment '时间',
   primary key (id)
);

alter table realTimeMessage comment '实时推送记录';

