package com.supermap.wisdombusiness.synchroinline.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@SuppressWarnings({"unchecked","rawtypes"})
public class ThreadLocal {
	private Map values = Collections.synchronizedMap(new HashMap());
	public Object get() {
		
		Thread currentThread = Thread.currentThread();
		Object result = values.get(currentThread);
		if (result == null && !values.containsKey(currentThread)) {
			result = initialValue();
			values.put(currentThread, result);
		}
		return result;
	}
	public void set(Object newValue) {
		values.put(Thread.currentThread(), newValue);
	}
	public Object initialValue() {
		return null;
	}
}
