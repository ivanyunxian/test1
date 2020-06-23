<template>
  <a-modal
    :title="title"
    :width="1000"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="保存"
    cancelText="取消">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="接口类型">
          <j-dict-select-tag style="width: 100%" v-decorator="['type', validatorRules.type]" :onChange="checktype()"
                             placeholder="请输入接口类型" :triggerChange="true" dictCode="ORGINTERTYPE"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="接口名称">
          <a-input placeholder="请输入接口名称" v-decorator="['name', validatorRules.name]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="接口地址">
          <a-input placeholder="请输入接口地址" v-decorator="['value', validatorRules.value]" />
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户名">
          <a-input placeholder="请输入用户名（机构提供的用户名）" v-decorator="['username', validatorRules.username]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="密码">
          <a-input placeholder="请输入密码（机构提供的密码）" v-decorator="['password', validatorRules.password]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-input placeholder="请输入备注" v-decorator="['memo', {}]" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "SysDepartConfigModal",
    data () {
      return {
        title:"接口编辑",
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
          type:{rules: [{ required: true, message: '请输入接口类型!' }]},
          name:{rules: [{ required: true, message: '请输入接口名称!' }]},
          value:{rules: [{ required: true, message: '请输入接口地址!' }]},
          username:{rules: [{ required: false, message: '请输入用户名!' }]},
          password:{rules: [{ required: false, message: '请输入密码!' }]}
        },
        url: {
          add: "/sys/sysDepartConfig/add",
          edit: "/sys/sysDepartConfig/edit",
        },
      }
    },
    created () {
    },
    props:['departid'],
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'username','password','name','value','type','memo'))
		  //时间格式化
        });

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
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.deptId = this.departid;

            console.log(formData)
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
      checktype() {
        var fieldValue = this.form.getFieldValue("type");
        if(fieldValue && fieldValue=='3001') {
          this.validatorRules.username.rules[0].required = true;
          this.validatorRules.password.rules[0].required = true;

        } else {
          this.validatorRules.username.rules[0].required = false;
          this.validatorRules.password.rules[0].required = false;
        }
      }

    }
  }
</script>

<style lang="less" scoped>

</style>