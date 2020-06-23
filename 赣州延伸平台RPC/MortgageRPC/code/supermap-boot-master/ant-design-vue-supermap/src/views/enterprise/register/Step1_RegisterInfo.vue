<template>
    <div>
        <a-form @submit="handleSubmit" :form="form" >
          <a-card class="card"  :bordered="false">
            <h2 style="text-align:center;">填写企业注册信息</h2>
            <a-row class="form-row" :gutter="16">
              <a-col :span="12">
                <a-form-item
                  label="企业名称"
                  :labelCol="{span: 6}"
                  :wrapperCol="{span: 18}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['enterpriseName', { rules: [{ required: true, message: '请输入企业名称!' },{ validator: this.checkUsername }] ,validateTrigger: ['change', 'blur']}]"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="统一社会信用代码"
                  :labelCol="{span: 8}"
                  :wrapperCol="{span: 16}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['enterpriseCode', { rules: [{ required: true, message: '请输入统一社会信用代码!' }] }]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row">
              <a-col :span="12">
                <a-form-item
                  label="企业地址"
                  :labelCol="{span: 6}"
                  :wrapperCol="{span: 18}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['enterpriseAddress', { rules: [{ required: true, message: '请输入企业地址!' }] }]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="16">
              <a-col :span="12">
                <a-form-item
                  label="法人代表姓名"
                  :labelCol="{span: 6}"
                  :wrapperCol="{span: 18}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['frdbxm', { rules: [{ required: true, message: '请输入法人代表姓名!' }] }]"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="法人代表身份证号码"
                  :labelCol="{span: 8}"
                  :wrapperCol="{span: 16}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['frdbzjhm', { rules: [{
                      required: true,
                      pattern: '^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$',
                      message: '身份证号格式不对!'
                  }] }]"
                  />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item
                  label="注册者姓名"
                  :labelCol="{span: 6}"
                  :wrapperCol="{span: 18}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['registerName', { rules: [{ required: true, message: '请输入注册者姓名!' }] }]"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                label="注册者身份证号码"
                :labelCol="{span: 8}"
                :wrapperCol="{span: 16}"
                class="stepFormText"
              >
                <a-input
                  v-decorator="['registerZjhm', {
                    rules: [{
                      required: true,
                      pattern: '^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$',
                      message: '身份证号格式不对!'
                  }] }]"
                />
              </a-form-item>
            </a-col>

              <a-col :span="9">
                <a-form-item
                  label="注册者手机号码"
                  :labelCol="{span: 8}"
                  :wrapperCol="{span: 16}"
                  class="stepFormText"
                >
                  <a-input
                  :disabled="phonedisable"
                    v-decorator="['registerPhone', { rules:
                    [{ required: true, pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号' },
                     { validator: this.handlePhoneCheck } ], validateTrigger: ['change', 'blur'] }]"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="3">
                <a-form-item>
                  <a-button type="primary" :disabled="yzmDisabled" @click="handleYZM">{{yzmBtnText}}</a-button>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="验证码"
                  :labelCol="{span: 8}"
                  :wrapperCol="{span: 16}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['code', { rules: [{ required: true, message: '请输入验证码!' }] }]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <!--<a-row class="form-row">
              <a-col :lg="12" :md="12" :sm="12">
                <a-form-item
                  label="注册者手机号码"
                  :labelCol="{span: 6}"
                  :wrapperCol="{span: 18}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['registerPhone', { rules:
                    [{ required: true, pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号' },
                     { validator: this.handlePhoneCheck } ], validateTrigger: ['change', 'blur'] }]"
                  />
                </a-form-item>
              </a-col>
             &lt;!&ndash; <a-col :lg="2" :md="2" :sm="12">
                <a-form-item>
                  <a-button type="primary" :disabled="yzmDisabled" @click="handleYZM">{{yzmBtnText}}</a-button>
                </a-form-item>
              </a-col>&ndash;&gt;
              <a-col :lg="12" :md="12" :sm="24">
                <a-form-item
                  label="验证证码"
                  :labelCol="{span: 8}"
                  :wrapperCol="{span: 16}"
                  class="stepFormText"
                >
                  <a-input
                    v-decorator="['code', { rules: [{ required: true, message: '请输入验证码!' }] }]"
                  />
                </a-form-item>
              </a-col>
            </a-row>-->
          </a-card>
          <a-card class="card"  :bordered="false">
            <h2 style="text-align:center;">附件资料上传</h2>
            <!-- <a-table
              ref="table"
              size="small"
              bordered
              rowKey="id"
              :columns="materlistcolnums"
              :dataSource="materclassdata"
              :pagination="false"
            >
          <span slot="action" slot-scope="text, record">
                  <a-upload
                    name="file"
                    class="avatar-uploader"
                    :multiple="true"
                  >
                    <a>上传</a>
                  </a-upload>
                  <a-divider type="vertical"/>
                   <a >预览</a>
                   <a-divider type="vertical" />
                    <a >删除</a>
                </span>
            </a-table>-->
            <div>
                <a-table
                  ref="table"
                  size="middle"
                  bordered
                  rowKey="id"
                  :columns="materlistcolnums"
                  :dataSource="materclassdata"
                  :pagination="false"
                  :loading="loadingfj"
                >

              <span slot="action" slot-scope="text, record">
                <!--<a @click="getEDM(record)">采集</a>
                 <a-divider type="vertical"/>-->
               <a-upload
                 :disabled="!disabled"
                 class="avatar-uploader"
                 :showUploadList="false"
                 :multiple="true"
                 :action="uploadAction"
                 :supportServerRender="false"
                 :data="Object.assign(record,{materdataindex: longtime})"
                 :beforeUpload="beforeUpload"
                 @change="handleChange"
               >
                    <a>上传</a>
                <a-divider type="vertical"/>
               </a-upload>
                 <a @click="showfiles(record)">预览</a>
                 <a-divider type="vertical" />
                 <a :disabled="!disabled" @click="removeAll(record)">清空</a>
              </span>
                  <template slot="customRenderStatus" slot-scope="required">
                    <a-tag v-if="required==0" color="orange">否</a-tag>
                    <a-tag v-if="required==1" color="red">是</a-tag>
                  </template>
                </a-table>
              </div>
              <imag-preview ref="imagpreview" :materclass="materclass" @loadmaterclasslist="loadmaterclasslist"
                                            :disabled="disabled"/>
          </a-card>
          <a-card class="card"  :bordered="false">
           <a-form-item :style="{ textAlign: 'center' }">
             <a-button style="margin-right:20px;" type="primary" htmlType="submit">保存</a-button>
              <a-button type="primary" @click="next">下一步</a-button>
            </a-form-item>
          </a-card>
        </a-form>
    </div>
</template>

<script>
    import { getAction, postAction } from '@/api/manage'
    import { checkOnlyEnterprise } from '@/api/api'
    import ImagPreview from '@/views/mortgagerpc/acceptProject/modalForm/ImagPreview'
    //import BlankLayout from '@/components/layouts/BlankLayout'

    export default {
      name: "Step1_RegisterInfo",
      components:{
        ImagPreview,
        //BlankLayout
      },
      mixins: [],
      data () {
        return {
          form: this.$form.createForm(this),
          formLayout: 'horizontal',
          loadingfj:false,
          qydata:{},
          loadingfj:false,
          materclassdata:[],
          materclass:{},
          longtime: '',
          yzmDisabled:false,
          yzmBtnText:'获取验证码',
          phonedisable:false,
          materlistcolnums: [{
            title: '目录序号',
            align: 'center',
            dataIndex: 'id',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
            {
              title: '申请材料清单',
              align: 'center',
              dataIndex: 'name'
            },
            {
              title: '备注',
              align: 'center',
              dataIndex: 'matedesc'
            },
            {
              title: '必要材料',
              align: 'center',
              dataIndex: 'required',
              scopedSlots: { customRender: 'customRenderStatus' },
              //filterMultiple: false,
              /*filters: [
                { text: '否', value: '0' },
                { text: '是', value: '1' }
              ]*/
            },
            {
              title: '文件数',
              align: 'center',
              dataIndex: 'count',
              customRender: function(t, r, index) {
                return t ? t : 0
              }
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: 'center',
              scopedSlots: { customRender: 'action' }
            }],
          saveflag:false,
          url : {
           yzmurl:'/yspt/enterprise/sendtextmsg',
           verifyurl:'/yspt/enterprise/verifytextcode',
           saveEnterpriseurl:'/yspt/enterprise/register',
           getEnterpriseurl:'/yspt/enterprise/getenterprise',
           getMaterlist:'/mongofile/getMaterlistEnterprise',
           genarateMaterlist:'/mongofile/genarateMater',
           removeAll:'/mongofile/removeAll',
          },
        }
      },
      computed: {

      },
      created(){
       this.longtime = new Date().getTime()
        if(this.enterpriseid!="" && this.enterpriseid!=undefined ){
          this.enterpriseid = this.enterpriseid;
        }else{
          this.enterpriseid = this.getuuid();
        }
        console.log("step1:"+this.enterpriseid);
        console.log("step1 step:"+this.checkstep2);
        if(this.checkstep2){
          this.saveflag = true;
        }
        this.loadPageData();
        this.loadFj();

      },
      props: [ 'disabled','enterpriseid','checkstep2'],
      methods: {
        getuuid() {
              var s = [];
              var hexDigits = "0123456789abcdef";
              for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
              }
              s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
              s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
              s[8] = s[13] = s[18] = s[23] = "-";

              var uuid = s.join("");
              return uuid;
        },
        /*文件上传*/
        removeAll(record) {
          var that = this
          this.$confirm({
            title: '确认清空？',
            content: '此操作将清空该目录下所有附件，是否确定清空?',
            onOk: function() {
              getAction(that.url.removeAll, { id: record.id }).then((res) => {
                if (res.success) {
                  that.$message.success('清空成功')
                  that.loadmaterclasslist(true)
                } else {
                  that.$message.warning(res.message)
                }
              })

            }
          })
        },
        handlePhoneCheck(rule, value, callback) {
          var params = {
            phone: value,
          };
          if(this.editable){
            checkOnlyUser(params).then((res) => {
              if (res.success) {
                callback()
              } else {
                callback("手机号已存在!")
              }
            })
          }else{
            callback();
          }

        },
        uploadAction: function() {
          //获取地址
          return window._CONFIG['domianURL'] + '/mongofile/upload'
        },
        getFileUploadData: function() {
          /*获取文件参数*/
        },
        beforeUpload: function(file) {
          var fileType = file.type
          if (fileType.indexOf('image') < 0&&fileType.indexOf('pdf')<0) {
            this.$message.warning('请上传图片')
            return false
          }
        },
        handleChange(info) {
          this.picUrl = ''
          if (info.file.status === 'uploading') {
            this.fileindex++
            this.longtime = this.longtime + this.fileindex
            this.uploadLoading = true
            return
          }
          if (info.file.status === 'done') {
            var response = info.file.response
            this.uploadLoading = false
            if (response.success) {
              this.loadmaterclasslist(false)
            } else {
              this.$message.warning(response.message)
            }
          }
        },
       loadmaterclasslist(nomessage) {
          getAction(this.url.getMaterlist, { prolsh: this.enterpriseid }).then((res) => {
            if (res.success) {
              this.materclassdata = res.result
              if (!nomessage) {
                this.$message.success('上传成功')
              }
            } else {
              this.$message.warning(res.message)
            }
          })
        },
        showfiles(record) {
          this.$nextTick(() => {
            this.materclass = record
            this.$refs.imagpreview.loadFileData(record.name)
          })
        },
        loadPageData(){
          getAction(this.url.getEnterpriseurl, { id: this.enterpriseid }).then((res) => {
            if (res.success) {
              this.qydata = res.result;
              this.form.setFieldsValue(this.qydata);
            } else {

            }
          })
        },
        loadFj(){
          //this.uuid;
          this.loadingfj = true
          getAction(this.url.genarateMaterlist, { prolsh: this.enterpriseid }).then((res) => {
            if (res.success) {
              this.loadmaterclasslist(true);
            } else {

            }
            this.loadingfj = false;
          })
        },
        next(){
          //this.$emit('nextStep')
          let that = this;
          for(let value of this.materclassdata){
               if(value.required == "1" && value.count <=0){
                     that.$notification['error']({
                       message:'请上传页面的'+value.name+'材料！'
                     })
                     return;
                }
           }
          this.form.validateFields((err, values) => {
            if (!err) {
              //先验证一下手机验证码是否正确
              let formData = values;
              that.verifyCode(formData,'next')
            }
          })
          /*if(this.saveflag){
            this.$emit('nextStep')
          }else{
            this.$notification['error']({
              message:'请先保存页面！'
            })
          }*/
        },
        checkUsername(rule, value, callback) {
          var that = this;
          var params = {
            enterpriseName: value,
          };
          if(this.qydata == null){//页面未经保存 要检查 一般不会出现此问题
              checkOnlyEnterprise(params).then((res) => {
                if (res.success) {
                  callback()
                } else {
                  if(that.form.getFieldValue("enterpriseName") != undefined){
                    callback("企业名已注册!")
                  }else{
                    callback()
                  }
                }
              })
          }else{
            if(!this.checkstep2){//第二步上一步的情况下 是不需要再次验证的
                checkOnlyEnterprise(params).then((res) => {
                  if (res.success) {
                    callback()
                  } else {
                    callback("企业名已注册!")
                  }
                })
              }else{
                 callback();
              }
          }


        },
        handleSubmit (e) {
          e.preventDefault()
          this.form.validateFields((err, values) => {
            if (!err) {
              //先验证一下手机验证码是否正确
              let formData = values;
              this.verifyCode(formData)
            }
          })
        },
        handleYZM(){
          var param = {};
          this.yzmDisabled = true;
          this.phonedisable = true;
          param.phone = this.form.getFieldValue("registerPhone");
          if(param.phone == "" || param.phone ==undefined){
            this.$message.error('请填写注册者手机号');
            return;
          }
          postAction(this.url.yzmurl, param).then((res) => {
            if(res.success) {
              this.$message.success('验证码发送成功');
            } else {
              this.$notification['error']({
                message: res.message
              })
            }
            //验证码发送
            let longtime =30;
            let that=this
            let a = setInterval(function() {
              if (longtime == 1) {
                that.yzmBtnText = "发送验证码";
                that.yzmDisabled = false;
                clearInterval(a);
                that.phonedisable = false;
              } else {
                longtime--;
                console.log(longtime);
                that.yzmBtnText = `重新发送(${longtime})`;
              }
            }, 1000);
          })
        },
        verifyCode(formData,type){
          var param = {};
          param.code = this.form.getFieldValue("code");
          postAction(this.url.verifyurl, param).then((res) => {
            if(res.success) {
               this.saveEnterprise(formData,type);
            } else {
              this.$notification['error']({
                message: res.message
              })
               //this.saveEnterprise(formData);
              return ;
            }

          })
        },
        saveEnterprise(formData,type){
           console.log(formData);
           formData.id = this.enterpriseid;
            postAction(this.url.saveEnterpriseurl, formData).then((res) => {
              if (res.success) {
                var result = res.result ;
                this.saveflag = true;
                this.$message.success('保存成功');
                if(type=="next"){
                  this.$emit('nextStep')
                }else{
                }
                 //this.$emit('nextStep')
              }else{
                 this.$notification['error']({
                    message: res.message
                  })
              }
            })
        }
      }
    }
</script>

<style lang="scss">
  table tbody tr:hover>td {
    background-color:#ffffff!important
  }
  .avatar-uploader{
    display:inline-block;
  }
</style>