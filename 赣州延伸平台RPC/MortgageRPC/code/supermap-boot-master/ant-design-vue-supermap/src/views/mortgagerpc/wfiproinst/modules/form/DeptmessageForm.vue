<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16">
      <a-col :lg="6" :md="12" :sm="24">
        <a-form-item label="所属机构名称">
          <a-select placeholder="请选择所属机构" v-decorator="[ 'dept.deptid', {rules: [{ required: true, message: '请选择所属机构'}]} ]">
            <a-select-option value="1" >中国银行南宁中泰路支行</a-select-option>
            <a-select-option value="A01A01">中国银行南宁朝阳路支行</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 7, offset: 1}" :lg="{span: 8}" :md="{span: 12}" :sm="24">
        <a-form-item
          label="所属机构代码">
          <a-input
            placeholder="所属机构代码"
            v-decorator="[
              'dept.deptzjh',
              {rules: [{ required: true, message: '所属机构代码', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <!--<a-col :xl="{span: 9, offset: 1}" :lg="{span: 10}" :md="{span: 24}" :sm="24">
        <a-form-item
          label="法人名称">
          <a-input
            placeholder="请输入法人名称"
            v-decorator="[
              'dept.deptfr',
              {rules: [{ required: true, message: '请输入法人名称', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>-->
    </a-row>

    <a-row class="form-row" :gutter="16">
      <a-col :lg="6" :md="12" :sm="24">
        <a-form-item label="代理人">
          <a-input
            placeholder="代理人"
            v-decorator="[
              'dept.username',
              {rules: [{ required: true, message: '请填写代理人姓名', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 7, offset: 1}" :lg="{span: 8}" :md="{span: 12}" :sm="24">
        <a-form-item
          label="代理人证件号">
          <a-input
            placeholder="代理人证件号"
            v-decorator="[
              'dept.userzjh',
              {rules: [{ required: true, message: '请填写代理人证件号', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 9, offset: 1}" :lg="{span: 10}" :md="{span: 24}" :sm="24">
        <a-form-item
          label="代理人联系电话">
          <a-input
            placeholder="请输入代理人联系电话"
            v-decorator="[
              'dept.usertel',
              {rules: [{ required: true, message: '请输入代理人联系电话', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
   <!-- <a-form-item v-if="showSubmit">
      <a-button htmlType="submit" >Submit</a-button>
    </a-form-item>-->
  </a-form>
</template>

<script>
  export default {
    name: "DeptmessageForm",
    props: {
      showSubmit: {
        type: Boolean,
        default: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this)
      }
    },
    methods: {
      handleSubmit (e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
          if (!err) {
            this.$notification['error']({
              message: 'Received values of form:',
              description: values
            })
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>