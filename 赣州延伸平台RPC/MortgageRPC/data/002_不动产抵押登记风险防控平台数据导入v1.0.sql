
--用户表的其他信息
INSERT INTO "BDC_MRPC"."STAFF" VALUES ('08375a2dff80e821d5a158dd98302b23', 'notebao', null, null, null, null, '2', '28', null, null, null, 'jeecg-boot', TO_DATE('2019-04-10 11:42:57', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-07-09 20:32:51', 'YYYY-MM-DD HH24:MI:SS'), null,'451202');


--月份金额收入统计表，可用于统计月办件量
INSERT INTO "BDC_MRPC"."BDC_MONTHLY_GROWTH_ANALYSIS" VALUES ('1', '2018', '1月', '114758.90', '4426054.19');


--客户表数据，用户表关联，一对多，可用于银行账号名下的多个企业客户
INSERT INTO "BDC_MRPC"."BDC_ORDER_CUSTOMER" VALUES ('15538561502720', '3333', '1', null, null, null, '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', TO_DATE('2019-03-29 18:42:55', 'YYYY-MM-DD HH24:MI:SS'), null, null);


--订单表，可用于业务预约？
INSERT INTO "BDC_MRPC"."BDC_ORDER_MAIN" VALUES ('163e2efcbc6d7d54eb3f8a137da8a75a', 'B100', null, null, '3000', null, 'jeecg-boot', TO_DATE('2019-03-29 18:43:59', 'YYYY-MM-DD HH24:MI:SS'), null, null);


--航班表，用途尚未明确
INSERT INTO "BDC_MRPC"."BDC_ORDER_TICKET" VALUES ('0f0e3a40a215958f807eea08a6e1ac0a', '88', null, '54e739bef5b67569c963c38da52581ec', 'admin', TO_DATE('2019-03-15 16:50:15', 'YYYY-MM-DD HH24:MI:SS'), null, null);


--项目费用表，用途尚未明确
INSERT INTO "BDC_MRPC"."BDC_PROJECT_NATURE_INCOME" VALUES ('1', '市场化-电商业务', '4865.41', '0', '0', '0', '0', '0');


--框架表，存储程序的悲观锁的信息。		
INSERT INTO "BDC_MRPC"."QRTZ_LOCKS" VALUES ('quartzScheduler', 'TRIGGER_ACCESS');

--系统通告表，用途发起通知
INSERT INTO "BDC_MRPC"."SYS_ANNOUNCEMENT" VALUES ('1b714f8ebc3cc33f8b4f906103b6a18d', '5467567', null, null, null, 'admin', null, '2', null, '1', TO_DATE('2019-03-30 12:40:38', 'YYYY-MM-DD HH24:MI:SS'), null, '0', 'admin', TO_DATE('2019-02-26 17:23:26', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-26 17:35:10', 'YYYY-MM-DD HH24:MI:SS'), null);

--用户通告阅读标记表，记录已阅读过通知的用户
INSERT INTO "BDC_MRPC"."SYS_ANNOUNCEMENT_SEND" VALUES ('646c0c405ec643d4dc4160db2446f8ff', '93a9060a1c20e4bf98b3f768a02c2ff9', 'e9ca23d68d884d4ebb19d07889727dae', '0', null, 'admin', TO_DATE('2019-05-17 11:50:56', 'YYYY-MM-DD HH24:MI:SS'), null, null);


--组织机构表，部门表
INSERT INTO "BDC_MRPC"."SYS_DEPART" VALUES ('4f1765520d6346f9bd9c79e2479e5b12', 'c6d7cb4deeac411cb3384b1b31278596', '市场部', null, null, '0', null, '2', 'A01A03', null, null, null, null, null, '0', 'admin', TO_DATE('2019-02-20 17:15:34', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-26 16:36:18', 'YYYY-MM-DD HH24:MI:SS'));



--菜单页面数据
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('ec8d607d0156e198b11853760319c646', '6e73eb3c26099c191bf03852ee1310a1', '安全设置', '/account/settings/security', 'account/settings/Security', null, null, '1', 'SecuritySettings', null, null, null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-26 18:59:52', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('f0675b52d89100ee88472b6800754a08', null, '统计报表', '/report', 'layouts/RouteView', null, null, '0', null, null, '1', '0', 'bar-chart', '1', '0', null, '0', null, 'admin', TO_DATE('2019-04-03 18:32:02', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-19 18:34:13', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('f1cb187abf927c88b89470d08615f5ac', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '数据字典', '/isystem/dict', 'system/DictList', null, null, '1', null, null, '5', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-28 13:54:43', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2018-12-28 15:37:54', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('f23d9bfff4d9aa6b68569ba2cff38415', '540a2936940846cb98114ffb0d145cb8', '标准列表', '/list/basic-list', 'list/StandardList', null, null, '1', null, null, '6', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('f780d0d3083d849ccbdb1b1baee4911d', '5c8042bd6c601270b2bbd9b20bccc68b', '模板管理', '/modules/message/sysMessageTemplateList', 'modules/message/SysMessageTemplateList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-09 11:50:31', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-12 10:16:34', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('fb07ca05a3e13674dbf6d3245956da2e', '540a2936940846cb98114ffb0d145cb8', '搜索列表', '/list/search', 'list/search/SearchLayout', null, '/list/search/article', '1', null, null, '8', '0', null, '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-12 15:09:13', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('fb367426764077dcf94640c843733985', '2a470fc0c3954d9dbb61de6d80846549', '一对多示例', '/jeecg/JeecgOrderMainList', 'jeecg/JeecgOrderMainList', null, null, '1', null, null, '2', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-15 16:24:11', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-18 10:50:14', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('fc810a2267dd183e4ef7c71cc60f4670', '700b7f95165c46cc7a78bf227aa8fed3', '请求追踪', '/monitor/HttpTrace', 'modules/monitor/HttpTrace', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-02 09:46:19', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:37:27', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('fedfbf4420536cacc0218557d263dfea', '6e73eb3c26099c191bf03852ee1310a1', '新消息通知', '/account/settings/notification', 'account/settings/Notification', null, null, '1', 'NotificationSettings', null, null, null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-26 19:02:05', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('00a2a0ae65cdca5e93209cdbde97cbe6', '2e42e3835c2b44ec9f7bc26c146ee531', '成功', '/result/success', 'result/Success', null, null, '1', null, null, '1', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('020b06793e4de2eee0007f603000c769', 'f0675b52d89100ee88472b6800754a08', 'ViserChartDemo', '/report/ViserChartDemo', 'jeecg/report/ViserChartDemo', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-03 19:08:53', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-03 19:08:53', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('024f1fd1283dc632458976463d8984e1', '700b7f95165c46cc7a78bf227aa8fed3', 'Tomcat信息', '/monitor/TomcatInfo', 'modules/monitor/TomcatInfo', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-02 09:44:29', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-07 15:19:10', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('05b3c82ddb2536a4a5ee1a4c46b5abef', '540a2936940846cb98114ffb0d145cb8', '用户列表', '/list/user-list', 'list/UserList', null, null, '1', null, null, '3', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('0620e402857b8c5b605e1ad9f4b89350', '2a470fc0c3954d9dbb61de6d80846549', '异步树列表Demo', '/jeecg/JeecgTreeTable', 'jeecg/JeecgTreeTable', null, null, '1', null, '0', '3', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-05-13 17:30:30', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-13 17:32:17', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', '1');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('078f9558cdeab239aecb2bda1a8ed0d1', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（文章）', '/list/search/article', 'list/TableList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-12 14:00:34', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-12 14:17:54', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('08e6b9dc3c04489c8e1ff2ce6f105aa4', null, '系统监控', '/dashboard3', 'layouts/RouteView', null, null, '0', null, null, '6', '0', 'dashboard', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-31 22:19:58', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('13212d3416eb690c2e1d5033166ff47a', '2e42e3835c2b44ec9f7bc26c146ee531', '失败', '/result/fail', 'result/Error', null, null, '1', null, null, '2', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('1367a93f2c410b169faa7abcbad2f77c', '6e73eb3c26099c191bf03852ee1310a1', '基本设置', '/account/settings/base', 'account/settings/BaseSetting', null, null, '1', 'BaseSettings', null, null, '0', null, '1', '1', null, '1', null, null, TO_DATE('2018-12-26 18:58:35', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-20 12:57:31', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('190c2b43bec6a5f7a4194a85db67d96a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色维护', '/isystem/roleUserList', 'system/RoleUserList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-17 15:13:56', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('1a0811914300741f4e11838ff37a1d3a', '3f915b2769fc80648e92d04e84ca059d', '手机号禁用', null, null, null, null, '2', 'user:form:phone', '2', '1', '0', null, '0', '1', null, '0', null, 'admin', TO_DATE('2019-05-11 17:19:30', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-11 18:00:22', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', '1');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('200006f0edf145a2b50eacca07585451', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（应用）', '/list/search/application', 'list/TableList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-12 14:02:51', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-12 14:14:01', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('265de841c58907954b8877fb85212622', '2a470fc0c3954d9dbb61de6d80846549', '图片拖拽排序', '/jeecg/imgDragSort', 'jeecg/ImgDragSort', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-25 10:43:08', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-25 10:46:26', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('277bfabef7d76e89b33062b16a9a5020', 'e3c13679c73a4f829bcff2aba8fd68b1', '基础表单', '/form/base-form', 'form/BasicForm', null, null, '1', null, null, '1', '0', null, '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-26 17:02:08', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('2a470fc0c3954d9dbb61de6d80846549', null, '常见案例', '/jeecg', 'layouts/RouteView', null, null, '0', null, null, '7', '0', 'qrcode', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:46:42', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('2aeddae571695cd6380f6d6d334d6e7d', 'f0675b52d89100ee88472b6800754a08', '布局统计报表', '/report/ArchivesStatisticst', 'jeecg/report/ArchivesStatisticst', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-03 18:32:48', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('2dbbafa22cda07fa5d169d741b81fe12', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '在线文档', '{{ window._CONFIG[''domianURL''] }}/swagger-ui.html#/', 'layouts/IframePageView', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-01-30 10:00:01', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-23 19:44:43', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('2e42e3835c2b44ec9f7bc26c146ee531', null, '结果页', '/result', 'layouts/PageView', null, null, '0', null, null, '8', '0', 'check-circle-o', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:46:56', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('339329ed54cf255e1f9392e84f136901', '2a470fc0c3954d9dbb61de6d80846549', 'helloworld', '/jeecg/helloworld', 'jeecg/helloworld', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-15 16:24:56', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/isystem/user', 'system/UserList', null, null, '1', null, null, '1', '0', null, '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-16 11:20:33', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('3fac0d3c9cd40fa53ab70d4c583821f8', '2a470fc0c3954d9dbb61de6d80846549', '分屏', '/jeecg/splitPanel', 'jeecg/SplitPanel', null, null, '1', null, null, '6', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-25 16:27:06', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('4148ec82b6acd69f470bea75fe41c357', '2a470fc0c3954d9dbb61de6d80846549', '单表模型示例', '/jeecg/jeecgDemoList', 'jeecg/JeecgDemoList', 'DemoList', null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, null, TO_DATE('2018-12-28 15:57:30', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-15 16:24:37', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('418964ba087b90a84897b62474496b93', '540a2936940846cb98114ffb0d145cb8', '查询表格', '/list/query-list', 'list/TableList', null, null, '1', null, null, '1', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('4356a1a67b564f0988a484f5531fd4d9', '2a470fc0c3954d9dbb61de6d80846549', '内嵌Table', '/jeecg/TableExpandeSub', 'jeecg/TableExpandeSub', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-04 22:48:13', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('45c966826eeff4c99b8f8ebfe74511fc', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '部门管理', '/isystem/depart', 'system/DepartList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-01-29 18:47:40', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-07 19:23:16', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('4875ebe289344e14844d8e3ea1edd73f', null, '详情页', '/profile', 'layouts/RouteView', null, null, '0', null, null, '8', '0', 'profile', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:46:48', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('4f66409ef3bbd69c1d80469d6e2a885e', '6e73eb3c26099c191bf03852ee1310a1', '账户绑定', '/account/settings/binding', 'account/settings/Binding', null, null, '1', 'BindingSettings', null, null, null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-26 19:01:20', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('4f84f9400e5e92c95f05b554724c2b58', '540a2936940846cb98114ffb0d145cb8', '角色列表', '/list/role-list', 'list/RoleList', null, null, '1', null, null, '4', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('53a9230444d33de28aa11cc108fb1dba', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '我的消息', '/isps/userAnnouncement', 'system/UserAnnouncementList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-19 10:16:00', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('540a2936940846cb98114ffb0d145cb8', null, '列表页', '/list', 'layouts/PageView', null, '/list/query-list', '0', null, null, '9', '0', 'table', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-31 22:20:20', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/permission', 'system/PermissionList', null, null, '1', null, null, '3', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('58857ff846e61794c69208e9d3a85466', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '日志管理', '/isystem/log', 'system/LogList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, null, TO_DATE('2018-12-26 10:11:18', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:38:17', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('58b9204feaf07e47284ddb36cd2d8468', '2a470fc0c3954d9dbb61de6d80846549', '图片翻页', '/jeecg/imgTurnPage', 'jeecg/ImgTurnPage', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-25 11:36:42', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('5c2f42277948043026b7a14692456828', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '我的部门', '/isystem/departUserList', 'system/DepartUserList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-17 15:12:24', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('5c8042bd6c601270b2bbd9b20bccc68b', null, '消息中心', '/message', 'layouts/RouteView', null, null, '0', null, null, '6', '0', 'message', '1', '0', null, '0', null, 'admin', TO_DATE('2019-04-09 11:05:04', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-11 19:47:54', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('6531cf3421b1265aeeeabaab5e176e6d', 'e3c13679c73a4f829bcff2aba8fd68b1', '分步表单', '/form/step-form', 'form/stepForm/StepForm', null, null, '1', null, null, '2', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('655563cd64b75dcf52ef7bcdd4836953', '2a470fc0c3954d9dbb61de6d80846549', '图片预览', '/jeecg/ImagPreview', 'jeecg/ImagPreview', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-17 11:18:45', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('65a8f489f25a345836b7f44b1181197a', 'c65321e57b7949b7a975313220de0422', '403', '/exception/403', 'exception/403', null, null, '1', null, null, '1', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('6ad53fd1b220989a8b71ff482d683a5a', '2a470fc0c3954d9dbb61de6d80846549', '一对多Tab示例', '/jeecg/tablist/JeecgOrderDMainList', 'jeecg/tablist/JeecgOrderDMainList', null, null, '1', null, null, '2', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-20 14:45:09', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-21 16:26:21', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('6e73eb3c26099c191bf03852ee1310a1', '717f6bee46f44a3897eca9abd6e2ec44', '个人设置', '/account/settings/base', 'account/settings/Index', null, null, '1', null, null, '2', '1', null, '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-19 09:41:05', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('700b7f95165c46cc7a78bf227aa8fed3', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '性能监控', '/monitor', 'layouts/RouteView', null, null, '1', null, null, '0', '0', null, '1', '0', null, '0', null, 'admin', TO_DATE('2019-04-02 11:34:34', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-05 17:49:47', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('717f6bee46f44a3897eca9abd6e2ec44', null, '个人页', '/account', 'layouts/RouteView', null, null, '0', null, null, '9', '0', 'user', '1', '0', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('73678f9daa45ed17a3674131b03432fb', '540a2936940846cb98114ffb0d145cb8', '权限列表', '/list/permission-list', 'list/PermissionList', null, null, '1', null, null, '5', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('7593c9e3523a17bca83b8d7fe8a34e58', '3f915b2769fc80648e92d04e84ca059d', '添加用户按钮', null, null, null, null, '2', 'user:add', '1', '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-03-16 11:20:33', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-17 18:31:25', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', '1');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('7960961b0063228937da5fa8dd73d371', '2a470fc0c3954d9dbb61de6d80846549', 'JEditableTable示例', '/jeecg/JEditableTable', 'jeecg/JeecgEditableTableExample', null, null, '1', null, null, '7', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-03-22 15:22:18', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('7ac9eb9ccbde2f7a033cd4944272bf1e', '540a2936940846cb98114ffb0d145cb8', '卡片列表', '/list/card', 'list/CardList', null, null, '1', null, null, '7', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('841057b8a1bef8f6b4b20f9a618a7fa6', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '数据日志', '/sys/dataLog-list', 'system/DataLogList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-03-11 19:26:49', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-12 11:40:47', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('882a73768cfd7f78f3a37584f7299656', '6e73eb3c26099c191bf03852ee1310a1', '个性化设置', '/account/settings/custom', 'account/settings/Custom', null, null, '1', 'CustomSettings', null, null, null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-26 19:00:46', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2018-12-26 21:13:25', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('8b3bff2eee6f1939147f5c68292a1642', '700b7f95165c46cc7a78bf227aa8fed3', '服务器信息', '/monitor/SystemInfo', 'modules/monitor/SystemInfo', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-02 11:39:19', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 15:40:02', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('8d1ebd663688965f1fd86a2f0ead3416', '700b7f95165c46cc7a78bf227aa8fed3', 'Redis监控', '/monitor/redis/info', 'modules/monitor/RedisInfo', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-02 13:11:33', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-07 15:18:54', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('8fb8172747a78756c11916216b8b8066', '717f6bee46f44a3897eca9abd6e2ec44', '工作台', '/dashboard/workplace', 'dashboard/Workplace', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:45:02', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('944abf0a8fc22fe1f1154a389a574154', '5c8042bd6c601270b2bbd9b20bccc68b', '消息管理', '/modules/message/sysMessageList', 'modules/message/SysMessageList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-09 11:27:53', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-09 19:31:23', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('9502685863ab87f0ad1134142788a385', null, '首页', '/dashboard/analysis', 'dashboard/Analysis', null, null, '0', null, null, '0', '0', 'home', '1', '1', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-29 11:04:13', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('97c8629abc7848eccdb6d77c24bb3ebb', '700b7f95165c46cc7a78bf227aa8fed3', '磁盘监控', '/monitor/Disk', 'modules/monitor/DiskMonitoring', null, null, '1', null, null, '6', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-25 14:30:06', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-05 14:37:14', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('9a90363f216a6a08f32eecb3f0bf12a3', '2a470fc0c3954d9dbb61de6d80846549', '常用选择组件', '/jeecg/SelectDemo', 'jeecg/SelectDemo', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-03-19 11:19:05', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-10 15:36:50', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('9cb91b8851db0cf7b19d7ecc2a8193dd', '1939e035e803a99ceecb6f5563570fb2', '我的任务表单', '/modules/bpm/task/form/FormModule', 'modules/bpm/task/form/FormModule', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-03-08 16:49:05', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-08 18:37:56', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('a400e4f4d54f79bf5ce160ae432231af', '2a470fc0c3954d9dbb61de6d80846549', '百度', 'http://www.baidu.com', 'layouts/IframePageView', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-01-29 19:44:06', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-15 16:25:02', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('ae4fed059f67086fd52a73d913cf473d', '540a2936940846cb98114ffb0d145cb8', '内联编辑表格', '/list/edit-table', 'list/TableInnerEditList', null, null, '1', null, null, '2', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('aedbf679b5773c1f25e9f7b10111da73', '08e6b9dc3c04489c8e1ff2ce6f105aa4', 'SQL监控', '{{ window._CONFIG[''domianURL''] }}/druid/', 'layouts/IframePageView', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-01-30 09:43:22', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-23 19:00:46', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('b1cb0a3fedf7ed0e4653cb5a229837ee', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '定时任务', '/isystem/QuartzJobList', 'system/QuartzJobList', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, null, TO_DATE('2019-01-03 09:38:52', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 10:24:13', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('b3c824fc22bd953e2eb16ae6914ac8f9', '4875ebe289344e14844d8e3ea1edd73f', '高级详情页', '/profile/advanced', 'profile/advanced/Advanced', null, null, '1', null, null, '2', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('b4dfc7d5dd9e8d5b6dd6d4579b1aa559', 'c65321e57b7949b7a975313220de0422', '500', '/exception/500', 'exception/500', null, null, '1', null, null, '3', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('b6bcee2ccc854052d3cc3e9c96d90197', '71102b3b87fb07e5527bbd2c530dd90a', '加班申请', '/modules/extbpm/joa/JoaOvertimeList', 'modules/extbpm/joa/JoaOvertimeList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-03 15:33:10', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-03 15:34:48', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('c431130c0bc0ec71b0a5be37747bb36a', '2a470fc0c3954d9dbb61de6d80846549', '一对多JEditable', '/jeecg/JeecgOrderMainListForJEditableTable', 'jeecg/JeecgOrderMainListForJEditableTable', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-03-29 10:51:59', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-04 20:09:39', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('c65321e57b7949b7a975313220de0422', null, '异常页', '/exception', 'layouts/RouteView', null, null, '0', null, null, '8', null, 'warning', '1', '0', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('c6cf95444d80435eb37b2f9db3971ae6', '2a470fc0c3954d9dbb61de6d80846549', '数据回执模拟', '/jeecg/InterfaceTest', 'jeecg/InterfaceTest', null, null, '1', null, null, '6', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-19 16:02:23', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-21 16:25:45', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('cc50656cf9ca528e6f2150eba4714ad2', '4875ebe289344e14844d8e3ea1edd73f', '基础详情页', '/profile/basic', 'profile/basic/Index', null, null, '1', null, null, '1', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('d07a2c87a451434c99ab06296727ec4f', '700b7f95165c46cc7a78bf227aa8fed3', 'JVM信息', '/monitor/JvmInfo', 'modules/monitor/JvmInfo', null, null, '1', null, null, '4', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-01 23:07:48', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-02 11:37:16', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('d2bbf9ebca5a8fa2e227af97d2da7548', 'c65321e57b7949b7a975313220de0422', '404', '/exception/404', 'exception/404', null, null, '1', null, null, '2', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', null, '系统管理', '/isystem', 'layouts/RouteView', null, null, '0', null, null, '4', '0', 'setting', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-31 22:19:52', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('d86f58e7ab516d3bc6bfb1fe10585f97', '717f6bee46f44a3897eca9abd6e2ec44', '个人中心', '/account/center', 'account/center/Index', null, null, '1', null, null, '1', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('de13e0f6328c069748de7399fcc1dbbd', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（项目）', '/list/search/project', 'list/TableList', null, null, '1', null, null, '1', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-12 14:01:40', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-02-12 14:14:18', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('e08cb190ef230d5d4f03824198773950', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '系统通告', '/isystem/annountCement', 'system/SysAnnouncementList', null, null, '1', 'annountCement', null, '6', null, null, '1', '1', null, null, null, null, TO_DATE('2019-01-02 17:23:01', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2019-01-02 17:31:23', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('e1979bb53e9ea51cecc74d86fd9d2f64', '2a470fc0c3954d9dbb61de6d80846549', 'PDF预览', '/jeecg/jeecgPdfView', 'jeecg/JeecgPdfView', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-04-25 10:39:35', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('e3c13679c73a4f829bcff2aba8fd68b1', null, '表单页', '/form', 'layouts/PageView', null, null, '0', null, null, '9', '0', 'form', '1', '0', null, '0', null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-31 22:20:14', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('e5973686ed495c379d829ea8b2881fc6', 'e3c13679c73a4f829bcff2aba8fd68b1', '高级表单', '/form/advanced-form', 'form/advancedForm/AdvancedForm', null, null, '1', null, null, '3', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('e6bfd1fcabfd7942fdd05f076d1dad38', '2a470fc0c3954d9dbb61de6d80846549', '打印测试', '/jeecg/PrintDemo', 'jeecg/PrintDemo', null, null, '1', null, null, '3', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-02-19 15:58:48', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-07 20:14:39', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('e8af452d8948ea49d37c934f5100ae6a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/isystem/role', 'system/RoleList', null, null, '1', null, null, '2', null, null, '1', '1', null, null, null, null, TO_DATE('2018-12-25 20:34:38', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0', '0', null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION" VALUES ('ebb9d82ea16ad864071158e0c449d186', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '分类字典', '/isys/category', 'system/SysCategoryList', null, null, '1', null, '1', '5', '0', null, '1', '1', null, '0', null, 'admin', TO_DATE('2019-05-29 18:48:07', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-29 18:48:27', 'YYYY-MM-DD HH24:MI:SS'), '0', '0', '1');


--菜单规则数据
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('32b62cb04d6c788d9d92e3ff5e66854e', '8d4683aacaa997ab86b966b464360338', '000', '00', '!=', '00', '1', TO_DATE('2019-04-02 18:36:08', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('40283181614231d401614234fe670003', '40283181614231d401614232cd1c0001', 'createBy', 'createBy', '=', '#{sys_user_code}', '1', TO_DATE('2018-01-29 21:57:04', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('4028318161424e730161424fca6f0004', '4028318161424e730161424f61510002', 'createBy', 'createBy', '=', '#{sys_user_code}', '1', TO_DATE('2018-01-29 22:26:20', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402880e6487e661a01487e732c020005', '402889fb486e848101486e93a7c80014', 'SYS_ORG_CODE', 'SYS_ORG_CODE', 'LIKE', '010201%', '1', TO_DATE('2014-09-16 20:32:30', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402880e6487e661a01487e8153ee0007', '402889fb486e848101486e93a7c80014', 'create_by', 'create_by', null, '#{SYS_USER_CODE}', '1', TO_DATE('2014-09-16 20:47:57', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402880ec5ddec439015ddf9225060038', '40288088481d019401481d2fcebf000d', '复杂关系', null, 'USE_SQL_RULES', 'name like ''%张%'' or age > 10', '1', null, null, TO_DATE('2017-08-14 15:10:25', 'YYYY-MM-DD HH24:MI:SS'), 'demo');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402880ec5ddfdd26015ddfe3e0570011', '4028ab775dca0d1b015dca3fccb60016', '复杂sql配置', null, 'USE_SQL_RULES', 'table_name like ''%test%'' or is_tree = ''Y''', '1', null, null, TO_DATE('2017-08-14 16:38:55', 'YYYY-MM-DD HH24:MI:SS'), 'demo');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402880f25b1e2ac7015b1e5fdebc0012', '402880f25b1e2ac7015b1e5cdc340010', '只能看自己数据', 'create_by', '=', '#{sys_user_code}', '1', TO_DATE('2017-03-30 16:40:51', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881875b19f141015b19f8125e0014', '40288088481d019401481d2fcebf000d', '可看下属业务数据', 'sys_org_code', 'LIKE', '#{sys_org_code}', '1', null, null, TO_DATE('2017-08-14 15:04:32', 'YYYY-MM-DD HH24:MI:SS'), 'demo');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881e45394d66901539500a4450001', '402881e54df73c73014df75ab670000f', 'sysCompanyCode', 'sysCompanyCode', '=', '#{SYS_COMPANY_CODE}', '1', TO_DATE('2016-03-21 01:09:21', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881e45394d6690153950177cb0003', '402881e54df73c73014df75ab670000f', 'sysOrgCode', 'sysOrgCode', '=', '#{SYS_ORG_CODE}', '1', TO_DATE('2016-03-21 01:10:15', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881e56266f43101626727aff60067', '402881e56266f43101626724eb730065', '销售自己看自己的数据', 'createBy', '=', '#{sys_user_code}', '1', TO_DATE('2018-03-27 19:11:16', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881e56266f4310162672fb1a70082', '402881e56266f43101626724eb730065', '销售经理看所有下级数据', 'sysOrgCode', 'LIKE', '#{sys_org_code}', '1', TO_DATE('2018-03-27 19:20:01', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881e56266f431016267387c9f0088', '402881e56266f43101626724eb730065', '只看金额大于1000的数据', 'money', '>=', '1000', '1', TO_DATE('2018-03-27 19:29:37', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402881f3650de25101650dfb5a3a0010', '402881e56266f4310162671d62050044', '22', null, 'USE_SQL_RULES', '22', '1', TO_DATE('2018-08-06 14:45:01', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402889fb486e848101486e913cd6000b', '402889fb486e848101486e8e2e8b0007', 'userName', 'userName', '=', 'admin', '1', TO_DATE('2014-09-13 18:31:25', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402889fb486e848101486e98d20d0016', '402889fb486e848101486e93a7c80014', 'title', 'title', '=', '12', '1', null, null, TO_DATE('2014-09-13 22:18:22', 'YYYY-MM-DD HH24:MI:SS'), 'scott');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('402889fe47fcb29c0147fcb6b6220001', '8a8ab0b246dc81120146dc8180fe002b', '12', '12', '>', '12', '1', TO_DATE('2014-08-22 15:55:38', 'YYYY-MM-DD HH24:MI:SS'), '8a8ab0b246dc81120146dc8181950052', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('4028ab775dca0d1b015dca4183530018', '4028ab775dca0d1b015dca3fccb60016', '表名限制', 'isDbSynch', '=', 'Y', '1', null, null, TO_DATE('2017-08-14 16:43:45', 'YYYY-MM-DD HH24:MI:SS'), 'demo');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('4028ef815595a881015595b0ccb60001', '40288088481d019401481d2fcebf000d', '限只能看自己', 'create_by', '=', '#{sys_user_code}', '1', null, null, TO_DATE('2017-08-14 15:03:56', 'YYYY-MM-DD HH24:MI:SS'), 'demo');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('4028ef81574ae99701574aed26530005', '4028ef81574ae99701574aeb97bd0003', '用户名', 'userName', '!=', 'admin', '1', TO_DATE('2016-09-21 12:07:18', 'YYYY-MM-DD HH24:MI:SS'), 'admin', null, null);
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('53609e1854f4a87eb23ed23a18a1042c', '4148ec82b6acd69f470bea75fe41c357', '只看当前部门数据', 'sysOrgCode', '=', '#{sys_org_code}', '1', TO_DATE('2019-05-11 19:40:39', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-11 19:40:50', 'YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('a7d661ef5ac168b2b162420c6804dac5', '4148ec82b6acd69f470bea75fe41c357', '只看自己的数据', 'createBy', '=', '#{sys_user_code}', '1', TO_DATE('2019-05-11 19:19:05', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-11 19:24:58', 'YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO "BDC_MRPC"."SYS_PERMISSION_DATA_RULE" VALUES ('f852d85d47f224990147f2284c0c0005', null, '小于', 'test', '<=', '11', '1', TO_DATE('2014-08-20 14:43:52', 'YYYY-MM-DD HH24:MI:SS'), '8a8ab0b246dc81120146dc8181950052', null, null);

--任务表
INSERT INTO "BDC_MRPC"."SYS_QUARTZ_JOB" VALUES ('df26ecacf0f75d219d746750fe84bbee', null, null, '0', 'admin', TO_DATE('2019-01-19 15:09:41', 'YYYY-MM-DD HH24:MI:SS'), 'org.jeecg.modules.quartz.job.SampleParamJob', '0/1 * * * * ?', 'scott', '带参测试 后台将每隔1秒执行输出日志', '-1');

--角色
INSERT INTO "BDC_MRPC"."SYS_ROLE" VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', null, TO_DATE('2018-12-21 18:03:39', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-20 11:40:26', 'YYYY-MM-DD HH24:MI:SS'),'');

--角色权限表，只留下管理员的权限数据
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('7ca833caa5eac837b7200d8b6de8b2e3', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('00b82058779cca5106fbb84783534c9b', 'f6817f48af4fb3af11b9e8bf182f618b', '4148ec82b6acd69f470bea75fe41c357', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('0254c0b25694ad5479e6d6935bbc176e', 'f6817f48af4fb3af11b9e8bf182f618b', '944abf0a8fc22fe1f1154a389a574154', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('09bd4fc30ffe88c4a44ed3868f442719', 'f6817f48af4fb3af11b9e8bf182f618b', 'e6bfd1fcabfd7942fdd05f076d1dad38', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('0c2d2db76ee3aa81a4fe0925b0f31365', 'f6817f48af4fb3af11b9e8bf182f618b', '024f1fd1283dc632458976463d8984e1', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('0c6b8facbb1cc874964c87a8cf01e4b1', 'f6817f48af4fb3af11b9e8bf182f618b', '841057b8a1bef8f6b4b20f9a618a7fa6', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('0c6e1075e422972083c3e854d9af7851', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('0e139e6c1b5b73eee81381ddf0b5a9f3', 'f6817f48af4fb3af11b9e8bf182f618b', '277bfabef7d76e89b33062b16a9a5020', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('0f861cb988fdc639bb1ab943471f3a72', 'f6817f48af4fb3af11b9e8bf182f618b', '97c8629abc7848eccdb6d77c24bb3ebb', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('126ea9faebeec2b914d6d9bef957afb6', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('154edd0599bd1dc2c7de220b489cd1e2', 'f6817f48af4fb3af11b9e8bf182f618b', '7ac9eb9ccbde2f7a033cd4944272bf1e', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('165acd6046a0eaf975099f46a3c898ea', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('1664b92dff13e1575e3a929caa2fa14d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('1c1dbba68ef1817e7fb19c822d2854e8', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb367426764077dcf94640c843733985', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('1e47db875601fd97723254046b5bba90', 'f6817f48af4fb3af11b9e8bf182f618b', 'baf16b7174bd821b6bab23fa9abb200d', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('20e53c87a785688bdc0a5bb6de394ef1', 'f6817f48af4fb3af11b9e8bf182f618b', '540a2936940846cb98114ffb0d145cb8', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('25491ecbd5a9b34f09c8bc447a10ede1', 'f6817f48af4fb3af11b9e8bf182f618b', 'd07a2c87a451434c99ab06296727ec4f', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('2779cdea8367fff37db26a42c1a1f531', 'f6817f48af4fb3af11b9e8bf182f618b', 'fef097f3903caf3a3c3a6efa8de43fbb', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('29fb6b0ad59a7e911c8d27e0bdc42d23', 'f6817f48af4fb3af11b9e8bf182f618b', '9a90363f216a6a08f32eecb3f0bf12a3', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('2ad37346c1b83ddeebc008f6987b2227', 'f6817f48af4fb3af11b9e8bf182f618b', '8d1ebd663688965f1fd86a2f0ead3416', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('38a2e55db0960262800576e34b3af44c', 'f6817f48af4fb3af11b9e8bf182f618b', '5c2f42277948043026b7a14692456828', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('3b1886f727ac503c93fecdd06dcb9622', 'f6817f48af4fb3af11b9e8bf182f618b', 'c431130c0bc0ec71b0a5be37747bb36a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('3de2a60c7e42a521fecf6fcc5cb54978', 'f6817f48af4fb3af11b9e8bf182f618b', '2d83d62bd2544b8994c8f38cf17b0ddf', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('3e4e38f748b8d87178dd62082e5b7b60', 'f6817f48af4fb3af11b9e8bf182f618b', '7960961b0063228937da5fa8dd73d371', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('3f1d04075e3c3254666a4138106a4e51', 'f6817f48af4fb3af11b9e8bf182f618b', '3fac0d3c9cd40fa53ab70d4c583821f8', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('439568ff7db6f329bf6dd45b3dfc9456', 'f6817f48af4fb3af11b9e8bf182f618b', '7593c9e3523a17bca83b8d7fe8a34e58', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('444126230885d5d38b8fa6072c9f43f8', 'f6817f48af4fb3af11b9e8bf182f618b', 'f780d0d3083d849ccbdb1b1baee4911d', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('445656dd187bd8a71605f4bbab1938a3', 'f6817f48af4fb3af11b9e8bf182f618b', '020b06793e4de2eee0007f603000c769', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('455cdb482457f529b79b479a2ff74427', 'f6817f48af4fb3af11b9e8bf182f618b', 'e1979bb53e9ea51cecc74d86fd9d2f64', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('4dab5a06acc8ef3297889872caa74747', 'f6817f48af4fb3af11b9e8bf182f618b', 'ffb423d25cc59dcd0532213c4a518261', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('4e0a37ed49524df5f08fc6593aee875c', 'f6817f48af4fb3af11b9e8bf182f618b', 'f23d9bfff4d9aa6b68569ba2cff38415', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('4ea403fc1d19feb871c8bdd9f94a4ecc', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('4f254549d9498f06f4cc9b23f3e2c070', 'f6817f48af4fb3af11b9e8bf182f618b', '93d5cfb4448f11e9916698e7f462b4b6', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('4f2fd4a190db856e21476de2704bbd99', 'f6817f48af4fb3af11b9e8bf182f618b', '1a0811914300741f4e11838ff37a1d3a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('504e326de3f03562cdd186748b48a8c7', 'f6817f48af4fb3af11b9e8bf182f618b', '027aee69baee98a0ed2e01806e89c891', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('51b11ce979730f8ce8606da16e4d69bb', 'f6817f48af4fb3af11b9e8bf182f618b', 'e8af452d8948ea49d37c934f5100ae6a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('520b5989e6fe4a302a573d4fee12a40a', 'f6817f48af4fb3af11b9e8bf182f618b', '6531cf3421b1265aeeeabaab5e176e6d', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('54fdf85e52807bdb32ce450814abc256', 'f6817f48af4fb3af11b9e8bf182f618b', 'cc50656cf9ca528e6f2150eba4714ad2', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('5d230e6cd2935c4117f6cb9a7a749e39', 'f6817f48af4fb3af11b9e8bf182f618b', 'fc810a2267dd183e4ef7c71cc60f4670', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('5de6871fadb4fe1cdd28989da0126b07', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160a3432231af', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('5e4015a9a641cbf3fb5d28d9f885d81a', 'f6817f48af4fb3af11b9e8bf182f618b', '2dbbafa22cda07fa5d169d741b81fe12', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('60eda4b4db138bdb47edbe8e10e71675', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb07ca05a3e13674dbf6d3245956da2e', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('61835e48f3e675f7d3f5c9dd3a10dcf3', 'f6817f48af4fb3af11b9e8bf182f618b', 'f0675b52d89100ee88472b6800754a08', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('660fbc40bcb1044738f7cabdf1708c28', 'f6817f48af4fb3af11b9e8bf182f618b', 'b3c824fc22bd953e2eb16ae6914ac8f9', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('66b202f8f84fe766176b3f51071836ef', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('6c74518eb6bb9a353f6a6c459c77e64b', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('6daddafacd7eccb91309530c17c5855d', 'f6817f48af4fb3af11b9e8bf182f618b', 'edfa74d66e8ea63ea432c2910837b150', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('6fb4c2142498dd6d5b6c014ef985cb66', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('7413acf23b56c906aedb5a36fb75bd3a', 'f6817f48af4fb3af11b9e8bf182f618b', 'a4fc7b64b01a224da066bb16230f9c5a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('76a54a8cc609754360bf9f57e7dbb2db', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('84eac2f113c23737128fb099d1d1da89', 'f6817f48af4fb3af11b9e8bf182f618b', '03dc3d93261dda19fc86dd7ca486c6cf', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('86060e2867a5049d8a80d9fe5d8bc28b', 'f6817f48af4fb3af11b9e8bf182f618b', '765dd244f37b804e3d00f475fd56149b', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('884f147c20e003cc80ed5b7efa598cbe', 'f6817f48af4fb3af11b9e8bf182f618b', 'e5973686ed495c379d829ea8b2881fc6', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('8b09925bdc194ab7f3559cd3a7ea0507', 'f6817f48af4fb3af11b9e8bf182f618b', 'ebb9d82ea16ad864071158e0c449d186', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('8d154c2382a8ae5c8d1b84bd38df2a93', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('8dd64f65a1014196078d0882f767cd85', 'f6817f48af4fb3af11b9e8bf182f618b', 'e3c13679c73a4f829bcff2aba8fd68b1', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('8e3dc1671abad4f3c83883b194d2e05a', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('905bf419332ebcb83863603b3ebe30f0', 'f6817f48af4fb3af11b9e8bf182f618b', '8fb8172747a78756c11916216b8b8066', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('9380121ca9cfee4b372194630fce150e', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('94911fef73a590f6824105ebf9b6cab3', 'f6817f48af4fb3af11b9e8bf182f618b', '8b3bff2eee6f1939147f5c68292a1642', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('9700d20dbc1ae3cbf7de1c810b521fe6', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('987c23b70873bd1d6dca52f30aafd8c2', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('9b2ad767f9861e64a20b097538feafd3', 'f6817f48af4fb3af11b9e8bf182f618b', '73678f9daa45ed17a3674131b03432fb', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('9d980ec0489040e631a9c24a6af42934', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('a034ed7c38c996b880d3e78f586fe0ae', 'f6817f48af4fb3af11b9e8bf182f618b', 'c89018ea6286e852b424466fd92a2ffc', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('a307a9349ad64a2eff8ab69582fa9be4', 'f6817f48af4fb3af11b9e8bf182f618b', '0620e402857b8c5b605e1ad9f4b89350', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('a5d25fdb3c62904a8474182706ce11a0', 'f6817f48af4fb3af11b9e8bf182f618b', '418964ba087b90a84897b62474496b93', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('acacce4417e5d7f96a9c3be2ded5b4be', 'f6817f48af4fb3af11b9e8bf182f618b', 'f9d3f4f27653a71c52faa9fb8070fbe7', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('af60ac8fafd807ed6b6b354613b9ccbc', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('b0c8a20800b8bf1ebdd7be473bceb44f', 'f6817f48af4fb3af11b9e8bf182f618b', '58b9204feaf07e47284ddb36cd2d8468', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('b128ebe78fa5abb54a3a82c6689bdca3', 'f6817f48af4fb3af11b9e8bf182f618b', 'aedbf679b5773c1f25e9f7b10111da73', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('b21b07951bb547b09cc85624a841aea0', 'f6817f48af4fb3af11b9e8bf182f618b', '4356a1a67b564f0988a484f5531fd4d9', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('b64c4ab9cd9a2ea8ac1e9db5fb7cf522', 'f6817f48af4fb3af11b9e8bf182f618b', '2aeddae571695cd6380f6d6d334d6e7d', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('bbec16ad016efec9ea2def38f4d3d9dc', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('bea2986432079d89203da888d99b3f16', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('c09373ebfc73fb5740db5ff02cba4f91', 'f6817f48af4fb3af11b9e8bf182f618b', '339329ed54cf255e1f9392e84f136901', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('c56fb1658ee5f7476380786bf5905399', 'f6817f48af4fb3af11b9e8bf182f618b', 'de13e0f6328c069748de7399fcc1dbbd', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('c6fee38d293b9d0596436a0cbd205070', 'f6817f48af4fb3af11b9e8bf182f618b', '4f84f9400e5e92c95f05b554724c2b58', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('c90b0b01c7ca454d2a1cb7408563e696', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('cf1feb1bf69eafc982295ad6c9c8d698', 'f6817f48af4fb3af11b9e8bf182f618b', 'a2b11669e98c5fe54a53c3e3c4f35d14', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('cf2ef620217673e4042f695743294f01', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('cf43895aef7fc684669483ab00ef2257', 'f6817f48af4fb3af11b9e8bf182f618b', '700b7f95165c46cc7a78bf227aa8fed3', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('d281a95b8f293d0fa2a136f46c4e0b10', 'f6817f48af4fb3af11b9e8bf182f618b', '5c8042bd6c601270b2bbd9b20bccc68b', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('d37ad568e26f46ed0feca227aa9c2ffa', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('d3ddcacee1acdfaa0810618b74e38ef2', 'f6817f48af4fb3af11b9e8bf182f618b', 'c6cf95444d80435eb37b2f9db3971ae6', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('d83282192a69514cfe6161b3087ff962', 'f6817f48af4fb3af11b9e8bf182f618b', '53a9230444d33de28aa11cc108fb1dba', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('d8a5c9079df12090e108e21be94b4fd7', 'f6817f48af4fb3af11b9e8bf182f618b', '078f9558cdeab239aecb2bda1a8ed0d1', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('dbc5dd836d45c5bc7bc94b22596ab956', 'f6817f48af4fb3af11b9e8bf182f618b', '1939e035e803a99ceecb6f5563570fb2', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('dc83bb13c0e8c930e79d28b2db26f01f', 'f6817f48af4fb3af11b9e8bf182f618b', '63b551e81c5956d5c861593d366d8c57', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('dc8fd3f79bd85bd832608b42167a1c71', 'f6817f48af4fb3af11b9e8bf182f618b', '91c23960fab49335831cf43d820b0a61', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('de82e89b8b60a3ea99be5348f565c240', 'f6817f48af4fb3af11b9e8bf182f618b', '56ca78fe0f22d815fabc793461af67b8', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('de8f43229e351d34af3c95b1b9f0a15d', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160ae432231af', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('e7467726ee72235baaeb47df04a35e73', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('eaef4486f1c9b0408580bbfa2037eb66', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('ec4bc97829ab56afd83f428b6dc37ff6', 'f6817f48af4fb3af11b9e8bf182f618b', '200006f0edf145a2b50eacca07585451', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('ec846a3f85fdb6813e515be71f11b331', 'f6817f48af4fb3af11b9e8bf182f618b', '732d48f8e0abe99fe6a23d18a3171cd1', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('ec93bb06f5be4c1f19522ca78180e2ef', 'f6817f48af4fb3af11b9e8bf182f618b', '265de841c58907954b8877fb85212622', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('ecdd72fe694e6bba9c1d9fc925ee79de', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('edefd8d468f5727db465cf1b860af474', 'f6817f48af4fb3af11b9e8bf182f618b', '6ad53fd1b220989a8b71ff482d683a5a', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('ef8bdd20d29447681ec91d3603e80c7b', 'f6817f48af4fb3af11b9e8bf182f618b', 'ae4fed059f67086fd52a73d913cf473d', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('f177acac0276329dc66af0c9ad30558a', 'f6817f48af4fb3af11b9e8bf182f618b', 'c2c356bf4ddd29975347a7047a062440', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('f99f99cc3bc27220cdd4f5aced33b7d7', 'f6817f48af4fb3af11b9e8bf182f618b', '655563cd64b75dcf52ef7bcdd4836953', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('fafe73c4448b977fe42880a6750c3ee8', 'f6817f48af4fb3af11b9e8bf182f618b', '9cb91b8851db0cf7b19d7ecc2a8193dd', null);
INSERT INTO "BDC_MRPC"."SYS_ROLE_PERMISSION" VALUES ('fced905c7598973b970d42d833f73474', 'f6817f48af4fb3af11b9e8bf182f618b', '4875ebe289344e14844d8e3ea1edd73f', null);

-- ----------------------------

--通知数据
INSERT INTO "BDC_MRPC"."SYS_SMS" VALUES ('402880e74dc2f361014dc2f8411e0001', '消息推送测试333', '2', '411944058@qq.com', null, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', TO_DATE('2015-06-05 17:06:01', 'YYYY-MM-DD HH24:MI:SS'), '3', null, null, '认证失败错误的用户名或者密码', 'admin', TO_DATE('2015-06-05 17:05:59', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2015-11-19 22:30:39', 'YYYY-MM-DD HH24:MI:SS'));


--通知模板数据
INSERT INTO "BDC_MRPC"."SYS_SMS_TEMPLATE" VALUES ('4028608164691b000164693108140003', '催办：${taskName}', 'SYS001', '3', '${userName}，您好！
请前待办任务办理事项！${taskName}此消息由系统发出', '{
"taskName":"HR审批",
"userName":"admin"
}', TO_DATE('2018-07-05 14:46:18', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2018-07-05 18:31:34', 'YYYY-MM-DD HH24:MI:SS'), 'admin');

-- ----------------------------

--用户数据
INSERT INTO "BDC_MRPC"."SYS_USER" VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', 'user/20190119/logo-2_1547868176839.png', TO_DATE('2018-12-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), '1', '11@qq.com', '18566666661', 'A01', '1', '0', '1', null, TO_DATE('2038-06-21 17:54:10', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-07-05 14:47:22', 'YYYY-MM-DD HH24:MI:SS'),'450701');


--用户与部门（组织机构）关联数据
INSERT INTO "BDC_MRPC"."SYS_USER_DEPART" VALUES ('ff9c8c6e06514fcf26c108b1395cc876', 'e9ca23d68d884d4ebb19d07889727dae', 'c6d7cb4deeac411cb3384b1b31278596');

--用户与角色关联数据
INSERT INTO "BDC_MRPC"."SYS_USER_ROLE" VALUES ('b694da35692bbfa1fff0e9d5b2dcf311', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');


--数据字典
truncate table  SYS_DICT;
truncate table  SYS_DICT_ITEM;

--字典表前
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('0b5d19e1fce4b2e6647e6b4a17760c14', '通告类型', 'msg_category', '消息类型1:通知公告2:系统消息', '0', 'admin', TO_DATE('2019-04-22 18:01:35', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('236e8a4baff0db8c62c00dd95632834d', '同步工作流引擎', 'activiti_sync', '同步工作流引擎', '0', 'admin', TO_DATE('2019-05-15 15:27:33', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('2e02df51611a4b9632828ab7e5338f00', '权限策略', 'perms_type', '权限策略', '0', 'admin', TO_DATE('2019-04-26 18:26:55', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('2f0320997ade5dd147c90130f7218c3e', '推送类别', 'msg_type', null, '0', 'admin', TO_DATE('2019-03-17 21:21:32', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-26 19:57:45', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('3486f32803bb953e7155dab3513dc68b', '删除状态', 'del_flag', null, '0', 'admin', TO_DATE('2019-01-18 21:46:26', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-30 11:17:11', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('3d9a351be3436fbefb1307d4cfb49bf2', '性别', 'sex', null, '0', null, TO_DATE('2019-01-04 14:56:32', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-30 11:28:27', 'YYYY-MM-DD HH24:MI:SS'), '1');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('404a04a15f371566c658ee9ef9fc392a', 'cehis2', '22', null, '1', 'admin', TO_DATE('2019-01-30 11:17:21', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-30 11:18:12', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('4274efc2292239b6f000b153f50823fd', '全局权限策略', 'global_perms_type', '全局权限策略', '0', 'admin', TO_DATE('2019-05-10 17:54:05', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('4c03fca6bf1f0299c381213961566348', 'Online图表展示模板', 'online_graph_display_template', 'Online图表展示模板', '0', 'admin', TO_DATE('2019-04-12 17:28:50', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('4c753b5293304e7a445fd2741b46529f', '字典状态', 'dict_item_status', null, '0', 'admin', TO_DATE('2020-06-18 23:18:42', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-30 19:33:52', 'YYYY-MM-DD HH24:MI:SS'), '1');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('4d7fec1a7799a436d26d02325eff295e', '优先级', 'priority', '优先级', '0', 'admin', TO_DATE('2019-03-16 17:03:34', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-16 17:39:23', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('4e4602b3e3686f0911384e188dc7efb5', '条件规则', 'rule_conditions', null, '0', 'admin', TO_DATE('2019-04-01 10:15:03', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 10:30:47', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('4f69be5f507accea8d5df5f11346181b', '发送消息类型', 'msgType', null, '0', 'admin', TO_DATE('2019-04-11 14:27:09', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('68168534ff5065a152bfab275c2136f9', '有效无效状态', 'valid_status', '有效无效状态', '0', 'admin', TO_DATE('2020-09-26 19:21:14', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-26 19:21:23', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('6b78e3f59faec1a4750acff08030a79b', '用户类型', 'user_type', null, '1', null, TO_DATE('2019-01-04 14:59:01', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-18 23:28:18', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('72cce0989df68887546746d8f09811aa', 'Online表单类型', 'cgform_table_type', null, '0', 'admin', TO_DATE('2019-01-27 10:13:02', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-30 11:37:36', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('78bda155fe380b1b3f175f1e88c284c7', '流程状态', 'bpm_status', '流程状态', '0', 'admin', TO_DATE('2019-05-09 16:31:52', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('83bfb33147013cc81640d5fd9eda030d', '日志类型', 'log_type', null, '0', 'admin', TO_DATE('2019-03-18 23:22:19', 'YYYY-MM-DD HH24:MI:SS'), null, null, '1');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('845da5006c97754728bf48b6a10f79cc', '状态', 'status', null, '1', 'admin', TO_DATE('2019-03-18 21:45:25', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-18 21:58:25', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('8dfe32e2d29ea9430a988b3b558bf234', '发布状态', 'send_status', '发布状态', '0', 'admin', TO_DATE('2019-04-16 17:40:42', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('a7adbcd86c37f7dbc9b66945c82ef9e6', '1是0否', 'yn', null, '1', 'admin', TO_DATE('2019-05-22 19:29:29', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('a9d9942bd0eccb6e89de92d130ec4c4b', '消息发送状态', 'msgSendStatus', null, '0', 'admin', TO_DATE('2019-04-12 18:18:17', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('ac2f7c0c5c5775fcea7e2387bcb22f01', '菜单类型', 'menu_type', null, '0', 'admin', TO_DATE('2020-12-18 23:24:32', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 15:27:06', 'YYYY-MM-DD HH24:MI:SS'), '1');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('ad7c65ba97c20a6805d5dcdf13cdaf36', 'onlineT类型', 'ceshi_online', null, '1', 'admin', TO_DATE('2019-03-22 16:31:49', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-22 16:34:16', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('bd1b8bc28e65d6feefefb6f3c79f42ff', 'Online图表数据类型', 'online_graph_data_type', 'Online图表数据类型', '0', 'admin', TO_DATE('2019-04-12 17:24:24', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-12 17:24:57', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('c36169beb12de8a71c8683ee7c28a503', '部门状态', 'depart_status', null, '0', 'admin', TO_DATE('2019-03-18 21:59:51', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('c5a14c75172783d72cbee6ee7f5df5d2', 'Online图表类型', 'online_graph_type', 'Online图表类型', '0', 'admin', TO_DATE('2019-04-12 17:04:06', 'YYYY-MM-DD HH24:MI:SS'), null, null, '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('d6e1152968b02d69ff358c75b48a6ee2', '流程类型', 'bpm_process_type', null, '0', 'admin', TO_DATE('2021-02-22 19:26:54', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-30 18:14:44', 'YYYY-MM-DD HH24:MI:SS'), '0');
INSERT INTO "BDC_MRPC"."SYS_DICT" VALUES ('fc6cd58fde2e8481db10d3a1e68ce70c', '用户状态', 'user_status', null, '0', 'admin', TO_DATE('2019-03-18 21:57:25', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-18 23:11:58', 'YYYY-MM-DD HH24:MI:SS'), '1');

insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('0b5d19e1fce4b2e6647e6b4a17760c13', '房屋类型', 'FWLX', '房屋类型', 0, 'admin', to_date('22-04-2019 18:01:35', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:46:55', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('236e8a4baff0db8c62c00dd95632834f', '权利性质', 'QLXZ', '权利性质', 0, 'admin', to_date('15-05-2019 15:27:33', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:08:32', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('4274efc2292239b6f000b153f50823ff', '房屋用途', 'FWYT', '房屋用途', 0, 'admin', to_date('10-05-2019 17:54:05', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:22:58', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('4c03fca6bf1f0299c381213961566349', '抵押方式', 'DYFS', '抵押方式', 0, 'admin', to_date('12-04-2019 17:28:50', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:52:17', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('4c753b5293304e7a445fd2741b46529d', '权利类型', 'QLLX', '权利类型', 0, 'admin', to_date('18-06-2020 23:18:42', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:00:36', 'dd-mm-yyyy hh24:mi:ss'), 1);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('4e4602b3e3686f0911384e188dc7efb4', '房屋结构', 'FWJG', '房屋结构', 0, 'admin', to_date('01-04-2019 10:15:03', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 12:01:25', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('4f69be5f507accea8d5df5f11346181a', '共有方式', 'GYFS', '共有方式', 0, 'admin', to_date('11-04-2019 14:27:09', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:57:31', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('68168534ff5065a152bfab275c2136f8', '面积单位', 'MJDW', '面积单位', 0, 'admin', to_date('26-09-2020 19:21:14', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 10:58:12', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('83bfb33147013cc81640d5fd9eda030c', '不动产单元类型', 'BDCDYLX', '不动产单元类型', 0, 'admin', to_date('18-03-2019 23:22:19', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 12:03:22', 'dd-mm-yyyy hh24:mi:ss'), 1);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('8dfe32e2d29ea9430a988b3b558bf233', '房屋性质', 'FWXZ', '房屋性质', 0, 'admin', to_date('16-04-2019 17:40:42', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:46:43', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('a9d9942bd0eccb6e89de92d130ec4c4a', '抵押不动产类型', 'DYBDCLX', '抵押不动产类型', 0, 'admin', to_date('12-04-2019 18:18:17', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:50:02', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('bd1b8bc28e65d6feefefb6f3c79f42fd', '预告登记种类', 'YGDJZL', '预告登记种类', 0, 'admin', to_date('12-04-2019 17:24:24', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:54:20', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('c5a14c75172783d72cbee6ee7f5df5d1', '查封类型', 'CFLX', '查封类型', 0, 'admin', to_date('12-04-2019 17:04:06', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:55:48', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into BDC_MRPC.SYS_DICT (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type)
values ('d6e1152968b02d69ff358c75b48a6ee1', '响应类型', 'XYLX', '响应类型', 0, 'admin', to_date('22-02-2021 19:26:54', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 10:53:55', 'dd-mm-yyyy hh24:mi:ss'), 0);
insert into bdc_mrpc.sys_dict (ID, DICT_NAME, DICT_CODE, DESCRIPTION, DEL_FLAG, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, TYPE)
values ('36e90421fd4940c5b1e935cde5d2c5d2', '区划代码', 'DIVISION_CODE', '区划代码', 0, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null, 0);
insert into bdc_mrpc.sys_dict (ID, DICT_NAME, DICT_CODE, DESCRIPTION, DEL_FLAG, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, TYPE)
values ('a010dd92f6ef4a1ea2732e3996d9c28f', '登记类型', 'DJLX', '登记类型', 0, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null, 0);


--字典表后
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('0072d115e07c875d76c9b022e2179128', '4d7fec1a7799a436d26d02325eff295e', '低', 'L', '低', '3', '1', 'admin', TO_DATE('2019-04-16 17:04:59', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('05a2e732ce7b00aa52141ecc3e330b4e', '3486f32803bb953e7155dab3513dc68b', '已删除', '1', null, null, '1', 'admin', TO_DATE('2025-10-18 21:46:56', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-28 22:23:20', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('096c2e758d823def3855f6376bc736fb', 'bd1b8bc28e65d6feefefb6f3c79f42ff', 'SQL', 'sql', null, '1', '1', 'admin', TO_DATE('2019-04-12 17:26:26', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('0c9532916f5cd722017b46bc4d953e41', '2f0320997ade5dd147c90130f7218c3e', '指定用户', 'USER', null, null, '1', 'admin', TO_DATE('2019-03-17 21:22:19', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-17 21:22:28', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('0ca4beba9efc4f9dd54af0911a946d5c', '72cce0989df68887546746d8f09811aa', '附表', '3', null, '3', '1', 'admin', TO_DATE('2019-03-27 10:13:43', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('1030a2652608f5eac3b49d70458b8532', '2e02df51611a4b9632828ab7e5338f00', '禁用', '2', '禁用', '2', '1', 'admin', TO_DATE('2021-03-26 18:27:28', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-26 18:39:11', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('147c48ff4b51545032a9119d13f3222a', 'd6e1152968b02d69ff358c75b48a6ee2', '测试流程', 'test', null, '1', '1', 'admin', TO_DATE('2019-03-22 19:27:05', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('1543fe7e5e26fb97cdafe4981bedc0c8', '4c03fca6bf1f0299c381213961566348', '单排布局', 'single', null, '2', '1', 'admin', TO_DATE('2022-07-12 17:43:39', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-12 17:43:57', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('1b8a6341163062dad8cb2fddd34e0c3b', '404a04a15f371566c658ee9ef9fc392a', '22', '222', null, '1', '1', 'admin', TO_DATE('2019-03-30 11:17:48', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('1ce390c52453891f93514c1bd2795d44', 'ad7c65ba97c20a6805d5dcdf13cdaf36', '000', '00', null, '1', '1', 'admin', TO_DATE('2019-03-22 16:34:34', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('1db531bcff19649fa82a644c8a939dc4', '4c03fca6bf1f0299c381213961566348', '组合布局', 'combination', null, '4', '1', 'admin', TO_DATE('2019-05-11 16:07:08', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('222705e11ef0264d4214affff1fb4ff9', '4f69be5f507accea8d5df5f11346181b', '短信', '1', null, '1', '1', 'admin', TO_DATE('2023-02-28 10:50:36', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-28 10:58:11', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('23a5bb76004ed0e39414e928c4cde155', '4e4602b3e3686f0911384e188dc7efb5', '不等于', '!=', '不等于', '3', '1', 'admin', TO_DATE('2019-04-01 16:46:15', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:48:40', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('25847e9cb661a7c711f9998452dc09e6', '4e4602b3e3686f0911384e188dc7efb5', '小于等于', '<=', '小于等于', '6', '1', 'admin', TO_DATE('2019-04-01 16:44:34', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:49:10', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('2d51376643f220afdeb6d216a8ac2c01', '68168534ff5065a152bfab275c2136f9', '有效', '1', '有效', '2', '1', 'admin', TO_DATE('2019-04-26 19:22:01', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('308c8aadf0c37ecdde188b97ca9833f5', '8dfe32e2d29ea9430a988b3b558bf234', '已发布', '1', '已发布', '2', '1', 'admin', TO_DATE('2019-04-16 17:41:24', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('333e6b2196e01ef9a5f76d74e86a6e33', '8dfe32e2d29ea9430a988b3b558bf234', '未发布', '0', '未发布', '1', '1', 'admin', TO_DATE('2019-04-16 17:41:12', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('337ea1e401bda7233f6258c284ce4f50', 'bd1b8bc28e65d6feefefb6f3c79f42ff', 'JSON', 'json', null, '1', '1', 'admin', TO_DATE('2019-04-12 17:26:33', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('33bc9d9f753cf7dc40e70461e50fdc54', 'a9d9942bd0eccb6e89de92d130ec4c4b', '发送失败', '2', null, '3', '1', 'admin', TO_DATE('2019-04-12 18:20:02', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('3fbc03d6c994ae06d083751248037c0e', '78bda155fe380b1b3f175f1e88c284c7', '已完成', '3', '已完成', '3', '1', 'admin', TO_DATE('2019-05-09 16:33:25', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('41d7aaa40c9b61756ffb1f28da5ead8e', '0b5d19e1fce4b2e6647e6b4a17760c14', '通知公告', '1', null, '1', '1', 'admin', TO_DATE('2019-04-22 18:01:57', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('41fa1e9571505d643aea87aeb83d4d76', '4e4602b3e3686f0911384e188dc7efb5', '等于', '=', '等于', '4', '1', 'admin', TO_DATE('2019-04-01 16:45:24', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:49:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('43d2295b8610adce9510ff196a49c6e9', '845da5006c97754728bf48b6a10f79cc', '正常', '1', null, null, '1', 'admin', TO_DATE('2019-03-18 21:45:51', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('4f05fb5376f4c61502c5105f52e4dd2b', '83bfb33147013cc81640d5fd9eda030d', '操作日志', '2', null, null, '1', 'admin', TO_DATE('2019-03-18 23:22:49', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('50223341bfb5ba30bf6319789d8d17fe', 'd6e1152968b02d69ff358c75b48a6ee2', '业务办理', 'business', null, '3', '1', 'admin', TO_DATE('2023-04-22 19:28:05', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-22 23:24:39', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('51222413e5906cdaf160bb5c86fb827c', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '是', '1', null, '1', '1', 'admin', TO_DATE('2019-05-22 19:29:45', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('538fca35afe004972c5f3947c039e766', '2e02df51611a4b9632828ab7e5338f00', '显示', '1', '显示', '1', '1', 'admin', TO_DATE('2025-03-26 18:27:13', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-26 18:39:07', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('5584c21993bde231bbde2b966f2633ac', '4e4602b3e3686f0911384e188dc7efb5', '自定义SQL表达式', 'USE_SQL_RULES', '自定义SQL表达式', '9', '1', 'admin', TO_DATE('2019-04-01 10:45:24', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:49:27', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('58b73b344305c99b9d8db0fc056bbc0a', '72cce0989df68887546746d8f09811aa', '主表', '2', null, '2', '1', 'admin', TO_DATE('2019-03-27 10:13:36', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('5b65a88f076b32e8e69d19bbaadb52d5', '2f0320997ade5dd147c90130f7218c3e', '全体用户', 'ALL', null, null, '1', 'admin', TO_DATE('2020-10-17 21:22:43', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-28 22:17:09', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('5d84a8634c8fdfe96275385075b105c9', '3d9a351be3436fbefb1307d4cfb49bf2', '女', '2', null, '2', '1', null, TO_DATE('2019-01-04 14:56:56', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2019-01-04 17:38:12', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('66c952ae2c3701a993e7db58f3baf55e', '4e4602b3e3686f0911384e188dc7efb5', '大于', '>', '大于', '1', '1', 'admin', TO_DATE('2019-04-01 10:45:46', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:48:29', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('6937c5dde8f92e9a00d4e2ded9198694', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'easyui', '3', null, '1', '1', 'admin', TO_DATE('2019-03-22 16:32:15', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('69cacf64e244100289ddd4aa9fa3b915', 'a9d9942bd0eccb6e89de92d130ec4c4b', '未发送', '0', null, '1', '1', 'admin', TO_DATE('2019-04-12 18:19:23', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('6a7a9e1403a7943aba69e54ebeff9762', '4f69be5f507accea8d5df5f11346181b', '邮件', '2', null, '2', '1', 'admin', TO_DATE('2031-02-28 10:50:44', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-28 10:59:03', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('6c682d78ddf1715baf79a1d52d2aa8c2', '72cce0989df68887546746d8f09811aa', '单表', '1', null, '1', '1', 'admin', TO_DATE('2019-03-27 10:13:29', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('6d404fd2d82311fbc87722cd302a28bc', '4e4602b3e3686f0911384e188dc7efb5', '模糊', 'LIKE', '模糊', '7', '1', 'admin', TO_DATE('2019-04-01 16:46:02', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:49:20', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('6d4e26e78e1a09699182e08516c49fc4', '4d7fec1a7799a436d26d02325eff295e', '高', 'H', '高', '1', '1', 'admin', TO_DATE('2019-04-16 17:04:24', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('700e9f030654f3f90e9ba76ab0713551', '6b78e3f59faec1a4750acff08030a79b', '333', '333', null, null, '1', 'admin', TO_DATE('2019-02-21 19:59:47', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('7050c1522702bac3be40e3b7d2e1dfd8', 'c5a14c75172783d72cbee6ee7f5df5d2', '柱状图', 'bar', null, '1', '1', 'admin', TO_DATE('2019-04-12 17:05:17', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('71b924faa93805c5c1579f12e001c809', 'd6e1152968b02d69ff358c75b48a6ee2', 'OA办公', 'oa', null, '2', '1', 'admin', TO_DATE('2021-03-22 19:27:17', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-22 23:24:36', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('75b260d7db45a39fc7f21badeabdb0ed', 'c36169beb12de8a71c8683ee7c28a503', '不启用', '0', null, null, '1', 'admin', TO_DATE('2019-03-18 23:29:41', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-18 23:29:54', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('7688469db4a3eba61e6e35578dc7c2e5', 'c36169beb12de8a71c8683ee7c28a503', '启用', '1', null, null, '1', 'admin', TO_DATE('2019-03-18 23:29:28', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('78ea6cadac457967a4b1c4eb7aaa418c', 'fc6cd58fde2e8481db10d3a1e68ce70c', '正常', '1', null, null, '1', 'admin', TO_DATE('2019-03-18 23:30:28', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('7ccf7b80c70ee002eceb3116854b75cb', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '按钮权限', '2', null, null, '1', 'admin', TO_DATE('2019-03-18 23:25:40', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('81fb2bb0e838dc68b43f96cc309f8257', 'fc6cd58fde2e8481db10d3a1e68ce70c', '冻结', '2', null, null, '1', 'admin', TO_DATE('2019-03-18 23:30:37', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('83250269359855501ec4e9c0b7e21596', '4274efc2292239b6f000b153f50823fd', '显示/访问(授权后显示/可访问)', '1', null, '1', '1', 'admin', TO_DATE('2019-05-10 17:54:51', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('84778d7e928bc843ad4756db1322301f', '4e4602b3e3686f0911384e188dc7efb5', '大于等于', '>=', '大于等于', '5', '1', 'admin', TO_DATE('2019-04-01 10:46:02', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:49:05', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('848d4da35ebd93782029c57b103e5b36', 'c5a14c75172783d72cbee6ee7f5df5d2', '饼图', 'pie', null, '3', '1', 'admin', TO_DATE('2019-04-12 17:05:49', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('84dfc178dd61b95a72900fcdd624c471', '78bda155fe380b1b3f175f1e88c284c7', '处理中', '2', '处理中', '2', '1', 'admin', TO_DATE('2019-05-09 16:33:01', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('86f19c7e0a73a0bae451021ac05b99dd', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '子菜单', '1', null, null, '1', 'admin', TO_DATE('2019-03-18 23:25:27', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('8bccb963e1cd9e8d42482c54cc609ca2', '4f69be5f507accea8d5df5f11346181b', '微信', '3', null, '3', '1', 'admin', TO_DATE('2021-05-11 14:29:12', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-11 14:29:31', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('8c618902365ca681ebbbe1e28f11a548', '4c753b5293304e7a445fd2741b46529f', '启用', '1', null, '0', '1', 'admin', TO_DATE('2020-07-18 23:19:27', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-17 14:51:18', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('8cdf08045056671efd10677b8456c999', '4274efc2292239b6f000b153f50823fd', '可编辑(未授权时禁用)', '2', null, '2', '1', 'admin', TO_DATE('2019-05-10 17:55:38', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('8ff48e657a7c5090d4f2a59b37d1b878', '4d7fec1a7799a436d26d02325eff295e', '中', 'M', '中', '2', '1', 'admin', TO_DATE('2019-04-16 17:04:40', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('9a96c4a4e4c5c9b4e4d0cbf6eb3243cc', '4c753b5293304e7a445fd2741b46529f', '不启用', '0', null, '1', '1', 'admin', TO_DATE('2019-03-18 23:19:53', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('a2321496db6febc956a6c70fab94cb0c', '404a04a15f371566c658ee9ef9fc392a', '3', '3', null, '1', '1', 'admin', TO_DATE('2019-03-30 11:18:18', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('a2be752dd4ec980afaec1efd1fb589af', '8dfe32e2d29ea9430a988b3b558bf234', '已撤销', '2', '已撤销', '3', '1', 'admin', TO_DATE('2019-04-16 17:41:39', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('aa0d8a8042a18715a17f0a888d360aa4', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '一级菜单', '0', null, null, '1', 'admin', TO_DATE('2019-03-18 23:24:52', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('adcf2a1fe93bb99a84833043f475fe0b', '4e4602b3e3686f0911384e188dc7efb5', '包含', 'IN', '包含', '8', '1', 'admin', TO_DATE('2019-04-01 16:45:47', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:49:24', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('b029a41a851465332ee4ee69dcf0a4c2', '0b5d19e1fce4b2e6647e6b4a17760c14', '系统消息', '2', null, '1', '1', 'admin', TO_DATE('2019-02-22 18:02:08', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-22 18:02:13', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('b2a8b4bb2c8e66c2c4b1bb086337f393', '3486f32803bb953e7155dab3513dc68b', '正常', '0', null, null, '1', 'admin', TO_DATE('2022-10-18 21:46:48', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-28 22:22:20', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('b57f98b88363188daf38d42f25991956', '6b78e3f59faec1a4750acff08030a79b', '22', '222', null, null, '0', 'admin', TO_DATE('2019-02-21 19:59:43', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-11 21:23:27', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('b5f3bd5f66bb9a83fecd89228c0d93d1', '68168534ff5065a152bfab275c2136f9', '无效', '0', '无效', '1', '1', 'admin', TO_DATE('2019-04-26 19:21:49', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('b9fbe2a3602d4a27b45c100ac5328484', '78bda155fe380b1b3f175f1e88c284c7', '待提交', '1', '待提交', '1', '1', 'admin', TO_DATE('2019-05-09 16:32:35', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('ba27737829c6e0e582e334832703d75e', '236e8a4baff0db8c62c00dd95632834d', '同步', '1', '同步', '1', '1', 'admin', TO_DATE('2019-05-15 15:28:15', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('c5700a71ad08994d18ad1dacc37a71a9', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '否', '0', null, '1', '1', 'admin', TO_DATE('2019-05-22 19:29:55', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('cbfcc5b88fc3a90975df23ffc8cbe29c', 'c5a14c75172783d72cbee6ee7f5df5d2', '曲线图', 'line', null, '2', '1', 'admin', TO_DATE('2019-05-12 17:05:30', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-12 17:06:06', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('d217592908ea3e00ff986ce97f24fb98', 'c5a14c75172783d72cbee6ee7f5df5d2', '数据列表', 'table', null, '4', '1', 'admin', TO_DATE('2019-04-12 17:05:56', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('df168368dcef46cade2aadd80100d8aa', '3d9a351be3436fbefb1307d4cfb49bf2', '男', '1', null, '1', '1', null, TO_DATE('2027-08-04 14:56:49', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-23 22:44:44', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('e6329e3a66a003819e2eb830b0ca2ea0', '4e4602b3e3686f0911384e188dc7efb5', '小于', '<', '小于', '2', '1', 'admin', TO_DATE('2019-04-01 16:44:15', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-04-01 17:48:34', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('e94eb7af89f1dbfa0d823580a7a6e66a', '236e8a4baff0db8c62c00dd95632834d', '不同步', '0', '不同步', '2', '1', 'admin', TO_DATE('2019-05-15 15:28:28', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('f0162f4cc572c9273f3e26b2b4d8c082', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'booostrap', '1', null, '1', '1', 'admin', TO_DATE('2021-08-22 16:32:04', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-03-22 16:33:57', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('f16c5706f3ae05c57a53850c64ce7c45', 'a9d9942bd0eccb6e89de92d130ec4c4b', '发送成功', '1', null, '2', '1', 'admin', TO_DATE('2019-04-12 18:19:43', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('f2a7920421f3335afdf6ad2b342f6b5d', '845da5006c97754728bf48b6a10f79cc', '冻结', '2', null, null, '1', 'admin', TO_DATE('2019-03-18 21:46:02', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('f37f90c496ec9841c4c326b065e00bb2', '83bfb33147013cc81640d5fd9eda030d', '登录日志', '1', null, null, '1', 'admin', TO_DATE('2019-03-18 23:22:37', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('f753aff60ff3931c0ecb4812d8b5e643', '4c03fca6bf1f0299c381213961566348', '双排布局', 'double', null, '3', '1', 'admin', TO_DATE('2019-04-12 17:43:51', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('fcec03570f68a175e1964808dc3f1c91', '4c03fca6bf1f0299c381213961566348', 'Tab风格', 'tab', null, '1', '1', 'admin', TO_DATE('2019-04-12 17:43:31', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "BDC_MRPC"."SYS_DICT_ITEM" VALUES ('fe50b23ae5e68434def76f67cef35d2d', '78bda155fe380b1b3f175f1e88c284c7', '已作废', '4', '已作废', '4', '1', 'admin', TO_DATE('2021-09-09 16:33:43', 'YYYY-MM-DD HH24:MI:SS'), 'admin', TO_DATE('2019-05-09 16:34:40', 'YYYY-MM-DD HH24:MI:SS'));




insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('112d2b4a9523bfd94ac82d394492ee20', '236e8a4baff0db8c62c00dd95632834f', '家庭承包', '201', null, 10, 1, 'admin', to_date('27-07-2019 11:11:51', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('cadb15c357ede53e890d4a72802d89c6', '236e8a4baff0db8c62c00dd95632834f', '其它方式承包', '202', null, 11, 1, 'admin', to_date('27-07-2019 11:12:07', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('f41c5d584e7a02fd99ecc1f5a899595b', '236e8a4baff0db8c62c00dd95632834f', '批准拨用', '203', null, 11, 1, 'admin', to_date('27-07-2019 11:12:24', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('1adaa3410b4598a6019cc813361c5b83', '236e8a4baff0db8c62c00dd95632834f', '入股', '204', null, 13, 1, 'admin', to_date('27-07-2019 11:12:47', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('b43f21b03f0dacc2fe2568e25d109878', '236e8a4baff0db8c62c00dd95632834f', '联营', '205', null, 14, 1, 'admin', to_date('27-07-2019 11:13:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('61ef6ab804d483fa381d2aef4867105f', '4274efc2292239b6f000b153f50823ff', '住宅', '10', null, 1, 1, 'admin', to_date('27-07-2019 11:30:06', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('8c7edd12e37f57da43e4ce9966687651', '4274efc2292239b6f000b153f50823ff', '成套住宅', '11', null, 1, 1, 'admin', to_date('27-07-2019 11:30:23', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:30:58', 'dd-mm-yyyy hh24:mi:ss'));
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('54b3d999965619dd6cdfc7b2501af15b', '4274efc2292239b6f000b153f50823ff', '别墅', '111', null, 1, 1, 'admin', to_date('27-07-2019 11:30:38', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('5ff77284fae15ed792868ebf87e4f410', '4274efc2292239b6f000b153f50823ff', '高档公寓', '112', null, 1, 1, 'admin', to_date('27-07-2019 11:30:53', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a4c6d560324267ce8f01d4e24ec5984a', '4274efc2292239b6f000b153f50823ff', '非成套住宅', '12', null, 1, 1, 'admin', to_date('27-07-2019 11:31:13', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('08c4bdd0d2b32ac2f4b82f110c52b107', '4274efc2292239b6f000b153f50823ff', '集体宿舍', '13', null, 1, 1, 'admin', to_date('27-07-2019 11:31:26', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('41cfd348d2d74a44dd7af1ad088d8a3f', '4274efc2292239b6f000b153f50823ff', '工业、交通、仓储', '20', null, 1, 1, 'admin', to_date('27-07-2019 11:31:36', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6685e0a1cb3146cf8ffdef1f23030af6', '4274efc2292239b6f000b153f50823ff', '工业', '21', null, 1, 1, 'admin', to_date('27-07-2019 11:31:48', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ec7ac6b4845fbf93ae1c4285347af5fc', '4274efc2292239b6f000b153f50823ff', '公共设施', '22', null, 1, 1, 'admin', to_date('27-07-2019 11:36:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('42f99a042cb5832830e275f75f286d8f', '4274efc2292239b6f000b153f50823ff', '铁路', '23', null, 1, 1, 'admin', to_date('27-07-2019 11:36:14', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('341c59640fa2bb59da920ad357cec1f9', '4274efc2292239b6f000b153f50823ff', '民航', '24', null, 1, 1, 'admin', to_date('27-07-2019 11:36:27', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('bb722fc13f7dfa1a15e762b336e21862', '4274efc2292239b6f000b153f50823ff', '航运', '25', null, 1, 1, 'admin', to_date('27-07-2019 11:36:36', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('8b162852ee2091c9c7704f958f8808e0', '4274efc2292239b6f000b153f50823ff', '公共运输', '26', null, 1, 1, 'admin', to_date('27-07-2019 11:36:49', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('faff081402ca63feccfab9ac63d5567e', '4274efc2292239b6f000b153f50823ff', '仓储', '27', null, 1, 1, 'admin', to_date('27-07-2019 11:36:59', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('42e1e1adc99a865bb4c7c986a0c776a6', '4274efc2292239b6f000b153f50823ff', '商业、金融、信息', '30', null, 1, 1, 'admin', to_date('27-07-2019 11:37:10', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:37:22', 'dd-mm-yyyy hh24:mi:ss'));
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a8551a46d701f8f6c62f35ac83910392', '4274efc2292239b6f000b153f50823ff', '商业服务', '31', null, 1, 1, 'admin', to_date('27-07-2019 11:37:36', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('3e832588940ed13ab025af600cc5260b', '4274efc2292239b6f000b153f50823ff', '经营', '32', null, 1, 1, 'admin', to_date('27-07-2019 11:37:47', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('b24570f728d1a1643f7a73ef89000eb3', '4274efc2292239b6f000b153f50823ff', '旅游', '33', null, 1, 1, 'admin', to_date('27-07-2019 11:37:56', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('201d9a1833d4317cb47a83bc16be59ab', '4274efc2292239b6f000b153f50823ff', '金融保险', '34', null, 1, 1, 'admin', to_date('27-07-2019 11:38:11', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9815999b1baf65871f888e7a7fe49ecf', '4274efc2292239b6f000b153f50823ff', '电讯信息', '35', null, 1, 1, 'admin', to_date('27-07-2019 11:38:21', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9ac2eeb153208a7187b7b667c9c1d901', '4274efc2292239b6f000b153f50823ff', '教育、医疗、卫生、科研', '40', null, 1, 1, 'admin', to_date('27-07-2019 11:38:37', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6e2d8d482a42059731a2c52983ebe6a2', '4274efc2292239b6f000b153f50823ff', '教育', '41', null, 1, 1, 'admin', to_date('27-07-2019 11:38:51', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d58678437c4534f01559aada84b83835', '4274efc2292239b6f000b153f50823ff', '医疗卫生', '42', null, 1, 1, 'admin', to_date('27-07-2019 11:39:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('0174d234cc1cbad97c2211f6a4f9bd8f', '4274efc2292239b6f000b153f50823ff', '科研', '43', null, 1, 1, 'admin', to_date('27-07-2019 11:39:16', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('e53074a393bd55c9284d47c689de9a4f', '4274efc2292239b6f000b153f50823ff', '文化、娱乐、体育', '50', null, 1, 1, 'admin', to_date('27-07-2019 11:39:26', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a116167de82c7548463653173c9b1473', '4274efc2292239b6f000b153f50823ff', '文化', '51', null, 1, 1, 'admin', to_date('27-07-2019 11:39:37', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6b0849fc7af0e2d1266a9a2eaced3306', '4274efc2292239b6f000b153f50823ff', '新闻', '52', null, 1, 1, 'admin', to_date('27-07-2019 11:39:46', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('b1489eb7c1dde1cfeb8a36ac6ab00cd7', '4274efc2292239b6f000b153f50823ff', '娱乐', '53', null, 1, 1, 'admin', to_date('27-07-2019 11:39:56', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('e2c89b5012ff5d90d3288cbd420c4bfe', '4274efc2292239b6f000b153f50823ff', '园林绿化', '54', null, 1, 1, 'admin', to_date('27-07-2019 11:40:06', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('411abeabf02a7ee0248ee97acff2dcfd', '4274efc2292239b6f000b153f50823ff', '体育', '55', null, 1, 1, 'admin', to_date('27-07-2019 11:40:24', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('17156e363f3ea02464884b92cef4f85d', '4274efc2292239b6f000b153f50823ff', '办公', '60', null, 1, 1, 'admin', to_date('27-07-2019 11:40:34', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d2132da5d63b6140cb641351f5b26bdd', '4274efc2292239b6f000b153f50823ff', '军事', '70', null, 1, 1, 'admin', to_date('27-07-2019 11:40:46', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('189a024ea38e101ecdb660c31862840c', '4274efc2292239b6f000b153f50823ff', '其它', '80', null, 1, 1, 'admin', to_date('27-07-2019 11:41:05', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('88c5a39ca9a4f60a3902c3ff2174d7db', '4274efc2292239b6f000b153f50823ff', '涉外', '81', null, 1, 1, 'admin', to_date('27-07-2019 11:41:16', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('5173ed6cd2f77b4e8efcb0629fbac4c3', '4274efc2292239b6f000b153f50823ff', '宗教', '82', null, 1, 1, 'admin', to_date('27-07-2019 11:41:26', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('38689704b38ca81560d5871a7a4099f4', '4274efc2292239b6f000b153f50823ff', '监狱', '83', null, 1, 1, 'admin', to_date('27-07-2019 11:41:35', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9a659c9cea941b3717740735236bd045', '4274efc2292239b6f000b153f50823ff', '物管用房', '84', null, 1, 1, 'admin', to_date('27-07-2019 11:41:45', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('2a47962cbc1018476e016c6f7f2406b7', '0b5d19e1fce4b2e6647e6b4a17760c13', '住宅', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:43:11', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('90da037cdb8a27c9204e4eb1197d8a0c', '0b5d19e1fce4b2e6647e6b4a17760c13', '商业用房', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:43:26', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('f545364a66187844a0c936e2ee74ff69', '8dfe32e2d29ea9430a988b3b558bf233', '公共租赁住房', '3', null, 1, 1, 'admin', to_date('27-07-2019 11:47:54', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('10c4392a7721333f55962a404695ad5e', '8dfe32e2d29ea9430a988b3b558bf233', '廉租住房', '4', null, 1, 1, 'admin', to_date('27-07-2019 11:48:12', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('b5c03ba10a447733b91b6c2f6ce0fdc8', '8dfe32e2d29ea9430a988b3b558bf233', '限价普通商品住房', '5', null, 1, 1, 'admin', to_date('27-07-2019 11:48:21', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9b212b4607042f6dd8f14cd61df8bb3c', '8dfe32e2d29ea9430a988b3b558bf233', '经济适用住房', '6', null, 1, 1, 'admin', to_date('27-07-2019 11:48:34', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('93f22d0ad532c79f3d5111810b6964dc', '8dfe32e2d29ea9430a988b3b558bf233', '定销商品房', '7', null, 1, 1, 'admin', to_date('27-07-2019 11:48:44', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('97a9543c855f851e1ada59548b91755b', '8dfe32e2d29ea9430a988b3b558bf233', '集资建房', '8', null, 1, 1, 'admin', to_date('27-07-2019 11:48:53', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('4dd1922da0cb7e024a341df9c608e9a4', '8dfe32e2d29ea9430a988b3b558bf233', '福利房', '9', null, 1, 1, 'admin', to_date('27-07-2019 11:49:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ee71b9a5f7e0010485d1037342fb8f3b', '8dfe32e2d29ea9430a988b3b558bf233', '其它', '99', null, 1, 1, 'admin', to_date('27-07-2019 11:49:13', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('24a9d0992ccc3c4ac9d156fcc984a988', 'a9d9942bd0eccb6e89de92d130ec4c4a', '土地', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:50:27', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('69d956f393862e7d494a83a563904ea4', 'a9d9942bd0eccb6e89de92d130ec4c4a', '土地和房屋', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:50:36', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('c0fdb02b951b9ca23221ab87cf3e8cfa', 'a9d9942bd0eccb6e89de92d130ec4c4a', '林地和林木', '3', null, 1, 1, 'admin', to_date('27-07-2019 11:50:48', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('e07f442a955ed97a291d78e5f209e4ac', 'a9d9942bd0eccb6e89de92d130ec4c4a', '土地和在建建筑物', '4', null, 1, 1, 'admin', to_date('27-07-2019 11:50:57', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('8df4530292d6adf24f0f3e5b6b74a3d0', 'a9d9942bd0eccb6e89de92d130ec4c4a', '海域', '5', null, 1, 1, 'admin', to_date('27-07-2019 11:51:06', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d6a0e0091a888b54f8468a6d4bfe1b13', 'a9d9942bd0eccb6e89de92d130ec4c4a', '海域和构筑物', '6', null, 1, 1, 'admin', to_date('27-07-2019 11:51:16', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('4103543437d6af8cbf207c6cf647b01c', 'a9d9942bd0eccb6e89de92d130ec4c4a', '其它', '7', null, 1, 1, 'admin', to_date('27-07-2019 11:51:27', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('1a248c877424d21b666f4db80f32a2d0', '4c03fca6bf1f0299c381213961566349', '一般抵押', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:52:55', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('5ebe35287ccb0437cf3d7b92a98173b9', '4c03fca6bf1f0299c381213961566349', '最高额抵押', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:53:05', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('59ae7acb4816a69f477e0d97542116d5', 'bd1b8bc28e65d6feefefb6f3c79f42fd', '预售商品房买卖预告登记', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:54:41', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a1bbc705066dc837766347c34a6f914a', 'bd1b8bc28e65d6feefefb6f3c79f42fd', '其它不动产买卖预告登记', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:54:51', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('3f63145631b4cc0ad99177f25f6867df', 'bd1b8bc28e65d6feefefb6f3c79f42fd', '预售商品房抵押权预告登记', '3', null, 1, 1, 'admin', to_date('27-07-2019 11:55:02', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('1b74031050e18eb27348ab0f20c262e1', 'bd1b8bc28e65d6feefefb6f3c79f42fd', '其它不动产抵押权预告登记', '4', null, 1, 1, 'admin', to_date('27-07-2019 11:55:11', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6513ccd73237909fd40d9e82a935198a', '4f69be5f507accea8d5df5f11346181a', '单独所有', '0', null, 1, 1, 'admin', to_date('27-07-2019 11:57:54', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('e737bf53db5d49be7f9f0745610d205e', '4f69be5f507accea8d5df5f11346181a', '共同共有', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:58:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('44a87a459090ab3dcfa14a3bfbde8825', '4f69be5f507accea8d5df5f11346181a', '按份共有', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:58:13', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d86089e6d8dcd44368f3829e0d925dcf', '4f69be5f507accea8d5df5f11346181a', '其它共有', '3', null, 1, 1, 'admin', to_date('27-07-2019 11:58:22', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a0c1c359ea01f3fde7d6bca70b6366a1', '4e4602b3e3686f0911384e188dc7efb4', '钢结构', '1', null, 1, 1, 'admin', to_date('27-07-2019 12:01:42', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('0467b7e38bd051e7cc90a1def95f7b20', '4e4602b3e3686f0911384e188dc7efb4', '钢和钢筋混凝土结构', '2', null, 1, 1, 'admin', to_date('27-07-2019 12:01:52', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d931da1d2e42c9c85081d641ba3ac2bf', '4e4602b3e3686f0911384e188dc7efb4', '钢筋混凝土结构', '3', null, 1, 1, 'admin', to_date('27-07-2019 12:02:01', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ace9bad344c7cbea852e52e121eca723', '4e4602b3e3686f0911384e188dc7efb4', '混合结构', '4', null, 1, 1, 'admin', to_date('27-07-2019 12:02:11', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('c155e32a1096983de6052dcca86a3dbe', '4e4602b3e3686f0911384e188dc7efb4', '砖木结构', '5', null, 1, 1, 'admin', to_date('27-07-2019 12:02:21', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('324b40a6b4b1dc855c331e278379cce6', '4e4602b3e3686f0911384e188dc7efb4', '其它结构', '6', null, 1, 1, 'admin', to_date('27-07-2019 12:02:32', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('8f949fcd5666e8ecb621fa6b6acf29f9', '83bfb33147013cc81640d5fd9eda030c', '构筑物', '06', null, 1, 1, 'admin', to_date('27-07-2019 12:04:35', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('e62b267f732d4d1513b7f1197ef35cf8', '83bfb33147013cc81640d5fd9eda030c', '其他定着物', '07', null, 1, 1, 'admin', to_date('27-07-2019 12:04:45', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('2fe4b619f97224c79505cf17e10f6c94', '83bfb33147013cc81640d5fd9eda030c', '点状定着物', '071', null, 1, 1, 'admin', to_date('27-07-2019 12:04:55', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('43fe4b99071c0e75af3cec27d59066af', '83bfb33147013cc81640d5fd9eda030c', '线状定着物', '072', null, 1, 1, 'admin', to_date('27-07-2019 12:05:16', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('25b91954c19ba4df7a615bafb64c1c7d', '83bfb33147013cc81640d5fd9eda030c', '面状定着物', '073', null, 1, 1, 'admin', to_date('27-07-2019 12:05:28', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('5ca5cceb84da7cbec58d11966110ab30', '83bfb33147013cc81640d5fd9eda030c', '户', '031', null, 1, 1, 'admin', to_date('27-07-2019 12:05:40', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d6036e8739f9218f851b41553eeaed3e', '83bfb33147013cc81640d5fd9eda030c', '预测户', '032', null, 1, 1, 'admin', to_date('27-07-2019 12:05:55', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('b024d8ab01046259efacd136369b078d', 'c5a14c75172783d72cbee6ee7f5df5d1', '查封', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:56:10', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('cb1e0460abbe6c4f8320fcf38cb2816d', '0b5d19e1fce4b2e6647e6b4a17760c13', '办公用房', '3', null, 1, 1, 'admin', to_date('27-07-2019 11:43:59', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9a5a3093c87a1427e73beb47f66982d1', '83bfb33147013cc81640d5fd9eda030c', '所有权宗地', '01', null, 1, 1, 'admin', to_date('27-07-2019 12:03:45', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('60e01acfc704097460a869dd2e26f3ae', 'c5a14c75172783d72cbee6ee7f5df5d1', '轮候查封', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:56:23', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a5c9f22b1b954f781fb022dd1d7acba5', '83bfb33147013cc81640d5fd9eda030c', '使用权宗地', '02', null, 1, 1, 'admin', to_date('27-07-2019 12:03:54', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('fb1802fb047a340d85b3a1b3fb26599d', '0b5d19e1fce4b2e6647e6b4a17760c13', '工业用房', '4', null, 1, 1, 'admin', to_date('27-07-2019 11:44:09', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('5c088ce2ec720e4ce9f8de772fb0d60f', '0b5d19e1fce4b2e6647e6b4a17760c13', '仓储用房', '5', null, 1, 1, 'admin', to_date('27-07-2019 11:44:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ea57328a490e86f738eacacac5382787', '0b5d19e1fce4b2e6647e6b4a17760c13', '车库', '6', null, 1, 1, 'admin', to_date('27-07-2019 11:44:31', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a97fb6fb9375f2ae616db0672c8818f3', '8dfe32e2d29ea9430a988b3b558bf233', '配套商品房', '2', null, 1, 1, 'admin', to_date('27-07-2019 11:47:39', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('467b9bf32bcd40f5752f349a24796ce3', '83bfb33147013cc81640d5fd9eda030c', '自然幢', '03', null, 1, 1, 'admin', to_date('27-07-2019 12:04:03', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('033cc4b4a8b6979496a442410ed67e41', '0b5d19e1fce4b2e6647e6b4a17760c13', '其它', '99', null, 1, 1, 'admin', to_date('27-07-2019 11:44:40', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('e3b1f0ba8f1cb3cad7b5061db3f24519', '8dfe32e2d29ea9430a988b3b558bf233', '市场化商品房', '0', null, 1, 1, 'admin', to_date('27-07-2019 11:47:14', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('871ba26dc571e4a2c7f80533ace4b6b2', '8dfe32e2d29ea9430a988b3b558bf233', '动迁房', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:47:26', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('19eb0d514fe9282b68d8c3aa375bce0a', 'c5a14c75172783d72cbee6ee7f5df5d1', '预查封', '3', null, 1, 1, 'admin', to_date('27-07-2019 11:56:33', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ed3e781783b85121fcf5f47eb427649b', 'c5a14c75172783d72cbee6ee7f5df5d1', '轮候预查封', '4', null, 1, 1, 'admin', to_date('27-07-2019 11:56:45', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('48dcb53b81cd46948ab16f0019869332', '83bfb33147013cc81640d5fd9eda030c', '海域', '04', null, 1, 1, 'admin', to_date('27-07-2019 12:04:13', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9e8e43fd615c2248297d77f9dc3187fe', '83bfb33147013cc81640d5fd9eda030c', '林地', '05', null, 1, 1, 'admin', to_date('27-07-2019 12:04:25', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('051bfbf31d0bc49ef8afba3e26595a29', '68168534ff5065a152bfab275c2136f8', '平方米', '1', null, 1, 1, 'admin', to_date('27-07-2019 10:58:34', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9dcc4b11a5b623dd2ad57f49c93595eb', 'd6e1152968b02d69ff358c75b48a6ee1', '内部错误', '02', '内部错误', 3, 1, 'admin', to_date('27-07-2019 10:55:25', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('253d08c100ce393a76591f1022ce17bf', 'd6e1152968b02d69ff358c75b48a6ee1', '未授权', '41', '未授权', 4, 1, 'admin', to_date('27-07-2019 10:55:56', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ae4289fa2898882decef8fce2fddc8b8', 'd6e1152968b02d69ff358c75b48a6ee1', '令牌（Token）过期', '42', null, 5, 1, 'admin', to_date('27-07-2019 10:56:17', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('1caf937468749a878e83b1ad656805a5', 'd6e1152968b02d69ff358c75b48a6ee1', '用户验证不成功', '43', null, 6, 1, 'admin', to_date('27-07-2019 10:56:36', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('c1cd6b8454a27a45c4157c9ed23b0423', 'd6e1152968b02d69ff358c75b48a6ee1', '其他错误', '99', null, 7, 1, 'admin', to_date('27-07-2019 10:56:59', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6dbb2a12113f41ba1088b2968849feaf', '68168534ff5065a152bfab275c2136f8', '亩', '2', null, 2, 1, 'admin', to_date('27-07-2019 10:58:48', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('bb661d2df197851226ab2ef6db103aa3', '68168534ff5065a152bfab275c2136f8', '公顷', '3', null, 3, 1, 'admin', to_date('27-07-2019 10:59:33', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9eaa90309f21d5fa998ae36dcdea8a4b', '4c753b5293304e7a445fd2741b46529d', '集体土地所有权', '1', null, 1, 1, 'admin', to_date('27-07-2019 11:01:07', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ca1459c5d1d717b7bdafb70cb51627ca', '4c753b5293304e7a445fd2741b46529d', '国家土地所有权', '2', null, 2, 1, 'admin', to_date('27-07-2019 11:01:26', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('83b82e3b541c5e8d435650de089c5a26', '4c753b5293304e7a445fd2741b46529d', '国有建设用地使用权', '3', '国有建设用地使用权', 3, 1, 'admin', to_date('27-07-2019 11:01:48', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('29999e579234b4e60ae0bee35716c3aa', '4c753b5293304e7a445fd2741b46529d', '国有建设用地使用权/房屋（构筑物）所有权', '4', null, 4, 1, 'admin', to_date('27-07-2019 11:02:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6b4d5054588fd3ffacc55cbe425ba6dc', '4c753b5293304e7a445fd2741b46529d', '宅基地使用权', '5', null, 5, 1, 'admin', to_date('27-07-2019 11:02:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('96c3da04735a88c9c69fc4077a212928', '4c753b5293304e7a445fd2741b46529d', '宅基地使用权/房屋（构筑物）所有权', '6', null, 6, 1, 'admin', to_date('27-07-2019 11:02:37', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('92e4a30e54e5e7ced4897ef319355f20', '4c753b5293304e7a445fd2741b46529d', '集体建设用地使用权', '7', null, 7, 1, 'admin', to_date('27-07-2019 11:02:53', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ac32b2f2d903e70a68625b002f7fec81', '4c753b5293304e7a445fd2741b46529d', '集体建设用地使用权/房屋（构筑物）所有权', '8', null, 8, 1, 'admin', to_date('27-07-2019 11:03:15', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('48f12292226def0799f98c5a30ff0d4f', '4c753b5293304e7a445fd2741b46529d', '土地承包经营权', '9', null, 9, 1, 'admin', to_date('27-07-2019 11:03:31', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('6096a461f31999f8379f289b52ca7a0f', '4c753b5293304e7a445fd2741b46529d', '土地承包经营权/森林、林木所有权', '10', null, 10, 1, 'admin', to_date('27-07-2019 11:03:51', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('4bacf5082301f8586786fe8a8e781ec0', '4c753b5293304e7a445fd2741b46529d', '林地使用权', '11', null, 11, 1, 'admin', to_date('27-07-2019 11:04:17', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('c0e5eec2a2eff412ba0aa755de080e36', '4c753b5293304e7a445fd2741b46529d', '林地使用权/森林、林木使用权', '12', null, 12, 1, 'admin', to_date('27-07-2019 11:04:33', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('428f810c0357cf147f3ab4e87cda19b5', '4c753b5293304e7a445fd2741b46529d', '草原使用权', '13', null, 13, 1, 'admin', to_date('27-07-2019 11:04:50', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('005f486207ba96b00324db0388d60e54', '4c753b5293304e7a445fd2741b46529d', '水域滩涂养殖权', '14', null, 14, 1, 'admin', to_date('27-07-2019 11:05:06', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('5a0283cf200e78523237bc92c9cacb56', '4c753b5293304e7a445fd2741b46529d', '海域使用权', '15', null, 15, 1, 'admin', to_date('27-07-2019 11:05:22', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('3b82283072072dbd023c74c9989e8281', '4c753b5293304e7a445fd2741b46529d', '海域使用权/构（建）筑物所有权', '16', null, 16, 1, 'admin', to_date('27-07-2019 11:05:35', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('30fa77e2fa4b06e20cdbbf7a165eb98f', '4c753b5293304e7a445fd2741b46529d', '无居民海岛使用权', '17', null, 17, 1, 'admin', to_date('27-07-2019 11:05:47', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ba11200da61f99deafea960e5d0bc3bd', '4c753b5293304e7a445fd2741b46529d', '无居民海岛使用权/构（建）筑物所有权', '18', null, 18, 1, 'admin', to_date('27-07-2019 11:06:04', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('50a919cc398d1f5a8d8fab63529f6abe', '4c753b5293304e7a445fd2741b46529d', '地役权', '19', null, 19, 1, 'admin', to_date('27-07-2019 11:06:22', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('c2b9175bcf7fd489de808bfba70c5601', '4c753b5293304e7a445fd2741b46529d', '取水权', '20', null, 20, 1, 'admin', to_date('27-07-2019 11:06:34', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('9568f06a66f3f3ac1127280b44d66368', '4c753b5293304e7a445fd2741b46529d', '探矿权', '21', null, 21, 1, 'admin', to_date('27-07-2019 11:06:56', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a5132826b16f3872069b9d4a0bacdb1b', '4c753b5293304e7a445fd2741b46529d', '采矿权', '22', null, 22, 1, 'admin', to_date('27-07-2019 11:07:17', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('c78dcbdcce5aff18b611115a9907420c', '4c753b5293304e7a445fd2741b46529d', '其它权利', '99', null, 23, 1, 'admin', to_date('27-07-2019 11:07:35', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ee6fb5dc763ae112f04b103542a899e6', '236e8a4baff0db8c62c00dd95632834f', '国有土地', '100', null, 1, 1, 'admin', to_date('27-07-2019 11:09:03', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('a653c49001577cf570b44967a3589a53', '236e8a4baff0db8c62c00dd95632834f', '划拨', '101', null, 2, 1, 'admin', to_date('27-07-2019 11:09:17', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('4059c732f3208d04f4273f3a840fa2a6', '236e8a4baff0db8c62c00dd95632834f', '出让', '102', null, 3, 1, 'admin', to_date('27-07-2019 11:09:29', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('d4f687b0193917595617e5b7edf5b6c9', '236e8a4baff0db8c62c00dd95632834f', '作价出资（入股）', '103', null, 4, 1, 'admin', to_date('27-07-2019 11:09:42', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('b47a1ffef91b99b88973d3d31c4da81b', '236e8a4baff0db8c62c00dd95632834f', '国有土地租赁', '104', null, 5, 1, 'admin', to_date('27-07-2019 11:10:02', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('f5c795b8752279cafdb30f0a656cd187', '236e8a4baff0db8c62c00dd95632834f', '授权经营', '105', null, 6, 1, 'admin', to_date('27-07-2019 11:10:29', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('62efdbdc76f8b68fb29f975de865d142', '236e8a4baff0db8c62c00dd95632834f', '家庭承包', '106', null, 6, 1, 'admin', to_date('27-07-2019 11:10:45', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('ea13fd0d4647403af2a3942b63ce1aad', '236e8a4baff0db8c62c00dd95632834f', '其它方式承包', '107', null, 8, 1, 'admin', to_date('27-07-2019 11:11:05', 'dd-mm-yyyy hh24:mi:ss'), null, null);
insert into BDC_MRPC.SYS_DICT_ITEM (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time)
values ('3348d5b803a79a9c52e66c3a40ae62bb', '236e8a4baff0db8c62c00dd95632834f', '集体土地', '200', null, 9, 1, 'admin', to_date('27-07-2019 11:11:24', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('27-07-2019 11:11:36', 'dd-mm-yyyy hh24:mi:ss'));

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('1a9a1f01bf7c455f8b127a56dfcdb117', 'a010dd92f6ef4a1ea2732e3996d9c28f', '首次登记', '100', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('914e969f03584fd2ae783a30cc063b04', 'a010dd92f6ef4a1ea2732e3996d9c28f', '转移登记', '200', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('427a1b68f7fe4b9e87fdd2ba1c0e8900', 'a010dd92f6ef4a1ea2732e3996d9c28f', '变更登记', '300', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('ac24e366e0ce430591d1da7d934cceca', 'a010dd92f6ef4a1ea2732e3996d9c28f', '注销登记', '400', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('f7f11feacea04b9b8875e0ef8541fd8a', 'a010dd92f6ef4a1ea2732e3996d9c28f', '更正登记', '500', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('1312ca530736483a83026ec3b8c1b5c5', 'a010dd92f6ef4a1ea2732e3996d9c28f', '异议登记', '600', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('249dbb4b9aca47c8a60bb09dbc87e9dc', 'a010dd92f6ef4a1ea2732e3996d9c28f', '预告登记', '700', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('e92e94dbe0eb480e966e0f8e20222477', 'a010dd92f6ef4a1ea2732e3996d9c28f', '查封登记', '800', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('7a0670cb064b4cfaba3ff70b1cb6e9bb', 'a010dd92f6ef4a1ea2732e3996d9c28f', '补证登记', '900', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('92fbe868957a48f8a5b0cbc6565323fd', '36e90421fd4940c5b1e935cde5d2c5d2', '柳州市市辖区', '450201', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('2b12974887504b18b6c4cc1630d65b5f', '36e90421fd4940c5b1e935cde5d2c5d2', '柳州市城中区', '450202', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('de7999fd9c40482b87288ff691d10ff8', '36e90421fd4940c5b1e935cde5d2c5d2', '柳州市鱼峰区', '450203', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('051e61e025b4471f9dae1c78e757795a', '36e90421fd4940c5b1e935cde5d2c5d2', '柳州市柳南区', '450204', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('7c28422402b44fbb9749f5c66056f9d2', '36e90421fd4940c5b1e935cde5d2c5d2', '柳州市柳北区', '450205', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into bdc_mrpc.sys_dict_item (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('3eeaea9f400c4fe0861e56f30263e23e', '36e90421fd4940c5b1e935cde5d2c5d2', '柳州市柳江区', '450206', null, 1, 1, 'admin', to_date('29-07-2019 18:01:20', 'dd-mm-yyyy hh24:mi:ss'), null, null);

--菜单配置SQL
insert into bdc_mrpc.sys_permission (ID, PARENT_ID, NAME, URL, COMPONENT, COMPONENT_NAME, REDIRECT, MENU_TYPE, PERMS, PERMS_TYPE, SORT_NO, ALWAYS_SHOW, ICON, IS_ROUTE, IS_LEAF, KEEP_ALIVE, HIDDEN, DESCRIPTION, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, DEL_FLAG, RULE_FLAG, STATUS)
values ('033192b7102274236236fe65811f6677', null, '不动产页面管理', '/mortgagerpc', 'layouts/RouteView', null, null, 0, null, '1', 2, 0, 'warning', 1, 0, 0, 0, null, 'admin', to_date('28-07-2019 16:05:21', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('30-07-2019 14:53:30', 'dd-mm-yyyy hh24:mi:ss'), 0, null, '1');

insert into bdc_mrpc.sys_permission (ID, PARENT_ID, NAME, URL, COMPONENT, COMPONENT_NAME, REDIRECT, MENU_TYPE, PERMS, PERMS_TYPE, SORT_NO, ALWAYS_SHOW, ICON, IS_ROUTE, IS_LEAF, KEEP_ALIVE, HIDDEN, DESCRIPTION, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, DEL_FLAG, RULE_FLAG, STATUS)
values ('e581c30ab6f65fd42caee3bd06548121', '033192b7102274236236fe65811f6677', '流程定义配置', '/mortgagerpc/wfiprodef', 'mortgagerpc/wfiprodef/Wfi_prodefList', null, null, 1, null, '1', 0, 0, 'arrow-up', 1, 1, 0, 0, null, 'admin', to_date('28-07-2019 17:37:44', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('30-07-2019 14:53:39', 'dd-mm-yyyy hh24:mi:ss'), 0, 1, '1');

insert into bdc_mrpc.sys_permission (ID, PARENT_ID, NAME, URL, COMPONENT, COMPONENT_NAME, REDIRECT, MENU_TYPE, PERMS, PERMS_TYPE, SORT_NO, ALWAYS_SHOW, ICON, IS_ROUTE, IS_LEAF, KEEP_ALIVE, HIDDEN, DESCRIPTION, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, DEL_FLAG, RULE_FLAG, STATUS)
values ('76fed9bb943668d2638a3984ea9713c3', '033192b7102274236236fe65811f6677', '流程卡片模式', '/mortgagerpc/wfiprodef/prodefCardList', 'mortgagerpc/wfiprodef/prodefCardList', null, null, 1, null, '1', 1, 0, 'plus-square', 1, 1, 0, 0, null, 'admin', to_date('30-07-2019 15:59:06', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('30-07-2019 17:08:40', 'dd-mm-yyyy hh24:mi:ss'), 0, null, '1');


--菜单授权SQL
insert into bdc_mrpc.sys_role_permission (ID, ROLE_ID, PERMISSION_ID, DATA_RULE_IDS)
values ('f83df5c13f79eca2c102a6906a07b08d', 'f6817f48af4fb3af11b9e8bf182f618b', '033192b7102274236236fe65811f6677', null);

insert into bdc_mrpc.sys_role_permission (ID, ROLE_ID, PERMISSION_ID, DATA_RULE_IDS)
values ('c0e64d4e93720110617435e3f6b2133f', 'f6817f48af4fb3af11b9e8bf182f618b', 'e581c30ab6f65fd42caee3bd06548121', null);

insert into bdc_mrpc.sys_permission (ID, PARENT_ID, NAME, URL, COMPONENT, COMPONENT_NAME, REDIRECT, MENU_TYPE, PERMS, PERMS_TYPE, SORT_NO, ALWAYS_SHOW, ICON, IS_ROUTE, IS_LEAF, KEEP_ALIVE, HIDDEN, DESCRIPTION, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, DEL_FLAG, RULE_FLAG, STATUS)
values ('76fed9bb943668d2638a3984ea9713c3', '033192b7102274236236fe65811f6677', '流程卡片模式', '/mortgagerpc/wfiprodef/prodefCardList', 'mortgagerpc/wfiprodef/prodefCardList', null, null, 1, null, '1', 1, 0, 'plus-square', 1, 1, 0, 0, null, 'admin', to_date('30-07-2019 15:59:06', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('30-07-2019 17:08:40', 'dd-mm-yyyy hh24:mi:ss'), 0, null, '1');


commit;



