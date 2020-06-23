<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="目录名称">
          <a-input placeholder="请输入目录名称" v-decorator="['name', {rules: [{ required: true, message: '请输入目录名称!' }]}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否必须">
          <j-dict-select-tag v-decorator="['required', {rules: [{ required: true, message: '请选择是否必须!' }]}]"
                             placeholder="请选择是否必须" :triggerChange="true" dictCode="yn"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否获取电子证照">
          <j-dict-select-tag v-decorator="['ecert', {rules: [{ required: true, message: '请选择是否获取电子证照!' }]}]"
                             @change="changeEcertCodeType" placeholder="请选择是否获取电子证照" :triggerChange="true" dictCode="yn"/>
        </a-form-item>
         <a-form-item
           v-show="dzlxflag==1"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="电子证照类型">
          <j-dict-select-tag v-decorator="['ecertCode', {rules: [{ required:(dzlxflag==1?true:false) , message: '请选择电子证照类型!' }]}]"
                         placeholder="请选择电子证照类型" :triggerChange="true" dictCode="E_CERTCODE"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="目录序号">
          <a-input placeholder="请输入目录序号" v-decorator="['fileindex', {rules: [{ required: true, message: '请输入目录序号!' }]}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-textarea placeholder="请输入备注"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'matedesc',
              {}
            ]" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "Wfd_materclassModal",
    data () {
      return {
        title:"操作",
        dzlxflag:0,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        },
        url: {
          add: "/mortgagerpc/wfd_materclass/add",
          edit: "/mortgagerpc/wfd_materclass/edit",
        },
      }
    },
    created () {
    },
    props: ['prodefid'],
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'matedesc','fileindex','name','required','ecert','ecertCode'))
		  //时间格式化
        });
        this.dzlxflag = this.model.ecert;
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            this.model.procodeid = this.prodefid;
            let formData = Object.assign(this.model, values);
            //时间格式化
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },
      changeEcertCodeType (data) {
        console.log(data)
        this.dzlxflag = data
      }

    }
  }
</script>

<style lang="less" scoped>

</style>