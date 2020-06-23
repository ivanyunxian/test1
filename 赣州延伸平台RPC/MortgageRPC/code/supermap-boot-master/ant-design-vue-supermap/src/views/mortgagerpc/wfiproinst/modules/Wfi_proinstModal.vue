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
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流程编号">
          <a-input placeholder="请输入流程编号" v-decorator="['prodefId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="员工编号">
          <a-input placeholder="请输入员工编号" v-decorator="['userId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="区划代码">
          <a-input placeholder="请输入区划代码" v-decorator="['divisionCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="区划名称">
          <a-input placeholder="请输入区划名称" v-decorator="['divisionName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流水号 外网流水号">
          <a-input placeholder="请输入流水号 外网流水号" v-decorator="['prolsh', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流水号 内网创建项目生成的流水号">
          <a-input placeholder="请输入流水号 内网创建项目生成的流水号" v-decorator="['lsh', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="业务类型（0:个人，1:企业）">
          <a-input placeholder="请输入业务类型（0:个人，1:企业）" v-decorator="['ywlx', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="登记类型 登记类型字典">
          <a-input placeholder="请输入登记类型 登记类型字典" v-decorator="['djlx', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="权利类型 权利类型字典">
          <a-input placeholder="请输入权利类型 权利类型字典" v-decorator="['qllx', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="项目类型 用于区分不同的需求，12：通用的，11：鹰潭">
          <a-input placeholder="请输入项目类型 用于区分不同的需求，12：通用的，11：鹰潭" v-decorator="['xmlx', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="审核状态 -1：拟申请未提交到前置机，0：未审核，10：权籍调查审核通过，11：权籍调查审核驳回，20：项目受理审核通过，21：项目受理审核驳回">
          <a-input placeholder="请输入审核状态 -1：拟申请未提交到前置机，0：未审核，10：权籍调查审核通过，11：权籍调查审核驳回，20：项目受理审核通过，21：项目受理审核驳回" v-decorator="['shzt', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="内网库流程ID 受理项目对应到内网库的流程ID">
          <a-input placeholder="请输入内网库流程ID 受理项目对应到内网库的流程ID" v-decorator="['wfProdefid', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="内网流程名称 对应到内网的流程名称">
          <a-input placeholder="请输入内网流程名称 对应到内网的流程名称" v-decorator="['wfProdefname', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流程名称">
          <a-input placeholder="请输入流程名称" v-decorator="['prodefName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="项目名称">
          <a-input placeholder="请输入项目名称" v-decorator="['projectName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="实例办理方式">
          <a-input placeholder="请输入实例办理方式" v-decorator="['transationType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流程编码">
          <a-input placeholder="请输入流程编码" v-decorator="['proinstCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="起始时间">
          <a-date-picker v-decorator="[ 'proinstStart', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="结束时间">
          <a-date-picker v-decorator="[ 'proinstEnd', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流程实例状态">
          <a-input placeholder="请输入流程实例状态" v-decorator="['proinstStatus', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="受理人员">
          <a-input placeholder="请输入受理人员" v-decorator="['acceptor', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="员工地区ID">
          <a-input placeholder="请输入员工地区ID" v-decorator="['staffDistid', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="流程实例类型">
          <a-input placeholder="请输入流程实例类型" v-decorator="['instanceType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="发送消息状态 0：不需要发送消息，1：需要推送消息">
          <a-input placeholder="请输入发送消息状态 0：不需要发送消息，1：需要推送消息" v-decorator="['sendMsg', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="项目备注">
          <a-input placeholder="请输入项目备注" v-decorator="['remarks', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="修改时间">
          <a-date-picker v-decorator="[ 'modifytime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="版本号">
          <a-input placeholder="请输入版本号" v-decorator="['versionno', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "Wfi_proinstModal",
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
        proinstId:{rules: [{ required: true, message: '请输入流程实例ID!' }]},
        },
        url: {
          add: "/modules/wfi_proinst/add",
          edit: "/modules/wfi_proinst/edit",
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
          this.form.setFieldsValue(pick(this.model,'proinstId','prodefId','userId','divisionCode','divisionName','prolsh','lsh','ywlx','djlx','qllx','xmlx','shzt','wfProdefid','wfProdefname','prodefName','projectName','transationType','proinstCode','proinstStatus','acceptor','staffDistid','instanceType','sendMsg','remarks','versionno'))
		  //时间格式化
          this.form.setFieldsValue({proinstStart:this.model.proinstStart?moment(this.model.proinstStart):null})
          this.form.setFieldsValue({proinstEnd:this.model.proinstEnd?moment(this.model.proinstEnd):null})
          this.form.setFieldsValue({modifytime:this.model.modifytime?moment(this.model.modifytime):null})
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
            if(!this.model.proinstId){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.proinstStart = formData.proinstStart?formData.proinstStart.format():null;
            formData.proinstEnd = formData.proinstEnd?formData.proinstEnd.format():null;
            formData.modifytime = formData.modifytime?formData.modifytime.format():null;
            
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
      },


    }
  }
</script>

<style lang="less" scoped>

</style>