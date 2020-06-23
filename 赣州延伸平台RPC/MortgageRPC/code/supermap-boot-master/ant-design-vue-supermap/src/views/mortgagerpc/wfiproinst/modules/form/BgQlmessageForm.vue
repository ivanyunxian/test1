<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押人">
          <a-input

            :disabled="disabled"
            v-decorator="[
              'dyr',
              {rules: [{ required: true, message: '抵押人'}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押不动产类型">
          <a-select placeholder="请选择" :disabled="disabled" v-decorator="[ 'dywlx', {rules: [{ required: true, message: '请选择抵押不动产类型'}]} ]">
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
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="建筑物坐落">
          <a-input

            :disabled="disabled"
            v-decorator="[
              'zl',
              {rules: [{ required: false, message: '建筑物坐落'}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押期限">
          <a-range-picker
            style="width: 100%"
            :disabled="disabled"
            v-decorator="[
              'qlsj',
              {rules: [{ required: true, message: '请选择抵押期限'}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7, offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押方式">
          <a-select placeholder="请选择" :disabled="disabled" v-decorator="[ 'dyfs', {rules: [{ required: true, message: '请选择抵押类型'}]} ]">
            <a-select-option value="1">一般抵押</a-select-option>
            <a-select-option value="2">最高债权抵押</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押金额（万元）">
          <a-tooltip
            :trigger="['focus']"
            placement="topLeft"
            overlayClassName="numeric-input"
          >
          <span slot="title" class="numeric-input-title">
            {{titleValue}}
          </span>
          <a-input
            :disabled="disabled"
            @change="onChange"
            @blur="onBlur"
            @focus="onFocus"
            v-decorator="[
              'dyje',
              {rules: [{ required: true, message: '请输入抵押金额'}]}
            ]" />
          </a-tooltip>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="持证方式">
          <a-select placeholder="请选择" :disabled="disabled" v-decorator="[ 'czfs', {rules: [{ required: true, message: '请选择持证方式'}]} ]">
            <a-select-option value="02">分别持证</a-select-option>
            <a-select-option value="01">共同持证</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7, offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="证书个数">
          <a-select placeholder="请选择" :disabled="disabled" v-decorator="[ 'sfhbzs', {rules: [{ required: true, message: '请选择证书个数'}]} ]">
            <a-select-option value="0">每个单元单独发证</a-select-option>
            <a-select-option value="1">多单元一本证</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押评估价值（万元）">
          <a-tooltip
            :trigger="['focus']"
            placement="topLeft"
            overlayClassName="numeric-input"
          >
          <span slot="title" class="numeric-input-title">
            {{titleValue}}
          </span>
            <a-input
              :disabled="disabled"
              @change="onChange"
              @blur="onBlur"
              @focus="onFocus"
              v-decorator="[
              'dypgjz',
              {rules: [{ required: false, message: '请输入抵押评估价值'}]}
            ]" />
          </a-tooltip>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="抵押范围">
          <a-input
            :disabled="disabled"
            v-decorator="[
              'zjjzwdyfw',
              {rules: [{ required: false, message: '抵押范围'}]}
            ]" />
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="最高债权确定事实">
          <a-input
            :disabled="disabled"
            v-decorator="[
              'zgzqqdss',
              {rules: [{ required: false, message: '最高债权确定事实'}]}
            ]" />
        </a-form-item>
      </a-col>

      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="债务人">
          <a-input
            :disabled="disabled"
            v-decorator="[
              'zwr',
              {rules: [{ required: false, message: '债务人'}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row >
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="登记原因">
          <a-textarea  :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" maxLength="200" v-decorator="[
              'djyy',
              {rules: [{ required: false, message: '请输入登记原因', whitespace: false}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row >
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="附记">
          <a-textarea  :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" maxLength="1000" v-decorator="[
              'fj',
              {rules: [{ required: false, message: '请输入附记内容', whitespace: false}]}
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
  import {changeNumMoneyToChinese} from '@/utils/util'

  export default {
    name: "BgQlmessageForm",
    data () {
      return {
        form: this.$form.createForm(this),
        titleValue: ''
      }
    },
    props:['prolsh','disabled'],
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
      },
      onChange (e) {
        const { value } = e.target
        const reg = /^-?(0|[1-9][0-9]*)(\.[0-9]*)?$/
        if ((!isNaN(value) && reg.test(value)) || value === '' || value === '-') {
          this.value = value;
          this.titleValue = changeNumMoneyToChinese(this.value, "元");
        }
      },
      onFocus (e) {
        const { value } = e.target
        const reg = /^-?(0|[1-9][0-9]*)(\.[0-9]*)?$/
        if ((!isNaN(value) && reg.test(value)) || value === '' || value === '-') {
          this.value = value;
          this.titleValue = changeNumMoneyToChinese(this.value, "元");
        }
      },
      onBlur () {
        // this.titleValue = '';
      }
    }
  }
</script>

<style scoped>

</style>