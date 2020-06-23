package com.supermap.wisdombusiness.synchroinline.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;

public class InlineFileItem extends DiskFileItem
{

	private File file;

	public InlineFileItem(String fileName, File repository)
	{
		super("file", "image/jpeg", false, fileName, 10240, repository);
		file = repository;
	}

	@Override
	public long getSize()
	{
		return this.file.length();
	}

	@Override
	public byte[] get()
	{
		byte[] bytes = new byte[(int) this.file.length()];
		try
		{
			FileInputStream fis = new FileInputStream(this.file);
			fis.read(bytes, 0, bytes.length);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return bytes;
	}

	@Override
	protected File getTempFile()
	{
		return this.file;
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return super.getOutputStream();
	}

}
