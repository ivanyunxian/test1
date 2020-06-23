<template>
  <div class="card-list" ref="content">
   <!-- <label style="margin-left:30px"></label>区域：
     <a-select :defaultValue="{ key: '360702',label:'市本级' }" placeholder="请选择区域" style="margin-bottom:10px" @change="handleChange" labelInValue >
                <a-select-option value="360702">市本级</a-select-option>
                <a-select-option value="3607029">蓉江新区</a-select-option>
                <a-select-option value="3607024">开发区</a-select-option>
      </a-select>-->
    <a-list  v-show="cardshow"
      :grid="{gutter: 24, lg: 3, md: 2, sm: 1, xs: 1}"
      :dataSource="dataSource"
    >

      <a-list-item slot="renderItem" slot-scope="item, index">
          <a-card :hoverable="true">
            <a-card-meta>
              <div style="margin-bottom: 3px" slot="title">{{ item.title }}</div>
              <a-avatar class="card-avatar" slot="avatar" :src="item.avatar" size="large"/>
              <div class="meta-content" slot="description">{{ item.content }}</div>
            </a-card-meta>
            <template class="ant-card-actions" slot="actions">

              <div style="width: 100%" >
              <a-button type="primary" @click="createProjectDemo(item.title,item.prodefid,item.divisioncode)">立即申报</a-button>
        <!--        <a-modal
                  title="提示"
                  :keyboard=false
                  :mask=false
                  :maskClosable=false
                  :centered=true
                  v-model="visible"
                  @ok="createProjectOk(item.prodefid)"

                >
                  <p>即将申报以下业务:<span style="color: red;">{{ item.title }}</span>,是否继续？</p>
                </a-modal> -->
              </div>
            </template>
          </a-card>
      </a-list-item>
    </a-list>
    <a-spin ref="loadimg" tip="加载中..." style="display: none;z-index: 99999;">
      <div class="spin-content"></div>
    </a-spin>

      <!--<OrderAcceptProject v-show="projectShow" ref="oap"></OrderAcceptProject>

      <SubProjectSuccess v-show="successShow" ref="projectok"></SubProjectSuccess>-->
    <accept-project-step-form ref="acceptstep" :prolsh="prolsh"></accept-project-step-form>


  </div>


</template>

<script>

  import { axios } from '@/utils/request'
  import { getAction } from '@/api/manage'
  import {Modal, notification} from 'ant-design-vue'
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import Vue from 'vue'
  import OrderAcceptProject from '../wfiproinst/OrderAcceptProject'
  import pic1 from '@/assets/logobdc.png'
  import SubProjectSuccess from '../wfiproinst/modules/SubProjectSuccess'
  import AcceptProjectStepForm from '@/views/mortgagerpc/acceptProject/AcceptProjectStepForm'

  const dataSource = [];
  export default {
    name: "prodefCardList",
    components: {
      OrderAcceptProject,
      Vue,
      SubProjectSuccess,
      AcceptProjectStepForm
    },
    data () {
      return {
        description: '信息',
        visible : false,
        linkList: [
          { icon: 'file-text', href: '#', title: '流程信息' }
        ],
        url: {
          list: '/modules/wfi_prodef/list',
          createProject: '/modules/wfi_proinst/createProject'
        },
        wthat:this,
        extraImage: '',
        dataSource,
        projectShow:false,
        cardshow:true,
        successShow:false,
        prolsh : '',
        pic:{
          name:'logo',
          src:pic1,
         },
        prodefid : '12',
        divisioncode:{key:'360702',label:"市本级"}
      }
    },
    created() {
      if(dataSource.length == 0){
        this.loadData();
      }
    },
    methods: {
      handleChange(value){
        console.log(value);
        this.divisioncode = value;
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = {column: "createtime", order: "desc",
          field: "id,,,divisionCode_dictText,prodefCode,prodefName,d…t,sfjsbl_dictText,sfqy_dictText,prodefDesc,action", pageNo: 1, pageSize: 100};//查询条件
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            //处理数据
            // dataSource.push(null);
            for (let i = 0; i < res.result.records.length; i++) {
              dataSource.push({
                title: res.result.records[i].prodefName,
                avatar: this.pic.src,
                content: '办理业务：'+res.result.records[i].prodefDesc,
                prodefid : res.result.records[i].prodefId,
              })
            }
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
        })
      },
      createProjectDemo(t,id) {
        var that = this;
        Modal.confirm({
          title: '业务申报',
          content: "当前行政区为"+that.divisioncode.label+",即将申报以下业务："+t+",是否继续？",
          okText: '确认',
          cancelText:'取消',
          mask: false,
          width:600,
          closable: true,
          onOk:function(){
            //点击确认后的操作事件
            //获取当前选择的区域
            var code = that.divisioncode.key;
            that.createProjectOk(id,code);
          },
          onCancel:function(){

          },
          centered: true
        })
      },
      createProjectOk(prodefid,code){
        //确定后立即创建对应的项目
        getAction(this.url.createProject, {id:prodefid,code:code}).then((res) => {
          if(res.success) {
            var prolsh = res.message;
            this.prolsh = prolsh;
            this.$message.success("创建项目成功");
            this.$refs.acceptstep.visible = true;
            this.$refs.acceptstep.initStep1Page();
          } else {
            this.$message.error(res.message);
          }
        })

      },showCard(){
        this.projectShow = !this.projectShow ;
        this.cardshow = !this.cardshow ;
      },
      subsuccess (ywlsh) {
        this.projectShow = false ;
        this.cardshow = false;
        this.successShow = true;
        this.$refs.projectok.initsbuPage(ywlsh);
      },
      nextProject() {
        this.projectShow = false ;
        this.cardshow = true;
        this.successShow = false;
      }
    }
  }
</script>


<style lang="scss" scoped>
  .card-avatar {
    width: 48px;
    height: 48px;
    border-radius: 48px;
  }

  .ant-card-actions {
    background: #f7f9fa;
    li {
      float: left;
      text-align: center;
      margin: 12px 0;
      color: rgba(0, 0, 0, 0.45);
      width: 50%;

      &:not(:last-child) {
        border-right: 1px solid #e8e8e8;
      }

      a {
        color: rgba(0, 0, 0, .45);
        line-height: 22px;
        display: inline-block;
        width: 100%;
        &:hover {
          color: #1890ff;
        }
      }
    }
  }

  .new-btn {
    background-color: #fff;
    border-radius: 2px;
    width: 100%;
    height: 188px;
  }

  .meta-content {
    position: relative;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    height: 64px;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
  }
</style>