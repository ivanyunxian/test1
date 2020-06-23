<template>
  <a-card :visible="visible">
    <a-form :form="form">
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"

        label="不动产单元号">
        <a-input style="border:0px;" placeholder=""  :disabled="disable" v-decorator="['bdcdyh', {}]"/>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"

        label="坐落">
        <a-input style="border:0px;" placeholder="" :disabled="disable" v-decorator="['zl', {}]"/>
      </a-form-item>

      <!--
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="查封状态">
        <a-input style="border:0px;" placeholder="" v-decorator="['cfzt', {}]"/>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="抵押状态">
        <a-input style="border:0px;" placeholder="" v-decorator="['dyzt', {}]"/>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="异议状态">
        <a-input style="border:0px;" placeholder="" v-decorator="['yyzt', {}]"/>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="宗地面积">
        <a-input style="border:0px;" placeholder="" v-decorator="['zdmj', {}]"/>
      </a-form-item>
      -->
    </a-form>
  </a-card>
</template>

<script>
    import {queryIdTree} from '@/api/api'
    import pick from 'lodash.pick'
    export default {
      name: "ZdBaseInfo",
      data() {
        return {
          id: '',
          model: {},
          visible: false,
          disable: true,
          form: this.$form.createForm(this),
          labelCol: {
            xs: {span: 24},
            sm: {span: 3}
          },
          wrapperCol: {
            xs: {span: 24},
            sm: {span: 16}
          },
        }
      },
      created() {
      },
      methods: {
        open(record) {
          this.form.resetFields();
          this.model = Object.assign({}, record);
          this.visible = true;
          console.log("record:");
          console.log(record);
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(record,'bdcdyh','zl'));
          });
        },
        clearForm() {
          this.form.resetFields();
        },
      }
    }
</script>

<style scoped>

</style>