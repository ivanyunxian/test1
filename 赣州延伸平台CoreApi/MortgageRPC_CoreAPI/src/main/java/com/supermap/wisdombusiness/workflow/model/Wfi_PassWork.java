

		    /**
            * 
            * 代码生成器自动生成[WFI_PASSWORK]
            * 
            */

            package com.supermap.wisdombusiness.workflow.model;

            import java.util.UUID;
            import java.util.Date;
            import javax.persistence.Column;
            import javax.persistence.Entity;
            import javax.persistence.Id;
            import javax.persistence.Table;

            @Entity
            @Table(name = "WFI_PASSWORK",schema = "BDC_WORKFLOW")
            public class Wfi_PassWork{

             private String Passwork_Id;
             private String Staff_Id;
             private String Tostaff_Id;
             private String Staff_Name;
             private String Tostaff_Name;
             private Date Passtime;
             private Date Pass_Start;
             private Date Pass_End;
             private String Actdefnames;
             private String Actdefnids;
             private Integer Status;
         	 private String Prodef_Ids;
        	 private Integer Isallprower;
 			private String Role_Ids;
 			

              @Id              @Column(name = "PASSWORK_ID",length = 400)
                public  String  getPasswork_Id() {
              if (Passwork_Id == null) Passwork_Id = UUID.randomUUID().toString().replace("-", "");                  return Passwork_Id;
             }

                public void setPasswork_Id( String Passwork_Id) {
                  this.Passwork_Id = Passwork_Id;
             }

              @Column(name = "STAFF_ID",length = 400)
                public  String  getStaff_Id() {
                  return Staff_Id;
             }

                public void setStaff_Id( String Staff_Id) {
                  this.Staff_Id = Staff_Id;
             }

              @Column(name = "TOSTAFF_ID",length = 400)
                public  String  getTostaff_Id() {
                  return Tostaff_Id;
             }

                public void setTostaff_Id( String Tostaff_Id) {
                  this.Tostaff_Id = Tostaff_Id;
             }

              @Column(name = "STAFF_NAME",length = 200)
                public  String  getStaff_Name() {
                  return Staff_Name;
             }

                public void setStaff_Name( String Staff_Name) {
                  this.Staff_Name = Staff_Name;
             }

              @Column(name = "TOSTAFF_NAME",length = 200)
                public  String  getTostaff_Name() {
                  return Tostaff_Name;
             }

                public void setTostaff_Name( String Tostaff_Name) {
                  this.Tostaff_Name = Tostaff_Name;
             }

              @Column(name = "PASSTIME", length=7)
                public  Date  getPasstime() {
                  return Passtime;
             }

                public void setPasstime( Date Passtime) {
                  this.Passtime = Passtime;
             }

              @Column(name = "PASS_START", length=7)
                public  Date  getPass_Start() {
                  return Pass_Start;
             }

                public void setPass_Start( Date Pass_Start) {
                  this.Pass_Start = Pass_Start;
             }

              @Column(name = "PASS_END", length=7)
                public  Date  getPass_End() {
                  return Pass_End;
             }

                public void setPass_End( Date Pass_End) {
                  this.Pass_End = Pass_End;
             }

              @Column(name = "ACTDEFNAMES",length = 1000)
                public  String  getActdefnames() {
                  return Actdefnames;
             }

                public void setActdefnames( String Actdefnames) {
                  this.Actdefnames = Actdefnames;
             }

              @Column(name = "ACTDEFNIDS",length = 2000)
                public  String  getActdefnids() {
                  return Actdefnids;
             }

                public void setActdefnids( String Actdefnids) {
                  this.Actdefnids = Actdefnids;
             }

              @Column(name = "STATUS")
                public  Integer  getStatus() {
                  return Status;
             }

                public void setStatus( Integer Status) {
                  this.Status = Status;
             }
     			@Column(name = "PRODEF_IDS",length = 400)
           	 public String getProdef_Ids() {
   				return Prodef_Ids;
   			}

   			public void setProdef_Ids(String prodef_Ids) {
   				Prodef_Ids = prodef_Ids;
   			}
   			@Column(name = "ISALLPROWER",length = 400)
   			public Integer getIsallprower() {
   				return Isallprower;
   			}

   			public void setIsallprower(Integer isallprower) {
   				Isallprower = isallprower;
   			}
   			@Column(name = "ROLE_IDS",length = 400)
   			public String getRole_Ids() {
   				return Role_Ids;
   			}

   			public void setRole_Ids(String role_Ids) {
   				Role_Ids = role_Ids;
   			}

}
