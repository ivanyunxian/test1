<template>
  <div>
  <a-card :bordered="false" v-show="tableshow" >
    <div class="table-page-search-wrapper">
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
              <a-input placeholder="请输入项目名称"  v-model="queryParam.projectName"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
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
    <div>
      <a-card
        style="margin-top: 24px"
        :bordered="false"
        title="">

        <a-list size="large"
                ref="list"
                :loading="loading"
                :pagination="{pageSize: 10, total: total, onChange:handleListChange}"
        >
          <a-list-item :key="index" v-for="(item, index) in data">
            <a-list-item-meta :description="item.PRODEF_NAME" >
              <a-avatar slot="avatar" size="large" shape="square" :src="pic.src"/>
              <a slot="title">{{ item.PROJECT_NAME }}</a>
            </a-list-item-meta>
            <div slot="actions">
              <a @click="showData(item.PROLSH)" >查看</a>
              <a-divider type="vertical"/>

            </div>

            <div slot="actions">
              <a-dropdown>
                <a-menu slot="overlay">
                  <!-- <a-menu-item><a>编辑</a></a-menu-item>-->
                  <a-menu-item v-show="item.SHZT==20||item.SHZT==30">
                    <a @click="printReceipt(item.PROLSH)" >打印回执单</a>
                  </a-menu-item>
                  <a-menu-item v-show="item.SHZT==30">
                    <a @click="printDjsf(item.PROLSH)" >打印收费单</a>
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


            <div class="list-content">
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
    <orderacceptproject-modal ref="modalForm" @ok="modalFormOk"></orderacceptproject-modal>
    <wfi_slxmsh-list ref="wfi_slxmsh" @ok="modalFormOk"></wfi_slxmsh-list>
    <recipient-modal ref="recipientModalForm"  @ok="modalFormOk"></recipient-modal>
    <djsf-info-modal ref="djsfmodal"  @ok="modalFormOk"></djsf-info-modal>
  </a-card>

  </div>

</template>

<script>
  import { getAction } from '@/api/manage'
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import OrderacceptprojectModal from '../wfiproinst/OrderAcceptProjectModal'
  import pic1 from '@/assets/logobdc.png'
  import Wfi_slxmshList from '../wfiproinst/modules/Wfi_slxmshList'
  import recipientModal from '../Receipt/modalForm/recipient_chuxiong'
  import DjsfInfoModal from '../wfiproinst/DjsfInfoModal'


  export default {
    name: "Wfi_proinstList",
    mixins:[SupermapListMixin],
    components: {
      OrderacceptprojectModal,
      Wfi_slxmshList,
      recipientModal,
      DjsfInfoModal
    },
    data () {
      return {
        description: '业务申报已办箱',
        data : [],
        queryParam: {},
        total: 0,
        // 表头
        url: {
              projectlist: "/modules/wfi_proinst/projectlistendbox",
              delete: "/modules/wfi_proinst/delete"
           },
        projectShow:false,
        tableshow:true,
        pic:{
          name:'logo',
          src:pic1,
        },
    }
    },
    created(){

    },
    computed: {

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
            this.total = result.total;
            this.data = result.records;


          }
        })
      },
      showData(prolsh) {
            //查看
        //调整为弹窗显示
        this.$refs.modalForm.title = "项目详情";
        this.$refs.modalForm.setdisabled(prolsh);

      },handleListChange(page, pageSize) {
        this.loadData(page);
      },showendbox(){
        this.projectShow = !this.projectShow ;
        this.tableshow = !this.tableshow ;
      },
      searchQuery() {
        this.loadData();
      },
      handleListChange(page, pageSize) {
        this.loadData(page);
      },
      showSlshxm(prolsh) {
        this.$refs.wfi_slxmsh.visible = true;
        this.$refs.wfi_slxmsh.loadData(prolsh);
      },
      printReceipt(prolsh) {
        var that = this;
        that.$refs.recipientModalForm.visible = true;
        that.$refs.recipientModalForm.printRecipient(prolsh);
      },
      printDjsf(prolsh) {
        var that = this;
        that.$refs.djsfmodal.visible = true;
        that.$refs.djsfmodal.showdata(prolsh);
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