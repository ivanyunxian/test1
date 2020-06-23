
/*==============================================================*/
/* Table: SYS_DEPART 添加字段 DEPT_TYPE                          */
/*==============================================================*/
ALTER TABLE BDC_MRPC.SYS_DEPART ADD DEPT_TYPE NVARCHAR2(10);
ALTER TABLE BDC_MRPC.SYS_DEPART ADD DIVISION_CODE NVARCHAR2(10);
COMMENT ON COLUMN BDC_MRPC.SYS_DEPART.DEPT_TYPE IS '部门类型（登记中心、系统运维、金融机构等）';
COMMENT ON COLUMN BDC_MRPC.SYS_DEPART.DIVISION_CODE IS '区划代码';

-- 插入字典值
insert into BDC_MRPC.SYS_DICT (ID, DICT_NAME, DICT_CODE, DESCRIPTION, DEL_FLAG, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, TYPE)
values ('0a4dc9282813dce065771acb2e8b498b', '部门类型', 'DEPT_TYPE', '登记中心、系统运维、金融机构', 0, 'admin', to_date('06-08-2019 16:35:59', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('06-08-2019 16:52:10', 'dd-mm-yyyy hh24:mi:ss'), null);

insert into BDC_MRPC.SYS_DICT_ITEM (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('fc49138d81c05304be59e931768c1f01', '0a4dc9282813dce065771acb2e8b498b', '登记中心', '0', null, 1, 1, 'admin', to_date('06-08-2019 16:36:34', 'dd-mm-yyyy hh24:mi:ss'), 'admin', to_date('06-08-2019 16:37:09', 'dd-mm-yyyy hh24:mi:ss'));

insert into BDC_MRPC.SYS_DICT_ITEM (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('5472a74dcb1cbc8d1ea1ed6b4ed8b1f4', '0a4dc9282813dce065771acb2e8b498b', '系统运维', '1', null, 1, 1, 'admin', to_date('06-08-2019 16:37:01', 'dd-mm-yyyy hh24:mi:ss'), null, null);

insert into BDC_MRPC.SYS_DICT_ITEM (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('7e6f56809e357d0f1f177a6c2198e82e', '0a4dc9282813dce065771acb2e8b498b', '金融机构', '2', null, 2, 1, 'admin', to_date('06-08-2019 16:37:23', 'dd-mm-yyyy hh24:mi:ss'), null, null);


/*==============================================================*/
/* Table: SYS_USER 添加字段 DEPT_ID                          	*/
/*==============================================================*/
ALTER TABLE BDC_MRPC.SYS_USER ADD DEPT_ID NVARCHAR2(32);
COMMENT ON COLUMN BDC_MRPC.SYS_USER.DEPT_ID IS '部门ID（登录时选择的部门）';

/*==============================================================*/
/* Table: SYS_DEPART 添加字段：DEPT_ZJLX、DEPT_ZJH              */
/*==============================================================*/
ALTER TABLE BDC_MRPC.SYS_DEPART ADD DEPT_ZJLX NVARCHAR2(10);
ALTER TABLE BDC_MRPC.SYS_DEPART ADD DEPT_ZJH NVARCHAR2(100);
COMMENT ON COLUMN BDC_MRPC.SYS_DEPART.DEPT_ZJLX IS '部门证件类型';
COMMENT ON COLUMN BDC_MRPC.SYS_DEPART.DEPT_ZJH IS '部门证件号';

--工作流资料目录表创建语句
CREATE TABLE BDC_MRPC.WFI_MATERCLASS
(
  ID    NVARCHAR2(64) NOT NULL,
  PROLSH    NVARCHAR2(64),
  MATEDESC  NVARCHAR2(500),
  FILEINDEX NUMBER(10),
  NAME  NVARCHAR2(200),
  PROCODEID   NVARCHAR2(64),
  REQUIRED           NVARCHAR2(10),
  COUNT       NUMBER(10) DEFAULT 0,
  DIVISION_CODE               NVARCHAR2(10)
);
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.ID IS
  '目录ID';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.PROLSH IS
  '项目流水号';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.MATEDESC IS
  '扩展';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.FILEINDEX IS
  '目录序号';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.NAME IS
  '目录名称';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.PROCODEID IS
  '流程ID';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.REQUIRED IS
  '是否必须 0-否 1-是';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.COUNT IS
  '文件数量';
COMMENT ON COLUMN BDC_MRPC.WFI_MATERCLASS.DIVISION_CODE IS
  '行政区划';

alter table BDC_MRPC.WFI_MATERCLASS
   add constraint PK_WFI_MATERCLASS_ID primary key (ID);

--流程资料目录模板表创建语句
CREATE TABLE BDC_MRPC.WFD_MATERCLASS
(
  ID    NVARCHAR2(64) NOT NULL,
  MATEDESC  NVARCHAR2(500),
  FILEINDEX NUMBER(10),
  NAME  NVARCHAR2(200),
  PROCODEID   NVARCHAR2(64),
  REQUIRED           NVARCHAR2(10),
  DIVISION_CODE               NVARCHAR2(10)
);
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.ID IS
  '目录ID';
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.MATEDESC IS
  '扩展';
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.FILEINDEX IS
  '目录序号';
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.NAME IS
  '目录名称';
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.PROCODEID IS
  '流程ID';
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.REQUIRED IS
  '是否必须 0-否 1-是';
COMMENT ON COLUMN BDC_MRPC.WFD_MATERCLASS.DIVISION_CODE IS
  '行政区划';

alter table BDC_MRPC.WFD_MATERCLASS
   add constraint PK_WFD_MATERCLASS_ID primary key (ID);

--附件资料表
CREATE TABLE "BDC_MRPC"."WFI_MATERDATA" 
   (  ID VARCHAR2(32) NOT NULL ENABLE, 
  NAME VARCHAR2(500), 
  PATH VARCHAR2(100), 
  SUFFIX VARCHAR2(50), 
  CREATED DATE, 
  OPBY VARCHAR2(50), 
  STATUS NUMBER, 
  MATERINST_ID VARCHAR2(32), 
  PROLSH VARCHAR2(36), 
  FILEINDEX NUMBER(16,0), 
  mongoid VARCHAR2(50), 
   CONSTRAINT PK_WFI_MATERDATA_ID PRIMARY KEY (ID)
 
   ) ;
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".ID IS '编号 ID';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".NAME IS '资料名称 NAME';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".PATH IS '路径 PATH';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".SUFFIX IS '扩展名 SUFFIX';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".CREATED IS '上传时间 CREATED';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".OPBY IS 'OPBY';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".STATUS IS '状态 STATUS';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".MATERINST_ID IS '模板实例编号 MATERINST_ID';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".PROLSH IS '受理项目编号';
 
   COMMENT ON COLUMN "BDC_MRPC"."WFI_MATERDATA".FILEINDEX IS '文件序号';
 
   COMMENT ON TABLE "BDC_MRPC"."WFI_MATERDATA"  IS '附件资料';
   
    COMMENT ON TABLE "BDC_MRPC"."mongoid"  IS 'mongoid';

/*==============================================================*/
/* Table: SYS_USER 添加字段  PASSWORD_LEVEL                     */
/*==============================================================*/
ALTER TABLE BDC_MRPC.SYS_USER ADD PASSWORD_LEVEL NVARCHAR2(32);
COMMENT ON COLUMN BDC_MRPC.SYS_USER.PASSWORD_LEVEL IS '密码等级';


-- 房源核验接口地址参数配置
insert into SYS_CONFIG (ID, DIVISION_CODE, NAME, VALUE, TYPE, CREATETIME, MEMO, CONFIG_KEY)
values ('3cd7aefe5dcc26a5879ee57ebe38cf85', null, '房屋核验接口地址', 'http://localhost:8080/MortgageRPC-CoreAPI/app/mrpc/test', null, to_date('13-08-2019 17:26:23', 'dd-mm-yyyy hh24:mi:ss'), null, 'houseSearchUrl');

-- 核心 API 业务申报接口地址参数配置
insert into sys_config (ID, DIVISION_CODE, NAME, VALUE, TYPE, CREATETIME, MEMO, CONFIG_KEY)
values ('4e2da64a44653daa9a0a58df3b14073b', null, '核心 API 业务申报接口', 'http://localhost:8080/MortgageRPC-CoreAPI/app/intelligentcore/declare', null, to_date('13-08-2019 19:23:51', 'dd-mm-yyyy hh24:mi:ss'), null, 'declareUrl');


ALTER TABLE BDC_MRPC.WFI_MATERDATA ADD MONGOID NVARCHAR2(64);
COMMENT ON COLUMN BDC_MRPC.WFI_MATERDATA.MONGOID IS 'mongodbid';

insert into SYS_CONFIG (ID, DIVISION_CODE, NAME, VALUE, TYPE, CREATETIME, MEMO, CONFIG_KEY)
values ('bf4985ae8642776b96d1413e2bd3e32d', null, '办理进度查询接口', 'http://localhost:8081/MortgageRPC-CoreAPI/app/mrpc/test', null, to_date('19-08-2019 09:50:18', 'dd-mm-yyyy hh24:mi:ss'), null, 'progressUrl');

ALTER TABLE WFI_MATERDATA MODIFY NAME  NVARCHAR2(200)


/*==============================================================*/
/* Table: BDC_QLDY 添加字段 SQRID                          	*/
/*==============================================================*/
ALTER TABLE BDC_MRPC.BDC_QLDY ADD SQRID NVARCHAR2(50);
COMMENT ON COLUMN BDC_MRPC.BDC_QLDY.SQRID IS '申请人ID';
-- Create/Recreate indexes
CREATE INDEX IDX_BDC_QLDY_PROLSH ON BDC_MRPC.BDC_QLDY (PROLSH);
CREATE INDEX IDX_BDC_QLDY_DYID ON BDC_MRPC.BDC_QLDY (DYID);
CREATE INDEX IDX_BDC_QLDY_SQRID ON BDC_MRPC.BDC_QLDY (SQRID);

/*==============================================================*/
/* Table: BDC_DY 添加字段 YCJZMJ、YCTNJZMJ                      */
/*==============================================================*/
ALTER TABLE BDC_MRPC.BDC_DY ADD YCJZMJ NUMBER(38,16);
COMMENT ON COLUMN BDC_MRPC.BDC_DY.YCJZMJ IS '预测建筑面积';
ALTER TABLE BDC_MRPC.BDC_DY ADD YCTNJZMJ NUMBER(38,16);
COMMENT ON COLUMN BDC_MRPC.BDC_DY.YCTNJZMJ IS '预测套内建筑面积';


/*==============================================================*/
/* Table: 添加表 HOUSESHIS - 房屋核验记录表                     */
/*==============================================================*/
create table BDC_MRPC.HOUSESHIS
(
	ID VARCHAR2(100) not null
		primary key,
	PROLSH NVARCHAR2(50),
	BDCDYH NVARCHAR2(100),
	ZL NVARCHAR2(500),
	YCJZMJ NUMBER,
	YCTNJZMJ NUMBER,
	SCJZMJ NUMBER,
	SCTNJZMJ NUMBER,
	CFZT VARCHAR2(10),
	DYZT VARCHAR2(10),
	YYZT VARCHAR2(10),
	OPERATOR VARCHAR2(100),
	CREATETIME DATE,
	FYZT NUMBER default 1,
	HOUSECLOB CLOB
);

comment on table BDC_MRPC.HOUSESHIS is '房屋核验记录表';

comment on column BDC_MRPC.HOUSESHIS.ID is '主键ID';

comment on column BDC_MRPC.HOUSESHIS.PROLSH is '业务流水号';

comment on column BDC_MRPC.HOUSESHIS.BDCDYH is '不动产单元号';

comment on column BDC_MRPC.HOUSESHIS.ZL is '坐落';

comment on column BDC_MRPC.HOUSESHIS.YCJZMJ is '预测建筑面积';

comment on column BDC_MRPC.HOUSESHIS.YCTNJZMJ is '预测套内建筑面积';

comment on column BDC_MRPC.HOUSESHIS.SCJZMJ is '实测建筑面积';

comment on column BDC_MRPC.HOUSESHIS.SCTNJZMJ is '实测套内建筑面积';

comment on column BDC_MRPC.HOUSESHIS.CFZT is '查封状态';

comment on column BDC_MRPC.HOUSESHIS.DYZT is '抵押状态';

comment on column BDC_MRPC.HOUSESHIS.YYZT is '异议状态';

comment on column BDC_MRPC.HOUSESHIS.OPERATOR is '操作人员';

comment on column BDC_MRPC.HOUSESHIS.CREATETIME is '操作时间';

comment on column BDC_MRPC.HOUSESHIS.FYZT is '房源状态 1-未选择 2-已选择 3-已失效';

comment on column BDC_MRPC.HOUSESHIS.HOUSECLOB is '房源接口返回内容';

-- Create/Recreate indexes
CREATE INDEX IDX_HOUSESHIS_PROLSH ON BDC_MRPC.HOUSESHIS (PROLSH);
CREATE INDEX IDX_HOUSESHIS_FYZT ON BDC_MRPC.HOUSESHIS (FYZT);



/*==============================================================*/
/* Table: sys_depart 添加字段 fddbr,fddbrzjlx,fddbrzjhm                          	*/
/*==============================================================*/
ALTER TABLE BDC_MRPC.sys_depart ADD fddbr NVARCHAR2(100);
COMMENT ON COLUMN BDC_MRPC.sys_depart.fddbr IS '法人姓名';

ALTER TABLE BDC_MRPC.sys_depart ADD fddbrzjlx NVARCHAR2(10);
COMMENT ON COLUMN BDC_MRPC.sys_depart.fddbrzjlx IS '法人证件类型';

ALTER TABLE BDC_MRPC.sys_depart ADD fddbrzjhm NVARCHAR2(100);
COMMENT ON COLUMN BDC_MRPC.sys_depart.fddbrzjhm IS '法人证件号';
/*==============================================================*/
/* Table: BDC_QLDY 添加字段 FSQLID-附属权利ID                   	*/
/*==============================================================*/
ALTER TABLE BDC_MRPC.BDC_QLDY ADD FSQLID NVARCHAR2(100);

COMMENT ON COLUMN BDC_MRPC.BDC_QLDY.FSQLID IS '附属权利ID';
-- Create/Recreate indexes

CREATE INDEX IDX_BDC_QLDY_FSQLID ON BDC_MRPC.BDC_QLDY (FSQLID);


/*==============================================================*/
/* Table:BDC_QL表添加字段 ZXFJ、DYWLX、ZQDW                   	*/
/*==============================================================*/
ALTER TABLE BDC_MRPC.BDC_QL ADD ZXFJ NVARCHAR2(1000);
ALTER TABLE BDC_MRPC.BDC_QL ADD DYWLX NVARCHAR2(10);
ALTER TABLE BDC_MRPC.BDC_QL ADD ZQDW NVARCHAR2(10);

COMMENT ON COLUMN BDC_MRPC.BDC_QL.ZXFJ IS '注销附记';
COMMENT ON COLUMN BDC_MRPC.BDC_QL.DYWLX IS '抵押物类型';
COMMENT ON COLUMN BDC_MRPC.BDC_QL.ZQDW IS '债权单位';


/*==============================================================*/
/* Table: 添加表 BDC_FSQL - 附属权利表                           */
/*==============================================================*/
CREATE TABLE BDC_MRPC.BDC_FSQL
(
	FSQLID      NVARCHAR2(100) NOT NULL PRIMARY KEY,
	PROLSH      NVARCHAR2(50),
	DYR         NVARCHAR2(200),
	ZWR         NVARCHAR2(200),
	ZWRZJH      NVARCHAR2(200),
	ZJJZWDYFW   NVARCHAR2(200),
	ZGZQQDSS    NVARCHAR2(200),
	DGBDBZZQSE  NUMBER(38,16),
	CREATE_TIME	DATE,
	MODIFYTIME	DATE,
	VERSIONNO	NVARCHAR2(50)
);

COMMENT ON TABLE BDC_MRPC.BDC_FSQL IS '附属权利表';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.FSQLID IS '附属权利ID';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.PROLSH IS '业务流水号';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.DYR IS '抵押人';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.ZWR IS '债务人';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.ZWRZJH IS '债务人证件号';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.ZJJZWDYFW IS '在建建筑物抵押范围';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.ZGZQQDSS IS '最高债权确定事实';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.DGBDBZZQSE IS '单个被担保主债权数额';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.DGBDBZZQSE IS '创建时间';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.DGBDBZZQSE IS '修改时间';

COMMENT ON COLUMN BDC_MRPC.BDC_FSQL.DGBDBZZQSE IS '版本号';

-- Create/Recreate indexes
CREATE INDEX IDX_BDC_FSQL_PROLSH ON BDC_MRPC.BDC_FSQL (PROLSH);


/*==============================================================*/
/* Table: 添加表 WFI_PROINST - 添加登簿时间字段DBSJ                           */
/*==============================================================*/

ALTER TABLE BDC_MRPC.Wfi_proinst ADD DBSJ Date;
COMMENT ON COLUMN BDC_MRPC.Wfi_proinst.DBSJ IS '登簿时间';


/*==============================================================*/
/* Table: 添加表 WFI_SLXMSH - 添加审核过程表                           */
/*==============================================================*/
CREATE TABLE BDC_MRPC.WFI_SLXMSH
(
  ID NVARCHAR2(100) NOT NULL
    PRIMARY KEY,
  PROLSH VARCHAR2(100),
  SHRY VARCHAR2(200),
  SHYJ NVARCHAR2(1000),
  SHSJ DATE,
  SHZT NUMBER DEFAULT NULL
);
COMMENT ON COLUMN BDC_MRPC.WFI_SLXMSH.id IS '主键id';
COMMENT ON COLUMN BDC_MRPC.WFI_SLXMSH.PROLSH IS '业务流水号';
COMMENT ON COLUMN BDC_MRPC.WFI_SLXMSH.shry IS '审核人员';
COMMENT ON COLUMN BDC_MRPC.WFI_SLXMSH.shyj IS '审核意见';
COMMENT ON COLUMN BDC_MRPC.WFI_SLXMSH.shsj IS '审核时间';
COMMENT ON COLUMN BDC_MRPC.WFI_SLXMSH.shzt IS '项目审核状态';
COMMENT ON TABLE BDC_MRPC.WFI_SLXMSH IS '审核过程表';

/*==============================================================*/
/* Table: 添加表 审核状态字典 -11 提交失败                     */
/*==============================================================*/
insert into SYS_DICT_ITEM (ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
values ('861dea3a13f1e547e49e8468694d745b', '085c594c3d8fcf56e64f19f0ded32eb1', '提交失败', '-11', '项目创建接口异常', 1, 1, 'admin', to_date('30-08-2019 17:19:47', 'dd-mm-yyyy hh24:mi:ss'), null, null);


-- 原房源核验、办理进度查询接口删除
delete bdc_mrpc.SYS_CONFIG t where t.id in('bf4985ae8642776b96d1413e2bd3e32d','3cd7aefe5dcc26a5879ee57ebe38cf85');

-- 核心查询接口配置添加
insert into bdc_mrpc.SYS_CONFIG (ID, DIVISION_CODE, NAME, VALUE, TYPE, CREATETIME, MEMO, CONFIG_KEY)
values ('58be6643cdc591593e0367f28f067b54', null, '核心查询接口', 'http://localhost:8099/MortgageRPC-CoreAPI/app/mrpc/coreQueryAlias', null, to_date('02-09-2019 16:26:01', 'dd-mm-yyyy hh24:mi:ss'), '核心查询接口，“证书、证明、房源核验以及进度查询”，配置自定义别名版', 'coreQueryUrl');



/*=============================OVER 结束标记，最新脚本按规范前往“004_BDC_MRPC库升级脚本” 进行更新=================================*/

/*=============================OVER 结束标记，最新脚本按规范前往“004_BDC_MRPC库升级脚本” 进行更新=================================*/

/*=============================OVER 结束标记，最新脚本按规范前往“004_BDC_MRPC库升级脚本” 进行更新=================================*/
































