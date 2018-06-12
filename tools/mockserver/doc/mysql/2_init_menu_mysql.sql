insert into menu(id,text,parentID,url,orderNo) values(1000,'权限管理',-1,'',1);
insert into menu(id,text,parentID,url,orderNo) values(1001,'系统管理',-1,'',2);

insert into menu(id,text,parentID,url,orderNo) values(10000,'机构管理',1000,'/org/list',1);
insert into menu(id,text,parentID,url,orderNo) values(10001,'用户管理',1000,'/user/list',2);
insert into menu(id,text,parentID,url,orderNo) values(10002,'角色管理',1000,'/role/list',3);
insert into menu(id,text,parentID,url,orderNo) values(10003,'权限管理',1000,'/permission/list',4);

insert into menu(id,text,parentID,url,orderNo) values(10004,'菜单管理',1001,'/menu/list',5);
insert into menu(id,text,parentID,url,orderNo) values(10005,'业务管理',1001,'/buss/list',6);
insert into menu(id,text,parentID,url,orderNo) values(10007,'注册类型管理',1001,'/registryType/list',8);
insert into menu(id,text,parentID,url,orderNo) values(10008,'注册表管理',1001,'/registryTable/list',9);
insert into menu(id,text,parentID,url,orderNo) values(10017,'任务管理',1001,'/job/list',19);
insert into menu(id,text,parentID,url,orderNo) values(10018,'数据库监控',1001,'/druid/index.html',20);
