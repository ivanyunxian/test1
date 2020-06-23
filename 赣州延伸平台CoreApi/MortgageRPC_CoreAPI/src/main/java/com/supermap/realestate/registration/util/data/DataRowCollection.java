package com.supermap.realestate.registration.util.data;


public class DataRowCollection {
	private DataTable table;

	DataRowCollection(DataTable table) {
		this.table = table;
	}

	public int size() {
		return this.table.getEntityRows().size();
	}

	public void clear() {
		this.table.getEntityRows().clear();
	}

	@SuppressWarnings("unchecked")
	public void add(int arg0, DataRow row) throws Exception {
		if (row.getTable().equals(this.table)) {
			this.table.getEntityRows().add(arg0, row);
		} else {
			throw new Exception("该Row不是由table生成的!");
		}
	}

	@SuppressWarnings("unchecked")
	public void add(DataRow row) throws Exception {
		if (row.getTable().equals(this.table)) {
			this.table.getEntityRows().add(row);
		} else {
			throw new Exception("该Row不是由table生成的!");
		}
	}

	public void remove(DataRow row) {
		this.table.getEntityRows().remove(row);
	}

	public void remove(int arg0) {
		this.table.getEntityRows().remove(arg0);
	}

	public DataRow get(int rowIndex) {
		return (DataRow) this.table.getEntityRows().get(rowIndex);
	}

	public DataRow addNew() throws Exception {
		DataRow tempRow = this.table.newRow();
		add(tempRow);
		return tempRow;
	}
}
