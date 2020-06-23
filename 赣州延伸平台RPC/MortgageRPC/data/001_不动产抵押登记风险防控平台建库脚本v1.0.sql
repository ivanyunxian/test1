--��������Ѻ�Ǽ�ƽ̨����ű�
--vision��v1.0
--date:2019-07-26
--author:wzn
--��ѯ��ʱ��ռ�
SELECT * FROM DBA_TABLESPACE_GROUPS;
--ɾ����ʱ��ռ�
ALTER TABLESPACE sm_tempspace01 TABLESPACE GROUP '';
/
DROP TABLESPACE sm_tempspace01 INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;
/
ALTER TABLESPACE sm_tempspace02 TABLESPACE GROUP '';
/
DROP TABLESPACE sm_tempspace02 INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;
/
ALTER TABLESPACE sm_tempspace03 TABLESPACE GROUP '';
/
DROP TABLESPACE sm_tempspace03 INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;
/
ALTER TABLESPACE sm_tempspace04 TABLESPACE GROUP '';
/
DROP TABLESPACE sm_tempspace04 INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;
/
ALTER TABLESPACE sm_tempspace05 TABLESPACE GROUP '';
/
DROP TABLESPACE sm_tempspace05 INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;
/
--000������ʱ��ռ���
create temporary tablespace sm_tempspace01 tempfile '/oracle/oraspace/sm_tempspace01.dbf' size 10m autoextend on next 50m  tablespace group sm_tempspacegroup;
/
create temporary tablespace sm_tempspace02 tempfile '/oracle/oraspace/sm_tempspace02.dbf' size 10m autoextend on next 50m  tablespace group sm_tempspacegroup;
/
create temporary tablespace sm_tempspace03 tempfile '/oracle/oraspace/sm_tempspace03.dbf' size 10m autoextend on next 50m  tablespace group sm_tempspacegroup;
/
create temporary tablespace sm_tempspace04 tempfile '/oracle/oraspace/sm_tempspace04.dbf' size 10m autoextend on next 50m  tablespace group sm_tempspacegroup;
/
create temporary tablespace sm_tempspace05 tempfile '/oracle/oraspace/sm_tempspace05.dbf' size 10m autoextend on next 50m  tablespace group sm_tempspacegroup;
/
--ɾ����ռ�
drop tablespace BDC_MRPC  including contents and datafiles;
 /
--001������ռ�
create tablespace BDC_MRPC datafile '/oracle/oradata/BDC_MRPC_001.dbf' size 100m reuse autoextend on next 10m maxsize unlimited logging online;
/
--002��չ��ռ�
alter tablespace BDC_MRPC add datafile '/oracle/oradata/BDC_MRPC_002.dbf' size 100m reuse autoextend on next 10m maxsize unlimited 
/
--003��չ��ռ�
alter tablespace BDC_MRPC add datafile '/oracle/oradata/BDC_MRPC_003.dbf' size 100m reuse autoextend on next 10m maxsize unlimited 
/
--ɾ���û�
drop user BDC_MRPC cascade;
/
--004�����û�
create user BDC_MRPC identified by supermap2019 default tablespace BDC_MRPC temporary tablespace sm_tempspacegroup profile default;
--005�����ɫȨ��
grant connect to BDC_MRPC;
GRANT RESOURCE TO BDC_MRPC;
--006����ϵͳȨ��
grant create type to BDC_MRPC;
grant alter any indextype to BDC_MRPC;
grant create table to BDC_MRPC;
grant alter user to BDC_MRPC;
grant select any table to BDC_MRPC;
grant alter any trigger to BDC_MRPC;
grant alter any sequence to BDC_MRPC;
grant alter any index to BDC_MRPC;
grant create role to BDC_MRPC;
grant unlimited tablespace to BDC_MRPC;
grant create rule to BDC_MRPC;
grant alter any table to BDC_MRPC;
grant create trigger to BDC_MRPC;
--009��������������
truncate table BDC_MRPC.bdc_dy DROP STORAGE;
--010������
alter table BDC_MRPC.QRTZ_BLOB_TRIGGERS
   drop constraint SYS_C0025584
/


alter table BDC_MRPC.QRTZ_CRON_TRIGGERS
   drop constraint SYS_C0025585
/

alter table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS
   drop constraint SYS_C0025586
/

alter table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS
   drop constraint SYS_C0025587
/

alter table BDC_MRPC.QRTZ_TRIGGERS
   drop constraint SYS_C0025588
/

alter table BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS
   drop primary key cascade
/

drop table BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS cascade constraints
/

alter table BDC_MRPC.BDC_ORDER_CUSTOMER
   drop primary key cascade
/

drop table BDC_MRPC.BDC_ORDER_CUSTOMER cascade constraints
/

alter table BDC_MRPC.BDC_ORDER_MAIN
   drop primary key cascade
/

drop table BDC_MRPC.BDC_ORDER_MAIN cascade constraints
/

alter table BDC_MRPC.BDC_ORDER_TICKET
   drop primary key cascade
/

drop table BDC_MRPC.BDC_ORDER_TICKET cascade constraints
/

alter table BDC_MRPC.BDC_PROJECT_NATURE_INCOME
   drop primary key cascade
/

drop table BDC_MRPC.BDC_PROJECT_NATURE_INCOME cascade constraints
/

drop table BDC_MRPC.JOA_DEMO cascade constraints
/

alter table BDC_MRPC.QRTZ_BLOB_TRIGGERS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_BLOB_TRIGGERS cascade constraints
/

alter table BDC_MRPC.QRTZ_CALENDARS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_CALENDARS cascade constraints
/

alter table BDC_MRPC.QRTZ_CRON_TRIGGERS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_CRON_TRIGGERS cascade constraints
/

alter table BDC_MRPC.QRTZ_FIRED_TRIGGERS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_FIRED_TRIGGERS cascade constraints
/

alter table BDC_MRPC.QRTZ_JOB_DETAILS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_JOB_DETAILS cascade constraints
/

alter table BDC_MRPC.QRTZ_LOCKS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_LOCKS cascade constraints
/

alter table BDC_MRPC.QRTZ_PAUSED_TRIGGER_GRPS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_PAUSED_TRIGGER_GRPS cascade constraints
/

alter table BDC_MRPC.QRTZ_SCHEDULER_STATE
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_SCHEDULER_STATE cascade constraints
/

alter table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS cascade constraints
/

alter table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS cascade constraints
/

drop index BDC_MRPC.SCHED_NAME
/

alter table BDC_MRPC.QRTZ_TRIGGERS
   drop primary key cascade
/

drop table BDC_MRPC.QRTZ_TRIGGERS cascade constraints
/

alter table BDC_MRPC.STAFF
   drop primary key cascade
/

drop table BDC_MRPC.STAFF cascade constraints
/

alter table BDC_MRPC.SYS_ANNOUNCEMENT
   drop primary key cascade
/

drop table BDC_MRPC.SYS_ANNOUNCEMENT cascade constraints
/

drop table BDC_MRPC.SYS_ANNOUNCEMENT_SEND cascade constraints
/

alter table BDC_MRPC.SYS_CATEGORY
   drop primary key cascade
/

drop table BDC_MRPC.SYS_CATEGORY cascade constraints
/

alter table BDC_MRPC.SYS_CONFIG
   drop primary key cascade
/

drop table BDC_MRPC.SYS_CONFIG cascade constraints
/

drop index BDC_MRPC.SINDEX
/

alter table BDC_MRPC.SYS_DATA_LOG
   drop primary key cascade
/

drop table BDC_MRPC.SYS_DATA_LOG cascade constraints
/

drop index BDC_MRPC.INDEX_DEPART_PARENT_ID
/

drop index BDC_MRPC.INDEX_DEPART_ORG_CODE
/

drop index BDC_MRPC.INDEX_DEPART_DEPART_ORDER
/

alter table BDC_MRPC.SYS_DEPART
   drop primary key cascade
/

drop table BDC_MRPC.SYS_DEPART cascade constraints
/

drop index BDC_MRPC.INDEXTABLE_DICT_CODE
/

alter table BDC_MRPC.SYS_DICT
   drop primary key cascade
/

drop table BDC_MRPC.SYS_DICT cascade constraints
/

drop index BDC_MRPC.INDEX_TABLE_SORT_ORDER
/

drop index BDC_MRPC.INDEX_TABLE_DICT_STATUS
/

drop index BDC_MRPC.INDEX_TABLE_DICT_ID
/

alter table BDC_MRPC.SYS_DICT_ITEM
   drop primary key cascade
/

drop table BDC_MRPC.SYS_DICT_ITEM cascade constraints
/

drop index BDC_MRPC.INDEX_TABLE_USERID
/

drop index BDC_MRPC.INDEX_OPERATE_TYPE
/

drop index BDC_MRPC.INDEX_LOGT_YPE
/

alter table BDC_MRPC.SYS_LOG
   drop primary key cascade
/

drop table BDC_MRPC.SYS_LOG cascade constraints
/

drop index BDC_MRPC.INDEX_PREM_SORT_NO
/

drop index BDC_MRPC.INDEX_PREM_PID
/

drop index BDC_MRPC.INDEX_PREM_IS_ROUTE
/

drop index BDC_MRPC.INDEX_PREM_IS_LEAF
/

drop index BDC_MRPC.INDEX_PREM_DEL_FLAG
/

alter table BDC_MRPC.SYS_PERMISSION
   drop primary key cascade
/

drop table BDC_MRPC.SYS_PERMISSION cascade constraints
/

drop index BDC_MRPC.INDEX_FUCNTIONID
/

alter table BDC_MRPC.SYS_PERMISSION_DATA_RULE
   drop primary key cascade
/

drop table BDC_MRPC.SYS_PERMISSION_DATA_RULE cascade constraints
/

alter table BDC_MRPC.SYS_QUARTZ_JOB
   drop primary key cascade
/

drop table BDC_MRPC.SYS_QUARTZ_JOB cascade constraints
/

drop index BDC_MRPC.INDEX_ROLE_CODE
/

alter table BDC_MRPC.SYS_ROLE
   drop primary key cascade
/

drop table BDC_MRPC.SYS_ROLE cascade constraints
/

drop index BDC_MRPC.INDEX_GROUP_ROLE_PER_ID
/

drop index BDC_MRPC.INDEX_GROUP_ROLE_ID
/

drop index BDC_MRPC.INDEX_GROUP_PER_ID
/

alter table BDC_MRPC.SYS_ROLE_PERMISSION
   drop primary key cascade
/

drop table BDC_MRPC.SYS_ROLE_PERMISSION cascade constraints
/

drop index BDC_MRPC.INDEX_TYPE
/

drop index BDC_MRPC.INDEX_STATUS
/

drop index BDC_MRPC.INDEX_SENDTIME
/

drop index BDC_MRPC.INDEX_RECEIVER
/

alter table BDC_MRPC.SYS_SMS
   drop primary key cascade
/

drop table BDC_MRPC.SYS_SMS cascade constraints
/

drop index BDC_MRPC.UNIQ_TEMPLATECODE
/

alter table BDC_MRPC.SYS_SMS_TEMPLATE
   drop primary key cascade
/

drop table BDC_MRPC.SYS_SMS_TEMPLATE cascade constraints
/

drop index BDC_MRPC.INDEX_USER_STATUS
/

drop index BDC_MRPC.INDEX_USER_NAME
/

drop index BDC_MRPC.INDEX_USER_DEL_FLAG
/

alter table BDC_MRPC.SYS_USER
   drop primary key cascade
/

drop table BDC_MRPC.SYS_USER cascade constraints
/

drop index BDC_MRPC.UNIQ_USERNAME
/

drop index BDC_MRPC.STATUX_INDEX
/

drop index BDC_MRPC.ENDTIME_INDEX
/

drop index BDC_MRPC.BEGINTIME_INDEX
/

alter table BDC_MRPC.SYS_USER_AGENT
   drop primary key cascade
/

drop table BDC_MRPC.SYS_USER_AGENT cascade constraints
/

drop index BDC_MRPC.INDEX_DEPART_GROUPK_USERID
/

drop index BDC_MRPC.INDEX_DEPART_GROUPK_UIDANDDID
/

drop index BDC_MRPC.INDEX_DEPART_GROUPKORGID
/

alter table BDC_MRPC.SYS_USER_DEPART
   drop primary key cascade
/

drop table BDC_MRPC.SYS_USER_DEPART cascade constraints
/

drop index BDC_MRPC.INDEX2_GROUPUU_USER_ID
/

drop index BDC_MRPC.INDEX2_GROUPUU_USERIDANDROLEID
/

drop index BDC_MRPC.INDEX2_GROUPUU_OLE_ID
/

alter table BDC_MRPC.SYS_USER_ROLE
   drop primary key cascade
/

drop table BDC_MRPC.SYS_USER_ROLE cascade constraints
/

alter table BDC_MRPC.TEST_PERSON
   drop primary key cascade
/

drop table BDC_MRPC.TEST_PERSON cascade constraints
/

/*==============================================================*/
/* Table: BDC_MONTHLY_GROWTH_ANALYSIS                           */
/*==============================================================*/
create table BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS 
(
   ID                   NUMBER(11)           not null,
   YEAR                 NVARCHAR2(50),
   MONTH                NVARCHAR2(50),
   MAIN_INCOME          NUMBER,
   OTHER_INCOME         NUMBER
)
/

comment on column BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS.MONTH is
'�·�'
/

comment on column BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS.MAIN_INCOME is
'Ӷ��/��Ӫ����'
/

comment on column BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS.OTHER_INCOME is
'��������'
/

alter table BDC_MRPC.BDC_MONTHLY_GROWTH_ANALYSIS
   add constraint SYS_C0025465 primary key (ID)
/

/*==============================================================*/
/* Table: BDC_ORDER_CUSTOMER                                    */
/*==============================================================*/
create table BDC_MRPC.BDC_ORDER_CUSTOMER 
(
   ID                   NVARCHAR2(32)        not null,
   NAME                 NVARCHAR2(100)       not null,
   SEX                  NVARCHAR2(4),
   IDCARD               NVARCHAR2(18),
   IDCARD_PIC           NVARCHAR2(500),
   TELPHONE             NVARCHAR2(32),
   ORDER_ID             NVARCHAR2(32)        not null,
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.ID is
'����'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.NAME is
'�ͻ���'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.SEX is
'�Ա�'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.IDCARD is
'���֤����'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.IDCARD_PIC is
'���֤ɨ���'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.TELPHONE is
'�绰1'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.ORDER_ID is
'���'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.CREATE_BY is
'������'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.UPDATE_BY is
'�޸���'
/

comment on column BDC_MRPC.BDC_ORDER_CUSTOMER.UPDATE_TIME is
'�޸�ʱ��'
/

alter table BDC_MRPC.BDC_ORDER_CUSTOMER
   add constraint SYS_C0025411 primary key (ID)
/

/*==============================================================*/
/* Table: BDC_ORDER_MAIN                                        */
/*==============================================================*/
create table BDC_MRPC.BDC_ORDER_MAIN 
(
   ID                   NVARCHAR2(32)        not null,
   ORDER_CODE           NVARCHAR2(50),
   CTYPE                NVARCHAR2(500),
   ORDER_DATE           DATE,
   ORDER_MONEY          NUMBER,
   CONTENT              NVARCHAR2(500),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.ID is
'����'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.ORDER_CODE is
'������'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.CTYPE is
'��������'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.ORDER_DATE is
'��������'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.ORDER_MONEY is
'�������'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.CONTENT is
'������ע'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.CREATE_BY is
'������'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.UPDATE_BY is
'�޸���'
/

comment on column BDC_MRPC.BDC_ORDER_MAIN.UPDATE_TIME is
'�޸�ʱ��'
/

alter table BDC_MRPC.BDC_ORDER_MAIN
   add constraint SYS_C0025413 primary key (ID)
/

/*==============================================================*/
/* Table: BDC_ORDER_TICKET                                      */
/*==============================================================*/
create table BDC_MRPC.BDC_ORDER_TICKET 
(
   ID                   NVARCHAR2(32)        not null,
   TICKET_CODE          NVARCHAR2(100)       not null,
   TICKECT_DATE         DATE,
   ORDER_ID             NVARCHAR2(32)        not null,
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.ID is
'����'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.TICKET_CODE is
'�����'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.TICKECT_DATE is
'����ʱ��'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.ORDER_ID is
'���'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.CREATE_BY is
'������'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.UPDATE_BY is
'�޸���'
/

comment on column BDC_MRPC.BDC_ORDER_TICKET.UPDATE_TIME is
'�޸�ʱ��'
/

alter table BDC_MRPC.BDC_ORDER_TICKET
   add constraint SYS_C0025478 primary key (ID)
/

/*==============================================================*/
/* Table: BDC_PROJECT_NATURE_INCOME                             */
/*==============================================================*/
create table BDC_MRPC.BDC_PROJECT_NATURE_INCOME 
(
   ID                   NUMBER(11)           not null,
   NATURE               NVARCHAR2(50)        not null,
   INSURANCE_FEE        NUMBER,
   RISK_CONSULTING_FEE  NUMBER,
   EVALUATION_FEE       NUMBER,
   INSURANCE_EVALUATION_FEE NUMBER,
   BIDDING_CONSULTING_FEE NUMBER,
   INTEROL_CONSULTING_FEE NUMBER
)
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.NATURE is
'��Ŀ����'
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.INSURANCE_FEE is
'���վ���Ӷ���'
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.RISK_CONSULTING_FEE is
'������ѯ��'
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.EVALUATION_FEE is
'�б�����������'
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.INSURANCE_EVALUATION_FEE is
'���չ�����'
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.BIDDING_CONSULTING_FEE is
'Ͷ����ѯ��'
/

comment on column BDC_MRPC.BDC_PROJECT_NATURE_INCOME.INTEROL_CONSULTING_FEE is
'�ڿ���ѯ��'
/

alter table BDC_MRPC.BDC_PROJECT_NATURE_INCOME
   add constraint SYS_C0025483 primary key (ID)
/

/*==============================================================*/
/* Table: JOA_DEMO                                              */
/*==============================================================*/
create table BDC_MRPC.JOA_DEMO 
(
   ID                   NVARCHAR2(32),
   NAME                 NVARCHAR2(100),
   DAYS                 NUMBER(11),
   BEGIN_DATE           DATE,
   END_DATE             DATE,
   REASON               NVARCHAR2(500),
   BPM_STATUS           NVARCHAR2(50),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32)
)
/

comment on table BDC_MRPC.JOA_DEMO is
'���̲���'
/

comment on column BDC_MRPC.JOA_DEMO.ID is
'ID'
/

comment on column BDC_MRPC.JOA_DEMO.NAME is
'�����'
/

comment on column BDC_MRPC.JOA_DEMO.DAYS is
'�������'
/

comment on column BDC_MRPC.JOA_DEMO.BEGIN_DATE is
'��ʼʱ��'
/

comment on column BDC_MRPC.JOA_DEMO.END_DATE is
'��ٽ���ʱ��'
/

comment on column BDC_MRPC.JOA_DEMO.REASON is
'���ԭ��'
/

comment on column BDC_MRPC.JOA_DEMO.BPM_STATUS is
'����״̬'
/

comment on column BDC_MRPC.JOA_DEMO.CREATE_BY is
'������id'
/

comment on column BDC_MRPC.JOA_DEMO.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.JOA_DEMO.UPDATE_TIME is
'�޸�ʱ��'
/

comment on column BDC_MRPC.JOA_DEMO.UPDATE_BY is
'�޸���id'
/

/*==============================================================*/
/* Table: QRTZ_BLOB_TRIGGERS                                    */
/*==============================================================*/
create table BDC_MRPC.QRTZ_BLOB_TRIGGERS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   TRIGGER_NAME         NVARCHAR2(200)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null,
   BLOB_DATA            BLOB
)
/

comment on table BDC_MRPC.QRTZ_BLOB_TRIGGERS is
'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE'
/

alter table BDC_MRPC.QRTZ_BLOB_TRIGGERS
   add constraint SYS_C0025417 primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

/*==============================================================*/
/* Table: QRTZ_CALENDARS                                        */
/*==============================================================*/
create table BDC_MRPC.QRTZ_CALENDARS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   CALENDAR_NAME        NVARCHAR2(200)       not null,
   CALENDAR             BLOB                 not null
)
/

alter table BDC_MRPC.QRTZ_CALENDARS
   add constraint SYS_C0025421 primary key (SCHED_NAME, CALENDAR_NAME)
/

/*==============================================================*/
/* Table: QRTZ_CRON_TRIGGERS                                    */
/*==============================================================*/
create table BDC_MRPC.QRTZ_CRON_TRIGGERS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   TRIGGER_NAME         NVARCHAR2(200)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null,
   CRON_EXPRESSION      NVARCHAR2(200)       not null,
   TIME_ZONE_ID         NVARCHAR2(80)
)
/

comment on table BDC_MRPC.QRTZ_CRON_TRIGGERS is
'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE'
/

alter table BDC_MRPC.QRTZ_CRON_TRIGGERS
   add constraint SYS_C0025426 primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

/*==============================================================*/
/* Table: QRTZ_FIRED_TRIGGERS                                   */
/*==============================================================*/
create table BDC_MRPC.QRTZ_FIRED_TRIGGERS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   ENTRY_ID             NVARCHAR2(95)        not null,
   TRIGGER_NAME         NVARCHAR2(200)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null,
   INSTANCE_NAME        NVARCHAR2(200)       not null,
   FIRED_TIME           NUMBER(20)           not null,
   SCHED_TIME           NUMBER(20)           not null,
   PRIORITY             NUMBER(11)           not null,
   STATE                NVARCHAR2(16)        not null,
   JOB_NAME             NVARCHAR2(200),
   JOB_GROUP            NVARCHAR2(200),
   IS_NONCONCURRENT     NVARCHAR2(1),
   REQUESTS_RECOVERY    NVARCHAR2(1)
)
/

alter table BDC_MRPC.QRTZ_FIRED_TRIGGERS
   add constraint SYS_C0025436 primary key (SCHED_NAME, ENTRY_ID)
/

/*==============================================================*/
/* Table: QRTZ_JOB_DETAILS                                      */
/*==============================================================*/
create table BDC_MRPC.QRTZ_JOB_DETAILS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   JOB_NAME             NVARCHAR2(200)       not null,
   JOB_GROUP            NVARCHAR2(200)       not null,
   DESCRIPTION          NVARCHAR2(250),
   JOB_CLASS_NAME       NVARCHAR2(250)       not null,
   IS_DURABLE           NVARCHAR2(1)         not null,
   IS_NONCONCURRENT     NVARCHAR2(1)         not null,
   IS_UPDATE_DATA       NVARCHAR2(1)         not null,
   REQUESTS_RECOVERY    NVARCHAR2(1)         not null,
   JOB_DATA             BLOB
)
/

alter table BDC_MRPC.QRTZ_JOB_DETAILS
   add constraint SYS_C0025445 primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
/

/*==============================================================*/
/* Table: QRTZ_LOCKS                                            */
/*==============================================================*/
create table BDC_MRPC.QRTZ_LOCKS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   LOCK_NAME            NVARCHAR2(40)        not null
)
/

alter table BDC_MRPC.QRTZ_LOCKS
   add constraint SYS_C0025448 primary key (SCHED_NAME, LOCK_NAME)
/

/*==============================================================*/
/* Table: QRTZ_PAUSED_TRIGGER_GRPS                              */
/*==============================================================*/
create table BDC_MRPC.QRTZ_PAUSED_TRIGGER_GRPS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null
)
/

alter table BDC_MRPC.QRTZ_PAUSED_TRIGGER_GRPS
   add constraint SYS_C0025451 primary key (SCHED_NAME, TRIGGER_GROUP)
/

/*==============================================================*/
/* Table: QRTZ_SCHEDULER_STATE                                  */
/*==============================================================*/
create table BDC_MRPC.QRTZ_SCHEDULER_STATE 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   INSTANCE_NAME        NVARCHAR2(200)       not null,
   LAST_CHECKIN_TIME    NUMBER(20)           not null,
   CHECKIN_INTERVAL     NUMBER(20)           not null
)
/

alter table BDC_MRPC.QRTZ_SCHEDULER_STATE
   add constraint SYS_C0025456 primary key (SCHED_NAME, INSTANCE_NAME)
/

/*==============================================================*/
/* Table: QRTZ_SIMPLE_TRIGGERS                                  */
/*==============================================================*/
create table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   TRIGGER_NAME         NVARCHAR2(200)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null,
   REPEAT_COUNT         NUMBER(20)           not null,
   REPEAT_INTERVAL      NUMBER(20)           not null,
   TIMES_TRIGGERED      NUMBER(20)           not null
)
/

comment on table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS is
'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE'
/

alter table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS
   add constraint SYS_C0025525 primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

/*==============================================================*/
/* Table: QRTZ_SIMPROP_TRIGGERS                                 */
/*==============================================================*/
create table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   TRIGGER_NAME         NVARCHAR2(200)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null,
   STR_PROP_1           NVARCHAR2(512),
   STR_PROP_2           NVARCHAR2(512),
   STR_PROP_3           NVARCHAR2(512),
   INT_PROP_1           NUMBER(11),
   INT_PROP_2           NUMBER(11),
   LONG_PROP_1          NUMBER(20),
   LONG_PROP_2          NUMBER(20),
   DEC_PROP_1           NUMBER,
   DEC_PROP_2           NUMBER,
   BOOL_PROP_1          NVARCHAR2(1),
   BOOL_PROP_2          NVARCHAR2(1)
)
/

comment on table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS is
'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE'
/

alter table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS
   add constraint SYS_C0025529 primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

/*==============================================================*/
/* Table: QRTZ_TRIGGERS                                         */
/*==============================================================*/
create table BDC_MRPC.QRTZ_TRIGGERS 
(
   SCHED_NAME           NVARCHAR2(120)       not null,
   TRIGGER_NAME         NVARCHAR2(200)       not null,
   TRIGGER_GROUP        NVARCHAR2(200)       not null,
   JOB_NAME             NVARCHAR2(200)       not null,
   JOB_GROUP            NVARCHAR2(200)       not null,
   DESCRIPTION          NVARCHAR2(250),
   NEXT_FIRE_TIME       NUMBER(20),
   PREV_FIRE_TIME       NUMBER(20),
   PRIORITY             NUMBER(11),
   TRIGGER_STATE        NVARCHAR2(16)        not null,
   TRIGGER_TYPE         NVARCHAR2(8)         not null,
   START_TIME           NUMBER(20)           not null,
   END_TIME             NUMBER(20),
   CALENDAR_NAME        NVARCHAR2(200),
   MISFIRE_INSTR        NUMBER(6),
   JOB_DATA             BLOB
)
/

comment on table BDC_MRPC.QRTZ_TRIGGERS is
'InnoDB free: 504832 kB; (`SCHED_NAME` `JOB_NAME` `JOB_GROUP`'
/

alter table BDC_MRPC.QRTZ_TRIGGERS
   add constraint SYS_C0025538 primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
/

/*==============================================================*/
/* Index: SCHED_NAME                                            */
/*==============================================================*/
create index BDC_MRPC.SCHED_NAME on BDC_MRPC.QRTZ_TRIGGERS (
   SCHED_NAME ASC,
   JOB_NAME ASC,
   JOB_GROUP ASC
)
/

/*==============================================================*/
/* Table: STAFF                                                 */
/*==============================================================*/
create table BDC_MRPC.STAFF 
(
   ID                   NVARCHAR2(50)        not null,
   NAME                 NVARCHAR2(30),
   KEY_WORD             NVARCHAR2(255),
   PUNCH_TIME           DATE,
   SALARY_MONEY         NUMBER,
   BONUS_MONEY          NUMBER,
   SEX                  NVARCHAR2(2),
   AGE                  NUMBER(11),
   BIRTHDAY             DATE,
   EMAIL                NVARCHAR2(50),
   CONTENT              NVARCHAR2(1000),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   SYS_ORG_CODE         NVARCHAR2(64),
   Division_Code        NVARCHAR2(10)
)
/

comment on column BDC_MRPC.STAFF.ID is
'����ID'
/

comment on column BDC_MRPC.STAFF.NAME is
'����'
/

comment on column BDC_MRPC.STAFF.KEY_WORD is
'�ؼ���'
/

comment on column BDC_MRPC.STAFF.PUNCH_TIME is
'��ʱ��'
/

comment on column BDC_MRPC.STAFF.SALARY_MONEY is
'����'
/

comment on column BDC_MRPC.STAFF.BONUS_MONEY is
'����'
/

comment on column BDC_MRPC.STAFF.SEX is
'�Ա� {��:1,Ů:2}'
/

comment on column BDC_MRPC.STAFF.AGE is
'����'
/

comment on column BDC_MRPC.STAFF.BIRTHDAY is
'����'
/

comment on column BDC_MRPC.STAFF.EMAIL is
'����'
/

comment on column BDC_MRPC.STAFF.CONTENT is
'���˼��'
/

comment on column BDC_MRPC.STAFF.CREATE_BY is
'������'
/

comment on column BDC_MRPC.STAFF.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.STAFF.UPDATE_BY is
'�޸���'
/

comment on column BDC_MRPC.STAFF.UPDATE_TIME is
'�޸�ʱ��'
/

comment on column BDC_MRPC.STAFF.SYS_ORG_CODE is
'�������ű���'
/

comment on column BDC_MRPC.STAFF.Division_Code is
'��������'
/

alter table BDC_MRPC.STAFF
   add constraint SYS_C0025407 primary key (ID)
/

/*==============================================================*/
/* Table: SYS_ANNOUNCEMENT                                      */
/*==============================================================*/
create table BDC_MRPC.SYS_ANNOUNCEMENT 
(
   ID                   NVARCHAR2(32)        not null,
   TITILE               NVARCHAR2(100),
   MSG_CONTENT          NCLOB,
   START_TIME           DATE,
   END_TIME             DATE,
   SENDER               NVARCHAR2(100),
   PRIORITY             NVARCHAR2(255),
   MSG_CATEGORY         NVARCHAR2(10)        not null,
   MSG_TYPE             NVARCHAR2(10),
   SEND_STATUS          NVARCHAR2(10),
   SEND_TIME            DATE,
   CANCEL_TIME          DATE,
   DEL_FLAG             NVARCHAR2(1),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   USER_IDS             NCLOB
)
/

comment on table BDC_MRPC.SYS_ANNOUNCEMENT is
'ϵͳͨ���'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.TITILE is
'����'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.MSG_CONTENT is
'����'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.START_TIME is
'��ʼʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.END_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.SENDER is
'������'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.PRIORITY is
'���ȼ���L�ͣ�M�У�H�ߣ�'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.MSG_CATEGORY is
'��Ϣ����1:֪ͨ����2:ϵͳ��Ϣ'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.MSG_TYPE is
'ͨ��������ͣ�USER:ָ���û���ALL:ȫ���û���'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.SEND_STATUS is
'����״̬��0δ������1�ѷ�����2�ѳ�����'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.SEND_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.CANCEL_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.DEL_FLAG is
'ɾ��״̬��0��������1��ɾ����'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.UPDATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT.USER_IDS is
'ָ���û�'
/

alter table BDC_MRPC.SYS_ANNOUNCEMENT
   add constraint SYS_C0025541 primary key (ID)
/

/*==============================================================*/
/* Table: SYS_ANNOUNCEMENT_SEND                                 */
/*==============================================================*/
create table BDC_MRPC.SYS_ANNOUNCEMENT_SEND 
(
   ID                   NVARCHAR2(32),
   ANNT_ID              NVARCHAR2(32),
   USER_ID              NVARCHAR2(32),
   READ_FLAG            NVARCHAR2(10),
   READ_TIME            DATE,
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on table BDC_MRPC.SYS_ANNOUNCEMENT_SEND is
'�û�ͨ���Ķ���Ǳ�'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.ANNT_ID is
'ͨ��ID'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.USER_ID is
'�û�id'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.READ_FLAG is
'�Ķ�״̬��0δ����1�Ѷ���'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.READ_TIME is
'�Ķ�ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_ANNOUNCEMENT_SEND.UPDATE_TIME is
'����ʱ��'
/

/*==============================================================*/
/* Table: SYS_CATEGORY                                          */
/*==============================================================*/
create table BDC_MRPC.SYS_CATEGORY 
(
   ID                   NVARCHAR2(36)        not null,
   PID                  NVARCHAR2(36),
   NAME                 NVARCHAR2(100),
   CODE                 NVARCHAR2(100),
   CREATE_BY            NVARCHAR2(50),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(50),
   UPDATE_TIME          DATE,
   SYS_ORG_CODE         NVARCHAR2(64),
   HAS_CHILD            NVARCHAR2(3)
)
/

comment on column BDC_MRPC.SYS_CATEGORY.PID is
'�����ڵ�'
/

comment on column BDC_MRPC.SYS_CATEGORY.NAME is
'��������'
/

comment on column BDC_MRPC.SYS_CATEGORY.CODE is
'���ͱ���'
/

comment on column BDC_MRPC.SYS_CATEGORY.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_CATEGORY.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_CATEGORY.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_CATEGORY.UPDATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_CATEGORY.SYS_ORG_CODE is
'��������'
/

comment on column BDC_MRPC.SYS_CATEGORY.HAS_CHILD is
'�Ƿ����ӽڵ�'
/

alter table BDC_MRPC.SYS_CATEGORY
   add constraint SYS_C0025543 primary key (ID)
/

/*==============================================================*/
/* Table: SYS_CONFIG                                            */
/*==============================================================*/
create table BDC_MRPC.SYS_CONFIG 
(
   ID                   NVARCHAR2(50)        not null,
   Division_Code        NVARCHAR2(10),
   NAME                 NVARCHAR2(50),
   VALUE                NVARCHAR2(200),
   TYPE                 NVARCHAR2(5),
   CREATETIME           DATE,
   MEMO                 NVARCHAR2(500)
)
/

comment on table BDC_MRPC.SYS_CONFIG is
'ϵͳ���ñ�'
/

alter table BDC_MRPC.SYS_CONFIG
   add constraint PK_SYS_CONFIG primary key (ID)
/

/*==============================================================*/
/* Table: SYS_DATA_LOG                                          */
/*==============================================================*/
create table BDC_MRPC.SYS_DATA_LOG 
(
   ID                   NVARCHAR2(32)        not null,
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   DATA_TABLE           NVARCHAR2(32),
   DATA_ID              NVARCHAR2(32),
   DATA_CONTENT         NCLOB,
   DATA_VERSION         NUMBER(11)
)
/

comment on column BDC_MRPC.SYS_DATA_LOG.ID is
'id'
/

comment on column BDC_MRPC.SYS_DATA_LOG.CREATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_DATA_LOG.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_DATA_LOG.UPDATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_DATA_LOG.UPDATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_DATA_LOG.DATA_TABLE is
'����'
/

comment on column BDC_MRPC.SYS_DATA_LOG.DATA_ID is
'����ID'
/

comment on column BDC_MRPC.SYS_DATA_LOG.DATA_CONTENT is
'��������'
/

comment on column BDC_MRPC.SYS_DATA_LOG.DATA_VERSION is
'�汾��'
/

alter table BDC_MRPC.SYS_DATA_LOG
   add constraint SYS_C0025545 primary key (ID)
/

/*==============================================================*/
/* Index: SINDEX                                                */
/*==============================================================*/
create index BDC_MRPC.SINDEX on BDC_MRPC.SYS_DATA_LOG (
   DATA_TABLE ASC,
   DATA_ID ASC
)
/

/*==============================================================*/
/* Table: SYS_DEPART                                            */
/*==============================================================*/
create table BDC_MRPC.SYS_DEPART 
(
   ID                   NVARCHAR2(32)        not null,
   PARENT_ID            NVARCHAR2(32),
   DEPART_NAME          NVARCHAR2(100)       not null,
   DEPART_NAME_EN       NVARCHAR2(500),
   DEPART_NAME_ABBR     NVARCHAR2(500),
   DEPART_ORDER         NUMBER(11),
   DESCRIPTION          NCLOB,
   ORG_TYPE             NVARCHAR2(10),
   ORG_CODE             NVARCHAR2(64)        not null,
   MOBILE               NVARCHAR2(32),
   FAX                  NVARCHAR2(32),
   ADDRESS              NVARCHAR2(100),
   MEMO                 NVARCHAR2(500),
   STATUS               NVARCHAR2(1),
   DEL_FLAG             NVARCHAR2(1),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on table BDC_MRPC.SYS_DEPART is
'��֯������'
/

comment on column BDC_MRPC.SYS_DEPART.ID is
'ID'
/

comment on column BDC_MRPC.SYS_DEPART.PARENT_ID is
'������ID'
/

comment on column BDC_MRPC.SYS_DEPART.DEPART_NAME is
'����/��������'
/

comment on column BDC_MRPC.SYS_DEPART.DEPART_NAME_EN is
'Ӣ����'
/

comment on column BDC_MRPC.SYS_DEPART.DEPART_NAME_ABBR is
'��д'
/

comment on column BDC_MRPC.SYS_DEPART.DEPART_ORDER is
'����'
/

comment on column BDC_MRPC.SYS_DEPART.DESCRIPTION is
'����'
/

comment on column BDC_MRPC.SYS_DEPART.ORG_TYPE is
'�������� 1һ������ 2�Ӳ���'
/

comment on column BDC_MRPC.SYS_DEPART.ORG_CODE is
'��������'
/

comment on column BDC_MRPC.SYS_DEPART.MOBILE is
'�ֻ���'
/

comment on column BDC_MRPC.SYS_DEPART.FAX is
'����'
/

comment on column BDC_MRPC.SYS_DEPART.ADDRESS is
'��ַ'
/

comment on column BDC_MRPC.SYS_DEPART.MEMO is
'��ע'
/

comment on column BDC_MRPC.SYS_DEPART.STATUS is
'״̬��1���ã�0�����ã�'
/

comment on column BDC_MRPC.SYS_DEPART.DEL_FLAG is
'ɾ��״̬��0��������1��ɾ����'
/

comment on column BDC_MRPC.SYS_DEPART.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_DEPART.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_DEPART.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_DEPART.UPDATE_TIME is
'��������'
/

alter table BDC_MRPC.SYS_DEPART
   add constraint SYS_C0025549 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_DEPART_DEPART_ORDER                             */
/*==============================================================*/
create index BDC_MRPC.INDEX_DEPART_DEPART_ORDER on BDC_MRPC.SYS_DEPART (
   DEPART_ORDER ASC
)
/

/*==============================================================*/
/* Index: INDEX_DEPART_ORG_CODE                                 */
/*==============================================================*/
create index BDC_MRPC.INDEX_DEPART_ORG_CODE on BDC_MRPC.SYS_DEPART (
   ORG_CODE ASC
)
/

/*==============================================================*/
/* Index: INDEX_DEPART_PARENT_ID                                */
/*==============================================================*/
create index BDC_MRPC.INDEX_DEPART_PARENT_ID on BDC_MRPC.SYS_DEPART (
   PARENT_ID ASC
)
/

/*==============================================================*/
/* Table: SYS_DICT                                              */
/*==============================================================*/
create table BDC_MRPC.SYS_DICT 
(
   ID                   NVARCHAR2(32)        not null,
   DICT_NAME            NVARCHAR2(100),
   DICT_CODE            NVARCHAR2(100),
   DESCRIPTION          NVARCHAR2(255),
   DEL_FLAG             NUMBER(11),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   TYPE                 NUMBER(11)
)
/

comment on column BDC_MRPC.SYS_DICT.DICT_NAME is
'�ֵ�����'
/

comment on column BDC_MRPC.SYS_DICT.DICT_CODE is
'�ֵ����'
/

comment on column BDC_MRPC.SYS_DICT.DESCRIPTION is
'����'
/

comment on column BDC_MRPC.SYS_DICT.DEL_FLAG is
'ɾ��״̬'
/

comment on column BDC_MRPC.SYS_DICT.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_DICT.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_DICT.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_DICT.UPDATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_DICT.TYPE is
'�ֵ�����0Ϊstring,1Ϊnumber'
/

alter table BDC_MRPC.SYS_DICT
   add constraint SYS_C0025551 primary key (ID)
/

/*==============================================================*/
/* Index: INDEXTABLE_DICT_CODE                                  */
/*==============================================================*/
create unique index BDC_MRPC.INDEXTABLE_DICT_CODE on BDC_MRPC.SYS_DICT (
   DICT_CODE ASC
)
/

/*==============================================================*/
/* Table: SYS_DICT_ITEM                                         */
/*==============================================================*/
create table BDC_MRPC.SYS_DICT_ITEM 
(
   ID                   NVARCHAR2(32)        not null,
   DICT_ID              NVARCHAR2(32),
   ITEM_TEXT            NVARCHAR2(100),
   ITEM_VALUE           NVARCHAR2(100),
   DESCRIPTION          NVARCHAR2(255),
   SORT_ORDER           NUMBER(11),
   STATUS               NUMBER(11),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on column BDC_MRPC.SYS_DICT_ITEM.DICT_ID is
'�ֵ�id'
/

comment on column BDC_MRPC.SYS_DICT_ITEM.ITEM_TEXT is
'�ֵ����ı�'
/

comment on column BDC_MRPC.SYS_DICT_ITEM.ITEM_VALUE is
'�ֵ���ֵ'
/

comment on column BDC_MRPC.SYS_DICT_ITEM.DESCRIPTION is
'����'
/

comment on column BDC_MRPC.SYS_DICT_ITEM.SORT_ORDER is
'����'
/

comment on column BDC_MRPC.SYS_DICT_ITEM.STATUS is
'״̬��1���� 0�����ã�'
/

alter table BDC_MRPC.SYS_DICT_ITEM
   add constraint SYS_C0025553 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_TABLE_DICT_ID                                   */
/*==============================================================*/
create index BDC_MRPC.INDEX_TABLE_DICT_ID on BDC_MRPC.SYS_DICT_ITEM (
   DICT_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX_TABLE_DICT_STATUS                               */
/*==============================================================*/
create index BDC_MRPC.INDEX_TABLE_DICT_STATUS on BDC_MRPC.SYS_DICT_ITEM (
   STATUS ASC
)
/

/*==============================================================*/
/* Index: INDEX_TABLE_SORT_ORDER                                */
/*==============================================================*/
create index BDC_MRPC.INDEX_TABLE_SORT_ORDER on BDC_MRPC.SYS_DICT_ITEM (
   SORT_ORDER ASC
)
/

/*==============================================================*/
/* Table: SYS_LOG                                               */
/*==============================================================*/
create table BDC_MRPC.SYS_LOG 
(
   ID                   NVARCHAR2(32)        not null,
   LOG_TYPE             NUMBER(11),
   LOG_CONTENT          NVARCHAR2(1000),
   OPERATE_TYPE         NUMBER(11),
   USERID               NVARCHAR2(32),
   USERNAME             NVARCHAR2(100),
   IP                   NVARCHAR2(100),
   METHOD               NVARCHAR2(500),
   REQUEST_URL          NVARCHAR2(255),
   REQUEST_PARAM        NCLOB,
   REQUEST_TYPE         NVARCHAR2(10),
   COST_TIME            NUMBER(20),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   Division_Code        NVARCHAR2(10)
)
/

comment on table BDC_MRPC.SYS_LOG is
'ϵͳ��־��'
/

comment on column BDC_MRPC.SYS_LOG.LOG_TYPE is
'��־���ͣ�1��¼��־��2������־��'
/

comment on column BDC_MRPC.SYS_LOG.LOG_CONTENT is
'��־����'
/

comment on column BDC_MRPC.SYS_LOG.OPERATE_TYPE is
'��������'
/

comment on column BDC_MRPC.SYS_LOG.USERID is
'�����û��˺�'
/

comment on column BDC_MRPC.SYS_LOG.USERNAME is
'�����û�����'
/

comment on column BDC_MRPC.SYS_LOG.IP is
'IP'
/

comment on column BDC_MRPC.SYS_LOG.METHOD is
'����java����'
/

comment on column BDC_MRPC.SYS_LOG.REQUEST_URL is
'����·��'
/

comment on column BDC_MRPC.SYS_LOG.REQUEST_PARAM is
'�������'
/

comment on column BDC_MRPC.SYS_LOG.REQUEST_TYPE is
'��������'
/

comment on column BDC_MRPC.SYS_LOG.COST_TIME is
'��ʱ'
/

comment on column BDC_MRPC.SYS_LOG.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_LOG.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_LOG.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_LOG.UPDATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_LOG.Division_Code is
'��������'
/

alter table BDC_MRPC.SYS_LOG
   add constraint SYS_C0025555 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_LOGT_YPE                                        */
/*==============================================================*/
create index BDC_MRPC.INDEX_LOGT_YPE on BDC_MRPC.SYS_LOG (
   LOG_TYPE ASC
)
/

/*==============================================================*/
/* Index: INDEX_OPERATE_TYPE                                    */
/*==============================================================*/
create index BDC_MRPC.INDEX_OPERATE_TYPE on BDC_MRPC.SYS_LOG (
   OPERATE_TYPE ASC
)
/

/*==============================================================*/
/* Index: INDEX_TABLE_USERID                                    */
/*==============================================================*/
create index BDC_MRPC.INDEX_TABLE_USERID on BDC_MRPC.SYS_LOG (
   USERID ASC
)
/

/*==============================================================*/
/* Table: SYS_PERMISSION                                        */
/*==============================================================*/
create table BDC_MRPC.SYS_PERMISSION 
(
   ID                   NVARCHAR2(32)        not null,
   PARENT_ID            NVARCHAR2(32),
   NAME                 NVARCHAR2(100),
   URL                  NVARCHAR2(255),
   COMPONENT            NVARCHAR2(255),
   COMPONENT_NAME       NVARCHAR2(100),
   REDIRECT             NVARCHAR2(255),
   MENU_TYPE            NUMBER(11),
   PERMS                NVARCHAR2(255),
   PERMS_TYPE           NVARCHAR2(10),
   SORT_NO              NUMBER(11),
   ALWAYS_SHOW          NUMBER(4),
   ICON                 NVARCHAR2(100),
   IS_ROUTE             NUMBER(4),
   IS_LEAF              NUMBER(4),
   KEEP_ALIVE           NUMBER(4),
   HIDDEN               NUMBER(11),
   DESCRIPTION          NVARCHAR2(255),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   DEL_FLAG             NUMBER(11),
   RULE_FLAG            NUMBER(11),
   STATUS               NVARCHAR2(2)
)
/

comment on table BDC_MRPC.SYS_PERMISSION is
'�˵�Ȩ�ޱ�'
/

comment on column BDC_MRPC.SYS_PERMISSION.ID is
'����id'
/

comment on column BDC_MRPC.SYS_PERMISSION.PARENT_ID is
'��id'
/

comment on column BDC_MRPC.SYS_PERMISSION.NAME is
'�˵�����'
/

comment on column BDC_MRPC.SYS_PERMISSION.URL is
'·��'
/

comment on column BDC_MRPC.SYS_PERMISSION.COMPONENT is
'���'
/

comment on column BDC_MRPC.SYS_PERMISSION.COMPONENT_NAME is
'�������'
/

comment on column BDC_MRPC.SYS_PERMISSION.REDIRECT is
'һ���˵���ת��ַ'
/

comment on column BDC_MRPC.SYS_PERMISSION.MENU_TYPE is
'�˵�����(0:һ���˵�; 1:�Ӳ˵�:2:��ťȨ��)'
/

comment on column BDC_MRPC.SYS_PERMISSION.PERMS is
'�˵�Ȩ�ޱ���'
/

comment on column BDC_MRPC.SYS_PERMISSION.PERMS_TYPE is
'Ȩ�޲���1��ʾ2����'
/

comment on column BDC_MRPC.SYS_PERMISSION.SORT_NO is
'�˵�����'
/

comment on column BDC_MRPC.SYS_PERMISSION.ALWAYS_SHOW is
'�ۺ���·��: 1��0��'
/

comment on column BDC_MRPC.SYS_PERMISSION.ICON is
'�˵�ͼ��'
/

comment on column BDC_MRPC.SYS_PERMISSION.IS_ROUTE is
'�Ƿ�·�ɲ˵�: 0:����  1:�ǣ�Ĭ��ֵ1��'
/

comment on column BDC_MRPC.SYS_PERMISSION.IS_LEAF is
'�Ƿ�Ҷ�ӽڵ�:    1:��   0:����'
/

comment on column BDC_MRPC.SYS_PERMISSION.KEEP_ALIVE is
'�Ƿ񻺴��ҳ��:    1:��   0:����'
/

comment on column BDC_MRPC.SYS_PERMISSION.HIDDEN is
'�Ƿ�����·��: 0��,1��'
/

comment on column BDC_MRPC.SYS_PERMISSION.DESCRIPTION is
'����'
/

comment on column BDC_MRPC.SYS_PERMISSION.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_PERMISSION.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_PERMISSION.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_PERMISSION.UPDATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_PERMISSION.DEL_FLAG is
'ɾ��״̬ 0���� 1��ɾ��'
/

comment on column BDC_MRPC.SYS_PERMISSION.RULE_FLAG is
'�Ƿ��������Ȩ��1��0��'
/

comment on column BDC_MRPC.SYS_PERMISSION.STATUS is
'��ťȨ��״̬(0��Ч1��Ч)'
/

alter table BDC_MRPC.SYS_PERMISSION
   add constraint SYS_C0025557 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_PREM_DEL_FLAG                                   */
/*==============================================================*/
create index BDC_MRPC.INDEX_PREM_DEL_FLAG on BDC_MRPC.SYS_PERMISSION (
   DEL_FLAG ASC
)
/

/*==============================================================*/
/* Index: INDEX_PREM_IS_LEAF                                    */
/*==============================================================*/
create index BDC_MRPC.INDEX_PREM_IS_LEAF on BDC_MRPC.SYS_PERMISSION (
   IS_LEAF ASC
)
/

/*==============================================================*/
/* Index: INDEX_PREM_IS_ROUTE                                   */
/*==============================================================*/
create index BDC_MRPC.INDEX_PREM_IS_ROUTE on BDC_MRPC.SYS_PERMISSION (
   IS_ROUTE ASC
)
/

/*==============================================================*/
/* Index: INDEX_PREM_PID                                        */
/*==============================================================*/
create index BDC_MRPC.INDEX_PREM_PID on BDC_MRPC.SYS_PERMISSION (
   PARENT_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX_PREM_SORT_NO                                    */
/*==============================================================*/
create index BDC_MRPC.INDEX_PREM_SORT_NO on BDC_MRPC.SYS_PERMISSION (
   SORT_NO ASC
)
/

/*==============================================================*/
/* Table: SYS_PERMISSION_DATA_RULE                              */
/*==============================================================*/
create table BDC_MRPC.SYS_PERMISSION_DATA_RULE 
(
   ID                   NVARCHAR2(32)        not null,
   PERMISSION_ID        NVARCHAR2(32),
   RULE_NAME            NVARCHAR2(50),
   RULE_COLUMN          NVARCHAR2(50),
   RULE_CONDITIONS      NVARCHAR2(50),
   RULE_VALUE           NVARCHAR2(300),
   STATUS               NVARCHAR2(3),
   CREATE_TIME          DATE,
   CREATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32)
)
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.ID is
'ID'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.PERMISSION_ID is
'�˵�ID'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.RULE_NAME is
'��������'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.RULE_COLUMN is
'�ֶ�'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.RULE_CONDITIONS is
'����'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.RULE_VALUE is
'����ֵ'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.STATUS is
'Ȩ����Ч״̬1��0��'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.UPDATE_TIME is
'�޸�ʱ��'
/

comment on column BDC_MRPC.SYS_PERMISSION_DATA_RULE.UPDATE_BY is
'�޸���'
/

alter table BDC_MRPC.SYS_PERMISSION_DATA_RULE
   add constraint SYS_C0025559 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_FUCNTIONID                                      */
/*==============================================================*/
create index BDC_MRPC.INDEX_FUCNTIONID on BDC_MRPC.SYS_PERMISSION_DATA_RULE (
   PERMISSION_ID ASC
)
/

/*==============================================================*/
/* Table: SYS_QUARTZ_JOB                                        */
/*==============================================================*/
create table BDC_MRPC.SYS_QUARTZ_JOB 
(
   ID                   NVARCHAR2(32)        not null,
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   DEL_FLAG             NUMBER(11),
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   JOB_CLASS_NAME       NVARCHAR2(255),
   CRON_EXPRESSION      NVARCHAR2(255),
   PARAMETER            NVARCHAR2(255),
   DESCRIPTION          NVARCHAR2(255),
   STATUS               NUMBER(11)
)
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.DEL_FLAG is
'ɾ��״̬'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.UPDATE_BY is
'�޸���'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.UPDATE_TIME is
'�޸�ʱ��'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.JOB_CLASS_NAME is
'��������'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.CRON_EXPRESSION is
'cron���ʽ'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.PARAMETER is
'����'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.DESCRIPTION is
'����'
/

comment on column BDC_MRPC.SYS_QUARTZ_JOB.STATUS is
'״̬ 0���� -1ֹͣ'
/

alter table BDC_MRPC.SYS_QUARTZ_JOB
   add constraint SYS_C0025561 primary key (ID)
/

/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
create table BDC_MRPC.SYS_ROLE 
(
   ID                   NVARCHAR2(32)        not null,
   ROLE_NAME            NVARCHAR2(200),
   ROLE_CODE            NVARCHAR2(100)       not null,
   DESCRIPTION          NVARCHAR2(255),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   Division_Code        NVARCHAR2(10)
)
/

comment on table BDC_MRPC.SYS_ROLE is
'��ɫ��'
/

comment on column BDC_MRPC.SYS_ROLE.ID is
'����id'
/

comment on column BDC_MRPC.SYS_ROLE.ROLE_NAME is
'��ɫ����'
/

comment on column BDC_MRPC.SYS_ROLE.ROLE_CODE is
'��ɫ����'
/

comment on column BDC_MRPC.SYS_ROLE.DESCRIPTION is
'����'
/

comment on column BDC_MRPC.SYS_ROLE.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_ROLE.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ROLE.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_ROLE.UPDATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_ROLE.Division_Code is
'��������'
/

alter table BDC_MRPC.SYS_ROLE
   add constraint SYS_C0025564 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_ROLE_CODE                                       */
/*==============================================================*/
create unique index BDC_MRPC.INDEX_ROLE_CODE on BDC_MRPC.SYS_ROLE (
   ROLE_CODE ASC
)
/

/*==============================================================*/
/* Table: SYS_ROLE_PERMISSION                                   */
/*==============================================================*/
create table BDC_MRPC.SYS_ROLE_PERMISSION 
(
   ID                   NVARCHAR2(32)        not null,
   ROLE_ID              NVARCHAR2(32),
   PERMISSION_ID        NVARCHAR2(32),
   DATA_RULE_IDS        NVARCHAR2(1000)
)
/

comment on table BDC_MRPC.SYS_ROLE_PERMISSION is
'��ɫȨ�ޱ�'
/

comment on column BDC_MRPC.SYS_ROLE_PERMISSION.ROLE_ID is
'��ɫid'
/

comment on column BDC_MRPC.SYS_ROLE_PERMISSION.PERMISSION_ID is
'Ȩ��id'
/

alter table BDC_MRPC.SYS_ROLE_PERMISSION
   add constraint SYS_C0025566 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_GROUP_PER_ID                                    */
/*==============================================================*/
create index BDC_MRPC.INDEX_GROUP_PER_ID on BDC_MRPC.SYS_ROLE_PERMISSION (
   PERMISSION_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX_GROUP_ROLE_ID                                   */
/*==============================================================*/
create index BDC_MRPC.INDEX_GROUP_ROLE_ID on BDC_MRPC.SYS_ROLE_PERMISSION (
   ROLE_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX_GROUP_ROLE_PER_ID                               */
/*==============================================================*/
create index BDC_MRPC.INDEX_GROUP_ROLE_PER_ID on BDC_MRPC.SYS_ROLE_PERMISSION (
   ROLE_ID ASC,
   PERMISSION_ID ASC
)
/

/*==============================================================*/
/* Table: SYS_SMS                                               */
/*==============================================================*/
create table BDC_MRPC.SYS_SMS 
(
   ID                   NVARCHAR2(32)        not null,
   ES_TITLE             NVARCHAR2(100),
   ES_TYPE              NVARCHAR2(1),
   ES_RECEIVER          NVARCHAR2(50),
   ES_PARAM             NVARCHAR2(1000),
   ES_CONTENT           NCLOB,
   ES_SEND_TIME         DATE,
   ES_SEND_STATUS       NVARCHAR2(1),
   ES_SEND_NUM          NUMBER(11),
   ES_RESULT            NVARCHAR2(255),
   REMARK               NVARCHAR2(500),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE
)
/

comment on column BDC_MRPC.SYS_SMS.ID is
'ID'
/

comment on column BDC_MRPC.SYS_SMS.ES_TITLE is
'��Ϣ����'
/

comment on column BDC_MRPC.SYS_SMS.ES_TYPE is
'���ͷ�ʽ��1���� 2�ʼ� 3΢��'
/

comment on column BDC_MRPC.SYS_SMS.ES_RECEIVER is
'������'
/

comment on column BDC_MRPC.SYS_SMS.ES_PARAM is
'�����������Json��ʽ'
/

comment on column BDC_MRPC.SYS_SMS.ES_CONTENT is
'��������'
/

comment on column BDC_MRPC.SYS_SMS.ES_SEND_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_SMS.ES_SEND_STATUS is
'����״̬ 0δ���� 1���ͳɹ� 2����ʧ�� -1ʧ�ܲ��ٷ���'
/

comment on column BDC_MRPC.SYS_SMS.ES_SEND_NUM is
'���ʹ��� ����5�β��ٷ���'
/

comment on column BDC_MRPC.SYS_SMS.ES_RESULT is
'����ʧ��ԭ��'
/

comment on column BDC_MRPC.SYS_SMS.REMARK is
'��ע'
/

comment on column BDC_MRPC.SYS_SMS.CREATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_SMS.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_SMS.UPDATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_SMS.UPDATE_TIME is
'��������'
/

alter table BDC_MRPC.SYS_SMS
   add constraint SYS_C0025568 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_RECEIVER                                        */
/*==============================================================*/
create index BDC_MRPC.INDEX_RECEIVER on BDC_MRPC.SYS_SMS (
   ES_RECEIVER ASC
)
/

/*==============================================================*/
/* Index: INDEX_SENDTIME                                        */
/*==============================================================*/
create index BDC_MRPC.INDEX_SENDTIME on BDC_MRPC.SYS_SMS (
   ES_SEND_TIME ASC
)
/

/*==============================================================*/
/* Index: INDEX_STATUS                                          */
/*==============================================================*/
create index BDC_MRPC.INDEX_STATUS on BDC_MRPC.SYS_SMS (
   ES_SEND_STATUS ASC
)
/

/*==============================================================*/
/* Index: INDEX_TYPE                                            */
/*==============================================================*/
create index BDC_MRPC.INDEX_TYPE on BDC_MRPC.SYS_SMS (
   ES_TYPE ASC
)
/

/*==============================================================*/
/* Table: SYS_SMS_TEMPLATE                                      */
/*==============================================================*/
create table BDC_MRPC.SYS_SMS_TEMPLATE 
(
   ID                   NVARCHAR2(32)        not null,
   TEMPLATE_NAME        NVARCHAR2(50),
   TEMPLATE_CODE        NVARCHAR2(32)        not null,
   TEMPLATE_TYPE        NVARCHAR2(1)         not null,
   TEMPLATE_CONTENT     NVARCHAR2(1000)      not null,
   TEMPLATE_TEST_JSON   NVARCHAR2(1000),
   CREATE_TIME          DATE,
   CREATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32)
)
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.ID is
'����'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.TEMPLATE_NAME is
'ģ�����'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.TEMPLATE_CODE is
'ģ��CODE'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.TEMPLATE_TYPE is
'ģ�����ͣ�1���� 2�ʼ� 3΢��'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.TEMPLATE_CONTENT is
'ģ������'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.TEMPLATE_TEST_JSON is
'ģ�����json'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.CREATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.UPDATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_SMS_TEMPLATE.UPDATE_BY is
'�����˵�¼����'
/

alter table BDC_MRPC.SYS_SMS_TEMPLATE
   add constraint SYS_C0025573 primary key (ID)
/

/*==============================================================*/
/* Index: UNIQ_TEMPLATECODE                                     */
/*==============================================================*/
create unique index BDC_MRPC.UNIQ_TEMPLATECODE on BDC_MRPC.SYS_SMS_TEMPLATE (
   TEMPLATE_CODE ASC
)
/

/*==============================================================*/
/* Table: SYS_USER                                              */
/*==============================================================*/
create table BDC_MRPC.SYS_USER 
(
   ID                   NVARCHAR2(32)        not null,
   USERNAME             NVARCHAR2(100),
   REALNAME             NVARCHAR2(100),
   PASSWORD             NVARCHAR2(255),
   SALT                 NVARCHAR2(45),
   AVATAR               NVARCHAR2(255),
   BIRTHDAY             DATE,
   SEX                  NUMBER(11),
   EMAIL                NVARCHAR2(45),
   PHONE                NVARCHAR2(45),
   ORG_CODE             NVARCHAR2(100),
   STATUS               NUMBER(11),
   DEL_FLAG             NVARCHAR2(1),
   ACTIVITI_SYNC        NVARCHAR2(6),
   CREATE_BY            NVARCHAR2(32),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(32),
   UPDATE_TIME          DATE,
   Division_Code        NVARCHAR2(10)
)
/

comment on table BDC_MRPC.SYS_USER is
'�û���'
/

comment on column BDC_MRPC.SYS_USER.ID is
'����id'
/

comment on column BDC_MRPC.SYS_USER.USERNAME is
'��¼�˺�'
/

comment on column BDC_MRPC.SYS_USER.REALNAME is
'��ʵ����'
/

comment on column BDC_MRPC.SYS_USER.PASSWORD is
'����'
/

comment on column BDC_MRPC.SYS_USER.SALT is
'md5������'
/

comment on column BDC_MRPC.SYS_USER.AVATAR is
'ͷ��'
/

comment on column BDC_MRPC.SYS_USER.BIRTHDAY is
'����'
/

comment on column BDC_MRPC.SYS_USER.SEX is
'�Ա�1���� 2��Ů��'
/

comment on column BDC_MRPC.SYS_USER.EMAIL is
'�����ʼ�'
/

comment on column BDC_MRPC.SYS_USER.PHONE is
'�绰'
/

comment on column BDC_MRPC.SYS_USER.ORG_CODE is
'����code'
/

comment on column BDC_MRPC.SYS_USER.STATUS is
'״̬(1������  2������ ��'
/

comment on column BDC_MRPC.SYS_USER.DEL_FLAG is
'ɾ��״̬��0��������1��ɾ����'
/

comment on column BDC_MRPC.SYS_USER.ACTIVITI_SYNC is
'ͬ������������1ͬ��0��ͬ��'
/

comment on column BDC_MRPC.SYS_USER.CREATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_USER.CREATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_USER.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.SYS_USER.UPDATE_TIME is
'����ʱ��'
/

comment on column BDC_MRPC.SYS_USER.Division_Code is
'��������'
/

alter table BDC_MRPC.SYS_USER
   add constraint SYS_C0025575 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_USER_DEL_FLAG                                   */
/*==============================================================*/
create index BDC_MRPC.INDEX_USER_DEL_FLAG on BDC_MRPC.SYS_USER (
   DEL_FLAG ASC
)
/

/*==============================================================*/
/* Index: INDEX_USER_NAME                                       */
/*==============================================================*/
create unique index BDC_MRPC.INDEX_USER_NAME on BDC_MRPC.SYS_USER (
   USERNAME ASC
)
/

/*==============================================================*/
/* Index: INDEX_USER_STATUS                                     */
/*==============================================================*/
create index BDC_MRPC.INDEX_USER_STATUS on BDC_MRPC.SYS_USER (
   STATUS ASC
)
/

/*==============================================================*/
/* Table: SYS_USER_AGENT                                        */
/*==============================================================*/
create table BDC_MRPC.SYS_USER_AGENT 
(
   ID                   NVARCHAR2(32)        not null,
   USER_NAME            NVARCHAR2(100),
   AGENT_USER_NAME      NVARCHAR2(100),
   START_TIME           DATE,
   END_TIME             DATE,
   STATUS               NVARCHAR2(2),
   CREATE_NAME          NVARCHAR2(50),
   CREATE_BY            NVARCHAR2(50),
   CREATE_TIME          DATE,
   UPDATE_NAME          NVARCHAR2(50),
   UPDATE_BY            NVARCHAR2(50),
   UPDATE_TIME          DATE,
   SYS_ORG_CODE         NVARCHAR2(50),
   SYS_COMPANY_CODE     NVARCHAR2(50)
)
/

comment on table BDC_MRPC.SYS_USER_AGENT is
'�û�����������'
/

comment on column BDC_MRPC.SYS_USER_AGENT.ID is
'���'
/

comment on column BDC_MRPC.SYS_USER_AGENT.USER_NAME is
'�û���'
/

comment on column BDC_MRPC.SYS_USER_AGENT.AGENT_USER_NAME is
'�������û���'
/

comment on column BDC_MRPC.SYS_USER_AGENT.START_TIME is
'����ʼʱ��'
/

comment on column BDC_MRPC.SYS_USER_AGENT.END_TIME is
'�������ʱ��'
/

comment on column BDC_MRPC.SYS_USER_AGENT.STATUS is
'״̬0��Ч1��Ч'
/

comment on column BDC_MRPC.SYS_USER_AGENT.CREATE_NAME is
'����������'
/

comment on column BDC_MRPC.SYS_USER_AGENT.CREATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_USER_AGENT.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_USER_AGENT.UPDATE_NAME is
'����������'
/

comment on column BDC_MRPC.SYS_USER_AGENT.UPDATE_BY is
'�����˵�¼����'
/

comment on column BDC_MRPC.SYS_USER_AGENT.UPDATE_TIME is
'��������'
/

comment on column BDC_MRPC.SYS_USER_AGENT.SYS_ORG_CODE is
'��������'
/

comment on column BDC_MRPC.SYS_USER_AGENT.SYS_COMPANY_CODE is
'������˾'
/

alter table BDC_MRPC.SYS_USER_AGENT
   add constraint SYS_C0025577 primary key (ID)
/

/*==============================================================*/
/* Index: BEGINTIME_INDEX                                       */
/*==============================================================*/
create index BDC_MRPC.BEGINTIME_INDEX on BDC_MRPC.SYS_USER_AGENT (
   START_TIME ASC
)
/

/*==============================================================*/
/* Index: ENDTIME_INDEX                                         */
/*==============================================================*/
create index BDC_MRPC.ENDTIME_INDEX on BDC_MRPC.SYS_USER_AGENT (
   END_TIME ASC
)
/

/*==============================================================*/
/* Index: STATUX_INDEX                                          */
/*==============================================================*/
create index BDC_MRPC.STATUX_INDEX on BDC_MRPC.SYS_USER_AGENT (
   STATUS ASC
)
/

/*==============================================================*/
/* Index: UNIQ_USERNAME                                         */
/*==============================================================*/
create unique index BDC_MRPC.UNIQ_USERNAME on BDC_MRPC.SYS_USER_AGENT (
   USER_NAME ASC
)
/

/*==============================================================*/
/* Table: SYS_USER_DEPART                                       */
/*==============================================================*/
create table BDC_MRPC.SYS_USER_DEPART 
(
   ID                   NVARCHAR2(32)        not null,
   USER_ID              NVARCHAR2(32),
   DEP_ID               NVARCHAR2(32)
)
/

comment on column BDC_MRPC.SYS_USER_DEPART.ID is
'id'
/

comment on column BDC_MRPC.SYS_USER_DEPART.USER_ID is
'�û�id'
/

comment on column BDC_MRPC.SYS_USER_DEPART.DEP_ID is
'����id'
/

alter table BDC_MRPC.SYS_USER_DEPART
   add constraint SYS_C0025579 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX_DEPART_GROUPKORGID                              */
/*==============================================================*/
create index BDC_MRPC.INDEX_DEPART_GROUPKORGID on BDC_MRPC.SYS_USER_DEPART (
   DEP_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX_DEPART_GROUPK_UIDANDDID                         */
/*==============================================================*/
create index BDC_MRPC.INDEX_DEPART_GROUPK_UIDANDDID on BDC_MRPC.SYS_USER_DEPART (
   USER_ID ASC,
   DEP_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX_DEPART_GROUPK_USERID                            */
/*==============================================================*/
create index BDC_MRPC.INDEX_DEPART_GROUPK_USERID on BDC_MRPC.SYS_USER_DEPART (
   USER_ID ASC
)
/

/*==============================================================*/
/* Table: SYS_USER_ROLE                                         */
/*==============================================================*/
create table BDC_MRPC.SYS_USER_ROLE 
(
   ID                   NVARCHAR2(32)        not null,
   USER_ID              NVARCHAR2(32),
   ROLE_ID              NVARCHAR2(32)
)
/

comment on table BDC_MRPC.SYS_USER_ROLE is
'�û���ɫ��'
/

comment on column BDC_MRPC.SYS_USER_ROLE.ID is
'����id'
/

comment on column BDC_MRPC.SYS_USER_ROLE.USER_ID is
'�û�id'
/

comment on column BDC_MRPC.SYS_USER_ROLE.ROLE_ID is
'��ɫid'
/

alter table BDC_MRPC.SYS_USER_ROLE
   add constraint SYS_C0025581 primary key (ID)
/

/*==============================================================*/
/* Index: INDEX2_GROUPUU_OLE_ID                                 */
/*==============================================================*/
create index BDC_MRPC.INDEX2_GROUPUU_OLE_ID on BDC_MRPC.SYS_USER_ROLE (
   ROLE_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX2_GROUPUU_USERIDANDROLEID                        */
/*==============================================================*/
create index BDC_MRPC.INDEX2_GROUPUU_USERIDANDROLEID on BDC_MRPC.SYS_USER_ROLE (
   USER_ID ASC,
   ROLE_ID ASC
)
/

/*==============================================================*/
/* Index: INDEX2_GROUPUU_USER_ID                                */
/*==============================================================*/
create index BDC_MRPC.INDEX2_GROUPUU_USER_ID on BDC_MRPC.SYS_USER_ROLE (
   USER_ID ASC
)
/

/*==============================================================*/
/* Table: TEST_PERSON                                           */
/*==============================================================*/
create table BDC_MRPC.TEST_PERSON 
(
   ID                   NVARCHAR2(36)        not null,
   CREATE_BY            NVARCHAR2(50),
   CREATE_TIME          DATE,
   UPDATE_BY            NVARCHAR2(50),
   UPDATE_TIME          DATE,
   SEX                  NVARCHAR2(32),
   NAME                 NVARCHAR2(200),
   CONTENT              NCLOB,
   BE_DATE              DATE,
   QJ_DAYS              NUMBER(11)
)
/

comment on column BDC_MRPC.TEST_PERSON.CREATE_BY is
'������'
/

comment on column BDC_MRPC.TEST_PERSON.CREATE_TIME is
'��������'
/

comment on column BDC_MRPC.TEST_PERSON.UPDATE_BY is
'������'
/

comment on column BDC_MRPC.TEST_PERSON.UPDATE_TIME is
'��������'
/

comment on column BDC_MRPC.TEST_PERSON.SEX is
'�Ա�'
/

comment on column BDC_MRPC.TEST_PERSON.NAME is
'�û���'
/

comment on column BDC_MRPC.TEST_PERSON.CONTENT is
'���ԭ��'
/

comment on column BDC_MRPC.TEST_PERSON.BE_DATE is
'���ʱ��'
/

comment on column BDC_MRPC.TEST_PERSON.QJ_DAYS is
'�������'
/

alter table BDC_MRPC.TEST_PERSON
   add constraint SYS_C0025583 primary key (ID)
/

alter table BDC_MRPC.QRTZ_BLOB_TRIGGERS
   add constraint SYS_C0025584 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      references BDC_MRPC.QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      not deferrable
/

alter table BDC_MRPC.QRTZ_CRON_TRIGGERS
   add constraint SYS_C0025585 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      references BDC_MRPC.QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      not deferrable
/

alter table BDC_MRPC.QRTZ_SIMPLE_TRIGGERS
   add constraint SYS_C0025586 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      references BDC_MRPC.QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      not deferrable
/

alter table BDC_MRPC.QRTZ_SIMPROP_TRIGGERS
   add constraint SYS_C0025587 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      references BDC_MRPC.QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
      not deferrable
/

alter table BDC_MRPC.QRTZ_TRIGGERS
   add constraint SYS_C0025588 foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP)
      references BDC_MRPC.QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
      not deferrable
/

alter table BDC_MRPC.WFI_PRODEF
   drop primary key cascade
/

drop table BDC_MRPC.WFI_PRODEF cascade constraints
/

/*==============================================================*/
/* Table: WFI_PRODEF                                            */
/*==============================================================*/
create table BDC_MRPC.WFI_PRODEF 
(
   PRODEF_ID            NVARCHAR2(50)        not null,
   PRODEFCLASS_ID       NVARCHAR2(50),
   DIVISION_CODE        NVARCHAR2(10),
   PRODEF_CODE          NVARCHAR2(300),
   PRODEF_NAME          NVARCHAR2(300),
   PRODEF_STATUS        NUMBER(8),
   OPERATION_TYPE       NVARCHAR2(300),
   DYLX                 NVARCHAR2(100),
   DJLX                 NVARCHAR2(100),
   QLLX                 NVARCHAR2(100),
   YHLX                 NVARCHAR2(100),
   SFJSBL               NVARCHAR2(100),
   SFQY                 NVARCHAR2(10),
   PRODEF_INDEX         NUMBER(8),
   PRODEF_DESC          NVARCHAR2(1000),
   PRODEF_TPL           NVARCHAR2(1000),
   TEMPLATEURL          NVARCHAR2(200),
   FORMURL              NVARCHAR2(100),
   SYSM                 NVARCHAR2(2000),
   YCXGZSH              NVARCHAR2(100),
   QLRFLAGE              NVARCHAR2(10),
   YWRFLAGE              NVARCHAR2(10),
   DYQRFLAGE              NVARCHAR2(10),
   CREATETIME           DATE,
   MODIFYTIME           DATE,
   VERSIONNO            NVARCHAR2(50)
)
/

comment on table BDC_MRPC.WFI_PRODEF is
'���̶���'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_ID is
'���̱��'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEFCLASS_ID is
'�������'
/

comment on column BDC_MRPC.WFI_PRODEF.DIVISION_CODE is
'��������'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_CODE is
'���̱���'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_NAME is
'��������'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_STATUS is
'����״̬'
/

comment on column BDC_MRPC.WFI_PRODEF.OPERATION_TYPE is
'ҵ������'
/

comment on column BDC_MRPC.WFI_PRODEF.DYLX is
'��Ԫ����'
/

comment on column BDC_MRPC.WFI_PRODEF.DJLX is
'�Ǽ�����'
/

comment on column BDC_MRPC.WFI_PRODEF.QLLX is
'Ȩ������'
/

comment on column BDC_MRPC.WFI_PRODEF.YHLX is
'�û�����'
/

comment on column BDC_MRPC.WFI_PRODEF.SFJSBL is
'�Ǽ�ʱʱ����'
/

comment on column BDC_MRPC.WFI_PRODEF.SFQY is
'�Ƿ�����'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_INDEX is
'��������'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_DESC is
'��������'
/

comment on column BDC_MRPC.WFI_PRODEF.PRODEF_TPL is
'����ģ��'
/

comment on column BDC_MRPC.WFI_PRODEF.TEMPLATEURL is
'ģ��·��'
/

comment on column BDC_MRPC.WFI_PRODEF.FORMURL is
'������Ϣ����ַ'
/

comment on column BDC_MRPC.WFI_PRODEF.SYSM is
'��������˵��'
/

comment on column BDC_MRPC.WFI_PRODEF.YCXGZSH is
'һ���Ը�֪�����µ�����id'
/

comment on column BDC_MRPC.WFI_PRODEF.CREATETIME is
'����ʱ��'
/

comment on column BDC_MRPC.WFI_PRODEF.MODIFYTIME is
'�޸�ʱ��'
/

comment on column BDC_MRPC.WFI_PRODEF.VERSIONNO is
'�汾��'
/

comment on column BDC_MRPC.WFI_PRODEF.QLRFLAGE is
'Ȩ���˱�ʶ���Ƿ���ҪȨ����0-��1-�ǣ�'
/

comment on column BDC_MRPC.WFI_PRODEF.YWRFLAGE is
'�����˱�ʶ���Ƿ���Ҫ������0-��1-�ǣ�'
/

comment on column BDC_MRPC.WFI_PRODEF.DYQRFLAGE is
'��ѺȨ���˱�ʶ���Ƿ���Ҫ��ѺȨ��0-��1-�ǣ�'
/


alter table BDC_MRPC.WFI_PRODEF
   add constraint PK_WFI_PRODEF primary key (PRODEF_ID)
/



alter table BDC_MRPC.WFI_PROINST
   drop primary key cascade
/

drop table BDC_MRPC.WFI_PROINST cascade constraints
/

/*==============================================================*/
/* Table: WFI_PROINST                                           */
/*==============================================================*/
create table BDC_MRPC.WFI_PROINST 
(
   PROINST_ID           NVARCHAR2(50)        not null,
   PRODEF_ID            NVARCHAR2(200),
   USER_ID              NVARCHAR2(106),
   Division_Code        NVARCHAR2(10),
   Division_Name        NVARCHAR2(10),
   YWLX                 NVARCHAR2(10),
   DJLX                 NVARCHAR2(10),
   QLLX                 NVARCHAR2(10),
   XMLX                 NVARCHAR2(10),
   SHZT                 NUMBER(10),
   PROLSH               NVARCHAR2(100),
   LSH                  NVARCHAR2(100),
   WF_PRODEFID          NVARCHAR2(50),
   WF_PRODEFNAME        NVARCHAR2(200),
   PRODEF_NAME          NVARCHAR2(300),
   PROJECT_NAME         NVARCHAR2(1000),
   TRANSATION_TYPE      NUMBER(8),
   PROINST_CODE         NVARCHAR2(300),
   PROINST_START        DATE,
   PROINST_END          DATE,
   PROINST_STATUS       NUMBER(8),
   CREAT_DATE           DATE,
   ACCEPTOR             NVARCHAR2(100),
   STAFF_DISTID         NVARCHAR2(100),
   INSTANCE_TYPE        NUMBER(8),
   SEND_MSG             NUMBER(10)           default 0,
   REMARKS              NVARCHAR2(1000)
)
/

comment on table BDC_MRPC.WFI_PROINST is
'����ʵ��'
/

comment on column BDC_MRPC.WFI_PROINST.PROINST_ID is
'����ʵ��ID'
/

comment on column BDC_MRPC.WFI_PROINST.PRODEF_ID is
'���̱��'
/

comment on column BDC_MRPC.WFI_PROINST.USER_ID is
'Ա�����'
/

comment on column BDC_MRPC.WFI_PROINST.Division_Code is
'��������'
/

comment on column BDC_MRPC.WFI_PROINST.Division_Name is
'��������'
/

comment on column BDC_MRPC.WFI_PROINST.YWLX is
'ҵ�����ͣ�0:���ˣ�1:��ҵ��'
/

comment on column BDC_MRPC.WFI_PROINST.DJLX is
'�Ǽ����� �Ǽ������ֵ�'
/

comment on column BDC_MRPC.WFI_PROINST.QLLX is
'Ȩ������ Ȩ�������ֵ�'
/

comment on column BDC_MRPC.WFI_PROINST.XMLX is
'��Ŀ���� �������ֲ�ͬ������12��ͨ�õģ�11��ӥ̶'
/

comment on column BDC_MRPC.WFI_PROINST.SHZT is
'���״̬ -1��������δ�ύ��ǰ�û���0��δ��ˣ�10��Ȩ���������ͨ����11��Ȩ��������˲��أ�20����Ŀ�������ͨ����21����Ŀ������˲���'
/

comment on column BDC_MRPC.WFI_PROINST.PROLSH is
'��ˮ�� ������ˮ��'
/

comment on column BDC_MRPC.WFI_PROINST.LSH is
'��ˮ�� ����������Ŀ���ɵ���ˮ��'
/

comment on column BDC_MRPC.WFI_PROINST.WF_PRODEFID is
'����������ID ������Ŀ��Ӧ�������������ID'
/

comment on column BDC_MRPC.WFI_PROINST.WF_PRODEFNAME is
'������������ ��Ӧ����������������'
/

comment on column BDC_MRPC.WFI_PROINST.PRODEF_NAME is
'��������'
/

comment on column BDC_MRPC.WFI_PROINST.PROJECT_NAME is
'��Ŀ����'
/

comment on column BDC_MRPC.WFI_PROINST.TRANSATION_TYPE is
'ʵ������ʽ'
/

comment on column BDC_MRPC.WFI_PROINST.PROINST_CODE is
'���̱���'
/

comment on column BDC_MRPC.WFI_PROINST.PROINST_START is
'��ʼʱ��'
/

comment on column BDC_MRPC.WFI_PROINST.PROINST_END is
'����ʱ��'
/

comment on column BDC_MRPC.WFI_PROINST.PROINST_STATUS is
'����ʵ��״̬'
/

comment on column BDC_MRPC.WFI_PROINST.CREAT_DATE is
'����ʱ��'
/

comment on column BDC_MRPC.WFI_PROINST.ACCEPTOR is
'������Ա'
/

comment on column BDC_MRPC.WFI_PROINST.STAFF_DISTID is
'Ա������ID'
/

comment on column BDC_MRPC.WFI_PROINST.INSTANCE_TYPE is
'����ʵ������'
/

comment on column BDC_MRPC.WFI_PROINST.SEND_MSG is
'������Ϣ״̬ 0������Ҫ������Ϣ��1����Ҫ������Ϣ'
/

comment on column BDC_MRPC.WFI_PROINST.REMARKS is
'��Ŀ��ע'
/

alter table BDC_MRPC.WFI_PROINST
   add constraint PK_WFI_PROINST primary key (PROINST_ID)
/


/*==============================================================*/
/* Table: BDC_DY                                                */
/*==============================================================*/
create table BDC_MRPC.BDC_DY 
(
   ID                   NVARCHAR2(50)        not null,
   BDCDYID              NVARCHAR2(50),
   YSDM                 NVARCHAR2(10),
   BDCDYH               NVARCHAR2(50),
   XMBH                 NVARCHAR2(50),
   FWBM                 NVARCHAR2(50),
   ZRZBDCDYID           NVARCHAR2(50),
   ZDDM                 NVARCHAR2(19),
   ZDBDCDYID            NVARCHAR2(50),
   Division_Code      NVARCHAR2(10),
   PROLSH               NVARCHAR2(100),
   ZRZH                 NVARCHAR2(50),
   ZL                   NVARCHAR2(100),
   MJDW                 NVARCHAR2(10),
   ZCS                  NUMBER,
   HH                   NUMBER,
   SHBW                 NVARCHAR2(20),
   HX                   NVARCHAR2(10),
   HXJG                 NVARCHAR2(10),
   FWYT                 NVARCHAR2(10),
   MJ                   NUMBER(38,16),
   TNJZMJ               NUMBER(38,16),
   FTJZMJ               NUMBER(38,16),
   GYTDMJ               NUMBER(38,16),
   FTTDMJ               NUMBER(38,16),
   DYTDMJ               NUMBER(38,16),
   TDSYQR               NVARCHAR2(1000),
   FDCJYJG              NUMBER(38,16),
   GHYT                 NVARCHAR2(10),
   FWJG                 NVARCHAR2(10),
   JGSJ                 DATE,
   FWLX                 NVARCHAR2(10),
   FWXZ                 NVARCHAR2(10),
   ZDMJ                 NUMBER(38,16),
   CQLY                 NVARCHAR2(50),
   QTGSD                NVARCHAR2(50),
   QTGSX                NVARCHAR2(50),
   QTGSN                NVARCHAR2(50),
   QTGSB                NVARCHAR2(50),
   FCFHT                NVARCHAR2(50),
   ZT                   NUMBER,
   YXBZ                 NVARCHAR2(10),
   CQZT                 NVARCHAR2(10),
   DYZT                 NVARCHAR2(10),
   XZZT                 NVARCHAR2(10),
   BLZT                 NVARCHAR2(10),
   YYZT                 NVARCHAR2(10),
   FH                   NVARCHAR2(50),
   DJZT                 NVARCHAR2(10),
   BGZT                 NVARCHAR2(10),
   RELATIONID           NVARCHAR2(50),
   QLXZ                 NVARCHAR2(10),
   TDSYQQSRQ            DATE,
   TDSYQZZRQ            DATE,
   TDSYNX               NUMBER,
   QSC                  NUMBER(38,16),
   ZZC                  NUMBER(38,16),
   SZC                  NVARCHAR2(100),
   DYH                  NVARCHAR2(100),
   BZ                   NVARCHAR2(500),
   CREATETIME           DATE                 default SYSDATE,
   MODIFYTIME           DATE,
   VERSIONNO            NVARCHAR2(50)
);

comment on table BDC_MRPC.BDC_DY is
'��������Ԫ��Ϣ��';

comment on column BDC_MRPC.BDC_DY.ID is
'����';

comment on column BDC_MRPC.BDC_DY.Division_Code is
'���ش���';

comment on column BDC_MRPC.BDC_DY.PROLSH is
'��ˮ�� ������ˮ��';

comment on column BDC_MRPC.BDC_DY.RELATIONID is
'����ID';

comment on column BDC_MRPC.BDC_DY.QLXZ is
'2��Ȩ������';

comment on column BDC_MRPC.BDC_DY.TDSYQQSRQ is
'����ʹ��Ȩ��ʼ����';

comment on column BDC_MRPC.BDC_DY.TDSYQZZRQ is
'����ʹ��Ȩ��ֹ����';

comment on column BDC_MRPC.BDC_DY.TDSYNX is
'����ʹ������';

comment on column BDC_MRPC.BDC_DY.QSC is
'��ʼ��';

comment on column BDC_MRPC.BDC_DY.ZZC is
'��ֹ��';

comment on column BDC_MRPC.BDC_DY.SZC is
'���ڲ�';

comment on column BDC_MRPC.BDC_DY.DYH is
'��Ԫ��';

comment on column BDC_MRPC.BDC_DY.BZ is
'��ע';

comment on column BDC_MRPC.BDC_DY.VERSIONNO is
'�汾��';

alter table BDC_MRPC.BDC_DY
   add constraint PK_BDC_DY primary key (ID);
   
   
/*==============================================================*/
/* Table: BDC_QLDY                                                */
/*==============================================================*/

create table BDC_MRPC.BDC_QLDY
(
  ID         VARCHAR2(64) not null
    primary key,
  PROLSH     VARCHAR2(60),
  DYID       VARCHAR2(100),
  QLID       VARCHAR2(100),
  CREATETIME DATE
)

COMMENT ON COLUMN BDC_QLDY.ID IS '����';
COMMENT ON COLUMN BDC_QLDY.PROLSH IS 'ҵ����ˮ��';
COMMENT ON COLUMN BDC_QLDY.DYID IS '��Ԫid';
COMMENT ON COLUMN BDC_QLDY.QLID IS 'Ȩ��id';
COMMENT ON COLUMN BDC_QLDY.CREATETIME IS '����ʱ��'

