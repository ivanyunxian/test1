<template>
  <div>

      <a-card class="card"  :bordered="false" >
        <h2 class="title">用户信息申请</h2>
        <!-- 操作按钮区域 -->
        <div class="table-operator">
          <a-button @click="handleAdd" type="primary" icon="plus" >新增</a-button>
        </div>
        <!--<div class="table-operator">
          <a-button @click="handleAdd" type="primary" icon="plus" >新增</a-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="batchDel">
                <a-icon type="delete"/>
                删除
              </a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px"> 批量操作
              <a-icon type="down"/>
            </a-button>
          </a-dropdown>
        </div> -->
        <!-- table区域-begin -->
         <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
            <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
            selectedRowKeys.length }}</a>项
            <a style="margin-left: 24px" @click="onClearSelected">清空</a>
          </div>
          -->
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :rowSelection="rowSelection"
            :loading="loading">

            <!-- 字符串超长截取省略号显示-->
            <span slot="templateContent" slot-scope="text">
              <j-ellipsis :value="text" :length="25" />
            </span>


            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>

              <a-divider type="vertical"/>
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                  </a-popconfirm>
            </span>

          </a-table>
      </a-card>
      <!--搜索栏-->
      <a-form @submit="handleSubmit" :form="form">
        <a-card class="card"  :bordered="false" >
          <h2 class="title">宗地</h2>
          <a-row class="form-row" :gutter="24">
            <a-col :md="8" :sm="10">
              <a-form-item
                label="宗地不动产单元号"
                :labelCol="{span: 10}"
                :wrapperCol="{span: 14}"
                class="stepFormText"
              >
                <a-input
                  @click="searchDyh"
                  v-decorator="['bdcdyh', { rules: [{ required: true, message: '请输入宗地不动产单元号!' }] }]"
                />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="10">
              <a-form-item label="权利人名称"
                           :labelCol="{span: 10}"
                           :wrapperCol="{span: 14}"
                           style="width: 100%">
                <a-input
                  placeholder="请输入权利人名称（其中一位权利人即可）"
                  v-decorator="[
                'qlrmc',
                {rules: [{ required: true, message: '请输入权利人名称'}]}
              ]" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="10">
              <a-form-item label="权利人证件号"
                           :labelCol="labelCol"
                           :wrapperCol="wrapperCol"
                           style="width: 100%">
                <a-input
                  placeholder="请输入权利人证件号"
                  v-decorator="[
                'qlrzjh',
                {rules: [{ required: true, message: '请输入权利人证件号'}]}
              ]" />
              </a-form-item>
            </a-col>
            <a-col :md="2" :sm="2">
              <a-form-item>
                <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                 <a-button type="primary" @click="searchQuery" icon="search">核验</a-button>
                </span>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
      <div style="margin-top:-46px;">
      <a-card class="card"  :bordered="false" >
        <a-row>
            <a-col :md="7" :sm="24">
              <template>
                <a-card title="已添加宗地列表" :bordered="true" style="margin-right: 10px">

                  <a-list size="large"
                          ref="list"
                          :loading="houselistloading"
                          :pagination="false"
                  >
                    <a-list-item :key="index" v-for="(item, index) in selectedZddata" @click="chooseZd(item,index)">
                      <div class="ant-alert " style="width: 100%" :class="item.thisselected">
                        <i class="anticon anticon-info-circle ant-alert-icon"></i> <a
                        style="font-weight: 600;color:#1e1e1e">{{++index}}. {{item.zl}}</a>
                      </div>
                    </a-list-item>
                  </a-list>
                  <a-button  v-if="selectedZddata.length>0" class="setButton" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="minus-circle" @click="handleremoved">移除</a-button>
                </a-card>
              </template>
            </a-col>
            <a-col :md="17" :sm="24">
              <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
                selectedRowKeys.length }}</a> 个宗地
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
              </div>

              <a-table
                ref="zdtable"
                size="middle"
                bordered
                rowKey="id"
                :columns="zdcolumns"
                :pagination="ipagination"
                :dataSource="data"
                :loading="zdtableloading"
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                @change="handleTableChange">


              <!--<span slot="action" slot-scope="text, record">
               <a @click="houseData(record)">详情</a>
              </span> -->

              </a-table>
            </a-col>
        </a-row>
      </a-card>
      </div>


      <a-row>
        <a-col :lg="24" :md="24" :sm="24">
          <a-form-item :style="{ textAlign: 'center' }">
            <a-button style="margin-right:20px;" @click="prevStep" type="primary">上一步</a-button>
            <a-button  style="margin-right: 8px" @click="selectzd">添加单元</a-button>
            <a-button type="primary" @click="submitProject">提交审核</a-button>
          </a-form-item>
        </a-col>
      </a-row>
    <userApplyTemplate-modal ref="modalForm" @ok="modalFormOk" :enterpriseid="enterpriseid"></userApplyTemplate-modal>
    <!--宗地不动产单元号弹窗-->
    <ZdbdcListModal ref="zdbdcmodalForm" @selectedDyh="selectedDyh" ></ZdbdcListModal>
  </div>
</template>

<script>
    import UserApplyTemplateModal from './modules/UserApplyTemplateModal'
    import {JeecgListMixin} from '@/mixins/JeecgListMixin'
    import { getAction, postAction } from '@/api/manage'
    import ZdbdcListModal from './modules/ZdbdcListModal'
    import ajaxaciton from '@/api/ajaxAction'

    export default {
        name: "Step2_UserApply",
        mixins: [JeecgListMixin],
        components: {
          UserApplyTemplateModal,
          ZdbdcListModal
        },
        props:['enterpriseid'],
        data () {
          return {
            form: this.$form.createForm(this),
            loading:false,
            zdtableloading:false,
            houselistloading:false,
            selectedZddata:[],//左边单元列表
            labelCol: {
              xs: {span: 24},
              sm: {span: 8},
            },
            wrapperCol: {
              xs: {span: 24},
              sm: {span: 16},
            },
            zdcolumns:[
            {
              title: '序号',
              dataIndex: '',
              key: 'rowIndex',
              width: 60,
              align: "center",
              customRender: function (t, r, index) {
                return parseInt(index) + 1;
              }
            },
            {
              title: '不动产单元号',
              align: "center",
              dataIndex: 'bdcdyh'
            },
            {
              title: '坐落',
              align: "center",
              dataIndex: 'zl'
            },
            {
              title: '查封状态',
              align: "center",
              dataIndex: 'cfzt',
              customRender:function (t,zd,index) {
                return  t == 1 ? '已查封':'无';
              }
            },
            {
              title: '抵押状态',
              align: "center",
              dataIndex: 'dyzt',
              customRender:function (t,zd,index) {
                return  t == 1 ? '已抵押':'无';
              }
            },
            {
              title: '异议状态',
              dataIndex: 'yyzt',
              align: "center",
              customRender:function (t,zd,index) {
                return  t == 1 ? '存在异议':'无';
              }
            },
            {
              title: '宗地面积',
              dataIndex: 'zdmj',
              align: "center",
            }
            ],
            columns: [
              {
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                customRender: function (t, r, index) {
                  return parseInt(index) + 1;
                }
              },
              {
                title: '姓名',
                align: "center",
                dataIndex: 'realname'
              },
              {
                title: '证件号码',
                align: "center",
                dataIndex: 'zjh'
              },
              {
                title: '用户名',
                align: "center",
                dataIndex: 'username',
              },
              {
                title: '手机号码',
                align: "center",
                dataIndex: 'phone',
              },
              {
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: {customRender: 'action'},
              }
            ],
            url: {
              list: "/sys/user/enterprise/list",
              delete: "/sys/user/enterprise/delete",
              deleteBatch: "/message/sysMessageTemplate/deleteBatch",
              exportXlsUrl: "message/sysMessageTemplate/exportXls",
              importExcelUrl: "message/sysMessageTemplate/importExcel",
              getuserbyenterprise:'/sys/user/enterprise/queryuserallbyenterpriseid',
              selectedzdlist : "/modules/yspt/shyqzd/bdc_shyqzd/queryByEnterpriseId",
              removehouse : "/modules/yspt/removehouse",
              zdhislist : "/modules/yspt/shyqzdhis/list",
              zdSearch: "/modules/yspt/zdSearch",
              selectzd : "/modules/yspt/selectzd",
              submitProject: '/modules/yspt/submitProject',
              markasadmin:'/sys/user/enterprise/markasadmin',
            },
            dataSource:[],
            rowSelection: {
              type:'radio',
              selectedRowKeys: [],
              columnTitle:"",
              onChange: this.onUserSelectChange
            },
            selectedUserRowKeys:[],
            data:[],
            ipagination:{
              current: 1,
              pageSize: 5,
              pageSizeOptions: ['10', '20', '30'],
              showTotal: (total, range) => {
                return range[0] + "-" + range[1] + " 共" + total + "条"
              },
              showQuickJumper: true,
              showSizeChanger: true,
              total: 0
            },
          }
        },
        created () {
          console.log("step2:"+this.enterpriseid);
          this.loadData();
          this.loadselectedData();
        },
         props: [ 'enterpriseid'],
        methods: {
          loadData() {
            this.dataSource = [];
            var param ={};
            this.loading = true;
            param.enterpriseid = this.enterpriseid;
            getAction(this.url.getuserbyenterprise, { enterpriseid: this.enterpriseid }).then((res) => {
              if (res.success) {
                this.dataSource = res.result ;
                //单选选中
                console.log(this.dataSource);
                 for(let value of this.dataSource){
                     if(value.zjlx == "admin"){
                          this.rowSelection = {
                            type: 'radio',
                            columnWidth:'10%',
                            columnTitle:"选中为管理员",
                            onChange: this.onUserSelectChange,
                            getCheckboxProps: record => ({
                              props: {
                                //默认选择第一个id
                                defaultChecked: record.id === value.id
                              }
                            })
                          };
                          this.selectedUserRowKeys.push(value.id)
                      }
                 }

              }
              this.loading = false;
            })
          },
          onUserSelectChange(selectedRowKeys){
            let that = this;
            getAction(this.url.markasadmin, { userid: selectedRowKeys[0] }).then((res) => {
              if (res.success) {
                //单选选中 用来判断下一步
                that.selectedUserRowKeys = selectedRowKeys ;
                that.$message.success('成功设置管理员');
                that.rowSelection.selectedRowKeys = Array.from(new Set(that.selectedUserRowKeys));
                that.loadData();
              }
            })
          },
          prevStep() {
            var that = this
            this.$emit('checkstep2',true)
            that.$confirm({
              title: '确认返回？',
              content: '您已保存企业信息，是否重新填写企业信息？',
              onOk: function() {
                that.$emit('prevStep')
              }
            })
          },
          handleSubmit (e) {
            e.preventDefault()
            this.form.validateFields((err, values) => {
              if (!err) {
                //先验证一下手机验证码是否正确
                let formData = values;
              }
            })
          },
          submitProject () {
          var that = this;
            this.$confirm({
                title: '确认提交',
                content: '提交前请确保填写的信息正确，是否确认提交?',
                onOk: function() {
                  console.log(that.dataSource);
                  console.log(that.selectedZddata);
                  if(that.dataSource.length == 0){
                    that.$notification['error']({
                      message: '请先添加人员信息',
                      description: ""
                    })
                    return;
                  }
                  if(that.selectedZddata.length == 0){
                    that.$notification['error']({
                      message: '请先添加宗地',
                      description: ""
                    })
                    return;
                  }
                   ajaxaciton.get(window._CONFIG['domianURL'] + that.url.submitProject, { enterpriseid: that.enterpriseid ,type:1}, function(response) {
                      if (response) {
                        if (response.success) {
                          that.nextStep()
                        } else {
                          that.$message.error(response.message)
                        }
                      } else {
                        that.$message.error(response)
                      }
                      that.confirmLoading = false
                    },
                    function(status) {
                      that.$message.error('网络异常，请求失败')
                    })

                }
                });

          },
          selectzd() {
            var flag = '';
            var that = this;
            var selectzds = this.selectionRows;
            if(selectzds.length==0) {
              this.$message.error('请选择至少一个宗地');
              return ;
            }
            for(var key in selectzds) {
              var zd = selectzds[key];
              if(zd.cfzt == 1) {
                flag = "已查封";
              } else if(zd.dyzt == 1) {
                flag = "已抵押";
              } else if(zd.yyzt == 1) {
                flag = "存在异议";
              }
            }
            // if(flag) {
            //   this.$confirm({
            //     title:"确认添加",
            //     content:"选择的单元中包含有"+flag+"的单元，是否确定继续添加?",
            //     onOk: function(){
            //       that.addHouse(selectzds);
            //     }
            //   });
            // } else {
              that.addZd(selectzds);
            // }


          },
          addZd(zdlist) {
            var param  = {};
            param.zdlist = zdlist;
            param.enterpriseid = this.enterpriseid;
            postAction(this.url.selectzd, param).then((res) => {
              if(res.success) {
                this.loadselectedData();
                this.loadZdData();
                this.$notification['success']({
                  message: res.message
                })

              } else {
                this.$notification['error']({
                  message: res.message
                })
              }
            })
          },
          searchQuery(){
            var that =  this;
            this.form.validateFields((err, values) => {
              if (!err) {
                this.zdtableloading = true;
                values = Object.assign(values,{enterpriseid:this.enterpriseid});
                getAction(this.url.zdSearch, values).then((res) => {
                  if (res.success) {
                    this.loadZdData();
                  } else {
                    that.$message.error(res.message);
                  }
                  this.zdtableloading = false;
                })
              }
            })
          },
          loadselectedData() {
            getAction(this.url.selectedzdlist, {enterpriseid:this.enterpriseid}).then((res) => {
              if(res.success) {
                this.selectedZddata = res.result;
              } else {
                this.$message.error(res.message);
              }
            })
          },
          chooseZd(item,index) {
            this.selectedzds(index-1);
          },
          //第几个房屋被选择
          selectedzds(index) {
            var newhousedata = [];
            for(var key in this.selectedZddata) {
              if(key == index) {
                //改变该单元的样式
                this.selectedZddata[key].thisselected = 'ant-alert-info';
                this.zddetail = this.selectedZddata[key];
              } else {
                this.selectedZddata[key].thisselected = '';
              }
              newhousedata.push(this.selectedZddata[key]);
            }
            this.selectedZddata = newhousedata;
          },
          handleremoved() {
            var that = this;
            if(this.zddetail.id) {
              getAction(this.url.removehouse, {zdid:this.zddetail.id,enterpriseid:this.enterpriseid}).then((res) => {
                if(res.success) {
                  this.$message.success('操作成功');
                  this.loadZdData();
                  this.loadselectedData();
                } else {
                  that.$message.error(res.message);
                }
              })
            } else {
              this.$message.error('请选择需要移除的单元');
            }
          },
          handleTableChange(pagination, filters, sorter) {
            //分页、排序、筛选变化时触发
            this.ipagination = pagination;
            this.loadZdData();
          },
          loadZdData(arg){
            if(arg){
              this.ipagination.current = arg;
            }
            var param = {enterpriseid:this.enterpriseid,zdzt:1};
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            getAction(this.url.zdhislist, param).then((zdhislistres) => {
              if(zdhislistres.success) {
                this.data = zdhislistres.result.records;
                this.ipagination.total = zdhislistres.result.total;
                this.onClearSelected();
                if(this.data.length>0){
                  this.actionshow = true;
                }
              } else {
                this.$message.error(zdhislistres.message);
              }
            })
          },
          houseData(record) {
            // this.$refs.modalForm.initData(record);
            // this.$refs.modalForm.visible = true;
            // this.$refs.modalForm.title = "房源详情";
          },
          searchDyh() {
            this.$refs.zdbdcmodalForm.visible = true;
          },
          selectedDyh(bdcdyh) {
            console.log(bdcdyh)
            this.form.setFieldsValue({bdcdyh:bdcdyh})
          },
          nextStep() {
            let that = this
            that.$emit('nextStep')
          },
        }
    }
</script>

<style scoped>
  .title{
    text-align:center;
    /*background: rgba(242, 242, 242, 1);*/
    /*padding:15px;*/
  }

</style>