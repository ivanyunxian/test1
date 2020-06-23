package com.supermap.wisdombusiness.synchroinline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分页结果集
 * @author pukx
 */
public class PageResult<T> {
    
    /*
     * 总记录数
     */
    @JsonIgnore
    private int total=0;
    
    /*
     * 页索引
     */
    @JsonIgnore
    private int pageIndex=1;
    
    /*
     * 分页大小
     */
    @JsonIgnore
    private int pageSize=10;
    
    /*
     * 分页数据
     */
    @JsonIgnore
    private T data=null;
    
    /*
     * 页索引
     */
    @JsonProperty("pageIndex")
    public int getPageIndex()
    {
        return this.pageIndex;
    }
    
    /*
     * 分页大小
     */
    @JsonProperty("pageSize")
    public int getPageSize()
    {
        return this.pageSize;
    }
    
    /*
     * 总记录数
     */
    @JsonProperty("total")
    public int getTotal()
    {
        return this.total;
    }
    
    /*
     * 分页数据
     */
    @JsonProperty("data")
    public T getData()
    {
        return this.data;
    }
    
    public PageResult(int total,int page,int size,T data)
    {
        this.pageIndex=page;
        this.pageSize=size;
        this.data=data;
        this.total=total;
    }
}
