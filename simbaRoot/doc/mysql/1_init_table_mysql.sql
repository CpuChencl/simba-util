/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/1/31 星期三 14:30:29                       */
/*==============================================================*/


drop table if exists buss;

drop table if exists deployLog;

drop table if exists devProject;

drop table if exists exceptionInfo;

drop table if exists job;

drop table if exists menu;

drop table if exists operLog;

drop table if exists org;

drop table if exists orgExt;

drop table if exists orgRole;

drop table if exists permission;

drop table if exists mockProject;

drop table if exists projectPackageResult;

drop table if exists projectServer;

drop table if exists projectServerRel;

drop table if exists projectUser;

drop table if exists projectVersion;

drop table if exists registryTable;

drop table if exists registryType;

drop table if exists role;

drop table if exists rolePermission;

drop table if exists systemUser;

drop table if exists urlData;

drop table if exists userExt;

drop table if exists userOrg;

drop table if exists userRole;

/*==============================================================*/
/* Table: buss                                                  */
/*==============================================================*/
create table buss
(
   name                 varchar(64) not null,
   description          varchar(128),
   script               text not null,
   primary key (name)
);

/*==============================================================*/
/* Table: deployLog                                             */
/*==============================================================*/
create table deployLog
(
   id                   bigint not null auto_increment,
   projectId            int comment '项目id',
   name                 varchar(64) comment '项目名称',
   createTime           datetime comment '时间',
   primary key (id)
);

alter table deployLog comment '部署日志';

/*==============================================================*/
/* Table: devProject                                            */
/*==============================================================*/
create table devProject
(
   id                   int not null auto_increment,
   code                 varchar(64) comment '编号',
   name                 varchar(64) comment '名称',
   versionType          varchar(64) comment '版本管理类型(svn/git)',
   account              varchar(128) comment '账号',
   pwd                  varchar(128) comment '密码',
   versionUrl           varchar(256) comment '版本管理地址',
   packageCommandFile   varchar(512) comment '打包命令文件路径',
   startParams          varchar(512) comment '服务启动参数',
   createTime           datetime comment '时间',
   notifyEmails         varchar(1024) comment '通知邮件地址',
   primary key (id),
   unique key AK_Key_Code (code)
);

alter table devProject comment '项目';

/*==============================================================*/
/* Table: exceptionInfo                                         */
/*==============================================================*/
create table exceptionInfo
(
   id                   bigint not null auto_increment,
   ip                   varchar(32) not null comment 'ip地址',
   ipInfo               varchar(64) not null comment 'ip地址信息',
   exceptionInfo        text not null comment '异常信息',
   createTime           datetime not null comment '时间',
   primary key (id)
);

alter table exceptionInfo comment '异常信息';

/*==============================================================*/
/* Table: job                                                   */
/*==============================================================*/
create table job
(
   id                   int not null auto_increment,
   name                 varchar(64) not null comment '名称',
   description          varchar(256) comment '描述',
   cronExpression       varchar(128) comment 'cron表达式',
   startTime            varchar(64) comment '开始执行时间',
   endTime              varchar(64) comment '结束执行时间',
   exeCount             int comment '执行次数',
   maxExeCount          int comment '最大执行次数',
   status               varchar(16) comment '状态',
   className            varchar(256) not null comment '完整类路径',
   methodName           varchar(128) not null comment '执行类方法名',
   delayTime            int comment '延迟时间',
   intervalTime         int comment '间隔时间',
   primary key (id)
);

alter table job comment '任务';

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu
(
   id                   int not null auto_increment,
   text                 varchar(64) not null,
   parentID             int not null,
   url                  varchar(256),
   orderNo              int,
   primary key (id)
);

alter table menu comment '菜单';

/*==============================================================*/
/* Table: operLog                                               */
/*==============================================================*/
create table operLog
(
   id                   bigint not null auto_increment,
   account              varchar(64) not null comment '账号',
   ip                   varchar(32) not null comment 'IP',
   address              varchar(64) not null comment '地址',
   content              varchar(512) not null comment '内容',
   createTime           datetime not null comment '时间',
   primary key (id),
   key AK_Key_Time (createTime),
   key AK_Key_Account (account)
);

alter table operLog comment '操作日志';

/*==============================================================*/
/* Table: org                                                   */
/*==============================================================*/
create table org
(
   id                   int not null auto_increment comment '机构ID',
   text                 varchar(128) not null comment '名称',
   parentID             int not null comment '父机构ID',
   orderNo              int comment '排序',
   primary key (id)
);

alter table org comment '机构';

/*==============================================================*/
/* Table: orgExt                                                */
/*==============================================================*/
create table orgExt
(
   id                   int not null comment '机构ID',
   primary key (id)
);

alter table orgExt comment '机构扩展';

/*==============================================================*/
/* Table: orgRole                                               */
/*==============================================================*/
create table orgRole
(
   id                   int not null auto_increment comment '机构ID',
   orgID                int not null comment '名称',
   roleName             varchar(64) not null comment '父机构ID',
   primary key (id)
);

alter table orgRole comment '机构角色关联表';

/*==============================================================*/
/* Table: permission                                            */
/*==============================================================*/
create table permission
(
   id                   int not null auto_increment,
   text                 varchar(64) not null,
   url                  varchar(512),
   parentID             int not null,
   primary key (id)
);

/*==============================================================*/
/* Table: mockProject                                               */
/*==============================================================*/
create table mockProject
(
   id                   int not null auto_increment,
   code                 varchar(128) not null,
   name                 varchar(128) not null,
   primary key (id),
   unique key AK_Key_Code (code)
);

alter table mockProject comment '项目';

/*==============================================================*/
/* Table: projectPackageResult                                  */
/*==============================================================*/
create table projectPackageResult
(
   id                   int not null auto_increment,
   projectId            int comment '项目id',
   filePath             varchar(512) comment '打包结果文件路径',
   primary key (id),
   key AK_Key_projectId (projectId)
);

alter table projectPackageResult comment '项目打包结果';

/*==============================================================*/
/* Table: projectServer                                         */
/*==============================================================*/
create table projectServer
(
   id                   int not null auto_increment,
   name                 varchar(64) comment '名称',
   ip                   varchar(64) comment 'IP',
   port                 int comment '端口号',
   primary key (id)
);

alter table projectServer comment '服务器';

/*==============================================================*/
/* Table: projectServerRel                                      */
/*==============================================================*/
create table projectServerRel
(
   id                   int not null auto_increment,
   projectId            int comment '项目id',
   serverId             int comment '服务器id',
   primary key (id)
);

alter table projectServerRel comment '项目绑定部署的服务器';

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
/* Table: projectVersion                                        */
/*==============================================================*/
create table projectVersion
(
   id                   int not null auto_increment,
   versionNo            varchar(128) not null comment '版本号',
   filePath             varchar(256) not null comment '文件地址',
   createTime           datetime not null comment '发布时间',
   primary key (id),
   unique key AK_Key_VersionNo (versionNo)
);

alter table projectVersion comment '项目版本';

/*==============================================================*/
/* Table: registryTable                                         */
/*==============================================================*/
create table registryTable
(
   id                   int not null auto_increment,
   code                 varchar(64) not null comment '编码',
   value                varchar(128) not null comment '值',
   typeID               int not null comment '类型ID',
   description          varchar(128) comment '描述',
   primary key (id)
);

alter table registryTable comment '注册表';

/*==============================================================*/
/* Table: registryType                                          */
/*==============================================================*/
create table registryType
(
   id                   int not null auto_increment,
   text                 varchar(128) not null comment '名称',
   parentID             int not null comment '父ID',
   primary key (id)
);

alter table registryType comment '注册类型';

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   name                 varchar(64) not null,
   description          varchar(64),
   primary key (name)
);

/*==============================================================*/
/* Table: rolePermission                                        */
/*==============================================================*/
create table rolePermission
(
   id                   int not null auto_increment,
   roleName             varchar(64) not null,
   permissionID         int not null,
   primary key (id)
);

/*==============================================================*/
/* Table: systemUser                                            */
/*==============================================================*/
create table systemUser
(
   account              varchar(64) not null,
   name                 varchar(64) not null,
   pwd                  varchar(256) not null,
   primary key (account)
);

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

/*==============================================================*/
/* Table: userExt                                               */
/*==============================================================*/
create table userExt
(
   userAccount          varchar(64) not null comment '用户账号',
   primary key (userAccount)
);

alter table userExt comment '用户扩展';

/*==============================================================*/
/* Table: userOrg                                               */
/*==============================================================*/
create table userOrg
(
   id                   int not null auto_increment,
   userAccount          varchar(64) not null comment '用户账号',
   orgID                int not null comment '机构ID',
   primary key (id)
);

alter table userOrg comment '用户机构';

/*==============================================================*/
/* Table: userRole                                              */
/*==============================================================*/
create table userRole
(
   userAccount          varchar(64) not null,
   roleName             varchar(64) not null,
   primary key (userAccount, roleName)
);

