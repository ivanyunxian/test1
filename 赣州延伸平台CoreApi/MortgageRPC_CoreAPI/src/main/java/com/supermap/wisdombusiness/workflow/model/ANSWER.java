

		    /**
            * 
            * 代码生成器自动生成[ANSWER]
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
            @Table(name = "ANSWER" ,schema = "BDC_WORKFLOW")
            public class ANSWER{

             private String Answer_Id;
             private String Question_Id;
             private String Answer_Content;
             private Date Answer_Time;
             private String Answer_Staffname;
             private String Answer_Staffid;



              @Id              @Column(name = "ANSWER_ID",length = 200)
                public  String  getAnswer_Id() {
              if (Answer_Id == null) Answer_Id = UUID.randomUUID().toString().replace("-", "");                  return Answer_Id;
             }

                public void setAnswer_Id( String Answer_Id) {
                  this.Answer_Id = Answer_Id;
             }

              @Column(name = "QUESTION_ID",length = 200)
                public  String  getQuestion_Id() {
                  return Question_Id;
             }

                public void setQuestion_Id( String Question_Id) {
                  this.Question_Id = Question_Id;
             }

              @Column(name = "ANSWER_CONTENT",length = 2000)
                public  String  getAnswer_Content() {
                  return Answer_Content;
             }

                public void setAnswer_Content( String Answer_Content) {
                  this.Answer_Content = Answer_Content;
             }

              @Column(name = "ANSWER_TIME", length=7)
                public  Date  getAnswer_Time() {
                  return Answer_Time;
             }

                public void setAnswer_Time( Date Answer_Time) {
                  this.Answer_Time = Answer_Time;
             }

              @Column(name = "ANSWER_STAFFNAME",length = 200)
                public  String  getAnswer_Staffname() {
                  return Answer_Staffname;
             }

                public void setAnswer_Staffname( String Answer_Staffname) {
                  this.Answer_Staffname = Answer_Staffname;
             }

              @Column(name = "ANSWER_STAFFID",length = 200)
                public  String  getAnswer_Staffid() {
                  return Answer_Staffid;
             }

                public void setAnswer_Staffid( String Answer_Staffid) {
                  this.Answer_Staffid = Answer_Staffid;
             }

}
