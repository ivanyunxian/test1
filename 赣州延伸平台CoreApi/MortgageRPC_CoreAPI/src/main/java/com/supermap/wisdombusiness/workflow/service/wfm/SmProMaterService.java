package com.supermap.wisdombusiness.workflow.service.wfm;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Bank_Trustbook;
import com.supermap.wisdombusiness.workflow.model.Bank_TrustbookPage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Mater;
import com.supermap.wisdombusiness.workflow.model.Wfd_MaterClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProMater;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterClass;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.Http;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import com.supermap.wisdombusiness.workflow.util.Message;

@Service("smProMaterService")
public class SmProMaterService {
	@Autowired
	CommonDao _CommonDao;
	@Autowired
	private SmProMater smProMater;
	
	@Autowired
	private SmStaff mStaff;

	//分页
	public Message FindAllWfd(String id, Integer page, Integer rows) {
		
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("Prodef_Id='");
		str.append(id);
		str.append("'");
		str.append(" order by Material_Index");
		
		Long  materscount = _CommonDao.getCountByFullSql(" from BDC_WORKFLOW.WFD_PROMATER where "+str.toString());
		List<Wfd_ProMater>  maters = _CommonDao.GetPagedListData(Wfd_ProMater.class,"BDC_WORKFLOW.WFD_PROMATER", page, rows,str.toString());
		msg.setTotal(materscount);
		msg.setRows(maters);
		return msg;
		//return _CommonDao.GetPagedData(Wfd_ProMater.class, page, rows,str.toString());
	}

	public void DeleteWfd_ProMaterById(String id) {
		_CommonDao.delete(Wfd_ProMater.class, id);
		_CommonDao.flush();
	}

	public SmObjInfo DeleteProMaterByIDs(JSONArray array) {
		SmObjInfo info = new SmObjInfo();

		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			Wfd_ProMater ProMater = _CommonDao.get(Wfd_ProMater.class, object
					.get("id").toString());
			if (ProMater != null) {
				_CommonDao.delete(ProMater);
			}
		}
		_CommonDao.flush();
		info.setDesc("删除成功！");
		info.setID("0");
		return info;
	}

	public void DeleteWfd_MaterClassById(String id) {
		// 同时删除子目录数据
		StringBuilder str = new StringBuilder();
		StringBuilder str2 = new StringBuilder();
		str.append("Materialtype_Id='");
		str.append(id);
		str.append("'");
		str2.append("Materialtype_Pid='");
		str2.append(id);
		str2.append("'");
		List<Wfd_Mater> list = _CommonDao.findList(Wfd_Mater.class,
				str.toString());

		for (int i = 0; i < list.size(); i++) {
			Wfd_Mater Mater = list.get(i);
			_CommonDao.delete(Mater);
		}

		List<Wfd_MaterClass> list2 = _CommonDao.findList(Wfd_MaterClass.class,
				str2.toString());
		for (int i = 0; i < list2.size(); i++) {
			Wfd_MaterClass MaterClass = list2.get(i);
			DeleteWfd_MaterClassById(MaterClass.getMaterialtype_Id());
			_CommonDao.delete(MaterClass);
		}

		_CommonDao.delete(Wfd_MaterClass.class, id);
		_CommonDao.flush();
	}

	public void DeleteWfd_MaterById(String id) {
		_CommonDao.delete(Wfd_Mater.class, id);
		_CommonDao.flush();
	}

	public List<Wfd_Mater> FindAllWfd_Mater() {
		StringBuilder str = new StringBuilder();
		str.append(" 1=1 ");
		str.append(" order by Material_Index");
		return _CommonDao.findList(Wfd_Mater.class, str.toString());
	}

	public List<TreeInfo> WfdMaterTree() {
		return WfdMaterChildTree(null);
	}

	public List<TreeInfo> WfdMaterChildTree(String Pid) {
		StringBuilder str = new StringBuilder();

		if (Pid == null) {
			str.append(" Materialtype_Pid is null or Materialtype_Pid='0' ");
		} else {
			str.append(" Materialtype_Pid ='");
			str.append(Pid);
			str.append("'");
		}

		str.append(" order by Materialtype_Index");

		List<Wfd_MaterClass> list = _CommonDao.findList(Wfd_MaterClass.class,
				str.toString());

		List<TreeInfo> _treelist = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			TreeInfo tree = new TreeInfo();
			tree.setId(list.get(i).getMaterialtype_Id());
			tree.setText(list.get(i).getMaterialtype_Name());
			tree.setType("catalog");
			tree.setState("closed");
			tree.children = WfdMaterChildTree(list.get(i).getMaterialtype_Id());
			_treelist.add(tree);

		}

		if (Pid != null) {
			StringBuilder str2 = new StringBuilder();
			str2.append(" Materialtype_Id ='");
			str2.append(Pid);
			str2.append("' order by Material_Index");

			List<Wfd_Mater> list2 = _CommonDao.findList(Wfd_Mater.class,
					str2.toString());
			for (int i = 0; i < list2.size(); i++) {
				TreeInfo tree = new TreeInfo();
				tree = new TreeInfo();
				tree.setId(list2.get(i).getMaterialdef_Id());
				tree.setText(list2.get(i).getMaterial_Name());
				tree.setType("data");
				_treelist.add(tree);
			}
		}

		return _treelist;
	}

	public Wfd_Mater GetWfd_MaterById(String id) {
		return _CommonDao.get(Wfd_Mater.class, id);
	}

	public Wfd_ProMater GetWfd_ProMaterById(String id) {
		return _CommonDao.get(Wfd_ProMater.class, id);
	}

	public Wfd_MaterClass GetWfd_MaterClassById(String id) {
		return _CommonDao.get(Wfd_MaterClass.class, id);
	}

	public void SaveOrUpdate(Wfd_ProMater ProMater) {
		_CommonDao.saveOrUpdate(ProMater);
		_CommonDao.flush();
	}

	public SmObjInfo SaveOrUpdate(Wfd_Mater Mater) {
		SmObjInfo info = new SmObjInfo();
		if (Mater.getMaterialdef_Id().equals("")) {
			Mater.setMaterialdef_Id(Common.CreatUUID());
		}
		_CommonDao.saveOrUpdate(Mater);
		_CommonDao.flush();
		info.setID(Mater.getMaterialdef_Id());
		info.setName(Mater.getMaterial_Name());
		info.setDesc("保存成功");
		return info;
	}

	public void SaveOrUpdate(Wfd_MaterClass MaterClass) {
		_CommonDao.saveOrUpdate(MaterClass);
		_CommonDao.flush();
	}

	public String CreateMaterByName(String name, String pid, int index) {
		Wfd_Mater Mater = new Wfd_Mater();
		Mater.setMaterial_Name(name);
		Mater.setMaterialtype_Id(pid);
		Mater.setMaterial_Index(index);
		_CommonDao.saveOrUpdate(Mater);
		_CommonDao.flush();
		return Mater.getMaterialdef_Id();
	}

	public String CreateMaterClassByName(String name, String pid, int index) {
		Wfd_MaterClass MaterClass = new Wfd_MaterClass();
		MaterClass.setMaterialtype_Name(name);
		MaterClass.setMaterialtype_Pid(pid);
		MaterClass.setMaterialtype_Index(index);
		_CommonDao.saveOrUpdate(MaterClass);
		_CommonDao.flush();
		return MaterClass.getMaterialtype_Id();
	}

	public void MaterToProMater(JSONArray array, String ProdefId) {
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			Wfd_Mater mater = _CommonDao.get(Wfd_Mater.class, object.get("id")
					.toString());
			if (mater != null) {
				Wfd_ProMater proMater = new Wfd_ProMater();
				MaterToProMater(mater, proMater);
				proMater.setProdef_Id(ProdefId);
				_CommonDao.save(proMater);
			}
		}
		_CommonDao.flush();
	}

	public void MaterToProMater(Wfd_Mater mater, Wfd_ProMater proMater) {
		if (mater.getMaterial_Count() != null)
			proMater.setMaterial_Count(mater.getMaterial_Count());
		if (mater.getMaterial_Index() != null)
			proMater.setMaterial_Index(mater.getMaterial_Index());
		if (mater.getMaterial_Type() != null
				&& !mater.getMaterial_Type().equals(""))
			proMater.setMaterial_Type(Integer.parseInt(mater.getMaterial_Type()));
		proMater.setMaterial_Desc(mater.getMaterial_Desc());
		proMater.setMaterial_Name(mater.getMaterial_Name());
		proMater.setMaterial_Bm(mater.getMaterial_Bm());
		proMater.setMaterial_Need(1);
		if (mater.getMaterial_Pagecount() != null)
			proMater.setMaterial_Pagecount(mater.getMaterial_Pagecount());
		// proMater.setMaterial_Type(mater.getMaterial_Type());
	}

	// 获取实体数据
	public Wfi_MaterData getMaterData(String materid) {
		Wfi_MaterData materData = null;
		materData = _CommonDao.get(Wfi_MaterData.class, materid);
		return materData;
	}
	public Bank_TrustbookPage getBank_TrustbookPage(String banktrustbookpage_id) {
		Bank_TrustbookPage materData = null;
		materData = _CommonDao.get(Bank_TrustbookPage.class, banktrustbookpage_id);
		return materData;
	}

	public SmObjInfo RenameWfdMaterClass(String id, String name) {
		SmObjInfo smInfo = new SmObjInfo();
		if (id != null && !id.equals("")) {
			Wfd_MaterClass MaterClass = _CommonDao
					.get(Wfd_MaterClass.class, id);
			MaterClass.setMaterialtype_Name(name);
			;
			_CommonDao.update(MaterClass);
			_CommonDao.flush();
			smInfo.setID(MaterClass.getMaterialtype_Id());
			smInfo.setDesc("更新成功");
		}
		return smInfo;
	}

	/**
	 * 修改流程实例资料的收件状态
	 * */
	public SmObjInfo ModifyMaterialInfo(String proinstid, String materalid,
			String Count, boolean checked) {
		SmObjInfo smInfo = new SmObjInfo();
		if (materalid != null && !"".equals(materalid)) {

			Wfi_ProMater proMater = _CommonDao.get(Wfi_ProMater.class,
					materalid);
			if (checked) {
				proMater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
				proMater.setMaterial_Date(new Date());
				proMater.setMaterial_Count(Integer.parseInt(Count));
				smInfo.setID(materalid);
				smInfo.setDesc("收件成功");
			} else {
				proMater.setMaterial_Status(WFConst.MateralStatus.CeratMateral.value);
				proMater.setMaterial_Date(null);
				smInfo.setID(materalid);
			}
			_CommonDao.update(proMater);
			_CommonDao.flush();
		}
		return smInfo;
	}

	/** 上传文件，修改文件标示 */
	public SmObjInfo ModifyMaterialPath(String materalid, String fileid) {
		SmObjInfo smInfo = new SmObjInfo();
		Wfi_ProMater proMater = _CommonDao.get(Wfi_ProMater.class, materalid);
		proMater.setImg_Path(fileid);
		proMater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
		if(proMater.getMaterial_Count()==null){
			proMater.setMaterial_Count(1);
		}
		_CommonDao.update(proMater);
		_CommonDao.flush();
		return smInfo;
	}
	
	/** 上传银行委托书，修改文件路径*/
	public SmObjInfo ModifyBankTrustbookPath(String trustbookid, String Img_Path) {
		SmObjInfo smInfo = new SmObjInfo();
		Bank_Trustbook trustbook = _CommonDao.get(Bank_Trustbook.class, trustbookid);
		trustbook.setImg_Path(Img_Path);
		_CommonDao.update(trustbook);
		_CommonDao.flush();
		return smInfo;
	}
    
	/*获取一个目录下文件数量*/
	public long GetFileCount(String folderid){
		return _CommonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_materdata where MATERILINST_ID='"+folderid+"'");
		
	}
	
	@SuppressWarnings("rawtypes")
	public Wfi_ProMater AddProMaterFile(String filename, String filecount,
			String filetype, String fileindex, String materials, String proinstid) {
		//
		int index = 100;
		List<Map> list = _CommonDao
				.getDataListByFullSql("select max(MATERIAL_INDEX) as MAXINDEX from "
						+ Common.WORKFLOWDB
						+ "Wfi_ProMater where proinst_id='"
						+ proinstid + "'"); 
		if (list != null && list.size() > 0) {
			Map m = list.get(0);
			if (m != null) {
				Object object = m.get("MAXINDEX");
				if (object != null) {
					index = Integer.parseInt(object.toString());
					if(null!=fileindex &&!"".equals(fileindex)){
						index = Integer.parseInt(fileindex);
					}else{
						index++;
					}
				}
			}
		}
		if(null!=materials&&!"".equals(materials)){
			String sql = "select * from "+ Common.WORKFLOWDB + "Wfi_ProMater ";
			sql+="where MATERILINST_ID in("+materials+")";
			List<Wfi_ProMater> materList = _CommonDao.getDataList(Wfi_ProMater.class,sql );
			for(Wfi_ProMater mater :  materList ){
				mater.setMaterial_Index(mater.getMaterial_Index()+1);
				_CommonDao.update(mater);
			}
		}
		List<Wfi_MaterClass> classList = _CommonDao.findList(Wfi_MaterClass.class,
				"proinst_id='"+proinstid+"'");
		if(null!=classList&&classList.size()>0){
			for(Wfi_MaterClass materclass : classList){
				Wfi_ProMater proMater = new Wfi_ProMater();
				proMater.setMaterilinst_Id(Common.CreatUUID());
				proMater.setMaterial_Name(filename);
				proMater.setMaterial_Count(Integer.parseInt(filecount));
				proMater.setMaterial_Type(Integer.parseInt(filetype));
				proMater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
				proMater.setMaterial_Index(index);
				proMater.setProinst_Id(proinstid);
				proMater.setMaterial_Need(1);
				proMater.setMaterial_Desc("用户自定义添加");
				proMater.setMaterialtype_Id(materclass.getMaterialType_Id());
				_CommonDao.save(proMater);
			}
		}else{			
			Wfi_ProMater proMater = new Wfi_ProMater();
			proMater.setMaterilinst_Id(Common.CreatUUID());
			proMater.setMaterial_Name(filename);
			proMater.setMaterial_Count(Integer.parseInt(filecount));
			proMater.setMaterial_Type(Integer.parseInt(filetype));
			proMater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
			proMater.setMaterial_Index(index);
			proMater.setProinst_Id(proinstid);
			proMater.setMaterial_Need(1);
			proMater.setMaterial_Desc("用户自定义添加");
			_CommonDao.save(proMater);
		}
		_CommonDao.flush();
		return new Wfi_ProMater();
	}

	public boolean DelProMateril(String materilid) {
		_CommonDao.delete(Wfi_MaterData.class, materilid);
		_CommonDao.flush();
		return true;
	}
	//兼容前期版本
	public SmObjInfo DelProMateril2(String materilid,String delfile,String file_number) {
		//非半人创建不能删除。
		SmObjInfo obj = new SmObjInfo();
		String psnid = mStaff.getCurrentWorkStaffID();
		Wfi_MaterData materdata = _CommonDao.get(Wfi_MaterData.class, materilid);
		List<Wfi_ProInst> proinsts = _CommonDao.findList(Wfi_ProInst.class, "file_number = '" + file_number + "'");
		Wfd_Prodef prodef = _CommonDao.get(Wfd_Prodef.class, proinsts.get(0).getProdef_Id());
		String powerDel = ConfigHelper.getNameByValue("powerDel");
		//如果是智能审批或者是最多跑一次项目，都可以删除图片
		if("1".equals(prodef.getHouse_Status()) || !"".equals(StringHelper.FormatByDatatype(proinsts.get(0).getWLSH()))) {
			_CommonDao.delete(Wfi_MaterData.class, materilid);
			_CommonDao.flush();
			obj.setConfirm("OK");
			obj.setDesc("删除成功！");
		}else {
			if(!"1".equals(powerDel)){
				if(delfile==null||delfile.isEmpty()){
					if(materdata.getUpload_Id()!=null&&!materdata.getUpload_Id().equals(psnid)){
						obj.setDesc("非本人创建,不能删除！");
						obj.setConfirm("Error");
						return obj;
					}
				}
			}
			_CommonDao.delete(Wfi_MaterData.class, materilid);
			_CommonDao.flush();
			obj.setConfirm("OK");
			obj.setDesc("删除成功！");

		}
		return obj;
	}
	public boolean DelAllProMateril(String materilid) {
		Wfi_MaterData data=_CommonDao.get(Wfi_MaterData.class, materilid);
		_CommonDao.deleteQuery("delete "+Common.WORKFLOWDB+"Wfi_MaterData where materilinst_id='"+data.getMaterilinst_Id()+"'");
		_CommonDao.flush();
		return true;
	}
	

	// 回去单个条目下的所有文件
	public List<Wfi_MaterData> getmaterData(String materlinstid) {

		return _CommonDao.getDataList(Wfi_MaterData.class, "select * from "
				+ Common.WORKFLOWDB + "Wfi_MaterData where materilinst_id='"
				+ materlinstid + "' order by file_index ");

	}

	/**
	 * 更新文件的数量
	 * */
	public boolean UpdatePromaterCount(String id, String count) {
		Wfi_ProMater mater = _CommonDao.get(Wfi_ProMater.class, id);
		if (mater != null && count != null && !"".equals(count)) {
			mater.setMaterial_Count(Integer.parseInt(count));
			_CommonDao.update(mater);
			_CommonDao.flush();
		}
		return true;
	}
	/**
	 * 更新文件的名称
	 * */
	public void UpdatePromaterName(String id, String name) {
		Wfi_ProMater mater = _CommonDao.get(Wfi_ProMater.class, id);
		if (mater != null && name != null && !"".equals(name)) {
			mater.setMaterial_Name(name);
			_CommonDao.update(mater);
			_CommonDao.flush();
		}
	}

	public boolean UpdatePromaterType(String id, String type) {
		Wfi_ProMater mater = _CommonDao.get(Wfi_ProMater.class, id);
		if (mater != null && type != null && !"".equals(type)) {
			mater.setMaterial_Type(Integer.parseInt(type));
			_CommonDao.update(mater);
			_CommonDao.flush();
		}
		return true;
	}

	/**
	 * 收件树的数据获取
	 * */

	public List<Tree> GetMaterDataTree(String File_Number,String clear) {
		return smProMater.GetMaterDataTree(File_Number,true,clear);
	}
	/**
	 * 收件树的数据获取
	 * */

	public List<Tree> GetAllMaterFolderTree(String File_Number) {
		return smProMater.getFolder(File_Number,true);
	}
	public List<Tree> GetAllMaterDataTree(String id) {
		return smProMater.getfilesByfolderID(id);
	}
	public List<Tree> GetBanktrustbookAllMaterDataTree(String id) {
		return smProMater.getbanktrustbookfilesByfolderID(id);
	}
    /**
     * 获取未分组的项目附件
     * */
	public List<Wfi_MaterData> getNoGroupMetar(String file_number) {
		return smProMater.getNoGroupMetar(file_number);
	}
	/**
	 * 保存文件到项目指定的文件夹
	 * @param file
	 * @param material_name
	 * @param Proinst_id
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public boolean SaveFileToProject(File file,String material_name,String Proinst_id,String filetype) throws ParseException, IOException{
		boolean Result=false;
		boolean updateimgpath=false;
		boolean add=false;
		if(Proinst_id!=null&&!Proinst_id.equals("")){
			Wfi_ProInst proinst=_CommonDao.get(Wfi_ProInst.class, Proinst_id);
			if(proinst!=null){
				if(material_name!=null&&!material_name.equals("")){
					String sql="material_name='"+material_name+"' and proinst_id='"+proinst.getProinst_Id()+"'";
					List<Wfi_ProMater> promaters =_CommonDao.getDataList(Wfi_ProMater.class, Common.WORKFLOWDB
							+"Wfi_ProMater", sql);
					Wfi_ProMater materialinst=null;
					if(promaters!=null&&promaters.size()>0){
						materialinst=promaters.get(0); 
					}else{
						materialinst=smProMater.CreatProMater(material_name,Proinst_id);
						add=true;
					
					}
					String Path=FileUpload.GetNewPath(proinst,materialinst.getMaterilinst_Id());
					List<Map> list=Http.postFile(file, file.getName(), Path);
					if(list!=null&&list.size()>0){
						
						for (int i = 0; i < list.size(); i++) {
							Map map = list.get(i);
							Wfi_MaterData materData=new Wfi_MaterData();
							SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
							String fileName = df.format(new Date())
									+ "_"
									+ java.net.URLEncoder.encode(
											material_name, "UTF-8");
							materData.setMaterialdata_Id(Common.CreatUUID());
							 String imgpath=materialinst.getImg_Path();
							if((imgpath==null||imgpath.equals(""))&&!updateimgpath){
						    	   materialinst.setImg_Path(materData.getMaterialdata_Id()); 
						       }
							materData.setMaterilinst_Id(materialinst.getMaterilinst_Id());
							materData.setFile_Name(fileName+filetype);
							materData.setUpload_Name("服务上传");
							materData.setFile_Path(map.get("filename").toString());
							materData
									.setUpload_Status(WFConst.Upload_Status.NotGroup.value);
							materData.setFile_Index(i);
							
							materData.setPath(map.get("filepath").toString());
							_CommonDao.save(materData);
							
						}
						if(add){
							_CommonDao.save(materialinst);
						}
						else{
							 _CommonDao.update(materialinst);
						}
						_CommonDao.flush();
					}
				}
				
			}
		}
		return Result;
		
	}
	
	
	/**
	 * 保存文件到项目指定的文件夹
	 * @param file
	 * @param material_name
	 * @param Proinst_id
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public boolean SaveFileToProject(File file,String material_name,String Proinst_id) throws ParseException, IOException{
		return SaveFileToProject(file,material_name,Proinst_id,".jpg");
	}
	
}
