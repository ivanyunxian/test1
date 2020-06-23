

		    /**
            * 
            * 代码生成器自动生成[WFD_BOXDEF]
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
            @Table(name = "WFD_BOXDEF",schema = "BDC_WORKFLOW")
            public class WFD_BOXDEF{

             private String Boxdef_Id;
             private String Boxdef_Pid;
             private String Box_Name;
             private String Lx;
             private String Box_Bm;
             private String Bz;
             private Integer Row_Num;
             private Integer Column_Num;
             private Integer Sort;
             private Integer Js;



              @Id              @Column(name = "BOXDEF_ID",length = 200)
                public  String  getBoxdef_Id() {
              if (Boxdef_Id == null) Boxdef_Id = UUID.randomUUID().toString().replace("-", "");                  return Boxdef_Id;
             }

                public void setBoxdef_Id( String Boxdef_Id) {
                  this.Boxdef_Id = Boxdef_Id;
             }

              @Column(name = "BOXDEF_PID",length = 200)
                public  String  getBoxdef_Pid() {
                  return Boxdef_Pid;
             }

                public void setBoxdef_Pid( String Boxdef_Pid) {
                  this.Boxdef_Pid = Boxdef_Pid;
             }

              @Column(name = "BOX_NAME",length = 200)
                public  String  getBox_Name() {
                  return Box_Name;
             }

                public void setBox_Name( String Box_Name) {
                  this.Box_Name = Box_Name;
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

              @Column(name = "BZ",length = 400)
                public  String  getBz() {
                  return Bz;
             }

                public void setBz( String Bz) {
                  this.Bz = Bz;
             }
                
              @Column(name = "ROW_NUM")
                public  Integer  getRow_Num() {
                  return Row_Num;
             }

                public void setRow_Num( Integer Row_Num) {
                  this.Row_Num = Row_Num;
             }
                
                @Column(name = "COLUMN_NUM")
                public  Integer  getColumn_Num() {
                  return Column_Num;
             }

                public void setColumn_Num( Integer Column_Num) {
                  this.Column_Num = Column_Num;
             }   
 
                @Column(name = "SORT")
                public  Integer  getSort() {
                  return Sort;
             }

                public void setSort( Integer Sort) {
                  this.Sort = Sort;
             }
                @Column(name = "JS")
				public Integer getJs() {
					return Js;
				}

				public void setJs(Integer js) {
					this.Js = js;
				} 

}
