<template>
  <div>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper" v-show="cardshow">
        <a-form layout="inline">
          <a-row :gutter="24">

             <a-col :md="6" :sm="8">
               <a-form-item label="企业名称">
                 <a-input placeholder="请输入企业名称" v-model="queryParam.enterpriseName"></a-input>
               </a-form-item>
             </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="驳回验证码">
                <a-input placeholder="请输入驳回验证码" v-model="queryParam.bhYzm"></a-input>
              </a-form-item>
            </a-col>

            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
            <!--<template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="企业地址">
                  <a-input placeholder="请输入企业地址" v-model="queryParam.enterpriseAddress"></a-input>
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
            </a-col>-->

          </a-row>
        </a-form>
      </div>
    </a-card>

    <!-- table区域-begin -->
    <div v-show="cardshow">
      <a-card
        style="margin-top: 24px"
        :bordered="false"
        title="企业申请信息列表">

        <a-list size="large"
                ref="list"
                :loading="loading"
                :pagination="{pageSize: 10, total: total, onChange:handleListChange}"
        >
          <a-list-item :key="index" v-for="(item, index) in data">
            <a-list-item-meta :description="item.ENTERPRISE_NAME">
              <a slot="title">{{ item.ENTERPRISE_NAME }}</a>
            </a-list-item-meta>
            <div slot="actions" @click="editProject(item.ID)">
              <a>编辑</a>
            </div>
            <div class="list-content">
              <div class="list-content-item">
                <span>企业地址</span>
                <p>{{ item.ENTERPRISE_ADDRESS }}</p>
              </div>
              <div class="list-content-item">
                <span>注册者姓名</span>
                <p>{{ item.REGISTER_NAME }}</p>
              </div>
              <div class="list-content-item">
                <span>统一社会信用代码</span>
                <p>{{ item.ENTERPRISE_CODE }}</p>
              </div>
              <div class="list-content-item">
                <span>状态</span>
                <p>{{ item.STATUS_TEXT }}</p>
              </div>
            </div>
          </a-list-item>
        </a-list>
      </a-card>
    </div>
    <!-- table区域-end -->
    <edit-list-modal ref="acceptstep" :enterpriseid="enterpriseid"></edit-list-modal>
  </div>
</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import { getAction } from '@/api/manage'
  import EditListModal from './modules/EditListModal'
  export default {
    name: 'EnterpriseList',
    mixins:[SupermapListMixin],
    components:{
      EditListModal
    },
    data () {
      return {
        queryParam: {},
        cardshow:true,
        description: '企业申请信息',
        total:0,
        data : [],
        loading:false,
        url: {
          projectlist: "/modules/yspt/projectlist",
        },
        enterpriseid:'',
      }
    },
    methods: {
      loadData(arg) {
        if (!this.url.projectlist) {
          this.$message.error("请设置url.projectlist属性!")
          return
        }
        var param = {};
        this.loading = true;
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
          }else{
             this.total = 0;
             this.data = [];
          }
          this.loading = false;
        })
      },
      searchQuery() {
        this.loadData();
      },
      handleListChange(page, pageSize) {
        this.loadData(page);
      },
      editProject(id) {
        var that = this;
        this.enterpriseid = id;
        that.$refs.acceptstep.loadecditData();
        that.$refs.acceptstep.visible = true;
        /*this.$refs.acceptstep.prodefid = prodefid;
        this.prolsh = prolsh;
        this.$refs.acceptstep.currentTab = 0;
        this.$refs.acceptstep.initStep1Page();*/
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .ant-list-item-meta{
    flex:none;
    width:30%;
  }
  .list-content{
    flex:1;
    display:flex;
  }
  .list-content-item {
    color: rgba(0, 0, 0, .45);
    font-size: 14px;
    margin-left: 40px;
    flex:1;
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