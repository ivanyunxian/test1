package com.supermap.wisdombusiness.core;

import java.io.OutputStream;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;

public class SuperXStream extends XStream {
	private String version;

	private String ecoding;

	public SuperXStream() {
		this("1.0", "GBK");
	}

	public SuperXStream(String version, String ecoding) {
		this.version = version;
		this.ecoding = ecoding;
	}

	public String getDeclaration() {
		return "<?xml version=\"" + this.version + "\" encoding=\"" + this.ecoding + "\"?>";
	}

	@Override
	public void toXML(Object arg0, OutputStream arg1) {
		try {
			String dec = this.getDeclaration();
			byte[] bytesOfDec = dec.getBytes(this.ecoding);
			arg1.write(bytesOfDec);
			arg1.write("\r\n".getBytes());
		} catch (Exception e) {
			return;
		}
		super.toXML(arg0, arg1);
	}

	@Override
	public void toXML(Object arg0, Writer arg1) {
		try {
			arg1.write(getDeclaration());
		} catch (Exception e) {
			return;
		}
		super.toXML(arg0, arg1);
	}

}