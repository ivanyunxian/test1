<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :okText="disabled?'确定':'保存'"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :label="this.sqrlxdesc+'名称'"
              hasFeedback>
              <a-input placeholder="请输入权利人名称"  :disabled="disabled || ywpageStatus || qlpageStatus"
                       v-decorator="['sqrxm', {rules: [{ required: true, message: '请输入权利人名称!' }]}]"/>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :label="this.sqrlxdesc+'证件类型'"
              hasFeedback>
              <a-select placeholder="请选择证件类型" :disabled="disabled || ywpageStatus"
                        v-decorator="[ 'zjlx', {rules: [{ required: true, message: '请选择证件类型'}]} ]">
                <a-select-option value="1">身份证</a-select-option>
                <a-select-option value="2">港澳台身份证</a-select-option>
                <a-select-option value="3">护照</a-select-option>
                <a-select-option value="4">户口簿</a-select-option>
                <a-select-option value="5">军官证（士兵证）</a-select-option>
                <a-select-option value="6">组织机构代码</a-select-option>
                <a-select-option value="7">营业执照</a-select-option>
                <a-select-option value="99">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :label="this.sqrlxdesc+'证件号'"
              hasFeedback>
              <a-input placeholder="请输入证件号" :disabled="disabled || ywpageStatus || qlpageStatus"
                       v-decorator="['zjh', {rules: [{ required: true, message: '请输入证件号' }]}]"/>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="不动产权证号/证明号"
              hasFeedback>
              <a-input placeholder="请输入不动产权证号/证明号" :disabled="disabled || ywpageStatus"
                       v-decorator="['bdcqzh', {rules: [{ required: false, message: '请输入不动产权证号/证明号' }]}]"/>
            </a-form-item>
          </a-col>

        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :label="this.sqrlxdesc+'旧证号'"
              hasFeedback>
              <a-input placeholder="请输入旧证号" :disabled="disabled || ywpageStatus"
                       v-decorator="['jzh', {rules: [{ required: false, message: '请输入旧证号!' }]}]"/>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :label="this.sqrlxdesc+'类型'"
              hasFeedback>
              <a-select :placeholder="'请选择'+this.sqrlxdesc+'类型'" :disabled="disabled || ywpageStatus"
                        v-decorator="[ 'sqrlx', {rules: [{ required: true, message: '请选择申请人类型'}]} ]">
                <a-select-option value="1">个人</a-select-option>
                <a-select-option value="2">企业</a-select-option>
                <a-select-option value="3">事业单位</a-select-option>
                <a-select-option value="4">户口簿</a-select-option>
                <a-select-option value="99">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="联系方式"
              hasFeedback>
              <a-input placeholder="请输入联系方式" :disabled="disabled || ywpageStatus"
                       v-decorator="['lxdh', {rules: [{ required: false, message: '请输入联系方式!' }]}]"/>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="通讯地址"
              hasFeedback>
              <a-input placeholder="请输入通讯地址" :disabled="disabled || ywpageStatus"
                       v-decorator="['txdz', {rules: [{ required: false, message: '请输入通讯地址!' }]}]"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="共有方式"
              hasFeedback>
              <a-select placeholder="请选择共有方式" :disabled="disabled || ywpageStatus"
                        v-decorator="[ 'gyfs', {rules: [{ required: true, message: '请选择共有方式'}]} ]">
                <a-select-option value="0">单独所有</a-select-option>
                <a-select-option value="1">共同共有</a-select-option>
                <a-select-option value="2">按份共有</a-select-option>
                <a-select-option value="3">其他共有</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="共有份额"
              hasFeedback>
              <a-input placeholder="请输入共有份额" :disabled="disabled || ywpageStatus"
                       v-decorator="['gyfe', {rules: [{ required: false, message: '请输入共有份额!' }]}]"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="法人姓名"
              hasFeedback>
              <a-input placeholder="请输入法人姓名" :disabled="disabled"
                       v-decorator="['fddbr', {rules: [{ required: false, message: '请输入法人姓名!' }]}]"/>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="法人证件类型"
              hasFeedback>
              <a-select placeholder="请选择法人证件类型" :disabled="disabled"
                        v-decorator="[ 'fddbrzjlx', {rules: [{ required: false, message: '请选择法人证件类型'}]} ]">
                <a-select-option value="1">身份证</a-select-option>
                <a-select-option value="2">港澳台身份证</a-select-option>
                <a-select-option value="3">护照</a-select-option>
                <a-select-option value="4">户口簿</a-select-option>
                <a-select-option value="5">军官证（士兵证）</a-select-option>
                <a-select-option value="6">组织机构代码</a-select-option>
                <a-select-option value="7">营业执照</a-select-option>
                <a-select-option value="99">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="法人证件号"
              hasFeedback>
              <a-input placeholder="请输入法人证件号" :disabled="disabled"
                       v-decorator="['fddbrzjhm', {rules: [{ required: false, message: '请输入法人证件号!' }]}]"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代理人姓名"
              hasFeedback>
              <a-input placeholder="请输入代理人姓名" :disabled="disabled"
                       v-decorator="['dlrxm', {rules: [{ required: false, message: '请输入代理人姓名!' }]}]"/>
            </a-form-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代理人证件类型"
              hasFeedback>
              <a-select placeholder="请选择代理人证件类型" :disabled="disabled"
                        v-decorator="[ 'dlrzjlx', {rules: [{ required: false, message: '请选择代理人证件类型'}]} ]">
                <a-select-option value="1">身份证</a-select-option>
                <a-select-option value="2">港澳台身份证</a-select-option>
                <a-select-option value="3">护照</a-select-option>
                <a-select-option value="4">户口簿</a-select-option>
                <a-select-option value="5">军官证（士兵证）</a-select-option>
                <a-select-option value="6">组织机构代码</a-select-option>
                <a-select-option value="7">营业执照</a-select-option>
                <a-select-option value="99">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">

          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代理人证件号"
              hasFeedback>
              <a-input placeholder="请输入代理人证件号" :disabled="disabled"
                       v-decorator="['dlrzjhm', {rules: [{ required: false, message: '请输入代理人证件号!' }]}]"/>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代理人联系方式"
              hasFeedback>
              <a-input placeholder="请输入代理人联系方式" :disabled="disabled"
                       v-decorator="['dlrlxdh', {rules: [{ required: false, message: '请输入代理人联系方式!' }]}]"/>
            </a-form-item>
          </a-col>

        </a-row>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "SqrmessageModal",
    data() {
      return {
        title: "添加权利人",
        sqrlxdesc: "",
        sqrlb:'',
        visible: false,
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
        validatorRules: {},
        url: {
          add: "/modules/bdc_sqr/add",
          edit: "/modules/bdc_sqr/edit",
        }
      }
    },
    created() {
    },
    props:['disabled','dyid','prolsh','ywpageStatus','qlpageStatus'],
    methods: {
      show(){
        this.visible = true;
      },
      add(){
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.sqrlb = record.sqrlb;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'sqrxm','bdcqzh', 'zjlx', 'zjh','sqrlx', 'txdz', 'jzh','gyfs','gyfe','lxdh','fddbr','fddbrzjlx','fddbrzjhm','dlrxm','dlrlxdh','dlrzjlx','dlrzjhm'));
        });

      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        if(this.disabled) {
          this.close();
          return ;
        }

        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            formData.sqrlb = that.sqrlb;
            formData.prolsh = that.prolsh;
            formData.dyid = that.dyid;
            //时间格式化
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

          }
        })
      },
      handleCancel() {
        this.close()
      },
      setProlsh(prolsh) {
        this.prolsh = prolsh;
      }

    }
  }
</script>

<style scoped>

</style>