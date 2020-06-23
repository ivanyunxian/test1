package com.supermap.wisdombusiness.workflow.service.wfi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Bank_Trustbook;
import com.supermap.wisdombusiness.workflow.model.Bank_TrustbookPage;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProMater;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterClass;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.util.FileUpload;

/**
 * @author 李想
 * @date 2015-5-22
 */
@Component("smProMater")
public class SmProMater {

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private SmStaff smStaff;
	
	@Autowired
	private SmProInst smProInst;

	/**
	 * 通过file_number获取流程资料信息
	 */
	public List<Wfi_ProMater> GetProMaterList(String file_Number) {
		StringBuilder str = new StringBuilder();
		str.append("File_Number='");
		str.append(file_Number);
		str.append("'");
		List<Wfi_ProInst> list = commonDao.findList(Wfi_ProInst.class, str.toString());
		if (list.size() > 0)
			return GetProMaterInfo(list.get(0).getProinst_Id());
		else {
			return new ArrayList<Wfi_ProMater>();
		}
	}

	public List<Tree> GetMaterDataTree(String file_Number, boolean isAccept) {
		return GetMaterDataTree(file_Number, isAccept, null);
	}

	public List<Tree> GetMaterDataTree(String file_Number, boolean isAccept, String clear) {
		List<Tree> treeList = new ArrayList<Tree>();
		List<Wfi_ProMater> list = GetProMaterList(file_Number);
		for (int i = 0; i < list.size(); i++) {
			Wfi_ProMater ProMater = list.get(i);
			if (isAccept && ProMater.getMaterial_Status() < 2) {
				continue;
			}
			Tree tree = new Tree();
			tree.setId(ProMater.getMaterilinst_Id());
			tree.setText(ProMater.getMaterial_Name());
			tree.setType("folder");
			tree.setFlag(ProMater.getMaterial_Isdossier());// 是否归档
			if (ProMater.getMaterial_Index() != null) {
				tree.setTag3(ProMater.getMaterial_Index() + "");
			} else {
				tree.setTag3("");
			}

			if (ProMater.getMaterial_Type()!=null) {
				tree.setTag1(ProMater.getMaterial_Type().toString());
			}else {
				tree.setTag1("2");
			}
			if (ProMater.getMaterial_Count() != null) {
				tree.setTag2(ProMater.getMaterial_Count().toString());
			}

			tree.children = getfilesByfolderID(ProMater.getMaterilinst_Id());
			if (clear != null && !clear.equals("") && !clear.equals("false") && tree.children.size() == 0) {
				continue;
			}
			treeList.add(tree);
		}
		return treeList;
	}

	/**
	 * 获取工作流的所有文件夹 isAccept 否只获取收件状态的文件夹
	 * */
	public List<Tree> getFolder(String file_Number, boolean isAccept) {
		List<Tree> treeList = new ArrayList<Tree>();
		List<Wfi_ProMater> list = GetProMaterList(file_Number);
		for (int i = 0; i < list.size(); i++) {
			Wfi_ProMater ProMater = list.get(i);
			if (isAccept && ProMater.getMaterial_Status() < 2) {
				continue;
			}

			Tree tree = new Tree();
			tree.setId(ProMater.getMaterilinst_Id());
			tree.setText(ProMater.getMaterial_Name());
			tree.setisParent(true);
			tree.setType("folder");
			treeList.add(tree);
		}
		return treeList;
	}

	public List<Tree> getfilesByfolderID(String id) {
		List<Tree> childList = new ArrayList<Tree>();
		if (id != null && !id.equals("")) {
			StringBuilder str = new StringBuilder();
			str.append("Materilinst_Id='");
			str.append(id);
			str.append("' order by file_index,upload_date");
			List<Wfi_MaterData> listChildDatas = commonDao.findList(Wfi_MaterData.class, str.toString());
			for (int j = 0; j < listChildDatas.size(); j++) {
				Wfi_MaterData MaterData = listChildDatas.get(j);
				Tree newtree = new Tree();
				newtree.setId(MaterData.getMaterialdata_Id());
				newtree.setText(MaterData.getFile_Name());
				newtree.setType("file");
				newtree.setTag1(MaterData.getPath());
				newtree.setTag2(MaterData.getFile_Path());
				newtree.setTag4(MaterData.getDisc());
				childList.add(newtree);
			}
		}
		return childList;
	}

	public List<Tree> getbanktrustbookfilesByfolderID(String trustbook_id) {
		List<Tree> childList = new ArrayList<Tree>();
		List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(Bank_Trustbook.class, "select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE trustbook_id='" + trustbook_id + "'");
		String trustbookpage_id = bank_Trustbooks.get(0).getTrustbookpage_Id();
		List<Bank_TrustbookPage> bank_TrustbookPages = commonDao.getDataList(Bank_TrustbookPage.class, "select * from BDC_WORKFLOW.BANK_TRUSTBOOKPAGE  WHERE trustbookpage_id='" + trustbookpage_id + "'");
		Bank_TrustbookPage data = new Bank_TrustbookPage();
		if (bank_TrustbookPages != null && bank_TrustbookPages.size() > 0) {
			data = bank_TrustbookPages.get(0);
		} else {
			return null;
		}
		for (int j = 0; j < bank_TrustbookPages.size(); j++) {
			Bank_TrustbookPage banktrustbook = bank_TrustbookPages.get(j);
			Tree newtree = new Tree();
			newtree.setId(banktrustbook.getTrustbookdata_id());
			newtree.setText(banktrustbook.getTrustbookpage_Path());
			newtree.setType("file");
			childList.add(newtree);
		}
		return childList;
	}

	/**
	 * 通过Pordef_ID从资料定义表获取所有关联的资料信息
	 * 
	 * @param Pordef_ID
	 *            流程定义内码
	 * @return Wfd_ProMater的List
	 */
	public List<Wfd_ProMater> GetWfdProMaterInfo(String Pordef_ID) {
		StringBuilder _nowhereStr = new StringBuilder();
		_nowhereStr.append("PRODEF_ID='");
		_nowhereStr.append(Pordef_ID);
		_nowhereStr.append("' ORDER BY MATERIAL_INDEX");
		List<Wfd_ProMater> list = commonDao.getDataList(Wfd_ProMater.class, Common.WORKFLOWDB + "WFD_PROMATER", _nowhereStr.toString());
		return list;
	}

	/**
	 * 使用Wfd_ProMater实体对象给Wfi_ProMater实体对象赋值
	 * 
	 * @param _Wfi_ProMater
	 *            流程资料实例 实体对象
	 * @param _Wfd_ProMater
	 *            流程资料定义 实体对象
	 * @return Wfi_ProMater 流程资料实例 实体对象
	 */
	public Wfi_ProMater GetInfoFromWfdProMater(Wfi_ProMater _Wfi_ProMater, Wfd_ProMater _Wfd_ProMater, Integer index) {
		if (_Wfd_ProMater != null) {
			_Wfi_ProMater.setMaterial_Count(_Wfd_ProMater.getMaterial_Count());
			_Wfi_ProMater.setMaterial_Desc(_Wfd_ProMater.getMaterial_Desc());
			_Wfi_ProMater.setMaterial_Index(index);
			_Wfi_ProMater.setMaterial_Id(_Wfd_ProMater.getMaterial_Id());
			_Wfi_ProMater.setMaterial_Name(_Wfd_ProMater.getMaterial_Name());
			_Wfi_ProMater.setMaterial_Pagecount(_Wfd_ProMater.getMaterial_Pagecount());
			_Wfi_ProMater.setMaterial_Need(_Wfd_ProMater.getMaterial_Need());
			if (_Wfi_ProMater.getMaterial_Need() == 1) {
				_Wfi_ProMater.setMaterial_Status(WFConst.MateralStatus.VirtualAccept.value);// 虚拟收件
			} else if (_Wfi_ProMater.getMaterial_Need() == 2) {// 补收收件
				_Wfi_ProMater.setMaterial_Status(WFConst.MateralStatus.Supply.value);
			} else {
				_Wfi_ProMater.setMaterial_Status(WFConst.MateralStatus.CeratMateral.value);
			}
			_Wfi_ProMater.setMaterial_Type(_Wfd_ProMater.getMaterial_Type());
			_Wfi_ProMater.setMaterialdef_Id(_Wfd_ProMater.getMaterialdef_Id());
			_Wfi_ProMater.setDossier_Index(_Wfd_ProMater.getDossier_Index());
			_Wfi_ProMater.setMaterial_Isdossier(_Wfd_ProMater.getMaterial_Isdossier());
			_Wfi_ProMater.setMaterial_Bm(_Wfd_ProMater.getMaterial_Bm());
		}
		return _Wfi_ProMater;
	}

	/**
	 * 
	 * @param Pordef_ID
	 *            流程定义内码
	 * @param Proinst_ID
	 *            流程实例内码
	 * @return Wfi_ProMater 流程资料实例List
	 */
	public List<Wfi_ProMater> CreateProMaterInst(String Pordef_ID, String Proinst_ID , String MaterClass_id) {
		List<Wfi_ProMater> wfilist = new ArrayList<Wfi_ProMater>();
		List<Wfd_ProMater> list = GetWfdProMaterInfo(Pordef_ID);
		/**
		 * 赣州需求 添加一项申请人图形
		 * */
		if (true) {
			Wfi_ProMater sqrtx = new Wfi_ProMater();
			sqrtx.setMaterial_Count(1);
			sqrtx.setMaterial_Desc("采集头像数据");
			sqrtx.setMaterial_Index(0);
			// sqrtx.setMaterial_Id("0");
			sqrtx.setMaterial_Name("申请人图像 ");
			sqrtx.setMaterial_Pagecount(1);
			sqrtx.setMaterial_Need(1);
			sqrtx.setMaterial_Type(3);
			sqrtx.setMaterialdef_Id("");
			sqrtx.setMaterial_Status(WFConst.MateralStatus.CeratMateral.value);
			sqrtx.setProinst_Id(Proinst_ID);
			sqrtx.setMaterialtype_Id(MaterClass_id);
			commonDao.save(sqrtx);
			wfilist.add(sqrtx);
		}
		for (int i = 0; i < list.size(); i++) {
			Wfi_ProMater _ProMater = new Wfi_ProMater();
			Wfd_ProMater _Wfd_ProMater = list.get(i);
			GetInfoFromWfdProMater(_ProMater, _Wfd_ProMater, i + 1);
			_ProMater.setProinst_Id(Proinst_ID);
			_ProMater.setMaterialtype_Id(MaterClass_id);
			commonDao.save(_ProMater);
			wfilist.add(_ProMater);
		}
		return wfilist;
	}

	/**
	 * 新建并获取流程的资料信息
	 * 
	 * @param Proinst_ID
	 *            流程实例内码
	 * @return Wfi_ProMater 流程资料实例List
	 */
	public List<Wfi_ProMater> GetProMaterInfo(String Proinst_ID) {
		StringBuilder _nowhereStr = new StringBuilder();
		_nowhereStr.append("PROINST_ID='");
		_nowhereStr.append(Proinst_ID);
		_nowhereStr.append("' ORDER BY DOSSIER_INDEX, MATERIAL_INDEX");
		List<Wfi_ProMater> list = commonDao.getDataList(Wfi_ProMater.class, Common.WORKFLOWDB + "WFI_PROMATER", _nowhereStr.toString());
		return list;
	}

	/**
	 * 删除该流程所有资料信息
	 * 
	 * @param Proinst_ID
	 *            流程实例内码
	 * @return void
	 */
	public void DeleteProMaterInfo(String Proinst_ID) {
		StringBuilder _sqlStr = new StringBuilder();
		_sqlStr.append("DELETE　" + Common.WORKFLOWDB + "WFI_PROMATE　WHERE　PROINST_ID=''");
		_sqlStr.append(Proinst_ID);
		_sqlStr.append("'");
		commonDao.deleteQuery(_sqlStr.toString());
		commonDao.flush();
	}

	/**
	 * 通过内码删除单个流程资料信息
	 * 
	 * @param ID
	 *            流程资料内码
	 * @return void
	 */
	public void DeleteProMaterById(String ID) {
		StringBuilder _sqlStr = new StringBuilder();
		_sqlStr.append("DELETE　" + Common.WORKFLOWDB + "WFI_PROMATE　WHERE　MATERILINST_ID=''");
		_sqlStr.append(ID);
		_sqlStr.append("'");
		commonDao.deleteQuery(_sqlStr.toString());
		commonDao.flush();
	}

	/**
	 * 获取流程资料清单
	 * 
	 * @param prodef_id
	 * @return
	 */
	public StringBuffer getProMate(String actinst_id) {
		String id = "";
		String name = "";
		StringBuffer folder = new StringBuffer();
		StringBuffer fileBuffer = new StringBuffer();
		StringBuffer json = new StringBuffer();

		List<Wfi_ProMater> wfiProMater = commonDao.getDataList(Wfi_ProMater.class, "select a.* from " + Common.WORKFLOWDB + "WFI_PROMATER a," + Common.WORKFLOWDB + "WFI_ACTINST c where a.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '" + actinst_id + "' order by a.MATERIAL_INDEX");

		folder.append("{");
		for (int i = 0; i < wfiProMater.size(); i++) {
			Wfi_ProMater list = wfiProMater.get(i);
			id = list.getMaterilinst_Id();
			name = list.getMaterial_Name();
			folder.append("\"" + id + "\":{");
			folder.append("\"name\":\"" + name + "\"");
			if (i == wfiProMater.size() - 1) {
				folder.append("}");
			} else {
				folder.append("},");
			}

		}
		folder.append("}");

		String patch = "";
		String fix = "";
		fileBuffer.append("{");
		List<Wfi_MaterData> materData = commonDao.getDataList(Wfi_MaterData.class, "select a.* from " + Common.WORKFLOWDB + "WFI_MATERDATA a," + Common.WORKFLOWDB + "WFI_PROMATER b," + Common.WORKFLOWDB + "WFI_ACTINST c where a.MATERILINST_ID = b.MATERILINST_ID and b.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '" + actinst_id + "' order by b.MATERIAL_INDEX,a.MATERIALDATA_ID");
		for (int j = 0; j < materData.size(); j++) {
			Wfi_MaterData list = materData.get(j);
			id = list.getMaterialdata_Id();
			name = list.getFile_Name();
			patch = list.getMaterilinst_Id();
			fix = list.getFile_Postfix();
			fileBuffer.append("\"" + id + "\":{");
			fileBuffer.append("\"name\":\"" + name + "\"");
			fileBuffer.append(",\"patch\":\"" + patch + "\"");
			fileBuffer.append(",\"fix\":\"" + fix + "\"");
			if (j == materData.size() - 1) {
				fileBuffer.append("}");
			} else {
				fileBuffer.append("},");
			}
		}
		fileBuffer.append("}");

		json.append("{");
		json.append("\"folder\":" + folder);
		json.append(",\"files\":" + fileBuffer);
		json.append("}");
		return json;
	}

	/**
	 * 获取资料
	 * */
	public Map<String, Object> getProMateEx(String actinst_id) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		List<Wfi_ProMater> wfiProMater = commonDao.getDataList(Wfi_ProMater.class, "select a.* from " + Common.WORKFLOWDB + "WFI_PROMATER a," + Common.WORKFLOWDB + "WFI_ACTINST c where a.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '" + actinst_id + "' order by a.MATERIAL_INDEX");
		List<Wfi_ActInst> wfi_actinst = commonDao.getDataList(Wfi_ActInst.class, "select * from " + Common.WORKFLOWDB + "WFI_ACTINST where  actinst_id='" + actinst_id + "'");
		List<Wfi_MaterData> materData = commonDao.getDataList(Wfi_MaterData.class, "select a.* from " + Common.WORKFLOWDB + "WFI_MATERDATA a," + Common.WORKFLOWDB + "WFI_PROMATER b," + Common.WORKFLOWDB + "WFI_ACTINST c where a.MATERILINST_ID = b.MATERILINST_ID and b.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '" + actinst_id + "' order by  b.MATERIAL_INDEX,a.file_index");
		List<Wfi_MaterClass> materClass = commonDao.getDataList(Wfi_MaterClass.class, "select a.* from " + Common.WORKFLOWDB + "WFI_MATERCLASS a," + Common.WORKFLOWDB + "WFI_ACTINST c where a.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '" + actinst_id + "' order by a.MATERIALTYPE_INDEX");
		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		if(XZQHDM.indexOf("45")==0){//广西需求，收费单显示权利人、义务人、代理人的电话号码
			String sqrSql = "select d.* from bdc_workflow.wfi_actinst a join bdc_workflow.wfi_proinst b on a.proinst_id = b.proinst_id join bdck.bdcs_xmxx c on b.file_number = c.project_id  join bdck.bdcs_sqr d on c.xmbh = d.xmbh where a.actinst_id = '" + 
				      actinst_id + "' order by sqrlb";
			List sqrs = this.commonDao.getDataListByFullSql(sqrSql);
			mapList.put("sqrs", sqrs);
			//获取义务人的代理人
			if (!StringHelper.isEmpty(sqrs)) {
				List<Map> sqrmap = sqrs;
				String xmbh = null;
				if(sqrmap != null&&sqrmap.size()>0){
					xmbh=sqrmap.get(0).get("XMBH").toString();
				}		
				if (!StringHelper.isEmpty(xmbh)) {
					String sql = ProjectHelper.GetXMBHCondition(xmbh);
					List<BDCS_SQR> sqrList = commonDao.findList(BDCS_SQR.class, sql + " ORDER BY SXH");
					for (BDCS_SQR bdcs_sqr : sqrList) {
						if ("2".equals(bdcs_sqr.getSQRLB())) {//如果是义务人
							mapList.put("ywrdlrlxdh",bdcs_sqr.getDLRLXDH());//义务人代理人联系电话
							mapList.put("ywrdlrxm",bdcs_sqr.getDLRXM());//义务人代理人姓名
							break;
						}
					}
				}
				
			}
		}
		mapList.put("folder", wfiProMater);
		mapList.put("file", materData);
		mapList.put("folderClass", materClass);
		mapList.put("actDef_Type", wfi_actinst);
		return mapList;
	}
/**
 * 收件资料上传(本机硬盘上传)
 * @param file 
 * @param materData
 * @param request
 * @param response
 */
	public void updateLoadImage(MultipartFile file, Wfi_MaterData materData, HttpServletRequest request, HttpServletResponse response, boolean thumb) {
		try {
			String materilinst_id = materData.getMaterilinst_Id();
			Wfi_ProMater Wfi_ProMater = commonDao.get(Wfi_ProMater.class, materilinst_id);
			List<Map> fileName = null;
			if (Wfi_ProMater != null) {
				//1.图片上传到硬盘
				Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, Wfi_ProMater.getProinst_Id());
				fileName = FileUpload.uploadFile(file, materilinst_id, inst);
			}

			if (fileName != null && fileName.size() > 0) {
				//2.数据库操作
				boolean update = true;
				for (int i = 0; i < fileName.size(); i++) {
					Map name = fileName.get(i);
					Wfi_MaterData materDataOracle = getMaterData(materData.getMaterialdata_Id());
					if (materDataOracle != null && update) {
						//重复上传文件-覆盖
						materDataOracle.setFile_Name(name.get("filename")+"");
						materDataOracle.setFile_Path(name.get("filename")+"");
						materDataOracle.setPath(name.get("filepath")+"");
						// olddata.setUpload_Date(new Date());
						commonDao.update(materDataOracle);
					} else {
						//增加新的数据行
						update = false;
						//Wfi_MaterData materData_add = new Wfi_MaterData();
						materDataOracle=new Wfi_MaterData();
						if (i == 0) {
							materDataOracle.setMaterialdata_Id(materData.getMaterialdata_Id());
						} else {
							materDataOracle.setMaterialdata_Id(Common.CreatUUID());
						}
						materDataOracle.setMaterilinst_Id(materData.getMaterilinst_Id());
						if (materData.getFile_Name() != null && !materData.getFile_Name().equals("")) {
							materDataOracle.setFile_Name(materData.getFile_Name());
						} else {
							materDataOracle.setFile_Name(StringHelper.formatObject(name.get("filename")));
						}
						materDataOracle.setFile_Index(materData.getFile_Index());
						materDataOracle.setUpload_Name(materData.getUpload_Name());
						materDataOracle.setUpload_Id(materData.getUpload_Id());
						materDataOracle.setFile_Path(StringHelper.formatObject(name.get("filename")));
						materDataOracle.setPath(StringHelper.formatObject(name.get("filepath")));
						materDataOracle.setDisc(StringHelper.formatObject(name.get("disc")));
						if (thumb) {
							try {
								// materData_add.setThumb(CreatThumb(file));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							// materData_add.setThumb(materData.getThumb());
						}

						materDataOracle.setUpload_Date(new Date());
						materDataOracle.setDisc(StringHelper.formatObject(name.get("disc")));
						commonDao.save(materDataOracle);

					}
					commonDao.flush();
				}
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(materData.getMaterialdata_Id());

			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	// 银行委托书属性信息保存、图片上传
	public void updateTrustBookImage(String trustbookid, MultipartFile file, Bank_TrustbookPage trustbookpage, HttpServletRequest request, HttpServletResponse response, boolean thumb) {
		try {
			Bank_Trustbook trustbook = commonDao.get(Bank_Trustbook.class, trustbookid);
			String Trustbookpage_Id = trustbook.getTrustbookpage_Id();
			// 先将文件保存到服务器
			List<String> fileName1 = FileUpload.uploadtrustbook(file, Trustbookpage_Id);
			if (fileName1.size() > 0) {
				for (int i = 0; i < fileName1.size(); i++) {
					// 在数据库中记录文件存储的路径
					Bank_TrustbookPage trustbookpage_add = new Bank_TrustbookPage();
					if (i == 0) {
						trustbookpage_add.setTrustbookdata_id(trustbookpage.getTrustbookdata_id());
					} else {
						trustbookpage_add.setTrustbookdata_id(Common.CreatUUID());
					}
					trustbookpage_add.setTrustbookpage_Id(trustbookpage.getTrustbookpage_Id());
					trustbookpage_add.setTrustbookpage_Id(trustbook.getTrustbookpage_Id());
					trustbookpage_add.setTrustbookpage_Index(trustbookpage.getTrustbookpage_Index());
					trustbookpage_add.setUpload_Name(trustbookpage.getUpload_Name());
					trustbookpage_add.setTrustbookpage_Path(fileName1.get(i));
					trustbookpage_add.setUpload_Date(new Date());
					commonDao.save(trustbookpage_add);
					commonDao.flush();
				}
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(trustbookpage.getTrustbookdata_id());
			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String CreatThumb(MultipartFile file) throws Exception {

		return null;

		/*
		 * File thumbfile=new File("/thumb.jpg"); byte[] buffer = null; try {
		 * FileInputStream fis = new FileInputStream(thumbfile);
		 * ByteArrayOutputStream bos = new ByteArrayOutputStream(); byte[] b =
		 * new byte[1024]; int n; while ((n = fis.read(b)) != -1) { bos.write(b,
		 * 0, n); } fis.close(); bos.close(); buffer = bos.toByteArray(); }
		 * catch (FileNotFoundException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */

		// 对字节数组Base64编码

	}

	public String makeSmallImage(MultipartFile files) throws Exception {
		String basicPath = ConfigHelper.getNameByValue("material");// GetProperties.getConstValueByKey("material");
		FileOutputStream fileOutputStream = null;
		JPEGImageEncoder encoder = null;
		BufferedImage tagImage = null;
		Image image = null;
		try {
			CommonsMultipartFile cf = (CommonsMultipartFile) files; // 这个myfile是MultipartFile的
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File file = fi.getStoreLocation();
			// Before is how to change ByteArray back to Image
			ByteArrayInputStream bis = new ByteArrayInputStream(File2byte(file));
			Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
			// ImageIO is a class containing static convenience methods for
			// locating ImageReaders
			// and ImageWriters, and performing simple encoding and decoding.

			ImageReader reader = (ImageReader) readers.next();
			Object source = bis; // File or InputStream, it seems file is OK

			ImageInputStream iis = ImageIO.createImageInputStream(source);
			// Returns an ImageInputStream that will take its input from the
			// given Object

			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();

			image = reader.read(0, param);

			int srcWidth = image.getWidth(null);// 原图片宽度
			int srcHeight = image.getHeight(null);// 原图片高度
			int dstMaxSize = 120;// 目标缩略图的最大宽度/高度，宽度与高度将按比例缩写
			int dstWidth = srcWidth;// 缩略图宽度
			int dstHeight = srcHeight;// 缩略图高度
			float scale = 0;
			// 计算缩略图的宽和高
			if (srcWidth > dstMaxSize) {
				dstWidth = dstMaxSize;
				scale = (float) srcWidth / (float) dstMaxSize;
				dstHeight = Math.round((float) srcHeight / scale);
			}
			srcHeight = dstHeight;
			if (srcHeight > dstMaxSize) {
				dstHeight = dstMaxSize;
				scale = (float) srcHeight / (float) dstMaxSize;
				dstWidth = Math.round((float) dstWidth / scale);
			}
			// 生成缩略图
			tagImage = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);
			tagImage.getGraphics().drawImage(image, 0, 0, dstWidth, dstHeight, null);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(tagImage, "jpg", out);
			byte[] b = out.toByteArray();
			return "data:image/jpeg;base64," + new String(Base64.encodeBase64(b));
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
				}
				fileOutputStream = null;
			}
			encoder = null;
			tagImage = null;
			image = null;
			System.gc();
		}
	}

	public void makeSmallImage(String basicPath, String relationath, String dstImageFileName) throws Exception {
		// String basicPath =ConfigHelper.getNameByValue("material");//
		// GetProperties.getConstValueByKey("material");
		FileOutputStream fileOutputStream = null;
		JPEGImageEncoder encoder = null;
		BufferedImage tagImage = null;
		Image srcImage = null;
		try {
			File file = new File(basicPath + "/" + relationath);
			srcImage = ImageIO.read(file);
			int srcWidth = srcImage.getWidth(null);// 原图片宽度
			int srcHeight = srcImage.getHeight(null);// 原图片高度
			int dstMaxSize = 120;// 目标缩略图的最大宽度/高度，宽度与高度将按比例缩写
			int dstWidth = srcWidth;// 缩略图宽度
			int dstHeight = srcHeight;// 缩略图高度
			float scale = 0;
			// 计算缩略图的宽和高
			if (srcWidth > dstMaxSize) {
				dstWidth = dstMaxSize;
				scale = (float) srcWidth / (float) dstMaxSize;
				dstHeight = Math.round((float) srcHeight / scale);
			}
			srcHeight = dstHeight;
			if (srcHeight > dstMaxSize) {
				dstHeight = dstMaxSize;
				scale = (float) srcHeight / (float) dstMaxSize;
				dstWidth = Math.round((float) dstWidth / scale);
			}
			// 生成缩略图
			tagImage = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);
			tagImage.getGraphics().drawImage(srcImage, 0, 0, dstWidth, dstHeight, null);
			fileOutputStream = new FileOutputStream(dstImageFileName);
			encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);

			encoder.encode(tagImage);
			fileOutputStream.close();
			fileOutputStream = null;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
				}
				fileOutputStream = null;
			}
			encoder = null;
			tagImage = null;
			srcImage = null;
			System.gc();
		}
	}

	private byte[] File2byte(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	private File byte2File(byte[] buf) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		String filePath = "/tmp";
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File("/tumb.jpg");
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	// 上传图像
	public void uploadPhoto(Wfi_MaterData materData) {
		Wfi_ProMater promater = commonDao.get(Wfi_ProMater.class, materData.getMaterilinst_Id());
		if (promater != null) {

			Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, promater.getProinst_Id());
			List<Map> fileNames = FileUpload.GenerateImage(materData.getThumb(), materData.getMaterilinst_Id(), materData.getFile_Name(), inst);
			if (fileNames != null && fileNames.size() > 0) {
				for (int i = 0; i < fileNames.size(); i++) {
					Map name = fileNames.get(i);
					Wfi_MaterData materData_add = new Wfi_MaterData();
					materData_add.setMaterialdata_Id(materData.getMaterialdata_Id());
					materData_add.setMaterilinst_Id(materData.getMaterilinst_Id());
					materData_add.setFile_Name(materData.getFile_Name());
					materData_add.setFile_Index(materData.getFile_Index());
					materData_add.setUpload_Name(materData.getUpload_Name());
					materData_add.setFile_Path(name.get("filename").toString());
					materData_add.setPath(name.get("filepath").toString());
					materData_add.setUpload_Id(materData.getUpload_Id());
					materData_add.setDisc(ConfigHelper.getNameByValue("MATERIAL"));
					commonDao.save(materData_add);
					commonDao.flush();
				}

			}

		}

	}

	public Wfi_MaterData uploadMore(MultipartFile file, Wfi_MaterData materData, HttpServletRequest request, HttpServletResponse response) {
		String tmpFolder = "tmpFile";
		List<Map> fileName = null;
		try {
			Wfi_ProMater Wfi_ProMater = commonDao.get(Wfi_ProMater.class, materData.getMaterilinst_Id());
			List<String> fileName1 = null;
			if (Wfi_ProMater != null) {
				Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, Wfi_ProMater.getProinst_Id());
				fileName = FileUpload.uploadFile(file, tmpFolder, inst);
			}

			if (fileName.size() > 0) {
				for (int i = 0; i < fileName.size(); i++) {
					Map map = fileName.get(i);
					materData.setFile_Path(map.get("filename").toString());
					materData.setUpload_Status(WFConst.Upload_Status.NotGroup.value);
					materData.setPath(map.get("filepath").toString());
					commonDao.save(materData);
					commonDao.flush();
				}

			}
		} catch (Exception ex) {

		}
		return materData;

	}

	/** 文件分组 */
	public boolean uploadgroup(String metardataid, String metarinstid) {
		boolean result = false;
		Wfi_MaterData materData = commonDao.get(Wfi_MaterData.class, metardataid);
		if (materData != null) {
			if (materData.getUpload_Status() == WFConst.Upload_Status.NotGroup.value) {

				result = FileUpload.FileGroup(materData, metarinstid);
				if (result) {
					materData.setMaterilinst_Id(metarinstid);
					materData.setUpload_Status(WFConst.Upload_Status.Normal.value);
					commonDao.update(materData);
					commonDao.flush();
				}
			}
		}
		return result;
	}

	public boolean deleteImage(HttpServletRequest request, String materilinst_id, String materialdata_id) {
		boolean flag = false;
		String fileName = "";
		Wfi_MaterData materData = commonDao.get(Wfi_MaterData.class, materialdata_id);
		if (materData != null) {
			fileName = materData.getFile_Path();

		}
		if ((fileName != null && !fileName.equals("")) || materData.getFile_Number() != null) {

			flag = FileUpload.deteleImage(request, materilinst_id, materData.getFile_Name());
			if (flag) {
				commonDao.delete(Wfi_MaterData.class, materialdata_id);
				commonDao.flush();
			}
		} else {
			flag = false;
		}
		return flag;
	}

	public boolean deleteFile(HttpServletRequest request, String materilinst_ids) {
		boolean flag = false;
		if (!materilinst_ids.equals("")) {
			String[] ids = materilinst_ids.split(",");
			for (String id : ids) {
				if (!id.equals("")) {
					commonDao.delete(Wfi_ProMater.class, id);
					commonDao.deleteQuery("delete from " + Common.WORKFLOWDB + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
				}
			}
			commonDao.flush();
			flag = true;
			YwLogUtil.addYwLog("删除收件资料", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		}
		return flag;

	}

	/**
	 * 重写deleteFile方法
	 * 
	 * **/
	public SmObjInfo deleteFile2(HttpServletRequest request, String materilinst_ids,String delfile) {
		SmObjInfo smObjInfomsg = new SmObjInfo();
		String currentPsnId = smStaff.getCurrentWorkStaffID();
		if (!materilinst_ids.equals("")) {
			String[] ids = materilinst_ids.split(",");
			boolean isDelete;
			for (String id : ids) {
				isDelete = true;
				if (!id.equals("")) {
					String powerDel = ConfigHelper.getNameByValue("powerDel");
					Wfi_ProMater promaster = commonDao.get(Wfi_ProMater.class, id);
					commonDao.delete(Wfi_ProMater.class, id);
					if(!"1".equals(powerDel)){	
						// 判断文件夹中是否有别人上传的问题件 ,有的话不能删除
						List<Wfi_MaterData> list = commonDao.getDataList(Wfi_MaterData.class, "select * from " + Common.WORKFLOWDB + "" + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
						for (int i = 0; i < list.size(); i++) {
							if (!currentPsnId.equals(list.get(i).getUpload_Id())) {
								isDelete = false;
								break;
							}
						}
						if(delfile!=null){
							isDelete=true;
						}
						if (isDelete) {
							commonDao.deleteQuery("delete from " + Common.WORKFLOWDB + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
							smObjInfomsg.setDesc("删除成功");
							smObjInfomsg.setConfirm("OK");
						} else {
							smObjInfomsg.setDesc("文件" + promaster.getMaterial_Name() + "中包含他人上传的文件，不能删除");
							smObjInfomsg.setConfirm("ERROR");
							return smObjInfomsg;
						}
					}else{
						commonDao.deleteQuery("delete from " + Common.WORKFLOWDB + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
						smObjInfomsg.setDesc("删除成功");
						smObjInfomsg.setConfirm("OK");
					}
				}
			}
			commonDao.flush();
			YwLogUtil.addYwLog("删除收件资料", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		}
		return smObjInfomsg;

	}

	/**
	 * @author JHX
	 * @date:2016-07-25 清空文件夹
	 * */
	public SmObjInfo emptyfolder(HttpServletRequest request, String materilinst_ids, String delfile) {
		String currentPsnId = smStaff.getCurrentWorkStaffID();
		SmObjInfo smObjInfomsg = new SmObjInfo();
		if (!materilinst_ids.equals("")) {
			String[] ids = materilinst_ids.split(",");
			boolean isDelete;
			for (String id : ids) {
				isDelete = true;
				if (!id.equals("")) {
					String powerDel = ConfigHelper.getNameByValue("powerDel");
					if(!"1".equals(powerDel)){
						Wfi_ProMater promaster = commonDao.get(Wfi_ProMater.class, id);
						// 判断文件夹中是否有别人上传的问题件 ,有的话不能删除
						List<Wfi_MaterData> list = commonDao.getDataList(Wfi_MaterData.class, "select * from " + Common.WORKFLOWDB + "" + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
						for (int i = 0; i < list.size(); i++) {
							if (!currentPsnId.equals(list.get(i).getUpload_Id())) {
								isDelete = false;
								break;
							}
						}
						if(delfile!=null){
							isDelete=true;
						}
						if (isDelete) {
							commonDao.deleteQuery("delete from "
									+ "" + Common.WORKFLOWDB + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
							smObjInfomsg.setDesc("成功清空");
							smObjInfomsg.setConfirm("OK");
						} else {
							smObjInfomsg.setDesc("文件" + promaster.getMaterial_Name() + "中包含他人上传的文件，不能执行清空操作");
							smObjInfomsg.setConfirm("ERROR");
							return smObjInfomsg;
						}
					}else{
						commonDao.deleteQuery("delete from "
								+ "" + Common.WORKFLOWDB + "Wfi_MaterData where MATERILINST_ID='" + id + "'");
						smObjInfomsg.setDesc("成功清空");
						smObjInfomsg.setConfirm("OK");
					}
				}
			}
			commonDao.flush();
			YwLogUtil.addYwLog("清空收件资料", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		}
		return smObjInfomsg;

	}

	public void showImage(String materilinst_id, HttpServletRequest request, HttpServletResponse response) {
		String url = "";

		try {
			Wfi_ProMater promater = commonDao.get(Wfi_ProMater.class, materilinst_id);
			if (promater != null) {
				url = promater.getImg_Path();
			}
			if (url != null && url != "") {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("<img src='../../imagedownload?mid=" + materilinst_id + "&fileName=" + url + "'/>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void DeleteProMaterByProInst(String proinst_Id) {
		StringBuilder str = new StringBuilder();
		str.append(" Proinst_Id='");
		str.append(proinst_Id);
		str.append("'");
		List<Wfi_ProMater> ProMaterlist = commonDao.findList(Wfi_ProMater.class, str.toString());
		for (int i = 0; i < ProMaterlist.size(); i++) {
			StringBuilder str2 = new StringBuilder();
			str2.append(" Materilinst_Id='");
			str2.append(ProMaterlist.get(i).getMaterilinst_Id());
			str2.append("'");

			List<Wfi_MaterData> MaterDatalist = commonDao.findList(Wfi_MaterData.class, str2.toString());
			for (int j = 0; j < MaterDatalist.size(); j++) {
				commonDao.delete(MaterDatalist.get(j));
			}
			commonDao.delete(ProMaterlist.get(i));
		}
	}

	/**
	 * 获取活动已收件的树形数据
	 * */
	/*
	 * public List<Tree> GetHasAcceptMater(String file_Number){ List<Tree>
	 * treeList = new ArrayList<Tree>(); List<Wfi_ProMater> list =
	 * GetProMaterList(file_Number); if(list!=null&&list.size()>0){ for
	 * (Wfi_ProMater wfi_ProMater : list) { int
	 * status=wfi_ProMater.getMaterial_Status();
	 * if(status==WFConst.MateralStatus.AcceotMateral.value){ Tree tree = new
	 * Tree(); tree.setId(wfi_ProMater.getMaterilinst_Id());
	 * tree.setText(wfi_ProMater.getMaterial_Name()); tree.setType("folder");
	 * 
	 * StringBuilder str = new StringBuilder(); str.append("Materilinst_Id='");
	 * str.append(wfi_ProMater.getMaterilinst_Id()); str.append("'");
	 * 
	 * List<Tree> childList = new ArrayList<Tree>(); List<Wfi_MaterData>
	 * listChildDatas = commonDao.findList( Wfi_MaterData.class,
	 * str.toString()); for (int j = 0; j < listChildDatas.size(); j++) {
	 * Wfi_MaterData MaterData = listChildDatas.get(j); Tree newtree = new
	 * Tree(); newtree.setId(MaterData.getMaterialdata_Id());
	 * newtree.setText(MaterData.getFile_Name()); newtree.setType("file");
	 * childList.add(newtree); } tree.children = childList; treeList.add(tree);
	 * } } } return treeList; }
	 */
	public List<Wfi_MaterData> getNoGroupMetar(String filenumber) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select * from ");
		sBuilder.append(Common.WORKFLOWDB);
		sBuilder.append("Wfi_MaterData where FILE_NUMBER='");
		sBuilder.append(filenumber);
		sBuilder.append("' and upload_status=");
		sBuilder.append(WFConst.Upload_Status.NotGroup.value);
		return commonDao.getDataList(Wfi_MaterData.class, sBuilder.toString());
	}

	public String CreatThumb2(String basicpath, String filename, String id) throws Exception {

		makeSmallImage(basicpath, "/" + filename, "/thumb.jpg");

		File thumbfile = new File("/thumb.jpg");
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(thumbfile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		return "data:image/jpeg;base64," + new String(Base64.encodeBase64(buffer));

	}

	public Wfi_MaterData getMaterData(String id) {
		return commonDao.get(Wfi_MaterData.class, id);
	}

	// 创建单个项目文件
	public Wfi_ProMater CreatProMater(String name, String proinst_id) {
		Wfi_ProMater mater = new Wfi_ProMater();
		List<Map> Result = commonDao.getDataListByFullSql("select max(Material_Index) maxindex from " + Common.WORKFLOWDB + "Wfi_ProMater where proinst_id='" + proinst_id + "'");
		mater.setMaterilinst_Id(Common.CreatUUID());
		mater.setMaterial_Count(1);
		mater.setMaterial_Name(name);
		mater.setMaterial_Date(new Date());
		mater.setProinst_Id(proinst_id);
		mater.setMaterial_Type(1);
		mater.setMaterial_Pagecount(1);
		mater.setMaterial_Need(2);
		Integer maxvalue = 1;
		if (Result != null && Result.size() > 0) {
			Object max = Result.get(0).get("MAXINDEX");
			maxvalue = Integer.parseInt(max.toString());
			maxvalue++;
		}
		mater.setMaterial_Index(maxvalue);
		mater.setMaterial_Status(WFConst.MateralStatus.CeratMateral.value);
		return mater;
	}

	public List<Wfi_ProMater> CloneProMaterInst(String actinstID, String proInstID) {
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstID);
		List<Wfi_ProMater> promaters =GetProMaterInfo(proinst.getProinst_Id());
		for(Wfi_ProMater mater : promaters){
			Wfi_ProMater newMater = new Wfi_ProMater();
			newMater.setProinst_Id(proInstID);
			newMater.setMaterial_Count(mater.getMaterial_Count());
			newMater.setMaterial_Desc(mater.getMaterial_Desc());
			newMater.setMaterial_Index(mater.getMaterial_Index());
			newMater.setMaterial_Id(mater.getMaterial_Id());
			newMater.setMaterial_Name(mater.getMaterial_Name());
			newMater.setMaterial_Pagecount(mater.getMaterial_Pagecount());
			newMater.setMaterial_Need(mater.getMaterial_Need());
			newMater.setMaterial_Status(mater.getMaterial_Status());// 虚拟收件
			newMater.setMaterial_Type(mater.getMaterial_Type());
			newMater.setMaterialdef_Id(mater.getMaterialdef_Id());
			newMater.setDossier_Index(mater.getDossier_Index());
			newMater.setMaterial_Isdossier(mater.getMaterial_Isdossier());
			newMater.setMaterial_Bm(mater.getMaterial_Bm());
			newMater.setMaterialtype_Id(mater.getMaterialtype_Id());
			commonDao.save(newMater);
		}
		commonDao.flush();
		return promaters;
	}

}
