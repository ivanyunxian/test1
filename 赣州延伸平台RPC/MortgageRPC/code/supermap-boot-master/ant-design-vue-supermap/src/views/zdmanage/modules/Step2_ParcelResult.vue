<template>
  <div>
    <a-form style="margin: 40px auto 0;">
      <result title="操作成功" :is-success="true" >
        <p>尊敬的 <b>{{data.enterpriseName}}</b>，您好：</p>
        <p>您提交的企业注册信息、用户及授权宗地申请信息我中心已收到，审核结果将在5个工作日内以短信形式通知到您！</p>
        <p>赣州市不动产登记中心</p>
        <div slot="action">
          <a-button type="primary" @click="close">关闭</a-button>
        </div>
      </result>
    </a-form>
  </div>
</template>

<script>
    import Result from '../../result/Result'
    import { getAction, postAction } from '@/api/manage'
    export default {
      name: "Step2_ParcelResult",
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
      methods: {
        close () {
          this.$emit('close')
        }
      }
    }
</script>

<style scoped>

</style>