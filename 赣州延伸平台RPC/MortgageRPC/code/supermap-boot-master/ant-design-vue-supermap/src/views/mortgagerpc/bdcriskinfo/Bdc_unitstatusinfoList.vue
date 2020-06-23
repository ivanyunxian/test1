<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" :form="form">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="不动产权证号"
                         style="width: 100%">
              <a-input
                :disabled="false"
                placeholder="请输入不动产权证号"
                v-decorator="[
              'bdcqzh',
              {rules: [{ required: false, message: '请输入不动产权证号'}]}
            ]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item
              label="不动产单元类型">
              <j-dict-select-tag
                 v-decorator="['bdcdylx', {}]"
                placeholder="请选择不动产单元类型" :triggerChange="true"  dictCode="BDCDYLX"/>
            </a-form-item>
          </a-col>


          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">

      </a-table>
    </div>
    <!-- table区域-end -->

  </a-card>
</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import { postAction } from '@/api/manage'
  import {initDictOptions, filterDictText ,ajaxFilterDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "Bdc_unitstatusinfoList",
    mixins:[SupermapListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        description: '单元限制信息查询',
        // 表头
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
            title: '坐落',
            align:"center",
            dataIndex: 'ZL'
           },
		   {
            title: '不动产证明号',
            align:"center",
            dataIndex: 'BDCQZH'
           },
		   {
            title: '权利起始时间',
            align:"center",
            dataIndex: 'QLQSSJ'
           },
		   {
            title: '权利结束时间',
            align:"center",
            dataIndex: 'QLJSSJ'
           },
		   {
            title: '登记时间',
            align:"center",
            dataIndex: 'DJSJ'
           },
		   {
            title: '查封状态',
            align:"center",
            dataIndex: 'LimitStatus',
             customRender:function (val) {
               if(val=='2'){
                  return '无'
               }
              }
           },
		   {
            title: '限制状态',
            align:"center",
            dataIndex: 'MortgageStatus'
           },
		   {
            title: '异议状态',
            align:"center",
            dataIndex: 'NoticeStatus'
           }
        ],
	    	url: {
          list: "/modules/bdc_riskinfo/getUnitStatusInfo",
       }
    }
  },
  computed: {
  },
  created() {
  },
  methods: {
    loadData() {
     //donothing
    },
    searchdata(arg) {
      if(!this.url.list){
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      var bdcdylx = this.form.getFieldValue('bdcdylx');
      var bdcqzh = this.form.getFieldValue('bdcqzh');
      if(bdcqzh){
        params.bdcqzh = bdcqzh;
      }
      if(bdcdylx){
        params.bdcdylx = bdcdylx;
      }
      this.loading = true;
      postAction(this.url.list, params).then((res) => {
        this.loading = false;
        if (res.success) {
          this.dataSource = res.result.data;
          if(this.dataSource.length>0) {
            this.ipagination.total = parseInt(this.dataSource[0].total);
          }
        } else {
          this.$message.warning(res.message)
        }
        if(res.code===510){
          this.$message.warning(res.message)
        }
      })
    },
    searchQuery() {
      this.searchdata(1);
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.ipagination = pagination;
      this.searchdata();
    }
  }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>