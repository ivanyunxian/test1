package com.supermap.realestate_gx.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 附件资料表
 */
@Entity
@Table(name = "DYYWFJZL", schema = "GLSZFGJJ")
public class Dyywfjzl {

	@Id
	@Column(name = "ID", length = 64)
	private String id;
	private String gjjywlsh;
	private String name;
	private String dnlj;
	private String jbjg;
	private Date scsj;
	private Date dysj;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGjjywlsh() {
		return gjjywlsh;
	}
	public void setGjjywlsh(String gjjywlsh) {
		this.gjjywlsh = gjjywlsh;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDnlj() {
		return dnlj;
	}
	public void setDnlj(String dnlj) {
		this.dnlj = dnlj;
	}
	public String getJbjg() {
		return jbjg;
	}
	public void setJbjg(String jbjg) {
		this.jbjg = jbjg;
	}
	public Date getScsj() {
		return scsj;
	}
	public void setScsj(Date scsj) {
		this.scsj = scsj;
	}
	public Date getDysj() {
		return dysj;
	}
	public void setDysj(Date dysj) {
		this.dysj = dysj;
	}
}
