drop table if exists activity;

drop table if exists pointDetail;

drop table if exists pointSummary;

/*==============================================================*/
/* Table: activity                                              */
/*==============================================================*/
create table activity
(
   id                   bigint not null auto_increment,
   activityID           varchar(100) not null,
   name                 varchar(100),
   description          varchar(500),
   ownerID              varchar(64),
   point                int,
   startTime            timestamp,
   endTime              timestamp,
   createTime           timestamp,
   updateTime           timestamp,
   primary key (id),
   unique key uk_activityID (activityID)
);

/*==============================================================*/
/* Table: pointDetail                                           */
/*==============================================================*/
create table pointDetail
(
   id                   bigint not null auto_increment,
   userID               varchar(64),
   activityID           bigint,
   point                int,
   createTime           timestamp,
   expireTime           timestamp,
   primary key (id)
);

/*==============================================================*/
/* Table: pointSummary                                          */
/*==============================================================*/
create table pointSummary
(
   id                   bigint not null auto_increment,
   userID               varchar(64),
   point                int,
   createTime           timestamp,
   updateTime           timestamp,
   primary key (id),
   unique key uk_userID (userID)
);
