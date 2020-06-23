<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="模板内容">
              <a-input placeholder="请输入模板内容" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
         <a-col :md="6" :sm="8">
           <a-form-item label="模板类型">
             <j-dict-select-tag :triggerChage="true" placeholder="请输入文件类型" v-model="queryParam.type" dictCode="CSLX" />
           </a-form-item>
         </a-col>
          <a-col :md="6" :sm="8" >
            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('系统参数')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
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
    <fj-template-modal ref="modalForm" @ok="modalFormOk"></fj-template-modal>
  </a-card>
</template>

<script>
    import FjTemplateModal from './modules/FjTemplateModal'
    import { SupermapListMixin } from '@/mixins/SupermapListMixin'
    export default {
      name: "FjTemplate",
      components: {FjTemplateModal},
      mixins:[SupermapListMixin],
      data () {
        return {
          description: '通用模板',
          // 表头
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
              title: '模板内容',
              align: "center",
              dataIndex: 'value'
            },
           {
              title: '模板类型',
              align: "center",
              dataIndex: 'type'
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: "center",
              scopedSlots: {customRender: 'action'},
            }
          ],
          url: {
            list: "/mortgagerpc/sys_config/list",
            delete: "/mortgagerpc/sys_config/delete",
            deleteBatch: "/mortgagerpc/sys_config/deleteBatch",
            exportXlsUrl: "mortgagerpc/sys_config/exportXls",
            importExcelUrl: "mortgagerpc/sys_config/importExcel",
          },
        }
      },
      computed: {
        importExcelUrl: function(){
          return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
      }
    }
</script>


<style scoped>
  @import '~@assets/less/common.less'
</style>