/**   
 * 流程以及处理器映射类
 * @Title: HandlerMapping.java 
 * @Package com.supermap.realestate.registration.mapping 
 * @author liushufeng 
 * @date 2015年7月13日 上午12:00:49 
 * @version V1.0   
 */
package com.supermap.realestate.registration.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 流程以及处理器映射类，包含了处理器定义列表和流程配置列表，每个流程配置中包含了流程编码和处理器定义名称
 * @ClassName: HandlerMapping
 * @author liushufeng
 * @date 2015年7月13日 上午12:00:49
 */
public class HandlerMapping implements Serializable {

	private static final long serialVersionUID = 4441208255220975283L;

	/**
	 * 构造函数，可序列化类必须要有
	 */
	public HandlerMapping() {

	}

	/**
	 * 处理器所在包的默认名称
	 */
	private String defaultpackagename = "com.supermap.realestate.registration.handlerImpl.";

	/**
	 * 处理器列表
	 */
	private List<HandlerDefine> handlers = new ArrayList<HandlerMapping.HandlerDefine>();

	/**
	 * 登记流程列表
	 */
	private List<RegisterWorkFlow> workflows = new ArrayList<HandlerMapping.RegisterWorkFlow>();

	/**
	 * 选择器列表
	 */
	private List<SelectorConfig> selectors = new ArrayList<SelectorConfig>();

	/**
	 * 对外接口：根据选择器名称获取选择器
	 * @Title: getSelector
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:24:23
	 * @param name
	 * @return
	 */
	public SelectorConfig getSelector(String name) {
		SelectorConfig config = null;
		if (this.selectors != null && this.selectors.size() > 0) {
			for (int i = 0; i < this.selectors.size(); i++) {
				if (this.selectors.get(i).getName().equals(name)) {
					config = this.selectors.get(i);
					break;
				}
			}
		}
		return config;
	}

	/**
	 * 对外接口：获取处理器定义
	 * @Title: getHandlerDefine
	 * @author:liushufeng
	 * @date：2015年7月13日 上午1:51:33
	 * @param handlerName
	 * @return
	 */
	public HandlerDefine getHandlerDefine(String handlerName) {
		HandlerDefine _define = null;
		if (StringHelper.isEmpty(handlerName))
			return null;
		if (handlers == null || handlers.size() <= 0)
			return null;
		for (HandlerDefine define : handlers) {
			if (define.name.equals(handlerName)||define.handlerclassname.equals(handlerName)) {
				_define = define;
				break;
			}
		}
		return _define;
	}

	/**
	 * 对外接口：根据流程编码获取流程定义
	 * @Title: getWorkflow
	 * @author:liushufeng
	 * @date：2015年7月13日 上午1:46:22
	 * @param code
	 * @return
	 */
	public RegisterWorkFlow getWorkflow(String code) {
		if (StringHelper.isEmpty(code))
			return null;
		RegisterWorkFlow _flow = null;
		if (workflows == null || workflows.size() <= 0)
			return null;
		for (RegisterWorkFlow flow : workflows) {
			if (flow != null && !StringHelper.isEmpty(flow.code)) {
				if (flow.code.contains(code)) {
					_flow = flow;
					break;
				}
			}
		}
		return _flow;
	}

	public RegisterWorkFlow getWorkflowByName(String name)
	{
		if (StringHelper.isEmpty(name))
			return null;
		RegisterWorkFlow _flow = null;
		if (workflows == null || workflows.size() <= 0)
			return null;
		for (RegisterWorkFlow flow : workflows) {
			if (flow != null && !StringHelper.isEmpty(flow.name)) {
				if (name.equals(flow.name)) {
					_flow = flow;
					break;
				}
			}
		}
		return _flow;
	}
	
	/**
	 * 对外接口：根据流程编码获取处理器类名
	 * @Title: getHandlerClassName
	 * @author:liushufeng
	 * @date：2015年7月13日 上午1:52:43
	 * @param workflowCode
	 * @return
	 */
	public String getHandlerClassName(String workflowCode) {
		RegisterWorkFlow _flow = getWorkflow(workflowCode);
		if (_flow == null)
			return null;
		String _handlerName = _flow.getHandlername();
		HandlerDefine _define = getHandlerDefine(_handlerName);
		if (_define == null)
			return null;
		String className = _define.handlerclassname;
		if (!className.contains("."))
			className = defaultpackagename + className;
		return className;
	}

	/************** getter and setter *************/

	public List<SelectorConfig> getSelectors() {
		return selectors;
	}

	public void setSelectors(List<SelectorConfig> selectors) {
		this.selectors = selectors;
	}

	public List<RegisterWorkFlow> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(List<RegisterWorkFlow> workflows) {
		this.workflows = workflows;
	}

	public String getDefaultpackagename() {
		return defaultpackagename;
	}

	public void setDefaultpackagename(String defaultpackagename) {
		this.defaultpackagename = defaultpackagename;
	}

	public List<HandlerDefine> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<HandlerDefine> handlers) {
		this.handlers = handlers;
	}

	/**
	 * 内部静态类：登记流程处理器定义类
	 * @ClassName: HandlerDefine
	 * @author liushufeng
	 * @date 2015年7月13日 上午1:01:53
	 */
	public static class HandlerDefine implements Serializable {

		/**
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = -3093621874724377438L;

		public HandlerDefine() {

		}

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 别名
		 */
		private String caption;

		/**
		 * 处理器对应类名
		 */
		private String handlerclassname;

		/**
		 * 描述
		 */
		private String description;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getHandlerClassName() {
			return handlerclassname;
		}

		public void setHandlerClassName(String handlerClassName) {
			this.handlerclassname = handlerClassName;
		}
	}

	/**
	 * 内部静态类：工作流程配置类
	 * @ClassName: Workflow
	 * @author liushufeng
	 * @date 2015年7月13日 上午1:02:23
	 */
	public static class RegisterWorkFlow implements Serializable {

		private static final long serialVersionUID = 244748405909984117L;

		private Map<String, String> houseeditmap = new HashMap<String, String>();

		public Map<String, String> getHouseeditmap() {
			return houseeditmap;
		}

		public void setHouseeditmap(Map<String, String> houseeditmap) {
			this.houseeditmap = houseeditmap;
		}
		
		private Map<String, String> landeditmap = new HashMap<String, String>();

		public Map<String, String> getLandeditmap() {
			return landeditmap;
		}

		public void setLandeditmap(Map<String, String> houseeditmap) {
			this.landeditmap = houseeditmap;
		}
		
		private Map<String, String> dataReportBtneditmap = new HashMap<String, String>();
		
		public Map<String, String> getDataReportBtneditmap() {
			return dataReportBtneditmap;
		}

		public void setDataReportBtneditmap(Map<String, String> dataReportBtneditmap) {
			this.dataReportBtneditmap = dataReportBtneditmap;
		}
		
        public Map<String, String> getBuildingTablemap() {
			return buildingTablemap;
		}

		public void setBuildingTablemap(Map<String, String> buildingTablemap) {
			this.buildingTablemap = buildingTablemap;
		}

		private Map<String, String> buildingTablemap = new HashMap<String, String>();
		
		
		private Map<String, String> dyfsmap = new HashMap<String, String>();

		public Map<String, String> getDyfsmap() {
			return dyfsmap;
		}

		public void setDyfsmap(Map<String, String> dyfsmap) {
			this.dyfsmap = dyfsmap;
		}
		
		private Map<String, String> unitpageidmap = new HashMap<String, String>();

		public Map<String, String> getUnitpageidmap() {
			return unitpageidmap;
		}

		public void setUnitpageidmap(Map<String, String> unitpageidmap) {
			this.unitpageidmap = unitpageidmap;
		}
		
		private Map<String, String> rightpageidmap = new HashMap<String, String>();

		public Map<String, String> getRightpageidmap() {
			return rightpageidmap;
		}

		public void setRightpageidmap(Map<String, String> rightpageidmap) {
			this.rightpageidmap = rightpageidmap;
		}
		

		public RegisterWorkFlow() {

		}

		/**
		 * 流程标识
		 */
		private String name;

		/**
		 * 流程名称
		 */
		private String caption;

		/**
		 * 流程编码
		 */
		private List<String> code;

		/**
		 * 处理器名称
		 */
		private String handlername;

		/**
		 * 登记类型
		 */
		private String djlx;

		/**
		 * 权利类型dELYCDY
		 */
		private String qllx;

		/**
		 * 不动产单元类型
		 */
		private String unittype;

		/**
		 * 流程描述
		 */
		private String description;

		/**
		 * 创建人员
		 */
		private String createpersion;

		/**
		 * 创建时间
		 */
		private Date createdate;

		/**
		 * 选择器
		 */
		private String selectorname;

		/**
		 * 单元信息页
		 */
		private String unitpagejsp;

		/**
		 * 权利信息页
		 */
		private String rightspagejsp;

		/**
		 * 登记簿预览信息页
		 */
		private String bookpagejsp;
		

		/**
		 * @return createdate
		 */
		public Date getCreatedate() {
			return createdate;
		}

		/**
		 * @param createdate
		 *            要设置的 createdate
		 */
		public void setCreatedate(Date createdate) {
			this.createdate = createdate;
		}

		/**
		 * @return selector
		 */
		public String getSelector() {
			return selectorname;
		}

		/**
		 * @param selector
		 *            要设置的 selector
		 */
		public void setSelector(String selector) {
			this.selectorname = selector;
		}

		/**
		 * @return unitpagejsp
		 */
		public String getUnitpagejsp() {
			return unitpagejsp;
		}

		/**
		 * @param unitpagejsp
		 *            要设置的 unitpagejsp
		 */
		public void setUnitpagejsp(String unitpagejsp) {
			this.unitpagejsp = unitpagejsp;
		}

		/**
		 * @return rightspagejsp
		 */
		public String getRightspagejsp() {
			return rightspagejsp;
		}

		/**
		 * @param rightspagejsp
		 *            要设置的 rightspagejsp
		 */
		public void setRightspagejsp(String rightspagejsp) {
			this.rightspagejsp = rightspagejsp;
		}

		/**
		 * @return bookpagejsp
		 */
		public String getBookpagejsp() {
			return bookpagejsp;
		}

		/**
		 * @param bookpagejsp
		 *            要设置的 bookpagejsp
		 */
		public void setBookpagejsp(String bookpagejsp) {
			this.bookpagejsp = bookpagejsp;
		}

		/**
		 * @return djlx
		 */
		public String getDjlx() {
			return djlx;
		}

		/**
		 * @param djlx
		 *            要设置的 djlx
		 */
		public void setDjlx(String djlx) {
			this.djlx = djlx;
		}

		/**
		 * @return qllx
		 */
		public String getQllx() {
			return qllx;
		}

		/**
		 * @param qllx
		 *            要设置的 qllx
		 */
		public void setQllx(String qllx) {
			this.qllx = qllx;
		}

		/**
		 * @return createtime
		 */
		public Date getCreatetime() {
			return createdate;
		}

		/**
		 * @param createtime
		 *            要设置的 createtime
		 */
		public void setCreatetime(Date createtime) {
			this.createdate = createtime;
		}

		/**
		 * @return createpersion
		 */
		public String getCreatepersion() {
			return createpersion;
		}

		/**
		 * @param createpersion
		 *            要设置的 createpersion
		 */
		public void setCreatepersion(String createpersion) {
			this.createpersion = createpersion;
		}

		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            要设置的 name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return code
		 */
		public List<String> getCode() {
			return code;
		}

		/**
		 * @param code
		 *            要设置的 code
		 */
		public void setCode(List<String> code) {
			this.code = code;
		}

		/**
		 * @return unittype
		 */
		public String getUnittype() {
			return unittype;
		}

		/**
		 * @param unittype
		 *            要设置的 unittype
		 */
		public void setUnittype(String unittype) {
			this.unittype = unittype;
		}

		/**
		 * @return description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param unittype
		 *            要设置的 description
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return handlername
		 */
		public String getHandlername() {
			return handlername;
		}

		/**
		 * @param handlername
		 *            要设置的 handlername
		 */
		public void setHandlername(String handlername) {
			this.handlername = handlername;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}
		
	}

}
