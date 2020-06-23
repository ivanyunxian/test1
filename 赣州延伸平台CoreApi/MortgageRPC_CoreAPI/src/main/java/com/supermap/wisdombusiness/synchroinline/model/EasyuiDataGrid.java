/*
 * Easyui DataGrid 数据结构
 * and open the template in the editor.
 */
package com.supermap.wisdombusiness.synchroinline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *Easyui DataGrid 数据结构
 * @author pukx
 */
public class EasyuiDataGrid {
    /*
     * 总记录数
     */
    @JsonIgnore
    private int total=0;
    
    /*
     * 数据行
     */
    @JsonIgnore
    private List<?> rows=null;
    
    /*
     * 总记录数
     */
    @JsonProperty("total")
    public int getTotal()
    {
        return this.total;
    }
    
    /*
     * 数据行
     */
    @JsonProperty("rows")
    public List<?> getRows()
    {
        return this.rows;
    }
    
    public EasyuiDataGrid(int total,List<?> rows)
    {
        this.total=total;
        this.rows=rows;
    }
}
