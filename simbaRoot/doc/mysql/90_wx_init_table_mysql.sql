/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/11/13 星期一 10:30:18                      */
/*==============================================================*/


drop table if exists article;

drop table if exists autoReply;

drop table if exists blacklist;

drop table if exists device;

drop table if exists deviceBind;

drop table if exists fans;

drop table if exists foreverMedia;

drop table if exists groupMessage;

drop table if exists receiveEvent;

drop table if exists receiveMessage;

drop table if exists sendMessage;

drop table if exists tag;

drop table if exists tagFans;

drop table if exists tempMedia;

drop table if exists uploadImage;

drop table if exists uploadNews;

drop table if exists uploadVideo;

drop table if exists wxMenu;

/*==============================================================*/
/* Table: article                                               */
/*==============================================================*/
create table article
(
   id                   bigint not null auto_increment,
   title                varchar(128),
   thumbMediaId         varchar(128),
   author               varchar(64),
   digest               varchar(128),
   showCoverPic         tinyint,
   content              text,
   contentSourceUrl     varchar(256),
   createTime           datetime,
   type                 tinyint,
   primary key (id)
);

alter table article comment '图文内容';

/*==============================================================*/
/* Table: autoReply                                             */
/*==============================================================*/
create table autoReply
(
   id                   int not null auto_increment,
   type                 tinyint not null,
   content              varchar(1024) not null,
   primary key (id)
);

alter table autoReply comment '自动回复设置';

/*==============================================================*/
/* Table: blacklist                                             */
/*==============================================================*/
create table blacklist
(
   id                   int not null auto_increment,
   fansId               int not null,
   createTime           datetime not null,
   primary key (id),
   key AK_Key_fansId (fansId)
);

alter table blacklist comment '黑名单';

/*==============================================================*/
/* Table: device                                                */
/*==============================================================*/
create table device
(
   id                   bigint not null auto_increment,
   wxDeviceId           varchar(128) not null comment '微信设备ID',
   qrcode               varchar(128) not null comment '二维码',
   wxProductId          varchar(128) not null comment '微信产品ID',
   status               int not null comment '状态',
   primary key (id)
);

alter table device comment '设备表';

/*==============================================================*/
/* Table: deviceBind                                            */
/*==============================================================*/
create table deviceBind
(
   id                   bigint not null auto_increment,
   openid               varchar(128) not null,
   wxDeviceId           varchar(128) not null,
   createTime           datetime not null,
   primary key (id)
);

alter table deviceBind comment '设备绑定表';

/*==============================================================*/
/* Table: fans                                                  */
/*==============================================================*/
create table fans
(
   id                   int not null auto_increment,
   openid               varchar(128) not null,
   remark               varchar(128),
   nickname             varchar(128),
   sex                  tinyint,
   city                 varchar(64),
   province             varchar(64),
   country              varchar(64),
   headimgurl           varchar(256),
   subscribeTime        datetime,
   primary key (id),
   unique key AK_Key_openid (openid)
);

alter table fans comment '粉丝';

/*==============================================================*/
/* Table: foreverMedia                                          */
/*==============================================================*/
create table foreverMedia
(
   id                   bigint not null auto_increment,
   mediaId              varchar(128),
   articles             varchar(128),
   type                 varchar(64),
   fileUrl              varchar(256),
   title                varchar(256),
   introduction         varchar(1024),
   imageUrl             varchar(256),
   createTime           datetime,
   name                 varchar(64),
   primary key (id)
);

alter table foreverMedia comment '永久素材';

/*==============================================================*/
/* Table: groupMessage                                          */
/*==============================================================*/
create table groupMessage
(
   id                   bigint not null auto_increment,
   createTime           datetime not null,
   content              varchar(512),
   type                 varchar(64) not null,
   wxTagId              int,
   isAll                tinyint,
   openids              text,
   status               tinyint,
   json                 text,
   account              varchar(128),
   mediaId              varchar(128),
   msgId                bigint,
   msgDataId            bigint,
   primary key (id),
   key AK_Key_time (createTime)
);

alter table groupMessage comment '群发消息';

/*==============================================================*/
/* Table: receiveEvent                                          */
/*==============================================================*/
create table receiveEvent
(
   id                   bigint not null auto_increment,
   openid               varchar(128) not null,
   createTime           datetime not null,
   type                 varchar(64) not null,
   event                varchar(128),
   eventKey             varchar(1024),
   ticket               varchar(128),
   latitude             varchar(128),
   longitude            varchar(128),
   eventPrecision       varchar(128),
   xml                  text not null,
   menuId               varchar(128),
   scanType             varchar(128),
   scanResult           varchar(256),
   primary key (id),
   key AK_Key_time (createTime)
);

alter table receiveEvent comment '接收到的事件';

/*==============================================================*/
/* Table: receiveMessage                                        */
/*==============================================================*/
create table receiveMessage
(
   id                   bigint not null auto_increment,
   openid               varchar(128) not null,
   createTime           datetime not null,
   type                 varchar(64) not null,
   msgId                varchar(64) not null,
   content              varchar(1024) not null,
   picUrl               varchar(256),
   mediaId              varchar(128),
   format               varchar(64),
   fileUrl              varchar(256),
   thumbMediaId         varchar(128),
   thumbFileUrl         varchar(256),
   locationX            varchar(32),
   locationY            varchar(32),
   scale                varchar(32),
   messageLabel         varchar(32),
   title                varchar(128),
   description          varchar(256),
   url                  varchar(512),
   xml                  text not null,
   recognition          varchar(1024),
   primary key (id),
   unique key AK_Key_msgId (msgId),
   key AK_Key_time (createTime)
);

alter table receiveMessage comment '接收的用户消息';

/*==============================================================*/
/* Table: sendMessage                                           */
/*==============================================================*/
create table sendMessage
(
   id                   bigint not null auto_increment,
   openid               varchar(128) not null,
   content              varchar(1024),
   createTime           datetime,
   mediaId              varchar(128),
   type                 varchar(64),
   thumbMediaId         varchar(128),
   title                varchar(128),
   descritption         varchar(512),
   musicUrl             varchar(512),
   hqMusicUrl           varchar(512),
   news                 text,
   json                 text,
   account              varchar(128),
   primary key (id),
   key AK_Key_time (createTime)
);

alter table sendMessage comment '发送消息';

/*==============================================================*/
/* Table: tag                                                   */
/*==============================================================*/
create table tag
(
   id                   int not null auto_increment,
   name                 varchar(32) not null,
   wxTagId              int not null,
   primary key (id)
);

alter table tag comment '标签';

/*==============================================================*/
/* Table: tagFans                                               */
/*==============================================================*/
create table tagFans
(
   id                   int not null auto_increment,
   tagId                int not null,
   fansId               int not null,
   primary key (id),
   key AK_Key_tagId (tagId),
   key AK_Key_fansId (fansId)
);

alter table tagFans comment '粉丝标签关系表';

/*==============================================================*/
/* Table: tempMedia                                             */
/*==============================================================*/
create table tempMedia
(
   id                   bigint not null auto_increment,
   type                 varchar(64),
   fileUrl              varchar(256),
   mediaId              varchar(128),
   createTime           datetime,
   name                 varchar(128),
   primary key (id),
   key AK_Key_time (createTime)
);

alter table tempMedia comment '临时素材';

/*==============================================================*/
/* Table: uploadImage                                           */
/*==============================================================*/
create table uploadImage
(
   id                   bigint not null auto_increment,
   sourceUrl            varchar(256),
   wxUrl                varchar(256) not null,
   createTime           datetime not null,
   name                 varchar(64),
   primary key (id),
   key AK_Key_time (createTime)
);

alter table uploadImage comment '上传图片';

/*==============================================================*/
/* Table: uploadNews                                            */
/*==============================================================*/
create table uploadNews
(
   id                   bigint not null auto_increment,
   acticles             varchar(512),
   mediaId              varchar(128),
   createTime           datetime,
   name                 varchar(64),
   primary key (id),
   key AK_Key_time (createTime)
);

alter table uploadNews comment '上传图文';

/*==============================================================*/
/* Table: uploadVideo                                           */
/*==============================================================*/
create table uploadVideo
(
   id                   bigint not null auto_increment,
   mediaId              varchar(128),
   targetMediaId        varchar(128),
   createTime           datetime,
   title                varchar(128),
   description          varchar(512),
   primary key (id),
   key AK_Key_time (createTime)
);

alter table uploadVideo comment '上传视频';

/*==============================================================*/
/* Table: wxMenu                                                */
/*==============================================================*/
create table wxMenu
(
   id                   int not null auto_increment,
   text                 varchar(16) not null,
   parentID             int not null,
   orderNo              int not null,
   menuKey              varchar(64) not null,
   url                  varchar(128),
   type                 varchar(64) not null,
   mediaId              varchar(128),
   appid                varchar(128),
   pagepath             varchar(256),
   primary key (id),
   unique key AK_Key_key (menuKey)
);

alter table wxMenu comment '微信公众号菜单';

