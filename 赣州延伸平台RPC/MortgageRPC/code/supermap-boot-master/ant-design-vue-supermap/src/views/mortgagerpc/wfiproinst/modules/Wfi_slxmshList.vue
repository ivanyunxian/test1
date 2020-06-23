<template>
  <a-modal
    title="审核过程"
    :width="800"
    :visible="visible"
    :footer="null"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-card :bordered="false">

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
          @change="handleTableChange"
          >

        </a-table>
      </div>
      <!-- table区域-end -->

    </a-card>
  </a-modal>
</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import { deleteAction, getAction,downFile } from '@/api/manage'

  export default {
    name: "Wfi_slxmshList",
    mixins:[SupermapListMixin],
    components: {
    },
    data () {
      return {
        description: '审核过程意见表管理页面',
        visible:false,
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
            title: '项目审核状态',
            align:"center",
            dataIndex: 'shzt_dictText'
          },
		   {
            title: '审核人员',
            align:"center",
            dataIndex: 'shry'
           },
		   {
            title: '审核意见',
            align:"center",
            dataIndex: 'shyj'
           },
		   {
            title: '审核时间',
            align:"center",
            dataIndex: 'shsj'
           }

        ],
        url: {
          list: "/ac/wfi_slxmsh/list"
       },
        prolsh : ''
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
      loadData(prolsh,pageNo,pageSize) {
        this.prolsh = prolsh;
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        if(pageNo==undefined || pageNo==null || pageNo==""){
          pageNo ='1';
        }
        if(pageSize==undefined || pageSize==null || pageSize==""){
          pageSize ='10';
        }
        //加载数据 若传入参数1则加载第一页的内容
        this.loading = true;
        getAction(this.url.list, {prolsh:prolsh, column: 'shsj',order: 'asc',pageNo:pageNo,pageSize:pageSize}).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      handleCancel () {
        this.$emit('close');
        this.visible = false;
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.loadData(this.prolsh,pagination.current,pagination.pageSize);
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>