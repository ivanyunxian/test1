package com.supermap.wisdombusiness.synchroinline.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Pro_slxmsh")
public class Pro_slxmsh
{
	@Column
	@Id
	protected String sh_id;
	@Column
	protected String sh_qjdc_ry;
	@Column
	protected String sh_qjdc_yj;
	@Column
	protected Date sh_qjdc_rq;
	@Column
	protected String sh_xmsl_ry;
	@Column
	protected String sh_xmsl_yj;
	@Column
	protected Date sh_xmsl_rq;
	@Column
	protected String slxm_id;
	@Column
	protected Integer shzt;
	
	public Integer getShzt() {
		return shzt;
	}
	public void setShzt(Integer shzt) {
		this.shzt = shzt;
	}
	/**
	 * @return the sh_id
	 */
	public String getSh_id()
	{
		return sh_id;
	}
	/**
	 * @param sh_id the sh_id to set
	 */
	public void setSh_id(String sh_id)
	{
		this.sh_id = sh_id;
	}
	/**
	 * @return the sh_qjdc_ry
	 */
	public String getSh_qjdc_ry()
	{
		return sh_qjdc_ry;
	}
	/**
	 * @param sh_qjdc_ry the sh_qjdc_ry to set
	 */
	public void setSh_qjdc_ry(String sh_qjdc_ry)
	{
		this.sh_qjdc_ry = sh_qjdc_ry;
	}
	/**
	 * @return the sh_qjdc_yj
	 */
	public String getSh_qjdc_yj()
	{
		return sh_qjdc_yj;
	}
	/**
	 * @param sh_qjdc_yj the sh_qjdc_yj to set
	 */
	public void setSh_qjdc_yj(String sh_qjdc_yj)
	{
		this.sh_qjdc_yj = sh_qjdc_yj;
	}
	/**
	 * @return the sh_qjdc_rq
	 */
	public Date getSh_qjdc_rq()
	{
		return sh_qjdc_rq;
	}
	/**
	 * @param sh_qjdc_rq the sh_qjdc_rq to set
	 */
	public void setSh_qjdc_rq(Date sh_qjdc_rq)
	{
		this.sh_qjdc_rq = sh_qjdc_rq;
	}
	/**
	 * @return the sh_xmsl_ry
	 */
	public String getSh_xmsl_ry()
	{
		return sh_xmsl_ry;
	}
	/**
	 * @param sh_xmsl_ry the sh_xmsl_ry to set
	 */
	public void setSh_xmsl_ry(String sh_xmsl_ry)
	{
		this.sh_xmsl_ry = sh_xmsl_ry;
	}
	/**
	 * @return the sh_xmsl_yj
	 */
	public String getSh_xmsl_yj()
	{
		return sh_xmsl_yj;
	}
	/**
	 * @param sh_xmsl_yj the sh_xmsl_yj to set
	 */
	public void setSh_xmsl_yj(String sh_xmsl_yj)
	{
		this.sh_xmsl_yj = sh_xmsl_yj;
	}
	/**
	 * @return the sh_xmsl_rq
	 */
	public Date getSh_xmsl_rq()
	{
		return sh_xmsl_rq;
	}
	/**
	 * @param sh_xmsl_rq the sh_xmsl_rq to set
	 */
	public void setSh_xmsl_rq(Date sh_xmsl_rq)
	{
		this.sh_xmsl_rq = sh_xmsl_rq;
	}
	/**
	 * @return the slxm_id
	 */
	public String getSlxm_id()
	{
		return slxm_id;
	}
	/**
	 * @param slxm_id the slxm_id to set
	 */
	public void setSlxm_id(String slxm_id)
	{
		this.slxm_id = slxm_id;
	}
	
	
}
