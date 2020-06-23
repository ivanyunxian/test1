package com.supermap.realestate.registration.util.data;

public class DataTypes {
	public static final int DATATABLE_OBJECT = 0;
	public static final int DATATABLE_STRING = 1;
	public static final int DATATABLE_BOOLEAN = 2;
	public static final int DATATABLE_SHORT = 3;
	public static final int DATATABLE_INT = 4;
	public static final int DATATABLE_LONG = 5;
	public static final int DATATABLE_FLOAT = 6;
	public static final int DATATABLE_DOUBLE = 7;
	public static final int DATATABLE_DATE = 8;
	public static final int DATATABLE_TIME = 9;
	public static final int DATATABLE_TIMESTAMP = 10;
	public static final int DATATABLE_BYTE = 11;
	public static final int DATATABLE_BYTES = 12;
	public static final int DATATABLE_BIGDECIMAL = 13;
	public static final String[] DATATABLE_TYPENAMES = { "OBJECT", "STRING",
			"BOOLEAN", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "DATE",
			"TIME", "TIMESTAMPLE", "BYTE", "BYTES", "BIGDECIMAL" };

	public static boolean checkDataType(int dataType) {
		return (dataType > -1) && (dataType < 14);
	}

	public static String getDataTypeName(int dataType) {
		if (checkDataType(dataType)) {
			return DATATABLE_TYPENAMES[dataType];
		}
		return null;
	}
}
