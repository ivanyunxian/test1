package com.supermap.realestate.registration.dataExchange;

import java.util.List;

import com.supermap.realestate.registration.dataExchange.cfdj.QLFQLCFDJ;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHBHQK;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHYDZB;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.hy.KTTZHJBXX;
import com.supermap.realestate.registration.dataExchange.hy.QLFQLHYSYQ;
import com.supermap.realestate.registration.dataExchange.hy.ZHK105;
import com.supermap.realestate.registration.dataExchange.lq.QLTQLLQ;
import com.supermap.realestate.registration.dataExchange.nydsyq.QLNYDSYQ;
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

public class exchangeFactory {

	/**
	 * 建设用地使用权
	 * @作者 diaoliwei
	 * @创建时间 2015年8月28日下午10:18:52
	 * @return
	 */
	public static Message createMessage(Object object) {
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();//权利人
		KTTZDJBXX jbxx = factory.createZDJBXX();//宗地基本信息
		KTFZDBHQK bhqk = factory.createZDBHQK();//宗地变化情况
		KTTGYJZX jzx = factory.createKTTGYJZX();
		KTTGYJZD jzd = factory.createKTTGYJZD();
		
	
		QLFQLJSYDSYQ ql = factory.createQLJSYDSYQ();
		
		DJTDJSLSQ slsq = factory.createDJSLSQ();
		List<DJFDJSJ> sj = factory.createDJSJ();
		List<DJFDJSF> sf = factory.createDJSF();
		List<DJFDJSH> sh = factory.createDJSH();
		List<DJFDJSZ> sz = factory.createDJSZ();
		List<DJFDJFZ> fz = factory.createDJFZ();
		List<DJFDJGD> gd = factory.createDJGD();
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setKTTZDJBXX(jbxx);
		data.setZDBHQK(bhqk);//  
		data.setKTTGYJZX(jzx);
		data.setKTTGYJZD(jzd);
		if (object !=null && object.toString().equals("true")) {
			List<ZDK103> zdk = factory.createZDK103();
			data.setZDK103(zdk);
		}
		
		data.setQLJSYDSYQ(ql);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 农用地
	 * @作者 lixin
	 * @创建时间 2018年8月21日下午10:18:52
	 * @return
	 */
	public static Message createMessageByNYD() {
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();//权利人
		KTTZDJBXX jbxx = factory.createZDJBXX();//宗地基本信息
		KTFZDBHQK bhqk = factory.createZDBHQK();//宗地变化情况
		List<ZDK103> zdk = factory.createZDK103();//宗地空间属性
		QLNYDSYQ ql = factory.createQLTQLNYD();//农用地使用权
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请信息
		List<DJFDJSJ> sj = factory.createDJSJ();//
		List<DJFDJSF> sf = factory.createDJSF();//
		List<DJFDJSH> sh = factory.createDJSH();//
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证信息
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证信息
		List<DJFDJGD> gd = factory.createDJGD();///登记归档信息
		List<DJFDJSQR> sqr = factory.createSQR();//申请人属性信息
		FJF100 fj = factory.createFJF100();//非结构化文档
		
		data.setGYQLR(qlrs);
		data.setKTTZDJBXX(jbxx);
		data.setZDBHQK(bhqk); 
	    data.setZDK103(zdk);
	    data.setQlnydsyq(ql);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 建设用地使用权、宅基地使用权转移登记(无界址线、界址点、空间数据)
	 * @return
	 */
	public static Message createMessageBySHYQ() {
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		KTTZDJBXX jbxx = factory.createZDJBXX();
		KTFZDBHQK bhqk = factory.createZDBHQK();
		QLFQLJSYDSYQ ql = factory.createQLJSYDSYQ();
		
		DJTDJSLSQ slsq = factory.createDJSLSQ();
		List<DJFDJSJ> sj = factory.createDJSJ();
		List<DJFDJSF> sf = factory.createDJSF();
		List<DJFDJSH> sh = factory.createDJSH();
	    List<DJFDJSZ> sz = factory.createDJSZ();
		List<DJFDJFZ> fz = factory.createDJFZ();
		List<DJFDJGD> gd = factory.createDJGD();
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		
		data.setGYQLR(qlrs);
		data.setKTTZDJBXX(jbxx);
		data.setZDBHQK(bhqk);
		data.setQLJSYDSYQ(ql);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 房屋所有权常规登记
	 * @作者 diaoliwei
	 * @创建时间 2015年8月28日下午10:19:46
	 * @return
	 */
	public static Message createMessageByFWSYQ() {
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		KTTFWZRZ fwzrz = factory.createKTTFWZRZ();
		KTTFWLJZ fwljz = factory.createKTTFWLJZ();
		KTTFWC fwc = factory.createKTTFWC();
		KTTFWH fwh = factory.createKTTFWH();
		List<ZDK103> zdk = factory.createZDK103();
		QLTFWFDCQYZ fdcqyz = factory.createQLTFWFDCQYZ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
//		QLTFWFDCQDZ fdcqdz = factory.createQLTFWFDCQDZ();
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
//		QLFFWFDCQDZXM dzxm = factory.createQLFFWFDCQDZXM();
//		QLFFWFDCQQFSYQ fsyq = factory.createQLFFWFDCQQFSYQ();
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setKTTFWZRZ(fwzrz);
		data.setKTTFWLJZ(fwljz);
		data.setKTTFWC(fwc);
		data.setKTTFWH(fwh);
		data.setQLTFWFDCQYZ(fdcqyz);
		data.setZDK103(zdk);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
//		data.setQLTFWFDCQDZ(fdcqdz);
//		data.setQLFFWFDCQDZXM(dzxm);
//		data.setQLFFWFDCQQFSYQ(fsyq);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	public static Message createMessageByFWSYQ2() {
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		KTTFWZRZ fwzrz = factory.createKTTFWZRZ();
		KTTFWLJZ fwljz = factory.createKTTFWLJZ();
		KTTFWC fwc = factory.createKTTFWC();
		KTTFWH fwh = factory.createKTTFWH();
		QLTFWFDCQYZ fdcqyz = factory.createQLTFWFDCQYZ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
//		QLTFWFDCQDZ fdcqdz = factory.createQLTFWFDCQDZ();
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
//		QLFFWFDCQDZXM dzxm = factory.createQLFFWFDCQDZXM();
//		QLFFWFDCQQFSYQ fsyq = factory.createQLFFWFDCQQFSYQ();
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setKTTFWZRZ(fwzrz);
		data.setKTTFWLJZ(fwljz);
		data.setKTTFWC(fwc);
		data.setKTTFWH(fwh);
		data.setQLTFWFDCQYZ(fdcqyz);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
//		data.setQLTFWFDCQDZ(fdcqdz);
//		data.setQLFFWFDCQDZXM(dzxm);
//		data.setQLFFWFDCQQFSYQ(fsyq);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 查封登记
	 * @作者 diaoliwei
	 * @创建时间 2015年8月31日下午10:49:05
	 * @return
	 */
	public static Message createMessageByCFDJ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		QLFQLCFDJ cfdj = factory.createQLFQLCFDJ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		data.setQLFQLCFDJ(cfdj);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 注销登记
	 * @作者 diaoliwei
	 * @创建时间 2015年9月1日上午12:49:20
	 * @return
	 */
	public static Message createMessageByZXDJ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		QLFZXDJ zxdj = factory.createZXDJ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		data.setZXDJ(zxdj);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 抵押权常规登记
	 * @作者 diaoliwei
	 * @创建时间 2015年9月1日下午5:03:40
	 * @return
	 */
	public static Message createMessageByDYQ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<ZTTGYQLR> qlrs = factory.createGYQLR(); //权利人
		QLFQLDYAQ dyaq = factory.createQLFQLDYAQ();
		List<DJFDJSQR> sqr = factory.createSQR();//申请人
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setQLFQLDYAQ(dyaq);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	public static Message createMessageByXZDY(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		List<QLFXZDY> xzdy = factory.createQLFXZDY();
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setQLFXZDY(xzdy);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
  
	/**
	 * 预告登记
	 * @作者 diaoliwei
	 * @创建时间 2015年9月15日下午1:54:00
	 * @return
	 */
	public static Message createMessageByYGDJ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		QLFQLYGDJ ygdj = factory.createQLFQLYGDJ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSQR> sqr = factory.createSQR();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setQLFQLYGDJ(ygdj);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 预告+预抵押登记
	 * @作者 lx
	 * @创建时间 20181116
	 * @return
	 */
	public static Message createMessageByYG_YDYDJ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		QLFQLYGDJ ygdj = factory.createQLFQLYGDJ();
		QLFQLDYAQ dyaq = factory.createQLFQLDYAQ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSQR> sqr = factory.createSQR();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		FJF100 fj = factory.createFJF100();
		data.setQLFQLDYAQ(dyaq);
		data.setGYQLR(qlrs);
		data.setQLFQLYGDJ(ygdj);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 预抵押登记
	 * @作者 weilb
	 * @创建时间 2017年8月10日下午19:04:00
	 * @return
	 */
	public static Message createMessageByYDYDJ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		QLFQLYGDJ ygdj = factory.createQLFQLYGDJ();
		QLFQLDYAQ dyaq = factory.createQLFQLDYAQ();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<DJFDJSQR> sqr = factory.createSQR();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		FJF100 fj = factory.createFJF100();
		data.setGYQLR(qlrs);
		data.setQLFQLYGDJ(ygdj);
		data.setQLFQLDYAQ(dyaq);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 异议登记表
	 * @作者 diaoliwei
	 * @创建时间 2015年9月15日下午3:19:17
	 * @return
	 */
	public static Message createMessageByYYDJ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		QLFQLYYDJ yydj = factory.createQLFQLYYDJ();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		List<DJFDJSQR> sqr = factory.createSQR();
		FJF100 fj = factory.createFJF100();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		data.setGYQLR(qlrs);
		data.setQLFQLYYDJ(yydj);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 土地所有权
	 * @author diaoliwei
	 * @date 2015-9-17 23:53:12
	 * @return
	 */
	public static Message createMessageByTDSYQ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		KTTZDJBXX jbxx = factory.createZDJBXX();
		FJF100 fj = factory.createFJF100();
		KTFZDBHQK bhqk = factory.createZDBHQK();
		QLFQLTDSYQ syq = factory.createQLFQLTDSYQ();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<ZDK103> zdk = factory.createZDK103();
		List<DJFDJSQR> sqr = factory.createSQR();
		KTTGYJZX jzx = factory.createKTTGYJZX();
		KTTGYJZD jzd = factory.createKTTGYJZD();
		data.setGYQLR(qlrs);
		data.setKTTZDJBXX(jbxx);
		data.setZDBHQK(bhqk);
		data.setKTTGYJZX(jzx);
		data.setKTTGYJZD(jzd);
		data.setQLFQLTDSYQ(syq);
		data.setZDK103(zdk);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJSH(sh);
		data.setDJSZ(sz);
		data.setDJFZ(fz);
		data.setDJGD(gd);
		data.setDJSQR(sqr);
		data.setFJF100(fj);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 林权常规登记
	 * @作者 diaoliwei
	 * @创建时间 2015年9月21日下午9:41:53
	 * @return
	 */
	public static Message createMessageByLQ(){
		ObjectFactory factory = new ObjectFactory();
		Message msg = factory.createMessage();
		Head head = factory.createHead();
		Data data = factory.createData();
		KTTZDJBXX jbxx = factory.createZDJBXX();
		FJF100 fj = factory.createFJF100();
		KTFZDBHQK bhqk = factory.createZDBHQK();
		QLTQLLQ lq = factory.createQLTQLLQ();
		List<ZTTGYQLR> qlrs = factory.createGYQLR();
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请表
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证
		List<DJFDJSH> sh = factory.createDJSH();//登记审核
		List<ZDK103> zdk = factory.createZDK103();
		List<DJFDJSQR> sqr = factory.createSQR();
		data.setKTTZDJBXX(jbxx);
		data.setDJFZ(fz);
		data.setFJF100(fj);
		data.setZDBHQK(bhqk);
		data.setQLTQLLQ(lq);
		data.setGYQLR(qlrs);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJGD(gd);
		data.setDJSZ(sz);
		data.setDJSH(sh);
		data.setZDK103(zdk);
		data.setDJSQR(sqr);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 海域(含无居民海岛)使用权常规登记
	 * @作者 diaoliwei
	 * @创建时间 2015年9月21日下午9:21:08
	 * @return
	 */
	public static Message createMessageByHY(){
		ObjectFactory factory = new ObjectFactory();//对象创建
		Message msg = factory.createMessage();//信息创建
		Head head = factory.createHead();//报文头
		Data data = factory.createData();//报文信息
		KTTZHJBXX jbxx = factory.createKTTZHJBXX();//宗海基本信息
		FJF100 fj = factory.createFJF100();
		KTFZHBHQK bhqk = factory.createKTFZHBHQK();//宗海变化表
		QLFQLHYSYQ hysyq = factory.createQLFQLHYSYQ();//海域使用权
		KTFZHYHZK yhzk = factory.createKTFZHYHZK();//用海状况
		KTFZHYHYDZB yhydzb = factory.createKTFZHYHYDZB();//用海坐标
		List<ZTTGYQLR> qlrs = factory.createGYQLR();//权力人表
		DJTDJSLSQ slsq = factory.createDJSLSQ();//登记受理申请
		List<DJFDJSJ> sj = factory.createDJSJ();//登记收件
		List<DJFDJSF> sf = factory.createDJSF();//登记收费
		List<DJFDJGD> gd = factory.createDJGD();//登记归档
		List<DJFDJSZ> sz = factory.createDJSZ();//登记缮证
		List<DJFDJFZ> fz = factory.createDJFZ();//登记发证信息
		List<DJFDJSH> sh = factory.createDJSH();//登记审核信息
		List<ZHK105> zhk = factory.createZHK105();//宗海空间属性
		List<DJFDJSQR> sqr = factory.createSQR();//申请人
		data.setKTTZHJBXX(jbxx);
		data.setDJFZ(fz);
		data.setFJF100(fj);
		data.setKTFZHBHQK(bhqk);
		data.setQLFQLHYSYQ(hysyq);
		data.setKTFZHYHZK(yhzk);
		data.setKTFZHYHYDZB(yhydzb);
		data.setGYQLR(qlrs);
		data.setDJSLSQ(slsq);
		data.setDJSJ(sj);
		data.setDJSF(sf);
		data.setDJGD(gd);
		data.setDJSZ(sz);
		data.setDJSH(sh);
		data.setZHK105(zhk);
		data.setDJSQR(sqr);
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}

}
