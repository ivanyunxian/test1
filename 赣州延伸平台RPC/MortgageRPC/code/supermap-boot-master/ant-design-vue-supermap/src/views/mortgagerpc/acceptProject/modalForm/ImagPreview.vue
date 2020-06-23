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
    <a-col :span="18">
      <a-spin tip="Loading..." :spinning="spinning">
        <div>
          <a-row>
            <a-col :span="18">
              <p>
                <a-divider orientation="left">{{matername}}</a-divider>
              </p>
            </a-col>
          </a-row>
          <a-row>
            <!-- 预览区域 -->
            <a-col :span="24">
              <template>

                <div class="uploadlist">
                  <a-upload
                    :action="uploadmodalAction"
                    listType="picture-card"
                    :fileList="fileList"
                    :data="materclass"
                    :showUploadList="showUploadList"
                    :beforeUpload="beforeUpload"
                    :remove="removefile"
                    @preview="handlePreview"
                    @change="uploadhandleChange">
                  <!--  <div >
                      <a-icon type="plus" />
                      <div class="ant-upload-text">上传</div>
                    </div>-->
                  </a-upload>
                </div>
                <a-modal :visible="previewVisible"
                         :width="modal.width"
                         :style="modal.style" :footer="null" @cancel="handleCancel">
                    <img v-show="picVisible" alt="example" style="width: 99%;height:820px" :src="previewImage" />
                    <embed v-show="pdfVisible" :src="previewImage" type="application/pdf" style="width: 99%;height:820px">
                    <div class="action" v-show="true" style=" text-align: center;">
                      <a-button @click="prev" type="primary" style="margin-right: 8px" >上一张</a-button>
                      <a-button @click="next" type="primary" style="margin-left: 8px" >下一张</a-button>
                    </div>
                </a-modal>
              </template>
            </a-col>
          </a-row>
        </div>

      </a-spin>
      <p></p>
    </a-col>
  </a-card>
  </a-modal>
</template>

<script>

  import ARow from 'ant-design-vue/es/grid/Row'
  import { getAction, postAction } from '@/api/manage'

  export default {
    name: 'ImagPreview',
    components: {
      ARow
    },
    data() {
      return {
        description: '附件预览',
        spinning:false,
        //数据集
        dataSource: [{
          key:0,
          fileDetails:[
          ]
        }],
        modal: {
          width: '90%',
          style: { top: '10px' }
        },
        visible : false,
        matername:'',
        url: {
          getMaterlistFiles:'/mongofile/getMaterlistFiles',
          removeFile:'/mongofile/removeFile',
          certFile:'/mongofile/getZSFiles'
        },
        fileList:[],
        previewVisible: false,
        pdfVisible :false,
        picVisible:false,
        previewImage: '',
        showUploadList:true,
        sort : 0
      }
    },
    created() {
    },
    props:['materclass','disabled'],
    methods: {
      loadFileData(name) {
        this.$nextTick(() => {
          if(this.disabled) {
            this.showUploadList={showRemoveIcon:false,showPreviewIcon:true}
          } else {
            this.showUploadList=true
          }
          this.dataSource=[];
          this.visible = true;
          this.matername = name;
          var param = { materclassid: this.materclass.id };
          getAction(this.url.getMaterlistFiles, param).then((res) => {
            if (res.success) {
              var filedatas = res.result;
              for (var index in filedatas) {
                var filedata = filedatas[index];
                filedata.uid = filedata.id;
                filedata.url = '/mrpc/mongofile/view?mongoid=' + filedata.mongoid;
              }
              this.fileList = filedatas;
            } else {
              this.$message.error(res.message);
            }

          })
        })
      },
      loadFileData_cert(zsid) {
              this.$nextTick(() => {
                if(this.disabled) {
                  this.showUploadList={showRemoveIcon:false,showPreviewIcon:true}
                } else {
                  this.showUploadList=true
                }
                this.dataSource=[];
                this.visible = true;
                //this.matername = name;
                var param = { zsid: zsid };
                getAction(this.url.certFile, param).then((res) => {
                  if (res.success) {
                    var filedatas = res.result;
                    for(var index in filedatas){
                      var filedata = filedatas[index];
                      filedata.uid = filedata.zsid;
                      filedata.url = '/mrpc/mongofile/viewcert?mongoid=' + filedata.mongoid;
                    }
                      this.fileList = filedatas;
                  } else {
                    this.$message.error(res.message);
                  }
                })
              })
            },
      uploadmodalAction() {
        return window._CONFIG['domianURL']+"/mongofile/upload";
      },
      beforeUpload: function(file){
        var fileType = file.type;
        if(fileType.indexOf('image')<0){
          this.$message.warning('请上传图片');
          return false;
        }
      },
      uploadhandleChange(info) {
        if (info.file.status === 'uploading') {
          return
        }
        if (info.file.status === 'done') {
          var response = info.file.response;
          if(response.success){
            this.loadFileData(this.matername);
          }else{
            this.$message.warning(response.message);
          }
        }
      },
      prev(){
              if(this.sort === 0){
                this.sort = this.fileList.length-1;
              }else{
                this.sort = this.sort - 1;
              }
              this.previewImage = this.fileList[this.sort].url || this.fileList[this.sort].thumbUrl;
              if(this.fileList[this.sort].suffix == '.pdf'){
                this.pdfVisible = true
                this.picVisible = false
              }else{
                this.pdfVisible = false
                this.picVisible = true
              }
            },
      next(){
        if(this.sort === this.fileList.length-1){
          this.sort = 0;
        }else{
          this.sort = this.sort + 1;
        }
        this.previewImage = this.fileList[this.sort].url || this.fileList[this.sort].thumbUrl;
        if(this.fileList[this.sort].suffix == '.pdf'){
          this.pdfVisible = true
          this.picVisible = false
        }else{
          this.pdfVisible = false
          this.picVisible = true
        }
      },
      close () {
        this.$emit('loadmaterclasslist', true);
        this.visible = false;
      },
      handlePreview (file) {
        this.previewImage = file.url || file.thumbUrl
        if(file.suffix == '.pdf'){
          this.pdfVisible = true
          this.picVisible = false
        }else{
          this.pdfVisible = false
          this.picVisible = true
        }
        this.previewVisible = true
      },
      handleCancel () {
        this.previewVisible = false
      },
      removefile(file) {
        var that = this;
        this.$confirm({
          title:"确认删除",
          content:"确定删除该附件?",
          onOk: function() {
            getAction(that.url.removeFile, {id:file.id}).then((res) => {
              if (res.success) {
                that.loadFileData(that.matername);
                that.$message.success(res.message);
              } else {
                that.$message.error(res.message);
              }
            })
          }
        })
      }
    }
  }
</script>
<style scoped>

  .table-operator {
    margin-bottom: 10px
  }

  .clName .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle {
    width: 10px !important;
  }

  .clName .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
    background-color: #1890FF !important;
  }

  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
</style>