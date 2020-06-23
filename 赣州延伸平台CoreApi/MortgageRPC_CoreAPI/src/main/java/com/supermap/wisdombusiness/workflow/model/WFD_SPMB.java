

		    /**
            * 
            * 代码生成器自动生成[WFD_SPMB]
            * 
            */

            package com.supermap.wisdombusiness.workflow.model;

            import java.util.UUID;
            import javax.persistence.Column;
            import javax.persistence.Entity;
            import javax.persistence.Id;
            import javax.persistence.Table;

            @Entity
            @Table(name = "WFD_SPMB",schema = "BDC_WORKFLOW")
            public class WFD_SPMB{

             private String Spmb_Id;
             private String Spcontent;
             private String Staff_Id;
             private String Dist_Id;
             private Integer Yxbz;
             private String Xh;



              @Id              @Column(name = "SPMB_ID",length = 200)
                public  String  getSpmb_Id() {
              if (Spmb_Id == null) Spmb_Id = UUID.randomUUID().toString().replace("-", "");                  return Spmb_Id;
             }

                public void setSpmb_Id( String Spmb_Id) {
                  this.Spmb_Id = Spmb_Id;
             }

              @Column(name = "SPCONTENT",length = 800)
                public  String  getSpcontent() {
                  return Spcontent;
             }

                public void setSpcontent( String Spcontent) {
                  this.Spcontent = Spcontent;
             }

              @Column(name = "STAFF_ID",length = 200)
                public  String  getStaff_Id() {
                  return Staff_Id;
             }

                public void setStaff_Id( String Staff_Id) {
                  this.Staff_Id = Staff_Id;
             }

              @Column(name = "DIST_ID",length = 200)
                public  String  getDist_Id() {
                  return Dist_Id;
             }

                public void setDist_Id( String Dist_Id) {
                  this.Dist_Id = Dist_Id;
             }

              @Column(name = "YXBZ")
                public  Integer  getYxbz() {
                  return Yxbz;
             }

                public void setYxbz( Integer Yxbz) {
                  this.Yxbz = Yxbz;
             }
                @Column(name = "XH")
                public  String  getXh() {
                  return Xh;
             }

                public void setXh( String Xh) {
                  this.Xh = Xh;
             }

}
