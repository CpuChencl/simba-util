/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/3/13 星期二 8:57:03                        */
/*==============================================================*/


drop table if exists cardMoneyBill;

drop table if exists looseMoneyBill;

drop table if exists payBill;

drop table if exists redPackBill;

/*==============================================================*/
/* Table: cardMoneyBill                                         */
/*==============================================================*/
create table cardMoneyBill
(
   id                   bigint not null auto_increment,
   mchId                varchar(32) comment '商户号',
   partnerTradeNo       varchar(32) comment '商户企业付款单号',
   bankNo               varchar(64) comment '收款方银行卡号',
   trueName             varchar(16) comment '收款方用户名',
   bankCode             varchar(64) comment '收款方开户行',
   amount               int comment '付款金额',
   description          varchar(128) comment '付款说明',
   status               varchar(32) comment '状态',
   errMsg               varchar(32) comment '错误信息',
   paymentNo            varchar(64) comment '微信企业付款单号',
   cmmsAmt              int comment '手续费金额',
   createTime           datetime comment '时间',
   createUser           varchar(64) comment '付款者',
   primary key (id),
   key AK_Key_Time (createTime)
);

alter table cardMoneyBill comment '银行卡账单';

/*==============================================================*/
/* Table: looseMoneyBill                                        */
/*==============================================================*/
create table looseMoneyBill
(
   id                   bigint not null auto_increment,
   appid                varchar(64) comment '商户账号appid',
   mchid                varchar(32) comment '商户号',
   deviceInfo           varchar(32) comment '设备号',
   partnerTradeNo       varchar(64) comment '商户订单号',
   openid               varchar(64) comment '用户openid',
   reUserName           varchar(32) comment '收款用户姓名',
   amount               int comment '金额',
   description          varchar(128) comment '企业付款描述信息',
   clientIp             varchar(64) comment 'Ip地址',
   status               varchar(64) comment '状态',
   errMsg               varchar(64) comment '错误信息',
   paymentNo            varchar(64) comment '微信订单号',
   paymentTime          varchar(64) comment '微信支付成功时间',
   createTime           datetime comment '创建时间',
   createUser           varchar(64) comment '付款者',
   primary key (id),
   key AK_Key_Time (createTime)
);

alter table looseMoneyBill comment '零钱账单';

/*==============================================================*/
/* Table: payBill                                               */
/*==============================================================*/
create table payBill
(
   id                   bigint not null auto_increment,
   appid                varchar(32) comment 'AppID',
   mchId                varchar(32) comment '商户号',
   deviceInfo           varchar(32) comment '设备号',
   productDesc          varchar(128) comment '商品描述',
   detail               varchar(6000) comment '商品详情',
   attach               varchar(127) comment '附加数据',
   outTradeNo           varchar(32) comment '商户订单号',
   fee                  int comment '标价金额',
   clientIp             varchar(16) comment '终端IP',
   startTime            varchar(16) comment '交易起始时间',
   endTime              varchar(16) comment '交易结束时间',
   goodsTag             varchar(32) comment '订单优惠标记',
   notifyUrl            varchar(256) comment '通知地址',
   tradeType            varchar(16) comment '交易类型',
   productId            varchar(32) comment '商品ID',
   openid               varchar(128) comment '用户标识',
   status               varchar(32) comment '状态',
   errMsg               varchar(32) comment '错误信息',
   prepayId             varchar(64) comment '预支付交易会话标识',
   codeUrl              varchar(64) comment '二维码链接',
   mwebUrl              varchar(128) comment 'H5支付链接',
   createTime           datetime comment '时间',
   primary key (id),
   key AK_Key_Time (createTime)
);

alter table payBill comment '支付账单';

/*==============================================================*/
/* Table: redPackBill                                           */
/*==============================================================*/
create table redPackBill
(
   id                   bigint not null auto_increment,
   type                 varchar(32) comment '红包类型',
   billNo               varchar(32) comment '商户订单号',
   mchId                varchar(32) comment '商户号',
   appid                varchar(32) comment '公众账号appid',
   sendName             varchar(32) comment '商户名称',
   openid               varchar(32) comment '用户openid',
   amount               int comment '付款金额',
   num                  int comment '红包发放总人数',
   wishing              varchar(128) comment '红包祝福语',
   clientIp             varchar(16) comment 'Ip地址',
   actName              varchar(32) comment '活动名称',
   remark               varchar(256) comment '备注',
   sceneId              varchar(32) comment '场景id',
   riskInfo             varchar(128) comment '活动信息',
   consumeMchId         varchar(32) comment '资金授权商户号',
   status               varchar(32) comment '状态',
   errMsg               varchar(32) comment '错误信息',
   sendListId           varchar(32) comment '微信单号',
   createTime           datetime comment '时间',
   createUser           varchar(32) comment '付款者',
   primary key (id),
   key AK_Key_Type (type),
   key AK_Key_Time (createTime)
);

alter table redPackBill comment '红包账单';

