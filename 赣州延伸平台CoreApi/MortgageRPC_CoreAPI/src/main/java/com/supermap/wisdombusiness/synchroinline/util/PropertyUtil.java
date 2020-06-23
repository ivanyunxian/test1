package com.supermap.wisdombusiness.synchroinline.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	public Properties p = null;
	public Properties getConfigProperties(){
		if(this.p==null){
			p = new Properties();
			try {
				InputStream in = PropertyUtil.class
						.getResourceAsStream("/db_inline.properties");
				p.clear();
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
}
