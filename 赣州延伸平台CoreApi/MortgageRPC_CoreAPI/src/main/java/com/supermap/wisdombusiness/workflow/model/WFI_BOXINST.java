

		    /**
            * 
            * 代码生成器自动生成[WFI_BOXINST]
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
            @Table(name = "WFI_BOXINST",schema = "BDC_WORKFLOW")
            public class WFI_BOXINST{

             private String Boxinst_Id;
             private String Box_Name;
             private Date Rgsj;
             private String Content;
             private String Boxdef_Pid;
             private String Boxdef_Id;
             private String Staff_Name;
             private String Staff_Id;
             private String Bz;
             private String Lx;
             private String Box_Bm;
             private Integer Status;



              @Id              @Column(name = "BOXINST_ID",length = 200)
                public  String  getBoxinst_Id() {
              if (Boxinst_Id == null) Boxinst_Id = UUID.randomUUID().toString().replace("-", "");                  return Boxinst_Id;
             }

                public void setBoxinst_Id( String Boxinst_Id) {
                  this.Boxinst_Id = Boxinst_Id;
             }

              @Column(name = "BOX_NAME",length = 200)
                public  String  getBox_Name() {
                  return Box_Name;
             }

                public void setBox_Name( String Box_Name) {
                  this.Box_Name = Box_Name;
             }

              @Column(name = "RGSJ", length=7)
                public  Date  getRgsj() {
                  return Rgsj;
             }

                public void setRgsj( Date Rgsj) {
                  this.Rgsj = Rgsj;
             }

              @Column(name = "CONTENT",length = 1000)
                public  String  getContent() {
                  return Content;
             }

                public void setContent( String Content) {
                  this.Content = Content;
             }

              @Column(name = "BOXDEF_PID",length = 200)
                public  String  getBoxdef_Pid() {
                  return Boxdef_Pid;
             }

                public void setBoxdef_Pid( String Boxdef_Pid) {
                  this.Boxdef_Pid = Boxdef_Pid;
             }

              @Column(name = "BOXDEF_ID",length = 200)
                public  String  getBoxdef_Id() {
                  return Boxdef_Id;
             }

                public void setBoxdef_Id( String Boxdef_Id) {
                  this.Boxdef_Id = Boxdef_Id;
             }

              @Column(name = "STAFF_NAME",length = 100)
                public  String  getStaff_Name() {
                  return Staff_Name;
             }

                public void setStaff_Name( String Staff_Name) {
                  this.Staff_Name = Staff_Name;
             }

              @Column(name = "STAFF_ID",length = 200)
                public  String  getStaff_Id() {
                  return Staff_Id;
             }

                public void setStaff_Id( String Staff_Id) {
                  this.Staff_Id = Staff_Id;
             }

              @Column(name = "BZ",length = 400)
                public  String  getBz() {
                  return Bz;
             }

                public void setBz( String Bz) {
                  this.Bz = Bz;
             }

              @Column(name = "LX",length = 100)
                public  String  getLx() {
                  return Lx;
             }

                public void setLx( String Lx) {
                  this.Lx = Lx;
             }

              @Column(name = "BOX_BM",length = 200)
                public  String  getBox_Bm() {
                  return Box_Bm;
             }

                public void setBox_Bm( String Box_Bm) {
                  this.Box_Bm = Box_Bm;
             }

              @Column(name = "STATUS")
                public  Integer  getStatus() {
                  return Status;
             }

                public void setStatus( Integer Status) {
                  this.Status = Status;
             }

}
