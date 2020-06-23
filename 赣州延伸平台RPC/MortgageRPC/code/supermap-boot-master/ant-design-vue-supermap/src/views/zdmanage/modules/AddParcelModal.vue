<template>
  <div>
    <a-modal
      centered
      :title="title"
      width="80%"
      style="top:20px;bottom:10px;"
      :visible="visible"
      :footer="null"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <a-card :bordered="false">
        <a-steps class="steps" :current="currentTab">
          <a-step :title="flag == 'personal'?'人员申请':'宗地申请'" />
          <a-step title="提交审核" />
        </a-steps>
        <div class="content">
          <Step1_AddParcel :flag="flag" v-if="currentTab === 0" ref="step1" :enterpriseid="enterpriseid" @nextStep="nextStep"/>
          <Step2_ParcelResult v-if="currentTab === 1" :enterpriseid="enterpriseid" @close="handleOk"/>
        </div>
      </a-card>
    </a-modal>
  </div>
</template>

<script>
    import Step1_AddParcel from './Step1_AddParcel'
    import Step2_ParcelResult from './Step2_ParcelResult'
    export default {
      name: "AddParcelModal",
      props:['flag','enterpriseid'],
      components:{
        Step1_AddParcel,
        Step2_ParcelResult
      },
      data() {
        return {
          visible:false,
          title: "新增宗地",
          currentTab: 0,
        }
      },
      methods: {
        loadData(flag){
          this.$nextTick(() => {
              if(flag == "1"){
                this.$refs.step1.loadData();
              }else if(flag == "2"){
                this.$refs.step1.loadselectedData();
              }

           })
        },
        handleCancel() {
          this.visible = false;
          this.currentTab = 0;
          this.$parent.loadData();
        },
        handleOk() {
          this.visible = false;
          this.currentTab = 0;
          this.$parent.loadData();
        },
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
        }
      }
    }
</script>

<style scoped>

</style>