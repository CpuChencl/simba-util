/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/6/11 11:57:24                           */
/*==============================================================*/


drop table if exists tradeAccount;

drop table if exists tradeChannel;

drop table if exists tradeChannelDetail;

drop table if exists tradeDepartment;

drop table if exists tradeDetail;

drop table if exists tradePartyDetail;

drop table if exists tradeUser;

/*==============================================================*/
/* Table: tradeAccount                                          */
/*==============================================================*/
create table tradeAccount
(
   id                   bigint not null auto_increment,
   tradeUserID          bigint not null comment '支付用户ID',
   accountID            varchar(100) not null comment '账号ID',
   accountType          varchar(20) not null comment '账户类型：部门资产账户/个人账户/渠道账号',
   feeType              varchar(10) not null default 'CNY' comment '货币类型：人民币为CNY',
   isAllowRecharge      tinyint not null default 0 comment '是否允许充值：0不允许，1允许',
   isAllowPay           tinyint not null default 0 comment '是否允许支付：0不允许，1允许',
   isActive             tinyint not null default 0 comment '是否激活：0未激活，1激活，-1注销',
   isFrozen             tinyint not null default 1 comment '是否冻结：0冻结，1未冻结',
   accountBalance       bigint not null default 0 comment '账户当前余额',
   availableBalance     bigint not null default 0 comment '账户当前可用余额',
   frozenBalance        bigint not null default 0 comment '账户当前冻结余额',
   createTime           datetime default CURRENT_TIMESTAMP comment '创建时间',
   lastUpdateTime       datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后修改时间',
   primary key (id),
   unique key uk_accountID (accountID)
);

alter table tradeAccount comment '支付账号';

/*==============================================================*/
/* Table: tradeChannel                                          */
/*==============================================================*/
create table tradeChannel
(
   id                   bigint not null auto_increment,
   name                 varchar(100) comment '渠道名称：微信支付/支付宝支付',
   type                 varchar(20) comment '渠道类型：WXPAY/ALIPAY',
   createTime           datetime default CURRENT_TIMESTAMP comment '创建时间',
   lastUpdateTime       datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id),
   unique key uk_type (type)
);

alter table tradeChannel comment '渠道信息';

/*==============================================================*/
/* Table: tradeChannelDetail                                    */
/*==============================================================*/
create table tradeChannelDetail
(
   id                   bigint not null auto_increment,
   tradeAccountID       varchar(100) not null comment '帐号ID',
   channelID            bigint comment '渠道ID',
   orderCreateTime      datetime comment '渠道提交时间',
   paymentTime          datetime comment '渠道支付时间',
   orderNO              varchar(100) comment '渠道订单号',
   openID               varchar(100) comment '用户的openID',
   errorMsg             varchar(200) default '' comment '错误信息',
   errorCode            varchar(20) default '' comment '错误代号',
   createTime           datetime default CURRENT_TIMESTAMP comment '创建时间',
   lastUpdateTime       datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id)
);

alter table tradeChannelDetail comment '交易的渠道信息';

/*==============================================================*/
/* Table: tradeDepartment                                       */
/*==============================================================*/
create table tradeDepartment
(
   id                   bigint not null auto_increment comment '收款部门',
   deptNO               varchar(50) comment '部门编号',
   deptName             varchar(50) comment '部门名称',
   createTime           datetime default CURRENT_TIMESTAMP,
   lastUpdateTime       datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (id),
   unique key uk_deptNO (deptNO)
);

alter table tradeDepartment comment '收款部门';

/*==============================================================*/
/* Table: tradeDetail                                           */
/*==============================================================*/
create table tradeDetail
(
   id                   bigint not null auto_increment,
   tradeNO              bigint not null comment '交易流水号',
   tradeType            varchar(10) not null comment '记录交易类型：充值/消费',
   tradeStatus          varchar(10) not null comment '记录支付状态 SUCCESS/FAILED/FROZON',
   orderNO              varchar(100) not null comment '订单号',
   orderName            varchar(100) not null comment '订单名称',
   orderDesc            varchar(200) default '' comment '订单描述',
   orderAddress         varchar(200) default '' comment '订单地址',
   feeType              varchar(10) not null default 'CNY' comment '货币类型',
   originalAmount       bigint not null default 0 comment '原始费用',
   paymentAmount        bigint not null default 0 comment '实际费用',
   partyTradeUserID     bigint comment '用户ID',
   counterpartyTradeUserID bigint comment '对手主题用户ID',
   channelTradeUserID   bigint comment '渠道用户ID',
   tradePartyID         bigint not null comment '交易主体ID',
   tradeCounterpartyID  bigint not null comment '交易对手ID',
   tradeChannelID       bigint comment '交易渠道ID',
   tradeCreateTime      datetime not null comment '请求支付时间',
   tradePaymentTime     datetime not null comment '支付创建时间',
   tradePaymentDate     date comment '支付创建日期',
   createTime           datetime not null default CURRENT_TIMESTAMP comment '创建时间',
   lastUpdateTime       datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id),
   unique key uk_orderNO (orderNO),
   unique key uk_tradeNO (tradeNO)
);

alter table tradeDetail comment '交易详情信息';

/*==============================================================*/
/* Table: tradePartyDetail                                      */
/*==============================================================*/
create table tradePartyDetail
(
   id                   bigint not null auto_increment,
   tradeUserID          bigint not null comment '支付用户ID',
   partyName            varchar(100) not null comment '主体名称：买家姓名/部门名称',
   partyType            varchar(20) not null comment '主体类型：个人/公司部门',
   tradeAccountID       varchar(100) not null comment '账户ID',
   ip                   varchar(16) default '' comment '交易的设备IP信息（对手主体可选填）',
   mobileNumber         varchar(11) default '' comment '用户的电话信息（对手主体可选填）',
   device               varchar(10) default '' comment '手机的平台： IOS/Android（ 对手主体可选填）',
   noticeMail           varchar(255) not null comment '通知的邮箱',
   location             varchar(200) default '' comment '当时的位置信息（对手主体可选填）',
   createTime           datetime default CURRENT_TIMESTAMP comment '创建时间',
   primary key (id)
);

alter table tradePartyDetail comment '交易主体';

/*==============================================================*/
/* Table: tradeUser                                             */
/*==============================================================*/
create table tradeUser
(
   id                   bigint not null auto_increment,
   userID               varchar(100) not null comment '用户ID/部门ID',
   name                 varchar(100) not null comment '用户/部门名称',
   type                 varchar(20) not null comment '用户类型：PERSON/CHANNEL/DEPARTMENT',
   isAllowPay           tinyint not null comment '是否允许支付：0不允许，1允许',
   isActive             tinyint not null comment '状态：1：激活，0：未激活，-1：注销',
   payPassword          varchar(128) not null comment '支付密码 ',
   payPhone             varchar(11) not null comment '支付手机',
   payEmail             varchar(200) not null comment '支付邮箱',
   createTime           datetime default CURRENT_TIMESTAMP comment '创建时间',
   lastUpdateTime       datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id)
);

alter table tradeUser comment '钱包用户信息';

