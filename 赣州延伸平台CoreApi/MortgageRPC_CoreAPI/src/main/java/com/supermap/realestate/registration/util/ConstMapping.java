package com.supermap.realestate.registration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ConstMapping {

	private static Map<String,List<Map>> ConstMap=null;
	public ConstMapping() {

	}
	@SuppressWarnings("rawtypes")
	public static Map GetConstMap(String lx, String value) throws IOException {
		
		Map Result=null;
		if(ConstMap==null){
			ConstMap=Serialize();
		}
		
		if(ConstMap!=null&&lx!=null&&!lx.equals("")){
			List<Map> maps=ConstMap.get(lx);
			if(maps!=null&&maps.size()>0){
				boolean success=false;
				for(Map m:maps){
					if(value.equals(m.get("srcDM").toString())){
						Result= m;
						success=true;
						break;
					}
				}
				if(!success){
					
				}
			}
		}
		return Result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<String,List<Map>> Serialize() {
		try {
			String str = ReadFile().substring(1);
			String str1 = "{FWYT:[{DM:'10',MC:'123'}]}";
			Map map = new HashMap();
			if (str != null && !str.equals("")) {
				JSONObject json = JSONObject.fromObject(str);
				if (json != null) {
					Iterator it = json.keys();

					while (it.hasNext()) {
						List<Map> cmap = new ArrayList<Map>();
						Object key = it.next();
						if (key != null && !key.equals("")) {
							JSONArray array = json.getJSONArray(key.toString());
							if (array != null && array.size() > 0) {

								for (int i = 0, len = array.size(); i < len; i++) {
									JSONObject child = array.getJSONObject(i);
									Iterator cit = child.keys();
									Map data = new HashMap();
									while (cit.hasNext()) {
										Object ckey = cit.next();
										data.put(ckey, child.get(ckey));
									}
									cmap.add(data);
								}
							}
							map.put(key, cmap);
						}
					}

				}
			}
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private static String ReadFile() throws IOException {
		StringBuilder Result =new StringBuilder();
		InputStream in = ConstMapping.class.getClassLoader().getResourceAsStream("ConstMapping.json");
		if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));// 构造一个BufferedReader类来读取文件
			String s = null;
			
			while ((s = br.readLine()) != null && !s.trim().equals("")) {// 使用readLine方法，一次读一行
				Result.append(s.trim());
			}
			br.close();
		}
		return Result.toString();
	}

}
