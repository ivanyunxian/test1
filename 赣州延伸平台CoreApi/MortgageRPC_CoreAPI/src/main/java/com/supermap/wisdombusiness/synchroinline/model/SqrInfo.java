package com.supermap.wisdombusiness.synchroinline.model;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.util.ConstValue;

public class SqrInfo
{
	private String sqr_name;
	private String sqr_tel;
	private String sqr_adress;
	private String sqr_zjlx;
	private String sqr_zjh;
	private String sqr_qt_fr_name;
	private String sqr_qt_dlr_name;
	private String sqr_qt_tel;
	private String sqr_qt_adress;
	private String sqr_qt_zjlx;
	private String sqr_qt_zjh;
	private String sqr_gyfs;
	private String sqr_gyfe;
	private int sqr_lx;
	private int sqr_sxh;
	
	//格式化之后的信息
	private String sqr_zjlx_format;
	private String sqr_qt_zjlx_format;
	private String sqr_gyfs_format;
	
	/**
	 * @return the sqr_name
	 */
	public String getSqr_name()
	{
		return sqr_name;
	}
	/**
	 * @param sqr_name the sqr_name to set
	 */
	public void setSqr_name(String sqr_name)
	{
		this.sqr_name = sqr_name;
	}
	/**
	 * @return the sqr_tel
	 */
	public String getSqr_tel()
	{
		return sqr_tel;
	}
	/**
	 * @param sqr_tel the sqr_tel to set
	 */
	public void setSqr_tel(String sqr_tel)
	{
		this.sqr_tel = sqr_tel;
	}
	/**
	 * @return the sqr_adress
	 */
	public String getSqr_adress()
	{
		return sqr_adress;
	}
	/**
	 * @param sqr_adress the sqr_adress to set
	 */
	public void setSqr_adress(String sqr_adress)
	{
		this.sqr_adress = sqr_adress;
	}
	/**
	 * @return the sqr_zjlx
	 */
	public String getSqr_zjlx()
	{
		return sqr_zjlx;
	}
	/**
	 * @param sqr_zjlx the sqr_zjlx to set
	 */
	public void setSqr_zjlx(String sqr_zjlx)
	{
		this.sqr_zjlx = sqr_zjlx;
	}
	/**
	 * @return the sqr_zjh
	 */
	public String getSqr_zjh()
	{
		return sqr_zjh;
	}
	/**
	 * @param sqr_zjh the sqr_zjh to set
	 */
	public void setSqr_zjh(String sqr_zjh)
	{
		this.sqr_zjh = sqr_zjh;
	}
	/**
	 * @return the sqr_qt_fr_name
	 */
	public String getSqr_qt_fr_name()
	{
		return sqr_qt_fr_name;
	}
	/**
	 * @param sqr_qt_fr_name the sqr_qt_fr_name to set
	 */
	public void setSqr_qt_fr_name(String sqr_qt_fr_name)
	{
		this.sqr_qt_fr_name = sqr_qt_fr_name;
	}
	/**
	 * @return the sqr_qt_dlr_name
	 */
	public String getSqr_qt_dlr_name()
	{
		return sqr_qt_dlr_name;
	}
	/**
	 * @param sqr_qt_dlr_name the sqr_qt_dlr_name to set
	 */
	public void setSqr_qt_dlr_name(String sqr_qt_dlr_name)
	{
		this.sqr_qt_dlr_name = sqr_qt_dlr_name;
	}
	/**
	 * @return the sqr_qt_tel
	 */
	public String getSqr_qt_tel()
	{
		return sqr_qt_tel;
	}
	/**
	 * @param sqr_qt_tel the sqr_qt_tel to set
	 */
	public void setSqr_qt_tel(String sqr_qt_tel)
	{
		this.sqr_qt_tel = sqr_qt_tel;
	}
	/**
	 * @return the sqr_qt_adress
	 */
	public String getSqr_qt_adress()
	{
		return sqr_qt_adress;
	}
	/**
	 * @param sqr_qt_adress the sqr_qt_adress to set
	 */
	public void setSqr_qt_adress(String sqr_qt_adress)
	{
		this.sqr_qt_adress = sqr_qt_adress;
	}
	/**
	 * @return the sqr_qt_zjlx
	 */
	public String getSqr_qt_zjlx()
	{
		return sqr_qt_zjlx;
	}
	/**
	 * @param sqr_qt_zjlx the sqr_qt_zjlx to set
	 */
	public void setSqr_qt_zjlx(String sqr_qt_zjlx)
	{
		this.sqr_qt_zjlx = sqr_qt_zjlx;
	}
	/**
	 * @return the sqr_qt_zjh
	 */
	public String getSqr_qt_zjh()
	{
		return sqr_qt_zjh;
	}
	/**
	 * @param sqr_qt_zjh the sqr_qt_zjh to set
	 */
	public void setSqr_qt_zjh(String sqr_qt_zjh)
	{
		this.sqr_qt_zjh = sqr_qt_zjh;
	}
	/**
	 * @return the sqr_gyfs
	 */
	public String getSqr_gyfs()
	{
		return sqr_gyfs;
	}
	/**
	 * @param sqr_gyfs the sqr_gyfs to set
	 */
	public void setSqr_gyfs(String sqr_gyfs)
	{
		this.sqr_gyfs = sqr_gyfs;
	}
	/**
	 * @return the sqr_lx
	 */
	public int getSqr_lx()
	{
		return sqr_lx;
	}
	/**
	 * @param sqr_lx the sqr_lx to set
	 */
	public void setSqr_lx(int sqr_lx)
	{
		this.sqr_lx = sqr_lx;
	}
	
	
	
	/**
	 * @return the sqr_sxh
	 */
	public int getSqr_sxh()
	{
		return sqr_sxh;
	}
	/**
	 * @param sqr_sxh the sqr_sxh to set
	 */
	public void setSqr_sxh(int sqr_sxh)
	{
		this.sqr_sxh = sqr_sxh;
	}
	
	
	
	/**
	 * @return 格式化之后的申请人证件类型
	 */
	public String getSqr_zjlx_format()
	{
		return this.sqr_zjlx_format;
	}
	/**
	 * @param sqr_zjlx_format the sqr_zjlx_format to set
	 */
	public void setSqr_zjlx_format(String sqr_zjlx_format)
	{
		this.sqr_zjlx_format = sqr_zjlx_format;
	}
	/**
	 * @return the sqr_qt_zjlx_format
	 */
	public String getSqr_qt_zjlx_format()
	{
		return this.sqr_qt_zjlx_format;
	}
	/**
	 * @param sqr_qt_zjlx_format the sqr_qt_zjlx_format to set
	 */
	public void setSqr_qt_zjlx_format(String sqr_qt_zjlx_format)
	{
		this.sqr_qt_zjlx_format = sqr_qt_zjlx_format;
	}
	/**
	 * @return 格式化之后的共有方式
	 */
	public String getSqr_gyfs_format()
	{
		return this.sqr_gyfs_format;
	}
	/**
	 * @param sqr_gyfs_format the sqr_gyfs_format to set
	 */
	public void setSqr_gyfs_format(String sqr_gyfs_format)
	{
		this.sqr_gyfs_format = sqr_gyfs_format;
	}
	
	
	
	/**
	 * @return the sqr_gyfe
	 */
	public String getSqr_gyfe()
	{
		return sqr_gyfe;
	}
	/**
	 * @param sqr_gyfe the sqr_gyfe to set
	 */
	public void setSqr_gyfe(String sqr_gyfe)
	{
		this.sqr_gyfe = sqr_gyfe;
	}
	/**
	 * 反序列化
	 * @param json
	 * @return
	 */
	public static SqrInfo fromJson(String json)
	{
		return JSONObject.parseObject(json,SqrInfo.class);
	}
}
