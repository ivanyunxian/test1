package com.supermap.realestate.registration.model.xmlmodel;

public enum XmlQLLX {
    /** 集体土地所有权 */
    JTTDSYQ("1", "TDSYQ"), //

    /** 国有建设用地使用权 */
    GYJSYDSHYQ("3", "JSYDSYQ"), //

    /** 宅基地使用权 */
    ZJDSYQ("5", "FDCQ1"), //

    /** 国有建设用地使用权/房屋（构筑物）所有权 */
    GYJSYDSHYQ_FWSYQ("4", "FDCQ2"), //

    /** 宅基地使用权/房屋（构筑物）所有权 */
    ZJDSYQ_FWSYQ("6", "FDCQ3"), //

    /** 海域使用权 */
    HYSYQ("15", "HYSYQ"),

    GJZWSYQ("gjzw", "GJZWSYQ"),

    NYDSYQ("nydsyq", "NYDSYQ"),

    /** 林地使用权 */
    LQ("11", "LQ"), //

    /**
     * 其它权利
     */
    QTXGQL("qtxgql", "QTXGQL"), //

    /** 地役权 */
    DYIQ("19", "DYIQ"), //

    /** 抵押权 */
    DYAQ("23", "DYAQ"), //

    /**
     * 异议登记
     */
    YYDJ("600", "YYDJ"), //
    /**
     * 预告登记
     */
    YGDJ("700", "YGDJ"), //
    /**
     * 查封登记
     */
    CFDJ("800", "CFDJ");//

    // 定义私有变量
    private String value;
    private String EntityName;

    // 构造函数，枚举类型只能为私有
    private XmlQLLX(String value, String name) {
	this.value = value;
	this.EntityName = name;
    }

    public String getVaule() {
	return this.value;
    }

    public String getTableName() {
	return this.EntityName;
    }

    @Override
    public String toString() {
	return String.valueOf(this.value);
    }

}
