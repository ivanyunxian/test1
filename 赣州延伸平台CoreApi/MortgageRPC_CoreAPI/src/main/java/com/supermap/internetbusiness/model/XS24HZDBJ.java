package com.supermap.internetbusiness.model;

// Generated 2016-3-28 10:42:41 by Hibernate Tools 4.0.0

import com.supermap.internetbusiness.model.genrt.GenerateXS24HZDBJ;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Da generated by hbm2java
 */
@Entity
@Table(name = "XS24HZDBJ", schema = "BDCK")
public class XS24HZDBJ extends GenerateXS24HZDBJ {

	@Override
	@Id
	@Column(name = "bsm", length = 50)
	public String getId() {
		return super.getId();
	}
	
    @Override
	@Column(name = "zwlsh")
	public String getZWLSH() {
		return super.getZWLSH();
	}
    
    @Override
	@Column(name = "djlsh")
	public String getDJLSH() {
		return super.getDJLSH();
	}
    
    @Override
    @Column(name = "dqsj")
    public Date getDQSJ() {
    	return super.getDQSJ();
    }
    
    @Override
    @Column(name = "sfdb")
    public String getSFDB() {
    	return super.getSFDB();
    }
    
    @Override
	@Column(name = "dqzt")
	public String getDQZT() {
		return super.getDQZT();
	}
    
    @Override
	@Column(name = "dqcs")
	public Integer getDQCS() {
		return super.getDQCS();
	}
    
    @Override
    @Column(name = "scdqsj")
    public Date getSCDQSJ() {
    	return super.getSCDQSJ();
    }
    
    @Override
    @Column(name = "dqsbyy")
    public String getDQSBYY() {
    	return super.getDQSBYY();
    }
    
    @Override
   	@Column(name = "sfxxbl")
   	public String getSFXXBL() {
   		return super.getSFXXBL();
   	}
    
    @Override
   	@Column(name = "scyy")
   	public String getSCYY() {
   		return super.getSCYY();
   	}
    
    @Override
    @Column(name = "djcode")
    public String getDJCODE() {
    	return super.getDJCODE();
    }
    
    @Override
    @Column(name = "errorlog")
    public String getERRORLOG() {
    	return super.getERRORLOG();
    }
    @Override
    @Column(name = "errorcode")
    public String getErrorCode() {
    	return super.getErrorCode();
    }
}
