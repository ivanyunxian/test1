<template>
  <a-modal
    :title="title"
    :width="800"
    :ok=false
    :visible="visible"
    :confirmLoading="confirmLoading"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="部门类型">
          <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['deptType', validatorRules.deptType]"
                             placeholder="请选择部门类型" :triggerChange="true" dictCode="DEPT_TYPE"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="机构区划">
          <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['divisionCode', validatorRules.divisionCode]"
                             placeholder="请选择机构区划" :triggerChange="true" dictCode="DIVISION_CODE"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="机构名称"
          :hidden="false"
          hasFeedback >
          <a-input id="departName" placeholder="请输入机构/部门名称" v-decorator="['departName', validatorRules.departName ]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :hidden="seen" label="上级部门" hasFeedback>
        <a-tree-select
          style="width:100%"
          :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
          :treeData="departTree"
          v-model="model.parentId"
          placeholder="请选择上级部门"
          :disabled="condition">
        </a-tree-select>
        </a-form-item>
        <a-form-item label="证件类型" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-select placeholder="请选择部门证件类型" v-decorator="[ 'deptZjlx', validatorRules.deptZjlx]" >
            <a-select-option value="6">组织机构代码</a-select-option>
            <a-select-option value="7">营业执照</a-select-option>
            <a-select-option value="99">其他</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="证件号" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-input placeholder="请输入部门组织机构代码或营业执照" v-decorator="[ 'deptZjh', validatorRules.deptZjh]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="联系方式">
          <a-input placeholder="请输入部门联系方式" v-decorator="['mobile',validatorRules.mobile]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="传真">
          <a-input placeholder="请输入传真" v-decorator="['fax', {}]"  />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="地址">
          <a-input placeholder="请输入地址" v-decorator="['address', {}]"  />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序">
          <a-input-number v-decorator="[ 'departOrder',{'initialValue':0}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-textarea placeholder="请输入备注" v-decorator="['memo', {}]"  />
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import { queryIdTree } from '@/api/api'
  import pick from 'lodash.pick'
  import ATextarea from 'ant-design-vue/es/input/TextArea'
  export default {
    name: "SysDepartModal",
    components: { ATextarea },
    data () {
      return {
        departTree:[],
        orgTypeData:[],
        phoneWarning:'',
        departName:"",
        title:"操作",
        seen:false,
        visible: false,
        condition:true,
        disableSubmit:false,
        model: {},
        menuhidden:false,
        menuusing:true,
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
          deptType: {rules: [{required: true, message: '请选择部门类型!'}]},
          divisionCode: {rules: [{required: true, message: '请选择机构区划!'}]},
          departName:{rules: [{ required: true, message: '请输入机构/部门名称!' }]},
          orgCode:{rules: [{ required: true, message: '请输入机构编码!' }]},
          deptZjlx: {rules: [{required: true, message: '请选择部门证件类型!'}]},
          deptZjh: {rules: [{required: true, message: '请输入部门组织机构代码或营业执照!'}]},
          mobile: {rules: [{required: true, message: '请输入部门联系方式!'}]}
        },
        url: {
          add: "/sysdepart/sysDepart/add",
        },
      }
    },
    created () {
    },
    methods: {
      loadTreeData(){
        var that = this;
        queryIdTree().then((res)=>{
          if(res.success){
            that.departTree = [];
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i];
              that.departTree.push(temp);
            }
          }

        })
      },
      add (depart) {
        if(depart){
          this.seen = false;
        }else{
          this.seen = true;
        }
        this.edit(depart);
      },
      edit (record) {
          this.form.resetFields();
          this.model = Object.assign({}, {});
          this.visible = true;
          this.loadTreeData();
          this.model.parentId = record!=null?record.toString():null;
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'deptZjlx','deptZjh','deptType','divisionCode','departName','departNameEn','departNameAbbr','departOrder','description','orgType','orgCode','mobile','fax','address','memo','status','delFlag'))
          });
      },
      close () {
        this.$emit('close');
        this.disableSubmit = false;
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);
            //时间格式化
            console.log(formData)
            httpAction(this.url.add,formData,"post").then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.loadTreeData();
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
      validateMobile(rule,value,callback){
        if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)){
          callback();
        }else{
          callback("您的手机号码格式不正确!");
        }

      },
      handleChangeRuleCondition(val){
        if(val=='USE_SQL_RULES'){
          this.form.setFieldsValue({
            ruleColumn:''
          })
          this.showRuleColumn = false
        }else{
          this.showRuleColumn = true
        }
      }
    }
  }
</script>

<style scoped>

</style>