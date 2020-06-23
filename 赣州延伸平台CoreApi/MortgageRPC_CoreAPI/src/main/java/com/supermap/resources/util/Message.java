package com.supermap.resources.util;

import java.util.List;


public class Message {

		private long total;

		private List<?> rows;

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		public List<?> getRows() {
			return rows;
		}

		public void setRows(List<?> rows) {
			this.rows = rows;
		}
		
		private String success;
		private String msg;

		public String getSuccess() {
			return success;
		}

		public void setSuccess(String success) {
			this.success = success;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	
}
