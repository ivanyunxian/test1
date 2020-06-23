<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="流程编码">
              <a-input placeholder="请输入流程编码" v-model="queryParam.prodefCode"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="流程名称">
              <a-input placeholder="请输入流程名称" v-model="queryParam.prodefName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">

          <a-col :md="6" :sm="8">
            <a-form-item label="区划代码">
              <a-input placeholder="请输入区划代码" v-model="queryParam.divisionCode"></a-input>
            </a-form-item>
          </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="流程ID">
                <a-input placeholder="请输入流程ID" v-model="queryParam.prodefId"></a-input>
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
      <!--    <a-button type="primary" icon="download" @click="handleExportXls('流程定义表')">导出</a-button>
          <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
            <a-button type="primary" icon="import">导入</a-button>
          </a-upload> -->
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
        rowKey="prodefId"
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
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.prodefId)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item>
                  <a @click="showmaterclass(record)">资料模板</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <wfi_prodef-modal ref="modalForm" @ok="modalFormOk"></wfi_prodef-modal>
    <wfd_materclass-List ref="materclasslist" @ok="modalFormOk" :prodefid="prodefid"></wfd_materclass-List>

  </a-card>
</template>

<script>
  import Wfi_prodefModal from './modules/Wfi_prodefModal'
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import Wfd_materclassList from '@/views/mortgagerpc/wfd_materclass/Wfd_materclassList'

  export default {
    name: "Wfi_prodefList",
    mixins:[SupermapListMixin],
    components: {
      Wfi_prodefModal,
      Wfd_materclassList
    },
    data: function() {
      return {
        description: '流程定义表管理页面',
        prodefid : '',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '区划代码',
            align: 'center',
            dataIndex: 'divisionCode_dictText'
          },
          {
            title: '流程编码',
            align: 'center',
            dataIndex: 'prodefCode'
          },
          {
            title: '流程名称',
            align: 'center',
            dataIndex: 'prodefName'
          },
          {
            title: '单元类型',
            align: 'center',
            dataIndex: 'dylx_dictText'
          },
          {
            title: '登记类型',
            align: 'center',
            dataIndex: 'djlx_dictText'

          },
          {
            title: '权利类型',
            align: 'center',
            dataIndex: 'qllx_dictText'
          },
          {
            title: '是即时时办理',
            align: 'center',
            dataIndex: 'sfjsbl_dictText'
          },
          {
            title: '是否启用',
            align: 'center',
            dataIndex: 'sfqy_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/modules/wfi_prodef/list',
          delete: '/modules/wfi_prodef/getdelete',
          deleteBatch: '/modules/wfi_prodef/deleteBatch',
          exportXlsUrl: 'modules/wfi_prodef/exportXls',
          importExcelUrl: 'modules/wfi_prodef/importExcel'
        }
      }
    },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
      showmaterclass(record) {
        this.prodefid = record.prodefId;
        this.$nextTick(() => {
          this.$refs.materclasslist.visible = true;
          this.$refs.materclasslist.showData();
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>