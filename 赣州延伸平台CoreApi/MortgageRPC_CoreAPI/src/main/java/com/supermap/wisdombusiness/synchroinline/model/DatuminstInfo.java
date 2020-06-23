package com.supermap.wisdombusiness.synchroinline.model;

public class DatuminstInfo
{
	protected String id;
	protected String name;
	protected String code;
	/**
	 * 资料总分数
	 */
	protected int count;
	/**
	 * 当前总份数
	 */
	protected int cur_count;
	protected String proinst_id;
	protected String bz;
	protected String required;

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * @return the count
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count)
	{
		this.count = count;
	}

	/**
	 * @return the cur_count
	 */
	public int getCur_count()
	{
		return cur_count;
	}

	/**
	 * @param cur_count
	 *            the cur_count to set
	 */
	public void setCur_count(int cur_count)
	{
		this.cur_count = cur_count;
	}

	/**
	 * @return the proinst_id
	 */
	public String getProinst_id()
	{
		return proinst_id;
	}

	/**
	 * @param proinst_id
	 *            the proinst_id to set
	 */
	public void setProinst_id(String proinst_id)
	{
		this.proinst_id = proinst_id;
	}

	/**
	 * @return the bz
	 */
	public String getBz()
	{
		return bz;
	}

	/**
	 * @param bz
	 *            the bz to set
	 */
	public void setBz(String bz)
	{
		this.bz = bz;
	}

	/**
	 * @return the required
	 */
	public String getRequired()
	{
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(String required)
	{
		this.required = required;
	}

}
