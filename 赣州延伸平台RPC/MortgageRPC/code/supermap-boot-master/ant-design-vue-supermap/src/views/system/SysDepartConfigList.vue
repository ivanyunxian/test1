<template>

  <a-modal
    title="接口管理"
    :width="1200"
    :visible="visible"
    @ok="close"
    @cancel="close"
  >
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
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />

          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
      <a-button  class="setButton" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="handleAdd">添加</a-button>

    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sysDepartConfig-modal ref="modalForm" :departid="departid" @ok="reloaddata"></sysDepartConfig-modal>
  </a-card>
  </a-modal>
</template>

<script>
  import SysDepartConfigModal from './modules/SysDepartConfigModal'
  import { deleteAction, getAction,downFile } from '@/api/manage'

  export default {
    name: "SysDepartConfigList",
    components: {
      SysDepartConfigModal
    },
    data () {
      return {
        description: '部门配置表管理页面',
        visible : false,
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
            title: '接口名称',
            align:"center",
            dataIndex: 'name'
           },
		   {
            title: '接口地址',
            align:"center",
            dataIndex: 'value'
           },
		   {
            title: '接口类型',
            align:"center",
            dataIndex: 'type'
           },
          {
            title: '用户名',
            align:"center",
            dataIndex: 'username'
          },
          {
            title: '密码',
            align:"center",
            dataIndex: 'password'
          },
		   {
            title: '备注',
            align:"center",
            dataIndex: 'memo'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/sys/sysDepartConfig/list",
          delete: "/sys/sysDepartConfig/delete",
          deleteBatch: "/sys/sysDepartConfig/deleteBatch",
          exportXlsUrl: "sys/sysDepartConfig/exportXls",
          importExcelUrl: "sys/sysDepartConfig/importExcel",
       },
    /* 数据源 */
    dataSource:[],
    /* 分页参数 */
    ipagination:{
      current: 1,
      pageSize: 10,
      pageSizeOptions: ['10', '20', '30'],
      showTotal: (total, range) => {
        return range[0] + "-" + range[1] + " 共" + total + "条"
      },
      showQuickJumper: true,
      showSizeChanger: true,
      total: 0
    },
    /* table加载状态 */
    loading:false,
    }
  },
    props:['departid'],
  computed: {
  },
    methods: {
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        this.loading = true;
        debugger
        getAction(this.url.list, {deptId:this.departid}).then((res) => {
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
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleEdit: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleTableChange(pagination, filters, sorter) {
        this.ipagination = pagination;
        this.loadData();
      },
      handleDelete: function (id) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        var that = this;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      handleAdd() {
        this.$refs.modalForm.visible = true;
        this.$refs.modalForm.add();
      },
      reloaddata() {
        this.loadData();
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>