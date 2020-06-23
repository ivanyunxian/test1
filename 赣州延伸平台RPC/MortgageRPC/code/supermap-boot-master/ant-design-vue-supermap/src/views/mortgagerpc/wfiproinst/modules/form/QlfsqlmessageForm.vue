<template>
  <div>
    <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="土地使用权起始时间">
          <a-date-picker
            style="width: 100%"
            format="YYYY-MM-DD"
            disabled
            v-decorator="[
              'ql.qlsj[0]',
              {rules: [{ required: false}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="土地使用权结束时间">
          <a-date-picker
            style="width: 100%"
            disabled
            format="YYYY-MM-DD"
            v-decorator="[
              'ql.qlsj[1]',
              {rules: [{ required: false}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="房地产交易价格">
          <a-input
            style="width: 100%"
            placeholder="请输入价格"
            v-decorator="[
              'ql.qdjg',
              {rules: [{ required: false}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="价格单位">
          <j-dict-select-tag
            disabled
          :triggerChange="true"
           placeholder="请选择价格单位" v-decorator="[ 'ql.zqdw', {rules: [{ required: false}]} ]"
          dictCode="JEDW"/>
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="土地使用权人">
          <a-input
            style="width: 100%"
            placeholder="请输入土地使用权人"
            v-decorator="[
              'ql.tdshyqr',
              {rules: [{ required: false}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="权利类型">
          <j-dict-select-tag
            disabled
           :triggerChange="true"
            placeholder="请选择权力类型"
            v-decorator="[ 'ql.qllx', {rules: [{ required: false}]} ]"
            dictCode="QLLX"/>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <!--<a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="证书版式">
          <j-dict-select-tag
           :triggerChange="true" placeholder="请选择证书版式" v-decorator="[ 'ql.zsbs', {rules: [{ required: true, message: '请选择证书版式'}]} ]"
          dictCode="ZSBS" />
        </a-form-item>
      </a-col>-->
      <a-col :xl="{span: 8}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="持证方式">
          <j-dict-select-tag
          :triggerChange="true"
          placeholder="请选择持证方式" v-decorator="[ 'ql.czfs', {rules: [{ required: true, message: '请选择持证方式'}]} ]"
          dictCode="CZFS"/>
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="合同号">
          <a-input
            disabled
            placeholder="请输入合同号"
            v-decorator="[ 'ql.hth', {rules: [{ required: false}]} ]"
          />
        </a-form-item>
      </a-col>
      <a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">
        <a-form-item
          label="房屋性质">
          <j-dict-select-tag
            disabled
            :triggerChange="true"
            placeholder="请选择房屋性质"
            v-decorator="[ 'fsql.fwxz', {rules: [{ required: false}]} ]"
            dictCode="FWXZ"/>
        </a-form-item>
      </a-col>
      <!--<a-col :xl="{span:7,  offset: 1}" :lg="6" :md="12" :sm="24">-->
        <!--<a-form-item-->
          <!--label="证书个数">-->
          <!--<j-dict-select-tag-->
           <!--:triggerChange="true" placeholder="请输入证书个数" v-decorator="[ 'ql.id_xmxx_zsgs', {rules: [{ required: true, message: '请输入证书个数'}]} ]"-->
            <!--dictCode="ZSGS"/>-->
        <!--</a-form-item>-->
      <!--</a-col>-->
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="登记原因">
          <!--@click="showModalReason('ql.djyy')"-->
          <a-textarea rows="4"
              placeholder="请输入登记原因" :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'ql.djyy',
              {rules: [{ required: false, message: '请输入登记原因', whitespace: false}]}
            ]"
          />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :xl="{span:24}" :lg="18" :md="36" :sm="72">
        <a-form-item
          label="附记">
          <!--@click="showModalReason('ql.fj')"-->
          <a-textarea rows="4"
             placeholder="请输入附记内容" :disabled="disabled"  :autosize="{ minRows: 4, maxRows: 8 }" v-decorator="[
              'ql.fj',
              {rules: [{ required: false, message: '请输入附记内容', whitespace: false}]}
            ]"/>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
    <a-modal
      :title="modalTitle"
      :visible="reasonVisible"
      :maskClosable="false"
      width="900px"
      height="300px"
      @ok="hideModal"
      @cancel="close"
      okText="确认"
      cancelText="取消"
    >
        <div style="height: 600px">
          <a-table :rowSelection="{selectedRowKeys: selectedRowKeys,onChange: onSelectChange,type: 'radio'}"
                   :columns="sfxxcolumns"
                   :pagination="false"
                   :dataSource="djyydata"
                   :scroll="{ y: 500 }"
          />
        </div>
    </a-modal>
  </div>
</template>

<script>
  import {changeNumMoneyToChinese} from '@/utils/util'
  import ACol from "ant-design-vue/es/grid/Col";
  import { mixinDevice } from '@/utils/mixin.js'
  import ATextarea from "ant-design-vue/es/input/TextArea"
  import { getAction, postAction } from '@/api/manage'
  export default {
    name: "QlfsqlmessageForm",
    components: {ATextarea, ACol},
    mixins: [mixinDevice],
    data () {
      return {
        form: this.$form.createForm(this),
        titleValue: '',
        reasonVisible:false,
        isShowModalSpin: false,
        sfxxcolumns: [{
          title: '序号',
          dataIndex: 'id',
          width: 100,
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        }, {
          title: '登记原因',
          dataIndex: 'value'
        }],
        djyydata: [],
        selectedValue:'',
        modalTitle:'',
        selectedKey:'',
        url : {
          djyyList:'/mortgagerpc/sys_config/list'
        },
        selectedRowKeys: []
      }
    },
    created() {
      this.djyyList()
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
      },
      djyyList() {
        getAction(this.url.djyyList).then((res) => {
          if (res.success) {
            this.djyydata = res.result.records
          } else {
          }
        })
      },
      showModalReason (type){
        this.reasonVisible = true;
        this.modalTitle = type == 'ql.fj' ? "附记" : "登记原因"
        this.selectedKey = type;
        var select = this.form.getFieldValue(type)
        this.selectedRowKeys = [];
        if(select){
          for (let i = 0; i < this.djyydata.length; i++) {
            if (this.djyydata[i].value === select) {
              this.selectedRowKeys.push(i)
            }
          }
        }
      },
      onSelectChange(selectedRowKeys,selectedRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectedValue = selectedRows[0].value;
        console.log(selectedRows)
      },
      hideModal() {
        var type = this.selectedKey;
        if(type == 'ql.djyy'){
          this.form.setFieldsValue({ "ql.djyy": this.selectedValue});
        }else{
          this.form.setFieldsValue({ "ql.fj": this.selectedValue});
        }
        this.reasonVisible = false
      },
      close() {
        this.reasonVisible = false
      },

    }
  }
</script>

<style scoped>

</style>