<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押期限">
          <a-range-picker
            style="width: 100%"
            :disabled="disabled"
            v-decorator="[
              'ql.qlsj',
              {rules: [{ required: true, message: '请选择抵押期限'}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7, offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押方式">
          <a-select placeholder="请选择抵押类型" :disabled="disabled" v-decorator="[ 'ql.dylx', {rules: [{ required: true, message: '请选择抵押类型'}]} ]">
            <a-select-option value="1">一般抵押</a-select-option>
            <a-select-option value="2">最高债权抵押</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="总抵押金额">
          <a-input
            placeholder="请输入总抵押金额"
            :disabled="disabled"
            v-decorator="[
              'ql.dyje',
              {rules: [{ required: true, message: '请输入总抵押金额'}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="持证方式">
          <a-select placeholder="请选择持证方式" :disabled="disabled" v-decorator="[ 'ql.czfs', {rules: [{ required: true, message: '请选择持证方式'}]} ]">
            <a-select-option value="02">分别持证</a-select-option>
            <a-select-option value="01">共同持证</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7, offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="证书个数">
          <a-select placeholder="请选择证书个数" :disabled="disabled" v-decorator="[ 'ql.sfhbzs', {rules: [{ required: true, message: '请选择证书个数'}]} ]">
            <a-select-option value="0">每个单元单独发证</a-select-option>
            <a-select-option value="1">多单元一本证</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7, offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="总抵押评估价值">
          <a-input
            placeholder="请输入总抵押评估价值"
            :disabled="disabled"
            v-decorator="[
              'ql.dypgjz',
              {rules: [{ required: false, message: '请输入总抵押评估价值'}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押不动产类型">
          <a-select placeholder="请选择抵押不动产类型" :disabled="disabled" v-decorator="[ 'ql.dywlx', {rules: [{ required: true, message: '请选择抵押不动产类型'}]} ]">
            <a-select-option value="1">土地</a-select-option>
            <a-select-option value="2">土地和房屋</a-select-option>
            <a-select-option value="3">林地和林木</a-select-option>
            <a-select-option value="4">土地和在建建筑物</a-select-option>
            <a-select-option value="5">海域</a-select-option>
            <a-select-option value="6">海域和构筑物</a-select-option>
            <a-select-option value="7">其它</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7, offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="债权单位">
          <a-select placeholder="请选择债权单位" :disabled="disabled" v-decorator="[ 'ql.zqdw', {rules: [{ required: true, message: '请选择债权单位'}]} ]">
            <a-select-option value="1">元</a-select-option>
            <!--<a-select-option value="2">美元</a-select-option>-->
            <a-select-option value="3">万元</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>

    </a-row>
    <a-row v-if="djlx!='400'">
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="登记原因">
          <a-textarea placeholder="请输入登记原因" :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'ql.djyy',
              {rules: [{ required: false, message: '请输入登记原因', whitespace: false}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row v-if="djlx!='400'">
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="附记">
          <a-textarea placeholder="请输入附记内容" :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'ql.fj',
              {rules: [{ required: false, message: '请输入附记内容', whitespace: false}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>

<!---注销-->
    <a-row v-if="djlx==400">
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="注销登记原因">
          <a-textarea placeholder="请输入注销登记原因" :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'ql.zxdyyy',
              {rules: [{ required: false, message: '请输入注销登记原因', whitespace: false}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row v-if="djlx==400">
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="注销附记">
          <a-textarea placeholder="请输入注销附记内容" :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'ql.zxfj',
              {rules: [{ required: false, message: '请输入注销附记内容', whitespace: false}]}
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
    name: "QlmessageForm",
    data () {
      return {
        form: this.$form.createForm(this)
      }
    },
    props:['disabled','djlx','qllx'],
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