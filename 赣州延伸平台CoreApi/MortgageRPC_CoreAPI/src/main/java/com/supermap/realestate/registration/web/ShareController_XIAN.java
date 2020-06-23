package com.supermap.realestate.registration.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.shareService_XIAN;

/**
 * 
 * @Description:西安交易共享
 * @author yuxuebin
 * @date 2016年6月25日 15:27:22
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/share")
@Component("ShareController_XIAN")
public class ShareController_XIAN {

	/** 登薄service */
	@Autowired
	private shareService_XIAN shareService;
	
	/**
	 * 服务接口：显示查询结果页面
	 * @Title: showQueryPage
	 * @author:yuxuebin
	 * @date：2015年7月16日 下午8:30:20
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accept/", method = RequestMethod.POST)
	public @ResponseBody String accept(HttpServletRequest request) {
		String result = request.getParameter("XMLStr");
		//String result = "";
//		try {
//			result = RequestHelper.getParam(request, "XMLStr");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		try{
			//result="<?xml version='1.0' encoding = 'GBK'?><Root><Head><OpCode>10004</OpCode><OpDate></OpDate><OpTime></OpTime><InfoCount>1</InfoCount><RetCode></RetCode><RetMsg></RetMsg></Head><RecordSet><Row><FDCQ><RecordSet><Row><YSDM></YSDM><BDCDYH>130104015008GB00013F00031020</BDCDYH><YWH></YWH><QLLX>4</QLLX><DJLX>100</DJLX><DJYY>登记原因</DJYY><FDZL>桥东区槐安东路28号仁和嘉园1-1-1508</FDZL><TDSYQR>张三</TDSYQR><DYTDMJ>123</DYTDMJ><FTTDMJ>0</FTTDMJ><QLQSSJ>20150505</QLQSSJ><QLJSSJ>20850505</QLJSSJ><FDCJYJG>100</FDCJYJG><GHYT>11</GHYT><FWXZ>0</FWXZ><FWJG>3</FWJG><SZC>15</SZC><ZCS>25</ZCS><JZMJ>118.49</JZMJ><ZYJZMJ>0</ZYJZMJ><FTJZMJ>0</FTJZMJ><JGSJ>20160520</JGSJ><BDCQZH></BDCQZH><QXDM>130000</QXDM><DJJG>登记机构</DJJG><DBR></DBR><DJSJ></DJSJ><FJ>附记</FJ><QSZT></QSZT><ZXYWH></ZXYWH><ZXSJ></ZXSJ><QLID>qlid1</QLID><BDCDYID>bdcdyid1</BDCDYID></Row></RecordSet></FDCQ><H><RecordSet><Row><BDCDYH>130104015008GB00013F00031020</BDCDYH><FWBM>2007080310125341634</FWBM><YSDM></YSDM><ZRZH>002</ZRZH><LJZH></LJZH><CH>15</CH><ZL>桥东区槐安东路28号仁和嘉园1-1-1508</ZL><MJDW>1</MJDW><SJCS>1</SJCS><HH>0</HH><SHBW>011508</SHBW><HX>3</HX><HXJG>1</HXJG><FWYT1>11</FWYT1><FWYT2></FWYT2><FWYT3></FWYT3><YCJZMJ></YCJZMJ><YCTNJZMJ></YCTNJZMJ><YCFTJZMJ></YCFTJZMJ><YCDXBFJZMJ></YCDXBFJZMJ><YCQTJZMJ></YCQTJZMJ><YCFTXS></YCFTXS><SCJZMJ>118.49</SCJZMJ><SCTNJZMJ>92.20</SCTNJZMJ><SCFTJZMJ>26.29</SCFTJZMJ><SCDXBFJZMJ></SCDXBFJZMJ><SCQTJZMJ></SCQTJZMJ><SCFTXS>0.285</SCFTXS><GYTDMJ></GYTDMJ><FTTDMJ></FTTDMJ><DYTDMJ>123</DYTDMJ><FWLX>1</FWLX><FWXZ></FWXZ><FCFHT></FCFHT><ZT></ZT><BDCDYID>bdcdyid1</BDCDYID><CID></CID><LJZID></LJZID><ZRZBDCDYID></ZRZBDCDYID><GXXMBH></GXXMBH><FWZT></FWZT><RELATIONID>relationid1</RELATIONID><ZCS>25</ZCS><JGSJ>20160520</JGSJ><DYH></DYH><FH>1-1-1508</FH><FWJG>3</FWJG><GHYT>11</GHYT><SZC>15</SZC></Row ></RecordSet></H><QLR><RecordSet><Row><YSDM></YSDM><BDCDYH>130104015008GB00013F00031020</BDCDYH><SXH>1</SXH><QLRMC>张三</QLRMC><BDCQZH></BDCQZH><QZYSXLH></QZYSXLH><ISCZR>1</ISCZR><ZJZL>1</ZJZL><ZJH>111111</ZJH><FZJG>发证机关</FZJG><SSHY>1</SSHY><GJ>142</GJ><HJSZSS>110000</HJSZSS><XB>1</XB><DH>123</DH><DZ>地址</DZ><YB>100000</YB><GZDW>单位</GZDW><DZYJ>sdf@qq.com</DZYJ><QLRLX>1</QLRLX><QLBL>100</QLBL><GYFS>0</GYFS><GYQK>共有情况</GYQK><BZ>备注</BZ><QLRID>qlrid1</QLRID><QLID>qlid1</QLID></Row ></RecordSet></QLR></Row></RecordSet></Root>";
			result=shareService.accept(request,result);
		}catch(Exception e){
		}
		return result;
	}
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value = "/send/", method = RequestMethod.GET)
	public @ResponseBody String send(HttpServletRequest request) {
		return "";
//		String strxml=shareService.createXMLtest(xmbh);
//		List<String> list=shareService.createXMLList(xmbh);
//		try {  
//            //服务WSDL Document的地址  
//            URL url = new URL("http://192.168.19.106:8031/ZhxtBdcWebService/RealEstateManage.asmx?wsdl");
//            //1.Qnameqname是qualified name 的简写  
//            //2.构成：由名字空间(namespace)前缀(prefix)以及冒号(:),还有一个元素名称构成  
//            //由发布的wsdl可知namespace为http://server.ws.platform.whaty.com/，  
//            QName qname=new QName("xafgj","RealEstateManage");  
//            RealEstateManage mgr=new RealEstateManage(url,qname);
//            RealEstateManageSoap soap=mgr.getRealEstateManageSoap();
//	   		//调用方法,得到XML格式的返回值
//	   		String strXmlResult=soap.executeBusiness(strxml);
//	   		System.out.println(strXmlResult);
//        } catch (Exception e) {  
//        }
   }
}
