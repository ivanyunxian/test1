package com.supermap.realestate.registration.factorys;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.handlerImpl.BGDJHandler;
import com.supermap.realestate.registration.handlerImpl.CFDJ_HouseHandler;
import com.supermap.realestate.registration.handlerImpl.CFDJ_LandHandler;
import com.supermap.realestate.registration.handlerImpl.CFDJ_ZX_HouseHandler;
import com.supermap.realestate.registration.handlerImpl.CFDJ_ZX_LandHandler;
import com.supermap.realestate.registration.handlerImpl.CSDJHandler;
import com.supermap.realestate.registration.handlerImpl.DYDJHandler;
import com.supermap.realestate.registration.handlerImpl.DYZXDJHandler;
import com.supermap.realestate.registration.handlerImpl.DYZYDJHandler;
import com.supermap.realestate.registration.handlerImpl.GZDJHandler;
import com.supermap.realestate.registration.handlerImpl.YCDYDJHandler;
import com.supermap.realestate.registration.handlerImpl.YCSCDYDJHandler;
import com.supermap.realestate.registration.handlerImpl.YGDJHandler;
import com.supermap.realestate.registration.handlerImpl.YYDJHandler;
import com.supermap.realestate.registration.handlerImpl.ZXDJHandler;
import com.supermap.realestate.registration.handlerImpl.ZYDJHandler;
import com.supermap.realestate.registration.mapping.CheckConfig;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckGroup;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckItem;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.mapping.HandlerMapping.HandlerDefine;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.mapping.PageControlsConfig;
import com.supermap.realestate.registration.mapping.PageControlsConfig.PageControlsItem;
import com.supermap.realestate.registration.mapping.PageControlsConfig.ids;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.mapping.SelectorConfig.GridConfig;
import com.supermap.realestate.registration.mapping.SelectorConfig.GridConfig.Detail;
import com.supermap.realestate.registration.mapping.SelectorConfig.GridConfig.Detail.Field;
import com.supermap.realestate.registration.mapping.SelectorConfig.GridConfig.GridColumn;
import com.supermap.realestate.registration.mapping.SelectorConfig.QueryConfig;
import com.supermap.realestate.registration.mapping.SelectorConfig.QueryConfig.QueryField;
import com.supermap.realestate.registration.mapping.SelectorConfig.ResultConfig;
import com.supermap.realestate.registration.mapping.SelectorConfig.ResultConfig.ConstMapping;
import com.supermap.realestate.registration.mapping.SelectorConfig.SortField;
import com.supermap.realestate.registration.model.T_BASEWORKFLOW;
import com.supermap.realestate.registration.model.T_DETAIL_SELECTOR;
import com.supermap.realestate.registration.model.T_GRIDCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.T_HANDLER;
import com.supermap.realestate.registration.model.T_QUERYCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.T_RESULT_SELECTOR;
import com.supermap.realestate.registration.model.T_SELECTOR;
import com.supermap.realestate.registration.model.T_SORTCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.core.SuperXStream;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * 
 * 工厂类，主要功能：创建DJHandler、加载流程配置文件、获取选择器、获取流程配置等
 * @author 刘树峰
 * @date 2015年7月9日 下午5:31:14
 * @Copyright SuperMap
 */
public class HandlerFactory {

	private static HandlerMapping mapping = null;

	private static CheckConfig _checkconfigxml = null;

	private static PageControlsConfig _pagecontrolsconfigxml = null;
	@SuppressWarnings("unused")
	private static CheckConfig _checkconfigdb = null;

	/**
	 * 静态构造函数，读取配置文件
	 */
	static {
		mapping = getHandlerMappingFromConfig();
		// _checkconfigxml = getCheckFromConfig();
	}

	/**
	 * 对外接口：根据Project_id获取选择器
	 * @Title: getSelector
	 * @author:liushufeng
	 * @date：2015年7月16日 下午3:44:04
	 * @param xmbh
	 * @return
	 */
	public static SelectorConfig getSelectorByProjectID(String project_id) {
		HandlerMapping _mapping = getMapping();
		RegisterWorkFlow workflow = getWorkflow(project_id);
		if (workflow == null)
			return null;
		if (StringHelper.isEmpty(workflow.getSelector()))
			return null;
		String selectorname = workflow.getSelector();
		return _mapping.getSelector(selectorname);
	}

	/**
	 * 对外接口：根据选择器名称获取选择器
	 * @Title: getSelectorByName
	 * @author:liushufeng
	 * @date：2015年7月17日 下午6:13:52
	 * @param selectorName
	 * @return
	 */
	public static SelectorConfig getSelectorByName(String selectorName) {
		SelectorConfig config = null;
		HandlerMapping _mapping = getMapping();
		if (_mapping != null && _mapping.getSelectors() != null && _mapping.getSelectors().size() > 0) {
			config = _mapping.getSelector(selectorName);
		}
		return config;
	}

	/**
	 * 根据项目编号获取选择器
	 * @Title: getSelectorByXMBH
	 * @author:liushufeng
	 * @date：2015年7月25日 下午2:32:55
	 * @param xmbh
	 * @return
	 */
	public static SelectorConfig getSelectorByXMBH(String xmbh) {
		SelectorConfig config = null;
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if (info != null) {
			config = getSelectorByProjectID(info.getProject_id());
		}
		return config;
	}

	/**
	 * 对外接口：根据Project_id获取workflow
	 * @Title: getWorkflow
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:40:59
	 * @param project_id
	 * @return
	 */
	public static RegisterWorkFlow getWorkflow(String project_id) {
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(project_id);
		if (StringHelper.isEmpty(workflowcode))
			return null;
		HandlerMapping _mapping = getMapping();
		RegisterWorkFlow workflow = null;
		if (Global.USECACHEFORREGISTERWORKFLOW) {
			workflow = _mapping.getWorkflow(workflowcode);
		} else {
			String sql = " WORKFLOWCODE='" + workflowcode + "'";
			CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
			List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);
			if (mappings != null && mappings.size() > 0) {
				String name = mappings.get(0).getWORKFLOWNAME();
				workflow = _mapping.getWorkflowByName(name);
			}

		}
		return workflow;
	}

	/**
	 * 对外接口：根据xmbh（项目编号）获取所属登记类型handler
	 * @作者 刘树峰
	 * @创建时间 2015年7月9日下午5:32:00
	 * @param xmbh
	 * @return
	 */
	public static DJHandler createDJHandler(String xmbh) {
		/**
		 * 处理逻辑，首先去配置文件里边读取指定的handler，如果没有在配置文件中配置或者没有指定handler，给出一个系统默认的
		 */
		if (mapping == null)
			return null;
		DJHandler handler = null;
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if (info == null)
			return null;
		handler = getSysConfigHandler(info);
		if (handler == null)
			handler = getSysDefaultHandler(info);
		return handler;
	}

	/**
	 * 对外接口：重新加载配置文件
	 * @Title: reloadMappingConfig
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:43:47
	 */
	public static void reloadMappingConfig() {
		mapping = getHandlerMappingFromConfig();
	}

	/**
	 * 对外接口：获取XML里边配置的检查项
	 * @Title: getCheckConfig
	 * @author:liushufeng
	 * @date：2015年8月5日 下午3:22:14
	 * @return
	 */
	public static CheckConfig getCheckConfig() {
		// return null;
		if (_checkconfigxml == null) {
			_checkconfigxml = getCheckFromConfig();
		}
		return _checkconfigxml;
	}

	/**
	 * 对外接口：获取XML里边配置的页面控件配置
	 * @Title: getCheckConfig
	 * @author:wuzhu
	 * @date：
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getPageControlsConfig(int nameHashCode, String pageType) {
		Map<String, String> r = new HashMap<String, String>();
		boolean defaultflag = true;// 是否用默认配置
		if (_pagecontrolsconfigxml == null) {
			_pagecontrolsconfigxml = getPageControlsFromConfig();
		}
		for (PageControlsItem workflow : _pagecontrolsconfigxml.getWorkflows()) {
			if (workflow.getName().hashCode() == nameHashCode) {
				r.clear();
				ids myids = new PageControlsConfig().new ids();
				if (pageType.equals("selectorname"))
					myids = workflow.getSelectornames();
				if (pageType.equals("unitpagejsp"))
					myids = workflow.getUnitpagejsps();
				if (pageType.equals("rightspagejsp"))
					myids = workflow.getRightspagejsps();
				if (pageType.equals("bookpagejsp"))
					myids = workflow.getBookpagejsps();
				if (!StringHelper.isEmpty(myids.getDisabledids())) {
					for (String id : myids.getDisabledids().split("#")) {
						r.put(id, "disabled");
					}
				}
				if (!StringHelper.isEmpty(myids.getHiddenids())) {
					for (String id : myids.getHiddenids().split("#")) {
						r.put(id, "hidden");
					}
				}
				if (!StringHelper.isEmpty(myids.getShowids())) {
					for (String id : myids.getShowids().split("#")) {
						r.put(id, "show");
					}
				}
				defaultflag = false;// 不用默认配置
				break;
			}
			if (workflow.getName().equals("default") && defaultflag)// 默认配置
			{
				ids myids = new PageControlsConfig().new ids();
				if (pageType.equals("selectorname"))
					myids = workflow.getSelectornames();
				if (pageType.equals("unitpagejsp"))
					myids = workflow.getUnitpagejsps();
				if (pageType.equals("rightspagejsp"))
					myids = workflow.getRightspagejsps();
				if (pageType.equals("bookpagejsp"))
					myids = workflow.getBookpagejsps();
				if (!StringHelper.isEmpty(myids.getDisabledids())) {
					for (String id : myids.getDisabledids().split("#")) {
						r.put(id, "disabled");
					}
				}
				if (!StringHelper.isEmpty(myids.getHiddenids())) {
					for (String id : myids.getHiddenids().split("#")) {
						r.put(id, "hidden");
					}
				}
				if (!StringHelper.isEmpty(myids.getShowids())) {
					for (String id : myids.getShowids().split("#")) {
						r.put(id, "show");
					}
				}
			}
		}
		return r;
	}

	/**
	 * 内部方法：获取系统默认的处理器（在配置文件中没有指定的情况下）
	 * @Title: getSysDefaultHandler
	 * @author:liushufeng
	 * @date：2015年7月16日 下午3:45:01
	 * @param info
	 * @return
	 */
	private static DJHandler getSysDefaultHandler(ProjectInfo info) {
		DJHandler handler = null;
		if (info != null) {
			if (info.getDjlx().equals(DJLX.CSDJ.Value)) {
				if (!info.getQllx().equals(QLLX.DIYQ.Value)) {
					// 初始登记,非抵押权
					handler = new CSDJHandler(info);
				} else {
					// 受理类型1：一般抵押权在建工程抵押（50）；最高额抵押权在建工程抵押（51）
					if (info.getSllx1().equals("50") || info.getSllx1().equals("51")) {
						if (info.getSllx2().equals("01"))// 预测户
						{
							handler = new YCDYDJHandler(info);
						} else if (info.getSllx2().equals("02") || info.getSllx2().equals("03"))// 在建工程抵押权登记转现房抵押登记
																								// :02;
																								// 预购商品房抵押权预告登记转现房抵押权登记
																								// :03
						{
							handler = new YCSCDYDJHandler(info);
						}
					} else {
						// 初始登记抵押权
						handler = new DYDJHandler(info);
					}
				}

			} else if (info.getDjlx().equals(DJLX.ZYDJ.Value)) {
				if (info.getQllx().equals(QLLX.DIYQ.Value)) {
					// 转移登记抵押权
					handler = new DYZYDJHandler(info);
				} else {
					// 转移登记
					handler = new ZYDJHandler(info);
				}
			} else if (info.getDjlx().equals(DJLX.BGDJ.Value)) {
				// 变更登记
				handler = new BGDJHandler(info);
			} else if (info.getDjlx().equals(DJLX.YGDJ.Value)) {
				// 预告登记
				handler = new YGDJHandler(info);
			} else if (info.getDjlx().equals(DJLX.GZDJ.Value)) {
				// 更正登记
				handler = new GZDJHandler(info);
			} else if (info.getDjlx().equals(DJLX.YYDJ.Value)) {
				// 更正登记
				handler = new YYDJHandler(info);
			} else if (info.getDjlx().equals(DJLX.ZXDJ.Value)) {
				// 注销登记
				if (!info.getQllx().equals(QLLX.DIYQ.Value)) {
					// 注销登记,非抵押权
					handler = new ZXDJHandler(info);
				} else {
					// 注销登记抵押权
					handler = new DYZXDJHandler(info);
				}
			} else if (info.getDjlx().equals(DJLX.CFDJ.Value)) {
				if (info.getQllx().equals("99")) {
					if (info.getSllx1().equals("01"))
						handler = new CFDJ_LandHandler(info);
					else
						handler = new CFDJ_HouseHandler(info);
				}
				if (info.getQllx().equals("98")) {
					if (info.getSllx1().equals("01"))
						handler = new CFDJ_ZX_LandHandler(info);
					else
						handler = new CFDJ_ZX_HouseHandler(info);
				}
			}
		}
		return handler;
	}

	/**
	 * 内部方法：获取配置文件中指定的处理器
	 * @Title: getHandlerInstance
	 * @author:liushufeng
	 * @date：2015年7月13日 上午2:08:33
	 * @param info
	 * @return
	 */
	private static DJHandler getSysConfigHandler(ProjectInfo info) {
		HandlerMapping _mapping = getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
		String _handleClassName = _mapping.getHandlerClassName(workflowcode);
		if (StringHelper.isEmpty(_handleClassName))
			return null;
		DJHandler _handler = (DJHandler) createHandlerObject(_handleClassName, info);
		return _handler;
	}

	/**
	 * 内部方法：根据类名和参数创建对象
	 * @Title: createHandlerObject
	 * @author:liushufeng
	 * @date：2015年7月13日 上午2:08:18
	 * @param handlerClassName
	 * @param info
	 * @return
	 */
	private static Object createHandlerObject(String handlerClassName, ProjectInfo info) {
		Object _object = null;
		try {
			Class<?> T = Class.forName(handlerClassName);
			Class<?> _projectInfoClass = ProjectInfo.class;
			Constructor<?> _constructor = T.getConstructor(_projectInfoClass);
			_object = _constructor.newInstance(info);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return _object;
	}

	/**
	 * 内部方法：从流程配置文件加载配置
	 * @Title: getHandlerMappingFromConfig
	 * @author:liushufeng
	 * @date：2015年7月13日 上午12:55:14
	 * @return
	 */
	private static HandlerMapping getHandlerMappingFromConfig() {
		boolean b=true;
		if(b){
			return getHandlerMappingFromConfig2();
		}
		XStream xStream = new SuperXStream();
		xStream.registerConverter(new DateConverter("yyyy-MM-dd", null, TimeZone.getTimeZone("GMT+8")));
		xStream.alias("Mapping", HandlerMapping.class);
		xStream.alias("Handler", HandlerDefine.class);
		xStream.alias("Workflow", RegisterWorkFlow.class);
		xStream.alias("selector", SelectorConfig.class);
		xStream.alias("queryconfig", QueryConfig.class);
		xStream.alias("gridconfig", GridConfig.class);
		xStream.alias("queryfield", QueryField.class);
		xStream.alias("gridcolumn", GridColumn.class);
		xStream.alias("resultconfig", ResultConfig.class);
		xStream.alias("constmapping", ConstMapping.class);
		xStream.alias("detail", Detail.class);
		xStream.alias("field", Field.class);
		xStream.alias("sortfield", SortField.class);

		xStream.useAttributeFor(HandlerMapping.class, "defaultpackagename");
		xStream.useAttributeFor(HandlerDefine.class, "name");
		xStream.useAttributeFor(HandlerDefine.class, "caption");
		xStream.useAttributeFor(HandlerDefine.class, "handlerclassname");

		xStream.useAttributeFor(RegisterWorkFlow.class, "name");
		xStream.useAttributeFor(RegisterWorkFlow.class, "caption");
		xStream.useAttributeFor(RegisterWorkFlow.class, "code");
		xStream.useAttributeFor(RegisterWorkFlow.class, "handlername");

		xStream.useAttributeFor(SelectorConfig.class, "text");
		xStream.useAttributeFor(SelectorConfig.class, "name");

		HandlerMapping _mapping = null;
		try {
			InputStream stream = new FileInputStream(HandlerFactory.class.getResource("/HandlerMapping.xml").getFile());
			_mapping = (HandlerMapping) xStream.fromXML(stream);
			LoadWorkflowCode(_mapping);
			stream.close();
			System.out.println("重新读取XML成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		}
		System.out.println(_mapping.getSelectors().get(0).getText());
		return _mapping;
	}
	
	/**
	 * 内部方法：从流程配置文件加载配置
	 * @Title: getHandlerMappingFromConfig
	 * @author:yuxuebin
	 * @date：2015年11月12日 20:32:14
	 * @return
	 */
	private static HandlerMapping getHandlerMappingFromConfig2() {
		HandlerMapping _mapping = null;
		try {
			_mapping=new HandlerMapping();
			_mapping.setDefaultpackagename("com.supermap.realestate.registration.handlerImpl.");
			List<HandlerDefine> handlers=LoadHandlers();
			_mapping.setHandlers(handlers);
			List<SelectorConfig> selectors=LoadSelectors();
			_mapping.setSelectors(selectors);
			List<RegisterWorkFlow> workflows=LoadWorkflows();
			_mapping.setWorkflows(workflows);
			LoadWorkflowCode(_mapping);
			System.out.println("重新读取XML成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		}
		System.out.println(_mapping.getSelectors().get(0).getText());
		return _mapping;
	}
	/*
	 * 获取登记操作类
	 */
	private static List<HandlerDefine> LoadHandlers(){
		List<HandlerDefine> handlers=new ArrayList<HandlerDefine>();
		CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<T_HANDLER> listhandler=baseCommonDao.getDataList(T_HANDLER.class, "1>0");
		if(listhandler!=null&&listhandler.size()>0){
			for(T_HANDLER handler:listhandler){
				HandlerDefine handlerdefine=new HandlerDefine();
				handlerdefine.setCaption(handler.getNAME());
				handlerdefine.setName(handler.getId());
				handlerdefine.setHandlerClassName(handler.getCLASSNAME());
				handlerdefine.setDescription(handler.getDESCRIPTION());
				handlers.add(handlerdefine);
			}
		}
		return handlers;
	}
	
	/*
	 * 获取基准流程
	 */
	private static List<RegisterWorkFlow> LoadWorkflows(){
		List<RegisterWorkFlow> workflows=new ArrayList<RegisterWorkFlow>();
		CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<T_BASEWORKFLOW> listworkflow=baseCommonDao.getDataList(T_BASEWORKFLOW.class, "1>0 ORDER BY ID");
		if(listworkflow!=null&&listworkflow.size()>0){
			for(T_BASEWORKFLOW workflow:listworkflow){
				RegisterWorkFlow registerworkflow=new RegisterWorkFlow();
				registerworkflow.setBookpagejsp(workflow.getBOOKPAGEJSP());
				registerworkflow.setCaption(workflow.getNAME());
				registerworkflow.setCreatedate(workflow.getCREATETIME());
				registerworkflow.setCreatepersion(workflow.getCREATOR());
				registerworkflow.setDescription(workflow.getDESCRIPTION());
				registerworkflow.setDjlx(workflow.getDJLX());
				registerworkflow.setQllx(workflow.getQLLX());
				registerworkflow.setHandlername(workflow.getHANDLERID());
				registerworkflow.setName(workflow.getId());
				registerworkflow.setRightspagejsp(workflow.getRIGHTSPAGEJSP());
				registerworkflow.setSelector(workflow.getSELECTORID());
				registerworkflow.setUnitpagejsp(workflow.getUNITPAGEJSP());
				registerworkflow.setUnittype(workflow.getUNITTYPE());
				workflows.add(registerworkflow);
			}
		}
		return workflows;
	}
	
	/*
	 * 获取基准流程
	 */
	private static List<SelectorConfig> LoadSelectors(){
		List<SelectorConfig> selectors=new ArrayList<SelectorConfig>();
		CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<T_SELECTOR> listselector=baseCommonDao.getDataList(T_SELECTOR.class, "1>0");
		if(listselector!=null&&listselector.size()>0){
			for(T_SELECTOR selector:listselector){
				SelectorConfig selectorconfig=new SelectorConfig();
				selectorconfig.setBdcdylx(selector.getBDCDYLX());
				selectorconfig.setCondition(selector.getCONDITION());
				selectorconfig.setConfigsql(selector.getCONFIGSQL());
				
				selectorconfig.setIdfieldname(selector.getIDFIELDNAME());
				selectorconfig.setLy(selector.getLY());
				selectorconfig.setName(selector.getId());
				
				if(SF.YES.Name.equals(selector.getSELECTBDCDY())){
					selectorconfig.setSelectbdcdy(true);
				}else{
					selectorconfig.setSelectbdcdy(false);
				}
				if(SF.YES.Name.equals(selector.getSELECTQL())){
					selectorconfig.setSelectql(true);
				}else{
					selectorconfig.setSelectql(false);
				}
				selectorconfig.setSelectqllx(selector.getSELECTQLLX());
				if(SF.YES.Name.equals(selector.getSINGLESELECT())){
					selectorconfig.setSingleselect(true);
				}else{
					selectorconfig.setSingleselect(false);
				}
				selectorconfig.setText(selector.getNAME());
				if(SF.YES.Name.equals(selector.getUSECONFIGSQL())){
					selectorconfig.setUseconfigsql(true);
				}else{
					selectorconfig.setUseconfigsql(false);
				}
				
				
				GridConfig gridconfig=new GridConfig();
				gridconfig.setCheckbox(true);
				if(!SF.YES.Name.equals(selector.getDEFAULTSELECTFIRT())){
					gridconfig.setDefaultselectfirst(false);
				}else{
					gridconfig.setDefaultselectfirst(true);
				}
				if(SF.YES.Name.equals(selector.getSHOWDETAILALTERSELECT())){
					gridconfig.setShowdetailafterselect(true);
				}else{
					gridconfig.setShowdetailafterselect(false);
				}
				if(SF.YES.Name.equals(selector.getSINGLESELECT())){
					gridconfig.setSingleselect(true);
				}else{
					gridconfig.setSingleselect(false);
				}
				
				List<GridColumn> columns=new ArrayList<GridConfig.GridColumn>();
				List<T_GRIDCONFIG_SELECTOR> listgrid=baseCommonDao.getDataList(T_GRIDCONFIG_SELECTOR.class, "SELECTORID='"+selector.getId()+"' ORDER BY SXH");
				if(listgrid!=null&&listgrid.size()>0){
					for(T_GRIDCONFIG_SELECTOR gridconfi:listgrid){
						GridColumn column=new GridColumn();
						column.setColumntext(gridconfi.getCOLUMNTEXT());
						column.setFieldname(gridconfi.getFIELDNAME());
						column.setWidth(gridconfi.getWIDTH());
						columns.add(column);
					}
				}
				gridconfig.setColumns(columns);
				Detail detial=new Detail();
				List<Field> fields=new ArrayList<Field>();
				List<T_DETAIL_SELECTOR> listdetail=baseCommonDao.getDataList(T_DETAIL_SELECTOR.class, "SELECTORID='"+selector.getId()+"' ORDER BY SXH");
				if(listdetail!=null&&listdetail.size()>0){
					for(T_DETAIL_SELECTOR detialconfig:listdetail){
						Field field=new Field();
						field.setColor(detialconfig.getFIELDCOLOR());
						field.setFieldname(detialconfig.getFIELDNAME());
						field.setFieldtext(detialconfig.getFIELDTEXT());
						fields.add(field);
					}
				}
				detial.setFields(fields);
				gridconfig.setDetail(detial);
				selectorconfig.setGridconfig(gridconfig);
				
				QueryConfig queryconfig=new QueryConfig();
				List<QueryField> queryfields=new ArrayList<QueryField>();
				List<T_QUERYCONFIG_SELECTOR> listquery=baseCommonDao.getDataList(T_QUERYCONFIG_SELECTOR.class, "SELECTORID='"+selector.getId()+"' ORDER BY SXH");
				if(listquery!=null&&listquery.size()>0){
					for(T_QUERYCONFIG_SELECTOR query:listquery){
						QueryField queryfield=new QueryField();
						queryfield.setEntityname(query.getENTITYNAME());
						queryfield.setFieldcaption(query.getFIELDCAPTION());
						queryfield.setFieldname(query.getFIELDNAME());
						queryfields.add(queryfield);
					}
				}
				queryconfig.setFields(queryfields);
				selectorconfig.setQueryconfig(queryconfig);
				
				
				ResultConfig resultconfig=new ResultConfig();
				List<ConstMapping> constmappings=new ArrayList<ConstMapping>();
				List<T_RESULT_SELECTOR> listresult=baseCommonDao.getDataList(T_RESULT_SELECTOR.class, "SELECTORID='"+selector.getId()+"'");
				if(listresult!=null&&listresult.size()>0){
					for(T_RESULT_SELECTOR result:listresult){
						ConstMapping constmap=new ConstMapping();
						constmap.setConsttype(result.getCONSTTYPE());
						constmap.setDefaultvalue(result.getDEFLAULTVALUE());
						constmap.setFieldname(result.getFIELDNAME());
						if(SF.YES.Name.equals(result.getNEWFIELDENDWITHNAME())){
							constmap.setNewfieldendwithname(true);
						}else{
							constmap.setNewfieldendwithname(false);
						}
						constmappings.add(constmap);
					}
				}
				resultconfig.setConstmappings(constmappings);
				selectorconfig.setResultconfig(resultconfig);
				
				
				List<SortField> sortfields=new ArrayList<SortField>();
				List<T_SORTCONFIG_SELECTOR> listsort=baseCommonDao.getDataList(T_SORTCONFIG_SELECTOR.class, "SELECTORID='"+selector.getId()+"' ORDER BY SXH");
				if(listsort!=null&&listsort.size()>0){
					for(T_SORTCONFIG_SELECTOR sortconfig:listsort){
						SortField sortfield=new SortField();
						sortfield.setEntityname(sortconfig.getENTITYNAME());
						sortfield.setFieldname(sortconfig.getFIELDNAME());
						sortfield.setSorttype(sortconfig.getSORTTYPE());
						sortfields.add(sortfield);
					}
				}
				selectorconfig.setSortfields(sortfields);
				selectors.add(selectorconfig);
			}
		}
		return selectors;
	}
	

	private static void LoadWorkflowCode(HandlerMapping _mapping) {
		if (_mapping != null) {
			Map<String, List<String>> name_code = new HashMap<String, List<String>>();
			Map<String, Map<String, String>> name_map = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> name_map_land = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> name_map_btn = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> name_map_dyfs = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> name_map_unitpageid = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> name_map_rightpageid = new HashMap<String, Map<String, String>>();
			//选择单元时，是否通过楼盘表显示选择单元
			Map<String,Map <String,String>> name_map_buildingtable=new HashMap<String,Map<String,String>>();
			CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
			List<WFD_MAPPING> listMap = baseCommonDao.getDataList(WFD_MAPPING.class, "1>0");
			if (listMap != null && listMap.size() > 0) {
				for (int imapping = 0; imapping < listMap.size(); imapping++) {
					WFD_MAPPING map = listMap.get(imapping);
					String name = map.getWORKFLOWNAME();
					String code = map.getWORKFLOWCODE();
					String houseedit = map.getHOUSEEDIT();
					String landedit = map.getLANDEDIT();
					String buildingtable=map.getSHOWBUILDINGTABLE();
					String showDataReportBtn = map.getSHOWDATAREPORTBTN();
					String dyfs = map.getDYFS();
					String unitpageid = map.getUNITPAGEID();
					String rightpageid = map.getRIGHTPAGEID();
					if (!StringHelper.isEmpty(code) && !StringHelper.isEmpty(code)) {
						if (!name_code.containsKey(name)) {
							List<String> list_code=new ArrayList<String>();
							list_code.add(code);
							name_code.put(name, list_code);
						} else {
							List<String> list_code=name_code.get(name);
							if(!list_code.contains(code)){
								list_code.add(code);
							}
							name_code.put(name, list_code);
						}
						if (!name_map.containsKey(name)) {
							Map<String, String> code_houseedit = new HashMap<String, String>();
							code_houseedit.put(code, houseedit);
							name_map.put(name, code_houseedit);
						} else {
							if (!name_map.get(name).containsKey(code)) {
								name_map.get(name).put(code, houseedit);
							}
						}
						if (!name_map_land.containsKey(name)) {
							Map<String, String> code_landedit = new HashMap<String, String>();
							code_landedit.put(code, landedit);
							name_map_land.put(name, code_landedit);
						} else {
							if (!name_map_land.get(name).containsKey(code)) {
								name_map_land.get(name).put(code, landedit);
							}
						}
						if (!name_map_btn.containsKey(name)){
							Map<String, String> code_dataReportBtn = new HashMap<String, String>();
							code_dataReportBtn.put(code, showDataReportBtn);
							name_map_btn.put(name, code_dataReportBtn);
						} else {
							if(!name_map_btn.get(name).containsKey(code)){
								name_map_btn.get(name).put(code, showDataReportBtn);
							}
						}
						if(!name_map_buildingtable.containsKey(name))
						{
							Map<String,String> code_buildingtable=new HashMap<String,String>();
							code_buildingtable.put(code,buildingtable);
							name_map_buildingtable.put(name, code_buildingtable);
						}
						else
						{
							if(!name_map_buildingtable.get(name).containsKey(code))
							{
								name_map_buildingtable.get(name).put(code, buildingtable);
							}
						}
						if (!name_map_dyfs.containsKey(name)) {
							Map<String, String> code_dyfs = new HashMap<String, String>();
							code_dyfs.put(code, dyfs);
							name_map_dyfs.put(name, code_dyfs);
						} else {
							if (!name_map_dyfs.get(name).containsKey(code)) {
								name_map_dyfs.get(name).put(code, dyfs);
							}
						}
						
						if (!name_map_unitpageid.containsKey(name)) {
							Map<String, String> code_unitpageid = new HashMap<String, String>();
							code_unitpageid.put(code, unitpageid);
							name_map_unitpageid.put(name, code_unitpageid);
						} else {
							if (!name_map_unitpageid.get(name).containsKey(code)) {
								name_map_unitpageid.get(name).put(code, unitpageid);
							}
						}
						
						if (!name_map_rightpageid.containsKey(name)) {
							Map<String, String> code_rightpageid = new HashMap<String, String>();
							code_rightpageid.put(code, rightpageid);
							name_map_rightpageid.put(name, code_rightpageid);
						} else {
							if (!name_map_rightpageid.get(name).containsKey(code)) {
								name_map_rightpageid.get(name).put(code, rightpageid);
							}
						}
					}
				}
			}
			List<RegisterWorkFlow> list = _mapping.getWorkflows();
			for (int i = 0; i < list.size(); i++) {
				RegisterWorkFlow workflow = list.get(i);
				String name = workflow.getName();
				if (name_code.containsKey(name)) {
					workflow.setCode(name_code.get(name));
				}
				if (name_map.containsKey(name)) {
					workflow.setHouseeditmap(name_map.get(name));
				}
				if (name_map_land.containsKey(name)) {
					workflow.setLandeditmap(name_map_land.get(name));
				}
				if (name_map_btn.containsKey(name)){
					workflow.setDataReportBtneditmap(name_map_btn.get(name));
				}
				if(name_map_buildingtable.containsKey(name))
				{
					workflow.setBuildingTablemap(name_map_buildingtable.get(name));
				}
				if (name_map_dyfs.containsKey(name)) {
					workflow.setDyfsmap(name_map_dyfs.get(name));
				}
				if (name_map_unitpageid.containsKey(name)) {
					workflow.setUnitpageidmap(name_map_unitpageid.get(name));
				}
				if (name_map_rightpageid.containsKey(name)) {
					workflow.setRightpageidmap(name_map_rightpageid.get(name));
				}
			}
		}
	}

	/**
	 * 内部方法：加载配置文件
	 * @Title: getMapping
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:41:39
	 * @return
	 */
	public static HandlerMapping getMapping() {
		if (mapping == null)
			mapping = getHandlerMappingFromConfig();
		return mapping;
	}

	private static CheckConfig getCheckFromConfig() {
		XStream xStream = new SuperXStream();
		xStream.registerConverter(new DateConverter("yyyy-MM-dd", null, TimeZone.getTimeZone("GMT+8")));
		xStream.alias("CheckConfig", CheckConfig.class);
		xStream.alias("CheckItem", CheckItem.class);
		xStream.alias("checkgroup", CheckGroup.class);
		xStream.alias("string", String.class);

		xStream.useAttributeFor(CheckItem.class, "itemname");
		xStream.useAttributeFor(CheckItem.class, "id");

		CheckConfig _mapping = null;
		try {
			InputStream stream = new FileInputStream(HandlerFactory.class.getResource("/CheckConfig.xml").getFile());
			_mapping = (CheckConfig) xStream.fromXML(stream);
			stream.close();
			System.out.println("重新读取XML成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		}
		return _mapping;
	}

	private static PageControlsConfig getPageControlsFromConfig() {
		XStream xStream = new SuperXStream();
		xStream.registerConverter(new DateConverter("yyyy-MM-dd", null, TimeZone.getTimeZone("GMT+8")));

		xStream.processAnnotations(PageControlsConfig.class);
		xStream.processAnnotations(PageControlsItem.class);
		xStream.processAnnotations(ids.class);
		// xStream.alias("controlid", String.class);
		// xStream.alias("controlid2", String.class);

		PageControlsConfig _mapping = null;
		try {
			InputStream stream = new FileInputStream(HandlerFactory.class.getResource("/PageControlsConfig.xml").getFile());
			_mapping = (PageControlsConfig) xStream.fromXML(stream);
			stream.close();
			System.out.println("重新读取XML成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		}
		return _mapping;
	}
/**
 * 加载workflow	
 * @return
 */
	public static List<RegisterWorkFlow> getWorkflows() {
		if(mapping==null){
			mapping = getHandlerMappingFromConfig();
		}
		 return mapping.getWorkflows();
	}
}
