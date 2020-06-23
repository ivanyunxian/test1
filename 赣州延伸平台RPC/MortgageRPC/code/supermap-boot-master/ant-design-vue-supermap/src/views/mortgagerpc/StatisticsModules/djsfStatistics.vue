<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" :form="form">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="城区">
              <j-dict-select-tag
                @change="handleChangeRuleCondition"  v-decorator="['divisioncode', {}]"
                placeholder="请选择城区" :triggerChange="true"  dictCode="DIVISION_CODE"/>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="审核状态">
              <j-dict-select-tag
                @change="handleChangeRuleCondition"  v-decorator="['shzt', {}]"
                placeholder="请选择审核状态" :triggerChange="true"  dictCode="SHZT"/>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="业务类型">
              <j-dict-select-tag
                @change="handleChangeRuleCondition"  v-decorator="['lclx', {}]"
                placeholder="请选择业务类型" :triggerChange="true"  dictCode="LCLX"/>
            </a-form-item>
          </a-col>

         <a-Col :md="6" :sm="8">
           <a-form-item
             :labelCol="labelCol"
             :wrapperCol="wrapperCol"
             label="起始时间">
             <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" style="width: 100%" v-decorator="[ 'sDate',{}]"/>
           </a-form-item>
          </a-Col>

          <a-Col :md="6" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="结束时间">
              <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" style="width: 100%" v-decorator="[ 'eDate',{}]"/>
            </a-form-item>
          </a-Col>



          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="ShowProjectData" icon="search">查询</a-button>
            <!--  <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">清空</a-button> -->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>


    <!-- table区域-begin -->
    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="RN"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">


        <span slot="action" slot-scope="text, record">
          <a @click="showProjectBox(record)">业务详情</a>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <div style="margin-top: 10px"  v-if="barshow" >
      <bar title="月抵押金额统计(元)" :dataSource="listData" yaxisText="金额" />
    </div>
    <div style="margin-top: 10px" v-if="pieshow">
      <h3  id="countm" ></h3>
      <pie style="margin-top: -100px;"  title="饼图" :height="600" :dataSource="pielist"/>
    </div>

    <orderacceptproject-modal ref="modalForm" @ok="modalFormOk"></orderacceptproject-modal>
  </a-card>


</template>

<script>
  import { axios } from '@/utils/request'
  import ChartCard from '@/components/ChartCard'
  import ACol from "ant-design-vue/es/grid/Col"
  import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
  import RankList from '@/components/chart/RankList'
  import Bar from '@/components/chart/Bar'
  import LineChartMultid from '@/components/chart/LineChartMultid'
  import HeadInfo from '@/components/tools/HeadInfo.vue'
  import Trend from '@/components/Trend'
  import Pie from '@/components/chart/Pie'
  import JDate from '@/components/jeecg/JDate'
  import { getLoginfo,getVisitInfo } from '@/api/api'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { deleteAction, postAction,downFile } from '@/api/manage'
  import OrderacceptprojectModal from '../wfiproinst/OrderAcceptProjectModal'

  const listData = [];

  const pielist = [];

  export default {
    name: "getData",
    mixins: [JeecgListMixin],
    components: {
      ATooltip,
      ACol,
      ChartCard,
      RankList,
      Bar,
      Trend,
      LineChartMultid,
      HeadInfo,
      Pie,
      JDate,
      OrderacceptprojectModal

    },
    data () {
      return {
        description: '统计台账',
        projectShow:false,
        loading:false,
        url: {
          list: "/modules/statistics/queryProjectData", //获取统计台账
          mma: "/modules/statistics/getMonthMortgageAmount", //获取部门每月抵押金额
          pms: "/modules/statistics/getProjectMortgageAmount", //获取部门金额百分比
          ama: "/modules/statistics/getAreaMortgageAmount", //获取各区域的总金额
        },
        columns: [
          {
            title: '序号',
            dataIndex: 'rowIndex',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '流程名称',
            align:"center",
            dataIndex: 'PROJECT_NAME',
            key:'PROJECT_NAME',
          },
          {
            title: '流水号',
            align:"center",
            dataIndex: 'PROLSH',
            key:'PROLSH',
          },

          {
            title: '区域',
            align:"center",
            dataIndex: 'DIVISION_CODE_dictText',
            key:'DIVISION_CODE_dictText',
          },
          {
            title: '申请人',
            align:"center",
            dataIndex: 'NAME',
            key:'NAME',
          },
          {
            title: '申请时间',
            align:"center",
            dataIndex: 'PROINST_START',
            key:'PROINST_START',
          },{
            title: '查看',
            dataIndex: 'action',
            key:'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }

        ],
        listData,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        form: this.$form.createForm(this),
        params:{},
        barshow:false,
        isdivision:false,
        pieshow: false,
        pielist,
      }
    },
    methods: {
      ShowProjectData(d){
        this.params = {};
          //获取下拉框选择的参数
        var selectdata = document.getElementsByClassName("ant-select-selection__rendered");
        for(var i = 0;i<selectdata.length;i++){
          if(selectdata[i].childNodes.length > 1){
            if(selectdata[i].childNodes[1].childNodes[0] != undefined){
              if(selectdata[i].childNodes[1].childNodes[0].childNodes.length != 0){
                var tvaluecode = selectdata[i].childNodes[1].childNodes[0].getAttribute("value"); //字典的代码值
                var tvaluename = selectdata[i].childNodes[1].innerText; //字典的中文值
                var tkey = selectdata[i].parentNode.parentNode.id; //查询的字段
                this.params[tkey] = tvaluecode + ";" + tvaluename;
              }
            }
          }
        }



        //获取时间类型参数
        var times = document.getElementsByClassName("ant-calendar-picker-input");
        for(var i = 0;i<times.length;i++){
          if(times[i].value != ""){
            var tkey = times[i].parentNode.parentNode.id
            this.params[tkey] = times[i].value;
          }
        }

        if(d == 1){
          this.params = {};
        }

        this.params.page = '1';
        this.params.size = '10'

         if(this.ipagination.pageSize != ""){
           this.params.size = this.ipagination.pageSize;
         }

         if(this.ipagination.current != ""){
           this.params.page = this.ipagination.current;
         }
        this.loading = true;
        var that = this;

        if(this.params.divisioncode){
          this.isdivision = true;
        }else{
          this.isdivision = false;
        }


        postAction(this.url.list, this.params).then((res) => {
          if (res.success) {
            that.dataSource = res.data;
            that.ipagination.total = res.total;

            if(res.total != 0){

              that.barshow = true;
              listData.length = 0;
              //获取部门每月抵押金额
              axios({
                url: this.url.mma,
                method: 'get',
                params: {
                  stime: this.params.sDate?this.params.sDate:"0",
                  etime: this.params.eDate?this.params.eDate:"0",
                  divisionc: this.params.divisioncode?this.params.divisioncode:"0"
                },
              }).then(function(response) {
                if(response.success){
                  for (let i = 0; i < response.data.length; i += 1) {
                    listData.push({
                      x: response.data[i].MTIME,
                      y: response.data[i].MDYJE?response.data[i].MDYJE:0
                    })
                  }
                }
              });

              if(that.isdivision){
                that.pieshow = true;
                //获取部门已办业务和未办业务的总抵押金额百分比
                axios({
                  url: this.url.pms,
                  method: 'get',
                  params: {
                    stime: this.params.sDate?this.params.sDate:"0",
                    etime: this.params.eDate?this.params.eDate:"0",
                    divisionc: this.params.divisioncode?this.params.divisioncode:"0"
                  },
                }).then(function(response) {
                  if(response.success){
                    pielist.length = 0;
                    var dyjecount = 0;
                    var zbdyje = 0;
                    var ybdyje = 0;
                    for (let i = 0; i < response.data.length; i += 1) {
                      pielist.push({
                        item: '区域'+response.data[i].S+'金额',
                        count: parseInt(response.data[i].DYJE?response.data[i].DYJE:'0')
                      });
                      dyjecount += response.data[i].DYJE?response.data[i].DYJE:0;
                      if(response.data[i].S == "在办"){
                        zbdyje = response.data[i].DYJE?response.data[i].DYJE:0;
                      }
                      if(response.data[i].S == "已办"){
                        ybdyje = response.data[i].DYJE?response.data[i].DYJE:0;
                      }
                    }
                    document.getElementById("countm").innerHTML =
                      "<p>区域抵押金额：<text style='color: red'>"+dyjecount+"万元</text></p>"+
                      "<p>区域在办抵押金额：<text style='color: red'>"+zbdyje+"万元</text></p>"+
                      "<p>区域已办抵押金额：<text style='color: red'>"+ybdyje+"万元</text></p>";
                  }
                });
              }else{
                //获取各区域的总抵押金额
                that.pieshow = true;
                var areahtml = "";
                axios({
                  url: this.url.ama,
                  method: 'get',
                  params: {
                    stime: this.params.sDate?this.params.sDate:"0",
                    etime: this.params.eDate?this.params.eDate:"0",
                    divisionc: this.params.divisioncode?this.params.divisioncode:"0"
                  },
                }).then(function(response) {
                  if(response.success){
                    pielist.length = 0;
                    var dyjecount = 0;
                    for (let i = 0; i < response.data.length; i += 1) {
                      pielist.push({
                        item: response.data[i].DIVISION_CODE_dictText,
                        count: parseInt(response.data[i].DYJE?response.data[i].DYJE:'0')
                      });
                      dyjecount += response.data[i].DYJE?response.data[i].DYJE:0;
                      areahtml += "<p>"+response.data[i].DIVISION_CODE_dictText+"抵押金额：<text style='color: red'>"+response.data[i].DYJE+"万元</text></p>";
                    }
                    document.getElementById("countm").innerHTML = "<p>总抵押金额：<text style='color: red'>"+dyjecount+"万元</text></p>"
                    +areahtml;
                  }
                });
              }

            }else{
              that.barshow = false;
              that.pieshow = false;
            }
          }
        })
        this.loading = false;
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
      },handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.ShowProjectData();
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      searchReset(){
        this.params = {};
        document.getElementById("xmmc").value = "";
      },
      showProjectBox(datas){
        this.$refs.modalForm.title = "项目详情";
        this.$refs.modalForm.setdisabled(datas.PROLSH);
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>