<template>
  <div>
    <a-form style="margin: 40px auto 0;">
      <result title="提交成功" :is-success="true" description="请耐心等待系统审核">
        <div class="information">
          <div style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">项目名称 : {{data.PROJECT_NAME}}</div>
          <a-row style="margin-bottom: 16px">
            <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="6">
              <span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">项目流水号 : {{data.PROLSH}}</span>
            </a-col>
            <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="6">
              <span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">申请人：{{data.ACCEPTOR}}</span>
            </a-col>
            <a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="12">
              <span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">申请时间： {{data.CREAT_DATE}}</span>
            </a-col>
          </a-row>
        </div>
        <div slot="action">
          <a-button type="primary" @click="nextProject">继续办理</a-button>
          <a-button style="margin-left: 8px" @click="toEndList">已办箱</a-button>
          <!--<a-button style="margin-left: 8px" @click="print">打印回执</a-button>-->
        </div>
      </result>
    </a-form>
    <div>
      <recipient-modal ref="modalForm" :prolsh="this.prolsh" :recipientData="this.recipientData" @ok="modalFormOk"></recipient-modal>
    </div>
  </div>
</template>

<script>
  import Result from '@/views/result/Result'
  import {getAction} from '@/api/manage'
  import recipientModal from '../../Receipt/modalForm/recipient_chuxiong'

  export default {
    name: "Step3_SubmitResult",
    components: {
      Result,
      recipientModal
    },
    data () {
      return {
        loading: false,
        data:{},
        recipientData:{},
        url:{
          projectlist: "/modules/wfi_proinst/projectlistendbox",
          recipientlist:"/modules/wfi_proinst/getRecipientData"
        }
      }
    },
    props:['prolsh'],
    created() {
      this.initsbuPage();
    },
    methods: {
      nextProject () {
        this.$emit('close');
      },
      initsbuPage() {
        getAction(this.url.projectlist,{prolsh:this.prolsh}).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.data = res.result.records[0];
          } else {
            this.$message.error(res.message);
          }
        });
      },
      toEndList () {
        this.$router.push('/mortgagerpc/wfi_proinst/projectEndlist');
      },
      print(){
        getAction(this.url.recipientlist,{prolsh:this.prolsh}).then((res)=> {
          if (res.success) {
            this.recipientData = res.result;
          } else {
            this.$message.error(res.message);
          }
        });
        this.$refs.modalForm.visible = true;
        this.$refs.modalForm.title = "打印回执单";
        this.$refs.modalForm.printRecipient();
      }
    }
  }
</script>
<style lang="scss" scoped>
  .information {
    line-height: 22px;

    .ant-row:not(:last-child) {
      margin-bottom: 24px;
    }
  }
  .money {
    font-family: "Helvetica Neue",sans-serif;
    font-weight: 500;
    font-size: 20px;
    line-height: 14px;
  }
  .coltitle {
    font-size: 16px;
  }
</style>