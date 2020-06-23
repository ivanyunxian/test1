/**   
 * 系统检查项配置文件
 * @Title: CheckConfig.java 
 * @Package com.supermap.realestate.registration.mapping 
 * @author liushufeng 
 * @date 2015年8月5日 下午1:29:50 
 * @version V1.0   
 */

package com.supermap.realestate.registration.mapping;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperXStream;
import com.thoughtworks.xstream.XStream;

/**
 * 系统级检查项配置文件类
 * @ClassName: CheckConfig
 * @author liushufeng
 * @date 2015年8月5日 下午1:29:50
 */
public class CheckConfig implements Serializable {

	private static final long serialVersionUID = -8417416623706184171L;

	private List<CheckItem> checkitems = new ArrayList<CheckConfig.CheckItem>();

	private List<CheckGroup> checkgroups = new ArrayList<CheckConfig.CheckGroup>();

	public CheckConfig() {

	}

	public List<CheckItem> getCheckitems() {
		return checkitems;
	}

	public List<CheckGroup> getCheckgroups() {
		return checkgroups;
	}

	public void setCheckgroups(List<CheckGroup> checkgroups) {
		this.checkgroups = checkgroups;
	}

	public void setCheckitems(List<CheckItem> checkitems) {
		this.checkitems = checkitems;
	}

	public ICheckItem getCheckItem(String id) {
		ICheckItem item = null;
		if (checkitems != null && checkitems.size() > 0) {
			for (int i = 0; i < checkitems.size(); i++) {
				if (checkitems.get(i).id.equals(id)) {
					item = checkitems.get(i);
					break;
				}
			}
		}
		return item;
	}

	public CheckGroup getCheckGroupByID(String groupid) {
		CheckGroup group = null;
		for (int i = 0; i < this.checkgroups.size(); i++) {
			if (this.checkgroups.get(i).getGroupid().equals(groupid)) {
				group = this.checkgroups.get(i);
				break;
			}
		}
		return group;
	}

	public List<CheckGroup> getCheckGroupByCode(String workflowcode) {
		List<CheckGroup> list=new ArrayList<CheckConfig.CheckGroup>();
		for (CheckGroup group : checkgroups) {
			if (group.workflowcodes.contains(workflowcode)) {
				list.add(group);
			}
		}
		return list;
	}

	public void saveConfig() {
		XStream xStream = new SuperXStream();
		xStream.alias("CheckConfig", CheckConfig.class);
		xStream.alias("CheckItem", CheckItem.class);
		xStream.alias("checkgroup", CheckGroup.class);
		xStream.alias("string", String.class);

		xStream.useAttributeFor(CheckItem.class, "itemname");
		xStream.useAttributeFor(CheckItem.class, "id");
		try {
			FileOutputStream stream = new FileOutputStream(CheckConfig.class.getResource("/CheckConfig.xml").getFile());
			xStream.toXML(this, stream);
			stream.flush();
			stream.close();
			System.out.println("重新读取XML成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("保存CheckConfig的时候出错！");
		}
	}

	public static class CheckItem implements Serializable, ICheckItem {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9142905944390553961L;

		/**
		 * 检查项ID
		 */
		private String id;

		/**
		 * 检查项名称
		 */
		private String itemname;

		/**
		 * 检查语句
		 */
		private String sqlexpr;

		/**
		 * 结果表达式
		 */
		private String resultexpr;

		/**
		 * 错误级别（提示、强制）
		 */
		private String checklevel;

		/**
		 * 错误信息
		 */
		private String errorinfo;

		/**
		 * 错误询问信息
		 */
		private String questioninfo;

		/**
		 * 定义人（系统，用户）
		 */
		private String creator;
		
		/**
		 * 检查类型
		 */
		private String checktype;

		private String description;

		private int sortvalue = 0;

		public CheckItem() {

		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String getITEMNAME() {
			return itemname;
		}

		@Override
		public void setITEMNAME(String iTEMNAME) {
			itemname = iTEMNAME;
		}

		@Override
		public String getSQLEXPR() {
			return sqlexpr;
		}

		@Override
		public void setSQLEXPR(String sQL) {
			sqlexpr = sQL;
		}
		
		@Override
		public String getCHECKTYPE() {
			return checktype;
		}

		@Override
		public void setCHECKTYPE(String tYPE) {
			checktype = tYPE;
		}

		@Override
		public String getRESULTEXPR() {
			return resultexpr;
		}

		@Override
		public void setRESULTEXPR(String rESULTEXPR) {
			resultexpr = rESULTEXPR;
		}

		@Override
		public String getCHECKLEVEL() {
			return checklevel;
		}

		@Override
		public void setCHECKLEVEL(String eRRORLEVEL) {
			checklevel = eRRORLEVEL;
		}

		@Override
		public String getERRORINFO() {
			return errorinfo;
		}

		@Override
		public void setERRORINFO(String eRRORINFO) {
			errorinfo = eRRORINFO;
		}

		@Override
		public String getQUESTIONINFO() {
			return questioninfo;
		}

		@Override
		public void setQUESTIONINFO(String qUESTIONINFO) {
			questioninfo = qUESTIONINFO;
		}

		@Override
		public String getCREATOR() {
			return creator;
		}

		@Override
		public void setCREATOR(String cREATOR) {
			creator = cREATOR;
		}

		@Override
		public String getDESCRIPTION() {
			return description;
		}

		@Override
		public void setDESCRIPTION(String dESCRPTION) {
			description = dESCRPTION;
		}

		public int getSortvalue() {
			return sortvalue;
		}

		public void setSortvalue(int sortvalue) {
			this.sortvalue = sortvalue;
		}

	}

	public static class CheckGroup implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String groupname;

		private String groupid;

		private String workflowcodes;

		private List<String> items=new ArrayList<String>();;

		private String description;

		public CheckGroup() {

		}

		public String getGroupname() {
			return groupname;
		}

		public void setGroupname(String groupname) {
			this.groupname = groupname;
		}

		public String getGroupid() {
			return groupid;
		}

		public void setGroupid(String groupid) {
			this.groupid = groupid;
		}

		public List<String> getItems() {
			return items;
		}

		public void setItems(List<String> items) {
			this.items = items;
		}

		public String getWorkflowcodes() {
			return workflowcodes;
		}

		public boolean containItem(String itemid) {
			boolean bcontainse = false;
			if (this.items != null) {
				for (int i = 0; i < this.items.size(); i++) {
					if (this.items.get(i).equals(itemid)) {
						bcontainse = true;
						break;
					}
				}
			}
			else
			{
				this.items=new ArrayList<String>();
			}
			return bcontainse;
		}

		public boolean removeItem(String itemid) {
			if (this.items != null) {
			for (int i = 0; i < this.items.size(); i++) {
				if (this.items.get(i).equals(itemid)) {
					this.items.remove(i);
					break;
				}
			}
			}
			else
			{
				this.items=new ArrayList<String>();	
			}
			return true;
		}

		public void setWorkflowcodes(String workflowcodes) {
			this.workflowcodes = workflowcodes;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

}
