package com.supermap.realestate.registration.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

import com.supermap.realestate.registration.ViewClass.DJInfo;
import com.supermap.realestate.registration.ViewClass.JKSInfo;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.SHB;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.model.BDCS_CRHT;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJGDFS;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_IDCARD_PIC;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRHT;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;

/**
 * 
 * @Description:项目相关的，申请人，收费，等等
 * @author 刘树峰
 * @date 2015年6月12日 下午3:09:42
 * @Copyright SuperMap
 */
public interface ProjectService {

	/**
	 * 根据project_id获取项目信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月7日下午4:36:18
	 * @param project_id
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public ProjectInfo getProjectInfo(String project_id, HttpServletRequest request) throws IOException;

	/**
	 * 根据xmbh获取项目信息
	 * 
	 * @作者 王洛燚
	 * @创建时间 2015年12月1日18:18:09
	 * @param xmbh
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public BDCS_XMXX getProjectId(String project_id, HttpServletRequest request) throws IOException;

	/**
	 * 将申请人名称设置为项目名称
	 * 
	 * @param xmbh
	 * @return
	 */
	public void RenameXmmc(String xmbh);

	
	/**
	 * 获取项目的申请人列表
	 * 
	 * @param xmbh
	 * @return
	 */
	public List<BDCS_SQR> getSQRList(String xmbh);
	
	  /**
     * 申请人导入excel,file读取文件
     *
     * @param xmbh
     * @return
     */
    public Message upsqrexcel(MultipartFile file, HttpServletRequest request);


	/**
	 * 根据申请人id获取申请人详细信息
	 * 
	 * @作者 欧展榕
	 * @创建时间 2015年6月7日下午4:37:37
	 * @param sqrid
	 *            申请人id
	 * @return
	 */
	public BDCS_SQR getSQRXX(String sqrid);

	/**
	 * 根据xmbh和sqrxm判断该申请人是否已经存在
	 * 
	 * @作者 欧展榕
	 * @创建时间 2015年6月7日下午4:52:58
	 * @param xmbh
	 * @param sqrxm
	 * @return 申请人id
	 */
	public String hasSQR(String xmbh, String sqrxm, String sqrlb,String zjh);

	/**
	 * 添加申请人信息
	 * 
	 * @作者 欧展榕
	 * @创建时间 2015年6月7日下午5:06:09
	 * @param sqr
	 * @return
	 */
	public String addSQRXX(BDCS_SQR sqr);

	/**
	 * 更新申请人信息
	 * 
	 * @作者 欧展榕
	 * @创建时间 2015年6月7日下午5:51:04
	 * @param sqr
	 * @return
	 */
	public void updateSQRXX(BDCS_SQR sqr);

	/**
	 * 删除申请人信息
	 * 
	 * @作者 欧展榕
	 * @创建时间 2015年6月7日下午8:08:51
	 * @param id
	 */
	public void deleteSQRXX(String sqrid);

	/**
	 * 分页获取收费信息
	 * 
	 * @param xmbh
	 * @return
	 */
	public Message getPagedSFList(String xmbh, int page, int rows);

	/**
	 * 添加收费信息
	 * 
	 * @param sf
	 */
	public void addSFXX(BDCS_DJSF sf);

	/**
	 * 更新收费信息
	 * 
	 * @param sf
	 */
	public void updateSFXX(BDCS_DJSF sf);

	/**
	 * 删除收费信息
	 * 
	 * @param sfid
	 */
	public void deleteSFXX(String sfid);

	/**
	 * 通过项目编号跟流程ID获取BDCS_DJGD信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:47:51
	 * @param xmbh
	 * @param projectid
	 * @return
	 */
	public BDCS_DJGD getDjgdInfo(String xmbh, String projectid);

	/**
	 * 保存或更新BDCS_DJGD信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:48:06
	 * @param bdcs_djgd
	 */
	public ResultMessage saveOrUpdateDjgd(BDCS_DJGD bdcs_djgd, String djgdid);

	/**
	 * 获取项目申请审批表
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午8:48:45
	 * @param xmbh
	 * @return
	 */
	public SQSPB GetSQSPB(String xmbh, String acinstid, HttpServletRequest request);

	public SQSPBex GetSQSPBex(String xmbh, String acinstid, HttpServletRequest request);

	/**
	 * 获取登记归档附属信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日上午6:45:51
	 * @param xmbh
	 * @param project_id
	 * @return
	 */
	public List<BDCS_DJGDFS> getDJGDFSList(String xmbh, String project_id);

	/**
	 * 保存或更新登记归档附属
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日上午7:58:38
	 * @param gdfs
	 */
	public void saveOrUpdateDJGDFS(BDCS_DJGDFS gdfs);

	/**
	 * 获取登记归档附属的详细信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日上午8:22:08
	 * @param xmbh
	 * @param id
	 * @return
	 */
	public BDCS_DJGDFS getDJGDFSInfo(String xmbh, String id);

	/**
	 * 移除登记归档附属的详细信息
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日上午9:09:57
	 * @param xmbh
	 * @param id
	 * @return
	 */
	public ResultMessage deleteDJGDFS(String xmbh, String id);

	/**
	 * 获取地图配置
	 * @return
	 */
	public JSONObject GetMapConfig();

	/**
	 * 获取不动产登记受理单/询问笔录/收费明细单信息
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 下午11:31:18
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	public DJInfo GetDJInfo(String project_id);

	/**
	 * 导出报文xml
	 * @param xmbh
	 * @param path
	 * @param actinstID
	 * @author diaoliwei
	 * @return
	 */
	public Map<String, String> exportXML(String xmbh, String path, String actinstID);

	public Map<String, String> downXmlFile(String path, Map<String, String> xmlNames,String actinstID);

	/**
	 * 通过流程编号删除项目信息
	 * @作者 海豹
	 * @创建时间 2015年7月6日下午9:20:05
	 * @param project_id
	 * @return
	 */
	public Boolean deleteXmxxByPrjID(String project_id);

	public void calculateCharge(String xmbh);

	/**
	 * 获取所有收费定义 不分页
	 * @郭浩龙 获取所有收费项目，除去在登记收费表DJSF中已有的记录（根据项目编号）
	 * @return
	 */
	public Message getSFDYList(String xmbh, Integer page, Integer rows);

	/**
	 * 获取登记收费信息
	 * @Title: getSFXX
	 * @author:liushufeng
	 * @date：2015年7月28日 上午4:16:02
	 * @param id
	 * @return
	 */
	BDCS_DJSF getSFXX(String id);

	/**
	 * 获取项目编号（xmbh）
	 */

	public String GetXmbhData(String file_number);

	/**
	 * 获取申请审核表内容
	 * @param xmbh
	 * @param request
	 * @return
	 */
	public SHB GetSHB(String project_id);

	public ResultMessage setSFHBZS(String xmbh, String sfhbzs);

	/**
	 * 获取登簿信息
	 * @param project_id
	 * @param request
	 * @return
	 */
	public Map<String, Object> GetDBXX(String project_id);

	public HashMap<String, List<HashMap<String, Object>>> GetDBXX2(String project_id);
	public ResultMessage setDJSFTS(String djsfid, int ts, Map<String, Object> param);

	/**
	 * @作者 buxiaobo
	 * @创建时间 2015年12月5日 22:13:31 根据project_id返回缴款书相应信息
	 * @param project_id
	 *            进入的原数据
	 * @return JKSInfo缴款书list
	 * @throws SQLException
	 */
	public JKSInfo GetJKSInfo(String project_id,String type);

	/**
	 * 获取档案柜号
	 * 
	 * @author:yuxuebin
	 * @param ywlsh
	 * @return
	 */
	public ResultMessage GetDAGH(String ywlsh);

	/**
	 * 保存档案柜号
	 * 
	 * @author:yuxuebin
	 * @param ywlsh
	 * @param dagh
	 * @return
	 */
	public ResultMessage SaveDAGH(String ywlsh, Integer dagh);

	/**
	 * 办结项目
	 * @Title: finishProject
	 * @author:liushufeng
	 * @date：2015年12月27日 上午12:04:26
	 * @param file_number
	 * @return
	 */
	public ResultMessage finishProject(String file_number);

	
	/**
	 * 删除申请人信息
	 * 
	 * @作者 赵梦帆
	 * @创建时间 2016-10-17 17:14:57
	 * @param id
	 */
	void deleteSQRPIC(String zjh);

	/*
	 * 赵梦帆
	 * 2016-10-17 20:39:27
	 */
	public List<BDCS_IDCARD_PIC> LoadSQRPIC(String zjh);
	
	
	/**
	 * 通过权利人 义务人 证件号 权证号 坐落查询业务流水号   逗号分隔：
	 * @date   2016年11月8日 下午12:34:52
	 * @author JHX
	 * @param qlr
	 * @param ywr
	 * @param zjh
	 * @param qzh
	 * @param zl
	 * @return String 
	 */
	public String getYwlshByCondtionsEx(String qlr,String ywr,String zjh,String qzh,String zl,boolean PreciseQueryQLR);
    /**
     * 获取合同信息
     * @param projectId
     * @return
     */

	public List<HashMap<String, String>> getCRHTInfo(String projectId);

	public List<HashMap<String,String>> getCRHT(String projectId);
	
	public BDCS_CRHT updateCRHT (HashMap<String,String> bdcsCrht,String projectId);
	
	public void delCRHT(String projectId);

	public List<HashMap<String, String>> getZRHT(String projectId);
	
	public BDCS_ZRHT updateZRHT (HashMap<String,String> bdcsCrht,String projectId);
	
	public void delZRHT(String projectId);
	
	public String GetDBJS(String xmbh, String actinst_id);

	public ResultMessage UpdateDBJS(String xmbh, String actinst_id, String dbjs);

	public ResultMessage PlDelSflist(String xmbh, HttpServletRequest request);

	public ResultMessage UpdateSfjbOnDJSF(String djsfid, String sfjb);

	public List<HashMap<String, String>> getTDZRInfo(String projectId);
	//创建项目信息和申请人信息 
	public ResultMessage createXmxxAndSqr(String proisntId,String fileNumber,List<Map<String , String> > sqrlist, HttpServletRequest request);

	public void Updatezdbtn(String project_id, HttpServletRequest request, Boolean type);

	public List<Map<String, String>> GetSQSPBexNJ(String xmbh, String acinstid,
			HttpServletRequest request); 
	
	public void UpdateXMXX(String lsh,String ajh);

	public void Jlsfzdh(String xmbh,int page,int rows);

	public void SaveSfdy(String xmbh,int page,int rows,String SFDLMC,String sfdyid,String CZFSRS);

	public void SaveSfxx(String djsfid, BDCS_XMXX xmxx, String SFKMMC, String SFJSALL,
			String SFLX, String sfdyid, String SFDLMC, int page, int rows,
			String xmbh,String CZFSRS);
	
	public String getYwlshByCondtionsPl(String operaStaffString,int protype ,String qlr,String ywr,String zjh,String qzh,String zl,String bdcdyh,boolean PreciseQueryQLR);
	
	/**
	 * 更正和预告登记的申请人页面，新增、更新或删除义务人时，把所有义务人更新到FSQL_GZ的YWR字段
	 * 
	 * @author:heks
	 * @date 2017-07-11
	 * @param xmbh
	 */
	public void updateFsqlYWR(String xmbh);

	public ResultMessage sfqr(HttpServletRequest request, String xmbh);
	public ResultMessage CheckSfqr(HttpServletRequest request);
}
