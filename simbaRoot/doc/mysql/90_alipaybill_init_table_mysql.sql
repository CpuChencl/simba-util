/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/5/25 星期五 14:39:29                       */
/*==============================================================*/


drop table if exists aliPayBill;

/*==============================================================*/
/* Table: aliPayBill                                            */
/*==============================================================*/
create table aliPayBill
(
   id                   bigint not null auto_increment,
   appid                varchar(32) comment '应用ID',
   body                 varchar(128) comment '描述',
   totalAmount          varchar(9) comment '订单总金额',
   subject              varchar(256) comment '标题',
   outTradeNo           varchar(64) comment '商户订单号',
   tradeNo              varchar(64) comment '支付宝交易流水号',
   productCode          varchar(64) comment '产品码',
   goodType             varchar(16) comment '商品主类型',
   storeId              varchar(16) comment '商户门店编号',
   sellId               varchar(64) comment '收款支付宝账号ID',
   timeoutExpress       varchar(16) comment '最晚付款时间',
   status               varchar(64) comment '状态',
   createTime           datetime comment '订单时间',
   primary key (id)
);

alter table aliPayBill comment '阿里支付账单';

