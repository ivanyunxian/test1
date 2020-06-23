package com.supermap.realestate_gx.registration.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.springframework.web.multipart.MultipartFile;

public class myFileItem   implements FileItem {

	 private final byte[] imgContent;
	 private final String originalFilename;
     public myFileItem(byte[] imgContent,String originalFilename)
     {
        this.imgContent = imgContent;
        this.originalFilename = originalFilename;
         }
	@Override
	public FileItemHeaders getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setHeaders(FileItemHeaders headers) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isInMemory() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public byte[] get() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getString(String encoding)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void write(File file) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setFieldName(String name) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isFormField() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setFormField(boolean state) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
