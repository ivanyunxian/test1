<template>
  <a-card style="margin-top: 50px;">
    <div>
      <a-form  :form="form" class="form">
        <a-row :gutter="24">
          <a-col :md="10" :sm="10">
            <a-form-item label="合同号"
                         :labelCol="labelCol"
                         :wrapperCol="wrapperCol"
                         style="width: 100%"
            >
              <!--@click="searchQzh"-->
              <a-input
                placeholder="请输入合同号"
                v-decorator="[
              'bdcqzh',
              {rules: [{ required: true, message: '请输入合同号'}]}
            ]" />
            </a-form-item>
          </a-col>
          <!--<a-col :md="7" :sm="10">
            <a-form-item label="权利人名称"
                         :labelCol="labelCol"
                         :wrapperCol="wrapperCol"
                         style="width: 100%">
              <a-input
                placeholder="请输入权利人名称（其中一位权利人即可）"
                v-decorator="[
              'qlrmc',
              {rules: [{ required: true, message: '请输入权利人名称'}]}
            ]" />
            </a-form-item>
          </a-col>-->
          <a-col :md="10" :sm="10">
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
          <a-col :md="4" :sm="4">
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
             <a-button type="primary" @click="searchQuery" icon="search">核验</a-button>
          </span>

          <span style="float: left;overflow: hidden;margin-left: 15px" class="table-page-search-submitButtons">
             <a-button type="primary" @click="loadidcard" icon="search">读卡</a-button>
          </span>



          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-row>
      <div style="margin-top: 30px  ">
        <a-col :md="7" :sm="24">
          <template>
            <a-card title="已添加单元列表" :bordered="true" style="margin-right: 10px">

              <a-list size="large"
                      ref="list"
                      :loading="houselistloading"
                      :pagination="false"
                      style="height:600px;overflow-y:auto;"
              >
                <a-list-item :key="index" v-for="(item, index) in selectedHousedata" @click="chooseHouse(item,index)">
                  <div class="ant-alert " style="width: 100%" :class="item.thisselected">
                    <i class="anticon anticon-info-circle ant-alert-icon"></i> <a
                    style="font-weight: 600;color:#1e1e1e">{{++index}}. {{item.zl}}</a>
                  </div>
                </a-list-item>
              </a-list>
              <a-button  v-if="selectedHousedata.length>0" class="setButton" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="minus-circle" @click="handleremoved">移除</a-button>
            </a-card>
          </template>
        </a-col>
        <a-col :md="17" :sm="24">
          <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
            <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
            selectedRowKeys.length }}</a> 个单元
            <a style="margin-left: 24px" @click="onClearSelected">清空</a>
          </div>

          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :pagination="ipagination"
            :dataSource="data"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            @change="handleTableChange">


          <span slot="action" slot-scope="text, record">
           <a @click="houseData(record)">详情</a>
          </span>

          </a-table>
        </a-col>
      </div>

    </a-row>

    <a-row>
      <div class="action" v-show="true">
        <a-button  style="margin-right: 8px" @click="selecthouse">添加单元</a-button>
        <a-button type="primary" @click="chooseDone">确定申报</a-button>
        <!--<a-button style="margin-left: 8px"  @click="houseData">房源详情</a-button>-->
      </div>
    </a-row>


    <!---弹出表单区域-->
    <div>
      <Housemessage-modal ref="modalForm" @ok="modalFormOk"></Housemessage-modal>

      <bdcqzh-list-modal ref="bdcqzhmodalForm" @selectedQzh="selectedQzh" ></bdcqzh-list-modal>
    </div>

    <!-- 维山高拍仪 -->
    <div style="display: none">
      <ExternalDevices-modal ref="EDmodalForm" @ok="modalFormOk"></ExternalDevices-modal>
    </div>

  </a-card>



</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import HousemessageModal from '../modalForm/HousemessageModal'
  import BdcqzhListModal from '../modalForm/BdcqzhListModal'
  import { getAction, postAction } from '@/api/manage'
  import DetailList from '@/components/tools/DetailList'
  import { mixinDevice } from '@/utils/mixin.js'
  import PageLayout from '@/components/page/PageLayout'
  import ExternalDevicesModal from '@/components/ViiSanCamSDK/ExternalDevicesModal'


  const DetailListItem = DetailList.Item
  export default {
    name: "Step1_HouseCheck",
    mixins:[SupermapListMixin,mixinDevice],
    components : {
      DetailList,
      DetailListItem,
      HousemessageModal,
      BdcqzhListModal,
      PageLayout,
      ExternalDevicesModal
    },
    data() {
      return {
        form: this.$form.createForm(this),
        labelCol: {
          xs: {span: 24},
          sm: {span: 8},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '不动产单元号',
            align:"center",
            dataIndex: 'bdcdyh',
          },
          {
            title: '坐落',
            align:"center",
            dataIndex: 'zl'
          },
          {
            title: '建筑面积',
            align:"center",
            dataIndex: 'jzmj',
            customRender:function (t,house,index) {
              return house.ycjzmj ? house.ycjzmj : house.scjzmj;
            }
          },
          {
            title: '套内面积',
            align:"center",
            dataIndex: 'tnjzmj',
            customRender:function (t,house,index) {
              return house.yctnjzmj ? house.yctnjzmj : house.sctnjzmj;
            }
          },
          /*{
            title: '宗地面积',
            align:"center",
            dataIndex: 'zdmj',
            customRender:function (t,house,index) {
              return house.zdmj;
            }
          },*/
          {
            title: '查封状态',
            align:"center",
            dataIndex: 'cfzt',
            customRender:function (t,house,index) {
              return  t == 1 ? '已查封':'无';
            }
          },
          {
            title: '抵押状态',
            dataIndex: 'dyzt',
            align:"center",
            customRender:function (t,house,index) {
              return  t == 1 ? '已抵押':'无';
            }
          },
          {
            title: '异议状态',
            dataIndex: 'yyzt',
            align:"center",
            customRender:function (t,house,index) {
              return  t == 1 ? '存在异议':'无';
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url : {
          houseSearch: "/modules/wfi_proinst/houseSearch",
          createProject: "/modules/wfi_proinst/createProjectByData",
          updataBdcDy: "/modules/wfi_proinst/updataBdcDy",
          houseshislist : "/modules/houseshis/list",
          selecthouse : "/modules/wfi_proinst/selecthouse",
          selectedhouselist : "/modules/bdc_dy/queryByLsh",
          removehouse : "/modules/wfi_proinst/removehouse",
          setNameAndInitDYQR : "/modules/wfi_proinst/setNameAndInitDYQR",
          ywlxurl:"/modules/wfi_proinst/userInfo",

        },
        actionshow : false,
        data : [],//房源核验的列表
        selectedHousedata:[],//左边单元列表
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
        houselistloading:false,
        housedetail:{},
        department:{},
        qlrshow:false
      }

    },
    props:['prolsh'],
    created() {
      if(this.data.length>0) {
        this.actionshow = true;
      }
      this.loadHouseData();
      this.loadselectedData();
      this.loadDepartInfo();
    },
    methods: {
      loadData() {
        console.log('开始，这里重写只是因为不需要初始化加载什么数据而已');
      },
      searchQuery() {
        var that =  this;
        this.form.validateFields((err, values) => {
          if (!err) {
            this.loading = true;
            values = Object.assign(values,{prolsh:this.prolsh});
            getAction(this.url.houseSearch, values).then((res) => {
              if (res.success) {
                this.loadHouseData();
              } else {
                that.$message.error(res.message);
              }
              this.loading = false;
            })
          }
        })
      },
      loadDepartInfo() {
        getAction(this.url.departInfo, {prolsh:this.prolsh}).then((res) => {
          if (res.success) {
            let departName = res.result.sysDepart.departName;
            let deptZjh = res.result.sysDepart.deptZjh;
            if (departName) {
              this.form.setFieldsValue({qlrmc:departName});
              document.getElementById("qlrmc").setAttribute("disabled", true);
            }
            if (deptZjh) {
              this.form.setFieldsValue({qlrzjh:deptZjh});
              document.getElementById("qlrzjh").disabled = "disabled";
              document.getElementById("qlrzjh").setAttribute("disabled", true);
            }
          } else {
            this.form.setFieldsValue({qlrmc:''});
            this.form.setFieldsValue({qlrzjh:''});
            document.getElementById("qlrmc").disabled = "";
            document.getElementById("qlrzjh").disabled = "";
          }
        })
      },
      loadselectedData() {
        getAction(this.url.selectedhouselist, {prolsh:this.prolsh}).then((res) => {
          if(res.success) {
            this.selectedHousedata = res.result;
          } else {
            this.$message.error(res.message);
          }
        })
      },
      loadHouseData(arg){
        if(arg){
          this.ipagination.current = arg;
        }
        var param = {prolsh:this.prolsh,fyzt:1};
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        getAction(this.url.houseshislist, param).then((houselistres) => {
          if(houselistres.success) {
            this.data = houselistres.result.records;
            this.ipagination.total = houselistres.result.total;
            this.onClearSelected();
            if(this.data.length>0){
              this.actionshow = true;
            }
          } else {
            this.$message.error(houselistres.message);
          }
        })
      },
      loadYwlx(){
         var that = this;
         getAction(this.url.ywlxurl,{prolsh:this.prolsh}).then((userinfo) => {
            console.log(userinfo);
            if(userinfo.success) {
              //this.data = userinfo;
              that.department = userinfo.result;
              if(that.department.departName && that.department.departName.indexOf("银行")>-1){
                 that.form.setFieldsValue({
                   qlrmc: this.department.departName,
                   qlrzjh:this.department.deptZjh
                 })
                this.qlrshow = true
               }
              /*this.department.forEach(function (item,i,array ) {
                if(item.departName.indexOf("银行")>-1){
                  that.form.setFieldsValue({
                     qlrmc: item.departName,
                     qlrzjh:item.deptZjh
                   })
                }
                //allSettingColumns.push(item.dataIndex);
              })*/
            } else {
              this.$message.error("登录失效");
            }
          })
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        this.ipagination = pagination;
        this.loadHouseData();
      },
      chooseDone() {
        var that =  this;
        if(this.selectedHousedata.length == 0) {
          this.$notification['error']({
            message: '请选择要办理业务的单元'
          })
          return ;
        }
        that.$emit('initStep2Page');
        that.$emit('nextStep');
        getAction(this.url.setNameAndInitDYQR, {prolsh:this.prolsh}).then((res) => {
        })

      },
      createProject() {
        //this.prolsh是父层传入的，如果存在，说明已经创建项目了，回到第一步只是重新选择单元，只做更新操作即可
        var param = {};
        var that = this;
        if(this.prolsh){
          this.updataProject();
        } else {
          param.qlrlist = this.qlrdata;
          param.dylist = this.selectionRows;
          param.prodefid = this.prodefid;
          postAction(this.url.createProject, param).then((res) => {
            if(res.success) {
              that.$emit('initStep2Page', res.result);
              that.$emit('nextStep');
              return res.result;
            } else {
              this.$notification['error']({
                message: res.message
              })
            }
          })
        }
      },
      updataProject() {
        var param = {};
        param.dylist = this.selectionRows;
        param.qlrlist = this.qlrdata;
        param.prolsh = this.prolsh;
        postAction(this.url.updataBdcDy, param).then((res) => {
          if(res.success) {
            this.$emit('initStep2Page', this.prolsh);
            this.$emit('nextStep');
          } else {
            this.$notification['error']({
              message: res.message
            })
          }
        })
      },
      houseData(record) {
        this.$refs.modalForm.initData(record);
        this.$refs.modalForm.visible = true;
        this.$refs.modalForm.title = "房源详情";
      },
      selecthouse() {
        var flag = '';
        var that = this;
        var selectehouses = this.selectionRows;
        if(selectehouses.length==0) {
          this.$message.error('请选择至少一个单元');
          return ;
        }
        for(var key in selectehouses) {
          var house = selectehouses[key];
          if(house.cfzt == 1) {
            flag = "已查封";
          } else if(house.dyzt == 1) {
            flag = "已抵押";
          } else if(house.yyzt == 1) {
            flag = "存在异议";
          }
        }
        // if(flag) {
        //   this.$confirm({
        //     title:"确认添加",
        //     content:"选择的单元中包含有"+flag+"的单元，是否确定继续添加?",
        //     onOk: function(){
        //       that.addHouse(selectehouses);
        //     }
        //   });
        // } else {
          that.addHouse(selectehouses);
        // }


      },
      addHouse(houselist) {
        var param  = {};
        param.houselist = houselist;
        param.prolsh = this.prolsh;
        postAction(this.url.selecthouse, param).then((res) => {
          if(res.success) {
            this.loadselectedData();
            this.loadHouseData();
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
      chooseHouse(item,index) {
        this.selectedhouses(index-1);
      },
      //第几个房屋被选择
      selectedhouses(index) {
        var newhousedata = [];
        for(var key in this.selectedHousedata) {
          if(key == index) {
            //改变该单元的样式
            this.selectedHousedata[key].thisselected = 'ant-alert-info';
            this.housedetail = this.selectedHousedata[key];
          } else {
            this.selectedHousedata[key].thisselected = '';
          }
          newhousedata.push(this.selectedHousedata[key]);
        }
        this.selectedHousedata = newhousedata;
      },
      handleremoved() {
        if(this.housedetail.id) {
          getAction(this.url.removehouse, {houseid:this.housedetail.id,prolsh:this.prolsh}).then((res) => {
            if(res.success) {
              this.$message.success('操作成功');
              this.loadHouseData();
              this.loadselectedData();
            } else {
              that.$message.error(res.message);
            }
          })
        } else {
          this.$message.error('请选择需要移除的单元');
        }
      },
      searchQzh() {
        this.$refs.bdcqzhmodalForm.visible = true;
      },
      selectedQzh(bdcqzh) {
        this.form.setFieldsValue({bdcqzh:bdcqzh})
      },
      loadidcard(){
        try{
          this.$refs.EDmodalForm.ReadIDCard();
          var vm = this;
          setTimeout(function(){
            var carddata = document.getElementById("idMsg").value;
            if(carddata != "||||||||"){
              vm.form.getFieldDecorator("qlrmc", {initialValue:carddata.split("|")[0]}); //赋值方法
              vm.form.getFieldDecorator("qlrzjh", {initialValue: carddata.split("|")[5]});
              document.getElementById("qlrmc").value = carddata.split("|")[0];
              document.getElementById("qlrzjh").value = carddata.split("|")[5];
            }else{
              vm.$message.error('读卡失败，请检查身份证是否正确放置');
            }

          },1500);
        }catch(e){
          console.log('读卡异常:', e);
          this.$message.error('读卡失败，请检查设备是否正常');
        }
      }
    }
  }
</script>

<style scoped>

  .action{
    text-align: center;
    margin: 0 auto;
    padding: 24px 0 8px;
  }

</style>