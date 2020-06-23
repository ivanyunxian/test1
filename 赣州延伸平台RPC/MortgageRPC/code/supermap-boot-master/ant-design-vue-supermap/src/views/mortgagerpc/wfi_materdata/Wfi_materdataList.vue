<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="资料名称 NAME">
              <a-input placeholder="请输入资料名称 NAME" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="路径 PATH">
              <a-input placeholder="请输入路径 PATH" v-model="queryParam.path"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="扩展名 SUFFIX">
              <a-input placeholder="请输入扩展名 SUFFIX" v-model="queryParam.suffix"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="上传时间 CREATED">
              <a-input placeholder="请输入上传时间 CREATED" v-model="queryParam.created"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="OPBY">
              <a-input placeholder="请输入OPBY" v-model="queryParam.opby"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('附件资料表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

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

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <wfi_materdata-modal ref="modalForm" @ok="modalFormOk"></wfi_materdata-modal>
  </a-card>
</template>

<script>
  import Wfi_materdataModal from './modules/Wfi_materdataModal'
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'

  export default {
    name: "Wfi_materdataList",
    mixins:[SupermapListMixin],
    components: {
      Wfi_materdataModal
    },
    data () {
      return {
        description: '附件资料表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '资料名称 NAME',
            align:"center",
            dataIndex: 'name'
           },
		   {
            title: '路径 PATH',
            align:"center",
            dataIndex: 'path'
           },
		   {
            title: '扩展名 SUFFIX',
            align:"center",
            dataIndex: 'suffix'
           },
		   {
            title: '上传时间 CREATED',
            align:"center",
            dataIndex: 'created'
           },
		   {
            title: 'OPBY',
            align:"center",
            dataIndex: 'opby'
           },
		   {
            title: '状态 STATUS',
            align:"center",
            dataIndex: 'status'
           },
		   {
            title: '模板实例编号 DATUMINST_ID',
            align:"center",
            dataIndex: 'datuminstId'
           },
		   {
            title: '受理项目编号',
            align:"center",
            dataIndex: 'proinstId'
           },
		   {
            title: '文件序号',
            align:"center",
            dataIndex: 'fileIndex'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/mortgagerpc/wfi_materdata/list",
          delete: "/mortgagerpc/wfi_materdata/delete",
          deleteBatch: "/mortgagerpc/wfi_materdata/deleteBatch",
          exportXlsUrl: "mortgagerpc/wfi_materdata/exportXls",
          importExcelUrl: "mortgagerpc/wfi_materdata/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>