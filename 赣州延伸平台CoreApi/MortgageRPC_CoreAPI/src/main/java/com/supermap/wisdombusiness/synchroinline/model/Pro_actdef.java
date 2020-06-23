package com.supermap.wisdombusiness.synchroinline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 角色表 */
@Entity
@Table(name = "Pro_actdef", schema = "inline_inner")
public class Pro_actdef {
    @Column
    @Id
    protected String id;
    @Column
    protected String prodefid;
    @Column
    protected int shzt;
    @Column
    protected String roleid;
    @Column
    protected String prodefname;


    public String getProdefname() {
        return prodefname;
    }

    public void setProdefname(String prodefname) {
        this.prodefname = prodefname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdefid() {
        return prodefid;
    }

    public void setProdefid(String prodefid) {
        this.prodefid = prodefid;
    }

    public int getShzt() {
        return shzt;
    }

    public void setShzt(int shzt) {
        this.shzt = shzt;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

}
