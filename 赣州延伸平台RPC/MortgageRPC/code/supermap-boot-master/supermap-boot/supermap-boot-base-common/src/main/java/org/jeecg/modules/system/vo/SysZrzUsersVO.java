package org.jeecg.modules.system.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SysZrzUsersVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**部门id*/
	private String zrzId;
	/**对应的用户id集合*/
	private List<String> userIdList;
	public SysZrzUsersVO(String zrzId, List<String> userIdList) {
		super();
		this.zrzId = zrzId;
		this.userIdList = userIdList;
	}
	
	public SysZrzUsersVO() {
		
	}
	
}