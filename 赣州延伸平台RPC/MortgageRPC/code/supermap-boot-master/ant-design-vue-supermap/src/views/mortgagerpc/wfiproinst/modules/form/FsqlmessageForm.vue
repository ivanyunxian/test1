<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押人">
          <a-input
            placeholder="请输入抵押人"
            :disabled="disabled"
            v-decorator="[
              'fsql.dyr',
              {rules: [{ required: true, message: '抵押人'}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="债务人">
          <a-input
            placeholder="请输入债务人"
            :disabled="disabled"
            v-decorator="[
              'fsql.zwr',
              {rules: [{ required: false, message: '债务人'}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="债务人证件号">
          <a-input
            placeholder="请输入债务人证件号"
            :disabled="disabled"
            v-decorator="[
              'fsql.zwrzjh',
              {rules: [{ required: false, message: '债务人证件号'}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押范围">
          <a-input
            placeholder="请输入抵押范围"
            :disabled="disabled"
            v-decorator="[
              'fsql.zjjzwdyfw',
              {rules: [{ required: false, message: '抵押范围'}]}
            ]" />
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="最高债权确定事实">
          <a-input
            placeholder="请输入最高债权确定事实"
            :disabled="disabled"
            v-decorator="[
              'fsql.zgzqqdss',
              {rules: [{ required: false, message: '最高债权确定事实'}]}
            ]" />
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="单个被担保主债权数额">
          <a-input
            placeholder="请输入单个被担保主债权数额"
            :disabled="disabled"
            v-decorator="[
              'fsql.dgbdbzzqse',
              {rules: [{ required: false, message: '单个被担保主债权数额'}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>

    <!--<a-form-item v-if="showSubmit">
      <a-button htmlType="submit" >Submit</a-button>
    </a-form-item>-->
  </a-form>
</template>

<script>
  export default {
    name: "FsqlmessageForm",
    data () {
      return {
        form: this.$form.createForm(this)
      }
    },
    props:['disabled'],
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