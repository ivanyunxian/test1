<template>
  <div>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper" v-show="cardshow">
        <a-form layout="inline">
          <a-row :gutter="24">

            <a-col :md="6" :sm="8">
              <a-form-item label="业务流水号">
                <a-input placeholder="请输入业务流水号" v-model="queryParam.prolsh"></a-input>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="项目名称">
                  <a-input placeholder="请输入项目名称" v-model="queryParam.projectName"></a-input>
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
            </a-col>

          </a-row>
        </a-form>
      </div>

      <!-- table区域-begin -->
      <div v-show="cardshow">
        <a-card
          style="margin-top: 24px"
          :bordered="false"
          title="在办箱列表">

          <a-list size="large"
                  ref="list"
                  :loading="loading"
                  :pagination="{pageSize: 10, total: total, onChange:handleListChange}"
          >
            <a-list-item :key="index" v-for="(item, index) in data">
              <a-list-item-meta :description="item.PRODEF_NAME">
                <a-avatar slot="avatar" size="large" shape="square" :src="pic.src"/>
                <a slot="title">{{ item.PROJECT_NAME }}</a>
              </a-list-item-meta>
              <div slot="actions" @click="editProject(item.PROLSH,item.PRODEF_ID)">
                <a>编辑</a>
              </div>
              <div slot="actions">
                <a-dropdown>
                  <a-menu slot="overlay">
                    <!-- <a-menu-item><a>编辑</a></a-menu-item>-->
                    <a-menu-item>
                      <a-popconfirm title="确定删除吗?" @confirm="() => deleteProject(item.PROLSH)">
                        <a>删除</a>
                      </a-popconfirm>
                    </a-menu-item>
                    <a-menu-item>
                        <a @click="showSlshxm(item.PROLSH)" >审核过程</a>
                    </a-menu-item>
                  </a-menu>
                  <a>更多
                    <a-icon type="down"/>
                  </a>
                </a-dropdown>
              </div>
              <div class="list-content" style="">
                <div class="list-content-item">
                  <span>业务流水号</span>
                  <p>{{ item.PROLSH }}</p>
                </div>
                <div class="list-content-item">
                  <span>申请人</span>
                  <p>{{ item.ACCEPTOR }}</p>
                </div>
                <div class="list-content-item">
                  <span>开始时间</span>
                  <p>{{ item.CREAT_DATE }}</p>
                </div>
                <div class="list-content-item">
                  <span>状态</span>
                  <p>{{ item.SHZT_dictText }}</p>
                </div>
              </div>
            </a-list-item>
          </a-list>
        </a-card>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <wfi_proinst-modal ref="modalForm" @ok="modalFormOk"></wfi_proinst-modal>
    </a-card>
    <div>
      <!--<OrderAcceptProject v-show="projectShow" ref="oap"></OrderAcceptProject>
      <SubProjectSuccess v-show="successShow" ref="projectok"></SubProjectSuccess>-->
      <accept-project-step-form ref="acceptstep" :prodefid = "prodefid" :prolsh="prolsh"></accept-project-step-form>
      <wfi_slxmsh-list ref="wfi_slxmsh" @ok="modalFormOk"></wfi_slxmsh-list>
    </div>
  </div>
</template>

<script>
  import Wfi_proinstModal from './modules/Wfi_proinstModal'
  import { getAction } from '@/api/manage'
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import AcceptProjectStepForm from '@/views/mortgagerpc/acceptProject/AcceptProjectStepForm'
  import Wfi_slxmshList from '../wfiproinst/modules/Wfi_slxmshList'
  import pic1 from '@/assets/logobdc.png'


  export default {
    name: "Wfi_proinstList",
    mixins:[SupermapListMixin],
    components: {
      Wfi_proinstModal,
      AcceptProjectStepForm,
      Wfi_slxmshList
    },
    data () {
      return {
        description: '业务申报在办箱',
        data : [],
        queryParam: {},
        // 表头
        total:0,
        url: {
              projectlist: "/modules/wfi_proinst/projectlist",
              projectDelete: "/modules/wfi_proinst/delete"
        },
        cardshow:true,
        projectShow:false,
        successShow : false,
        prodefid : '',
        prolsh : '',
      pic:{
        name:'logo',
          src:pic1,
      },
      }
    },
    created(){
    },
    methods: {
      loadData(arg) {
        if (!this.url.projectlist) {
          this.$message.error("请设置url.projectlist属性!")
          return
        }
        var param = {};
        if (arg) {
          //加载数据 若传入参数1则加载第一页的内容
          param.pageNo = arg;
        }
        param = Object.assign(param, this.queryParam);
        getAction(this.url.projectlist, param).then((res) => {
          if (res.success) {
            var result = res.result ;
            console.log( result.records);
            this.total = result.total;
            this.data = result.records;
          }
        })
      },
      searchQuery() {
        this.loadData();
      },
      handleListChange(page, pageSize) {
        this.loadData(page);
      },
      editProject(prolsh,prodefid) {
        var that = this;
        /*that.projectShow = !that.projectShow ;
        that.cardshow = !that.cardshow ;
        that.$refs.oap.getprolsh(prolsh);*/
        that.$refs.acceptstep.visible = true;
        this.$refs.acceptstep.prodefid = prodefid;
        this.prolsh = prolsh;
        this.$refs.acceptstep.currentTab = 0;
        this.$refs.acceptstep.initStep1Page();
      },
      showproinstbox(){
        this.projectShow = !this.projectShow ;
        this.cardshow = !this.cardshow ;
      },
      deleteProject(prolsh){
        var that = this;
        getAction(this.url.projectDelete, {prolsh:prolsh}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            this.loadData();
          } else {
            that.$message.error(res.message)
          }
        })
      },
      subsuccess (ywlsh,urlpath) {
        this.projectShow = false ;
        this.cardshow = false;
        this.successShow = true;
        this.$refs.projectok.initsbuPage(ywlsh,urlpath);
      },
      nextProject() {
        this.projectShow = false ;
        this.cardshow = true;
        this.successShow = false;
      },
      showSlshxm(prolsh) {
        this.$refs.wfi_slxmsh.visible = true;
        this.$refs.wfi_slxmsh.loadData(prolsh);
      }

    }
  }

</script>
<style scoped>
  @import '~@assets/less/common.less';

  .list-content-item {
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    vertical-align: middle;
    font-size: 14px;
    margin-left: 40px;
  }
  span {
    line-height: 20px;
  }
  p {
    margin-top: 4px;
    margin-bottom: 0;
    line-height: 22px;
  }


</style>