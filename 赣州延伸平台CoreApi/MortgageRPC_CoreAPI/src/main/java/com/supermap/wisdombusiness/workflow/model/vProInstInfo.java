
/**
 * 
 * 代码生成器自动生成[V_PROINSTINFO]
 * 
 */
package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_PROINSTINFO",schema = "BDC_WORKFLOW")
public class vProInstInfo {

	private String _actinst_id;
	private String _prodef_id;
	private String _prodef_type;
	private String _protype_id;
	private String _lcbh;
	private String _prodef_name;
	private Integer _prodef_order;
	private String _proinst_name;
	private String _proinst_usernumber;
	private String _staffdist_id;
	private String _district_id;
	private String _accepttype_id;
	private String _file_number;
	private String _acceptor;
	private String _staffid;
	private String _remark;
	private String _proinst_id;
	private String _message;
	private String _proinst_time;
	private String _finishdate;
	private String _distmodify;
	private String _havedone;
	private Integer _file_urgency;
	private String _actdef_name;
	private Integer _status;
	private String _starttime;
	private String _endtime;
	private String _prostarttime;
	private String _actwillfinishtime;
	private String _actdef_type;

	@Id
	@Column(name = "ACTINST_ID", length = 400)
	public String getActinst_Id() {
		if (_actinst_id == null)
			_actinst_id = UUID.randomUUID().toString().replace("-", "");
		return _actinst_id;
	}

	public void setActinst_Id(String _actinst_id) {
		this._actinst_id = _actinst_id;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return _prodef_id;
	}

	public void setProdef_Id(String _prodef_id) {
		this._prodef_id = _prodef_id;
	}

	@Column(name = "PRODEF_TYPE", length = 400)
	public String getProdef_Type() {
		return _prodef_type;
	}

	public void setProdef_Type(String _prodef_type) {
		this._prodef_type = _prodef_type;
	}

	@Column(name = "PROTYPE_ID", length = 400)
	public String getProtype_Id() {
		return _protype_id;
	}

	public void setProtype_Id(String _protype_id) {
		this._protype_id = _protype_id;
	}

	@Column(name = "LCBH", length = 600)
	public String getLcbh() {
		return _lcbh;
	}

	public void setLcbh(String _lcbh) {
		this._lcbh = _lcbh;
	}

	@Column(name = "PRODEF_NAME", length = 600)
	public String getProdef_Name() {
		return _prodef_name;
	}

	public void setProdef_Name(String _prodef_name) {
		this._prodef_name = _prodef_name;
	}

	@Column(name = "PRODEF_ORDER", precision = 22, scale = 0)
	public Integer getProdef_Order() {
		return _prodef_order;
	}

	public void setProdef_Order(Integer _prodef_order) {
		this._prodef_order = _prodef_order;
	}

	@Column(name = "PROINST_NAME", length = 1000)
	public String getProinst_Name() {
		return _proinst_name;
	}

	public void setProinst_Name(String _proinst_name) {
		this._proinst_name = _proinst_name;
	}

	@Column(name = "PROINST_USERNUMBER", length = 400)
	public String getProinst_Usernumber() {
		return _proinst_usernumber;
	}

	public void setProinst_Usernumber(String _proinst_usernumber) {
		this._proinst_usernumber = _proinst_usernumber;
	}

	@Column(name = "STAFFDIST_ID", length = 400)
	public String getStaffdist_Id() {
		return _staffdist_id;
	}

	public void setStaffdist_Id(String _staffdist_id) {
		this._staffdist_id = _staffdist_id;
	}

	@Column(name = "DISTRICT_ID", length = 400)
	public String getDistrict_Id() {
		return _district_id;
	}

	public void setDistrict_Id(String _district_id) {
		this._district_id = _district_id;
	}

	@Column(name = "ACCEPTTYPE_ID", length = 400)
	public String getAccepttype_Id() {
		return _accepttype_id;
	}

	public void setAccepttype_Id(String _accepttype_id) {
		this._accepttype_id = _accepttype_id;
	}

	@Column(name = "FILE_NUMBER", length = 100)
	public String getFile_Number() {
		return _file_number;
	}

	public void setFile_Number(String _file_number) {
		this._file_number = _file_number;
	}

	@Column(name = "ACCEPTOR", length = 100)
	public String getAcceptor() {
		return _acceptor;
	}

	public void setAcceptor(String _acceptor) {
		this._acceptor = _acceptor;
	}

	@Column(name = "STAFFID", length = 400)
	public String getStaffid() {
		return _staffid;
	}

	public void setStaffid(String _staffid) {
		this._staffid = _staffid;
	}

	@Column(name = "REMARK", length = 400)
	public String getRemark() {
		return _remark;
	}

	public void setRemark(String _remark) {
		this._remark = _remark;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return _proinst_id;
	}

	public void setProinst_Id(String _proinst_id) {
		this._proinst_id = _proinst_id;
	}

	@Column(name = "MESSAGE", length = 400)
	public String getMessage() {
		return _message;
	}

	public void setMessage(String _message) {
		this._message = _message;
	}

	@Column(name = "PROINST_TIME", length = 400)
	public String getProinst_Time() {
		return _proinst_time;
	}

	public void setProinst_Time(String _proinst_time) {
		this._proinst_time = _proinst_time;
	}

	@Column(name = "FINISHDATE", length = 400)
	public String getFinishdate() {
		return _finishdate;
	}

	public void setFinishdate(String _finishdate) {
		this._finishdate = _finishdate;
	}

	@Column(name = "DISTMODIFY", length = 400)
	public String getDistmodify() {
		return _distmodify;
	}

	public void setDistmodify(String _distmodify) {
		this._distmodify = _distmodify;
	}

	@Column(name = "HAVEDONE", length = 400)
	public String getHavedone() {
		return _havedone;
	}

	public void setHavedone(String _havedone) {
		this._havedone = _havedone;
	}

	@Column(name = "FILE_URGENCY")
	public Integer getFile_Urgency() {
		return _file_urgency;
	}

	public void setFile_Urgency(Integer _file_urgency) {
		this._file_urgency = _file_urgency;
	}

	@Column(name = "ACTDEF_NAME", length = 1200)
	public String getActdef_Name() {
		return _actdef_name;
	}

	public void setActdef_Name(String _actdef_name) {
		this._actdef_name = _actdef_name;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return _status;
	}

	public void setStatus(Integer _status) {
		this._status = _status;
	}

	@Column(name = "STARTTIME", length = 10)
	public String getStarttime() {
		return _starttime;
	}

	public void setStarttime(String _starttime) {
		this._starttime = _starttime;
	}

	@Column(name = "ENDTIME", length = 10)
	public String getEndtime() {
		return _endtime;
	}

	public void setEndtime(String _endtime) {
		this._endtime = _endtime;
	}

	@Column(name = "PROSTARTTIME", length = 10)
	public String getProstarttime() {
		return _prostarttime;
	}

	public void setProstarttime(String _prostarttime) {
		this._prostarttime = _prostarttime;
	}

	@Column(name = "ACTWILLFINISHTIME", length = 10)
	public String getActwillfinishtime() {
		return _actwillfinishtime;
	}

	public void setActwillfinishtime(String _actwillfinishtime) {
		this._actwillfinishtime = _actwillfinishtime;
	}
	@Column(name = "ACTDEF_TYPE", length = 400)
	public String get_actdef_type() {
		return _actdef_type;
	}

	public void set_actdef_type(String _actdef_type) {
		this._actdef_type = _actdef_type;
	}

}
