<template>
  <a-modal
    :visible="visible"
    :width="1400"
    @ok="close"
    @cancel="close"
    height="750px"
    cancelText="关闭"
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
          :pagination="false"
          :loading="loading"
         >

          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
          <template slot="customRenderStatus" slot-scope="required">
            <a-tag v-if="required==0" color="orange">否</a-tag>
            <a-tag v-if="required==1" color="red">是</a-tag>
          </template>
        </a-table>
      </div>
      <!-- table区域-end -->
      <div class="action" v-show="true">
        <a-button type="primary" @click="handleAdd">添加</a-button>
      </div>
      <!-- 表单区域 -->
      <wfd_materclass-modal ref="modalForm" @ok="modalFormOk" :prodefid="prodefid"></wfd_materclass-modal>
    </a-card>
  </a-modal>
</template>

<script>
  import Wfd_materclassModal from './modules/Wfd_materclassModal'
  import { getAction } from '@/api/manage'

  export default {
    name: "Wfd_materclassList",
    components: {
      Wfd_materclassModal
    },
    data () {
      return {
        description: '流程资料目录模板表管理页面',
        visible : false,
        loading : false,
        alias :'E_CERTCODE',
        // 表头
        columns: [
		   {
            title: '目录序号',
            align:"center",
            dataIndex: 'fileindex'
           },
		   {
            title: '目录名称',
            align:"center",
            dataIndex: 'name'
           },
		   {
            title: '是否必须',
            align:"center",
            dataIndex: 'required',
             scopedSlots: { customRender: 'customRenderStatus' }
           },
       {
           title: '电子证照类型',
           align:"center",
           dataIndex: 'ecertCode' //DIVISION_CODE_dictText
          },
       {
           title: '是否获取电子证照',
           align:"center",
           dataIndex: 'ecert',
            scopedSlots: { customRender: 'customRenderStatus' }
          },
          {
            title: '备注',
            align:"center",
            dataIndex: 'matedesc'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/mortgagerpc/wfd_materclass/list",
          delete: "/mortgagerpc/wfd_materclass/delete",
          exportXlsUrl: "mortgagerpc/wfd_materclass/exportXls",
          importExcelUrl: "mortgagerpc/wfd_materclass/importExcel",
       },
        dataSource : []
    }
  },
    props: ['prodefid'],
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
      close () {
        this.visible = false;
      },
      showData() {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        this.loading = true;
        getAction(this.url.list, {procodeid:this.prodefid,alias:this.alias}).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      modalFormOk() {
        this.showData();
      },
      handleEdit: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleAdd: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleDelete: function(id) {
        getAction(this.url.delete, {id:id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message)
          }
          this.showData();
        })
      }

    }
  }
</script>
<style >
  .action{
    text-align: center;
    margin: 0 auto;
    padding: 24px 0 8px;
  }
</style>