

		    /**
            * 
            * 代码生成器自动生成[WFI_NOWACTINST]
            * 
            */

            package com.supermap.wisdombusiness.workflow.model;

            import java.util.UUID;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

            @Entity
            @Table(name = "WFI_NOWACTINST",schema = "BDC_WORKFLOW")
            public class Wfi_NowActInst{
            	
             private String Actinst_Id;
             private String Actdef_Id;
             private String Proinst_Id;
             private String Staff_Id;
             private Date Actinst_Start;
             private Date Actinst_End;
             private String Dept_Code;
             private Date Actinst_Willfinish;
             private String Operation_Type;
             private String Version;
             private String Staff_Ids;
             private String Actinst_Msg;
             private String Actinst_Name;
             private Integer Actinst_Status;
             private Integer Instance_Type;
             private String Actdef_Type;
             private String Overruleactinst_Ids;
             private Date Hangpausetime;
             private String Passoverdesc;
             private Short Askdelay_Days;
             private Date Hungup_Date;
             private Integer Passedroute_Count;
             private Integer Status;
             private Integer IsapplyHangup;
             private String NotApppassers;
             private Date UnlockDate;
             private String LockType;
             private String Msg;
             private Integer Codeal;
	
             @Id  @Column(name = "ACTINST_ID",length = 400)
                public  String  getActinst_Id() {                
              return Actinst_Id;
             }
				public void setActinst_Id( String Actinst_Id) {
                  this.Actinst_Id = Actinst_Id;
             }

              @Column(name = "ACTDEF_ID",length = 400)
                public  String  getActdef_Id() {
                  return Actdef_Id;
             }

                public void setActdef_Id( String Actdef_Id) {
                  this.Actdef_Id = Actdef_Id;
             }

              @Column(name = "PROINST_ID",length = 400)
                public  String  getProinst_Id() {
                  return Proinst_Id;
             }

                public void setProinst_Id( String Proinst_Id) {
                  this.Proinst_Id = Proinst_Id;
             }

              @Column(name = "STAFF_ID",length = 400)
                public  String  getStaff_Id() {
                  return Staff_Id;
             }

                public void setStaff_Id( String Staff_Id) {
                  this.Staff_Id = Staff_Id;
             }

              @Column(name = "ACTINST_START", length=7)
                public  Date  getActinst_Start() {
                  return Actinst_Start;
             }

                public void setActinst_Start( Date Actinst_Start) {
                  this.Actinst_Start = Actinst_Start;
             }

              @Column(name = "ACTINST_END", length=7)
                public  Date  getActinst_End() {
                  return Actinst_End;
             }

                public void setActinst_End( Date Actinst_End) {
                  this.Actinst_End = Actinst_End;
             }

              @Column(name = "DEPT_CODE",length = 400)
                public  String  getDept_Code() {
                  return Dept_Code;
             }

                public void setDept_Code( String Dept_Code) {
                  this.Dept_Code = Dept_Code;
             }

              @Column(name = "ACTINST_WILLFINISH", length=7)
                public  Date  getActinst_Willfinish() {
                  return Actinst_Willfinish;
             }

                public void setActinst_Willfinish( Date Actinst_Willfinish) {
                  this.Actinst_Willfinish = Actinst_Willfinish;
             }

              @Column(name = "OPERATION_TYPE",length = 600)
                public  String  getOperation_Type() {
                  return Operation_Type;
             }

                public void setOperation_Type( String Operation_Type) {
                  this.Operation_Type = Operation_Type;
             }

              @Column(name = "VERSION",length = 600)
                public  String  getVersion() {
                  return Version;
             }

                public void setVersion( String Version) {
                  this.Version = Version;
             }

              @Column(name = "STAFF_IDS",length = 600)
                public  String  getStaff_Ids() {
                  return Staff_Ids;
             }

                public void setStaff_Ids( String Staff_Ids) {
                  this.Staff_Ids = Staff_Ids;
             }

              @Column(name = "ACTINST_MSG",length = 600)
                public  String  getActinst_Msg() {
                  return Actinst_Msg;
             }

                public void setActinst_Msg( String Actinst_Msg) {
                  this.Actinst_Msg = Actinst_Msg;
             }

              @Column(name = "ACTINST_NAME",length = 1200)
                public  String  getActinst_Name() {
                  return Actinst_Name;
             }

                public void setActinst_Name( String Actinst_Name) {
                  this.Actinst_Name = Actinst_Name;
             }

              @Column(name = "ACTINST_STATUS")
                public  Integer  getActinst_Status() {
                  return Actinst_Status;
             }

                public void setActinst_Status( Integer Status) {
                  this.Actinst_Status = Status;
             }

              @Column(name = "INSTANCE_TYPE")
                public  Integer  getInstance_Type() {
                  return Instance_Type;
             }

                public void setInstance_Type( Integer Instance_Type) {
                  this.Instance_Type = Instance_Type;
             }

              @Column(name = "ACTDEF_TYPE",length = 400)
                public  String  getActdef_Type() {
                  return Actdef_Type;
             }

                public void setActdef_Type( String Actdef_Type) {
                  this.Actdef_Type = Actdef_Type;
             }

              @Column(name = "OVERRULEACTINST_IDS",length = 400)
                public  String  getOverruleactinst_Ids() {
                  return Overruleactinst_Ids;
             }

                public void setOverruleactinst_Ids( String Overruleactinst_Ids) {
                  this.Overruleactinst_Ids = Overruleactinst_Ids;
             }

              @Column(name = "HANGPAUSETIME", length=7)
                public  Date  getHangpausetime() {
                  return Hangpausetime;
             }

                public void setHangpausetime( Date Hangpausetime) {
                  this.Hangpausetime = Hangpausetime;
             }

              @Column(name = "PASSOVERDESC",length = 400)
                public  String  getPassoverdesc() {
                  return Passoverdesc;
             }

                public void setPassoverdesc( String Passoverdesc) {
                  this.Passoverdesc = Passoverdesc;
             }

              @Column(name = "ASKDELAY_DAYS")
                public  Short  getAskdelay_Days() {
                  return Askdelay_Days;
             }

                public void setAskdelay_Days( Short Askdelay_Days) {
                  this.Askdelay_Days = Askdelay_Days;
             }

              @Column(name = "HUNGUP_DATE", length=7)
                public  Date  getHungup_Date() {
                  return Hungup_Date;
             }

                public void setHungup_Date( Date Hungup_Date) {
                  this.Hungup_Date = Hungup_Date;
             }

              @Column(name = "PASSEDROUTE_COUNT")
                public  Integer  getPassedroute_Count() {
                  return Passedroute_Count;
             }

                public void setPassedroute_Count( Integer Passedroute_Count) {
                  this.Passedroute_Count = Passedroute_Count;
             }
                
                @Column(name = "STATUS")
                public Integer getStatus() {
    				return Status;
    			}

    			public void setStatus(Integer status) {
    				Status = status;
    			}
    			
    			@Column(name = "ISAPPLYHANGUP")
    			public Integer getIsapplyHangup() {
    				return IsapplyHangup;
    			}

    			public void setIsapplyHangup(Integer isapplyHangup) {
    				IsapplyHangup = isapplyHangup;
    			}

    			@Column(name = "NOTAPPPASSRES")
    			public String getNotApppassers() {
    				return NotApppassers;
    			}

    			public void setNotApppassers(String notApppassers) {
    				NotApppassers = notApppassers;
    			}

    			@Column(name = "UNLOCKDATE")
    			public Date getUnlockDate() {
    				return UnlockDate;
    			}

    			public void setUnlockDate(Date unlockDate) {
    				UnlockDate = unlockDate;
    			}

    			@Column(name = "LOCKTYPE")
    			public String getLockType() {
    				return LockType;
    			}

    			public void setLockType(String lockType) {
    				LockType = lockType;
    			}

    			@Column(name = "MSG")
    			public String getMsg() {
    				return Msg;
    			}

    			public void setMsg(String msg) {
    				Msg = msg;
    			}

    			@Column(name = "CODEAL")
    			public Integer getCodeal() {
    				return Codeal;
    			}

    			public void setCodeal(Integer codeal) {
    				Codeal = codeal;
    			}

                
}
