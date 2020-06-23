package com.supermap.wisdombusiness.workflow.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

public class BASE64DecodedMultipartFile implements MultipartFile {
	private  byte[] imgContent;
	private File file;
	
	public BASE64DecodedMultipartFile(byte[] buffer) {
		this.imgContent = buffer;
		
		
	}
	public void setByte(String str) {
		
		this.imgContent = null;
		
		
	}

	
	@Override
	public String getName() {
		// TODO - implementation depends on your requirements
		return null;
	}

	@Override
	public String getOriginalFilename() {
		// TODO - implementation depends on your requirements
		return null;
	}

	@Override
	public String getContentType() {
		// TODO - implementation depends on your requirements
		return null;
	}

	@Override
	public boolean isEmpty() {
		return imgContent != null && imgContent.length > 0;
	}

	@Override
	public long getSize() {
		return imgContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return imgContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(imgContent);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		  FileOutputStream out = new FileOutputStream(dest);
		  out.write(this.imgContent);
		  out.close();
	}


}
