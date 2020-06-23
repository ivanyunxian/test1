
/**
 * 
 * 代码生成器自动生成[BDCS_COMPACT_H]
 * 
 */

package com.supermap.luzhouothers.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDCS_COMPACT_H", schema = "BDCK")
public class BDCS_COMPAC_H {

	private String Rowno;
	private Integer Relationid;
	private Integer Id;
	private String Zl;
	private Integer Buildarea;
	private Integer Usearea;
	private Integer Publicarea;
	private Short Flooron;
	private Short Flooronend;
	private String Type;
	private String Structure;
	private String Designusage;
	private String Usage;
	private Integer Buildid;
	private Short Isvalid;

	@Id
	@Column(name = "ROWNO", length = 32)
	public String getRowno() {
		if (Rowno == null)
			Rowno = UUID.randomUUID().toString().replace("-", "");
		return Rowno;
	}

	public void setRowno(String Rowno) {
		this.Rowno = Rowno;
	}

	@Column(name = "RELATIONID")
	public Integer getRelationid() {
		return Relationid;
	}

	public void setRelationid(Integer Relationid) {
		this.Relationid = Relationid;
	}

	@Column(name = "ID")
	public Integer getId() {
		return Id;
	}

	public void setId(Integer Id) {
		this.Id = Id;
	}

	@Column(name = "ZL", length = 648)
	public String getZl() {
		return Zl;
	}

	public void setZl(String Zl) {
		this.Zl = Zl;
	}

	@Column(name = "BUILDAREA")
	public Integer getBuildarea() {
		return Buildarea;
	}

	public void setBuildarea(Integer Buildarea) {
		this.Buildarea = Buildarea;
	}

	@Column(name = "USEAREA")
	public Integer getUsearea() {
		return Usearea;
	}

	public void setUsearea(Integer Usearea) {
		this.Usearea = Usearea;
	}

	@Column(name = "PUBLICAREA")
	public Integer getPublicarea() {
		return Publicarea;
	}

	public void setPublicarea(Integer Publicarea) {
		this.Publicarea = Publicarea;
	}

	@Column(name = "FLOORON")
	public Short getFlooron() {
		return Flooron;
	}

	public void setFlooron(Short Flooron) {
		this.Flooron = Flooron;
	}

	@Column(name = "FLOORONEND")
	public Short getFlooronend() {
		return Flooronend;
	}

	public void setFlooronend(Short Flooronend) {
		this.Flooronend = Flooronend;
	}

	@Column(name = "TYPE", length = 20)
	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	@Column(name = "STRUCTURE", length = 30)
	public String getStructure() {
		return Structure;
	}

	public void setStructure(String Structure) {
		this.Structure = Structure;
	}

	@Column(name = "DESIGNUSAGE", length = 40)
	public String getDesignusage() {
		return Designusage;
	}

	public void setDesignusage(String Designusage) {
		this.Designusage = Designusage;
	}

	@Column(name = "USAGE", length = 40)
	public String getUsage() {
		return Usage;
	}

	public void setUsage(String Usage) {
		this.Usage = Usage;
	}

	@Column(name = "BUILDID")
	public Integer getBuildid() {
		return Buildid;
	}

	public void setBuildid(Integer Buildid) {
		this.Buildid = Buildid;
	}

	@Column(name = "ISVALID")
	public Short getIsvalid() {
		return Isvalid;
	}

	public void setIsvalid(Short Isvalid) {
		this.Isvalid = Isvalid;
	}

}
