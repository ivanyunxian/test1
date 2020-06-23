package com.supermap.realestate.registration.util.data;

public class DataColumn {
	private String label;
	private boolean readOnly;
	private DataTable table;
	private String columnName;
	private ObjectStorage dataStorage;

	public String getDataTypeName() {
		return this.dataStorage.getDataTypeName();
	}

	public void setDataTypeName(String arg0) {
		getDataStorage().setDataTypeName(arg0);
	}

	public int getDataType() {
		return getDataStorage().getDataType();
	}

	public void setDataType(int arg0) {
		getDataStorage().setDataType(arg0);
	}

	public DataColumn() {
		this("default1");
	}

	public DataColumn(int dataType) {
		this("default1", dataType);
	}

	public DataColumn(String columnName) {
		this(columnName, 0);
	}

	public DataColumn(String columnName, int dataType) {
		this.dataStorage = new ObjectStorage(dataType);
		this.columnName = columnName;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String caption) {
		this.label = caption;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getDefaultValue() {
		return this.dataStorage.getDefaultValue();
	}

	public void setDefaultValue(Object arg0) {
		this.dataStorage.setDefaultValue(arg0);
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public DataTable getTable() {
		return this.table;
	}

	public void setTable(DataTable table) {
		this.table = table;
	}

	void expandArray(int newLength) {
		this.dataStorage.expandArray(newLength);
	}

	void clearData() {
		clearData();
	}

	ObjectStorage getDataStorage() {
		return this.dataStorage;
	}

	void setObject(int arg0, Object arg1) throws DataException {
		this.dataStorage.setObject(arg0, arg1);
	}

	Object getObject(int arg0) throws DataException {
		return this.dataStorage.getObject(arg0);
	}

	void setString(int arg0, String arg1) throws DataException {
		this.dataStorage.setString(arg0, arg1);
	}

	String getString(int arg0) throws DataException {
		return this.dataStorage.getString(arg0);
	}

	boolean isNull(int arg0) {
		return this.dataStorage.isNull(arg0);
	}
}
