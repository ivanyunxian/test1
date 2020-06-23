<template>
  <Layout>
    <a-card class="card" :bordered="false" style="margin:0 auto;width:80%;">
      <a-steps class="steps" :current="currentTab">
        <a-step title="企业注册信息填写" />
        <a-step title="用户及授权宗地申请" />
        <a-step title="提交审核" />
      </a-steps>
      <div class="content">
        <Step1_RegisterInfo v-if="currentTab === 0" @nextStep="nextStep" ref="step1page" :enterpriseid="enterpriseid" disabled="false" :checkstep2 ="checkstep2"/>
        <Step2_UserApply v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep" :enterpriseid="enterpriseid"  @checkstep2="getcheckstep2"/>
        <Step3_RegisterFeedback v-if="currentTab === 2" @prevStep="prevStep"  :enterpriseid="enterpriseid" @finish="finish" @closeModal="closeModal"/>
      </div>
    </a-card>
  </Layout>
</template>

<script>
  import Step1_RegisterInfo from './Step1_RegisterInfo'
  import Step2_UserApply from './Step2_UserApply'
  import Step3_RegisterFeedback from './Step3_RegisterFeedback'

  export default {
    name: "RegisterEnterprise",
    components: {
      Step1_RegisterInfo,
      Step2_UserApply,
      Step3_RegisterFeedback,
    },
    mixins: [],
    data() {
      return {
        currentTab: 0,
        checkstep2:''
      }
    },
    created(){
      if(this.enterpriseid == null || this.enterpriseid=="" || this.enterpriseid==undefined){
        this.enterpriseid = this.getuuid();
      }
      console.log("step0:"+this.enterpriseid);
    },
    props:['enterpriseid'],
    methods: {
      loadinitData(){
         this.$nextTick(() => {
            this.checkstep2 = true;
            this.$refs.step1page.loadPageData();
            this.$refs.step1page.loadFj()
         })
      },
      getcheckstep2(msg){
        this.checkstep2 = msg
      },
      getuuid() {
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
              s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            return uuid;
      },
      // handler
      nextStep () {
        if (this.currentTab < 2) {
          this.currentTab += 1
        }
      },
      prevStep () {
        if (this.currentTab > 0) {
          this.currentTab -= 1
        }
      },
      finish () {
        this.currentTab = 0
      },
      closeModal () {
        this.$emit('closeModal');
      }
    }
  }
</script>
