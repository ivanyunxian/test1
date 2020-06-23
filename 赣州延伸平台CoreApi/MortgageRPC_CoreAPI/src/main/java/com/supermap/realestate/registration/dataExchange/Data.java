package com.supermap.realestate.registration.dataExchange;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.dataExchange.cfdj.QLFQLCFDJ;
import com.supermap.realestate.registration.dataExchange.dyq.FWGZW;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLFFWFDCQDZXM;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQDZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHBHQK;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHYDZB;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.hy.KTTZHJBXX;
import com.supermap.realestate.registration.dataExchange.hy.QLFQLHYSYQ;
import com.supermap.realestate.registration.dataExchange.hy.ZHK105;
import com.supermap.realestate.registration.dataExchange.lq.QLTQLLQ;
import com.supermap.realestate.registration.dataExchange.nydsyq.QLNYDSYQ;
import com.supermap.realestate.registration.dataExchange.qfsyq.QLFFWFDCQQFSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.xzdy.QLFXZDY;
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.dataExchange.yydj.QLFQLYYDJ;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"gyqlr","zdjbxx","zdbhqk","kttzhjbxx","ktfzhyhzk","ktfzhyhydzb","ktfzhbhqk","kttgyjzx","kttgyjzd",
		"kttfwzrz","kttfwljz","kttfwc","kttfwh","zdk103","zhk105","qlfqljsydsyq","qltfwfdcqyz",
		"qlfqlygdj","qlfqlyydj","zxdj","qlfqltdsyq","qlffwfdcqdzxm","qlffwfdcqqfsyq","qlnydsyq","qltqllq",
		"qlfqlhysyq","qltfwfdcqdz","qlfqlcfdj","qlfqldyaq","qlfxzdy","fwgzw",
		"djslsq","djsj","djsf","djsh","djsz","djfz","djgd"
		,"djsqr","fjf100" 
})
@XmlRootElement(name = "Data")
public class Data {

	//界址线
	@XmlElement(name = "KTT_GY_JZX", required = true)
	protected KTTGYJZX kttgyjzx;
	//界址点
	@XmlElement(name = "KTT_GY_JZD", required = true)
	protected KTTGYJZD kttgyjzd;
	//建设用地、宅基地使用权表
	@XmlElement(name = "QLF_QL_JSYDSYQ", required = true)
	protected QLFQLJSYDSYQ qlfqljsydsyq;
	//宗地基本信息表
	@XmlElement(name = "KTT_ZDJBXX", required = true)
	protected KTTZDJBXX zdjbxx;
	//登记归档表
	@XmlElement(name = "DJF_DJ_GD", required = true)
	protected List<DJFDJGD> djgd;
	//登记收件表
	@XmlElement(name = "DJF_DJ_SJ", required = true)
	protected List<DJFDJSJ> djsj;
	//登记审核表
	@XmlElement(name = "DJF_DJ_SH", required = true)
	protected List<DJFDJSH> djsh;
	//权利人表
	@XmlElement(name = "ZTT_GY_QLR", required = true)
	protected List<ZTTGYQLR> gyqlr;
	//登记收费表
	@XmlElement(name = "DJF_DJ_SF", required = true)
	protected List<DJFDJSF> djsf;
	//登记受理申请表
	@XmlElement(name = "DJT_DJ_SLSQ", required = true)
	protected DJTDJSLSQ djslsq;
	
	//宗地变化情况表
	@XmlElement(name = "KTF_ZDBHQK", required = true)
	protected KTFZDBHQK zdbhqk;
	//登记发证表
	@XmlElement(name = "DJF_DJ_FZ", required = true)
	protected List<DJFDJFZ> djfz;
	//非结构化文档上传表
	@XmlElement(name = "FJ_F_100", required = true)
	protected FJF100 fjf100;
	//登记缮证表
	@XmlElement(name = "DJF_DJ_SZ", required = true)
	protected List<DJFDJSZ> djsz;
	
	//宗地属性表
	@XmlElement(name = "ZD_K_103", required = true)
	protected List<ZDK103> zdk103;
	//申请人属性表
	@XmlElement(name = "DJF_DJ_SQR", required = true)
	protected List<DJFDJSQR> djsqr;
	
	//自然幢表
	@XmlElement(name = "KTT_FW_ZRZ", required = true)
	protected KTTFWZRZ kttfwzrz;
	//逻辑幢表
	@XmlElement(name = "KTT_FW_LJZ", required = true)
	protected KTTFWLJZ kttfwljz;
	//户表
	@XmlElement(name = "KTT_FW_H", required = true)
	protected KTTFWH kttfwh;
	//层表
	@XmlElement(name = "KTT_FW_C", required = true)
	protected KTTFWC kttfwc;
	//房地产权_独幢、层、套、间房屋表
	@XmlElement(name = "QLT_FW_FDCQ_YZ", required = true)
	protected QLTFWFDCQYZ qltfwfdcqyz;
	//房地产权_项目内多幢房屋
	@XmlElement(name = "QLT_FW_FDCQ_DZ")
	protected QLTFWFDCQDZ qltfwfdcqdz;
	
	//查封登记表
	@XmlElement(name = "QLF_QL_CFDJ")
	protected QLFQLCFDJ qlfqlcfdj;
	
	//抵押权表
	@XmlElement(name = "QLF_QL_DYAQ")
	protected QLFQLDYAQ qlfqldyaq;
	
	//限制单元表
	@XmlElement(name = "QLF_XZDY")
	protected List<QLFXZDY> qlfxzdy;
	
	//构筑物属性结构表
	@XmlElement(name = "FW_GZW")
	protected List<FWGZW> fwgzw;
	//房地产权_项目内多幢房屋）项目属性表
	@XmlElement(name = "QLF_FW_FDCQ_DZ_XM")
	protected QLFFWFDCQDZXM qlffwfdcqdzxm;
	//建筑物区分所有权业主共有部分表
	@XmlElement(name = "QLF_FW_FDCQ_QFSYQ")
	protected QLFFWFDCQQFSYQ qlffwfdcqqfsyq;
	
	//预告登记表
	@XmlElement(name = "QLF_QL_YGDJ")
	protected QLFQLYGDJ qlfqlygdj;
	
	//异议登记表
	@XmlElement(name = "QLF_QL_YYDJ")
	protected QLFQLYYDJ qlfqlyydj;
	
	//注销登记属性结构描述表
	@XmlElement(name = "QLF_QL_ZXDJ")
	protected QLFZXDJ zxdj;
	
	//土地所有权表
	@XmlElement(name = "QLF_QL_TDSYQ")
	protected QLFQLTDSYQ qlfqltdsyq;
	
	//农用地使用权表
	@XmlElement(name = "QLF_QL_NYDSYQ")
	protected QLNYDSYQ qlnydsyq;
	
	//林权表
	@XmlElement(name = "QLT_QL_LQ")
	protected QLTQLLQ qltqllq;
	
	//宗海基本信息表
	@XmlElement(name = "KTT_ZHJBXX")
	protected KTTZHJBXX kttzhjbxx;
	//宗海变化情况表
	@XmlElement(name = "KTF_ZHBHQK")
	protected KTFZHBHQK ktfzhbhqk;
	//海域（含无居民海岛）使用权
	@XmlElement(name = "QLF_QL_HYSYQ")
	protected QLFQLHYSYQ qlfqlhysyq;
	//用海状况表
	@XmlElement(name = "KTF_ZH_YHZK")
	protected KTFZHYHZK ktfzhyhzk;
	//用海、用岛坐标表
	@XmlElement(name = "KTF_ZH_YHYDZB")
	protected KTFZHYHYDZB ktfzhyhydzb;
	//宗海属性表
	@XmlElement(name = "ZH_K_105")
	protected List<ZHK105> zhk105;
	
	public KTTGYJZX getKTTGYJZX() {
		return kttgyjzx;
	}

	public void setKTTGYJZX(KTTGYJZX value) {
		this.kttgyjzx = value;
	}
	
	public KTTGYJZD getKTTGYJZD() {
		return kttgyjzd;
	}

	public void setKTTGYJZD(KTTGYJZD value) {
		this.kttgyjzd = value;
	}
	
	public List<ZHK105> getZHK105() {
		return zhk105;
	}

	public void setZHK105(List<ZHK105> value) {
		this.zhk105 = value;
	}
	
	public KTFZHYHYDZB getKTFZHYHYDZB() {
		return ktfzhyhydzb;
	}

	public void setKTFZHYHYDZB(KTFZHYHYDZB value) {
		this.ktfzhyhydzb = value;
	}
	
	public KTFZHYHZK getKTFZHYHZK() {
		return ktfzhyhzk;
	}

	public void setKTFZHYHZK(KTFZHYHZK value) {
		this.ktfzhyhzk = value;
	}
	
	public QLFQLHYSYQ getQLFQLHYSYQ() {
		return qlfqlhysyq;
	}

	public void setQLFQLHYSYQ(QLFQLHYSYQ value) {
		this.qlfqlhysyq = value;
	}
	
	public KTFZHBHQK getKTFZHBHQK() {
		return ktfzhbhqk;
	}

	public void setKTFZHBHQK(KTFZHBHQK value) {
		this.ktfzhbhqk = value;
	}
	
	public KTTZHJBXX getKTTZHJBXX() {
		return kttzhjbxx;
	}

	public void setKTTZHJBXX(KTTZHJBXX value) {
		this.kttzhjbxx = value;
	}
	
	public QLTQLLQ getQLTQLLQ() {
		return qltqllq;
	}

	public void setQLTQLLQ(QLTQLLQ value) {
		this.qltqllq = value;
	}
	
	public QLFQLTDSYQ getQLFQLTDSYQ() {
		return qlfqltdsyq;
	}

	public void setQLFQLTDSYQ(QLFQLTDSYQ value) {
		this.qlfqltdsyq = value;
	}
	
	public QLFZXDJ getZXDJ() {
		return zxdj;
	}

	public void setZXDJ(QLFZXDJ value) {
		this.zxdj = value;
	}
		
	public QLFQLYYDJ getQLFQLYYDJ() {
		return qlfqlyydj;
	}

	public void setQLFQLYYDJ(QLFQLYYDJ value) {
		this.qlfqlyydj = value;
	}
	
	public QLFQLYGDJ getQLFQLYGDJ() {
		return qlfqlygdj;
	}

	public void setQLFQLYGDJ(QLFQLYGDJ value) {
		this.qlfqlygdj = value;
	}
	
	public QLFFWFDCQQFSYQ getQLFFWFDCQQFSYQ() {
		return qlffwfdcqqfsyq;
	}

	public void setQLFFWFDCQQFSYQ(QLFFWFDCQQFSYQ value) {
		this.qlffwfdcqqfsyq = value;
	}
	
	public QLFFWFDCQDZXM getQLFFWFDCQDZXM() {
		return qlffwfdcqdzxm;
	}

	public void setQLFFWFDCQDZXM(QLFFWFDCQDZXM value) {
		this.qlffwfdcqdzxm = value;
	}
	
	public QLTFWFDCQDZ getQLTFWFDCQDZ() {
		return qltfwfdcqdz;
	}

	public void setQLTFWFDCQDZ(QLTFWFDCQDZ value) {
		this.qltfwfdcqdz = value;
	}
	
	public List<FWGZW> getFWGZW() {
		return fwgzw;
	}

	public void setFWGZW(List<FWGZW> value) {
		this.fwgzw = value;
	}
	
	public QLFQLDYAQ getQLFQLDYAQ() {
		return qlfqldyaq;
	}

	public void setQLFQLDYAQ(QLFQLDYAQ value) {
		this.qlfqldyaq = value;
	}
	
	public List<QLFXZDY> getQLFXZDY() {
		return qlfxzdy;
	}
	
	public void setQLFXZDY(List<QLFXZDY> value) {
		this.qlfxzdy = value;
	}
	
	public QLFQLCFDJ getQLFQLCFDJ() {
		return qlfqlcfdj;
	}

	public void setQLFQLCFDJ(QLFQLCFDJ value) {
		this.qlfqlcfdj = value;
	}
	
	public QLTFWFDCQYZ getQLTFWFDCQYZ() {
		return qltfwfdcqyz;
	}

	public void setQLTFWFDCQYZ(QLTFWFDCQYZ value) {
		this.qltfwfdcqyz = value;
	}
	
	public KTTFWC getKTTFWC() {
		return kttfwc;
	}

	public void setKTTFWC(KTTFWC value) {
		this.kttfwc = value;
	}
	
	public KTTFWH getKTTFWH() {
		return kttfwh;
	}

	public void setKTTFWH(KTTFWH value) {
		this.kttfwh = value;
	}
	
	public KTTFWLJZ getKTTFWLJZ() {
		return kttfwljz;
	}

	public void setKTTFWLJZ(KTTFWLJZ value) {
		this.kttfwljz = value;
	}
	
	public KTTFWZRZ getKTTFWZRZ() {
		return kttfwzrz;
	}

	public void setKTTFWZRZ(KTTFWZRZ value) {
		this.kttfwzrz = value;
	}

	/**
	 * Gets the value of the qlfqljsydsyq property.
	 * 
	 * @return possible object is {@link QLFQLJSYDSYQ }
	 * 
	 */
	public QLFQLJSYDSYQ getQLJSYDSYQ() {
		return qlfqljsydsyq;
	}

	/**
	 * Sets the value of the qljsydsyq property.
	 * 
	 * @param value
	 *            allowed object is {@link QLFQLJSYDSYQ }
	 * 
	 */
	public void setQLJSYDSYQ(QLFQLJSYDSYQ value) {
		this.qlfqljsydsyq = value;
	}

	/**
	 * Gets the value of the zdjbxx property.
	 * 
	 * @return possible object is {@link ZDJBXX }
	 * 
	 */
	public KTTZDJBXX getKTTZDJBXX() {
		return zdjbxx;
	}

	/**
	 * Sets the value of the zdjbxx property.
	 * 
	 * @param value
	 *            allowed object is {@link ZDJBXX }
	 * 
	 */
	public void setKTTZDJBXX(KTTZDJBXX value) {
		this.zdjbxx = value;
	}

	/**
	 * Gets the value of the djgd property.
	 * 
	 * @return possible object is {@link DJFDJGD }
	 * 
	 */
	public List<DJFDJGD> getDJGD() {
		return djgd;
	}

	/**
	 * Sets the value of the djgd property.
	 * 
	 * @param value
	 *            allowed object is {@link DJFDJGD }
	 * 
	 */
	public void setDJGD(List<DJFDJGD> value) {
		this.djgd = value;
	}

	/**
	 * Gets the value of the djsj property.
	 * 
	 * @return possible object is {@link DJFDJSJ }
	 * 
	 */
	public List<DJFDJSJ> getDJSJ() {
		return djsj;
	}

	/**
	 * Sets the value of the djsj property.
	 * 
	 * @param value
	 *            allowed object is {@link DJFDJSJ }
	 * 
	 */
	public void setDJSJ(List<DJFDJSJ> value) {
		this.djsj = value;
	}

	/**
	 * Gets the value of the djsh property.
	 * 
	 * @return possible object is {@link DJFDJSH }
	 * 
	 */
	public List<DJFDJSH> getDJSH() {
		return djsh;
	}

	/**
	 * Sets the value of the djsh property.
	 * 
	 * @param value
	 *            allowed object is {@link DJFDJSH }
	 * 
	 */
	public void setDJSH(List<DJFDJSH> value) {
		this.djsh = value;
	}

	public List<ZTTGYQLR> getGYQLR() {
		return gyqlr;
	}

	public void setGYQLR(List<ZTTGYQLR> value) {
		this.gyqlr = value;
	}

	public QLNYDSYQ getQlnydsyq() {
		return qlnydsyq;
	}

	public void setQlnydsyq(QLNYDSYQ qlnydsyq) {
		this.qlnydsyq = qlnydsyq;
	}

	/**
	 * Gets the value of the djsf property.
	 * 
	 * @return possible object is {@link DJFDJSF }
	 * 
	 */
	public List<DJFDJSF> getDJSF() {
		return djsf;
	}

	/**
	 * Sets the value of the djsf property.
	 * 
	 * @param value
	 *            allowed object is {@link DJFDJSF }
	 * 
	 */
	public void setDJSF(List<DJFDJSF> value) {
		this.djsf = value;
	}

	/**
	 * Gets the value of the djslsq property.
	 * 
	 * @return possible object is {@link DJTDJSLSQ }
	 * 
	 */
	public DJTDJSLSQ getDJSLSQ() {
		return djslsq;
	}

	/**
	 * Sets the value of the djslsq property.
	 * 
	 * @param value
	 *            allowed object is {@link DJTDJSLSQ }
	 * 
	 */
	public void setDJSLSQ(DJTDJSLSQ value) {
		this.djslsq = value;
	}

	/**
	 * Gets the value of the zdbhqk property.
	 * 
	 * @return possible object is {@link KTFZDBHQK }
	 * 
	 */
	public KTFZDBHQK getZDBHQK() {
		return zdbhqk;
	}

	/**
	 * Sets the value of the zdbhqk property.
	 * 
	 * @param value
	 *            allowed object is {@link KTFZDBHQK }
	 * 
	 */
	public void setZDBHQK(KTFZDBHQK value) {
		this.zdbhqk = value;
	}

	/**
	 * Gets the value of the djfz property.
	 * 
	 * @return possible object is {@link DJFDJFZ }
	 * 
	 */
	public List<DJFDJFZ> getDJFZ() {
		return djfz;
	}

	/**
	 * Sets the value of the djfz property.
	 * 
	 * @param value
	 *            allowed object is {@link DJFDJFZ }
	 * 
	 */
	public void setDJFZ(List<DJFDJFZ> value) {
		this.djfz = value;
	}

	/**
	 * Gets the value of the fjf100 property.
	 * 
	 * @return possible object is {@link FJF100 }
	 * 
	 */
	public FJF100 getFJF100() {
		return fjf100;
	}

	/**
	 * Sets the value of the fjf100 property.
	 * 
	 * @param value
	 *            allowed object is {@link FJF100 }
	 * 
	 */
	public void setFJF100(FJF100 value) {
		this.fjf100 = value;
	}

	/**
	 * Gets the value of the djsz property.
	 * 
	 * @return possible object is {@link DJFDJSZ }
	 * 
	 */
	public List<DJFDJSZ> getDJSZ() {
		return djsz;
	}

	/**
	 * Sets the value of the djsz property.
	 * 
	 * @param value
	 *            allowed object is {@link DJFDJSZ }
	 * 
	 */
	public void setDJSZ(List<DJFDJSZ> value) {
		this.djsz = value;
	}

	/**
	 * Gets the value of the zdk103 property.
	 * 
	 * @return possible object is {@link ZDK103 }
	 * 
	 */
	public List<ZDK103> getZDK103() {
		return zdk103;
	}

	/**
	 * Sets the value of the zdk103 property.
	 * 
	 * @param value
	 *            allowed object is {@link ZDK103 }
	 * 
	 */
	public void setZDK103(List<ZDK103> value) {
		this.zdk103 = value;
	}
	
	public List<DJFDJSQR> getDJSQR(){
		return djsqr;
	}
	
	public void setDJSQR(List<DJFDJSQR> value){
		this.djsqr = value;
	}
}
