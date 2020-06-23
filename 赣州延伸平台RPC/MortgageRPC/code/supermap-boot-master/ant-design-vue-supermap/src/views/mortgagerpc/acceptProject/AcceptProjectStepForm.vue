<template>
  <a-modal
    :visible="visible"
    :width="modal.width"
    :style="modal.style"
    :footer="null"
    @ok="close"
    @cancel="close"
    height="750px"
    cancelText="关闭">
    <a-card :bordered="false">
      <a-steps class="steps" :current="currentTab">
        <a-step title="房源核验" />
        <a-step title="业务申报" />
        <a-step title="完成" />
      </a-steps>
      <div class="content">
        <step1 v-if="currentTab === 0" @nextStep="nextStep" @initStep2Page="initStep2Page" ref="step1page"
               :prolsh="prolsh"
               />
        <step2 v-if="currentTab === 1" ref="step2page" @nextStep="nextStep" @prevStep="prevStep" :prolsh="prolsh" :actionshow="true" :disabled="false"/>
        <step3 v-if="currentTab === 2" @prevStep="prevStep" @close="close" :prolsh="prolsh"/>
      </div>
    </a-card>
  </a-modal>
</template>

<script>
  import Step1 from './stepForm/Step1_HouseCheck'
  import Step2 from './stepForm/Step2_AcceptProject'
  import Step3 from './stepForm/Step3_SubmitResult'

  export default {
    name: "AcceptProjectStepForm",
    components: {
      Step1,
      Step2,
      Step3
    },
    data () {
      return {
        visible : false,
        description: '将受理步骤分为三步：房源核验->填写申请->提交反馈。',
        currentTab: 0,
        form: null,
        modal: {
          title: '业务申报',
          width: '95%',
          style: { top: '20px' },
          fullScreen: true
        }
      }
    },
    props:['prolsh'],
    methods: {
      close () {
        this.housedata = [];
        this.qlrlist = [];
        this.currentTab = 0;
        Step1.queryParam={};
        this.$emit('close');
        this.$emit('loadData');
        this.visible = false;
      },
      initStep2Page() {
        // this.$nextTick(() => {
        //   this.$refs.step2page.selectedhouse(0);
        // })
      },
      initStep1Page() {
        this.$nextTick(() => {
          this.$refs.step1page.loadYwlx();
          this.$refs.step1page.loadHouseData();
          this.$refs.step1page.loadselectedData();
        })
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

<style lang="scss" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
</style>