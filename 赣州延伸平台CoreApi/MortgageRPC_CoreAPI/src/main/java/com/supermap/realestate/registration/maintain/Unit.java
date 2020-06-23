package com.supermap.realestate.registration.maintain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单元信息的完整对象，包括权利，权利人。
 * @ClassName: Land
 * @author liushufeng
 * @date 2016年7月13日 下午3:08:27
 */
public class Unit {

	/**
	 * 构造函数
	 */
	public Unit() {

	}

	/**
	 * 操作类型
	 */
	private String OPERATE;

	/**
	 * 单元基本信息
	 */
	public Value baseinfo = new Value();
	
	/**
	 * 所有权/使用权
	 */
	public List<RightClass> rights = new ArrayList<RightClass>();

	/**
	 * 抵押
	 */
	public List<RightClass> mortgages = new ArrayList<Unit.RightClass>();

	/**
	 * 查封
	 */
	public List<RightClass> seals = new ArrayList<Unit.RightClass>();

	/**
	 * 关联的自然幢ID
	 */
	public List<String> buildingids=new ArrayList<String>();
	
	/**
	 * 其他权利，暂时未定义
	 */
	public List<RightClass> others = new ArrayList<Unit.RightClass>();

	/**
	 * 子单元
	 */
	public List<Unit> units=new ArrayList<Unit>();
	
	/**
	 * @return oPERATE
	 */
	public String getOPERATE() {
		return OPERATE;
	}

	/**
	 * @param oPERATE
	 *            要设置的 oPERATE
	 */
	public void setOPERATE(String oPERATE) {
		OPERATE = oPERATE;
	}

	/**
	 * @return baseinfo
	 */
	public Value getBaseinfo() {
		return baseinfo;
	}

	/**
	 * @param baseinfo
	 *            要设置的 baseinfo
	 */
	public void setBaseinfo(Value baseinfo) {
		this.baseinfo = baseinfo;
	}
	
	/**
	 * 值对象，包含了oldvalue和newvalue，都是 Map<String, Object>对象
	 * @ClassName: Value
	 * @author liushufeng
	 * @date 2016年7月13日 下午10:30:52
	 */
	public static class Value {

		public Value() {

		}

		public ZDValue oldvalue;

		public ZDValue newvalue;

	}

	/**
	 * 宗地信息
	 */
	public static class ZDValue extends HashMap<String, Object> {

		private static final long serialVersionUID = 1540323927754461946L;

		public ZDValue() {

		}

		public ZDValue(Map<String, Object> os) {
			super(os);
			this.put("tdyts", this.tdyts);
		}

		private List<Map<String, Object>> tdyts = new ArrayList<Map<String, Object>>();

		/**
		 * @return tdyts
		 */
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getTdyts() {
			return (List<Map<String, Object>>) this.get("tdyts");
		}

		/**
		 * @param tdyts
		 *            要设置的 tdyts
		 */
		public void setTdyts(List<Map<String, Object>> tdyts) {
			this.tdyts = tdyts;
		}

	}

	/**
	 * 权利的上级节点，里边包含了权利的旧值oldvalue和新值newvalue
	 * @ClassName: RightClass
	 * @author liushufeng
	 * @date 2016年7月13日 下午10:28:28
	 */
	public static class RightClass {

		public RightClass() {

		}

		public Right newvalue;
		public Right oldvalue;
	}

	/**
	 * 权利对象，继承自HashMap<String, Object>，里边包含了权利人数组holders
	 * @ClassName: Right
	 * @author liushufeng
	 * @date 2016年7月13日 下午10:29:12
	 */
	public static class Right extends HashMap<String, Object> implements Serializable {
		public Right() {

		}

		private static final long serialVersionUID = 1L;

		public Right(Map<String, Object> os) {
			super(os);
			this.put("holders", this.holders);
			this.put("fsql", this.fsql);
			this.put("certs", this.certs);
		}

		private List<Holder> holders = new ArrayList<Unit.Holder>();

		/**
		 * @return holders
		 */
		@SuppressWarnings("unchecked")
		public List<Holder> getHolders() {
			return (List<Holder>) super.get("holders");
		}

		/**
		 * @param holders
		 *            要设置的 holders
		 */

		public void setHolders(List<Holder> holders) {
			this.put("holders", holders);
		}

		private Map<String, Object> fsql = new HashMap<String, Object>();

		/**
		 * @return fsql
		 */
		@SuppressWarnings("unchecked")
		public Map<String, Object> getFsql() {
			return (Map<String, Object>) this.get("fsql");
		}

		/**
		 * @param fsql
		 *            要设置的 fsql
		 */
		public void setFsql(Map<String, Object> fsql) {
			this.put("fsql", fsql);
		}

		private List<Cert> certs = new ArrayList<Unit.Cert>();

		/**
		 * @return holders
		 */
		@SuppressWarnings("unchecked")
		public List<Cert> getCerts() {
			return (List<Cert>) super.get("certs");
		}

		/**
		 * @param holders
		 *            要设置的 holders
		 */

		public void setCerts(List<Cert> certs) {
			this.certs = certs;
		}
	}

	/**
	 * 权利人对象，继承自HashMap<String, Object>
	 * @ClassName: Holder
	 * @author liushufeng
	 * @date 2016年7月13日 下午10:29:53
	 */
	public static class Holder extends LinkedHashMap<String, Object> implements Serializable {

		private static final long serialVersionUID = 1L;

		public Holder() {

		}

		public Holder(Map<String, Object> os) {
			super(os);
		}
	}

	/**
	 * 证书对象，继承自HashMap<String, Object>
	 * @ClassName: Cert
	 * @author yuxuebin
	 * @date 2016年7月14日 21:18:53
	 */
	public static class Cert extends HashMap<String, Object> implements Serializable {

		private static final long serialVersionUID = 1L;

		public Cert() {

		}

		public Cert(Map<String, Object> os) {
			super(os);
		}
	}
}
