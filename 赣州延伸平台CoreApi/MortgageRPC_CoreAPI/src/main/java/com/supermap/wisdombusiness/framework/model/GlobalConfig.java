package com.supermap.wisdombusiness.framework.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author JHX
 * 
 */
@Entity
@Table(name = "T_GLOBALCONFIG" )
public class GlobalConfig {
	
	private String id;
	private String configContent;
	private String version;
	
	private String tenantId;
	
	
	@Id
	@Column(name = "ID", length = 32)
	public String getId() {
		if (id == null) id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "CONTENT_CONFIG")
	public String getConfigContent() {
		return configContent;
	}
	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}
	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "TENANT_ID")
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	

}
