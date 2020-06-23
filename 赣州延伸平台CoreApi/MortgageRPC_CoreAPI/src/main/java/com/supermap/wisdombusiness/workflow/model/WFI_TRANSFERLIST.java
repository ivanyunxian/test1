

		    /**
            * 
            * 代码生成器自动生成[WFI_TRANSFERLIST]
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
            @Table(name = "WFI_TRANSFERLIST",schema = "BDC_WORKFLOW")
            public class WFI_TRANSFERLIST{

             private String Transferlistid;
             private Date Receive_Time;
             private String Receive_Staffid;
             private String Receive_Staffname;
             private Integer Receive_Status;
             private String Transferid;
             private String File_Number;
             private String Project_Name;
             private String Fromactinst_Name;
             private String Toactinst_Name;
             private String Dah;
             private String Lsh;
             private Date Create_Time;
             private Integer SFZYZl;
             
             private String AJID;
             private String ZL;
             private String QZH;
             private String SQR;
             
             private String ACTINST_ID;
             
             private Integer Success;
             
             private String OLDDAH;
             
             private String CB;



              @Id              @Column(name = "TRANSFERLISTID",length = 200)
                public  String  getTransferlistid() {
              if (Transferlistid == null) Transferlistid = UUID.randomUUID().toString().replace("-", "");                  return Transferlistid;
             }

                public void setTransferlistid( String Transferlistid) {
                  this.Transferlistid = Transferlistid;
             }

              @Column(name = "RECEIVE_TIME", length=7)
                public  Date  getReceive_Time() {
                  return Receive_Time;
             }

                public void setReceive_Time( Date Receive_Time) {
                  this.Receive_Time = Receive_Time;
             }

              @Column(name = "RECEIVE_STAFFID",length = 200)
                public  String  getReceive_Staffid() {
                  return Receive_Staffid;
             }

                public void setReceive_Staffid( String Receive_Staffid) {
                  this.Receive_Staffid = Receive_Staffid;
             }

              @Column(name = "RECEIVE_STAFFNAME",length = 200)
                public  String  getReceive_Staffname() {
                  return Receive_Staffname;
             }

                public void setReceive_Staffname( String Receive_Staffname) {
                  this.Receive_Staffname = Receive_Staffname;
             }

              @Column(name = "RECEIVE_STATUS")
                public  Integer  getReceive_Status() {
                  return Receive_Status;
             }

                public void setReceive_Status( Integer Receive_Status) {
                  this.Receive_Status = Receive_Status;
             }

              @Column(name = "TRANSFERID",length = 200)
                public  String  getTransferid() {
                  return Transferid;
             }

                public void setTransferid( String Transferid) {
                  this.Transferid = Transferid;
             }

              @Column(name = "FILE_NUMBER",length = 200)
                public  String  getFile_Number() {
                  return File_Number;
             }

                public void setFile_Number( String File_Number) {
                  this.File_Number = File_Number;
             }

              @Column(name = "PROJECT_NAME",length = 600)
                public  String  getProject_Name() {
                  return Project_Name;
             }

                public void setProject_Name( String Project_Name) {
                  this.Project_Name = Project_Name;
             }

              @Column(name = "FROMACTINST_NAME",length = 200)
                public  String  getFromactinst_Name() {
                  return Fromactinst_Name;
             }

                public void setFromactinst_Name( String Fromactinst_Name) {
                  this.Fromactinst_Name = Fromactinst_Name;
             }

              @Column(name = "TOACTINST_NAME",length = 200)
                public  String  getToactinst_Name() {
                  return Toactinst_Name;
             }

                public void setToactinst_Name( String Toactinst_Name) {
                  this.Toactinst_Name = Toactinst_Name;
             }
                @Column(name = "DAH",length = 200)
				public String getDah() {
					return Dah;
				}

				public void setDah(String dah) {
					Dah = dah;
				}
				@Column(name = "LSH",length = 200)
				public String getLsh() {
					return Lsh;
				}

				public void setLsh(String lsh) {
					Lsh = lsh;
				}
				 @Column(name = "CREATE_TIME", length=7)
				public Date getCreate_Time() {
					return Create_Time;
				}
				public void setCreate_Time(Date create_Time) {
					Create_Time = create_Time;
				}
				 @Column(name = "SFZYZl")
				public Integer getSFZYZl() {
					return SFZYZl;
				}

				public void setSFZYZl(Integer sFZYZl) {
					SFZYZl = sFZYZl;
				}
				@Column(name = "AJID",length = 100)
				public String getAJID() {
					return AJID;
				}

				public void setAJID(String aJID) {
					AJID = aJID;
				}
				@Column(name = "ZL",length = 1000)
				public String getZL() {
					return ZL;
				}

				public void setZL(String zL) {
					ZL = zL;
				}
				@Column(name = "QZH",length = 500)
				public String getQZH() {
					return QZH;
				}

				public void setQZH(String qZH) {
					QZH = qZH;
				}
				@Column(name = "SQR",length = 100)
				public String getSQR() {
					return SQR;
				}

				public void setSQR(String sQR) {
					SQR = sQR;
				}
				@Column(name = "ACTINST_ID",length = 100)
				public String getACTINST_ID() {
					return ACTINST_ID;
				}

				public void setACTINST_ID(String aCTINST_ID) {
					ACTINST_ID = aCTINST_ID;
				}
				@Column(name = "SUCCESS")
				public Integer getSuccess() {
					return Success;
				}

				public void setSuccess(Integer success) {
					Success = success;
				}
				@Column(name = "OLDDAH",length = 200)
				public String getOLDDAH() {
					return OLDDAH;
				}

				public void setOLDDAH(String oLDDAH) {
					OLDDAH = oLDDAH;
				}
				@Column(name = "CB",length = 100)
				public String getCB() {
					return CB;
				}

				public void setCB(String cB) {
					CB = cB;
				}
              

}
