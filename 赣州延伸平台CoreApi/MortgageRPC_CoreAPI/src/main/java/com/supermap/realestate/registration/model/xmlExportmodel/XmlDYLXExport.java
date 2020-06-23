package com.supermap.realestate.registration.model.xmlExportmodel;

public enum XmlDYLXExport {
	/**
	 * 所有权宗地
	 */
	SYQZD("01", "SYQZD", "BDCS_SYQZD_GZ"),

	/**
	 * 使用权宗地
	 */
	SHYQZD("02", "SHYQZD", "BDCS_SHYQZD_GZ"),
	/**
	 * 宗地变化情况
	 */
	ZDBHQK("zdbhqk", "ZDBHQK", "BDCS_ZDBHQK_GZ"),
	/**
	 * 宗海
	 */
	ZH("04", "ZH", "BDCS_ZH_GZ"),
	/**
	 * 用海状况
	 */
	YHZK("yhzk", "YHZK", "BDCS_YHZK_GZ"),
	/**
	 * 用海用地坐标
	 */
	YHYDZB("yhdzb", "YHYDZB", "BDCS_YHYDZB_GZ"),
	/**
	 * 宗海变化情况
	 */
	ZHBHQK("zhbhqk", "ZHBHQK", "BDCS_ZHBHQK_GZ"),
	/**
	 * 自然幢
	 */
	ZRZ("03", "ZRZ", "BDCS_ZRZ_GZ"),
	/**
	 * 预测自然幢
	 */
	ZRZY("08", "ZRZ", "BDCS_ZRZ_GZY"),
	/**
	 * 构筑物
	 */
	GZW("06", "GZW", "BDCS_GZW_GZ"),
	/**
	 * 面状定着物
	 */
	MZDZW("073", "MZDZW", "BDCS_MZDZW_GZ"),
	/**
	 * 线状定着物
	 */
	XZDZW("072", "XZDZW", "BDCS_XZDZW_GZ"),
	/**
	 * 点状定着物
	 */
	DZDZW("071", "DZDZW", "BDCS_DZDZW_GZ"),
	/**
	 * 逻辑幢
	 */
	LJZ("ljz", "LJZ", "BDCS_LJZ_GZ"), C("c", "C", "BDCS_C_GZ"),
	/**
	 * 户
	 */
	H("031", "H", "BDCS_H_GZ"),
	/**
	 * 预测户
	 */
	HY("032", "H", "BDCS_H_GZY");
	public String Value;
	public String Name;
	public String djName;

	private XmlDYLXExport(String value, String name, String djName) {
		this.Value = value;
		this.Name = name;
		this.djName = djName;
	}
}
