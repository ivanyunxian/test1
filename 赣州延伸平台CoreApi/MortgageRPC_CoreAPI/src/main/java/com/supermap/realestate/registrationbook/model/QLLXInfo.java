package com.supermap.realestate.registrationbook.model;

/** 已不用 可删除
 * @author WUZHU ,(该类已经不用) 权利属性类，用来存储每个不动产单元号的一个权利类型包含的信息：该权利类型，该权利类型名称，该类型 有多少数量以及对应的权利ID
 * 主要目的是用来判断 权利类型登记信息是否分页，确定索引页中权利类型的页数，和获取该节点对应的页面
 */
public class QLLXInfo {
	
    //包含的权力ID 格式是1，2，3
    private String qlids;
  
    //该权利类型名称
    private String qllxmc;
  
    //该权利类型数量
    private double count_qllx;
  
    //权利类型
    private String qllx;
  
	public String getQlids() {
		return qlids;
	}
	
	public void setQlids(String qlids) {
		this.qlids = qlids;
	}
	
	public String getQllxmc() {
		return qllxmc;
	}
	
	public void setQllxmc(String qllxmc) {
		this.qllxmc = qllxmc;
	}
	
	public double getCount_qllx() {
		return count_qllx;
	}
	
	public void setCount_qllx(double count_qllx) {
		this.count_qllx = count_qllx;
	}
	
	public String getQllx() {
		return qllx;
	}
	
	public void setQllx(String qllx) {
		this.qllx = qllx;
	}
	
}
