
/**
 * 
 * 代码生成器自动生成[WFI_ACTINST]
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
@Table(name = "WFI_ACTINST",schema = "BDC_WORKFLOW")
public class Wfi_ActInst {

	private String Actinst_Id;
    private Date Hangup_Time;
    private Date Hangdowm_Time;
    private String Hangup_Staff_Name;
    private String Hangup_Staff_Id;
	private Short Askdelay_Days;
	private Date Hungup_Date;
	private Integer Passedroute_Count;
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
	private String Prodef_Id;
	private Integer Actinst_Status;
	private Integer Instance_Type;
	private String Actdef_Type;
	private String Overruleactinst_Ids;
	private Date Hangpausetime;
	private String Passoverdesc;
	private String Staff_Name;
	private Integer Istransfer;
	private Date Transfer_Time;
	private String Msg;
	private Integer Status;
    private Integer Uploadfile;
    private Integer Codeal;
    
    private Integer isApplyHandup;
    private String NotAppPassRes;
    private Integer statusExt;
    private Integer IsSkipPassOver;
    private String ToStaffName;
    
    
    private String fromActinstid;//用于聚合转出标志来自于同一个聚合实例
    
    private Date   LockDate;
    private Date   UnLockDate;
    private String LockStaffId;
    private String LockStaffName;
    private String LockMsg;
    private String LockType;
    
    @Column(name = "STATUSEXT")
    public Integer getStatusExt() {
		return statusExt;
	}
                      
	public void setStatusExt(Integer statusExt) {
		this.statusExt = statusExt;
	}

	@Column(name = "NOTAPPPASSRES",length = 2000)
    public String getNotAppPassRes() {
		return NotAppPassRes;
	}

	public void setNotAppPassRes(String notAppPassRes) {
		NotAppPassRes = notAppPassRes;
	}

	@Column(name = "ISAPPLYHANGUP",length = 400)
	public Integer getIsApplyHandup() {
		return isApplyHandup;
	}

	public void setIsApplyHandup(Integer isApplyHandup) {
		this.isApplyHandup = isApplyHandup;
	}

	@Column(name = "MSG",length = 400)
	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	@Id
	@Column(name = "ACTINST_ID", length = 400)
	public String getActinst_Id() {
		if (Actinst_Id == null)
			Actinst_Id = UUID.randomUUID().toString().replace("-", "");
		return Actinst_Id;
	}

	public void setActinst_Id(String Actinst_Id) {
		this.Actinst_Id = Actinst_Id;
	}
	 @Column(name = "HANGUP_TIME", length=7)
     public  Date  getHangup_Time() {
       return Hangup_Time;
  }

     public void setHangup_Time( Date Hangup_Time) {
       this.Hangup_Time = Hangup_Time;
  }

   @Column(name = "HANGDOWM_TIME", length=7)
     public  Date  getHangdowm_Time() {
       return Hangdowm_Time;
  }

     public void setHangdowm_Time( Date Hangdowm_Time) {
       this.Hangdowm_Time = Hangdowm_Time;
  }

   @Column(name = "HANGUP_STAFF_NAME",length = 400)
     public  String  getHangup_Staff_Name() {
       return Hangup_Staff_Name;
  }

     public void setHangup_Staff_Name( String Hangup_Staff_Name) {
       this.Hangup_Staff_Name = Hangup_Staff_Name;
  }

   @Column(name = "HANGUP_STAFF_ID",length = 400)
     public  String  getHangup_Staff_Id() {
       return Hangup_Staff_Id;
  }

     public void setHangup_Staff_Id( String Hangup_Staff_Id) {
       this.Hangup_Staff_Id = Hangup_Staff_Id;
  }
	@Column(name = "ASKDELAY_DAYS")
	public Short getAskdelay_Days() {
		return Askdelay_Days;
	}

	public void setAskdelay_Days(Short Askdelay_Days) {
		this.Askdelay_Days = Askdelay_Days;
	}

	@Column(name = "HUNGUP_DATE", length = 7)
	public Date getHungup_Date() {
		return Hungup_Date;
	}

	public void setHungup_Date(Date Hungup_Date) {
		this.Hungup_Date = Hungup_Date;
	}

	@Column(name = "PASSEDROUTE_COUNT")
	public Integer getPassedroute_Count() {
		return Passedroute_Count;
	}

	public void setPassedroute_Count(Integer Passedroute_Count) {
		this.Passedroute_Count = Passedroute_Count;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "STAFF_ID", length = 400)
	public String getStaff_Id() {
		return Staff_Id;
	}

	public void setStaff_Id(String Staff_Id) {
		this.Staff_Id = Staff_Id;
	}

	@Column(name = "ACTINST_START", length = 7)
	public Date getActinst_Start() {
		return Actinst_Start;
	}

	public void setActinst_Start(Date Actinst_Start) {
		this.Actinst_Start = Actinst_Start;
	}

	@Column(name = "ACTINST_END", length = 7)
	public Date getActinst_End() {
		return Actinst_End;
	}

	public void setActinst_End(Date Actinst_End) {
		this.Actinst_End = Actinst_End;
	}

	@Column(name = "DEPT_CODE", length = 400)
	public String getDept_Code() {
		return Dept_Code;
	}

	public void setDept_Code(String Dept_Code) {
		this.Dept_Code = Dept_Code;
	}

	@Column(name = "ACTINST_WILLFINISH", length = 7)
	public Date getActinst_Willfinish() {
		return Actinst_Willfinish;
	}

	public void setActinst_Willfinish(Date Actinst_Willfinish) {
		this.Actinst_Willfinish = Actinst_Willfinish;
	}

	@Column(name = "OPERATION_TYPE", length = 600)
	public String getOperation_Type() {
		return Operation_Type;
	}

	public void setOperation_Type(String Operation_Type) {
		this.Operation_Type = Operation_Type;
	}

	@Column(name = "VERSION", length = 600)
	public String getVersion() {
		return Version;
	}

	public void setVersion(String Version) {
		this.Version = Version;
	}

	@Column(name = "STAFF_IDS", length = 600)
	public String getStaff_Ids() {
		return Staff_Ids;
	}

	public void setStaff_Ids(String Staff_Ids) {
		this.Staff_Ids = Staff_Ids;
	}

	@Column(name = "ACTINST_MSG", length = 600)
	public String getActinst_Msg() {
		return Actinst_Msg;
	}

	public void setActinst_Msg(String Actinst_Msg) {
		this.Actinst_Msg = Actinst_Msg;
	}

	@Column(name = "ACTINST_NAME", length = 1200)
	public String getActinst_Name() {
		return Actinst_Name;
	}

	public void setActinst_Name(String Actinst_Name) {
		this.Actinst_Name = Actinst_Name;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

	@Column(name = "ACTINST_STATUS")
	public Integer getActinst_Status() {
		return Actinst_Status;
	}

	public void setActinst_Status(Integer Status) {
		this.Actinst_Status = Status;
	}

	@Column(name = "INSTANCE_TYPE")
	public Integer getInstance_Type() {
		return Instance_Type;
	}

	public void setInstance_Type(Integer Instance_Type) {
		this.Instance_Type = Instance_Type;
	}

	@Column(name = "ACTDEF_TYPE", length = 400)
	public String getActdef_Type() {
		return Actdef_Type;
	}

	public void setActdef_Type(String Actdef_Type) {
		this.Actdef_Type = Actdef_Type;
	}

	@Column(name = "OVERRULEACTINST_IDS", length = 400)
	public String getOverruleactinst_Ids() {
		return Overruleactinst_Ids;
	}

	public void setOverruleactinst_Ids(String Overruleactinst_Ids) {
		this.Overruleactinst_Ids = Overruleactinst_Ids;
	}

	@Column(name = "HANGPAUSETIME", length = 7)
	public Date getHangpausetime() {
		return Hangpausetime;
	}

	public void setHangpausetime(Date Hangpausetime) {
		this.Hangpausetime = Hangpausetime;
	}

	@Column(name = "PASSOVERDESC", length = 400)
	public String getPassoverdesc() {
		return Passoverdesc;
	}

	public void setPassoverdesc(String Passoverdesc) {
		this.Passoverdesc = Passoverdesc;
	}

	/**
	 * @return the staff_Name
	 */
	@Column(name = "STAFF_NAME", length = 400)
	public String getStaff_Name() {
		return Staff_Name;
	}

	/**
	 * @param staff_Name the staff_Name to set
	 */
	public void setStaff_Name(String staff_Name) {
		Staff_Name = staff_Name;
	}

	
	 /* @return the istransfer
			 */
			@Column(name = "ISTRANSFER")
			public Integer getIstransfer() {
				return Istransfer;
			}

			/**
			 * @param istransfer the istransfer to set
			 */
			public void setIstransfer(Integer istransfer) {
				Istransfer = istransfer;
			}

			/**
			 * @return the transfer_Time
			 */
			@Column(name = "TRANSFER_TIME", length = 7)
			public Date getTransfer_Time() {
				return Transfer_Time;
			}

			/**
			 * @param transfer_Time the transfer_Time to set
			 */
			public void setTransfer_Time(Date transfer_Time) {
				Transfer_Time = transfer_Time;
			}
			@Column(name = "UPLOADFILE")
			public Integer getUploadfile() {
				return Uploadfile;
			}

			public void setUploadfile(Integer uploadfile) {
				Uploadfile = uploadfile;
			}
			@Column(name = "CODEAL")
			public Integer getCodeal() {
				return Codeal;
			}

			public void setCodeal(Integer codeal) {
				Codeal = codeal;
			}

			public Integer getIsSkipPassOver() {
				return IsSkipPassOver;
			}

			public void setIsSkipPassOver(Integer isSkipPassOver) {
				IsSkipPassOver = isSkipPassOver;
			}
		    @Column(name = "TOSTAFFNAME",length = 400)
			public  String  getToStaffName() {
			  return ToStaffName;
		    }

			public void setToStaffName( String toStaffName) {
			  this.ToStaffName = toStaffName;
			}
			 @Column(name = "FROMACTINSTID",length = 400)
			public String getFromActinstid() {
				return fromActinstid;
			}

			public void setFromActinstid(String fromActinstid) {
				this.fromActinstid = fromActinstid;
			}
			
			@Column(name = "LOCKDATE")
			public Date getLockDate() {
				return LockDate;
			}

			public void setLockDate(Date lockDate) {
				LockDate = lockDate;
			}
			
			@Column(name = "LOCKSTAFFID",length = 400)
			public String getLockStaffId() {
				return LockStaffId;
			}

			public void setLockStaffId(String lockStaffId) {
				LockStaffId = lockStaffId;
			}
			
			@Column(name = "LOCKSTAFFNAME",length = 400)
			public String getLockStaffName() {
				return LockStaffName;
			}

			public void setLockStaffName(String lockStaffName) {
				LockStaffName = lockStaffName;
			}
			
			@Column(name = "LOCKMSG",length = 400)
			public String getLockMsg() {
				return LockMsg;
			}

			public void setLockMsg(String lockMsg) {
				LockMsg = lockMsg;
			}
			
			@Column(name = "LOCKTYPE",length = 400)
			public String getLockType() {
				return LockType;
			}

			public void setLockType(String lockType) {
				LockType = lockType;
			}
			
			@Column(name = "UNLOCKDATE")
			public Date getUnLockDate() {
				return UnLockDate;
			}

			public void setUnLockDate(Date unLockDate) {
				UnLockDate = unLockDate;
			}

}
