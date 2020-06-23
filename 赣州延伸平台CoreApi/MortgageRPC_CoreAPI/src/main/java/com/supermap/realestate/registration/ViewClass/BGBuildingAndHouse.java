package com.supermap.realestate.registration.ViewClass;

import java.util.ArrayList;
import java.util.List;

import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;

/**
 * 存储了土地分割时，需要更新自然幢及房屋信息所需要的对象，主要是因为创建不动产单元号时，事务影响了创建，所以才用这种方法
 * @author shb
 *
 */
public class BGBuildingAndHouse {

	private Building building;
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public String getZdbdcdyid() {
		return zdbdcdyid;
	}

	public void setZdbdcdyid(String zdbdcdyid) {
		this.zdbdcdyid = zdbdcdyid;
	}

	public String getZddm() {
		return zddm;
	}

	public void setZddm(String zddm) {
		this.zddm = zddm;
	}

	public String getZrzbdcdyh() {
		return zrzbdcdyh;
	}

	public void setZrzbdcdyh(String zrzbdcdyh) {
		this.zrzbdcdyh = zrzbdcdyh;
	}

	public String getZrzbdcdyid() {
		return zrzbdcdyid;
	}

	public void setZrzbdcdyid(String zrzbdcdyid) {
		this.zrzbdcdyid = zrzbdcdyid;
	}
   
	private BDCDYLX bdcdylx;
	
	public BDCDYLX getBdcdylx() {
		return bdcdylx;
	}

	public void setBdcdylx(BDCDYLX bdcdylx) {
		this.bdcdylx = bdcdylx;
	}

	public List<BGHouse> getBghouses() {
		return bghouses;
	}

	public void setBghouses(List<BGHouse> bghouses) {
		this.bghouses = bghouses;
	}

	private String zdbdcdyid;
	private String zddm;
	private String zrzbdcdyh;
	private String zrzbdcdyid;
	private List<BGHouse> bghouses=new ArrayList<BGHouse>();
	
	public class BGHouse{
		private House house;
		public House getHouse() {
			return house;
		}
		public void setHouse(House house) {
			this.house = house;
		}
		public String getHbdcdyh() {
			return hbdcdyh;
		}
		public void setHbdcdyh(String hbdcdyh) {
			this.hbdcdyh = hbdcdyh;
		}
		private String hbdcdyh;
		
		private  BDCDYLX hbdcylx;
		public BDCDYLX getHbdcylx() {
			return hbdcylx;
		}
		public void setHbdcylx(BDCDYLX hbdcylx) {
			this.hbdcylx = hbdcylx;
		}
	}
}


