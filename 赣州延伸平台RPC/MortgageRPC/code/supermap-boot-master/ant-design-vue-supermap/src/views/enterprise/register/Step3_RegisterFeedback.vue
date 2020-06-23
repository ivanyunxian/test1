<template>
  <div>
    <a-form style="margin: 40px auto 0;">
      <result title="操作成功" :is-success="true" >
        <p>尊敬的 <b>{{data.enterpriseName}}</b>，您好：</p>
        <p>您提交的企业注册信息、用户及授权宗地申请信息我中心已收到，审核结果将在5个工作日内以短信形式通知到您！</p>
        <p>赣州市不动产登记中心</p>
        <div slot="action">
          <a-button type="primary" @click="nexproject">关闭</a-button>
        </div>
      </result>
    </a-form>
  </div>
</template>

<script>
    import Result from '../../result/Result'
    import { getAction, postAction } from '@/api/manage'
    export default {
        name: "Step3_RegisterFeedback",
        components: {
          Result
        },
        data () {
          return {
            data:{},
            url:{
              getEnterpriseurl:'/yspt/enterprise/getenterprise',
            }
          }
        },
        created() {
          this.initsbuPage();
        },
        props:['enterpriseid'],
        methods: {
          nexproject () {
            this.$emit('closeModal');
          },
          prevStep () {
            this.$emit('prevStep')
          },
          initsbuPage() {
            getAction(this.url.getEnterpriseurl,{id:this.enterpriseid}).then((res)=>{
              if(res.success){
                this.$message.success(res.message);
                this.data = res.result;
              } else {
                this.$message.error(res.message);
              }
            });
          },
        }
    }
</script>

<style scoped>
  .title{
    text-align:center;
    background: rgba(242, 242, 242, 1);
    padding:15px;
  }
</style>