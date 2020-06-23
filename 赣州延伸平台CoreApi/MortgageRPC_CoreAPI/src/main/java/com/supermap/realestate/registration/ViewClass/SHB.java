
package com.supermap.realestate.registration.ViewClass;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.service.impl.QueryServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DYBDCLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.services.metadata.iso19139.Date;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

/**
 * 
 * @Description：审核表
 * @author mss
 * @date
 * @Copyright SuperMap
 */

public class SHB {
	
	/**
	 * 流程名称
	 */
	private String prodef_name = "";

	/**
	 * 当前流程节点
	 */
	private String actinstName;
	
	/**
	 * 判断是否是含有ZYDJHandler
	 */
	private boolean flag_isHaveZY =false ; 

	/**判断是否是变更登记**/
	private boolean flag_isHaveBG =false ; 
	
	public String getProdef_name() {
		return prodef_name;
	}

	public void setProdef_name(String prodef_name) {
		this.prodef_name = prodef_name;
	}

	/**
	 * 现房屋坐落
	 */
	private String xfwzl = "";
	/**
	 * 原房屋坐落
	 */
	private String yfwzl = "";
	/**
	 * 不动产权证号
	 */
	private String fwbdcqzh = "";
	
	private String ygzxywr = "";
	
	public String getYgzxywr() {
		return ygzxywr;
	}

	public void setYgzxywr(String ygzxywr) {
		this.ygzxywr = ygzxywr;
	}

	/**
	 * 原房屋不动产权证号
	 */
	private String yfwbdcqzh = "";
	/**
	 * 原房屋不动产权证号列表
	 */
	private List<String> list_yfwbdcqzh = new ArrayList<String>();
	public String getYfwbdcqzh() {
		return yfwbdcqzh;
	}

	public void setYfwbdcqzh(String yfwbdcqzh) {
		this.yfwbdcqzh = yfwbdcqzh;
	}

	public String getXfwbdcqzh() {
		return xfwbdcqzh;
	}

	public void setXfwbdcqzh(String xfwbdcqzh) {
		this.xfwbdcqzh = xfwbdcqzh;
	}

	public String getXfwdybdcqzh() {
		return xfwdybdcqzh;
	}

	public void setXfwdybdcqzh(String xfwdybdcqzh) {
		this.xfwdybdcqzh = xfwdybdcqzh;
	}

	/**
	 * 现房屋不动产权证号
	 */
	private String xfwbdcqzh = "";
	
	/**
	 * 现房屋不动产权证号列表
	 */
	private List<String> list_xfwbdcqzh = new ArrayList<String>();
	public List<String> getList_yfwbdcqzh() {
		return list_yfwbdcqzh;
	}

	public void setList_yfwbdcqzh(List<String> list_yfwbdcqzh) {
		this.list_yfwbdcqzh = list_yfwbdcqzh;
	}

	public List<String> getList_xfwbdcqzh() {
		return list_xfwbdcqzh;
	}

	public void setList_xfwbdcqzh(List<String> list_xfwbdcqzh) {
		this.list_xfwbdcqzh = list_xfwbdcqzh;
	}

	public List<String> getList_xfwdybdcqzh() {
		return list_xfwdybdcqzh;
	}

	public void setList_xfwdybdcqzh(List<String> list_xfwdybdcqzh) {
		this.list_xfwdybdcqzh = list_xfwdybdcqzh;
	}

	/**
	 * 现房屋抵押权权证号
	 */
	private String xfwdybdcqzh = "";
	/**
	 * 现房屋抵押权权证号列表
	 */
	private List<String> list_xfwdybdcqzh = new ArrayList<String>();

	/**
	 * 宗地坐落
	 */
	private String zdzl = "";

	/**
	 * 不动产权证号
	 */
	private String zdbdcqzh = "";
	/**
	 * 项目信息
	 */
	private ProjectInfo xminfo;

	public ProjectInfo getXminfo() {
		return xminfo;
	}

	public void setXminfo(ProjectInfo info) {
		this.xminfo = info;
	}

	public String getZdbdcqzh() {
		return zdbdcqzh;
	}

	public void setZdbdcqzh(String zdbdcqzh) {
		this.zdbdcqzh = zdbdcqzh;
	}

	/**
	 * 权证附记内容
	 */

	private String qzfjnr = "";
	/**
	 * 说明栏内容
	 */

	/*
	 * 注销抵押情况
	 */
	private List<SHDYQ> zxdyqk = null;

	public List<SHDYQ> getZxdyqk() {
		return zxdyqk;
	}

	public void setZxdyqk(List<SHDYQ> zxdyqk) {
		this.zxdyqk = zxdyqk;
	}

	/*
	 * 抵押情况
	 */
	private List<SHDYQ> dyqk = null;

	public List<SHDYQ> getDyqk() {
		return dyqk;
	}

	public void setDyqk(List<SHDYQ> dyqk) {
		this.dyqk = dyqk;
	}

	private List<SHCFQK> cfqk = null;

	public List<SHCFQK> getCfqk() {
		return cfqk;
	}

	public void setCfqk(List<SHCFQK> cfqk) {
		this.cfqk = cfqk;
	}

	private List<SHCFQK> jfqk = null;

	public List<SHCFQK> getJfqk() {
		return jfqk;
	}

	public void setJfqk(List<SHCFQK> jfqk) {
		this.jfqk = jfqk;
	}
	
	private List<SHXZQK> xzqk = null;
	
	private List<SHXZQK> jcxzqk = null;

	public List<SHXZQK> getXzqk() {
		return xzqk;
	}

	public void setXzqk(List<SHXZQK> xzqk) {
		this.xzqk = xzqk;
	}

	public List<SHXZQK> getJcxzqk() {
		return jcxzqk;
	}

	public void setJcxzqk(List<SHXZQK> jcxzqk) {
		this.jcxzqk = jcxzqk;
	}

	public String getActinstName() {
		return actinstName;
	}

	public void setActinstName(String actinstName) {
		this.actinstName = actinstName;
	}

	private String smlnr = "";

	public SHB() {
		this.dao = SuperSpringContext.getContext().getBean(CommonDao.class);
	}

	private CommonDao dao;

	/**
	 * 房屋状况
	 */

	public List<FWZK> fwzks;
	/**
	 * 自然幢状况
	 */

	public List<ZRZZK> zrzzks;

	/**
	 * 土地状况
	 */

	public List<TDZK> tdzks;

	/**
	 * 申请人情况
	 */

	public List<SHSQR> sqrs;
	private String djyy = "";
	private String djyyjzmwj = "";

	public String getDjyy() {
		return djyy;
	}

	public String getDjyyjzmwj() {
		return djyyjzmwj;
	}

	/**
	 * 历史权利列表
	 */
	private List<HistoryRight> historyRights;

	public List<HistoryRight> getHistoryRights() {
		return historyRights;
	}

	public void setHistoryRights(List<HistoryRight> historyRights) {
		this.historyRights = historyRights;
	}

	public class HistoryRight{
		private QueryService queryService;
		/**
		 * 不动产登记单元id
		 */
		private String unitId;

		/**
		 * 不动产阻络
		 */
		private String houseAddress;

		/**
		 * 权利列表
		 */
		private List<Map> rightList;

		HistoryRight(String unitId, String houseAddress){
			this.unitId = unitId;
			this.houseAddress = houseAddress;
			this.queryService = SuperSpringContext.getContext().getBean("queryService", QueryService.class);
			List<List<Map>> rights = this.queryService.GetQLList(this.unitId);
			if (rights != null && rights.size() >0 )
				this.rightList = rights.get(0);

		}

		public String getHouseAddress() {
			return houseAddress;
		}

		public void setHouseAddress(String houseAddress) {
			this.houseAddress = houseAddress;
		}

		public List<Map> getRightList() {
			return rightList;
		}

		public void setRightList(List<Map> rightList) {
			this.rightList = rightList;
		}
	}

	/**
	 * 房屋状况属性定义
	 */

	public class FWZK {
		/**
		 * 现房屋坐落
		 */
		private String xfwzl = "";
		/**
		 * 原房屋坐落
		 */
		private String yfwzl = "";
		/**
		 * 不动产权证号
		 */
		private String fwbdcqzh = "";
		/**
		 * 房号
		 */
		private String fh = "";
		/**
		 * 不动产单元号
		 */
		private String bdcdyh = "";
		/**
		 * 使用期限
		 */
		private String syqx = "";
        /**
         * 单元号
         */
		private String dyh="";
		public String getDyh() {
			return dyh;
		}
        
		public void setDyh(String dyh) {
			this.dyh = dyh;
		}
		/**
		 * 起始层
		 */
		private String qsc="";
		public String getQsc() {
			return qsc;
		}

		public void setQsc(String qsc) {
			this.qsc = qsc;
		}

		
		public String getSyqx() {
			return syqx;
		}

		public void setSyqx(String syqx) {
			this.syqx = syqx;
		}

		public String getBdcdyh() {
			return bdcdyh;
		}

		public void setBdcdyh(String bdcdyh) {
			this.bdcdyh = bdcdyh;
		}

		public String getXfwzl() {
			return xfwzl;
		}

		public String getYfwzl() {
			return yfwzl;
		}

		public String getFwbdcqzh() {
			return fwbdcqzh;
		}

		public String getFh() {
			return fh;
		}

		public String getJzmj() {
			return jzmj;
		}

		public String getZyjzmj() {
			return zyjzmj;
		}

		public String getFtmj() {
			return ftmj;
		}

		public String getZcs() {
			return zcs;
		}

		public String getSzcs() {
			return szcs;
		}

		public String getCjzj() {
			return cjzj;
		}

		public String getJzjg() {
			return jzjg;
		}

		public String getGhyt() {
			return ghyt;
		}

		public String getFwxz() {
			return fwxz;
		}

		public String getCb() {
			return cb;
		}

		public String getLjqpjyc() {
			return ljqpjyc;
		}

		public String getYxbz() {
			return yxbz;
		}

		public void setYxbz(String yxbz) {
			this.yxbz = yxbz;
		}
		
		/**
		 * 建筑面积
		 */
		private String jzmj = "";
		/**
		 * 专有建筑面积
		 */
		private String zyjzmj = "";
		/**
		 * 分摊面积
		 */
		private String ftmj = "";
		/**
		 * 总层户
		 */
		private String zcs = "";
		/**
		 * 所在层数
		 */
		private String szcs = "";
		/**
		 * 成交总价
		 */
		private String cjzj = "";
		/**
		 * 建筑结构
		 */
		private String jzjg = "";
		/**
		 * 规划用途
		 */
		private String ghyt = "";
		/**
		 * 房屋性质
		 */
		private String fwxz = "";
		/**
		 * 产别
		 */
		private String cb = "";
		/**
		 * 临街且平街一层
		 */
		private String ljqpjyc = "";
		/**
		 * 变更登记，变更前单元标识为0；变更后标识为1
		 */
		private String yxbz = "1";
		public String zrzh;
		public int index;
		/**
		 * 房屋编码
		 */
		public String fwbm = "";
	}

	/**
	 * 自然幢状况属性定义
	 */

	public class ZRZZK {
		/*
		 * 幢号
		 */
		private String zh = "";
		/*
		 * 结构
		 */
		private String jg = "";
		/*
		 * 建造年代
		 */
		private String jcnd = "";
		/*
		 * 地上层数
		 */
		private String ds = "";
		/*
		 * 地下层数
		 */
		private String dx = "";
		/*
		 * 用途
		 */
		private String yt = "";
		/*
		 * 套数或间数
		 */
		private String tshjs = "";
		/*
		 * 建筑面积
		 */
		private String jzmj = "";
		/*
		 * 室号
		 */
		private String sh = "";
		/*
		 * 分摊面积
		 */
		private String ftmj = "";

		public String getZh() {
			return zh;
		}

		public String getSh() {
			return sh;
		}

		public void setSh(String sh) {
			this.sh = sh;
		}

		public String getFtmj() {
			return ftmj;
		}

		public void setFtmj(String ftmj) {
			this.ftmj = ftmj;
		}

		public void setZh(String zh) {
			this.zh = zh;
		}

		public String getJg() {
			return jg;
		}

		public void setJg(String jg) {
			this.jg = jg;
		}

		public String getJcnd() {
			return jcnd;
		}

		public void setJcnd(String jcnd) {
			this.jcnd = jcnd;
		}

		public String getDs() {
			return ds;
		}

		public void setDs(String ds) {
			this.ds = ds;
		}

		public String getDx() {
			return dx;
		}

		public void setDx(String dx) {
			this.dx = dx;
		}

		public String getYt() {
			return yt;
		}

		public void setYt(String yt) {
			this.yt = yt;
		}

		public String getTshjs() {
			return tshjs;
		}

		public void setTshjs(String tshjs) {
			this.tshjs = tshjs;
		}

		public String getJzmj() {
			return jzmj;
		}

		public void setJzmj(String jzmj) {
			this.jzmj = jzmj;
		}

	}

	/**
	 * 土地状况属性
	 */

	public class TDZK {
		/**
		 * 宗地坐落
		 */
		private String zdzl = "";
		/**
		 * 宗地编码
		 */
		private String zdbm = "";
		/**
		 * 不动产权证号
		 */
		private String bdcqzh = "";
		/**
		 * 宗地面积
		 */
		private String zdmj = "";
		/**
		 * 土地分摊面积
		 */
		private String tdftmj = "";
		/**
		 * 出让起止年限
		 */
		private String crqznx = "";
		/**
		 * 权利性质
		 */
		private String qlxz = "";

		/**
		 * 取得价格
		 */
		private String qdjg = "";

		public String getQdjg() {
			return qdjg;
		}

		public String getZdzl() {
			return zdzl;
		}

		public String getZdbm() {
			return zdbm;
		}

		public String getBdcqzh() {
			return bdcqzh;
		}

		public String getZdmj() {
			return zdmj;
		}

		public String getTdftmj() {
			return tdftmj;
		}

		public String getCrqznx() {
			return crqznx;
		}

		public String getQlxz() {
			return qlxz;
		}

		public String getCrje() {
			return crje;
		}

		public String getQlsdms() {
			return qlsdms;
		}

		public String getQllx() {
			return qllx;
		}

		public String getZdyt() {
			return zdyt;
		}

		/**
		 * 出让金额
		 */
		private String crje = "";
		/**
		 * 权利设定模式
		 */
		private String qlsdms = "";

		/**
		 * 权利类型
		 */
		private String qllx = "";
		/**
		 * 宗地用途
		 */
		private String zdyt = "";
		
		/**
		 * 不动产单元号
		 */
		private String bdcdyh = "";
		public int index;

		public String getBdcdyh() {
			return bdcdyh;
		}

		public void setBdcdyh(String bdcdyh) {
			this.bdcdyh = bdcdyh;
		}
		
	}

	/**
	 * 审核抵押权
	 */

	public class SHDYQ {
		/**
		 * 被担保债权数额
		 */
		private String bdbzqse = "";
		/**
		 * 债权履行期限
		 */
		private String zwlxqx = "";
		/**
		 * 抵押范围
		 */
		private String dyfw = "";
		/**
		 * 抵押面积
		 */
		private String dymj = "";
		/**
		 * 抵押注销时间
		 */
		private String dyzxsj = "";
		/**
		 * 他项权利种类
		 */
		private String txqlzl = "";
		/**
		 * 抵押房号
		 */
		private String dyfh = "";

		/**
		 * 抵押评估价值
		 */
		private String dypgjz;

		/**
		 * 抵押方式
		 */
		private String dyfs;

		/**
		 * 证书个数
		 */
		private String zsgs;

		public String getDypgjz() {
			return dypgjz;
		}

		public void setDypgjz(String dypgjz) {
			this.dypgjz = dypgjz;
		}

		public String getDyfs() {
			return dyfs;
		}

		public void setDyfs(String dyfs) {
			this.dyfs = dyfs;
		}

		public String getZsgs() {
			return zsgs;
		}

		public void setZsgs(String zsgs) {
			this.zsgs = zsgs;
		}

		public String getDyfh() {
			return dyfh;
		}

		public void setDyfh(String dyfh) {
			this.dyfh = dyfh;
		}

		public String getDymj() {
			return dymj;
		}

		public void setDymj(String dymj) {
			this.dymj = dymj;
		}

		public String getDyzxsj() {
			return dyzxsj;
		}

		public void setDyzxsj(String dyzxsj) {
			this.dyzxsj = dyzxsj;
		}

		public String getTxqlzl() {
			return txqlzl;
		}

		public void setTxqlzl(String txqlzl) {
			this.txqlzl = txqlzl;
		}

		public String getBdbzqse() {
			return bdbzqse;
		}

		public void setBdbzqse(String bdbzqse) {
			this.bdbzqse = bdbzqse;
		}

		public String getZwlxqx() {
			return zwlxqx;
		}

		public void setZwlxqx(String zqlvqx) {
			this.zwlxqx = zqlvqx;
		}

		public String getDyfw() {
			return dyfw;
		}

		public void setDyfw(String dyfw) {
			this.dyfw = dyfw;
		}

		public String getSwqk() {
			return swqk;
		}

		public void setSwqk(String swqk) {
			this.swqk = swqk;
		}

		/**
		 * 顺位情况
		 */
		private String swqk = "";

		/*
		 * 他项权证号
		 */
		private String txqzh = "";
		public String zrzh;

		public String getTxqzh() {
			return txqzh;
		}

		public void setTxqzh(String txqzh) {
			this.txqzh = txqzh;
		}
	}

	/**
	 * 审核查封情况
	 */

	public class SHCFQK {
		/*
		 * 查封文号
		 */
		private String cfwh = "";

		private String lhsx;

		private String bcfqlr;


		public String getLhsx() {
			return lhsx;
		}

		public void setLhsx(String lhsx) {
			this.lhsx = lhsx;
		}

		public String getBcfqlr() {
			return bcfqlr;
		}

		public void setBcfqlr(String bcfqlr) {
			this.bcfqlr = bcfqlr;
		}

		public String getCfqx() {
			return cfqx;
		}

		public void setCfqx(String cfqx) {
			this.cfqx = cfqx;
		}

		public String getCflx() {
			return cflx;
		}

		private void setCflx(String cflx) {
			this.cflx = cflx;
		}

		public String getCfwh() {
			return cfwh;
		}

		public void setCfwh(String cfwh) {
			this.cfwh = cfwh;
		}

		public String getCfjg() {
			return cfjg;
		}

		public void setCfjg(String cfjg) {
			this.cfjg = cfjg;
		}

		public String getJfjg() {
			return jfjg;
		}

		public void setJfjg(String jfjg) {
			this.jfjg = jfjg;
		}

		public String getJfwh() {
			return jfwh;
		}

		public void setJfwh(String jfwh) {
			this.jfwh = jfwh;
		}

		
		private String cfqx = "";
		
		private String cflx = "";
		/*
		 * 查封机关
		 */
		private String cfjg = "";
		/*
		 * 解封机关
		 */
		private String jfjg = "";
		/*
		 * 解封文号
		 */
		private String jfwh = "";
	}
	
	/**
	 * 审核查封情况
	 */

	public class SHXZQK {
		/*
		 * 限制文号
		 */
		private String xzwjhm = "";
		/*
		 * 限制单位
		 */
		private String xzdw = "";
		/*
		 * 限制类型
		 */
		private String xzlxmc = "";
		/*
		 * 限制起止日期
		 */
		private String xzqzrq = "";
		public String getXzwjhm() {
			return xzwjhm;
		}
		public void setXzwjhm(String xzwjhm) {
			this.xzwjhm = xzwjhm;
		}
		public String getXzdw() {
			return xzdw;
		}
		public void setXzdw(String xzdw) {
			this.xzdw = xzdw;
		}
		public String getXzlxmc() {
			return xzlxmc;
		}
		public void setXzlxmc(String xzlxmc) {
			this.xzlxmc = xzlxmc;
		}
		public String getXzqzrq() {
			return xzqzrq;
		}
		public void setXzqzrq(String xzqzrq) {
			this.xzqzrq = xzqzrq;
		}
		/*
		 * 解除限制文号
		 */
		private String zxxzwjhm = "";
		/*
		 * 解除限制单位
		 */
		private String zxxzdw = "";
		public String getZxxzwjhm() {
			return zxxzwjhm;
		}
		public void setZxxzwjhm(String zxxzwjhm) {
			this.zxxzwjhm = zxxzwjhm;
		}
		public String getZxxzdw() {
			return zxxzdw;
		}
		public void setZxxzdw(String zxxzdw) {
			this.zxxzdw = zxxzdw;
		}
	}

	public class SHSQR {
		/**
		 * 权利人角色
		 */
		private String qlrjs = "";
		/**
		 * 权利人角色
		 */
		private String qlrmc = "";
		/**
		 * 权利人角色
		 */
		private String zjmc = "";
		/**
		 * 权利人角色
		 */
		private String zjhm = "";
		/**
		 * 权利人角色
		 */
		private String gyfs = "";
		/**
		 * 权利人角色
		 */
		private String qlfe = "";

		public String getQlrjs() {
			return qlrjs;
		}

		public void setQlrjs(String qlrjs) {
			this.qlrjs = qlrjs;
		}

		public String getQlrmc() {
			return qlrmc;
		}

		public void setQlrmc(String qlrmc) {
			this.qlrmc = qlrmc;
		}

		public String getZjmc() {
			return zjmc;
		}

		public void setZjmc(String zjmc) {
			this.zjmc = zjmc;
		}

		public String getZjhm() {
			return zjhm;
		}

		public void setZjhm(String zjhm) {
			this.zjhm = zjhm;
		}

		public String getGyfs() {
			return gyfs;
		}

		public void setGyfs(String gyfs) {
			this.gyfs = gyfs;
		}

		public String getQlfe() {
			return qlfe;
		}

		public void setQlfe(String qlfe) {
			this.qlfe = qlfe;
		}
	}

	/**
	 * 申请登记的内容
	 */

	public class DJNR {
		/**
		 * 登记内容
		 */
		private String djnr = "";
		/**
		 * 登记原因证明文件
		 */
		private String djyyzmwj = "";

		public String getDjnr() {
			return djnr;
		}

		public String getDjyyzmwj() {
			return djyyzmwj;
		}

	}

	/**
	 * 获取所以的项目审核表信息
	 * 
	 * @param dy
	 * @param xmbh
	 * @return
	 */

	public SHB createSHB(String project_id) {
		SHB shbs = new SHB();
		String fwtdftmj = "";

		ArrayList<FWZK> fwzks = new ArrayList<SHB.FWZK>();
		ArrayList<TDZK> tdzks = new ArrayList<SHB.TDZK>();
		ArrayList<ZRZZK> zrzzks = new ArrayList<SHB.ZRZZK>();
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if (info == null) {
			return null;
		}
		String xmbh = info.getXmbh();
		String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
		String qllx = info.getQllx();
		String djlx = info.getDjlx();
		List<BDCS_QL_GZ> fsqllist = dao.getDataList(BDCS_QL_GZ.class, xmbhcondition);
		String fwqdjg = null;
		if (fsqllist != null && fsqllist.size() > 0) {
			BDCS_QL_GZ fsql = fsqllist.get(0);
			try {
				fwqdjg = fsql.getQDJG().toString();
			} catch (Exception e) {

			}

		}

		String sql = "SELECT t.ACTINST_NAME AS \"name\" FROM BDC_WORKFLOW.WFI_ACTINST t INNER JOIN BDC_WORKFLOW.WFI_PROINST t1 ON t.PROINST_ID=t1.PROINST_ID\n" +
				"WHERE t1.FILE_NUMBER ='"+info.getProject_id()+"' ORDER BY t.ACTINST_START DESC";
		List<Map> results = this.dao.getDataListByFullSql(sql);
		if (results != null && results.size()>0)
			shbs.setActinstName(String.valueOf(results.get(0).get("name")));

		List<Wfi_ProInst> list_proinst=dao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+info.getProject_id()+"'");
		if(list_proinst!=null&&list_proinst.size()>0){
			shbs.setProdef_name(list_proinst.get(0).getProdef_Name().replaceAll(",", "-"));
		}

		RegisterWorkFlow flow = HandlerFactory.getWorkflow(info.getProject_id());
		
		if("XZ001".equals(flow.getName())||"XZ002".equals(flow.getName())
				||"ZXXZ001".equals(flow.getName())||"ZXXZ002".equals(flow.getName())){
			return CreateDYXZSHB(info, flow);
		}
		if("BG018".equals(flow.getName())){
			return CreateDYBGSHB(info, flow);
		}
		if (!QLLX.DIYQ.Value.equals(qllx) && !QLLX.JTJSYDSYQ.Value.equals(qllx) 
				&& !QLLX.GYJSYDSHYQ.Value.equals(qllx) && !QLLX.QTQL.Value.equals(qllx)
				&& !QLLX.ZJDSYQ.Value.equals(qllx) && !QLLX.JTTDSYQ.Value.equals(qllx)
				&& !QLLX.GJTDSYQ.Value.equals(qllx) && !QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)
				&& !QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) && !QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)
				&& !QLLX.TDCBJYQ.Value.equals(qllx) && !QLLX.TDCBJYQ_SLLMSYQ.Value.equals(qllx)
				&& !DJLX.CFDJ.Value.equals(djlx) && !DJLX.YGDJ.Value.equals(djlx)
				&& !QLLX.HYSYQ.Value.equals(qllx)&& !QLLX.GYNYDSHYQ.Value.equals(qllx)) {
			return null;
		}
		boolean bBZorHZ = false;
		if (flow.getName().contains("BZ") || flow.getName().contains("HZ")) {
			bBZorHZ = true;
		}
		if (!DJLX.ZYDJ.Value.equals(djlx) && !DJLX.BGDJ.Value.equals(djlx) && !bBZorHZ && !QLLX.DIYQ.Value.equals(qllx)
				&& !DJLX.CFDJ.Value.equals(djlx) && !DJLX.ZXDJ.Value.equals(djlx) && !DJLX.GZDJ.Value.equals(djlx)
				&& !DJLX.YGDJ.Value.equals(djlx) && !DJLX.CSDJ.Value.equals(djlx)&&!DJLX.YYDJ.Value.equals(djlx)) {
			return null;
		}
		getUnitInfo(shbs, info);
		// BDCDYLX lx = BDCDYLX.initFrom(info.getBdcdylx());
		// if (BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
		// StringBuilder querybuilder=new StringBuilder();
		// querybuilder.append(xmbhcondition);
		// List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
		// querybuilder.toString());
		// if (djdys != null && djdys.size() > 0) {
		// for (BDCS_DJDY_GZ djdy : djdys) {
		// DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
		//
		// if(DJDYLY.XZ.equals(ly)){
		// List<Rights> qls_zd=RightsTools.loadRightsByCondition(DJDYLY.XZ,
		// "DJDYID='"+djdy.getDJDYID()+"' AND QLLX IN ('1','2','3','5','7')");
		// if(qls_zd!=null&&qls_zd.size()>0){
		// shbs.zdbdcqzh=shbs.zdbdcqzh+"
		// "+(qls_zd.get(0).getBDCQZH()!=null?qls_zd.get(0).getBDCQZH():"");
		// }
		// if(DJLX.BGDJ.Value.equals(djlx)){
		// continue;
		// }
		// }
		//
		// if(flow.getHandlername().contains("GZ")){
		// List<Rights> qls_zd=RightsTools.loadRightsByCondition(DJDYLY.XZ,
		// "DJDYID='"+djdy.getDJDYID()+"' AND QLLX IN ('4','6','8')");
		// if(qls_zd!=null&&qls_zd.size()>0){
		// shbs.fwbdcqzh=shbs.fwbdcqzh+"
		// "+qls_zd.get(0).getBDCQZH()!=null?qls_zd.get(0).getBDCQZH():"";
		// }
		// List<BDCS_DJDY_XZ> djdys_xz=dao.getDataList(BDCS_DJDY_XZ.class,
		// "DJDYID='"+djdy.getDJDYID()+"'");
		// if(djdys_xz!=null&&djdys_xz.size()>0){
		// RealUnit unit_y = UnitTools.loadUnit(lx, DJDYLY.XZ,
		// djdys_xz.get(0).getBDCDYID());
		// if(unit_y!=null){
		// shbs.yfwzl=shbs.yfwzl+" "+unit_y.getZL()!=null?unit_y.getZL():"";
		// }
		// }
		// }
		//
		// RealUnit unit = UnitTools.loadUnit(lx, ly,
		// djdy.getBDCDYID());
		// UseLand land = (UseLand) unit;
		// shbs.zdzl=shbs.zdzl+(land.getZL()!=null?land.getZL():"");
		// List<TDYT> list = land.getTDYTS();
		// if (list == null || list.size() == 0) {
		// TDZK zk = new TDZK();
		// zk.zdbm = land.getZDDM()!=null?land.getZDDM():"";
		// if(land.getZDMJ()!=null){
		// zk.zdmj = StringHelper.formatObject(land.getZDMJ());
		// }
		// if(land.getZDMJ()!=null){
		// zk.tdftmj = StringHelper.formatObject(land
		// .getJZMJ());
		// }
		// zk.qdjg="";
		// if(fsqllist!=null && fsqllist.size()>0){
		// BDCS_QL_GZ ql=fsqllist.get(0);
		// if(ql.getQDJG() != null){
		// String qdjgs=StringHelper.formatDouble(ql.getQDJG());
		// zk.qdjg=qdjgs;
		// }
		//
		// }
		//
		// zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX());
		// zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ());
		// zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());
		// tdzks.add(zk);
		// } else {
		// for (TDYT tdyt : list) {
		// TDZK zk = new TDZK();
		// zk.zdbm = land.getZDDM()!=null?land.getZDDM():"";
		// if(land.getZDMJ()!=null){
		// zk.zdmj = StringHelper.formatObject(land
		// .getZDMJ());
		// }
		// if(land.getJZMJ()!=null){
		// zk.tdftmj = StringHelper.formatObject(land
		// .getJZMJ());
		// }
		// zk.crqznx = "";
		// if (tdyt.getQSRQ() != null) {
		// zk.crqznx = StringHelper
		// .FormatByDatetime(tdyt.getQSRQ())
		// + "至";
		// }
		// if (tdyt.getZZRQ() != null) {
		// zk.crqznx = zk.crqznx
		// + StringHelper
		// .FormatByDatetime(tdyt
		// .getZZRQ());
		// }
		// String tdytid=tdyt.getBDCDYID();
		// String crjbz=tdyt.getCRJBZ();
		//
		// zk.crje="";
		// if(crjbz!=null){
		// zk.crje=crjbz;
		// }
		// zk.qdjg="";
		// if(fsqllist!=null && fsqllist.size()>0){
		// BDCS_QL_GZ ql=fsqllist.get(0);
		// if(ql.getQDJG() != null){
		// String qdjgs=StringHelper.formatDouble(ql.getQDJG());
		// zk.qdjg=qdjgs;
		// }
		//
		// }
		// zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX());
		// zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ());
		// zk.zdyt = ConstHelper.getNameByValue("TDYT", tdyt.getTDYT());
		// zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());
		// tdzks.add(zk);
		// }
		// }
		// }
		// }
		// }
		// else if (BDCDYLX.H.equals(lx) || BDCDYLX.YCH.equals(lx)) {
		// StringBuilder querybuilder=new StringBuilder();
		// querybuilder.append(xmbhcondition);
		// List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
		// querybuilder.toString());
		// if (djdys != null && djdys.size() > 0) {
		// for (BDCS_DJDY_GZ djdy : djdys) {
		// DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
		// if(DJDYLY.XZ.equals(ly)){
		// List<Rights> qls_zd=RightsTools.loadRightsByCondition(DJDYLY.XZ,
		// "DJDYID='"+djdy.getDJDYID()+"' AND QLLX IN ('4','6','8')");
		// if(qls_zd!=null&&qls_zd.size()>0){
		// shbs.fwbdcqzh=shbs.fwbdcqzh+"
		// "+qls_zd.get(0).getBDCQZH()!=null?qls_zd.get(0).getBDCQZH():"";
		// }
		// RealUnit unit_y = UnitTools.loadUnit(lx, ly,
		// djdy.getBDCDYID());
		// if(unit_y!=null){
		// if(DJLX.BGDJ.Value.equals(djlx)){
		// shbs.yfwzl=shbs.yfwzl+" "+unit_y.getZL()!=null?unit_y.getZL():"";
		// continue;
		// }
		// }
		// }
		// if(flow.getHandlername().contains("GZ")){
		// List<Rights> qls_zd=RightsTools.loadRightsByCondition(DJDYLY.XZ,
		// "DJDYID='"+djdy.getDJDYID()+"' AND QLLX IN ('4','6','8')");
		// if(qls_zd!=null&&qls_zd.size()>0){
		// shbs.fwbdcqzh=shbs.fwbdcqzh+"
		// "+qls_zd.get(0).getBDCQZH()!=null?qls_zd.get(0).getBDCQZH():"";
		// }
		// List<BDCS_DJDY_XZ> djdys_xz=dao.getDataList(BDCS_DJDY_XZ.class,
		// "DJDYID='"+djdy.getDJDYID()+"'");
		// if(djdys_xz!=null&&djdys_xz.size()>0){
		// RealUnit unit_y = UnitTools.loadUnit(lx, DJDYLY.XZ,
		// djdys_xz.get(0).getBDCDYID());
		// if(unit_y!=null){
		// shbs.yfwzl=shbs.yfwzl+" "+unit_y.getZL()!=null?unit_y.getZL():"";
		// }
		// }
		// }
		// RealUnit unit = UnitTools.loadUnit(lx, ly,
		// djdy.getBDCDYID());
		// House house = (House) unit;
		// shbs.xfwzl=shbs.xfwzl+" "+house.getZL()!=null?house.getZL():"";
		// FWZK fwzk = new FWZK();
		// fwzk.fh = house.getFH()!=null?house.getFH():"";
		// if(lx.Value.equals("031")){
		// if(house.getSCJZMJ()!=null){
		// fwzk.jzmj = StringHelper
		// .formatObject(house.getSCJZMJ());
		// }
		// if(house.getSCTNJZMJ()!=null){
		// fwzk.zyjzmj = StringHelper.formatObject(house
		// .getSCTNJZMJ());
		// }
		// if(house.getSCFTJZMJ()!=null){
		// fwzk.ftmj = StringHelper.formatObject(house
		// .getSCFTJZMJ());
		// }
		// }else{
		// if(house.getYCJZMJ()!=null){
		// fwzk.jzmj = StringHelper
		// .formatObject(house.getYCJZMJ());
		// }
		// if(house.getYCTNJZMJ()!=null){
		// fwzk.zyjzmj = StringHelper.formatObject(house
		// .getYCTNJZMJ());
		// }
		// if(house.getYCFTJZMJ()!=null){
		// fwzk.ftmj = StringHelper.formatObject(house
		// .getYCFTJZMJ());
		// }
		// }
		//
		// if(house.getZCS()!=null){
		// fwzk.zcs = StringHelper.formatObject(house.getZCS());
		// }
		// if(house.getSZC()!=null){
		// fwzk.szcs = StringHelper.formatObject(house.getSZC());
		// }
		// try{
		// fwtdftmj=house.getFTTDMJ().toString();
		// }
		// catch(Exception e){
		//
		// }
		//
		// if(fwqdjg != null){
		// fwzk.cjzj = fwqdjg;
		// }
		// fwzk.jzjg = ConstHelper.getNameByValue("FWJG", house.getFWJG());
		// fwzk.ghyt = ConstHelper.getNameByValue("FWYT", house.getGHYT());
		// fwzk.fwxz = ConstHelper.getNameByValue("FWXZ", house.getFWXZ());
		// fwzk.cb = ConstHelper.getNameByValue("FWCB", house.getFWCB());
		// fwzk.ljqpjyc = "否";
		// if(SF.YES.Value.equals(StringHelper.formatObject(house.getSFLJQPJYC()))){
		// fwzk.ljqpjyc = "是";
		// }
		// try {
		// fwzks.add(fwzk);
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// } finally {
		// if (house.getZDBDCDYID() != null) {
		// DJDYLY ly_t=ly;
		// if(flow.getHandlername().contains("GZ")){
		// ly_t=DJDYLY.XZ;
		// }
		// RealUnit unit_land = UnitTools.loadUnit(
		// BDCDYLX.SHYQZD, ly_t,
		// house.getZDBDCDYID());
		// if(unit_land!=null){
		// UseLand land = (UseLand) unit_land;
		// shbs.zdzl=shbs.zdzl+(land.getZL()!=null?land.getZL():"");
		// List<BDCS_DJDY_XZ> djdys_zd=dao.getDataList(BDCS_DJDY_XZ.class,
		// "BDCDYID='"+land.getId()+"'");
		// if(djdys_zd!=null&&djdys_zd.size()>0){
		// List<Rights> qls_zd=RightsTools.loadRightsByCondition(DJDYLY.XZ,
		// "DJDYID='"+djdys_zd.get(0).getDJDYID()+"' AND QLLX IN
		// ('1','2','3','5','7')");
		// if(qls_zd!=null&&qls_zd.size()>0){
		// shbs.zdbdcqzh=shbs.zdbdcqzh+"
		// "+(qls_zd.get(0).getBDCQZH()!=null?qls_zd.get(0).getBDCQZH():"");
		// }
		// }
		// List<TDYT> list = land.getTDYTS();
		// if (list == null || list.size() == 0) {
		// TDZK zk = new TDZK();
		// zk.zdbm = land.getZDDM()!=null?land.getZDDM():"";
		// if(land.getZDMJ()!=null){
		// zk.zdmj = StringHelper.formatObject(land
		// .getZDMJ());
		// }
		// if(fwtdftmj!=null){
		// zk.tdftmj = fwtdftmj;
		// }
		// zk.qdjg="";
		// if(fsqllist!=null && fsqllist.size()>0){
		// BDCS_QL_GZ ql=fsqllist.get(0);
		// if(ql.getQDJG() != null){
		// String qdjgs=StringHelper.formatDouble(ql.getQDJG());
		// zk.qdjg=qdjgs;
		// }
		// }
		// zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX());
		// zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ());
		// zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());
		// tdzks.add(zk);
		// } else {
		// for (TDYT tdyt : list) {
		// TDZK zk = new TDZK();
		// zk.zdbm = land.getZDDM()!=null?land.getZDDM():"";
		// if(land.getZDMJ()!=null){
		// zk.zdmj = StringHelper
		// .formatObject(land.getZDMJ());
		// }
		// if(fwtdftmj!=null){
		// zk.tdftmj = fwtdftmj;
		// }
		// zk.crqznx = "";
		// if (tdyt.getQSRQ() != null) {
		// zk.crqznx = StringHelper
		// .FormatByDatetime(tdyt
		// .getQSRQ())
		// + "至";
		// }
		// if (tdyt.getZZRQ() != null) {
		// zk.crqznx = zk.crqznx
		// + StringHelper
		// .FormatByDatetime(tdyt
		// .getZZRQ());
		// }
		// String crjbz=tdyt.getCRJBZ();
		// zk.crje="";
		// if(crjbz!=null){
		// zk.crje=crjbz;
		// }
		// zk.qdjg="";
		// if(fsqllist!=null && fsqllist.size()>0){
		// BDCS_QL_GZ ql=fsqllist.get(0);
		// if(ql.getQDJG() != null){
		// String qdjgs=StringHelper.formatDouble(ql.getQDJG());
		// zk.qdjg=qdjgs;
		// }
		//
		// }
		// zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX());
		// zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ());
		// zk.zdyt = ConstHelper.getNameByValue("TDYT", tdyt.getTDYT());
		// zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());
		// tdzks.add(zk);
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// shbs.fwzks=fwzks;
		// shbs.tdzks=tdzks;

		/**
		 * 获取申请人信息
		 */
		String isformatzj = ConfigHelper.getNameByValue("ISFORMATZJ");
		shbs.sqrs = new ArrayList<SHB.SHSQR>();
		List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, xmbhcondition+" ORDER BY SQRLB DESC,SXH ASC");
		if (sqrlist != null && sqrlist.size() > 0) {
			for (BDCS_SQR sqr : sqrlist) {
				SHSQR shsqr = new SHSQR();
				shsqr.setQlrjs(ConstHelper.getNameByValue("SQRLB", sqr.getSQRLB()));
				shsqr.setQlrmc(sqr.getSQRXM() != null ? sqr.getSQRXM() : "");
				shsqr.setZjmc(ConstHelper.getNameByValue("ZJLX", sqr.getZJLX()));
				if("2".equals(isformatzj)) {
					shsqr.setZjhm(sqr.getZJH() != null ? sqr.getZJH() : "");
				}else {
					shsqr.setZjhm(StringHelper.formatZJ(sqr.getZJH() != null ? sqr.getZJH() : ""));
				}
				shsqr.setGyfs(ConstHelper.getNameByValue("GYFS", sqr.getGYFS()));
				shsqr.setQlfe(sqr.getQLBL() != null ? sqr.getQLBL() : "");
				shbs.sqrs.add(shsqr);
			}
		}

		/**
		 * 获取权证附记内容
		 */
		List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, xmbhcondition);
		if (qls != null && qls.size() > 0) {
			BDCS_QL_GZ getqls = qls.get(0);
			shbs.qzfjnr = getqls.getFJ() != null ? getqls.getFJ() : "";
			shbs.djyy = getqls.getDJYY() != null ? getqls.getDJYY() : "";
			/**
			 * 获取抵押权信息
			 */
			String isformatdyj= ConfigHelper.getNameByValue("ISFORMATDYJ");
			if (QLLX.DIYQ.Value.equals(qllx) && DJLX.ZXDJ.Value.equals(djlx)) {
				shbs.zxdyqk = new ArrayList<SHB.SHDYQ>();
				for (BDCS_QL_GZ gzql : qls) {
					List<BDCS_DJDY_XZ> dys=dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+gzql.getDJDYID()+"'");
					BDCS_H_XZ h_xz=null;
					if(dys!=null&&dys.size()>0){
						BDCS_DJDY_XZ dy_=dys.get(0);
						h_xz=dao.get(BDCS_H_XZ.class, dy_.getBDCDYID());
					}
					SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, gzql.getId());
					if (fsql != null) {
						//审核表里面抵押金增加单位
						String dw=ConstHelper.getNameByValue("JEDW", fsql.getZQDW());
						SHDYQ qk = new SHDYQ();
						if (!("2").equals(fsql.getDYFS())) {
							if (fsql.getBDBZZQSE() != null) {
								if("2".equals(isformatdyj)) {
									qk.setBdbzqse(StringHelper.formatDouble(fsql.getBDBZZQSE())+dw);
								}else {
									qk.setBdbzqse(StringHelper.formatNum(StringHelper.formatDouble(fsql.getBDBZZQSE()))+dw);
								}
							}
						} else {
							if (fsql.getZGZQSE() != null) {
								if("2".equals(isformatdyj)) {
									qk.setBdbzqse(StringHelper.formatDouble(fsql.getZGZQSE())+dw);
								}else {
									qk.setBdbzqse(StringHelper.formatNum(StringHelper.formatDouble(fsql.getZGZQSE()))+dw);
								}
							}
						}
						if (fsql.getDBFW() != null) {
							qk.setDyfw(fsql.getDBFW());
						}
						if (fsql.getDYSW() != null) {
							qk.setSwqk(StringHelper.formatDouble(fsql.getDYSW()));
						}

						String zqlvqx = "";
						if (getqls.getQLQSSJ() != null) {
							zqlvqx = StringHelper.FormatByDatetime(gzql.getQLQSSJ()) + "至";
						}
						if (getqls.getQLJSSJ() != null) {
							zqlvqx = zqlvqx + StringHelper.FormatByDatetime(gzql.getQLJSSJ());
						}
						qk.setZwlxqx(zqlvqx);
						if (gzql.getBDCQZH() != null) {
							qk.setTxqzh(gzql.getBDCQZH());
						}
						//SCJZMJ
						if(h_xz!=null&&!"".equals(h_xz.getSCJZMJ())){
							qk.setDymj(h_xz.getSCJZMJ().toString());
						}
						shbs.zxdyqk.add(qk);
					}
				}
			} // 2016年7月5日16:47:25 从现状层获取权利信息
			else if (QLLX.DIYQ.Value.equals(qllx)
					&& flow.getHandlername().toUpperCase().contains("YDYZXDYDJHandler_LuZhou".toUpperCase())) {
				shbs.dyqk = new ArrayList<SHB.SHDYQ>();
				for (BDCS_QL_GZ QL_GZ : qls) {
					Rights ql=RightsTools.loadRights(DJDYLY.LS, QL_GZ.getLYQLID());
					SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, QL_GZ.getLYQLID());
					if(ql!=null&&fsql!=null){
						SHDYQ dy = new SHDYQ();
						//审核表里面抵押金增加单位
						String dw=ConstHelper.getNameByValue("JEDW", fsql.getZQDW());
						if (!StringHelper.isEmpty(ql.getDJDYID())) {
							List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class,
									"DJDYID='" + ql.getDJDYID() + "'");
							if (djdys.size() > 0) {
								BDCS_DJDY_XZ djdy = djdys.get(0);
								BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
								RealUnit unit = UnitTools.loadUnit(lx, DJDYLY.LS, djdy.getBDCDYID());
								if (unit instanceof House) {
									House house = (House) unit;
									if (!StringHelper.isEmpty(house.getFH())) {
										dy.setDyfh(house.getFH());
									}
									if(!StringHelper.isEmpty(house.getYCJZMJ())){
										dy.setDymj(StringHelper.formatDouble(house.getYCJZMJ()));
									}
									dy.zrzh=house.getZRZH();
								}
							}

						}
						if (!("2").equals(fsql.getDYFS())) {
							if (fsql.getBDBZZQSE() != null) {
								if("2".equals(isformatdyj)) {
									dy.setBdbzqse(StringHelper.formatDouble(fsql.getBDBZZQSE())+dw);
								}else {
									dy.setBdbzqse(StringHelper.formatNum(StringHelper.formatDouble(fsql.getBDBZZQSE()))+dw);
								}
							}
						} else {
							if (fsql.getZGZQSE() != null) {
								if("2".equals(isformatdyj)) {
									dy.setBdbzqse(StringHelper.formatDouble(fsql.getZGZQSE())+dw);
								}else {
									dy.setBdbzqse(StringHelper.formatNum(StringHelper.formatDouble(fsql.getZGZQSE()))+dw);
								}
							}
						}
						if (fsql.getZJJZWDYFW() != null) {
							dy.setDyfw(fsql.getZJJZWDYFW());
						}
						if (fsql.getDYSW() != null) {
							dy.setSwqk(StringHelper.formatDouble(fsql.getDYSW()));
						}
						if (fsql.getZXSJ() != null) {
							dy.setDyzxsj(StringHelper.FormatYmdhmsByDate(fsql.getZXSJ()));
						}

						if (fsql.getDYMJ() != null) {
							dy.setDymj(StringHelper.formatDouble(fsql.getDYMJ()));
						}

						if (fsql.getDYBDCLX() != null) {
							dy.setTxqlzl(DYBDCLX.TDHFW.Name);
						}

						String zqlvqx = "";
						if (ql.getQLQSSJ() != null) {
							zqlvqx = StringHelper.FormatByDatetime(ql.getQLQSSJ()) + "至";
						}
						if (ql.getQLJSSJ() != null) {
							zqlvqx = zqlvqx + StringHelper.FormatByDatetime(ql.getQLJSSJ());
						}
						dy.setSwqk(StringHelper.formatDouble(fsql.getDYSW()));
						dy.setZwlxqx(zqlvqx);
						
						shbs.dyqk.add(dy);
					}
				}

			} else if (QLLX.DIYQ.Value.equals(qllx)
					|| flow.getHandlername().toUpperCase().contains("YGYDYDJHandler_LuZhou".toUpperCase())
					|| flow.getHandlername().toUpperCase().contains("YGYDYDJHandler".toUpperCase())) {
				shbs.dyqk = new ArrayList<SHB.SHDYQ>();
				for (BDCS_QL_GZ gzql : qls) {
					if (QLLX.DIYQ.Value.equals(gzql.getQLLX())) {
						List<BDCS_DJDY_XZ> dys=dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+gzql.getDJDYID()+"'");
						RealUnit unit_1=null;
						if(dys!=null&&dys.size()>0){
							unit_1=UnitTools.loadUnit(BDCDYLX.initFrom(dys.get(0).getBDCDYLX()), dys.get(0).getLY() == null ? DJDYLY.XZ:DJDYLY.initFrom(dys.get(0).getLY()), dys.get(0).getBDCDYID());
							if(unit_1!=null){
								
							}
						}
						SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, gzql.getId());
						if (fsql != null) {
							//审核表里面抵押金增加单位
							String dw=ConstHelper.getNameByValue("JEDW", fsql.getZQDW());
							SHDYQ dy = new SHDYQ();
							if (!StringHelper.isEmpty(gzql.getDJDYID())) {
								List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
										"DJDYID='" + gzql.getDJDYID() + "'");
								if (djdys.size() > 0) {
									BDCS_DJDY_GZ djdy = djdys.get(0);
									DJDYLY ly = DJDYLY.GZ;
									BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
									if (DJDYLY.XZ.Value.equals(djdy.getLY())||DJDYLY.LS.Value.equals(djdy.getLY())) {
										ly = DJDYLY.LS;
									}
									RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
									if (unit instanceof House) {
										House house = (House) unit;
										if (!StringHelper.isEmpty(house.getFH())) {
											dy.setDyfh(house.getFH());
										}
										dy.zrzh=house.getZRZH();
									}
								}

							}
							if (!("2").equals(fsql.getDYFS())) {
								if (fsql.getBDBZZQSE() != null) {
									if("2".equals(isformatdyj)) {
										dy.setBdbzqse(StringHelper.formatDouble(fsql.getBDBZZQSE())+dw);
									}else {
										dy.setBdbzqse(StringHelper.formatNum(StringHelper.formatDouble(fsql.getBDBZZQSE()))+dw);
									}
								}
							} else {
								if (fsql.getZGZQSE() != null) {
									if("2".equals(isformatdyj)) {
										dy.setBdbzqse(StringHelper.formatDouble(fsql.getZGZQSE())+dw);
									}else {
										dy.setBdbzqse(StringHelper.formatNum(StringHelper.formatDouble(fsql.getZGZQSE()))+dw);
									}
								}
							}
							if (fsql.getZJJZWDYFW() != null) {
								dy.setDyfw(fsql.getZJJZWDYFW());
							}
							if (fsql.getDYSW() != null) {
								dy.setSwqk(StringHelper.formatDouble(fsql.getDYSW()));
							}
							if (fsql.getZXSJ() != null) {
								dy.setDyzxsj(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy年MM月dd"));
							}

							if (fsql.getDYMJ() != null) {
								dy.setDymj(StringHelper.formatDouble(fsql.getDYMJ()));
							}

							if (fsql.getDYBDCLX() != null) {
								dy.setTxqlzl(StringHelper.formatObject(fsql.getDYBDCLX()));
							}

							String zqlvqx = "";
							if (gzql.getQLQSSJ() != null) {
								zqlvqx = StringHelper.FormatByDatetime(gzql.getQLQSSJ()) + "至";
							}
							if (gzql.getQLJSSJ() != null) {
								zqlvqx = zqlvqx + StringHelper.FormatByDatetime(gzql.getQLJSSJ());
							}
							dy.setZwlxqx(zqlvqx);
							//SCJZMJ
							if(!StringHelper.isEmpty(unit_1)){
								if(!StringHelper.isEmpty(unit_1.getMJ())){
									dy.setDymj(StringHelper.formatDouble(unit_1.getMJ()));
								}
							}

							dy.setDypgjz(fsql.getDYPGJZ() + "");

							dy.setDyfs( "1".equals(fsql.getDYFS()) ? "一般抵押 ":"最高额抵押 ");

							dy.setZsgs("0".equals(info.getSfhbzs())? "每个单元一本证": "多个单元一本证");
							//dy.setZsgs();
							shbs.dyqk.add(dy);
						}
					}
				}
			} else if (DJLX.CFDJ.Value.equals(djlx)) {
				if (QLLX.QTQL.Value.equals(qllx)) {
					shbs.cfqk = new ArrayList<SHB.SHCFQK>();
					for (BDCS_QL_GZ gzql : qls) {
						SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, gzql.getId());
						if (fsql != null) {
							SHCFQK qk = new SHCFQK();
							if (fsql.getCFJG() != null) {
								qk.setCfjg(fsql.getCFJG());
							}
							if (fsql.getCFWH() != null) {
								qk.setCfwh(fsql.getCFWH());
							}
							if (fsql.getCFLX() != null) {
								qk.setCflx((ConstHelper.getNameByValue("CFLX", fsql.getCFLX())));
							}
							if (gzql.getQLJSSJ() != null&&gzql.getQLQSSJ()!=null) {
								String qs = StringHelper.FormatByDatetime(gzql.getQLQSSJ());
								String js = StringHelper.FormatByDatetime(gzql.getQLJSSJ());
								qk.setCfqx(qs+"-"+js);
							}

							if (fsql.getLHSX()!=null) {
								qk.setLhsx(String.valueOf(fsql.getLHSX()));
							}

							String condition = MessageFormat.format("QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24') AND DJDYID=''{0}''", gzql.getDJDYID());
							List<Rights> lyql = RightsTools.loadRightsByCondition(DJDYLY.XZ,condition);
							if(!StringHelper.isEmpty(lyql) && lyql.size()>0){
								String qlrmc= "";
								List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, "qlid ='" + lyql.get(0).getId() + "'");
								if(!StringHelper.isEmpty(qlrs) && qlrs.size()>0){
									boolean flag = true;
									for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
										if (flag) {
											qlrmc += bdcs_qlr_xz.getQLRMC();
											flag = false;
										} else {
											qlrmc += "," + bdcs_qlr_xz.getQLRMC();
										}
									}
								}
								qk.setBcfqlr(qlrmc);
							}
							shbs.cfqk.add(qk);
						}
					}

				} else {
					shbs.jfqk = new ArrayList<SHB.SHCFQK>();
					for (BDCS_QL_GZ gzql : qls) {
						SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, gzql.getId());
						if (fsql != null) {
							SHCFQK qk = new SHCFQK();
							if (fsql.getCFJG() != null) {
								qk.setCfjg(fsql.getCFJG());
							}
							if (fsql.getCFJG() != null) {
								qk.setCfwh(fsql.getCFWH());
							}
							if (fsql.getJFJG() != null) {
								qk.setJfjg(fsql.getJFJG());
							}
							if (fsql.getJFWH() != null) {
								qk.setJfwh(fsql.getJFWH());
							}
							shbs.jfqk.add(qk);
						}
					}
				}
			}
			//预告注销义务人货物
			HashSet<String> hs = null;
			for (BDCS_QL_GZ gzql : qls) {
				SubRights subs = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, gzql.getLYQLID());
				hs = new HashSet<String>();
				if(subs!=null&&!StringHelper.isEmpty(subs.getYWR())){
					hs.add(subs.getYWR().trim());
				}
			}
			if(hs!=null){
				String str = hs.toString();
				str = str.substring(1, str.length()-1);
				this.ygzxywr = str;
			}
		}
		
		/**
		 * 添加排序方法，不能直接获取，好像属于引用方式，只能重新定义一个，再清空原有数据
		 */
		if(!StringHelper.isEmpty(this.ygzxywr)){
			shbs.setYgzxywr(this.ygzxywr);
		}
		List<FWZK> lst=new ArrayList<SHB.FWZK>();
		if(!StringHelper.isEmpty(shbs.getFwzks()) && shbs.getFwzks().size()>0){
			for(FWZK fwzk :shbs.getFwzks()){
				lst.add(fwzk);
			}  
		}
        lst= ObjectHelper.SortList(lst);	
        shbs.fwzks.clear();
        for(int i=0;i<lst.size();i++){
        	FWZK fwzk=lst.get(i);
        	if(!StringHelper.isEmpty(fwzk)){
        		fwzk.index=i+1;
        		shbs.fwzks.add(fwzk);
        	}
        }

		List<HistoryRight> historyRights = new ArrayList<HistoryRight>();
        if (shbs.fwzks!=null && shbs.fwzks.size()>0) {
			for (FWZK fwzk : shbs.fwzks) {
				List<Map> bdcdyids = this.dao.getDataListByFullSql("SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ WHERE BDCDYH='"+fwzk.bdcdyh+"'");
				if (bdcdyids!=null && bdcdyids.size()>0)
					historyRights.add(new HistoryRight(String.valueOf(bdcdyids.get(0).get("BDCDYID")), fwzk.getXfwzl()));
			}
		}
		shbs.setHistoryRights(historyRights);
		/**
		 * 申请登记内容
		 */
		return shbs;
	}
	
	private SHB CreateDYXZSHB(ProjectInfo info,RegisterWorkFlow flow){
		SHB shbs = new SHB();
		List<Wfi_ProInst> list_proinst=dao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+info.getProject_id()+"'");
		if(list_proinst!=null&&list_proinst.size()>0){
			shbs.setProdef_name(list_proinst.get(0).getProdef_Name().replaceAll(",", "-"));
		}
		shbs.setXminfo(info);
		String sqlxmbh = ProjectHelper.GetXMBHCondition(info.getXmbh());
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(info.getXmbh());
		ArrayList<TDZK> tdzks = new ArrayList<SHB.TDZK>();
		ArrayList<FWZK> fwzks = new ArrayList<SHB.FWZK>();
		ArrayList<ZRZZK> zrzzks = new ArrayList<SHB.ZRZZK>();
		if (!StringHelper.isEmpty(xmxx)) {
			List<BDCS_XM_DYXZ> djdys = dao.getDataList(BDCS_XM_DYXZ.class, sqlxmbh);
			for (BDCS_XM_DYXZ djdy : djdys) {
				DJDYLY ly = DJDYLY.XZ;
				BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					if (!StringHelper.isEmpty(flow)) {
						UnitInfoEX(unit, shbs, djdy, ly, lx, tdzks, fwzks, zrzzks);
						
						shbs.tdzks = tdzks;
						shbs.fwzks = fwzks;
						shbs.zrzzks = zrzzks;
					} 
				}
			}
			//宗地坐落去重
			String[] zdzl = shbs.zdzl.split(" ");			
			if (zdzl != null && zdzl.length > 0) {
				shbs.zdzl = "";
				List<String> zdzl_qc = new ArrayList<String>();					
				for (int i = 0; i < zdzl.length; i++) {
					if (!zdzl_qc.contains(zdzl[i])) {
						zdzl_qc.add(zdzl[i]);							
					}
				}
				for (int j = 0; j < zdzl_qc.size(); j++) {
					shbs.zdzl = shbs.zdzl + " " + zdzl_qc.get(j);
				}
				
			}
		}
		
		
		
		
		List<BDCS_XM_DYXZ> list_dyxz=dao.getDataList(BDCS_XM_DYXZ.class, "XMBH='"+info.getXmbh()+"'");
		if(list_dyxz!=null&&list_dyxz.size()>0){
			if("XZ001".equals(flow.getName())||"XZ002".equals(flow.getName())){
				shbs.xzqk=new ArrayList<SHB.SHXZQK>();
			}else if("ZXXZ001".equals(flow.getName())||"ZXXZ002".equals(flow.getName())){
				shbs.jcxzqk=new ArrayList<SHB.SHXZQK>();
			}
			for(BDCS_XM_DYXZ xmdyxz:list_dyxz){
				SHXZQK qk=new SHXZQK();
				BDCS_DYXZ dyxz=dao.get(BDCS_DYXZ.class, xmdyxz.getDYXZID());
				if(dyxz!=null){
					qk.setXzdw(dyxz.getXZDW());
					qk.setXzlxmc(dyxz.getXZLXName());
					qk.setXzwjhm(dyxz.getXZWJHM());
					String qs = StringHelper.FormatByDatetime(dyxz.getXZQSRQ());
					String js = StringHelper.FormatByDatetime(dyxz.getXZZZRQ());
					qk.setXzqzrq(qs+"-"+js);
				}
				qk.setZxxzdw(xmdyxz.getZXXZDW());
				qk.setZxxzwjhm(xmdyxz.getZXXZWJHM());
				if("XZ001".equals(flow.getName())||"XZ002".equals(flow.getName())){
					shbs.xzqk.add(qk);
				}else if("ZXXZ001".equals(flow.getName())||"ZXXZ002".equals(flow.getName())){
					shbs.jcxzqk.add(qk);
				}
			}
		}
		/**
		 * 获取申请人信息
		 */
		String isformatzj = ConfigHelper.getNameByValue("ISFORMATZJ");
		shbs.sqrs = new ArrayList<SHB.SHSQR>();
		List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, "XMBH='"+info.getXmbh()+"' ORDER BY SQRLB DESC,SXH ASC");
		if (sqrlist != null && sqrlist.size() > 0) {
			for (BDCS_SQR sqr : sqrlist) {
				SHSQR shsqr = new SHSQR();
				shsqr.setQlrjs(ConstHelper.getNameByValue("SQRLB", sqr.getSQRLB()));
				shsqr.setQlrmc(sqr.getSQRXM() != null ? sqr.getSQRXM() : "");
				shsqr.setZjmc(ConstHelper.getNameByValue("ZJLX", sqr.getZJLX()));
				if("2".equals(isformatzj)) {
					shsqr.setZjhm(sqr.getZJH() != null ? sqr.getZJH() : "");
				}else {
					shsqr.setZjhm(StringHelper.formatZJ(sqr.getZJH() != null ? sqr.getZJH() : ""));
				}
				shsqr.setGyfs(ConstHelper.getNameByValue("GYFS", sqr.getGYFS()));
				shsqr.setQlfe(sqr.getQLBL() != null ? sqr.getQLBL() : "");
				shbs.sqrs.add(shsqr);
			}
		}
		return shbs;
	}
	
	private SHB CreateDYBGSHB(ProjectInfo info,RegisterWorkFlow flow){
		SHB shbs = new SHB();
		List<Wfi_ProInst> list_proinst=dao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+info.getProject_id()+"'");
		if(list_proinst!=null&&list_proinst.size()>0){
			shbs.setProdef_name(list_proinst.get(0).getProdef_Name().replaceAll(",", "-"));
		}
		shbs.setXminfo(info);
		String sqlxmbh = ProjectHelper.GetXMBHCondition(info.getXmbh());
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(info.getXmbh());
		ArrayList<TDZK> tdzks = new ArrayList<SHB.TDZK>();
		ArrayList<FWZK> fwzks = new ArrayList<SHB.FWZK>();
		ArrayList<ZRZZK> zrzzks = new ArrayList<SHB.ZRZZK>();
		if (!StringHelper.isEmpty(xmxx)) {
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, sqlxmbh);
			int index= 0;
			for (BDCS_DJDY_GZ djdy : djdys) {
				if(djdy.getLY()==DJDYLY.XZ.Value){
					continue;
				}
				DJDYLY ly = DJDYLY.GZ;
				BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					if (!StringHelper.isEmpty(flow)) {
						UnitInfo(unit, shbs, djdy, ly, lx, tdzks, fwzks, zrzzks,++index);
						
						shbs.tdzks = tdzks;
						shbs.fwzks = fwzks;
						shbs.zrzzks = zrzzks;
					} 
				}
			}
			//宗地坐落去重
			String[] zdzl = shbs.zdzl.split(" ");			
			if (zdzl != null && zdzl.length > 0) {
				shbs.zdzl = "";
				List<String> zdzl_qc = new ArrayList<String>();					
				for (int i = 0; i < zdzl.length; i++) {
					if (!zdzl_qc.contains(zdzl[i])) {
						zdzl_qc.add(zdzl[i]);							
					}
				}
				for (int j = 0; j < zdzl_qc.size(); j++) {
					shbs.zdzl = shbs.zdzl + " " + zdzl_qc.get(j);
				}
				
			}
		}
		
		/**
		 * 获取申请人信息
		 */
		String isformatzj = ConfigHelper.getNameByValue("ISFORMATZJ");
		shbs.sqrs = new ArrayList<SHB.SHSQR>();
		List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, "XMBH='"+info.getXmbh()+"' ORDER BY SQRLB DESC,SXH ASC");
		if (sqrlist != null && sqrlist.size() > 0) {
			for (BDCS_SQR sqr : sqrlist) {
				SHSQR shsqr = new SHSQR();
				shsqr.setQlrjs(ConstHelper.getNameByValue("SQRLB", sqr.getSQRLB()));
				shsqr.setQlrmc(sqr.getSQRXM() != null ? sqr.getSQRXM() : "");
				shsqr.setZjmc(ConstHelper.getNameByValue("ZJLX", sqr.getZJLX()));
				if("2".equals(isformatzj)) {
					shsqr.setZjhm(sqr.getZJH() != null ? sqr.getZJH() : "");
				}else {
					shsqr.setZjhm(StringHelper.formatZJ(sqr.getZJH() != null ? sqr.getZJH() : ""));
				}
				shsqr.setGyfs(ConstHelper.getNameByValue("GYFS", sqr.getGYFS()));
				shsqr.setQlfe(sqr.getQLBL() != null ? sqr.getQLBL() : "");
				shbs.sqrs.add(shsqr);
			}
		}
		return shbs;
	}

	/**
	 * 获取实体单元的信息及对应的权利信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年4月4日上午12:04:06
	 * @param shbs
	 * @param info
	 * @param fwzks
	 * @param tdzks
	 */
	@SuppressWarnings("rawtypes")
	private void getUnitInfo(SHB shbs, ProjectInfo info) {
		if (StringHelper.isEmpty(info)) {
			return;
		}
		shbs.setXminfo(info);
		String sqlxmbh = ProjectHelper.GetXMBHCondition(info.getXmbh());
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(info.getXmbh());
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(info.getProject_id());
		if(DJLX.BGDJ.Value.equals(flow.getDjlx())){
			flag_isHaveBG = true;
		}
		ArrayList<TDZK> tdzks = new ArrayList<SHB.TDZK>();
		ArrayList<FWZK> fwzks = new ArrayList<SHB.FWZK>();
		ArrayList<ZRZZK> zrzzks = new ArrayList<SHB.ZRZZK>();
		if (!StringHelper.isEmpty(xmxx)) {
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, sqlxmbh);
			int index=0;
			for (BDCS_DJDY_GZ djdy : djdys) {
				DJDYLY ly = DJDYLY.GZ;
				BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				if (DJDYLY.XZ.Value.equals(djdy.getLY())||DJDYLY.LS.Value.equals(djdy.getLY())) {
					ly = DJDYLY.LS;
				}
				RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					if (!StringHelper.isEmpty(flow)) {
						if (flow.getHandlername().toUpperCase().contains("BGDJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("HZ_SYQ_DYQ_DJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("BGDJEXHandler".toUpperCase())) {
							if (DJDYLY.LS.equals(ly))// 变更前数据
							{
								String zl = StringHelper.FormatByDatatype(unit.getZL());
								/*
								 *广西要求纯土地登记时，不显示现（原）房屋坐落
								 *heks 2017/3/15
								 */
								if(lx.equals("SHYQZD")||lx.equals("SYQZD")||lx.equals("NYD")||lx.equals("LD")||lx.equals("HY")){
									shbs.yfwzl = shbs.yfwzl + " ";
								}else{
									shbs.yfwzl = shbs.yfwzl + " " + zl;
								}
								//shbs.yfwzl = shbs.yfwzl + " " + zl;
								if ("1".equals(xmxx.getSFDB())) // 登簿后（跟权力有个的）
								{
									// 不动产权证号
									String fulSql = "select ql.qlid,ql.BDCQZH from BDCK.BDCS_QL_LS ql left join BDCK.BDCS_FSQL_LS fsql on "
											+ "fsql.qlid=ql.qlid where fsql.zxdyywh='" + xmxx.getPROJECT_ID() + "' "
											+ "and ql.djdyid='" + djdy.getDJDYID()
											+ "' AND ql.QLLX IN ('1','2','3','5','7','24')";
									List<Map> lstql = dao.getDataListByFullSql(fulSql);
									if (!StringHelper.isEmpty(lstql) && lstql.size() > 0) {
										Map m = lstql.get(0);
										String ybdcqzh = StringHelper.FormatByDatatype(m.get("BDCQZH"));
										shbs.zdbdcqzh = shbs.zdbdcqzh + " " + ybdcqzh;
									}
								} else// 登簿前
								{
									List<Rights> qls_zd = RightsTools.loadRightsByCondition(DJDYLY.XZ,
											"DJDYID='" + djdy.getDJDYID()
													+ "' AND QLLX IN ('1','2','3','5','7','24')");
									if (qls_zd != null && qls_zd.size() > 0) {
										String ybdcqzh = StringHelper.FormatByDatatype(qls_zd.get(0).getBDCQZH());
										shbs.zdbdcqzh = shbs.zdbdcqzh + " " + ybdcqzh;
									}
								}	
								UnitInfo(unit, shbs, djdy, ly, lx, tdzks, fwzks, zrzzks,++index);
								if(fwzks!=null&&fwzks.size()>0){
									FWZK fwzkex=fwzks.get(fwzks.size()-1);
									fwzkex.setYxbz("0");
									fwzks.remove(fwzks.size()-1);
									fwzks.add(fwzkex);
								}
								shbs.tdzks = tdzks;
								shbs.fwzks = fwzks;
								shbs.zrzzks = zrzzks;
							} else {
								UnitInfo(unit, shbs, djdy, ly, lx, tdzks, fwzks, zrzzks,++index);
								shbs.tdzks = tdzks;
								shbs.fwzks = fwzks;
								shbs.zrzzks = zrzzks;
							}
						} else if (flow.getHandlername().toUpperCase().contains("GZDJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("HZDJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("ZYDJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("QFZYYGDJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("ZY_DYZXANDQZZY_DJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("ZY_DYZXANDZY_DJHandler".toUpperCase())
								|| flow.getHandlername().toUpperCase().contains("ZY_DYQBG_DJHandler".toUpperCase())){//转移登记——商品房预告登记
						
							if(flow.getHandlername().toUpperCase().contains("ZYDJHandler".toUpperCase())
									|| flow.getHandlername().toUpperCase().contains("QFZYYGDJHandler".toUpperCase())
									|| flow.getHandlername().toUpperCase().contains("ZY_DYZXANDQZZY_DJHandler".toUpperCase())
									|| flow.getHandlername().toUpperCase().contains("ZY_DYZXANDZY_DJHandler".toUpperCase())
									|| flow.getHandlername().toUpperCase().contains("ZY_DYQBG_DJHandler".toUpperCase()) 									
									){
								flag_isHaveZY = true;  // 全局变量flag_isHaveZY，   UnitInfo方法需要，又不能做参数，因为该方法调用过多
							}
							if ("1".equals(xmxx.getSFDB())) // 登簿后（跟权力有个的）
							{
								// 不动产权证号
								RealUnit l_unit = UnitTools.loadUnit(lx, DJDYLY.LS, unit.getId());
								if (!StringHelper.isEmpty(l_unit)) {
									String zl = StringHelper.FormatByDatatype(l_unit.getZL());
									/*
									 *广西要求纯土地登记时，不显示现（原）房屋坐落
									 *heks 2017/3/15
									 */
									if(lx.equals("SHYQZD") || lx.equals("SYQZD")|| lx.equals("LD")|| lx.equals("NYD")|| lx.equals("HY")){
										shbs.yfwzl = shbs.yfwzl + " ";
									}else{
										shbs.yfwzl = shbs.yfwzl + " " + zl;
									}
								
									//shbs.yfwzl = shbs.yfwzl + " " + zl;
								}
								// 获取主体权利
								String fulSql = "select ql.qlid,ql.BDCQZH,ql.LYQLID from BDCK.BDCS_QL_GZ ql  where "
										+ " ql.djdyid='" + djdy.getDJDYID()
										+ "' AND ql.QLLX IN ('1','2','3','5','7','24')";
								List<Map> lstql = dao.getDataListByFullSql(fulSql);
//								if (!StringHelper.isEmpty(lstql) && lstql.size() > 0) {
//									Map m = lstql.get(0);
//									String lyqlid = StringHelper.FormatByDatatype(m.get("LYQLID"));
//									if (!StringHelper.isEmpty(lyqlid)) {
//										Rights right = RightsTools.loadRights(DJDYLY.LS, lyqlid);
//										if (!StringHelper.isEmpty(right)) {
//											String ybdcqzh = StringHelper.FormatByDatatype(right.getBDCQZH());
//											shbs.zdbdcqzh = shbs.zdbdcqzh + " " + ybdcqzh;
//										}
//									}
//								}
								if (!StringHelper.isEmpty(lstql) && lstql.size() > 0) {
									Map m = lstql.get(0);
									String ybdcqzh = StringHelper.FormatByDatatype(m.get("BDCQZH"));
									shbs.zdbdcqzh = shbs.zdbdcqzh + " " + ybdcqzh;
								}
							} else// 登簿前
							{
								List<Rights> qls_zd = RightsTools.loadRightsByCondition(ly, "DJDYID='"
										+ djdy.getDJDYID() + "' AND QLLX IN ('1','2','3','5','7','24')");
								if (qls_zd != null && qls_zd.size() > 0) {
									String ybdcqzh = StringHelper.FormatByDatatype(qls_zd.get(0).getBDCQZH());
									shbs.zdbdcqzh = shbs.zdbdcqzh + " " + ybdcqzh;
								}
								List<RealUnit> lstunit = UnitTools.loadUnits(lx, ly,
										"BDCDYID='" + djdy.getBDCDYID() + "'");
								for (RealUnit realunit : lstunit) {
									String zl = StringHelper.FormatByDatatype(realunit.getZL());
									
									String _bdcdylx = djdy.getBDCDYLX();
									if(_bdcdylx != null ){//宗地的不显示房屋坐落
										if(_bdcdylx.equals("02")){
											shbs.yfwzl = shbs.yfwzl + " ";
										}else{
											shbs.yfwzl = shbs.yfwzl + " " + zl;
										}
									}
									
								}
							}
							UnitInfo(unit, shbs, djdy, ly, lx, tdzks, fwzks, zrzzks,++index);
							shbs.tdzks = tdzks;
							shbs.fwzks = fwzks;
							shbs.zrzzks = zrzzks;
						} else {
							/*
							 * 有两层含义：1、预告登记-房屋所有权登记的比较特殊，单元信息DJDYLY.XZ，
							 * 而权利信息来源于DJDYLY.GZ；
							 * 2、首次登记-房屋所有权登记和预告登记-房屋所有权登记没有原不动产权证号
							 */
							if ((DJLX.YGDJ.Value.equals(xmxx.getDJLX()) || DJLX.CSDJ.Value.equals(xmxx.getDJLX())
									&& !QLLX.DIYQ.Value.equals(xmxx.getQLLX()))
									|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(xmxx.getQLLX())
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(xmxx.getQLLX())
									|| QLLX.ZJDSYQ_FWSYQ.Value.equals(xmxx.getQLLX())) {
								UnitInfo(unit, shbs, djdy, DJDYLY.GZ, lx, tdzks, fwzks, zrzzks,++index);
							} else {
								List<Rights> qls_zd = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"
										+ djdy.getDJDYID() + "' AND QLLX IN ('1','2','3','5','7','24')");
								if (qls_zd != null && qls_zd.size() > 0) {
									String ybdcqzh = StringHelper.FormatByDatatype(qls_zd.get(0).getBDCQZH());
									shbs.zdbdcqzh = shbs.zdbdcqzh + " " + ybdcqzh;
								}
								UnitInfo(unit, shbs, djdy, ly, lx, tdzks, fwzks, zrzzks,++index);
							}

							shbs.tdzks = tdzks;
							shbs.fwzks = fwzks;
							shbs.zrzzks = zrzzks;
						}
					} 
				}
			}
			//宗地坐落去重
			String[] zdzl = shbs.zdzl.split(" ");			
			if (zdzl != null && zdzl.length > 0) {
				shbs.zdzl = "";
				List<String> zdzl_qc = new ArrayList<String>();					
				for (int i = 0; i < zdzl.length; i++) {
					if (!zdzl_qc.contains(zdzl[i])) {
						zdzl_qc.add(zdzl[i]);							
					}
				}
				for (int j = 0; j < zdzl_qc.size(); j++) {
					shbs.zdzl = shbs.zdzl + " " + zdzl_qc.get(j);
				}
				
			}
		}
	}

	/**
	 * 获取具体的单元信息（如：变更后的、更正后等等）
	 * 
	 * @作者 海豹
	 * @创建时间 2016年4月4日上午12:16:41
	 * @param unit
	 * @param shbs
	 * @param djdy
	 * @param ly
	 * @param lx
	 * @param index 
	 */
	private void UnitInfo(RealUnit unit, SHB shbs, BDCS_DJDY_GZ djdy, DJDYLY ly, BDCDYLX lx, ArrayList<TDZK> tdzks,
			ArrayList<FWZK> fwzks, ArrayList<ZRZZK> zrzzks, int index) {
		String zl = StringHelper.FormatByDatatype(unit.getZL());
		String _bdcdylx = djdy.getBDCDYLX();
		/*
		 *广西要求纯土地登记时，不显示现（原）房屋坐落
		 *heks 2017/3/15
		 */
//		if(_bdcdylx.equals("02") || _bdcdylx.equals("01")){
//			shbs.xfwzl = shbs.xfwzl + " ";
//		}else{
//			if (DJDYLY.GZ.equals(ly))// 变更后的坐落才是现房屋坐落
//			shbs.xfwzl = shbs.xfwzl + " " + zl;
//				
//		}
		/*----------------------------------------------------------------------------------*/
		 
	    if(_bdcdylx.equals("02") || _bdcdylx.equals("01")|| _bdcdylx.equals("04")|| _bdcdylx.equals("05")|| _bdcdylx.equals("09")){
			shbs.xfwzl = shbs.xfwzl + " ";
	    }else{
			if(!flag_isHaveZY){ //是否含有ZYDJHandler
				if(DJDYLY.GZ.equals(ly)){
					shbs.xfwzl = (shbs.xfwzl == zl ? shbs.xfwzl : shbs.xfwzl + " " + zl) ; 
				}else if(!flag_isHaveBG){
					shbs.xfwzl = (shbs.xfwzl == zl ? shbs.xfwzl : shbs.xfwzl + " " + zl) ; 
				}
			}else if(!flag_isHaveBG){
				shbs.xfwzl = (shbs.xfwzl==zl ? shbs.xfwzl : shbs.xfwzl + " " + zl) ; 
			}else{
				List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, " XMBH='"+djdy.getXMBH()+"'");
				String sfdb = (xmxx!=null && xmxx.size()>0 ? xmxx.get(0).getSFDB() : "");
				if("0".equals(sfdb)){
					// 转移登记登簿前 ，坐落取工作
					// 四川省某些地方转移登记时候要求变更坐落的值，
			     	// 转移从工作层取坐落，通常工作层坐落和历史层坐落相同
					List<BDCS_H_GZ> h_gz  = dao.getDataList(BDCS_H_GZ.class, " BDCDYID='"+djdy.getBDCDYID()+"'");
			        String zl_gz = (h_gz.size()!=0 ? h_gz.get(0).getZL() : "" ); 
				    if(shbs.xfwzl!=""){
					   shbs.xfwzl = (shbs.xfwzl==zl_gz ? shbs.xfwzl : shbs.xfwzl + " " + zl_gz) ; 
				    }else{
					   shbs.xfwzl =  zl_gz ; 
				    }
				}else{
					shbs.xfwzl = (shbs.xfwzl==zl ? shbs.xfwzl : shbs.xfwzl + " " + zl) ; 
				}
			}
        }
		/*----------------------------------------------------------------------------------*/
		
		//shbs.xfwzl = shbs.xfwzl + " " + zl;
		String jg = "";
		FWZK fwzk = new FWZK();
		if (unit instanceof House) {
			TDZK zk = new TDZK();
			ZRZZK zrzzk = new ZRZZK();
			House house = (House) unit;
			String condition = "  DJDYID ='" + djdy.getDJDYID() + "' AND BDCDYH='" + house.getBDCDYH()
					+ "' AND QLLX IN ('4','6','8') ORDER BY DJSJ DESC NULLS LAST";
			List<Rights> rights_gz = RightsTools.loadRightsByCondition(DJDYLY.GZ, "  DJDYID ='" + djdy.getDJDYID() + "'"
					+ "AND QLLX IN ('4','6','8') "
					+ "AND XMBH='"+shbs.xminfo.getXmbh()+"'");
			if(!StringHelper.isEmpty(rights_gz) && rights_gz.size() > 0){
				Rights right = rights_gz.get(0);
				jg = StringHelper.formatDouble(right.getQDJG());
				boolean bhav_ly=false;
				if(!StringHelper.isEmpty(right.getLYQLID())){
					Rights right_y=RightsTools.loadRights(DJDYLY.LS, right.getLYQLID());
					if(right_y!=null&&right.getQLLX().equals(right_y.getQLLX())){
						String ybdcqzh = StringHelper.FormatByDatatype(right_y.getBDCQZH());
						if(shbs.fwbdcqzh!=""){
							shbs.fwbdcqzh = (shbs.fwbdcqzh==ybdcqzh ? shbs.fwbdcqzh : shbs.fwbdcqzh + " " + ybdcqzh);
						}else{
							shbs.fwbdcqzh = ybdcqzh;
						}
						bhav_ly=true;
					}
				}
				if(!bhav_ly){
					String ybdcqzh = StringHelper.FormatByDatatype(right.getBDCQZH());
					if(shbs.fwbdcqzh!=""){
						shbs.fwbdcqzh = (shbs.fwbdcqzh==ybdcqzh ? shbs.fwbdcqzh : shbs.fwbdcqzh + " " + ybdcqzh);
					}else{
						shbs.fwbdcqzh = ybdcqzh;
					}				
				}
			}else{
				List<Rights> rights = RightsTools.loadRightsByCondition(ly, condition);
				if (!StringHelper.isEmpty(rights) && rights.size() > 0) {
					Rights right = rights.get(0);
					jg = StringHelper.formatDouble(right.getQDJG());
					String ybdcqzh = StringHelper.FormatByDatatype(right.getBDCQZH());
					if(shbs.fwbdcqzh!=""){
						shbs.fwbdcqzh = (shbs.fwbdcqzh==ybdcqzh ? shbs.fwbdcqzh : shbs.fwbdcqzh + " " + ybdcqzh);
					}else{
						shbs.fwbdcqzh = ybdcqzh;
					}				
				}
			}
			
			List<Rights> qls_gz = RightsTools.loadRightsByCondition(DJDYLY.GZ, "  DJDYID ='" + djdy.getDJDYID() + "' AND XMBH='"+shbs.xminfo.getXmbh()+"'");
			
			if(qls_gz!=null&&qls_gz.size()>0){
				boolean bcqdj=false;
				List<String> list_ycqzh=new ArrayList<String>();
				List<String> list_xcqzh=new ArrayList<String>();
				List<String> list_xdyqzh=new ArrayList<String>();
				for(Rights ql_gz:qls_gz){
					if("4".equals(ql_gz.getQLLX())||"6".equals(ql_gz.getQLLX())||"8".equals(ql_gz.getQLLX())){
						bcqdj=true;
						List<RightsHolder> list_qlr_gz=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_gz.getId());
						if(list_qlr_gz!=null&&list_qlr_gz.size()>0){
							for(RightsHolder qlr_gz:list_qlr_gz){
								if(!StringHelper.isEmpty(qlr_gz.getBDCQZH())&&!list_xcqzh.contains(qlr_gz.getBDCQZH())){
									list_xcqzh.add(qlr_gz.getBDCQZH());
								}
							}
						}
						if(!StringHelper.isEmpty(ql_gz.getLYQLID())){
							Rights ql_ls=RightsTools.loadRights(DJDYLY.LS, ql_gz.getLYQLID());
							if(ql_ls!=null&&("4".equals(ql_ls.getQLLX())||"6".equals(ql_ls.getQLLX())||"8".equals(ql_ls.getQLLX()))){
								List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.LS, ql_gz.getLYQLID());
								if(list_qlr_ls!=null&&list_qlr_ls.size()>0){
									for(RightsHolder qlr_ls:list_qlr_ls){
										if(!StringHelper.isEmpty(qlr_ls.getBDCQZH())&&!list_ycqzh.contains(qlr_ls.getBDCQZH())){
											list_ycqzh.add(qlr_ls.getBDCQZH());
										}
									}
								}
							}
						}
					}else if("23".equals(ql_gz.getQLLX())){
						List<RightsHolder> list_qlr_gz=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_gz.getId());
						if(list_qlr_gz!=null&&list_qlr_gz.size()>0){
							for(RightsHolder qlr_gz:list_qlr_gz){
								if(!StringHelper.isEmpty(qlr_gz.getBDCQZH())&&!list_xdyqzh.contains(qlr_gz.getBDCQZH())){
									list_xdyqzh.add(qlr_gz.getBDCQZH());
								}
							}
						}
					}
				}
				if(!bcqdj){
					List<Rights> list_ql_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "  DJDYID ='" + djdy.getDJDYID() + "'");
					if(list_ql_xz!=null&&list_ql_xz.size()>0){
						for(Rights ql_xz:list_ql_xz){
							if(ql_xz!=null&&("1".equals(ql_xz.getQLLX())||"2".equals(ql_xz.getQLLX())||"3".equals(ql_xz.getQLLX())||"5".equals(ql_xz.getQLLX())||"7".equals(ql_xz.getQLLX())||"4".equals(ql_xz.getQLLX())||"6".equals(ql_xz.getQLLX())||"8".equals(ql_xz.getQLLX()))){
								List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql_xz.getId());
								if(list_qlr_ls!=null&&list_qlr_ls.size()>0){
									for(RightsHolder qlr_ls:list_qlr_ls){
										if(!StringHelper.isEmpty(qlr_ls.getBDCQZH())&&!list_xcqzh.contains(qlr_ls.getBDCQZH())){
											list_xcqzh.add(qlr_ls.getBDCQZH());
										}
									}
								}
							}
						}
					}
				}
				List<String> list_xfwcqzh=shbs.getList_xfwbdcqzh();
				List<String> list_yfwcqzh=shbs.getList_yfwbdcqzh();
				List<String> list_xfwdyqzh=shbs.getList_xfwdybdcqzh();
				for(String qzh:list_xcqzh){
					if(!list_xfwcqzh.contains(qzh)){
						list_xfwcqzh.add(qzh);
					}
				}
				for(String qzh:list_ycqzh){
					if(!list_yfwcqzh.contains(qzh)){
						list_yfwcqzh.add(qzh);
					}
				}
				for(String qzh:list_xdyqzh){
					if(!list_xfwdyqzh.contains(qzh)){
						list_xfwdyqzh.add(qzh);
					}
				}
				shbs.setList_xfwbdcqzh(list_xfwcqzh);
				shbs.setList_yfwbdcqzh(list_yfwcqzh);
				shbs.setList_xfwdybdcqzh(list_xfwdyqzh);
				shbs.setXfwbdcqzh(StringHelper.formatList(list_xfwcqzh, "、"));
				shbs.setYfwbdcqzh(StringHelper.formatList(list_yfwcqzh, "、"));
				shbs.setXfwdybdcqzh(StringHelper.formatList(list_xfwdyqzh, "、"));
			}
			fwzk.index = index;
			fwzk.zrzh = house.getZRZH();
			fwzk.fh = StringHelper.FormatByDatatype(house.getFH());// 房号
			fwzk.jzmj = StringHelper.formatDouble(house.getMJ());// 建筑面积
			if (BDCDYLX.YCH.equals(lx)) {
				fwzk.zyjzmj = StringHelper.formatDouble(house.getYCTNJZMJ());// 专有建筑面积
				fwzk.ftmj = StringHelper.formatDouble(house.getYCFTJZMJ());// 分摊面积
			} else {
				fwzk.zyjzmj = StringHelper.formatDouble(house.getSCTNJZMJ());
				fwzk.ftmj = StringHelper.formatDouble(house.getSCFTJZMJ());
			}
			fwzk.zcs = StringHelper.formatDouble(house.getZCS());// 总层数
			fwzk.szcs = StringHelper.FormatByDatatype(house.getSZC());// 所在层
			fwzk.cjzj = jg;// 成交总价(ql)
			fwzk.jzjg = ConstHelper.getNameByValue("FWJG", house.getFWJG());// 建筑结构
			fwzk.ghyt = ConstHelper.getNameByValue("FWYT", house.getGHYT());// 规划用途
			fwzk.fwxz = ConstHelper.getNameByValue("FWXZ", house.getFWXZ());// 房屋性质
			fwzk.cb = ConstHelper.getNameByValue("FWCB", house.getFWCB());// 产别
			fwzk.ljqpjyc = "否";// 临街且平街一层
			fwzk.bdcdyh = djdy.getBDCDYH();// 产别
			fwzk.xfwzl=house.getZL();
			fwzk.fwbm = StringHelper.FormatByDatatype(house.getFWBM());
			String qsrq = StringHelper.FormatByDatetime(house.getTDSYQQSRQ()) + "至";
			String zzrq = StringHelper.FormatByDatetime(house.getTDSYQZZRQ());
			if (qsrq != null && qsrq != "" && zzrq != null && zzrq != "") {
				fwzk.syqx = qsrq + zzrq;
			}

			if (SF.YES.Value.equals(StringHelper.formatObject(house.getSFLJQPJYC()))) {
				fwzk.ljqpjyc = "是";
			}
			fwzk.qsc=StringHelper.FormatByDatatype(house.getQSC());
			fwzk.dyh=StringHelper.FormatByDatatype(house.getDYH());
			fwzks.add(fwzk);
			// 对应的自然幢情况信息
			String zrzbdcdtid = house.getZRZBDCDYID();
			BDCDYLX zrzlx = BDCDYLX.ZRZ;// 默认
			if (!StringHelper.isEmpty(zrzbdcdtid)) {
				List<BDCS_ZRZ_XZ> zrzlist = dao.getDataList(BDCS_ZRZ_XZ.class, "BDCDYID='" + zrzbdcdtid + "'");
				if (zrzlist.size() > 0) {
					BDCS_ZRZ_XZ zrz = zrzlist.get(0);
					if (!StringHelper.isEmpty(zrz)) {
						if (!StringHelper.isEmpty(zrz.getZRZH())) {
							zrzzk.zh = zrz.getZRZH();
						}
						if (!StringHelper.isEmpty(zrz.getFWJGName())) {
							zrzzk.jg = zrz.getFWJGName();
						}
						if (!StringHelper.isEmpty(zrz.getJGRQ())) {
							zrzzk.jcnd = StringHelper.FormatByDatetime(zrz.getJGRQ());
						}
						if (!StringHelper.isEmpty(zrz.getDXCS())) {
							zrzzk.dx = StringHelper.formatDouble(zrz.getDXCS());
						}
						if (!StringHelper.isEmpty(zrz.getDSCS())) {
							zrzzk.ds = StringHelper.formatDouble(zrz.getDSCS());
						}

						if (!StringHelper.isEmpty(zrz.getJZWJBYT())) {
							zrzzk.yt = zrz.getJZWJBYT();
						}
						if (!StringHelper.isEmpty(zrz.getZTS())) {
							zrzzk.tshjs = StringHelper.formatDouble(zrz.getZTS());
						}
						if (!StringHelper.isEmpty(zrz.getSCJZMJ())) {
							zrzzk.jzmj = StringHelper.formatDouble(zrz.getSCJZMJ());
						}

						if (!StringHelper.isEmpty(zrz.getFTTDMJ())) {
							zrzzk.ftmj = StringHelper.formatDouble(zrz.getFTTDMJ());
						}

						if (!StringHelper.isEmpty(zrz.getBZ())) {
							zrzzk.sh = zrz.getBZ();
						}

					}
				}
			}
			zrzzks.add(zrzzk);
			// 对应的土地状况信息
			String zdbdcdyid = house.getZDBDCDYID();
			String qlxz = house.getQLXZ();
			BDCDYLX zdlx = BDCDYLX.SHYQZD;// 默认
			if (!StringHelper.isEmpty(zdbdcdyid)) {
				UseLand land = (UseLand) UnitTools.loadUnit(zdlx, DJDYLY.XZ, zdbdcdyid);
				if (!StringHelper.isEmpty(land)) {
					zk.zdbm = StringHelper.formatObject(land.getZDDM());// 宗地代码
					zk.index=index;
					shbs.zdzl = shbs.zdzl + " " +  (land.getZL() != null ? land.getZL() : "");
					zk.zdmj = StringHelper.formatDouble(land.getZDMJ());// 宗地面积
					zk.tdftmj = StringHelper.formatDouble(house.getFTTDMJ());// 土地分摊面积
					zk.crqznx = ""; // 出让起止年限
					qlxz = land.getQLXZ();
					if (!("101".equals(qlxz)))// 划拨不存在起止时间
					{
						//要考虑到首登问题，上面查的是XZ层，TDYT表的数据是null，那么在获取下标值会 报错
						if (land.getTDYTS().size()>0) {//取土用途的起始时间、终止时间
							if(!StringHelper.isEmpty(land.getTDYTS().get(0).getQSRQ())){
								zk.crqznx = StringHelper.FormatByDatetime(land.getTDYTS().get(0).getQSRQ()) + "至";
							}
							if (!StringHelper.isEmpty(land.getTDYTS().get(0).getZZRQ())) {
								zk.crqznx = zk.crqznx + StringHelper.FormatByDatetime(land.getTDYTS().get(0).getZZRQ());
							}
						}
					}
					zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX()); // 权利类型
					zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ()); // 权利性质
					zk.qdjg = jg;// 缺取得价格(QL)
					zk.zdyt = ConstHelper.getNameByValue("TDYT", house.getFWTDYT());
					zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());// 权利设定方式
					List<TDYT> tdyts = land.getTDYTS();
					for (TDYT tdyt : tdyts) {
						if (!StringHelper.isEmpty(tdyt.getTDYT()) && tdyt.getTDYT().equals(house.getFWTDYT())) {
							zk.crje = tdyt.getCRJBZ();
						}
					}
					List<BDCS_DJDY_XZ> djdys_zd = dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + land.getId() + "'");
					if (djdys_zd != null && djdys_zd.size() > 0) {
						List<Rights> qls_zd = RightsTools.loadRightsByCondition(DJDYLY.XZ,
								"DJDYID='" + djdys_zd.get(0).getDJDYID() + "' AND QLLX IN ('1','2','3','5','7','24')");
						if (!StringHelper.isEmpty(qls_zd) && qls_zd.size() > 0) {
							String zdbdcqzh = StringHelper.FormatByDatatype(qls_zd.get(0).getBDCQZH());
							shbs.zdbdcqzh = shbs.zdbdcqzh + " " + zdbdcqzh;
						}
					}
				}
			}
			tdzks.add(zk);
		} else if (unit instanceof UseLand) {
			UseLand land = (UseLand) unit;
			shbs.zdzl = shbs.zdzl + " " +  (land.getZL() != null ? land.getZL() : "");
			String condition = " DJDYID ='" + djdy.getDJDYID() + "' AND BDCDYH='" + land.getBDCDYH()
					+ "'AND QLLX IN ('1','2','3','5','7','24')";
			List<Rights> rights = RightsTools.loadRightsByCondition(ly, condition);
			if (!StringHelper.isEmpty(rights) && rights.size() > 0) {
				Rights right = rights.get(0);
				jg = StringHelper.formatDouble(right.getQDJG());
			}
			List<TDYT> list = land.getTDYTS();
			for (TDYT tdyt : list) {
				TDZK zk = new TDZK();
				zk.bdcdyh = land.getBDCDYH();
				zk.zdbm = StringHelper.formatObject(land.getZDDM());// 宗地代码
				zk.zdmj = StringHelper.formatDouble(land.getZDMJ());// 宗地面积
				zk.tdftmj = StringHelper.formatDouble(land.getJZMJ());// 土地分摊面积
				String qlxz = land.getQLXZ();
				zk.crqznx = "";// 出让起止年限
				if (!("101".equals(qlxz)))// 划拨不存在起止时间
				{
					if (!StringHelper.isEmpty(tdyt.getQSRQ())) {
						zk.crqznx = StringHelper.FormatByDatetime(tdyt.getQSRQ()) + "至";
					}
					if (!StringHelper.isEmpty(tdyt.getZZRQ())) {
						zk.crqznx = zk.crqznx + StringHelper.FormatByDatetime(tdyt.getZZRQ());
					}
				}
				if (!StringHelper.isEmpty(tdyt.getTDYT())) {
					zk.crje = tdyt.getCRJBZ();
				}
				zk.qdjg = jg;
				zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX());
				zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ());
				zk.zdyt = ConstHelper.getNameByValue("TDYT", tdyt.getTDYT());
				zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());
				tdzks.add(zk);
			}

		}
		else if (unit instanceof AgriculturalLand) {//农用地状况
			AgriculturalLand land = (AgriculturalLand) unit;
			shbs.zdzl = shbs.zdzl + " " +  (land.getZL() != null ? land.getZL() : "");
			String condition = " DJDYID ='" + djdy.getDJDYID() + "' AND BDCDYH='" + land.getBDCDYH()
					+ "'AND QLLX IN ('1','2','3','5','7','24')";
			List<Rights> rights = RightsTools.loadRightsByCondition(ly, condition);
			if (!StringHelper.isEmpty(rights) && rights.size() > 0) {
				Rights right = rights.get(0);
				jg = StringHelper.formatDouble(right.getQDJG());
			}
			List<TDYT> list = land.getTDYTS();
			for (TDYT tdyt : list) {
				TDZK zk = new TDZK();
				zk.bdcdyh = land.getBDCDYH();
				zk.zdbm = StringHelper.formatObject(land.getZDDM());// 宗地代码
				zk.zdmj = StringHelper.formatDouble(land.getMJ());// 宗地面积
				//zk.tdftmj = StringHelper.formatDouble(land.getJZMJ());// 土地分摊面积
				String qlxz = land.getQLXZ();
				zk.crqznx = "";// 出让起止年限
				if (!("101".equals(qlxz)))// 划拨不存在起止时间
				{
					if (!StringHelper.isEmpty(tdyt.getQSRQ())) {
						zk.crqznx = StringHelper.FormatByDatetime(tdyt.getQSRQ()) + "至";
					}
					if (!StringHelper.isEmpty(tdyt.getZZRQ())) {
						zk.crqznx = zk.crqznx + StringHelper.FormatByDatetime(tdyt.getZZRQ());
					}
				}
				if (!StringHelper.isEmpty(tdyt.getTDYT())) {
					zk.crje = tdyt.getCRJBZ();
				}
				zk.qdjg = jg;
				zk.qllx = ConstHelper.getNameByValue("QLLX", land.getQLLX());
				zk.qlxz = ConstHelper.getNameByValue("QLXZ", land.getQLXZ());
				zk.zdyt = ConstHelper.getNameByValue("TDYT", tdyt.getTDYT());
				//zk.qlsdms = ConstHelper.getNameByValue("QLSDFS", land.getQLSDFS());
				tdzks.add(zk);
			}

		}
		else // 其它
		{

		}
	}
	
	/**
	 * 获取具体的单元信息（如：变更后的、更正后等等）
	 * 
	 * @作者 海豹
	 * @创建时间 2016年4月4日上午12:16:41
	 * @param unit
	 * @param shbs
	 * @param djdy
	 * @param ly
	 * @param lx
	 */
	private void UnitInfoEX(RealUnit unit, SHB shbs, BDCS_XM_DYXZ djdy, DJDYLY ly, BDCDYLX lx, ArrayList<TDZK> tdzks,
			ArrayList<FWZK> fwzks, ArrayList<ZRZZK> zrzzks) {
		String zl = StringHelper.FormatByDatatype(unit.getZL());
		/*
		 *广西要求纯土地登记时，不显示现（原）房屋坐落
		 *heks 2017/3/15
		 */
		String _bdcdylx = djdy.getBDCDYLX();
		if(_bdcdylx.equals("02") || _bdcdylx.equals("01")|| _bdcdylx.equals("09")|| _bdcdylx.equals("04")|| _bdcdylx.equals("05")){
			shbs.xfwzl = shbs.xfwzl + " ";
		}else{
			shbs.xfwzl = shbs.xfwzl + " " + zl;
		}
		//shbs.xfwzl = shbs.xfwzl + " " + zl;
		String jg = "";
		FWZK fwzk = new FWZK();
		if (unit instanceof House) {
			ZRZZK zrzzk = new ZRZZK();
			House house = (House) unit;
			fwzk.fh = StringHelper.FormatByDatatype(house.getFH());// 房号
			fwzk.jzmj = StringHelper.formatDouble(house.getMJ());// 建筑面积
			if (BDCDYLX.YCH.equals(lx)) {
				fwzk.zyjzmj = StringHelper.formatDouble(house.getYCTNJZMJ());// 专有建筑面积
				fwzk.ftmj = StringHelper.formatDouble(house.getYCFTJZMJ());// 分摊面积
			} else {
				fwzk.zyjzmj = StringHelper.formatDouble(house.getSCTNJZMJ());
				fwzk.ftmj = StringHelper.formatDouble(house.getSCFTJZMJ());
			}
			
			fwzk.zcs = StringHelper.formatDouble(house.getZCS());// 总层数
			fwzk.szcs = StringHelper.FormatByDatatype(house.getSZC());// 所在层
			fwzk.cjzj = jg;// 成交总价(ql)
			fwzk.jzjg = ConstHelper.getNameByValue("FWJG", house.getFWJG());// 建筑结构
			fwzk.ghyt = ConstHelper.getNameByValue("FWYT", house.getGHYT());// 规划用途
			fwzk.fwxz = ConstHelper.getNameByValue("FWXZ", house.getFWXZ());// 房屋性质
			fwzk.cb = ConstHelper.getNameByValue("FWCB", house.getFWCB());// 产别
			fwzk.ljqpjyc = "否";// 临街且平街一层
			fwzk.bdcdyh = house.getBDCDYH();// 产别
			String qsrq = StringHelper.FormatByDatetime(house.getTDSYQQSRQ()) + "至";
			String zzrq = StringHelper.FormatByDatetime(house.getTDSYQZZRQ());
			if (qsrq != null && qsrq != "" && zzrq != null && zzrq != "") {
				fwzk.syqx = qsrq + zzrq;
			}

			if (SF.YES.Value.equals(StringHelper.formatObject(house.getSFLJQPJYC()))) {
				fwzk.ljqpjyc = "是";
			}
			fwzk.qsc=StringHelper.FormatByDatatype(house.getQSC());
			fwzk.dyh=StringHelper.FormatByDatatype(house.getDYH());
			fwzk.fwbm = StringHelper.FormatByDatatype(house.getFWBM());
			fwzks.add(fwzk);
			// 对应的自然幢情况信息
			String zrzbdcdtid = house.getZRZBDCDYID();
			BDCDYLX zrzlx = BDCDYLX.ZRZ;// 默认
			if (!StringHelper.isEmpty(zrzbdcdtid)) {
				List<BDCS_ZRZ_XZ> zrzlist = dao.getDataList(BDCS_ZRZ_XZ.class, "BDCDYID='" + zrzbdcdtid + "'");
				if (zrzlist.size() > 0) {
					BDCS_ZRZ_XZ zrz = zrzlist.get(0);
					if (!StringHelper.isEmpty(zrz)) {
						if (!StringHelper.isEmpty(zrz.getZRZH())) {
							zrzzk.zh = zrz.getZRZH();
						}
						if (!StringHelper.isEmpty(zrz.getFWJGName())) {
							zrzzk.jg = zrz.getFWJGName();
						}
						if (!StringHelper.isEmpty(zrz.getJGRQ())) {
							zrzzk.jcnd = StringHelper.FormatByDatetime(zrz.getJGRQ());
						}
						if (!StringHelper.isEmpty(zrz.getDXCS())) {
							zrzzk.dx = StringHelper.formatDouble(zrz.getDXCS());
						}
						if (!StringHelper.isEmpty(zrz.getDSCS())) {
							zrzzk.ds = StringHelper.formatDouble(zrz.getDSCS());
						}

						if (!StringHelper.isEmpty(zrz.getJZWJBYT())) {
							zrzzk.yt = zrz.getJZWJBYT();
						}
						if (!StringHelper.isEmpty(zrz.getZTS())) {
							zrzzk.tshjs = StringHelper.formatDouble(zrz.getZTS());
						}
						if (!StringHelper.isEmpty(zrz.getSCJZMJ())) {
							zrzzk.jzmj = StringHelper.formatDouble(zrz.getSCJZMJ());
						}

						if (!StringHelper.isEmpty(zrz.getFTTDMJ())) {
							zrzzk.ftmj = StringHelper.formatDouble(zrz.getFTTDMJ());
						}

						if (!StringHelper.isEmpty(zrz.getBZ())) {
							zrzzk.sh = zrz.getBZ();
						}

					}
				}
			}
			zrzzks.add(zrzzk);
			
			List<String> list_ycqzh=new ArrayList<String>();
			List<String> list_xcqzh=new ArrayList<String>();
			List<String> list_xdyqzh=new ArrayList<String>();
			List<Rights> list_ql_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "  DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYLX IN ('031','032') AND BDCDYID='"+unit.getId()+"')");
			if(list_ql_xz!=null&&list_ql_xz.size()>0){
				for(Rights ql_xz:list_ql_xz){
					if(ql_xz!=null&&("4".equals(ql_xz.getQLLX())||"6".equals(ql_xz.getQLLX())||"8".equals(ql_xz.getQLLX()))){
						List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql_xz.getId());
						if(list_qlr_ls!=null&&list_qlr_ls.size()>0){
							for(RightsHolder qlr_ls:list_qlr_ls){
								if(!StringHelper.isEmpty(qlr_ls.getBDCQZH())&&!list_xcqzh.contains(qlr_ls.getBDCQZH())){
									list_xcqzh.add(qlr_ls.getBDCQZH());
								}
							}
						}
					}
				}
			}
			List<String> list_xfwcqzh=shbs.getList_xfwbdcqzh();
			List<String> list_yfwcqzh=shbs.getList_yfwbdcqzh();
			List<String> list_xfwdyqzh=shbs.getList_xfwdybdcqzh();
			for(String qzh:list_xcqzh){
				if(!list_xfwcqzh.contains(qzh)){
					list_xfwcqzh.add(qzh);
				}
			}
			for(String qzh:list_ycqzh){
				if(!list_yfwcqzh.contains(qzh)){
					list_yfwcqzh.add(qzh);
				}
			}
			for(String qzh:list_xdyqzh){
				if(!list_xfwdyqzh.contains(qzh)){
					list_xfwdyqzh.add(qzh);
				}
			}
			shbs.setList_xfwbdcqzh(list_xfwcqzh);
			shbs.setList_yfwbdcqzh(list_yfwcqzh);
			shbs.setList_xfwdybdcqzh(list_xfwdyqzh);
			shbs.setXfwbdcqzh(StringHelper.formatList(list_xfwcqzh, "、"));
			shbs.setYfwbdcqzh(StringHelper.formatList(list_yfwcqzh, "、"));
			shbs.setXfwdybdcqzh(StringHelper.formatList(list_xfwdyqzh, "、"));
		}
	}

	public String getXfwzl() {
		return xfwzl;
	}

	public void setXfwzl(String xfwzl) {
		this.xfwzl = xfwzl;
	}

	public String getYfwzl() {
		return yfwzl;
	}

	public void setYfwzl(String yfwzl) {
		this.yfwzl = yfwzl;
	}

	public String getFwbdcqzh() {
		return fwbdcqzh;
	}

	public void setFwbdcqzh(String fwbdcqzh) {
		this.fwbdcqzh = fwbdcqzh;
	}

	public String getZdzl() {
		return zdzl;
	}

	public void setZdzl(String zdzl) {
		this.zdzl = zdzl;
	}

	public String getQzfjnr() {
		return qzfjnr;
	}

	public void setQzfjnr(String qzfjnr) {
		this.qzfjnr = qzfjnr;
	}

	public String getSmlnr() {
		return smlnr;
	}

	public void setSmlnr(String smlnr) {
		this.smlnr = smlnr;
	}

	public List<FWZK> getFwzks() {
		return fwzks;
	}

	public void setFwzks(List<FWZK> fwzks) {
		this.fwzks = fwzks;
	}

	public List<TDZK> getTdzks() {
		return tdzks;
	}

	public void setTdzks(List<TDZK> tdzks) {
		this.tdzks = tdzks;
	}

	public List<SHSQR> getSqrs() {
		return sqrs;
	}

	public void setSqrs(List<SHSQR> sqrs) {
		this.sqrs = sqrs;
	}
}