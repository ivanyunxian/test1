package com.supermap.wisdombusiness.workflow.service.wfm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.utility.Helper;
import com.supermap.wisdombusiness.workflow.util.Page;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.WFD_SPMB;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spdy;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import com.supermap.wisdombusiness.workflow.model.Wfi_Tr_ActDefToSpdy;
import com.supermap.wisdombusiness.workflow.service.common.Approval;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmApproval;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmRouteInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSpdy;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.util.Message;

@Service("smProInstService")
public class SmProSPService {

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmActDef smActDef;
	@Autowired
	private SmActInst smActInst;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmSpdy smSpdy;
	@Autowired
	private SmRouteInst smRouteInst;
	@Autowired
	private OperationService operationService;
	public List<Approval> GetSPYJ(String actinstid) {
		return GetSPYJ(actinstid, "1");
	}

	public List<Approval> GetSPYJ(String actinstid, String readonly) {	
		List<Approval> list = new ArrayList<Approval>();
		list = GetSPYJ(actinstid,readonly,null);
		return list;
	}
	/**
	 * 获取活动的审批信息
	 * */
	public List<Approval> GetSPYJ(String actinstid,String readonly,String bdcdyh){
		//需要判断是否保留历时意见。使用自定义参数设置
		List<Approval> list = new ArrayList<Approval>();
		Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
		Wfi_ActInst inst = smActInst.GetActInst(actinstid);
		if (actdef != null) {
			// 当前活动是否有意见，没有则按照定义添加一个
			List<Wfi_Tr_ActDefToSpdy> actdefspdys = GetSPdyByactdefid(actdef.getActdef_Id());
			if (actdefspdys != null && actdefspdys.size() > 0) {
				for (Wfi_Tr_ActDefToSpdy wfi_Tr_ActDefToSpdy : actdefspdys) {
					List<Wfi_Spyj> spyjs=null;
					Wfi_Spdy spdy = commonDao.get(Wfi_Spdy.class, wfi_Tr_ActDefToSpdy.getSpdy_Id());
					Approval approval = new Approval();
					approval.setSpdyid(spdy.getSpdy_Id());
					approval.setSpmc(spdy.getSpmc());
					approval.setSplx(spdy.getSplx());
					approval.setSigntypeString(spdy.getSigntype());
					approval.setReadonly(wfi_Tr_ActDefToSpdy.getReadonly());
					approval.setMryj(spdy.getMryj());
					approval.setBdcdyh(bdcdyh);
					Wfi_Spyj spyj = null;
					//海口版本：审批意见需要进行真实性的校验//查询行政区配置
					String area = operationService.getNativeAreaCodeConfig();
					if (approval.getReadonly() == 0 && !readonly.equals("1")) {
						boolean CanNewSpyj = true;
						if(area!=null&&area.equals("460100")){
							Wfi_ActInst actinst = commonDao.get(Wfi_ActInst.class,actinstid);
							String actinstname = actinst.getActinst_Name();
							String splx ="";
							if(actinstname.equals("初审")){
								splx = "CS";
							}else if(actinstname.equals("审核")){
								splx = "FS";
							}else {
								splx = "HD";
							}
							StringBuilder sql = new StringBuilder();
							sql.append("select spyj.spyj_id , spyj.actinst_id from "+Common.WORKFLOWDB+"Wfi_Spyj spyj left join ");
							sql.append(Common.WORKFLOWDB+"Wfi_Actinst actinst ");
							sql.append("on spyj.proinst_id=actinst.proinst_id ");
							sql.append("where actinst.actinst_id=");
							sql.append("'"+ actinstid + "' and spyj.splx=");
							sql.append("'"+splx+"'");
							List<Map> yj=commonDao.getDataListByFullSql(sql.toString());
							if(yj.size()>0){
								CanNewSpyj = false;
								if(!yj.get(0).get("ACTINST_ID").toString().equals(actinstid)){
									String spyjid =yj.get(0).get("SPYJ_ID").toString();
									Wfi_Spyj SpyjUpdate =commonDao.get(Wfi_Spyj.class, spyjid);
									SpyjUpdate.setActinst_Id(actinstid);
									commonDao.update(SpyjUpdate);
								}
							}
						}
						List<Wfi_Spyj> _spyj = new ArrayList<Wfi_Spyj>();
						if(!StringHelper.isEmpty(bdcdyh)){
							_spyj = commonDao.getDataList(Wfi_Spyj.class, "select * from " + Common.WORKFLOWDB + "Wfi_Spyj where actinst_id='" + actinstid + "' and bdcdyh='"+bdcdyh+"'");
						}else{
							_spyj = commonDao.getDataList(Wfi_Spyj.class, "select * from " + Common.WORKFLOWDB + "Wfi_Spyj where actinst_id='" + actinstid + "'");
						}
						
						if (_spyj == null || _spyj.size() == 0&&CanNewSpyj) {
							//Wfi_Spyj spyj = null;
							spyj = new Wfi_Spyj();
							spyj.setSpyj_Id(Common.CreatUUID());
							spyj.setSpdy_Id(approval.getSpdyid());
							spyj.setSplx(approval.getSplx());
							spyj.setActinst_Id(actinstid);
							spyj.setSpr_Id(smStaff.getCurrentWorkStaffID());
							spyj.setSpr_Name(smStaff.GetStaffName(spyj.getSpr_Id()));
							spyj.setProinst_Id(inst.getProinst_Id());
							spyj.setSpyj("");
							spyj.setSpsj(new Date());
							spyj.setBDCDYH(bdcdyh);
							commonDao.save(spyj);
							commonDao.flush();

						}
						
					}
					if(!StringHelper.isEmpty(bdcdyh)){
						spyjs=commonDao.getDataList(Wfi_Spyj.class, "select * from " + Common.WORKFLOWDB + "Wfi_Spyj where  splx='" + spdy.getSplx() + "' and SPDY_ID='" + spdy.getSpdy_Id() + "' and proinst_id='" + inst.getProinst_Id() + "' and bdcdyh='"+bdcdyh+"' order by spsj ");	
					}else{
						spyjs=commonDao.getDataList(Wfi_Spyj.class, "select * from " + Common.WORKFLOWDB + "Wfi_Spyj where  splx='" + spdy.getSplx() + "' and SPDY_ID='" + spdy.getSpdy_Id() + "' and proinst_id='" + inst.getProinst_Id() + "' order by spsj ");
					}
					
					List<Wfi_Spyj> spyjlists = new ArrayList<Wfi_Spyj>();
					if(area!=null&&area.equals("460100")){
						Wfi_Spyj itemspyj= null;
						if(spyjs.size()>0){
							for(int i=0;i<spyjs.size();i++){
								itemspyj = spyjs.get(i);
								if(itemspyj.getSIGNJG()!=null&&!itemspyj.getSIGNJG().equals("")){
									//如果不为空表明是意见修改这里需要调用签名检查
									String sprid = itemspyj.getSpr_Id();
									User user = commonDao.get(User.class, sprid);
									String psncode =user==null?smStaff.getCurrentWorkStaff().getLoginName()
											:user.getLoginName();
									String _spyj = itemspyj.getSpyj();
									Date _spdate = itemspyj.getSIGNDATE();
						            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						            if(_spdate!=null){
						            	    String _spdatestr = df.format(_spdate);
								            String content =psncode+_spyj+_spdatestr;
											String _spcontengMW = itemspyj.getSIGNFLAG();
											String info = SignCheck(content, _spcontengMW);
											if(info!=null&&!info.equals("")){
												com.alibaba.fastjson.JSONObject jsonobjet = new com.alibaba.fastjson.JSONObject();
												com.alibaba.fastjson.JSONObject jsonresult = (com.alibaba.fastjson.JSONObject)jsonobjet.parseObject(info);;
												String checkBase64 = jsonresult.get("印章图片").toString();
												itemspyj.setSIGNJG(checkBase64);
										    }
						            }
								}
								spyjlists.add(itemspyj);
							}
						}
						approval.setSpyjs(spyjlists);
					}else{
						approval.setSpyjs(spyjs);
					}
					list.add(approval);
				}
			}
		}
		// 刘树峰：把flush从for循环里边提出来
		return list;
	}

	public SmObjInfo SaveApproval(String spyjid, String spdyid, String splx, String actinstid, String yj,String signid,String signflag, String signjg ,String spid_batch,String bdcdyh) {
		return SaveApproval(spyjid, spdyid, splx, actinstid, yj, signid, signflag, signjg , spid_batch, bdcdyh, null, null);
	}
	/**
	 * 保存意见
	 * */
	public SmObjInfo SaveApproval(String spyjid, String spdyid, String splx, String actinstid, String yj,String signid,String signflag, String signjg ,String spid_batch,String bdcdyh, String sprid, String sprname) {
		Wfi_Spyj spyj = null;
		SmObjInfo smInfo = new SmObjInfo();
		if (spyjid == null || spyjid.equals("")) {
			spyj = new Wfi_Spyj();
			spyj.setSpyj_Id(Common.CreatUUID());
		} else {
			spyj = commonDao.get(Wfi_Spyj.class, spyjid);
			//首次加载审批不保存意见
			if(spyj==null){
				spyj = new Wfi_Spyj();
				spyj.setSpyj_Id(spyjid);
			}else{
				//海口版本：审批意见需要进行真实性的校验//查询行政区配置
				String area = operationService.getNativeAreaCodeConfig();
				if(area!=null&&area.equals("460100")){
						//如果不为空表明是意见修改这里需要调用签名检查
						String psncode = smStaff.getCurrentWorkStaff().getLoginName();
						String _spyj = spyj.getSpyj();
						Date _spdate = spyj.getSIGNDATE();
			            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			            if(_spdate==null){
			            	_spdate=new Date();
			            }
			            String _spdatestr = df.format(_spdate);
			            String content =psncode+yj+_spdatestr;
						String _spcontengMW = spyj.getSIGNFLAG();
						String info = SignCheck(content, _spcontengMW);
						if(info!=null&&!info.equals("")){
							com.alibaba.fastjson.JSONObject jsonobjet = new com.alibaba.fastjson.JSONObject();
							com.alibaba.fastjson.JSONObject jsonresult = (com.alibaba.fastjson.JSONObject)jsonobjet.parseObject(info);;
							String checkBase64 = jsonresult.get("印章图片").toString();
							spyj.setSIGNJG(checkBase64);
					}
						//驳回环节签名后不能跳跃转出
						Wfi_Route isPassBack=smRouteInst.PassOver(actinstid);
						if(null!=isPassBack){
							Wfi_ActInst inst=  smActInst.GetActInst(actinstid);
							inst.setIsSkipPassOver(1);
							commonDao.save(inst);
						}
				}
			}

		}
		Wfi_ActInst actInst = smActInst.GetActInst(actinstid);
		if (actInst != null) {
			spyj.setProinst_Id(actInst.getProinst_Id());
		}
		if (spdyid == null || spdyid.equals("")) {
			Wfi_Spdy spdy = smSpdy.getspdyBySPLX(splx);
			if (spdy != null) {
				spdyid = spdy.getSpdy_Id();
			}
		}
		spyj.setSpdy_Id(spdyid);
		spyj.setSplx(splx);
		spyj.setActinst_Id(actinstid);
		if (StringHelper.isEmpty(sprid)) {
			spyj.setSpr_Id(smStaff.getCurrentWorkStaffID());
		} else {
			spyj.setSpr_Id(sprid);
		}
		spyj.setSpyj(yj);
		spyj.setStatus(1);
		spyj.setSpsj(new Date());
		if (StringHelper.isEmpty(sprname)) {
			spyj.setSpr_Name(smStaff.GetStaffName(spyj.getSpr_Id()));
		} else {
			spyj.setSpr_Name(sprname);
		}
		spyj.setSpid_Bath(spid_batch);
		spyj.setBDCDYH(bdcdyh);
		if(signid!=null&&!signid.equals("")){
			spyj.setSIGNID(signid);
		}
		if(signflag!=null&&!signflag.equals("")){
			spyj.setSIGNFLAG(signflag);
		}
		if(signjg!=null&&!signjg.equals("")){
			spyj.setSIGNJG(signjg);
		}
		commonDao.saveOrUpdate(spyj);
		commonDao.flush();
		smInfo.setID(spyj.getSpyj_Id());
		smInfo.setDesc("保存成功");
		return smInfo;
	}
	 /**
     * @author JHX
     * @DATE:2016-08-05
     * 删除审批意见
     * */
	public SmObjInfo deleteApproval(String spyjid){
		SmObjInfo smInfo = new SmObjInfo();
		Wfi_Spyj spyj = commonDao.get(Wfi_Spyj.class, spyjid);
		if(spyj!=null){
			commonDao.delete(spyj);
			commonDao.flush();
			smInfo.setConfirm("OK");
			smInfo.setDesc("删除成功");
		}else{
			smInfo.setConfirm("NO");
			smInfo.setDesc("删除失败");
		}
		return smInfo;
	}
	public List<TreeInfo> GetSpdyTree() {
		StringBuilder str = new StringBuilder();
		str.append(" Status=1 ");
		List<Wfi_Spdy> list = commonDao.findList(Wfi_Spdy.class, str.toString());
		List<TreeInfo> treekist = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			Wfi_Spdy Spdy = list.get(i);
			TreeInfo tree = new TreeInfo();
			tree.setId(Spdy.getSpdy_Id());
			tree.setText(Spdy.getSpmc());
			tree.setType("data");
			tree.setDesc(Spdy.getSplx());
			//只是用来传递个数据过去用在申请审批表动态形成审批框处 WUZHU
			tree.setState(String.valueOf(Spdy.getSigntype()));
			treekist.add(tree);
		}
		return treekist;
	}

	public SmObjInfo CreateActToSpdy(String spdyid, String actdefid, int index) {
		SmObjInfo sminInfo = new SmObjInfo();
		Wfi_Tr_ActDefToSpdy Tr_ActDefToSpdy = new Wfi_Tr_ActDefToSpdy();
		Tr_ActDefToSpdy.setActdef_Id(actdefid);
		Tr_ActDefToSpdy.setSpdy_Id(spdyid);
		Tr_ActDefToSpdy.setActdefspdy_Id(Common.CreatUUID());
		Tr_ActDefToSpdy.setShow_Index(index);
		Tr_ActDefToSpdy.setStatus(1);
		Tr_ActDefToSpdy.setReadonly(0);
		commonDao.save(Tr_ActDefToSpdy);
		commonDao.flush();
		sminInfo.setID(Tr_ActDefToSpdy.getActdefspdy_Id());
		sminInfo.setDesc("保存成功");
		return sminInfo;
	}

	public Message GetActdefSpdy(String actdefid) {
		StringBuilder str = new StringBuilder();
		str.append("Actdef_Id='");
		str.append(actdefid);
		str.append("' order by Show_Index");
		List<Wfi_Tr_ActDefToSpdy> list = commonDao.findList(Wfi_Tr_ActDefToSpdy.class, str.toString());

		List<Wfi_Spdy> TreeList = new ArrayList<Wfi_Spdy>();
		for (int i = 0; i < list.size(); i++) {
			Wfi_Tr_ActDefToSpdy ActDefToSpdy = list.get(i);
			Wfi_Spdy _Spdy = commonDao.get(Wfi_Spdy.class, ActDefToSpdy.getSpdy_Id());
			TreeList.add(_Spdy);
		}
		Message msg = new Message();
		msg.setRows(TreeList);
		msg.setTotal(TreeList.size());
		return msg;
	}

	public void UpdateActToSpdy(JSONArray array) {
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String actspdyid = object.get("actdefspdyid").toString();
			Wfi_Tr_ActDefToSpdy actspdy = commonDao.get(Wfi_Tr_ActDefToSpdy.class, actspdyid);

			actspdy.setShow_Index(Integer.parseInt(object.get("index").toString()));
			commonDao.update(actspdy);

		}
		commonDao.flush();
	}

	/**
	 * 获取活动配置的审批定义
	 */
	public List<SmApproval> GetActSPDY(String actdef_id) {

		List<SmApproval> list = new ArrayList<SmApproval>();
		List<Wfi_Tr_ActDefToSpdy> spdys = GetSPdyByactdefid(actdef_id);
		if (spdys != null && spdys.size() > 0) {

			for (Wfi_Tr_ActDefToSpdy wfi_Tr_ActDefToSpdy : spdys) {
				SmApproval approval = new SmApproval();
				approval.setActdefid(wfi_Tr_ActDefToSpdy.getActdef_Id());
				approval.setActdefspdyid(wfi_Tr_ActDefToSpdy.getActdefspdy_Id());
				approval.setReadonly(wfi_Tr_ActDefToSpdy.getReadonly());
				approval.setIndex(wfi_Tr_ActDefToSpdy.getShow_Index());

				Wfi_Spdy spdy = commonDao.get(Wfi_Spdy.class, wfi_Tr_ActDefToSpdy.getSpdy_Id());
				if (spdy != null) {
					approval.setSplx(spdy.getSplx());
					approval.setSpmc(spdy.getSpmc());
					approval.setSpdyid(spdy.getSpdy_Id());
					approval.setMryj(spdy.getMryj());
				}
				list.add(approval);
			}
		}

		return list;
	}

	private List<Wfi_Tr_ActDefToSpdy> GetSPdyByactdefid(String actdef_id) {
		if (actdef_id != null && !actdef_id.equals("")) {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("select * from ");
			sBuilder.append(Common.WORKFLOWDB);
			sBuilder.append("Wfi_Tr_ActDefToSpdy where actdef_id='");
			sBuilder.append(actdef_id);
			sBuilder.append("' order by show_index asc");
			return commonDao.getDataList(Wfi_Tr_ActDefToSpdy.class, sBuilder.toString());
		} else {
			return null;
		}
	}

	// 删除审批活动关系
	public SmObjInfo DelActSPDY(String actspdyid) {
		SmObjInfo smObjInfo = new SmObjInfo();
		if (!actspdyid.equals("")) {
			commonDao.delete(Wfi_Tr_ActDefToSpdy.class, actspdyid);
			commonDao.flush();
			smObjInfo.setID(actspdyid);
			smObjInfo.setDesc("删除成功");
		}
		return smObjInfo;
	}

	// 更新只读状态
	public SmObjInfo updateReadonly(String actspdyid, int readonly) {
		SmObjInfo smObjInfo = new SmObjInfo();
		if (!actspdyid.equals("")) {
			Wfi_Tr_ActDefToSpdy actspdy = commonDao.get(Wfi_Tr_ActDefToSpdy.class, actspdyid);
			if (actspdy != null) {
				actspdy.setReadonly(readonly);
				commonDao.update(actspdy);
				commonDao.flush();
			}
			smObjInfo.setID(actspdyid);
			smObjInfo.setDesc("设置成功");
		}
		return smObjInfo;
	}

	// 增加审批定义
	public SmObjInfo SaveSpdy(Wfi_Spdy spdy) {
		SmObjInfo smObjInfo = new SmObjInfo();
		if (spdy != null) {
			String spdy_idString = spdy.getSpdy_Id();
			if ("".equals(spdy_idString)) {
				spdy.setSpdy_Id(Common.CreatUUID());
				commonDao.save(spdy);
				commonDao.flush();
			} else {
				commonDao.update(spdy);
				commonDao.flush();
			}
			smObjInfo.setID(spdy.getSpdy_Id());
			smObjInfo.setName(spdy.getSpmc());
			smObjInfo.setDesc("保存成功");
		}
		return smObjInfo;
	}

	public Wfi_Spdy GetSPDYBySpdyid(String spdyid) {
		if (!spdyid.equals("")) {
			return commonDao.get(Wfi_Spdy.class, spdyid);
		} else {
			return null;
		}

	}

	// 删除审批定义
	public SmObjInfo DelSpInfo(String spdy) {
		SmObjInfo smObjInfo = new SmObjInfo();
		commonDao.delete(Wfi_Spdy.class, spdy);
		commonDao.flush();
		smObjInfo.setID(spdy);
		smObjInfo.setDesc("删除成功");
		return smObjInfo;
	}

	public Page GetSPMBDefine(String staffid) {
		Page list = null;
		if (staffid != null && !"".equals(staffid)) {
			StringBuilder sbBuilder = new StringBuilder();
			sbBuilder.append("staff_id='");
			sbBuilder.append(staffid);
			sbBuilder.append("' and YXBZ=1 order by XH");
			list = commonDao.GetPagedData(WFD_SPMB.class, Common.WORKFLOWDB + "WFD_SPMB", 1, 30, sbBuilder.toString());
		}
		return list;
	}

	public SmObjInfo SaveTpl(String Content, String Staff_id, String id,String xh) {
		SmObjInfo info = new SmObjInfo();
		WFD_SPMB spmb = null;
		boolean update = false;
		if (id != null && !id.equals("")) {
			spmb = commonDao.get(WFD_SPMB.class, id);
			update = true;
		} else {
			spmb = new WFD_SPMB();
			spmb.setSpmb_Id(Common.CreatUUID());
		}
		spmb.setSpcontent(Content);

		spmb.setStaff_Id(Staff_id);
		spmb.setYxbz(1);
		spmb.setXh(xh);
		if (update) {
			commonDao.update(spmb);
		} else {
			commonDao.save(spmb);
		}
		commonDao.flush();
		info.setID(spmb.getSpmb_Id());
		return info;
	}

	public SmObjInfo DelSp(String spmbid) {
		SmObjInfo info = new SmObjInfo();
		if (spmbid != null) {
			commonDao.delete(WFD_SPMB.class, spmbid);
			commonDao.flush();
			info.setID(spmbid);
		}
		return info;
	}

	// 获取活动某种审批类型的数据
	public List<Wfi_Spyj> batchGetApproval(String splx, String actinsts) {
		if (actinsts != null && !actinsts.equals("")) {
			String[] _actinsts = actinsts.split(",");
			if (_actinsts != null && _actinsts.length > 0) {
				String str = "'0'";
				for (String actinst : _actinsts) {
					actinst = "'" + actinst + "'";
					str += "," + actinst;
				}
				List<Wfi_Spyj> spyjs = commonDao.getDataList(Wfi_Spyj.class, Common.WORKFLOWDB + "Wfi_Spyj", "splx='" + splx + "' and actinst_id in (" + str + ") order by spsj desc");
				return spyjs;
			}
		}
		return null;

	}

	   /**
     * 调用第三方的电子签章
     * @param SignName SignPassword SignContent
     * @author JHX
     * @DATE2016-08-05
     * */

	public String Sign(String password,String spyj,String yjid) {
		String info=null;
		User user = smStaff.getCurrentWorkStaff();
		String sso = GetProperties.getConstValueByKey("SIGN");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		Wfi_Spyj wfi_spyj = commonDao.get(Wfi_Spyj.class, yjid);
		wfi_spyj.setSIGNDATE(date);
		wfi_spyj.setSpyj(spyj);
		String Content = user.getLoginName()+spyj+df.format(date);
		if (user != null) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("SignName", user.getLoginName());

			m.put("SignPassword", password);
			m.put("SignContent", Content);
			info = (String)Helper.WebService(m, sso, "RunSignature",true);
			if(info!=null&&!info.equals("")){
				com.alibaba.fastjson.JSONObject jsonobjet = new com.alibaba.fastjson.JSONObject();
				com.alibaba.fastjson.JSONObject jsonresult = (com.alibaba.fastjson.JSONObject)jsonobjet.parseObject(info);;
				wfi_spyj.setSIGNFLAG(jsonresult.get("签章数据").toString());
				wfi_spyj.setSIGNID(jsonresult.get("签章ID").toString());
				wfi_spyj.setSIGNJG(jsonresult.get("印章图片").toString());
		    }
		} 
		//wfi_spyj.setSIGNFLAG(sIGNFLAG);
		commonDao.update(wfi_spyj);
		commonDao.flush();
		return info;
	}
	/**
	 * 意见检查
	 * @param 之前前面的content（code+ yj + datestr） 和 密文
	 * */
	public String SignCheck(String content, String _spcontengMW) {
			Map<String, String> m = new HashMap<String, String>();
			String sso = GetProperties.getConstValueByKey("SIGN");
			m.put("SignContent", content);
			m.put("SignatureValue", _spcontengMW);
			String info = (String)Helper.WebService(m, sso, "CheckSignature",true);
			return info;
	}
	
	/**
	 * 根据审批ID获取审批意见
	 * */
	public Wfi_Spyj GetSPYJById(String id){
		Wfi_Spyj spyj =  commonDao.get(Wfi_Spyj.class, id);
		return spyj;
	}
	/**
	 * @author JHX
	 * @DATE:2016-08-08
	 * 编辑意见的验证密码时候是够正确
	 * */
	public boolean SignCheckPassword(String passwor) {
		Map<String, String> m = new HashMap<String, String>();
		String sso = GetProperties.getConstValueByKey("SIGN");
		User user = smStaff.getCurrentWorkStaff();
		m.put("StaffCode", user.getLoginName());
		m.put("SignPassword", passwor);
		String info = (String)Helper.WebService(m, sso, "checkQZPassword",true);
		boolean flag = Boolean.parseBoolean(info);
		return flag;
    }
	/**
	 * @author JHX
	 * @DATE:2016-08-08
	 * 更改审批意见
	 * */
	public boolean updateSpyjById(String id,String content){
		Wfi_Spyj spyj =  commonDao.get(Wfi_Spyj.class, id);
		if(spyj!=null){
			spyj.setSpyj(content);
			spyj.setSpsj(new Date());
			commonDao.update(spyj);
			commonDao.flush();
			return true;
		}
		return false;
		

	}
	
	/**
	 * 海口批量审核之前的签章
	 */
	public Map<String,String> beforeApproval(String spyjid , String actinstid , String spyj , String splx ,String password){
		Map<String,String> result = new HashMap<String, String>();
		Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
		StringBuilder sb = new StringBuilder();
		sb.append(" proinst_id='");
		sb.append(actinst.getProinst_Id());
		sb.append("' and splx = '");
		sb.append(splx+"'");
		List<Wfi_Spyj> spyjlist = commonDao.findList(Wfi_Spyj.class, sb.toString());
		if(null!=spyjlist&&spyjlist.size()>0){
			Wfi_Spyj sp = spyjlist.get(0);
			result.put("spyjid", sp.getSpyj_Id());
			result.put("actinstid", sp.getActinst_Id());
			Sign(password , spyj , sp.getSpyj_Id());
		}else{
			Wfi_Spyj sp = new Wfi_Spyj();
			sp.setActinst_Id(actinstid);
			sp.setProinst_Id(actinst.getProinst_Id());
			sp.setSplx(splx);
			sp.setSpyj(spyj);
			commonDao.save(sp);
			commonDao.flush();
			spyjid = sp.getSpyj_Id();
			Sign(password , spyj , spyjid);
			result.put("spyjid", spyjid);
			result.put("actinstid", actinstid);
		}
		return result;
	}
	
}
