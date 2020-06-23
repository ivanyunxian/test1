package org.jeecg.modules.mortgagerpc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zrz;


public class BdcZrzTreeModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 对应BdcZrz中的id字段,前端数据树中的key*/
    private String key;

    /** 对应BdcZrz中的id字段,前端数据树中的value*/
    private String value;

    /** 对应zl字段,前端数据树中的title*/
    private String title;


    private boolean isLeaf;
    
	private String id;

	private String enterpriseid;

	private String bdcdyid;

	private String bdcdyh;

	private String zl;

	private String zrzh;

	private String operator;

	private Date createtime;
	
	private String zdid;
	
	private List<BdcZrzTreeModel> children = new ArrayList<>();
	
	public BdcZrzTreeModel(Bdc_zrz bdczrz) {
		this.key = bdczrz.getId();
        this.value = bdczrz.getId();
        this.title = bdczrz.getZl();
        this.id = bdczrz.getId();
        this.enterpriseid = bdczrz.getEnterpriseid();
        this.bdcdyid = bdczrz.getBdcdyid();
        this.bdcdyh = bdczrz.getBdcdyh();
        this.zl = bdczrz.getZl();
        this.zrzh = bdczrz.getZrzh();
        this.operator = bdczrz.getOperator();
        this.createtime = bdczrz.getCreatetime();
        this.zdid = bdczrz.getZdid();
    }
	
	public BdcZrzTreeModel(Bdc_shyqzd bdcsyqzd) {
		this.key = bdcsyqzd.getId();
        this.value = bdcsyqzd.getId();
        this.title = bdcsyqzd.getZl();
        this.id = bdcsyqzd.getId();
        this.enterpriseid = bdcsyqzd.getEnterpriseid();
        this.bdcdyh = bdcsyqzd.getBdcdyh();
        this.zl = bdcsyqzd.getZl();
        this.operator = bdcsyqzd.getOperator();
        this.createtime = bdcsyqzd.getCreatetime();
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnterpriseid() {
		return enterpriseid;
	}

	public void setEnterpriseid(String enterpriseid) {
		this.enterpriseid = enterpriseid;
	}

	public String getBdcdyid() {
		return bdcdyid;
	}

	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}

	public String getBdcdyh() {
		return bdcdyh;
	}

	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}

	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getZrzh() {
		return zrzh;
	}

	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public List<BdcZrzTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<BdcZrzTreeModel> children) {
		if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
	}
	
	public String getZdid() {
		return zdid;
	}

	public void setZdid(String zdid) {
		this.zdid = zdid;
	}

	public BdcZrzTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        BdcZrzTreeModel model = (BdcZrzTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(enterpriseid, model.enterpriseid) &&
                Objects.equals(bdcdyid, model.bdcdyid) &&
                Objects.equals(bdcdyh, model.bdcdyh) &&
                Objects.equals(zl, model.zl) &&
                Objects.equals(zrzh, model.zrzh) &&
                Objects.equals(operator, model.operator) &&
                Objects.equals(zdid, model.zdid);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, enterpriseid, bdcdyid, bdcdyh, zl,
        		zrzh, operator,zdid);
    }
	
}
