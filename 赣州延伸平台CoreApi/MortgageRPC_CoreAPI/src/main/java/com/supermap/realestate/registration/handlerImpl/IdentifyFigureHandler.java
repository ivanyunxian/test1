package com.supermap.realestate.registration.handlerImpl;

import java.util.ArrayList;
import java.util.List;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_IDENTIFYFIGURE;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 *指认图处理类
 */
/**
 * 
 * 指认图处理类
 * @ClassName: IdentifyFigureHandler
 * @author yuxuebin
 * @date 2017年06月01日 14:23:29
 */
public class IdentifyFigureHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public IdentifyFigureHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		if(StringHelper.isEmpty(bdcdyid)){
			super.setErrMessage("未查询到单元");
			return bsuccess;
		}
		RealUnit unit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		if(unit==null){
			super.setErrMessage("未查询到单元");
			return bsuccess;
		}
		
		BDCS_IDENTIFYFIGURE identifyFigure = new BDCS_IDENTIFYFIGURE();
		identifyFigure.setXMBH(this.getXMBH());
		identifyFigure.setBDCDYID(bdcdyid);
		identifyFigure.setBDCDYLX(this.getBdcdylx().Value);
		
		if(identifyFigure!=null){
			dao.save(identifyFigure);
			dao.flush();
			bsuccess=true;
		}
		return bsuccess;
	}
	
	
	

	
	
	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao baseCommonDao = this.getCommonDao();
		List<BDCS_IDENTIFYFIGURE> list=baseCommonDao.getDataList(BDCS_IDENTIFYFIGURE.class, "XMBH='"+super.getXMBH()+"' AND BDCDYID='"+bdcdyid+"'");
		if(list!=null&&list.size()>0){
			for(BDCS_IDENTIFYFIGURE identifyFigure:list){
				baseCommonDao.deleteEntity(identifyFigure);
			}
			baseCommonDao.flush();
			bsuccess = true;
		}else{
			
		}
		return bsuccess;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> listUnitTree=new ArrayList<UnitTree>();
		List<RealUnit> listUnit=UnitTools.loadUnits(super.getBdcdylx(), DJDYLY.XZ, "BDCDYID IN (SELECT BDCDYID FROM BDCS_IDENTIFYFIGURE WHERE XMBH='"+super.getXMBH()+"')");
		if(listUnit!=null&&listUnit.size()>0){
			for(RealUnit unit :listUnit){
				House h=(House)unit;
				UnitTree unitTree=new UnitTree();
				unitTree.setBdcdyid(h.getId());
				unitTree.setBdcdylx(h.getBDCDYLX().Value);
				unitTree.setCid(h.getCID());
				unitTree.setFh(h.getFH());
				unitTree.setFttdmj(h.getFTTDMJ());
				unitTree.setId(h.getId());
				unitTree.setLjzbdcdyid(h.getLJZID());
				unitTree.setLy(DJDYLY.XZ.Value);
				unitTree.setMj(h.getMJ());
				unitTree.setText(h.getZL());
				unitTree.setZdbdcdyid(h.getZDBDCDYID());
				unitTree.setZdly(DJDYLY.XZ.Value);
				unitTree.setZl(h.getZL());
				unitTree.setZrzbdcdyid(h.getZRZBDCDYID());
				unitTree.setZrzly(DJDYLY.XZ.Value);
				listUnitTree.add(unitTree);
			}
		}
		return listUnitTree;
	}
	
	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		return true;
	}

	/**
	 * 根据申请人ID添加权利人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
	}

	@Override
	public String getError() {
		return super.getErrMessage();
	}

	@Override
	public void SendMsg(String bljc) {
	}

}
