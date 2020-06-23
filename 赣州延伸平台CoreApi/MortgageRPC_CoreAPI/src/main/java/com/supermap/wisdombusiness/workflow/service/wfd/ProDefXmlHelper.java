package com.supermap.wisdombusiness.workflow.service.wfd;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.service.common.Common;

@Component("smProDefXmlHelper")
public class ProDefXmlHelper {

	@Autowired
	private SmProDef _SmProDef;
	@Autowired
	CommonDao _CommonDao;

	private Document _XDocument;

	private Element _RootXElement;

	private List<Element> _FlowGraphicsList;

	private List<Element> _FlowArrowList;

	@SuppressWarnings("unchecked")
	public void LoadData(InputStream _InputStream) {
		if (_InputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(
						new InputStreamReader(_InputStream));

				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				String _Result_XmlString = URLDecoder.decode(
						tStringBuffer.toString(), "UTF-8");

				if (null != _Result_XmlString && !"".equals(_Result_XmlString)) {
					if (_Result_XmlString.indexOf("<") != -1
							&& _Result_XmlString.lastIndexOf(">") != -1
							&& _Result_XmlString.lastIndexOf(">") > _Result_XmlString
									.indexOf("<"))
						_Result_XmlString = _Result_XmlString.substring(
								_Result_XmlString.indexOf("<"),
								_Result_XmlString.lastIndexOf(">") + 1);
				}

				// 加载为XML
				set_XDocument(DocumentHelper.parseText(_Result_XmlString));
				// 获取根节点
				set_RootXElement(get_XDocument().getRootElement());
				// 获取_FlowGraphicsList
				set_FlowGraphicsList(get_RootXElement().element(
						"FlowGraphicsCollection").elements("FlowGraphics"));
				// 获取_FlowGraphicsList
				set_FlowArrowList(get_RootXElement().element(
						"FlowArrowCollection").elements("FlowArrow"));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void LoadData(String XmlText) {

		try {
			// 加载为XML
			set_XDocument(DocumentHelper.parseText(XmlText));
			// 获取根节点
			set_RootXElement(get_XDocument().getRootElement());
			// 获取_FlowGraphicsList
			set_FlowGraphicsList(get_RootXElement().element(
					"FlowGraphicsCollection").elements("FlowGraphics"));
			// 获取_FlowGraphicsList
			set_FlowArrowList(get_RootXElement().element("FlowArrowCollection")
					.elements("FlowArrow"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 
	 */
	public void Save() {
		String ProcessID = get_RootXElement().element("Process").elementText(
				"Process_ID");
		// 删除不存在的
		DeleteNotExisitData(ProcessID);
		// 添加或修改
		SaveData(ProcessID);

		UpdatePreActCount(ProcessID);
	}

	/**
	 * 
	 * @param PrddefID
	 */
	public void DeleteNotExisitData(String PrddefID) {
		Boolean _IsExisit = false;
		List<Wfd_Route> RouteList = _SmProDef.RouteList(PrddefID);
		// 路由
		
		for (int i = 0; i < RouteList.size(); i++) {
			_IsExisit = false;
			Wfd_Route _Route = new Wfd_Route();
			_Route = RouteList.get(i);
			for (int j = 0; j < _FlowArrowList.size(); j++) {
				{
					String Route_ID = _FlowArrowList.get(j).elementText(
							"Route_ID");
					if (Route_ID == null || Route_ID.equals("")) {
						continue;
					}
					if (_Route.getRoute_Id().equals(Route_ID)) {
						_IsExisit = true;
						break;
					}
				}
			}
			if (!_IsExisit) {
				_CommonDao.delete(Wfd_Route.class, _Route.getRoute_Id());
				//_CommonDao.flush();
			}
		}

		List<Wfd_Actdef> ActdefList = _SmProDef.ActdefList(PrddefID);
		// 活动
		
		for (int i = 0; i < ActdefList.size(); i++) {
			_IsExisit = false;
			Wfd_Actdef _Actdef = new Wfd_Actdef();
			_Actdef = ActdefList.get(i);
			for (int j = 0; j < _FlowGraphicsList.size(); j++) {
				{
					String Actdef_ID = _FlowGraphicsList.get(j).elementText(
							"Activity_ID");
					if (Actdef_ID == null || Actdef_ID.equals("")) {
						continue;
					}
					if (_Actdef.getActdef_Id().equals(Actdef_ID)) {
						_IsExisit = true;
						break;
					}
				}
			}
			if (!_IsExisit) {
				_CommonDao.delete(Wfd_Actdef.class, _Actdef.getActdef_Id());
				//_CommonDao.flush();
			}
		}

		_CommonDao.flush();

	}

	/**
	 *  
	 */
	private void SaveData(String ProdefID) {
		Boolean _IsExisit = false;
		// Wfd_Prodef Prodef=_CommonDao.load(Wfd_Prodef.class, PrddefID);
		List<Wfd_Route> RouteList = _SmProDef.RouteList(ProdefID);
		List<Wfd_Actdef> ActdefList = _SmProDef.ActdefList(ProdefID);
		// 活动
		Wfd_Actdef _Actdef = null;
		for (int i = 0; i < _FlowGraphicsList.size(); i++) {
			{
				_IsExisit = false;
				Element _Element = _FlowGraphicsList.get(i);
				String Actdef_ID = _Element.elementText("Activity_ID");
				if (Actdef_ID != null && !Actdef_ID.equals("")) {
					for (int j = 0; j < ActdefList.size(); j++) {
						_Actdef = ActdefList.get(j);
						if (Actdef_ID.equals(_Actdef.getActdef_Id())) {
							_IsExisit = true;
							break;
						}
					}
				}

				// 库里不存在
				if (!_IsExisit) {
					_Actdef = new Wfd_Actdef();
					_Actdef.setProdef_Id(ProdefID);
					//设置默认值
					_Actdef.setActdef_Time(3.0);
					_Actdef.setActdef_Status(1);
					_Actdef.setCanlogoutornot(1);
					_Actdef.setIsrandom(0);
					_Actdef.setRandomvalue(0.0);
					_Actdef.setIsautopass(0);
					
				}

				_Actdef.setActdef_Name(_Element.elementText("Activity_Name"));
				_Actdef.setActdef_Positionx(_Element
						.elementText("Activity_PositionX"));
				_Actdef.setActdef_Positiony(_Element
						.elementText("Activity_PositionY"));

				if (_IsExisit) {
					_CommonDao.update(_Actdef);
				} else {
					_Actdef.setProdef_Id(ProdefID);
					_Actdef.setActdef_Type(_Element
							.elementText("Activity_Type"));
					_CommonDao.save(_Actdef);
					ActdefList.add(_Actdef);
				}

				// 更新路由集合中标志
				for (int j = 0; j < _FlowArrowList.size(); j++) {
					Element _Element2 = _FlowArrowList.get(j);
					String tActdef_ID = _Element2.elementText("Activity_ID");
					String tNextActdef_ID = _Element2
							.elementText("NextActivity_ID");
					if (tActdef_ID.equals("") || tActdef_ID == null) {
						if (_Element2.elementText("FlowGraphics_Start").equals(
								_Element.elementText("UniqueID"))) {
							_Element2.element("Activity_ID").setText(
									_Actdef.getActdef_Id());
						}
					}
					if (tNextActdef_ID.equals("") || tNextActdef_ID == null) {
						if (_Element2.elementText("FlowGraphics_End").equals(
								_Element.elementText("UniqueID"))) {
							_Element2.element("NextActivity_ID").setText(
									_Actdef.getActdef_Id());
						}
					}
				}
			}
		}

		// 路由
		Wfd_Route _Route = null;
		for (int i = 0; i < _FlowArrowList.size(); i++) {
			_IsExisit = false;
			Element _Element = _FlowArrowList.get(i);
			String Route_ID = _Element.elementText("Route_ID");
			if (Route_ID != null && !Route_ID.equals("")) {
				for (int j = 0; j < RouteList.size(); j++) {
					_Route = RouteList.get(j);
					if (Route_ID.equals(_Route.getRoute_Id())) {
						_IsExisit = true;
						break;
					}
				}
			}

			if (!_IsExisit) {
				_Route = new Wfd_Route();
				_Route.setProdef_Id(ProdefID);
			}

			_Route.setRoute_Value(_Element.elementText("Route_Value"));
			_Route.setRoute_Name(_Element.elementText("Route_Value"));
			_Route.setActdef_Id(_Element.elementText("Activity_ID"));
			_Route.setNext_Actdef_Id(_Element.elementText("NextActivity_ID"));

			if (!_IsExisit) {
				_CommonDao.save(_Route);
				RouteList.add(_Route);
			} else {
				_CommonDao.update(_Route);
			}
		}
		_CommonDao.flush();
	}

	public void UpdatePreActCount(String ProdefID) {
		List<Wfd_Actdef> ActdefList = _SmProDef.ActdefList(ProdefID);
		for (int i = 0; i < ActdefList.size(); i++) {
			StringBuilder _str = new StringBuilder();
			_str.append("Prodef_Id='");
			_str.append(ProdefID);
			_str.append("' and Actdef_Id in (select Next_Actdef_Id from ");
			_str.append(Common.WORKFLOWDB);
			_str.append(" WFD_ROUTE where Prodef_Id='");
			_str.append(ProdefID);
			_str.append("' and Actdef_Id='");
			_str.append(ActdefList.get(i).getActdef_Id());
			_str.append("')");
			List<Wfd_Actdef> list = _CommonDao.getDataList(Wfd_Actdef.class,
					Common.WORKFLOWDB + "WFD_ACTDEF", _str.toString());
			Wfd_Actdef _Wfd_Actdef = ActdefList.get(i);
			_Wfd_Actdef.setPre_Actcount(list.size());
			_CommonDao.update(_Wfd_Actdef);
		}
		_CommonDao.flush();
	}

	public String GetXmlData(String ProdefID) {
		Wfd_Prodef _Prodef = _CommonDao.load(Wfd_Prodef.class, ProdefID);
		// 创建文档:使用了一个Helper类
		Document document = DocumentHelper.createDocument();
		if (_Prodef != null) {

			// SuperMap
			Element root = DocumentHelper.createElement("SuperMap");
			document.setRootElement(root);

			// 添加子节点:add之后就返回这个元素
			// Process
			Element ProcessElement = root.addElement("Process");

			Element ProcessIDElement = ProcessElement.addElement("Process_ID");
			ProcessIDElement.setText(_Prodef.getProdef_Id());

			Element ProcessNameElement = ProcessElement
					.addElement("Process_Name");
			ProcessNameElement.setText(_Prodef.getProdef_Name());

			// FlowGraphicsCollection
			Element FlowGraphicsCollectionElement = root
					.addElement("FlowGraphicsCollection");
			List<Wfd_Actdef> ActdefList = _SmProDef.ActdefList(ProdefID);
			for (int i = 0; i < ActdefList.size(); i++) {
				Wfd_Actdef _Actdef = ActdefList.get(i);

				Element FlowGraphicsElement = FlowGraphicsCollectionElement
						.addElement("FlowGraphics");

				Element ActivityIDElement = FlowGraphicsElement
						.addElement("Activity_ID");
				if (_Actdef.getActdef_Id() != null)
					ActivityIDElement.setText(_Actdef.getActdef_Id());

				Element ActivityNameElement = FlowGraphicsElement
						.addElement("Activity_Name");
				if (_Actdef.getActdef_Name() != null)
					ActivityNameElement.setText(_Actdef.getActdef_Name());

				Element PositionXElement = FlowGraphicsElement
						.addElement("Activity_PositionX");
				if (_Actdef.getActdef_Positionx() != null)
					PositionXElement.setText(_Actdef.getActdef_Positionx());

				Element PositionYElement = FlowGraphicsElement
						.addElement("Activity_PositionY");
				if (_Actdef.getActdef_Positiony() != null)
					PositionYElement.setText(_Actdef.getActdef_Positiony());

				Element ActivityTypeElement = FlowGraphicsElement
						.addElement("Activity_Type");
				if (_Actdef.getActdef_Type() != null)
					ActivityTypeElement.setText(_Actdef.getActdef_Type());

				Element UniqueIDElement = FlowGraphicsElement
						.addElement("UniqueID");
				if (_Actdef.getActdef_Id() != null)
					UniqueIDElement.setText(_Actdef.getActdef_Id());

			}

			// FlowGraphicsCollection
			Element FlowArrowCollectionElement = root
					.addElement("FlowArrowCollection");
			List<Wfd_Route> RouteList = _SmProDef.RouteList(ProdefID);
			for (int i = 0; i < RouteList.size(); i++) {
				Wfd_Route _Route = RouteList.get(i);
				Element FlowArrowElement = FlowArrowCollectionElement
						.addElement("FlowArrow");

				Element RouteIDElement = FlowArrowElement
						.addElement("Route_ID");
				if (_Route.getRoute_Id() != null)
					RouteIDElement.setText(_Route.getRoute_Id());

				Element RouteValueElement = FlowArrowElement
						.addElement("Route_Value");
				if (_Route.getRoute_Value() != null)
					RouteValueElement.setText(_Route.getRoute_Value());

				Element StartElement = FlowArrowElement
						.addElement("FlowGraphics_Start");
				if (_Route.getActdef_Id() != null)
					StartElement.setText(_Route.getActdef_Id());

				Element EndElement = FlowArrowElement
						.addElement("FlowGraphics_End");
				if (_Route.getNext_Actdef_Id() != null)
					EndElement.setText(_Route.getNext_Actdef_Id());

			}
		}
		return document.asXML();
	}

	public Document get_XDocument() {
		return _XDocument;
	}

	public void set_XDocument(Document _XDocument) {
		this._XDocument = _XDocument;
	}

	public Element get_RootXElement() {
		return _RootXElement;
	}

	public void set_RootXElement(Element _RootXElement) {
		this._RootXElement = _RootXElement;
	}

	public List<Element> get_FlowGraphicsList() {
		return _FlowGraphicsList;
	}

	public void set_FlowGraphicsList(List<Element> _FlowGraphicsList) {
		this._FlowGraphicsList = _FlowGraphicsList;
	}

	public List<Element> get_FlowArrowList() {
		return _FlowArrowList;
	}

	public void set_FlowArrowList(List<Element> _FlowArrowList) {
		this._FlowArrowList = _FlowArrowList;
	}
}
