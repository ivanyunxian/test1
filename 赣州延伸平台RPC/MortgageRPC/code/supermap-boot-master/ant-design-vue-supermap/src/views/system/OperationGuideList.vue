<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="文件名称">
              <a-input placeholder="请输入文件名称" v-model="filename"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="文件类型">
              <j-dict-select-tag :triggerChage="true" placeholder="请输入文件类型" v-model="status" dictCode="WJLX" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="queryFile" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="margin-bottom:20px;">
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="true"
        :action="uploadAction"
        :supportServerRender="false"
        :beforeUpload="beforeUpload"
        @change="handleChange"
      >
        <a-button type="primary"> <a-icon type="upload" /> 上传 </a-button>
      </a-upload>
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
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->
  </a-card>
</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import { getAction, postAction } from '@/api/manage'
    export default {
      name: "OperationGuideList",
      mixins:[SupermapListMixin],
      uploadLoading:false,
      loading:false,
      data(){
        return {
          filename: '',
          status:'',
          dataSource:[],
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
              title: '文件名称',
              align: "center",
              dataIndex: 'name'
            },
            {
              title: '文件类型',
              align: "center",
              dataIndex: 'status',
              customRender:function (t,index) {
                 if(t == "100"){
                   return "驱动文件"
                 }else{
                   return '操作文档';
                 }

               }
            },
            {
              title: '上传时间',
              align: "center",
              dataIndex: 'fileindex',
              customRender:function (t,index) {
                if(t){
                  var date = new Date(t);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
                  var Y = date.getFullYear() + '-';
                  var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                  var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate() ) + ' ';
                  var h = (date.getHours() < 10 ? '0' + (date.getHours()) : date.getHours() ) + ':';
                  var m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes() ) + ':';
                  var s = (date.getSeconds() < 10 ? '0' + (date.getSeconds()) : date.getSeconds() );
                  return Y + M + D + h + m + s;
                }else{
                  return '';
                }

              }
             // new Date(endDate );
            },

            {
              title: '操作',
              dataIndex: 'action',
              align: "center",
              scopedSlots: {customRender: 'action'},
            }
          ],
          url: {
            list: "/mortgagerpc/filedata/list",
            delete: "/mortgagerpc/filedata/delete",
            deleteBatch: "/mortgagerpc/filedata/deleteBatch",
          }

        }
      },
      methods: {
        queryFile() {
          var that = this;
          this.loading = true;
          getAction(this.url.list, { filename: this.filename==""?null:this.filename ,wjlx:this.status}).then((res) => {
            if (res.success) {
              that.dataSource = res.result.records;
            } else {
              this.$message.warning(res.message)
            }
            this.loading = false;
          })
        },
        timestampToTime(timestamp) {
        	    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        	    var Y = date.getFullYear() + '-';
        	    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        	    var D = date.getDate() + ' ';
        	    var h = date.getHours() + ':';
        	    var m = date.getMinutes() + ':';
        	    var s = date.getSeconds();
        	    return Y + M + D + h + m + s;
        	},
        uploadAction: function() {
          //获取地址
          return window._CONFIG['domianURL'] + '/mongofile/templateupload?status='+this.status
        },
         getFileUploadData: function() {
            /*获取文件参数*/
          },
          beforeUpload: function(file) {

            if(this.status ==""){
              this.$message.warning('请选择文件类型!')
              return;
            }else if(this.status == "200"){
              var fileType = file.name
                if (fileType.indexOf('.doc') < 0 && fileType.indexOf('.docx')<0) {
                  this.$message.warning('请上传word!')
                  return false
                }
            }else if(this.status == "100"){
               var fileType = file.name
                 if (fileType.indexOf('.rar') < 0 ) {
                   this.$message.warning('请上传rar格式压缩文件!')
                   return false
                 }
             }

          },
        handleChange(info) {
          this.picUrl = ''
          if (info.file.status === 'uploading') {
            // this.fileindex++
            // this.longtime = this.longtime + this.fileindex
            this.uploadLoading = true
            return
          }
          if (info.file.status === 'done') {
            var response = info.file.response
            this.uploadLoading = false
            if (response.success) {
              // this.loadmaterclasslist(false)
              this.queryFile();
            } else {
              this.$message.warning(response.message)
            }
          }
        },
      }
    }
</script>

<style scoped>

</style>