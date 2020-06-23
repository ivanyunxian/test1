package com.supermap.realestate.registration.dataExchange;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

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
 * @Description:工厂类
 * @author diaoliwei
 * @date 2015年10月26日 下午4:04:48
 * @Copyright SuperMap
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RegType_QNAME = new QName("", "RegType");
    private final static QName _ASID_QNAME = new QName("", "ASID");
    private final static QName _RightType_QNAME = new QName("", "RightType");
    private final static QName _RegOrgID_QNAME = new QName("", "RegOrgID");
    private final static QName _CreateDate_QNAME = new QName("", "CreateDate");
    private final static QName _RecType_QNAME = new QName("", "RecType");
    private final static QName _AreaCode_QNAME = new QName("", "AreaCode");
    private final static QName _Mark_QNAME = new QName("", "Mark");
    private final static QName _ParcelID_QNAME = new QName("", "ParcelID");
    private final static QName _BizMsgID_QNAME = new QName("", "BizMsgID");
    private final static QName _RecFlowID_QNAME = new QName("", "RecFlowID");
    private final static QName _IsRoutine_QNAME = new QName("", "IsRoutine");
    private final static QName _EstateNum_QNAME = new QName("", "EstateNum");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dataexchange
     * 
     */
    public ObjectFactory() {
    }
    
    /**
     * 申请人
     * @作者 diaoliwei
     * @创建时间 2015年8月28日下午10:35:22
     * @return
     */
    public List<DJFDJSQR> createSQR(){
    	return new ArrayList<DJFDJSQR>();
    }
    
    public KTTGYJZX createKTTGYJZX(){
    	return new KTTGYJZX();
    }
    
    public KTTGYJZD createKTTGYJZD(){
    	return new KTTGYJZD();
    }
    
    /**
     * 构筑物属性表
     * @作者 diaoliwei
     * @创建时间 2015年9月1日下午9:43:40
     * @return
     */
    public List<FWGZW> createFWGZW(){
    	return new ArrayList<FWGZW>();
    }

    /**
     * Create an instance of {@link ZDK103 }
     * 
     */
    public List<ZDK103> createZDK103() {
        return new ArrayList<ZDK103>();
    }

    /**
     * Create an instance of {@link DJFDJSF }
     * 
     */
    public List<DJFDJSF> createDJSF() {
        return new ArrayList<DJFDJSF>();
    }

    /**
     * Create an instance of {@link KTFZDBHQK }
     * 
     */
    public KTFZDBHQK createZDBHQK() {
        return new KTFZDBHQK();
    }
    
    public QLFQLTDSYQ createQLFQLTDSYQ(){
    	return new QLFQLTDSYQ();
    }

    /**
     * Create an instance of {@link FJF100 }
     * 
     */
    public FJF100 createFJF100() {
        return new FJF100();
    }
  
    /**
     * Create an instance of {@link QLNYDSYQ( }
     * 
     */
    public QLNYDSYQ createQLTQLNYD() {
        return new QLNYDSYQ();
    }

    /**
     * Create an instance of {@link DJTDJSLSQ }
     * 
     */
    public DJTDJSLSQ createDJSLSQ() {
        return new DJTDJSLSQ();
    }

    /**
     * Create an instance of {@link DJFDJFZ }
     * 
     */
    public List<DJFDJFZ> createDJFZ() {
        return new ArrayList<DJFDJFZ>();
    }

    /**
     * Create an instance of {@link Message }
     * 
     */
    public Message createMessage() {
        return new Message();
    }

    /**
     * Create an instance of {@link DJFDJSJ }
     * 
     */
    public List<DJFDJSJ> createDJSJ() {
        return new ArrayList<DJFDJSJ>();
    }

    /**
     * Create an instance of {@link ZDJBXX }
     * 
     */
    public KTTZDJBXX createZDJBXX() {
        return new KTTZDJBXX();
    }

    /**
     * Create an instance of {@link Data }
     * 
     */
    public Data createData() {
        return new Data();
    }

    /**
     * Create an instance of {@link ZTTGYQLR }
     * 
     */
    public List<ZTTGYQLR> createGYQLR() {
        return new ArrayList<ZTTGYQLR>();
    }
    
    public QLFQLDYAQ createQLFQLDYAQ(){
    	return new QLFQLDYAQ();
    }
    public List<QLFXZDY> createQLFXZDY(){
    	return new ArrayList<QLFXZDY>();
    }

    /**
     * Create an instance of {@link Head }
     * 
     */
    public Head createHead() {
        return new Head();
    }

    /**
     * Create an instance of {@link DJFDJGD }
     * 
     */
    public List<DJFDJGD> createDJGD() {
        return new ArrayList<DJFDJGD>();
    }

    /**
     * Create an instance of {@link DJFDJSH }
     * 
     */
    public List<DJFDJSH> createDJSH() {
        return new ArrayList<DJFDJSH>();
    }

    /**
     * Create an instance of {@link DJFDJSZ }
     * 
     */
    public List<DJFDJSZ> createDJSZ() {
        return new ArrayList<DJFDJSZ>();
    }

    /**
     * Create an instance of {@link QLFQLJSYDSYQ }
     * 
     */
    public QLFQLJSYDSYQ createQLJSYDSYQ() {
        return new QLFQLJSYDSYQ();
    }
    
    /**
     * 报文自然幢信息
     * @作者 diaoliwei
     * @创建时间 2015年9月1日上午12:44:26
     * @return
     */
    public KTTFWZRZ createKTTFWZRZ(){
    	return new KTTFWZRZ();
    }
    
    /**
     * 报文逻辑幢信息
     * @作者 diaoliwei
     * @创建时间 2015年9月1日上午12:44:53
     * @return
     */
    public KTTFWLJZ createKTTFWLJZ(){
    	return new KTTFWLJZ();
    }
    
    /**
     * 报文户信息
     * @作者 diaoliwei
     * @创建时间 2015年9月1日上午12:45:10
     * @return
     */
    public KTTFWH createKTTFWH(){
    	return new KTTFWH();
    }
    
    public KTTFWC createKTTFWC(){
    	return new KTTFWC();
    }
    
    /**
     * 报文查封信息
     * @作者 diaoliwei
     * @创建时间 2015年9月1日上午12:45:28
     * @return
     */
    public QLFQLCFDJ createQLFQLCFDJ(){
    	return new QLFQLCFDJ();
    }
    
    /**
     * 报文房地产权_独幢表
     * @作者 diaoliwei
     * @创建时间 2015年9月1日上午12:45:47
     * @return
     */
    public QLTFWFDCQYZ createQLTFWFDCQYZ(){
    	return new QLTFWFDCQYZ();
    }
    
    public QLFFWFDCQDZXM createQLFFWFDCQDZXM(){
    	return new QLFFWFDCQDZXM();
    }
    
    public QLFFWFDCQQFSYQ createQLFFWFDCQQFSYQ(){
    	return new QLFFWFDCQQFSYQ();
    }
    
    public QLTFWFDCQDZ createQLTFWFDCQDZ(){
    	return new QLTFWFDCQDZ();
    }
    
    /**
     * 预告登记
     * @作者 diaoliwei
     * @创建时间 2015年9月8日下午12:57:03
     * @return
     */
    public QLFQLYGDJ createQLFQLYGDJ(){
    	return new QLFQLYGDJ();
    }
    
    /**
     * 异议登记
     * @作者 diaoliwei
     * @创建时间 2015年9月7日下午10:05:32
     * @return
     */
    public QLFQLYYDJ createQLFQLYYDJ(){
    	return new QLFQLYYDJ();
    }
    
    /**
     * 注销登记
     * @作者 diaoliwei
     * @创建时间 2015年9月11日下午10:55:51
     * @return
     */
    public QLFZXDJ createZXDJ(){
    	return new QLFZXDJ();
    }
    
    /**
     * 林权表
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午8:55:37
     * @return
     */
    public QLTQLLQ createQLTQLLQ(){
    	return new QLTQLLQ();
    }
    
    /**
     * 宗海基本信息表
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午9:23:35
     * @return
     */
    public KTTZHJBXX createKTTZHJBXX(){
    	return new KTTZHJBXX();
    }
    
    /**
     * 宗海变化情况表
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午9:28:49
     * @return
     */
    public KTFZHBHQK createKTFZHBHQK(){
    	return new KTFZHBHQK();
    }
    
    /**
     * 海域（含无居民海岛）使用权
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午9:36:51
     * @return
     */
    public QLFQLHYSYQ createQLFQLHYSYQ(){
    	return new QLFQLHYSYQ();
    }
    
    /**
     * 用海状况表
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午9:40:54
     * @return
     */
    public KTFZHYHZK createKTFZHYHZK(){
    	return new KTFZHYHZK();
    }
    
    /**
     * 用海、用岛坐标表
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午9:44:23
     * @return
     */
    public KTFZHYHYDZB createKTFZHYHYDZB(){
    	return new KTFZHYHYDZB();
    }
    
    /**
     * 宗海属性表
     * @作者 diaoliwei
     * @创建时间 2015年9月21日下午9:48:17
     * @return
     */
    public List<ZHK105> createZHK105(){
    	return new ArrayList<ZHK105>();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RegType")
    public JAXBElement<String> createRegType(String value) {
        return new JAXBElement<String>(_RegType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ASID")
    public JAXBElement<String> createASID(String value) {
        return new JAXBElement<String>(_ASID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RightType")
    public JAXBElement<String> createRightType(String value) {
        return new JAXBElement<String>(_RightType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RegOrgID")
    public JAXBElement<String> createRegOrgID(String value) {
        return new JAXBElement<String>(_RegOrgID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CreateDate")
    public JAXBElement<XMLGregorianCalendar> createCreateDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CreateDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RecType")
    public JAXBElement<String> createRecType(String value) {
        return new JAXBElement<String>(_RecType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "AreaCode")
    public JAXBElement<String> createAreaCode(String value) {
        return new JAXBElement<String>(_AreaCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Mark")
    public JAXBElement<String> createMark(String value) {
        return new JAXBElement<String>(_Mark_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ParcelID")
    public JAXBElement<String> createParcelID(String value) {
        return new JAXBElement<String>(_ParcelID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BizMsgID")
    public JAXBElement<String> createBizMsgID(String value) {
        return new JAXBElement<String>(_BizMsgID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RecFlowID")
    public JAXBElement<String> createRecFlowID(String value) {
        return new JAXBElement<String>(_RecFlowID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IsRoutine")
    public JAXBElement<String> createIsRoutine(String value) {
        return new JAXBElement<String>(_IsRoutine_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "EstateNum")
    public JAXBElement<String> createEstateNum(String value) {
        return new JAXBElement<String>(_EstateNum_QNAME, String.class, null, value);
    }

}
