package com.supermap.realestate.registration.util.data;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public final class DataTable {
	public static boolean DEFAULT_READONLY = false;
	public static String DEFAULT_GETSTRING_NULL = "";
	public static int DEFAULT_GETINT_NULL = 0;
	public static Date DEFAULT_GETDATETIME_NULL = null;
	private DataColumnCollection columns;
	private DataRowCollection rows;
	private String tableName;
	private boolean readOnly = false;
	@SuppressWarnings("rawtypes")
	private List entityRows;
	@SuppressWarnings({ "rawtypes", "unused" })
	private List deleteRows;
	private int maxIndex = -1;

	@SuppressWarnings("rawtypes")
	public DataTable() {
		this.columns = new DataColumnCollection(this);
		this.rows = new DataRowCollection(this);
		this.entityRows = new Vector();
		this.readOnly = DEFAULT_READONLY;
	}

	@SuppressWarnings("rawtypes")
	List getEntityRows() {
		return this.entityRows;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@SuppressWarnings("rawtypes")
	public void setEntityRows(List rows) {
		this.entityRows = rows;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DataRowCollection getRows() {
		return this.rows;
	}

	public void setRows(DataRowCollection rows) {
		this.rows = rows;
	}

	public DataColumnCollection getColumns() {
		return this.columns;
	}

	public void setColumns(DataColumnCollection columns) {
		this.columns = columns;
	}

	int getNewIndex() {
		this.maxIndex += 1;
		this.columns.expandArray(this.maxIndex);
		return this.maxIndex;
	}

	public DataRow newRow() {
		DataRow tempRow = new DataRow(this, getNewIndex());
		return tempRow;
	}

	public String asXmlText() {
		try {
			Document xmlTableDoc = DocumentHelper.createDocument();
			xmlTableDoc.setXMLEncoding("gb2312");
			Element tableElement = xmlTableDoc.addElement("table");
			tableElement.addAttribute("name", getTableName());
			tableElement.addAttribute("readonly", String.valueOf(isReadOnly()));
			Element columensElement = tableElement.addElement("columns");
			for (int i = 0; i < getColumns().size(); i++) {
				Element columenElement = columensElement.addElement("column");
				columenElement.addAttribute("name", getColumns().get(i)
						.getColumnName());
				columenElement.addAttribute("caption", getColumns().get(i)
						.getLabel());
				columenElement.addAttribute("type",
						String.valueOf(getColumns().get(i).getDataType()));
				columenElement.addAttribute("typename", getColumns().get(i)
						.getDataTypeName());
			}
			Element rowsElement = tableElement.addElement("rows");
			for (int i = 0; i < getRows().size(); i++) {
				Element rowElement = rowsElement.addElement("row");
				for (int j = 0; j < getColumns().size(); j++) {
					String key = getColumns().get(j).getColumnName();
					String val = getRows().get(i).getString(j);
					rowElement.addAttribute(key, val);
				}
			}
			return xmlTableDoc.asXML();
		} catch (Exception e) {
			System.out.print("DataTable生成XML错误:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
