package com.supermap.realestate.registration.model.xmlExportmodel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import com.supermap.realestate.registration.util.StringHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
public class PropertyInfo {
	@XStreamAsAttribute
	@XStreamAlias("name")
	private String name = "";

	public String getname() {
		return name;
	}

	public void setname(String _Value) {
		name = _Value;
	}

	@XStreamAlias("value")
	private String value = "";

	public String getvalue() {
		return value;
	}

	public void setvalue(Object _Value) {
		if (_Value != null) {
			if (_Value instanceof Date) {
				value = StringHelper.FormatByDatetime(_Value);
			} else if((_Value instanceof Double)||(_Value instanceof Float)){
				DecimalFormat df = new DecimalFormat("####.####");//最多保留几位小数，就用几个#，最少位就用0来确定
				df.setRoundingMode(RoundingMode.HALF_UP);
				value=df.format(_Value);
			} else {
				value=StringHelper.formatObject(_Value);
			} 
		}
	}

	public PropertyInfo(String _name) {
		name = _name;
	}
}
