<template>
  <a-modal
    :title="title"
    :width="1400"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading" >
      <a-form :form="form">
        <a-Row>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="区划代码">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['divisionCode', validatorRules.divisionCode]"
                                 placeholder="请选择区划代码" :triggerChange="true" dictCode="DIVISION_CODE"/>
            </a-form-item>
          </a-Col>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="流程编码">
              <a-input placeholder="请输入流程编码"   v-decorator="['prodefCode', validatorRules.prodefCode]" />
            </a-form-item>
          </a-Col>
        </a-Row>

        <a-Row>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="流程名称">
              <a-input placeholder="请输入流程名称"   v-decorator="['prodefName', validatorRules.prodefName]" />
            </a-form-item>
          </a-Col>

          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="业务类型">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['prodefclassId', validatorRules.prodefclassId]"
                                 placeholder="请选择业务类型" :triggerChange="true" dictCode="LCLX"/>
            </a-form-item>
          </a-Col>

        </a-Row>

        <a-Row>

          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="登记类型">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['djlx', validatorRules.djlx]"
                                 placeholder="请选择登记类型" :triggerChange="true" dictCode="DJLX"/>
            </a-form-item>
          </a-Col>

          <a-Col :span="12">

            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="不动产单元类型">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['dylx', validatorRules.dylx]"
                                 placeholder="请选择单元类型" :triggerChange="true" dictCode="BDCDYLX"/>
            </a-form-item>

          </a-Col>

        </a-Row>

        <a-Row>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="权利类型">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['qllx', validatorRules.qllx]"
                                 placeholder="请选择权利类型" :triggerChange="true" dictCode="QLLX"/>
            </a-form-item>
          </a-Col>
          <a-Col :span="12">
          <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否即时时办理">
          <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['sfjsbl', validatorRules.sfjsbl]"
                             placeholder="请选择是否即时时办理" :triggerChange="true" dictCode="yn"/>
        </a-form-item>

          </a-Col>


        </a-Row>

        <a-Row>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="是否启用">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['sfqy', validatorRules.sfqy]"
                                 placeholder="请选择是否启用" :triggerChange="true" dictCode="yn"/>
            </a-form-item>
          </a-Col>

          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="流程描述">
              <a-input placeholder="请输入流程描述" v-decorator="['prodefDesc', {}]" />
            </a-form-item>
          </a-Col>

        </a-Row>

        <a-Row>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="是否需要权利人">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['qlrflage', validatorRules.qlrflage]"
                                 placeholder="是否需要权利人" :triggerChange="true" dictCode="yn"/>
            </a-form-item>
          </a-Col>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="是否需要义务人">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['ywrflage', validatorRules.ywrflage]"
                                 placeholder="是否需要义务人" :triggerChange="true" dictCode="yn"/>
            </a-form-item>
          </a-Col>
        </a-Row>
        <a-Row>
          <a-Col :span="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="是否需要抵押权人">
              <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['dyqrflage', validatorRules.dyqrflage]"
                                 placeholder="是否需要抵押权人" :triggerChange="true" dictCode="yn"/>
            </a-form-item>
          </a-Col>
        </a-Row>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "Wfi_prodefModal",
    data () {
      return {
        title:"操作",
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
          dylx:{rules: [{ required: true, message: '请选择单元类型!' }]},
          sfjsbl:{rules: [{ required: true, message: '请选择是否即时办结!' }]},
          sfqy:{rules: [{ required: true, message: '请选择是否启用!' }]},
          djlx:{rules: [{ required: true, message: '请选择权利类型!' }]},
          sfjsbl:{rules: [{ required: true, message: '请选择是否即时办结!' }]},
          qllx:{rules: [{ required: true, message: '请选择权利类型!' }]},
          divisionCode:{rules: [{ required: true, message: '请选择区划代码!' }]},
          prodefclassId:{rules: [{ required: true, message: '请选择业务类型!' }]},
          prodefName:{rules: [{ required: true, message: '请输入流程名称!' }]},
          prodefCode:{rules: [{ required: true, message: '请输入流程编码!' }]},
          qlrflage:{rules: [{ required: true, message: '请选择需要权利人!' }]},
          ywrflage:{rules: [{ required: true, message: '请选择需要义务人!' }]},
          dyqrflage:{rules: [{ required: true, message: '请选择是否需要抵押权人!' }]},
        },
        url: {
          list: '/sys/dictItem/list',
          add: "/modules/wfi_prodef/add",
          edit: "/modules/wfi_prodef/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'prodefId','prodefclassId',
            'divisionCode','prodefCode','prodefName','prodefStatus','operationType','dylx','djlx','qllx','yhlx','sfjsbl',
            'sfqy','prodefIndex','prodefDesc','prodefTpl','templateurl','formurl','sysm','ycxgzsh','qlrflage','ywrflage','dyqrflage'))
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
            if(!this.model.prodefId){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            
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
      },handleChangeRuleCondition(val){
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

<style lang="less" scoped>

</style>