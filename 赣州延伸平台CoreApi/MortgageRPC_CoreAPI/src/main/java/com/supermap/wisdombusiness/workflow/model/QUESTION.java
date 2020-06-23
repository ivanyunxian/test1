

		    /**
            * 
            * 代码生成器自动生成[QUESTION]
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
            @Table(name = "QUESTION" ,schema = "BDC_WORKFLOW")
            public class QUESTION{

             private String Question_Id;
             private String Question_Title;
             private String Question_Content;
             private Date Question_Time;
             private String Question_Staffname;
             private String Question_Staffid;
             private String Question_Dept;
             private String Question_Deptid;
             private String Question_Type;
             private String Question_Typename;
             private String Question_Status;



              @Id              @Column(name = "QUESTION_ID",length = 200)
                public  String  getQuestion_Id() {
              if (Question_Id == null) Question_Id = UUID.randomUUID().toString().replace("-", "");                  return Question_Id;
             }

                public void setQuestion_Id( String Question_Id) {
                  this.Question_Id = Question_Id;
             }

              @Column(name = "QUESTION_TITLE",length = 200)
                public  String  getQuestion_Title() {
                  return Question_Title;
             }

                public void setQuestion_Title( String Question_Title) {
                  this.Question_Title = Question_Title;
             }

              @Column(name = "QUESTION_CONTENT",length = 2000)
                public  String  getQuestion_Content() {
                  return Question_Content;
             }

                public void setQuestion_Content( String Question_Content) {
                  this.Question_Content = Question_Content;
             }

              @Column(name = "QUESTION_TIME", length=7)
                public  Date  getQuestion_Time() {
                  return Question_Time;
             }

                public void setQuestion_Time( Date Question_Time) {
                  this.Question_Time = Question_Time;
             }

              @Column(name = "QUESTION_STAFFNAME",length = 200)
                public  String  getQuestion_Staffname() {
                  return Question_Staffname;
             }

                public void setQuestion_Staffname( String Question_Staffname) {
                  this.Question_Staffname = Question_Staffname;
             }

              @Column(name = "QUESTION_STAFFID",length = 200)
                public  String  getQuestion_Staffid() {
                  return Question_Staffid;
             }

                public void setQuestion_Staffid( String Question_Staffid) {
                  this.Question_Staffid = Question_Staffid;
             }

              @Column(name = "QUESTION_DEPT",length = 200)
                public  String  getQuestion_Dept() {
                  return Question_Dept;
             }

                public void setQuestion_Dept( String Question_Dept) {
                  this.Question_Dept = Question_Dept;
             }

              @Column(name = "QUESTION_DEPTID",length = 200)
                public  String  getQuestion_Deptid() {
                  return Question_Deptid;
             }

                public void setQuestion_Deptid( String Question_Deptid) {
                  this.Question_Deptid = Question_Deptid;
             }

              @Column(name = "QUESTION_TYPE",length = 20)
                public  String  getQuestion_Type() {
                  return Question_Type;
             }

                public void setQuestion_Type( String Question_Type) {
                  this.Question_Type = Question_Type;
             }

              @Column(name = "QUESTION_TYPENAME",length = 200)
                public  String  getQuestion_Typename() {
                  return Question_Typename;
             }

                public void setQuestion_Typename( String Question_Typename) {
                  this.Question_Typename = Question_Typename;
             }

              @Column(name = "QUESTION_STATUS",length = 20)
                public  String  getQuestion_Status() {
                  return Question_Status;
             }

                public void setQuestion_Status( String Question_Status) {
                  this.Question_Status = Question_Status;
             }

}
