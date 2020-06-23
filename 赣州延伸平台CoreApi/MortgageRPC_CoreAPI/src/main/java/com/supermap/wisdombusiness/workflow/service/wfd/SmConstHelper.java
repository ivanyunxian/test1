package com.supermap.wisdombusiness.workflow.service.wfd;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Const;
import com.supermap.wisdombusiness.workflow.model.Wfd_Const_Value;

@Service("smConstHelper")
public class SmConstHelper {
	@Autowired
	CommonDao _CommonDao;
	
	public List<Wfd_Const_Value> GetConstValueList(String const_Code)
	{
		StringBuilder str=new StringBuilder();
		str.append(" Const_Code='");
		str.append(const_Code);
		str.append("'");
		List<Wfd_Const> constList=_CommonDao.findList(Wfd_Const.class, str.toString());
		if(constList.size()>0)
		{
			StringBuilder str2=new StringBuilder();
			str2.append("Const_Id='");
			str2.append(constList.get(0).getConst_Id());
			str2.append("' order by Const_Order");		
			return 	_CommonDao.findList(Wfd_Const_Value.class, str2.toString());		
		}
		else {
			Wfd_Const_Value _Const_Value=new Wfd_Const_Value();
			_Const_Value.setConst_Desc("---无---");
			_Const_Value.setConst_Value("");
			List<Wfd_Const_Value> valuesList=new ArrayList<Wfd_Const_Value>();
			valuesList.add(_Const_Value);
			
			return valuesList;
		}
	}
}
