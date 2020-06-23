<template>
  <a-modal
    :visible="visible"
    width="95%"
    style="top:20px;bottom:10px;"
    :footer="null"
    @cancel="handleCancel"
    cancelText="关闭">
    <div slot="footer">
     <button type="button" @click="handleCancel" class="ant-btn"><span>关 闭</span></button>
    </div>
    <register-enterprise @closeModal="close" ref="acceptstep" :enterpriseid="enterpriseid"></register-enterprise>
  </a-modal>
</template>

<script>
    import RegisterEnterprise from '@/views/enterprise/register/RegisterEnterprise'
    export default {
      name: "EditListModal",
      components:{
        RegisterEnterprise
      },
      data () {
        return {
          visible : false,
          description: '将受理步骤分为三步：房源核验->填写申请->提交反馈。',
          model: {
            width: '95%'
          },
        }
      },
      create(){
        console.log("edit")
      },
      props:['enterpriseid'],
      methods: {
        loadecditData(){
           this.$nextTick(() => {
              this.$refs.acceptstep.loadinitData();
           })
        },
        edit (record) {
          this.visible = true;
        },
        close() {
          //this.$emit('close');
          this.$refs.acceptstep.currentTab = 0;
          this.$parent.loadData();
          this.visible = false;
        },
        handleCancel() {
          this.close()
        },
        handleOk() {
          //this.model.templateType = this.templateType;
          //this.model.enterpriseid = this.enterpriseid;
          const that = this;
          // 触发表单验证
          this.form.validateFields((err, values) => {
            if (!err) {
              that.verifycode(values);
            }
          })
        },
      }
    }
</script>

<style scoped>

</style>