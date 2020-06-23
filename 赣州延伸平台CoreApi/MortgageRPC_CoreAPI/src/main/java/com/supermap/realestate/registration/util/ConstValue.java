package com.supermap.realestate.registration.util;

import org.springframework.context.annotation.Description;

/**
 * 
 * @Description:枚举
 * @author liusf
 * @date 2015年6月12日 上午11:53:45
 * @Copyright SuperMap
 */
public class ConstValue {

	/**
	 * 登记类型枚举
	 * 
	 * @author liusf
	 */
	@Description(value = "登记类型枚举")
	public enum DJLX {

		/** 初始登记(100) */
		CSDJ("100", "首次登记"),

		/** 转移登记(200) */
		ZYDJ("200", "转移登记"),

		/** 变更登记(300) */
		BGDJ("300", "变更登记"),

		/** 注登记(400) */
		ZXDJ("400", "注销登记"),

		/** 更正登记(500) */
		GZDJ("500", "更正登记"),

		/** 异议登记(600) */
		YYDJ("600", "异议登记"),

		/** 预告登记(700) */
		YGDJ("700", "预告登记"),

		/** 查封登记(800) */
		CFDJ("800", "查封登记"),

		/** 其他登记(900) */
		QTDJ("900", "其他登记");

		public String Value;

		public String Name;

		private DJLX(String value) {
			this.Value = value;
		}

		private DJLX(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static DJLX initFrom(String value) {
			if (value == null) {
				return null;
			}
			for (DJLX examType : DJLX.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}

	/**
	 * 预告登记类型枚举
	 * 
	 * @author 李想
	 */
	@Description(value = "预告登记类型枚举")
	public enum YGDJLX {

		/** 预售商品房买卖预告登记(1) */
		YSSPFMMYGDJ(1, "预售商品房买卖预告登记"),

		/** 其它不动产买卖预告登记(2) */
		QTBDCMMYGDJ(2, "其它不动产买卖预告登记"),

		/** 预售商品房抵押权预告登记(3) */
		YSSPFDYQYGDJ(3, "预售商品房抵押权预告登记"),

		/** 其它不动产抵押权预告登记(4) */
		ZQTBDCDYQYGDJ(4, "其它不动产抵押权预告登记");

		public Integer Value;

		public String Name;

		private YGDJLX(Integer value) {
			this.Value = value;
			this.Name=ConstHelper.getNameByValue("YGDJZL", StringHelper.formatObject(value));
		}

		private YGDJLX(Integer value, String name) {
			this.Value = value;
			//this.Name = name;
			this.Name=ConstHelper.getNameByValue("YGDJZL", StringHelper.formatObject(value));
		}

		public static YGDJLX initFrom(Integer value) {
			if (value == null) {
				return null;
			}
			for (YGDJLX examType : YGDJLX.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}

	/**
	 * 权利类型枚举
	 * 
	 * @author liusf
	 */
	@Description(value = "权利类型枚举")
	public enum QLLX {

		/** 集体土地所有权 */
		JTTDSYQ("1", "集体土地所有权"),

		/** 国家土地所有权 */
		GJTDSYQ("2", "国家土地所有权"),

		/** 国有建设用地使用权 */
		GYJSYDSHYQ("3", "国有建设用地使用权"),

		/** 国有建设用地使用权/房屋（构筑物）所有权 */
		GYJSYDSHYQ_FWSYQ("4", "国有建设用地使用权/房屋（构筑物）所有权"),

		/** 宅基地使用权 */
		ZJDSYQ("5", "宅基地使用权"),

		/** 宅基地使用权/房屋（构筑物）所有权 */
		ZJDSYQ_FWSYQ("6", "宅基地使用权/房屋（构筑物）所有权"),

		/** 集体建设用地使用权 */
		JTJSYDSYQ("7", "集体建设用地使用权"),

		/** 集体建设用地使用权/房屋（构筑物）所有权 */
		JTJSYDSYQ_FWSYQ("8", "集体建设用地使用权/房屋（构筑物）所有权"),

		/** 土地承包经营权 */
		TDCBJYQ("9", "土地承包经营权"),

		/** 土地承包经营权/森林、林木所有权 */
		TDCBJYQ_SLLMSYQ("10", "土地承包经营权/森林、林木所有权"),

		/** 林地使用权 */
		LDSYQ("11", "林地使用权"),

		/** 林地使用权/森林、林木使用权 */
		LDSYQ_SLLMSYQ("12", "林地使用权/森林、林木使用权"),

		/** 草原使用权 */
		CYSQY("13", "草原使用权"),

		/** 水域滩涂养殖权 */
		SYTTYZQ("14", "水域滩涂养殖权"),

		/** 海域使用权 */
		HYSYQ("15", "海域使用权"),

		/** 海域使用权/构（建）筑物所有权 */
		HYSYQ_GZWSYQ("16", "海域使用权/构（建）筑物所有权"),

		/** 无居民海岛使用权 */
		WJMHDSYQ("17", "无居民海岛使用权"),

		/** 无居民海岛使用权/构（建）筑物所有权 */
		WJMHDSYQ_GZWSYQ("18", "无居民海岛使用权/构（建）筑物所有权"),

		/** 地役权 */
		DYQ("19", "地役权"),

		/** 取水权 */
		QSQ("20", "取水权"),

		/** 探矿权 */
		TKQ("21", "探矿权"),

		/** 采矿权 */
		CKQ("22", "采矿权"),

		/** 抵押权 */
		DIYQ("23", "抵押权"),

		/** 国有农用地使用权 */
		GYNYDSHYQ("24", "国有农用地使用权"),

		/** 林地使用权/森林、林木所有权 */
		LDSHYQ_SLLMSYQ("36", "林地使用权/森林、林木所有权"),

		/** 查封登记注销 */
		CFZX("98", "查封登记注销"),

		/** 其它权利 */
		QTQL("99", "其它权利"),
		
		/** 国有建设用地使用权/构筑物所有权 */
		GYGZWSYQ("28", "国有建设用地使用权/构筑物所有权"),
		
		/** 宅基地使用权/构筑物所有权 */
		ZJDGZWSYQ("29", "宅基地使用权/构筑物所有权"),
		
		/** 集体建设用地使用权/构筑物所有权 */
		JTGZWSYQ("30", "集体建设用地使用权/构筑物所有权");

		public String Value;

		public String Name;

		private QLLX(String value) {
			this.Value = value;
		}

		private QLLX(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static QLLX initFrom(String value) {
			if (value == null) {
				return null;
			}
			for (QLLX examType : QLLX.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}

	/**
	 * 不动产单元类型枚举
	 * 
	 * @author liusf
	 */
	@Description(value = "不动产单元类型枚举")
	public enum BDCDYLX {

		/**
		 * 所有权宗地
		 */
		SYQZD("01", "所有权宗地", "BDCS_SYQZD_GZ", "BDCS_SYQZD_XZ", "BDCS_SYQZD_LS", "DCS_SYQZD_GZ", "土地"),

		/**
		 * 使用权宗地
		 */
		SHYQZD("02", "使用权宗地", "BDCS_SHYQZD_GZ", "BDCS_SHYQZD_XZ", "BDCS_SHYQZD_LS", "DCS_SHYQZD_GZ", "土地"),

		/**
		 * 自然幢
		 */
		ZRZ("03", "自然幢", "BDCS_ZRZ_GZ", "BDCS_ZRZ_XZ", "BDCS_ZRZ_LS", "DCS_ZRZ_GZ", "房屋"),

		/**
		 * 预测自然幢
		 */
		YCZRZ("08", "预测自然幢", "BDCS_ZRZ_GZY", "BDCS_ZRZ_XZY", "BDCS_ZRZ_LSY", "DCS_ZRZ_GZY", "房屋"),

		/** 海域 */
		HY("04", "海域", "BDCS_ZH_GZ", "BDCS_ZH_XZ", "BDCS_ZH_LS", "DCS_ZH_GZ", "海域"),

		/** 林地 */
		LD("05", "林地", "BDCS_SLLM_GZ", "BDCS_SLLM_XZ", "BDCS_SLLM_LS", "DCS_SLLM_GZ", "林地"),

		/** 构筑物 */
		GZW("10", "构筑物", "BDCS_GZW_GZ", "BDCS_GZW_XZ", "BDCS_GZW_LS", "DCS_GZW_GZ", "构筑物"),

		/**
		 * 其他定着物
		 */
		QTDZW("07", "其他定着物", "BDCS_QTDZW_GZ", "BDCS_QTDZW_XZ", "BDCS_QTDZW_LS", "", "其他定着物"),

		/**
		 * 点状定着物
		 */
		DZDZW("071", "点状定着物", "BDCS_DZDZW_GZ", "BDCS_DZDZW_XZ", "BDCS_DZDZW_LS", "", "点状定着物"),

		/**
		 * 线状定着物
		 */
		XZDZW("072", "线状定着物", "BDCS_XZDZW_GZ", "BDCS_XZDZW_XZ", "BDCS_XZDZW_LS", "", "线状定着物"),

		/**
		 * 面状定着物
		 */
		MZDZW("073", "面状定着物", "BDCS_MZDZW_GZ", "BDCS_MZDZW_XZ", "BDCS_MZDZW_LS", "", "面状定着物"),

		/** 户 */
		H("031", "户", "BDCS_H_GZ", "BDCS_H_XZ", "BDCS_H_LS", "DCS_H_GZ", "房屋"),

		/** 预测户 */
		YCH("032", "预测户", "BDCS_H_GZY", "BDCS_H_XZY", "BDCS_H_LSY", "DCS_H_GZY", "房屋"),

		/** 农用地 */
		NYD("09", "农用地", "BDCS_NYD_GZ", "BDCS_NYD_XZ", "BDCS_NYD_LS", "DCS_NYD_GZ", "土地"),
		
		/** 逻辑幢 */
		LJZ("20", "逻辑幢", "BDCS_LJZ_GZ", "BDCS_LJZ_XZ", "BDCS_LJZ_LS", "DCS_LJZ_GZ", "逻辑幢"),
		
		/** 预测逻辑幢 */
		YCLJZ("21", "预测逻辑幢", "BDCS_LJZ_GZY", "BDCS_LJZ_XZY", "BDCS_LJZ_LSY", "DCS_LJZ_GZY", "预测逻辑幢");

		/** 工作表名 */
		public String GZTableName;

		/** 现状表名 */
		public String XZTableName;

		/** 历史表名 */
		public String LSTableName;

		/** 调查表名 */
		public String DCTableName;

		public String Value;

		public String Name;

		public String Bdclx;

		private BDCDYLX(String value) {
			this.Value = value;
		}

		private BDCDYLX(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		private BDCDYLX(String value, String name, String gztablename, String xztablename, String lstablename,
				String dctablename, String bdclx) {
			this.Value = value;
			this.Name = name;
			this.GZTableName = gztablename;
			this.XZTableName = xztablename;
			this.LSTableName = lstablename;
			this.DCTableName = dctablename;
			this.Bdclx = bdclx;
		}

		public static BDCDYLX initFrom(String value) {
			if (value == null) {
				return null;
			}
			for (BDCDYLX examType : BDCDYLX.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}

		public static BDCDYLX initFromByEnumName(String name) {
			if (name == null) {
				return null;
			}
			for (BDCDYLX examType : BDCDYLX.values()) {
				if (name.equals(examType.name())) {
					return examType;
				}
			}
			return null;
		}

		/**
		 * 根据来源返回表名
		 * 
		 * @Title: getTableName
		 * @author:liushufeng
		 * @date：2015年7月11日 下午11:04:40
		 * @param ly
		 *            登记单元来源
		 * @return
		 */
		public String getTableName(DJDYLY ly) {
			String str = "";
			if (ly.equals(DJDYLY.GZ)) {
				str = this.GZTableName;
			} else if (ly.equals(DJDYLY.XZ)) {
				str = this.XZTableName;
			} else if (ly.equals(DJDYLY.LS)) {
				str = this.LSTableName;
			} else if (ly.equals(DJDYLY.DC)) {
				str = this.DCTableName;
			}
			return str;
		}
	}

	/**
	 * 持证方式枚举
	 * 
	 * @author liusf
	 */
	@Description(value = "持证方式枚举")
	public enum CZFS {

		/** 共同持证 */
		GTCZ("01", "共同持证"),

		/** 分别持证 */
		FBCZ("02", "分别持证");

		public String Value;

		public String Name;

		private CZFS(String value) {
			this.Value = value;
		}

		private CZFS(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

	}

	/**
	 * 证书版式枚举
	 * 
	 * @author liusf
	 */
	@Description(value = "证书版式枚举")
	public enum ZSBS {

		/** 单一版 */
		DYB("01", "单一版"),

		/** 集成版 */
		JCB("02", "集成版");

		public String Value;

		public String Name;

		private ZSBS(String value) {
			this.Value = value;
		}

		private ZSBS(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 抵押方式枚举
	 * 
	 * @author 海豹
	 *
	 */
	@Description(value = "抵押方式枚举")
	public enum DYFS {
		YBDY("1", "一般抵押"), ZGEDY("2", "最高额抵押");
		public String Value;
		public String Name;

		private DYFS(String value) {
			this.Value = value;
		}

		private DYFS(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 是否登簿枚举
	 * 
	 * @author 刘树峰
	 */
	public enum SFDB {

		/** 否 */
		NO("0", "否"),

		/** 是 */
		YES("1", "是");

		public String Value;

		public String Name;

		private SFDB(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 有效标志枚举
	 * 
	 * @author Administrator
	 */
	public enum YXBZ {

		有效("0", "有效"), 无效("-1", "无效");

		public String Value;

		public String Name;

		private YXBZ(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 登记状态枚举
	 * 
	 * @author 刘树峰
	 */
	public enum DJZT {
		/** 未登记 */
		WDJ("01", "未登记"),

		/** 登记中 */
		DJZ("02", "登记中"),

		/** 已登记 */
		YDJ("03", "已登记");

		public String Value;

		public String Name;

		private DJZT(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 登记单元来源枚举
	 * 
	 * @author Administrator
	 */
	public enum DJDYLY {
		/** 工作层 */
		GZ("01", "gz", "_GZ"),

		/** 现状层 */
		XZ("02", "xz", "_XZ"),

		/** 历史层 */
		LS("03", "ls", "_LS"),

		/** 调查层 */
		DC("04", "", "");

		public String Value;

		public String Name;

		public String TableSuffix;

		private DJDYLY(String value, String name, String tableSuffix) {
			this.Value = value;
			this.Name = name;
			this.TableSuffix = tableSuffix;
		}

		public static DJDYLY initFrom(String value) {
			if (value == null) {
				return null;
			}
			for (DJDYLY examType : DJDYLY.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}

		public static DJDYLY initFromByEnumName(String value) {
			if (value == null) {
				return null;
			}
			for (DJDYLY examType : DJDYLY.values()) {
				if (value.equals(examType.name())) {
					return examType;
				}
			}
			return null;
		}
	}

	/**
	 * 是否枚举
	 * 
	 * @ClassName: SF
	 * @author liushufeng
	 * @date 2015年7月12日 下午2:11:35
	 */
	public enum SF {
		YES("1", "true"), NO("0", "false");

		public String Value;

		public String Name;

		private SF(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 房屋用途枚举
	 * 
	 * @ClassName: FWYT
	 * @author
	 * @date 2015年7月25日 下午9:09:47
	 */
	public enum FWYT {

		/**
		 * 住宅
		 */
		ZZ("1", "住宅"),
		/**
		 * 商业用房
		 */
		SYYF("2", "商业用房"),

		/** 办公用房 */
		BGYF("3", "办公用房"),
		/**
		 * 工业用房
		 */
		GYYF("4", "工业用房"),

		/** 仓储用房 */
		CCYF("5", "仓储用房"),

		/** 车库 */
		CK("6", "车库"),

		/** 其它 */
		QT("99", "其它");
		public String Value;

		public String Name;

		private FWYT(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 抵押不动产类型
	 * 
	 * @author diaoliwei
	 * @date 2015年7月24日 下午3:47:30
	 */
	public enum DYBDCLX {
		/** 土地 */
		TD("1", "土地"),

		/** 土地和房屋 */
		TDHFW("2", "土地和房屋"),

		/** 林地和林木 */
		LDHLM("3", "林地和林木"),

		/** 土地和在建建筑物 */
		TDHZJZZW("4", "土地和在建建筑物"),

		/** 海域 */
		HY("5", "海域"),

		/** 海域和构筑物 */
		HYHGZW("6", "海域和构筑物"),

		/** 其它 */
		QT("7", "其它");

		public String Value;

		public String Name;

		private DYBDCLX(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 约束类型
	 * 
	 * @ClassName: ConstraintsType
	 * @author liushufeng
	 * @date 2015年7月24日 上午12:13:14
	 */
	public enum ConstraintsType {

		PRE("01", "前置约束"), CUR("02", "当前约束"), IGNOR("03", "忽略约束");
		public String Value;
		public String Name;

		private ConstraintsType(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

	}

	/**
	 * 查封类型
	 * 
	 * @ClassName: CFLX
	 * @author liushufeng
	 * @date 2015年8月6日 下午11:00:45
	 */
	public enum CFLX {
		CF("1", "查封"), LHCF("2", "轮候查封"), YCF("3", "预查封"), LHYCF("4", "轮候预查封"), XF("5", "续封");

		public String Value;
		public String Name;

		private CFLX(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 申请人类别
	 * 
	 * @ClassName: SQRLB
	 * @author liushufeng
	 * @date 2015年8月6日 下午11:03:08
	 */
	public enum SQRLB {
		JF("1", "甲方"), YF("2", "乙方"), BF("3", "丙方"), FQF("4", "放弃方"),LHGXF("5","利害关系方");

		public String Value;
		public String Name;

		private SQRLB(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 检查项级别枚举
	 * 
	 * @ClassName: CHECKLEVEL
	 * @author liushufeng
	 * @date 2015年8月10日 下午1:59:40
	 */
	public enum CHECKLEVEL {
		/**
		 * 1,严重
		 */
		ERROR("1", "严重"),
		/**
		 * 警告
		 */
		WARNING("2", "警告"),
		/**
		 * 提示
		 */
		INFO("3", "提示");

		public String Value;

		public String Name;

		private CHECKLEVEL(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 限制状态
	 * 
	 * @ClassName: XZZT
	 * @author liushufeng
	 * @date 2015年8月15日 下午3:54:07
	 */
	public enum XZZT {
		QZGH("01", "强制转移");

		public String Value;
		public String Name;

		private XZZT(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 接入业务编码
	 * 
	 * @author diaoliwei
	 * @date 2015年10月16日 下午3:34:17
	 */
	public enum RECCODE {

		TDSYQ_CSDJ("1000101", "土地所有权首次登记"),

		TDSYQ_ZYDJ("2000101", "土地所有权转移登记"),

		TDSYQ_BGDJ("3000101", "土地所有权变更登记"),

		TDSYQ_GZDJ("5000101", "土地所有权更正登记"),

		JSYDSHYQ_CSDJ("1000301", "建设用地使用权、宅基地使用权首次登记"),

		JSYDSHYQ_ZYDJ("2000301", "建设用地使用权、宅基地使用权转移登记"),

		JSYDSHYQ_BGDJ("3000301", "建设用地使用权、宅基地使用权变更登记"),

		JSYDSHYQ_GZDJ("5000301", "建设用地使用权、宅基地使用权更正登记"),

		FDCQDUOZ_CSDJ("1000401", "房地产权（项目内多幢房屋）首次登记"),

		FDCQDUOZ_ZYDJ("2000401", "房地产权（项目内多幢房屋）转移登记"),

		FDCQDUOZ_BGDJ("3000401", "房地产权（项目内多幢房屋）变更登记"),

		FDCQDUOZ_GZDJ("5000401", "房地产权（项目内多幢房屋）更正登记"),

		FDCQDZ_CSDJ("1000402", "房地产权（独幢、层、套、间、房屋）首次登记"),

		FDCQDZ_ZYDJ("2000402", "房地产权（独幢、层、套、间、房屋）转移登记"),

		FDCQDZ_BGDJ("3000402", "房地产权（独幢、层、套、间、房屋）变更登记"),

		FDCQDZ_GZDJ("5000402", "房地产权（独幢、层、套、间、房屋）更正登记"),

		JZW_CSDJ("1000403", "建筑物区分所有权业主共有部分首次登记"),

		JZW_ZYDJ("2000403", "建筑物区分所有权业主共有部分转移登记"),

		JZW_BGDJ("3000403", "建筑物区分所有权业主共有部分变更登记"),

		JZW_GZDJ("5000403", "建筑物区分所有权业主共有部分更正登记"),

		HY_CSDJ("1001501", "海域(含无居民海岛)使用权首次登记"),

		HY_ZYDJ("2001501", "海域(含无居民海岛)使用权转移登记"),

		HY_BGDJ("3001501", "海域(含无居民海岛)使用权变更登记"),

		HY_GZDJ("5001501", "海域(含无居民海岛)使用权更正登记"),

		GJW_CSDJ("1001601", "构（建）筑物所有权首次登记"),

		GJW_ZYDJ("2001601", "构（建）筑物所有权转移登记"),

		GJW_BGDJ("3001601", "构（建）筑物所有权变更登记"),

		GJW_GZDJ("5001601", "构（建）筑物所有权更正登记"),

		NYD_CSDJ("1000901", "农用地使用权（非林地）首次登记"),

		NYD_ZYDJ("2000901", "农用地使用权（非林地）转移登记"),

		NYD_BGDJ("3000901", "农用地使用权（非林地）变更登记"),

		NYD_GZDJ("5000901", "农用地使用权（非林地）更正登记"),

		LQ_CSDJ("1001201", "林权首次登记"),

		LQ_ZYDJ("2001201", "林权转移登记"),

		LQ_BGDJ("3001201", "林权变更登记"),

		LQ_GZDJ("5001201", "林权更正登记"),

		ZX_ZXDJ("4000101", "注销登记（不含抵押、预告、异议、查封、地役权的注销）"),

		DIYQ_ZXDJ("9000101", "抵押权登记（含首次、转移、变更、更正、注销登记）"),

		DYQ_ZXDJ("9000102", "地役权登记（含首次、转移、变更、更正、注销登记）"),

		YY_ZXDJ("6000101", "异议登记（含首次、转移、变更、更正、注销登记）"),

		YG_ZXDJ("7000101", "预告登记（含首次、转移、变更、更正、注销登记）"),

		CF_CFDJ("8000101", "查封登记（含查封、解封登记）"),

		// TODO 其他权利暂时不定义
		QTQL_CSDJ("1009901", "其他相关权利首次登记"),

		QTQL_ZYDJ("1009901", "其他相关权利转移登记"),

		QTQL_BGDJ("1009901", "其他相关权利变更登记"),

		QTQL_GZDJ("1009901", "其他相关权利更正登记");

		public String Value;
		public String Name;

		private RECCODE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static String initFromName(String value) {
			if (value == null) {
				return null;
			}
			for (RECCODE examType : RECCODE.values()) {
				if (value.equals(examType.Value)) {
					return examType.Name;
				}
			}
			return null;
		}
	}

	public enum GYFS {

		DYSY("0", "单独所有"), GTGY("1", "共同共有"), AFGY("2", "按份共有"), QTGY("3", "其它共有");
		public String Value;
		public String Name;

		private GYFS(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 检查类型
	 * 
	 * @ClassName: CHECKTYPE
	 * @author yuxuebin
	 * @date 2015年8月15日 下午3:54:07
	 */
	public enum CHECKTYPE {
		BASE("01", "基准流程"), USERDEFINE("02", "用户自定义");

		public String Value;
		public String Name;

		private CHECKTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

	/**
	 * 日志级别
	 * 
	 * @author diaoliwei
	 * @date 2016-1-14
	 */
	public enum LOG {
		LOGIN("01", "登录"), UNKNOWN("02", "未知级别"), SEARCH("03", "查询级别"), ADD("04", "添加操作"), DELETE("05",
				"删除操作"), UPDATE("06", "更新操作"), PRINT("07", "打印操作");

		public String Value;
		public String Name;

		private LOG(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}


	/**
	 * 税收种类枚举
	 *
	 * @author liusf
	 */
	@Description(value = "税收种类枚举")
	public enum SSZL {

		QS("0", "契税"),

		YHS("1", "印花税"),

		ZZS("2", "增值税"),

		GRSDS("3", "个人所得税"),

		TDZZS("4", "土地增值税"),

		JYFJ("5", "教育费附加"),

		DFJYFJ("6", "地方教育附加"),

		CSJSS("7", "城市建设税"),
		;

		public String Value;

		public String Name;

		private SSZL(String value) {
			this.Value = value;
		}

		private SSZL(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static SSZL initFrom(String value) {
			if (value == null) {
				return null;
			}
			for (SSZL examType : SSZL.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}

	/**
	 * @Author taochunda
	 * @Description 短信错误码类型
	 * @Date 2019-07-22 11:38
	 * @Param
	 * @return
	 **/
	@Description(value = "短信错误码类型")
	public enum SMSERRORTYPE {

		SUCCESS("0", "短信推送成功"),

		ERROR_1001("1001", "sig 校验失败"),
		ERROR_1002("1002", "短信/语音内容中含有敏感词"),
		ERROR_1003("1003", "请求包体没有 sig 字段或 sig 为空"),
		ERROR_1004("1004", "请求包解析失败，通常情况下是由于没有遵守 API 接口说明规范导致的"),
		ERROR_1006("1006", "请求没有权限"),
		ERROR_1007("1007", "其他错误"),
		ERROR_1008("1008", "请求下发短信/语音超时"),
		ERROR_1009("1009", "请求 IP 不在白名单中"),
		ERROR_1011("1011", "不存在该 REST API 接口"),
		ERROR_1012("1012", "签名格式错误或者签名未审批"),
		ERROR_1013("1013", "下发短信/语音命中了频率限制策略"),
		ERROR_1014("1014", "模版未审批或请求的内容与审核通过的模版内容不匹配"),
		ERROR_1015("1015", "手机号在黑名单库中，通常是用户退订或者命中运营商黑名单导致的"),
		ERROR_1016("1016", "手机号格式错误"),
		ERROR_1017("1017", "请求的短信内容太长"),
		ERROR_1018("1018", "语音验证码格式错误"),
		ERROR_1019("1019", "sdkappid 不存在"),
		ERROR_1020("1020", "sdkappid 已禁用"),
		ERROR_1021("1021", "请求发起时间不正常，通常是由于您的服务器时间与腾讯云服务器时间差异超过10分钟导致的"),
		ERROR_1022("1022", "业务短信日下发条数超过设定的上限"),
		ERROR_1023("1023", "单个手机号30秒内下发短信条数超过设定的上限"),
		ERROR_1024("1024", "单个手机号1小时内下发短信条数超过设定的上限"),
		ERROR_1025("1025", "单个手机号日下发短信条数超过设定的上限"),
		ERROR_1026("1026", "单个手机号下发相同内容超过设定的上限"),
		ERROR_1029("1029", "营销短信发送时间限制"),
		ERROR_1030("1030", "不支持该请求"),
		ERROR_1031("1031", "套餐包余量不足"),
		ERROR_1032("1032", "个人用户没有发营销短信的权限"),
		ERROR_1033("1033", "欠费被停止服务"),
		ERROR_1034("1034", "群发请求里既有国内手机号也有国际手机号"),
		ERROR_1036("1036", "单个模板变量字符数超过12个"),
		ERROR_1045("1045", "不支持该地区短信下发"),
		ERROR_1046("1046", "调用群发 API 接口单次提交的手机号个数超过200个"),
		ERROR_1047("1047", "国际短信日下发条数被限制"),
		ERROR_60008("60008", "处理请求超时"),
		;

		public String Value;

		public String Name;

		private SMSERRORTYPE(String value) {
			this.Value = value;
		}

		private SMSERRORTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static String initFrom(String value) {
			if (value == null) {
				return null;
			}
			for (SMSERRORTYPE examType : SMSERRORTYPE.values()) {
				if (value.equals(examType.Value)) {
					return examType.Name;
				}
			}
			return null;
		}
	}
}
