package com.supermap.realestate.registration.maintain;

import java.util.ArrayList;
import java.util.List;


/**
 * 宗地信息的完整对象，包括权利，权利人。
 * @ClassName: Land
 * @author liushufeng
 * @date 2016年7月13日 下午3:08:27
 */
public class Units {

	public Units(){
		
	}
	
	public List<Unit> units=new ArrayList<Unit>();

	/** 
	 * @return units 
	 */
	public List<Unit> getUnits() {
		return units;
	}

	/** 
	 * @param units 要设置的 units 
	 */
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
}
