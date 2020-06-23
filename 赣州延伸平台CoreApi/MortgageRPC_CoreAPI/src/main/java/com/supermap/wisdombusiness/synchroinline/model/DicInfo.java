package com.supermap.wisdombusiness.synchroinline.model;

import java.util.ArrayList;
import java.util.List;

public class DicInfo
{
	private String key;

	private String name;

	private List<DicItem> items = new ArrayList<DicInfo.DicItem>();

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 通过字典值获取对应的名称
	 * 
	 * @param code
	 * @return
	 */
	public String getNameByCode(String code)
	{
		String text = "";
		if (code != null)
		{
			for (DicItem item : this.items)
			{
				if (code.equals(item.code))
				{
					text = item.text;
					break;
				}
			}
		}
		return text == null || text.isEmpty() ? code : text;
	}

	/**
	 * @return the items
	 */
	public List<DicItem> getItems()
	{
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	private void setItems(List<DicItem> items)
	{
		this.items = items;
	}

	/**
	 * 字典项
	 * 
	 * @author pukx
	 *
	 */
	public static class DicItem
	{

		private String code;
		private String text;

		/**
		 * @return the code
		 */
		public String getCode()
		{
			return code;
		}

		/**
		 * @param code
		 *            the code to set
		 */
		public void setCode(String code)
		{
			this.code = code;
		}

		/**
		 * @return the text
		 */
		public String getText()
		{
			return text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setText(String text)
		{
			this.text = text;
		}

	}
}
