package com.supermap.realestate.registration.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_RKQZB;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

/**
 * 
 * @Description:证书service
 * @author 李桐
 * @date 2015年6月12日 下午3:11:27
 * @Copyright SuperMap
 */
public interface ZSService {

	/**
	 * 更具fzid获取BDCS_DJFZ对象
	 * 
	 * @作者 李桐
	 * @创建时间 2015年6月7日下午8:19:15
	 * @param fzid
	 * @return BDCS_DJFZ
	 */
	public BDCS_DJFZ GetFZList(String fzid);

	/**
	 * 分页获取发证列表
	 * 
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page GetPagedFZList(String xmbh, int page, int rows);

	/**
	 * 添加发证信息
	 * 
	 * @param djfz
	 */
	public void AddFZXX(BDCS_DJFZ djfz);

	/**
	 * 删除发证记录及rkqzb里的发证信息
	 * 
	 * @param djfzid
	 */
	public void DeleteFZXX(String djfzid,String xmbh);

	/**
	 * 添加发证信息到rkqzb
	 * 
	 * @param djfz
	 */
	public void AddFZXXtoRKQZB(String xmbh, String bdcqzh, Date fzsj, String lzrxm, String lzrzjhm, String bdcdylx);


	/**
	 * 通过项目编号从
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午11:22:24
	 * @param xmbh
	 * @return
	 */
	public List<Tree> getZsTreeEx(String xmbh);
 
	
	public List<Tree>getZsTreeCombo(String xmbh,String qllx);
	/**
	 * 根据证书ID获取证书
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月14日上午2:45:48
	 * @param zsid
	 * @return
	 */
	public BDCS_ZS_GZ getZS(String zsid);

	/**
	 * 获取证书表单信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午10:07:49
	 * @param xmbh
	 *            项目编号
	 * @param zsid
	 *            证书ID
	 * @return 证书表单信息
	 */
	public ZS getZSForm(String xmbh, String zsid);
	
	/**
	 * 获取初始登记新建商品房首次登记的证书信息，其中不需要封面信息，不需要二维码信息，附记需要添加预购商品房信息
	 * @作者 海豹
	 * @创建时间 2015年12月5日下午10:00:04
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	public ZS getBuildingZSForm(String xmbh, String zsid);

	/**
	 * 更新证书信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月14日上午2:47:13
	 * @param zs
	 */
	public void updateZS(BDCS_ZS_GZ zs);

	/**
	 *配置流程时，不知道登簿在前还是登簿在后 
	 * @作者 海豹
	 * @创建时间 2015年8月5日上午11:00:25
	 * @param zsid
	 * @param zsbh
	 * @return
	 */
	public boolean updateZs(String zsid,String zsbh);
	
	/**
	 * 保存编号后，维护入库权证管理表
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年10月30日下午17:11:25
	 * @param qzlx
	 * @param zsbh
	 * @return
	 */
	public boolean updateRKQZB(String qzlx, String zsbh);
	public boolean AddSZXXToRKQZB(String qzlx, String zsbh,String qzzl, String zsid, String xmbh, String bdcdylx);
	/**
	 * 获取不动产登记证明
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月15日上午4:23:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getBDCDJZM(String xmbh, String zsid);

	public BDCS_DJFZ getDJFZ(String condition);
	
	public BDCS_RKQZB getRKQZB(String condition);
    /**
     * 在打证环节，通过项目编号查找登记缮证信息，若存在，不做任何处理，若不存在，新增一条信息
     * @作者 海豹
     * @创建时间 2015年9月8日下午2:48:57
     * @param xmbh
     */
	public void updateDJSZ(String xmbh);
	
	/**
	 * 获取证书二维码信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年11月05日12:08:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	public String GetQRCodeInfo(String xmbh, String zsid);

	
	/**
	 * 判断证书是否可以打印
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年11月25日20:59:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	public ResultMessage CanPrintZS(String xmbh, String zsid);
	
	/**
	 * 孝感市获取证书二维码信息
	 * 
	 * @作者 罗雨
	 * @创建时间 2015年11月05日12:08:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	public String GetQRCodeInfoXG(String xmbh, String zsid);
	
	public ResultMessage saveDAGH(String xmbh,String dagh);
	
	public String getDAGH(String xmbh);
	
	public List<ZS> getZsInfoList(String xmbh);

	public String ZsInfoExportExcel(JSONObject ColIndex, JSONObject column, JSONArray rows, HttpServletRequest request) throws FileNotFoundException, IOException;

	public Message GetDYZSList(String xmbh);

	public Page GetPagedFZListEX(String xmbh, Integer page, Integer rows);
	
	//zs_保存编号的验证方法
	public Long validateZSBH(String zsid, String zsbh, String qzlx);
	//zs_的保存方法_已开启zsbh管理
	public ResultMessage saveZSBH(String zsid, String xmbh, String zsbh_all, String qzlx, String qzzl, HttpServletRequest request, String bdcdylx);
	//zs_的保存方法_未开启zsbh管理
	public ResultMessage saveZSBHWithManager(String zsid, String xmbh, String zsbh, String qzlx, String qzzl, String bdcdylx);

}
