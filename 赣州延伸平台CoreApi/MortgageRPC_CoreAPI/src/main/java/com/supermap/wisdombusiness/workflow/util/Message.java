package com.supermap.wisdombusiness.workflow.util;

import java.util.List;

public class Message {
	
	private long total;  
    
    private List<?> rows; 
    
    private String desc;
    
    private List<?> footer;
  
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}  

	public List<?> getFooter() {
		return footer;
	}

	public void setFooter(List<?> footer) {
		this.footer = footer;
	}
}
