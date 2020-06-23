<template>
  <div class="page-header-index-wide"   >
    <a-row :gutter="24">

      <!-- 总金额 -->
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card  title="总抵押金额（万元）"  id="cmmid"  total="0">
          <div style="margin-bottom: -75px; margin-left: 65px;">
            <pie title="饼图" :height="height" :dataSource="PMS"/>
          </div>
          <template slot="footer">今日抵押金额<span id="todaym">0</span></template>
        </chart-card>
      </a-col>

      <!-- 总办件量 -->
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card  title="总受理量" id = 'cpid' :total="0 | NumberFormat">
          <a-tooltip title="所属部门的总业务受理数量" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <template slot="footer">今日受理量<span id="todayp">0</span></template>
        </chart-card>
      </a-col>

      <!-- 总办理结束量 -->
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card  title="总业务完成数量" id = 'cpeid' :total="0 | NumberFormat">
          <a-tooltip title="所属部门的总业务办理完成数量" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <template slot="footer">今日业务完成数量<span id="todayo">0</span></template>
          <!--    <template slot="footer">成功率 <span>99%</span></template> -->
        </chart-card>
      </a-col>

      <!-- 每日办件成功率 -->
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card  title="总办件量完成率" id = 'shztpsid'  total="0%">
          <!-- 饼图 -->
         <div style="margin-bottom: -75px; margin-left: 100px;">
           <pie title="饼图" :height="height" :dataSource="SHZTP"/>
         </div>
          <template slot="footer">今日办件量完成率<span id="todayl">0</span></template>
        </chart-card>
      </a-col>
    </a-row>


    <a-card  :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <!-- 排行
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{width: '256px'}" />
          </div>-->

          <!-- 业务类型排行 -->
          <a-tab-pane loading="true" tab="每月办件量" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="办件量" :dataSource="projectNameCount" yaxisText="件"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="业务类型办理数量排行榜" :list="cpnData"/>
              </a-col>
            </a-row>
          </a-tab-pane>


          <!-- 业务类型金额排行 -->
          <a-tab-pane tab="每月抵押金额（万元）" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="抵押金额（万元）" :dataSource="MCount" yaxisText="金额"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="业务抵押金额排行榜" :list="cmmaData"/>
              </a-col>
            </a-row>
          </a-tab-pane>

        </a-tabs>
      </div>
    </a-card>

  </div>
</template>

<script>
  import { axios } from '@/utils/request'
  import ChartCard from '@/components/ChartCard'
  import ACol from "ant-design-vue/es/grid/Col"
  import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
  import MiniArea from '@/components/chart/MiniArea'
  import MiniBar from '@/components/chart/MiniBar'
  import MiniProgress from '@/components/chart/MiniProgress'
  import RankList from '@/components/chart/RankList'
  import Bar from '@/components/chart/Bar'
  import LineChartMultid from '@/components/chart/LineChartMultid'
  import HeadInfo from '@/components/tools/HeadInfo.vue'
  import Trend from '@/components/Trend'
  import Pie from '@/components/chart/Pie'

  import { getLoginfo,getVisitInfo } from '@/api/api'
  const cmmaData = [];
  const cpnData = [];
  const projectNameCount = [];
  const MCount = [];
  const SHZTP = [];
  const PMS = [];

  export default {
    name: "getMainData",
    components: {
      ATooltip,
      ACol,
      ChartCard,
      MiniArea,
      MiniBar,
      MiniProgress,
      RankList,
      Bar,
      Trend,
      LineChartMultid,
      HeadInfo,
      Pie,
    },
    data () {
      return {
        description: '证书信息管理页面',
        url: {
          cma: "/modules/statistics/getCountMortgageAmount", //获取部门总抵押金额
          mma: "/modules/statistics/getMonthMortgageAmount", //获取部门每月抵押金额
          cmma: "/modules/statistics/getCountMonthMortgageAmount", //获取部门业务类型金额量
          cp: "/modules/statistics/getCountProject", //获取部门总办件量
          mp: "/modules/statistics/getMonthProject", //获取部门每月办件量
          cpn: "/modules/statistics/getCountProjectName", //获取部门业务类型办理量
          shztps: "/modules/statistics/getSHZTProportionServer", //获取部门业务百分比
          pms: "/modules/statistics/getProjectMortgageAmount", //获取部门金额百分比

        },
        datas:{},
        height: 200,
        cmmaData,
        cpnData,
        center: null,
        visitFields:['ip','visit'],
        visitInfo:[],
        indicator: '<a-icon type="loading" style="font-size: 24px" spin />',
        MCount,
        projectNameCount,
        SHZTP,
        PMS,

      }
    },
    created() {
      this.loadDatas();
    },
    methods: {
      loadDatas(){
        var that = this;
        //获取部门总抵押金额
        axios({
          url: this.url.cma,
          method: 'get',
          params: {type: '0'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              document.getElementById("cmmid").getElementsByClassName('total')[0].firstChild.innerHTML = response.data[0].DYJE
            }
          }
        });

        //今日业务金额
        axios({
          url: this.url.cma,
          method: 'get',
          params: {type: '1'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              if(response.data[0] != null){
                document.getElementById("todaym").innerHTML = response.data[0].DYJE;
              }
            }
          }
        });

        //获取部门每月抵押金额
        axios({
          url: this.url.mma,
          method: 'get',
          params: {
            stime: '0',
            etime:'0',
            divisionc: "0"
          },
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              MCount.length = 0;
              for (let i = 0; i < response.data.length; i += 1) {
                MCount.push({
                  x: response.data[i].MTIME ,
                  y: response.data[i].MDYJE?response.data[i].MDYJE:0
                })
              }
            }
          }
        });

        //获取部门业务类型金额量
        axios({
          url: this.url.cmma,
          method: 'get',
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              cmmaData.length = 0;
              for(var i =0;i< response.data.length;i++){
                cmmaData.push({
                  name: response.data[i].PRODEF_NAME,
                  total: response.data[i].DYJE?response.data[i].DYJE:0
                })
              }
            }
          }
        });

        //获取部门办件完成量
        axios({
          url: this.url.cp,
          method: 'get',
          params: {type: '0',shzt:'99'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              document.getElementById("cpid").getElementsByClassName('total')[0].firstChild.innerHTML = response.data[0].TOTAL
            }
          }
        });


        //获取今日部门总办件量
        axios({
          url: this.url.cp,
          method: 'get',
          params: {type: '1',shzt:'99'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              if(response.data[0] != null){
                document.getElementById("todayp").innerHTML = response.data[0].TOTAL;
              }
            }
          }
        });

        //获取部门总业务成功量
        axios({
          url: this.url.cp,
          method: 'get',
          params: {type: '0',shzt:'20'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              document.getElementById("cpeid").getElementsByClassName('total')[0].firstChild.innerHTML = response.data[0].TOTAL
            }
          }
        });


        //获取今日部门总业务成功量
        axios({
          url: this.url.cp,
          method: 'get',
          params: {type: '1',shzt:'20'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              document.getElementById("todayo").innerHTML = response.data[0].TOTAL;
            }
          }
        });

        //获取部门每月办件量
        axios({
          url: this.url.mp,
          method: 'get',
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              projectNameCount.length = 0;
              for (let i = 0; i < response.data.length; i += 1) {
                projectNameCount.push({
                  x: response.data[i].MTIME,
                  y: response.data[i].TOTAL
                })
              }
            }
          }
        });

        //获取部门业务类型办理量
        axios({
          url: this.url.cpn,
          method: 'get',
        }).then(function(response) {
          if(response.success){
            if(cpnData.length != 0){
              cpnData.length = 0;
            }
            if(response.data.length > 0){
              cpnData.length = 0;
              for(var i =0;i< response.data.length;i++){
                cpnData.push({
                  name: response.data[i].PRODEF_NAME,
                  total: response.data[i].TOTAL
                })
              }
            }
          }
        });


        //获取部门办理业务成功的百分比
        axios({
          url: this.url.shztps,
          method: 'get',
          params: {type: '0'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              var psdata = response.data[0].P * 100;
              document.getElementById("shztpsid").getElementsByClassName('total')[0].firstChild.innerHTML = psdata.toFixed(2) +"%"
              var yindex = psdata.toFixed(2);
              SHZTP.push({
                item: '已完成',
                count: parseInt(yindex)
              });

              var xindex = 100 - psdata.toFixed(2);
              SHZTP.push({
                item: '未完成',
                count: parseInt(xindex)
              });
            }
          }
        });

        //获取业务金额百分比
        axios({
          url: this.url.pms,
          method: 'get',
          params: {
            stime: '0',
            etime:'0',
            divisionc: "0"},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              PMS.length = 0;
              for(var pm = 0;pm < response.data.length;pm++){
                PMS.push({
                  item: response.data[pm].S,
                  count: parseInt(response.data[pm].DYJE?response.data[pm].DYJE:'0')
                });
              }
            }
          }
        });

        //获取部门办理每日业务成功的百分比
        axios({
          url: this.url.shztps,
          method: 'get',
          params: {type: '1'},
        }).then(function(response) {
          if(response.success){
            if(response.data.length > 0){
              var psdata = response.data[0].P * 100;
              document.getElementById("todayl").innerHTML = psdata.toFixed(2) +"%"
            }
          }
        });

       }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>