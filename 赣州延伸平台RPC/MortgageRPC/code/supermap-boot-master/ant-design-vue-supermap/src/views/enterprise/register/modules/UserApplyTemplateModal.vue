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
          v-show=false
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="主键">
          <a-input
            :disabled="disable"
            placeholder="请输入主键"
            v-decorator="['enterpriseid' , validatorRules.enterpriseid]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="姓名">
          <a-input
            placeholder="请输入姓名"
            v-decorator="['realname', validatorRules.realname ]"
          />
        </a-form-item>


        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="证件号码">
          <a-input
            placeholder="请输入证件号码"
            v-decorator="['zjh', validatorRules.zjh ]"
          />
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户名">
          <a-input
            :disabled="disable"
            placeholder="请输入用户名"
            v-decorator="['username', validatorRules.username ]"
          />
        </a-form-item>

        <a-form-item
          v-show="!useEditor"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="手机号码">
          <a-row :gutter="8">
            <a-col :span="18">
              <a-input
                :disabled="phonedisable"
                placeholder="请输入手机号码"
                v-decorator="['phone', validatorRules.phone ]"
              />
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button @click="handleYZM" :disabled="yzmDisabled" type="primary">{{yzmBtnText}}</a-button>
            </a-col>
          </a-row>

        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="验证码">
          <a-input
            placeholder="请输入验证码"
            v-decorator="['code', validatorRules.code ]"
          />
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import { duplicateCheck } from '@/api/api'
  import JEditor from '@/components/jeecg/JEditor'
  import {checkOnlyUser} from '@/api/api'
  import { getAction, postAction } from '@/api/manage'

  export default {
    name: "UserApplyTemplateModal",
    components:{
      JEditor
    },
    data() {
      return {
        title: "操作",
        visible: false,
        disable: true,
        editable:true,
        phonedisable:false,
        yzmDisabled:false,
        yzmBtnText:'获取验证码',
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
          realname: {rules: [{required: true, message: '请输入姓名!' }]},
          zjh: {rules: [{required: true,pattern: '^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$', message: '身份证号格式不对!'}]},
          username: {rules: [{ required: true, message: '用户名不能为空'}, { validator: this.checkUsername }], validateTrigger: ['change', 'blur']},
          phone: {rules: [{ required: true, pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号' }, { validator: this.handlePhoneCheck } ], validateTrigger: ['change', 'blur'] },
          enterpriseid:{rules: []},
          code:{rules: [{required: true, message: '请输入验证码!' }]},
        },
        url: {
          add: "/sys/user/enterprise/add",
          edit: "/sys/user/enterprise/edit",
          yzmurl:'/yspt/enterprise/sendtextmsg',
          verifyurl:'/yspt/enterprise/verifytextcode',
        },
        useEditor:false,
        templateEditorContent:""
      }
    },
    created() {
      console.log("测试"+this.enterpriseid);
      this.form.setFieldsValue({enterpriseid:this.enterpriseid});
      this.editable=true
    },
    props: [ 'enterpriseid'],
    methods: {
      checkUsername(rule, value, callback) {
          var params = {
            username: value,
          };
          if(this.editable){
            checkOnlyUser(params).then((res) => {
              if (res.success) {
                callback()
              } else {
                callback("用户名已存在!")
              }
            })
          }else{
            callback();
          }
      },
      handlePhoneCheck(rule, value, callback) {
         var params = {
           phone: value,
         };
         if(this.editable){
            checkOnlyUser(params).then((res) => {
              if (res.success) {
                callback()
              } else {
                callback("手机号已存在!")
              }
            })
         }else{
            callback();
          }

      },
      add() {
        this.disable = false;
        this.editable = true;
        this.edit({});
      },
      edit(record) {
        if(JSON.stringify(record)!='{}'){
          this.editable = false;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;

        this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model, 'realname', 'zjh', 'username','phone', 'enterpriseid'))
        });
      },
      close() {
        this.$emit('close');
        this.visible = false;
        this.disable = true;
        //this.$ref.
      },
      handleOk() {
        //this.model.templateType = this.templateType;
        this.model.enterpriseid = this.enterpriseid;
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
              that.verifycode(values);
          }
        })
      },
      verifycode(values){
        const that = this;
        var param = {};
        param.code = this.form.getFieldValue("code");
        postAction(this.url.verifyurl, param).then((res) => {
          if(res.success) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化

            if(this.useEditor){
              formData.templateContent=this.templateEditorContent
            }
            formData.enterpriseid = that.enterpriseid;
            console.log(formData)
            //formData.enterpriseid = this.enterpriseid;
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          } else {
            this.$notification['error']({
              message: res.message
            })
             //this.saveEnterprise(formData);
            return ;
          }

        })



      },
      handleYZM(){
        var param = {};
        param.phone = this.form.getFieldValue("phone");
        if(param.phone == "" || param.phone ==undefined){
          this.$message.error('请填写注册者手机号');
          return;
        }
        postAction(this.url.yzmurl, param).then((res) => {
          if(res.success) {
            this.$message.success('验证码发送成功');
            //设置手机号码不可编辑
            this.phonedisable = true;
            this.yzmDisabled = true;
            //验证码发送
            let longtime =30;
            let that=this
            let a = setInterval(function() {
              if (longtime == 1) {
                that.yzmBtnText = "发送验证码";
                that.yzmDisabled = false;
                clearInterval(a);
                that.phonedisable = false;
              } else {
                longtime--;
                console.log(longtime);
                that.yzmBtnText = `重新发送(${longtime})`;
              }
            }, 1000);
          } else {
            this.$notification['error']({
              message: res.message
            })
          }
        })
      },
      validateTemplateCode(rule, value, callback){
        var params = {
          tableName: "sys_sms_template",
          fieldName: "template_code",
          fieldVal: value,
          dataId: this.model.id
        }
        duplicateCheck(params).then((res)=>{
          if(res.success){
            callback();
          }else{
            callback(res.message);
          }
        })

      },
      handleCancel() {
        this.close()
      },
      handleChangeTemplateType(value){
        //如果是邮件类型那么则改变模板内容是富文本编辑器
        this.useEditor = value==2
      }

    }
  }
</script>

<style scoped>

</style>