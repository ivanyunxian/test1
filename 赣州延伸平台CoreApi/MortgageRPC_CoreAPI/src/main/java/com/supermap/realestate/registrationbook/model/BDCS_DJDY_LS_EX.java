package com.supermap.realestate.registrationbook.model;

import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author wuzhu 扩展 BDCS_DJDY_LS 类 ，以历史层登记单元表某条数据为入口，封装了登记薄树需要的信息。
 */
public class BDCS_DJDY_LS_EX   {
	private RealUnit unit;
	private RealUnit glunit; //期限房关联时的关联的那个单元（unit为期房时，glunit为现房；unit为现房时，glunit为期房）
	private BDCS_DJDY_LS djdy;
	private List<BDCS_QL_LS> qls;// 一个登记单元对应的权利集
	private List<BDCS_QL_LS> glqls;// 一个登记单元对应的权利集
	private Map<String, Integer> lx_groupBy = new HashMap<String, Integer>();// 一个登记单元同种权利类型、登记类型的个数。
	private Map<String, Integer> gllx_groupBy = new HashMap<String, Integer>();// 期限房关联时的关联的那个单元:一个登记单元同种权利类型、登记类型的个数。

	// 初始化该类
	public void Init(CommonDao dao,String bdcdyid,BDCDYLX bdcdylx) {
		 RealUnit u = null, glUnit = null;
		 String glbdcdyid = null;
		 if(bdcdylx!=null && !StringUtils.isEmpty(bdcdyid)){
		    u=UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcdyid);//初始化不动产单元信息
		    if ("032".equals(bdcdylx.Value)) {//期房
		    	String fulSql = MessageFormat
						.format("from BDCK.YC_SC_H_XZ WHERE YCBDCDYID=''{0}'' ",
								bdcdyid);
				List<Map> list = dao.getDataListByFullSql("SELECT * "+fulSql);
				if(list!=null && list.size()>0){
					glbdcdyid = list.get(0).get("SCBDCDYID").toString();
					glUnit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.LS, glbdcdyid);//初始化不动产单元信息
				}
			}else if ("031".equals(bdcdylx.Value)) {//期房
		    	String fulSql = MessageFormat
						.format("from BDCK.YC_SC_H_XZ WHERE SCBDCDYID=''{0}'' ",
								bdcdyid);
				List<Map> list = dao.getDataListByFullSql("SELECT * "+fulSql);
				if(list!=null && list.size()>0){
					glbdcdyid = list.get(0).get("YCBDCDYID").toString();
					glUnit = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.LS, glbdcdyid);//初始化不动产单元信息
				}
			}
		 }
		 this.setUnit(u);
		 this.setGlunit(glUnit);
		 getLxgroupBy(dao,bdcdyid,"lx");
		 getLxgroupBy(dao,glbdcdyid,"gllx");
	}

	private void getLxgroupBy(CommonDao dao,String bdcdyid, String flag) {
		BDCS_DJDY_LS _djdy=null;
		List<BDCS_DJDY_LS> djdys = dao.getDataList(BDCS_DJDY_LS.class, " BDCDYID='"+bdcdyid+"'");//初始化登记单元信息
		  if(djdys!=null&&djdys.size()>0)
		  {
			   _djdy=djdys.get(0);
		  }
		  this.setDjdy(_djdy);
		  if(_djdy==null)
			  return ;
		List<BDCS_QL_LS> ql_lss = dao.getDataList(BDCS_QL_LS.class, " DJDYID='"
				+ _djdy.getDJDYID() + "'");
		if ("lx".equals(flag)) {
			this.setQls(ql_lss);
		}else if ("gllx".equals(flag)) {
			this.setGlqls(ql_lss);
		}		
		
		Map<String, Integer> lxgroup = new HashMap<String, Integer>();
		for (BDCS_QL_LS ql : ql_lss) {
			if (!StringUtils.isEmpty(ql.getDJLX())) {
				if (lxgroup.containsKey(ql.getDJLX())) {
					int index_djlx = lxgroup.get(ql.getDJLX())
							.intValue() + 1;
					lxgroup.put(ql.getDJLX(), index_djlx);
				} else
					lxgroup.put(ql.getDJLX(), 1);
			}
			if (!StringUtils.isEmpty(ql.getQLLX())
					&&!StringUtils.isEmpty(ql.getDJLX())
					&& !ql.getDJLX().equals(ConstValue.DJLX.YGDJ.Value)//权利类型中不包含 预告登记信息、异议登记信息、查封登记信息
					&& !ql.getDJLX().equals(ConstValue.DJLX.YYDJ.Value)
					&& !ql.getDJLX().equals(ConstValue.DJLX.CFDJ.Value)) {
				if (lxgroup.containsKey(ql.getQLLX())) {
					int index_qllx = lxgroup.get(ql.getQLLX())
							.intValue() + 1;
					lxgroup.put(ql.getQLLX(), index_qllx);
				} else
					lxgroup.put(ql.getQLLX(), 1);
			}
		}
		if ("lx".equals(flag)) {
			this.lx_groupBy = lxgroup;
		}else if ("gllx".equals(flag)) {
			this.gllx_groupBy = lxgroup;
		}		
	}
	// 登记薄索引菜单名枚举
	// 规则：menuName：菜单名字
	// code 可以是权利类型、登记类型，属于此类型的就显示该菜单。比如取水权、探矿权为：20,21。-1表示不显示
	// bdcdylx 表示该菜单只有属于该不动产单元类型集合时候才显示。空值表示不进行该项判断。
	// url 该菜单的连接。想对路径是BookController
	// printTemplate 打印模板名
	public enum DJBMenu {
		/**
		 * 封面 -1不参与遍历
		 */
		FM("封面", "-1", "", "bdcdjbfm", "bdcdjbfm"),
		/**
		 * 宗地基本信息
		 */
		ZDJBXX("宗地基本信息", "-1", "", "dbzdjbxx", "dbzdjbxx"),
		/**
		 * 宗海基本信息
		 */
		ZHJBXX("宗海基本信息", "-1", "", "dbzhjbxx", "dbzhjbxx"),
		/**
		 * 不动产权利登记目录
		 */
		BDCQLDJML("不动产权利登记目录", "-1", "", "dbbdcqldjml", "dbbdcqldjml"),
		/**
		 * 登记信息索引页
		 */
		DJSY("登记信息索引页", "-1", "", "dbbdcqjqtsx", "dbbdcqjqtsx"),
		/**
		 * 土地所有权登记信息
		 */
		TDSYQDJXX("土地所有权登记信息", "1,2", "", "dbtdsyqdjxx", "dbtdsyqdjxx"),
		/**
		 * 建设用地使用权、宅基地使用权登记信息
		 */
		JSYDZJDSYQ("建设用地使用权、宅基地使用权登记信息", "3,5,7", "", "dbjsydsyqxx",
				"dbjsydsyqxx"),
		/**
		 * 房地产权登记信息（项目内多幢房屋）
		 */
		FDCQDJXX_DZ("房地产权登记信息（项目内多幢房屋）", "-1", "", "dbfdcqdjxxdz", ""),
		/**
		 * 房地产权登记信息（独幢、层、套、间房屋）
		 */
		FDCQDJXX_DL("房地产权登记信息（独幢、层、套、间房屋）", "4,6,8", "031", "dbfdcqdjxxdz",
				"dbfdcqdjxxdz"),
		/**
		 * 建筑物区分所有权业主共有部分登记信息, 暂时用不到用 编码用A
		 */
		JCWSYQDJXX("建筑物区分所有权业主共有部分登记信息", "-1", "", "#", ""),
		/**
		 * 海域（含无居民海岛）使用权登记信息
		 */
		HYSYQDJXX("海域（含无居民海岛）使用权登记信息", "15,16,17,18", "", "dbhydjxx",
				"dbhydjxx"),
		/**
		 * 构（建）筑物所有权登记信息
		 */
		GZSYQDJXX("构（建）筑物所有权登记信息", "6", "", "dbgzwsyqdjxx", "dbgzwsyqdjxx"),
		/**
		 * 土地承包经营权、农用地的其他使用权登记信息（非林地）
		 */
		TDCBQDJXX("土地承包经营权、农用地的其他使用权登记信息（非林地）", "9", "", "#", ""),
		/**
		 * 林权登记信息
		 */
		LQDJXX("林权登记信息", "10,11,12,36", "", "dblqdjxx", "dblqdjxx"),
		/**
		 * 国有农用地使用权登记信息
		 */
		NYDDJXX("国有农用地使用权登记信息", "24", "", "dbnyddjxx", "dbnyddjxx"),
		/**
		 * 其他相关权利登记信息（取水权、探矿权、采矿权等）
		 */
		QTXGQLDJXX("其他相关权利登记信息（取水权、探矿权、采矿权等）", "20,21,22", "", "#", ""),
		/**
		 * 地役权登记信息
		 */
		DYQDJXX("地役权登记信息", "19", "", "#", ""),
		/**
		 * 抵押权登记信息
		 */
		DHYQDJXX("抵押权登记信息", "23", "", "dbdyqdjxx", "dbdyqdjxx"),
		/**
		 * 预告登记信息
		 */
		YGDJXX("预告登记信息", "700", "", "dbygdjxx", "dbygdjxx"),
		/**
		 * 异议登记信息
		 */
		YYDJXX("异议登记信息", "600", "", "dbyydjxx", "dbyydjxx"),
		/**
		 * 查封登记信息
		 */
		CFDJXX("查封登记信息", "800", "", "dbcfdjxx", "dbcfdjxx");
		// 成员变量
		private String menuName;
		private String url;
		private String code;// 包含用到该枚举项的编码。比如抵押权、查封登记为：23,800。该编码为表A.8
							// 权利类型字典表-QLLX 或登记类型
		private String bdcdylx;
		private String printTemplate;

		// 构造方法
		private DJBMenu(String menuName, String code, String bdcdylx,
				String url, String printTemplate) {
			this.setMenuName(menuName);
			this.setCode(code);
			this.setUrl(url);
			this.setBdcdylx(bdcdylx);
			this.setPrintTemplate(printTemplate);
		}

		public String getMenuName() {
			return menuName;
		}

		public void setMenuName(String menuName) {
			this.menuName = menuName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getBdcdylx() {
			return bdcdylx;
		}

		public void setBdcdylx(String bdcdylx) {
			this.bdcdylx = bdcdylx;
		}

		public String getPrintTemplate() {
			return printTemplate;
		}

		public void setPrintTemplate(String printTemplate) {
			this.printTemplate = printTemplate;
		}

	}

	// 该登记单元菜单集合
	public List<BookMenu> getMenus() {
		List<BookMenu> menus = new ArrayList<BookMenu>();
		BookMenu node_index = new BookMenu();// 索引页节点
		Map<String, String> attr_index = new HashMap<String, String>();// 索引页节点属性
		node_index.setText(DJBMenu.DJSY.menuName);
		attr_index.put("url", DJBMenu.DJSY.url);// 访问页面
		attr_index.put("bdcdyh", this.getUnit() == null ? "" : this.getUnit().getBDCDYH());// 不动产单元号
		attr_index.put("tpl", DJBMenu.DJSY.getPrintTemplate());// 索引页模板
		//增加打印时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		attr_index.put("printDate", sdf.format(new Date()));// 索引页模板
		node_index.setAttributes(attr_index);
		node_index.setId(java.util.UUID.randomUUID().toString());
		menus.add(node_index);// 添加索引页面
		int yh = 0;// 页号数
		int flag = 0;// 用于确保X 权页只赋值一次的标识。
		for (DJBMenu m : DJBMenu.values()) {
			int nodeCount = getMenuCount(m);// 获取该菜单节点的个数,如无该菜单则为0
			int glnodeCount = getMenuCount_gl(m);
			for (int j = 0; j < glnodeCount; j++) {
				BookMenu node = new BookMenu();
				Map<String, String> attr = new HashMap<String, String>();// 节点属性

				attr.put("url", m.url);// 访问页面
				attr.put("page", String.valueOf(j + 1));// 分页
				attr.put("bdcdyid", this.getGlunit().getId());// 不动产单元ID
				attr.put("bdcdyh", this.getGlunit().getBDCDYH());// 不动产单元号
//				attr.put("djdyid", this.getDJDYID());// 登记单元ID
//				attr.put("xmbh", this.getXMBH());// 项目编号
				attr.put("bdcdylx", this.getGlunit().getBDCDYLX().Value);// 不动产单元类型
				attr.put("qllxORdjlx", m.code);// 权利类型或登记类型
				attr.put("tpl", m.printTemplate);// 打印模板

				yh += 1;// 页号数加1
				//给每页添加页码
				attr.put("yh",Integer.toString(yh));
				
				if (nodeCount > 1)// 如果节点数多于1 命名后加编号
					node.setText("("+ this.getGlunit().getBDCDYLX().Name+")" + m.menuName + (j + 1));
				else
					node.setText("("+ this.getGlunit().getBDCDYLX().Name+")" + m.menuName);
				node.setId(java.util.UUID.randomUUID().toString());
				node.setAttributes(attr);
				menus.add(node);
			}
			for (int j = 0; j < nodeCount; j++) {
				BookMenu node = new BookMenu();
				Map<String, String> attr = new HashMap<String, String>();// 节点属性

				attr.put("url", m.url);// 访问页面
				attr.put("page", String.valueOf(j + 1));// 分页
				attr.put("bdcdyid", this.getUnit() == null ? "" : this.getUnit().getId());// 不动产单元ID
				attr.put("bdcdyh", this.getUnit() == null ? "" : this.getUnit().getBDCDYH());// 不动产单元号
//				attr.put("djdyid", this.getDJDYID());// 登记单元ID
//				attr.put("xmbh", this.getXMBH());// 项目编号
				attr.put("bdcdylx", this.getUnit() == null ? "" : this.getUnit().getBDCDYLX().Value);// 不动产单元类型
				attr.put("qllxORdjlx", m.code);// 权利类型或登记类型
				attr.put("tpl", m.printTemplate);// 打印模板

				yh += 1;// 页号数加1
				//给每页添加页码
				attr.put("yh",Integer.toString(yh));
				
				if (nodeCount > 1)// 如果节点数多于1 命名后加编号
					node.setText(m.menuName + (j + 1));
				else
					node.setText(m.menuName);
				node.setId(java.util.UUID.randomUUID().toString());
				node.setAttributes(attr);
				menus.add(node);
			}
			
			if (nodeCount > 0 || glnodeCount > 0) {// 插入节点时候计算页码
				String _yh="";
				int beginPage = yh - nodeCount - glnodeCount + 1;
				int endPage = yh;
				if (endPage - beginPage == 0 ) {
					 _yh = Integer.toString(endPage);
				}else if(endPage - beginPage <= 2){
					 _yh = Integer.toString(beginPage);
					for (int i = 1; i < (endPage - beginPage); i++) {
						_yh += "、" + Integer.toString(beginPage + i)  ;						
					}
					_yh += "、" + Integer.toString(endPage);
				}else if(endPage - beginPage > 2){
					 _yh = beginPage > 0 ? Integer.toString(beginPage)+"-"+Integer.toString(endPage):Integer.toString(endPage);
				}
				attr_index.put(m.name() + "_YH", _yh);// 给索引页加入该菜单节点页号属性
				if (flag == 0 && m != DJBMenu.DHYQDJXX && m != DJBMenu.DYQDJXX
						&& m != DJBMenu.YGDJXX && m != DJBMenu.YYDJXX
						&& m != DJBMenu.CFDJXX) {
					attr_index.put("X_YH", _yh);// 加入X权利页码号属性
					String _menuname = m.menuName.substring(0,
							m.menuName.lastIndexOf("权"));
					attr_index.put("X_NAME", _menuname);
					flag = 1;// 确保只赋值一次
				}
			}
		}
		return menus;
	}

	// 获取该菜单的个数，如无该菜单返回0。每个菜单只显示同一类型，如果该类型数量超过4条，则需新增一条该类型菜单显示剩余的
	private int getMenuCount(DJBMenu m) {
		if (!StringUtils.isEmpty(m.bdcdylx)) {
			if (this.getUnit() == null) {
				return 0;
			}
			if (m.bdcdylx.indexOf(this.getUnit().getBDCDYLX().Value) == -1)
				return 0;
		}
		if (StringUtils.isEmpty(m.code))
			return 0;
		if (m.code.equals("-1"))// 类型为-1的不参与遍历this.getBDCDYLX()
			return 0;
		if (this.lx_groupBy.size() == 0)
			return 0;
		double resultCount = 0;
		String[] cs = m.code.split("\\,");
		for (Entry<String, Integer> entry : this.lx_groupBy.entrySet()) {
			for (String c : cs) {
				if (entry.getKey().equals(c))// 如枚举项中包含该类型，将该类型数量相加。
					resultCount += entry.getValue();
			}
		}
		return (int) Math.ceil(resultCount / (double) 4);
	}

	// 获取该菜单的个数，如无该菜单返回0。每个菜单只显示同一类型，如果该类型数量超过4条，则需新增一条该类型菜单显示剩余的
	private int getMenuCount_gl(DJBMenu m) {
		if (this.getGlunit()==null) {
			return 0;
		}
		if (!StringUtils.isEmpty(m.bdcdylx)) {
			if (m.bdcdylx.indexOf(this.getGlunit().getBDCDYLX().Value) == -1)
				return 0;
		}
		if (StringUtils.isEmpty(m.code))
			return 0;
		if (m.code.equals("-1"))// 类型为-1的不参与遍历this.getBDCDYLX()
			return 0;
		if (this.gllx_groupBy.size() == 0)
			return 0;
		double resultCount = 0;
		String[] cs = m.code.split("\\,");
		for (Entry<String, Integer> entry : this.gllx_groupBy.entrySet()) {
			for (String c : cs) {
				if (entry.getKey().equals(c))// 如枚举项中包含该类型，将该类型数量相加。
					resultCount += entry.getValue();
			}
		}
		return (int) Math.ceil(resultCount / (double) 4);
	}
		
	public List<BDCS_QL_LS> getQls() {
		return qls;
	}

	public void setQls(List<BDCS_QL_LS> qls) {
		this.qls = qls;
	}
	// （旧方法、不用）获取该菜单的个数，每个菜单只显示同一类型，如果该类型数量超过4条，则需新增一条该类型菜单显示剩余的
	// private int getMenuCount2(DJBMenu m) {
	// if(!StringUtils.isEmpty(m.bdcdylx)) {
	// if(m.bdcdylx.indexOf(this.getBDCDYLX()) == -1)
	// return 0;
	// }
	// if(StringUtils.isEmpty(m.code))
	// return 0;
	// if(m.code.equals("-1"))// 类型为-1的不参与遍历this.getBDCDYLX()
	// return 0;
	// if (StringUtils.isEmpty(this.qllxs_count) ||
	// StringUtils.isEmpty(this.qllxs)|| StringUtils.isEmpty(this.qlids))
	// return 0;
	// double resultCount = 0;
	// Map<String, QLLXInfo> qllxinfo = new TreeMap<String, QLLXInfo>();//
	// 用于保存权利类型信息
	// String[] array_qllxs_count = this.qllxs_count.split("\\#");
	// String[] array_qllxs = this.qllxs.split("\\#");
	// String[] array_qlids = this.qlids.split("\\#");
	// String[] cs=m.code.split("\\,");
	// for (int i = 0; i < array_qllxs.length; i++) {
	// QLLXInfo info_tmp = new QLLXInfo();
	// info_tmp.setCount_qllx(Double.parseDouble(array_qllxs_count[i]));
	// info_tmp.setQlids(array_qlids[i]);
	// info_tmp.setQllx(array_qllxs[i]);
	// qllxinfo.put(array_qllxs[i], info_tmp);
	// for(String c:cs){
	// if(array_qllxs[i].equals(c))//如枚举项中包含该类型，将该类型数量相加。
	// resultCount += info_tmp.getCount_qllx();
	// }
	// }
	// return (int) Math.ceil(resultCount / (double) 4);
	// }

	public RealUnit getUnit() {
		return unit;
	}

	public void setUnit(RealUnit unit) {
		this.unit = unit;
	}

	public RealUnit getGlunit() {
		return glunit;
	}

	public void setGlunit(RealUnit glunit) {
		this.glunit = glunit;
	}

	public Map<String, Integer> getGllx_groupBy() {
		return gllx_groupBy;
	}

	public void setGllx_groupBy(Map<String, Integer> gllx_groupBy) {
		this.gllx_groupBy = gllx_groupBy;
	}

	public BDCS_DJDY_LS getDjdy() {
		return djdy;
	}

	public void setDjdy(BDCS_DJDY_LS djdy) {
		this.djdy = djdy;
	}

	public List<BDCS_QL_LS> getGlqls() {
		return glqls;
	}

	public void setGlqls(List<BDCS_QL_LS> glqls) {
		this.glqls = glqls;
	}

	// /** 权利类型数量集合拼成的字符串。格式 1#2#3 */
	// private String qllxs_count;
	//
	// /** 权利类型集合拼成的字符串。格式 1#2#3 */
	// private String qllxs;
	//
	// /** 权利ID集合拼成的字符串。格式 1,2,3#4,5,6#7,8,9 */
	// private String qlids;
	// public String getQllxs_count() {
	// return qllxs_count;
	// }
	//
	// public void setQllxs_count(String qllxs_count) {
	// this.qllxs_count = qllxs_count;
	// }
	//
	// public String getQllxs() {
	// return qllxs;
	// }
	//
	// public void setQllxs(String qllxs) {
	// this.qllxs = qllxs;
	// }
	//
	// public String getQlids() {
	// return qlids;
	// }
	//
	// public void setQlids(String qlids) {
	// this.qlids = qlids;
	// }

}
