<template>
  <div>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper" v-show="cardshow">
        <a-form layout="inline">
          <a-row :gutter="24">

            <a-col :md="6" :sm="8">
              <a-form-item label="用户名称">
                <a-input placeholder="请输入用户名称" v-model="queryParam.realname"></a-input>
              </a-form-item>
            </a-col>

            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="padding-left:32px;background:#fff">
      <a-button @click="handleAddParcel" type="primary" icon="plus">新增</a-button>
    </div>
    <!-- table区域-begin -->
    <div v-show="cardshow">
      <a-card
        :bordered="false"
        title="人员补录列表">
        <a-list size="large"
                ref="list"
                :loading="loading"
                :pagination="{pageSize: 10, total: total, onChange:handleListChange}"
        >
          <a-list-item :key="index" v-for="(item, index) in data">
            <a-list-item-meta :description="item.realname">
              <a slot="title">真实姓名</a>
            </a-list-item-meta>
            <div class="list-content">
              <div class="list-content-item">
                <span>用户名</span>
                <p>{{ item.username }}</p>
              </div>
              <div class="list-content-item">
                <span>电话</span>
                <p>{{ item.phone }}</p>
              </div>
              <div class="list-content-item">
                <span>证件号</span>
                <p>{{ item.zjh }}</p>
              </div>
              <div class="list-content-item">
                <span>提交时间</span>
                <p>{{ item.createTime }}</p>
              </div>
              <div class="list-content-item">
                <span>状态</span>
                <p>{{ item.status_text }}</p>
              </div>
               <div class="list-content-item">
                <span>操作</span>
                <p><a-popconfirm title="确定要删除吗?" @confirm="() => delProject(item.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </p>
                </div>
            </div>
          </a-list-item>
        </a-list>
      </a-card>
    </div>
    <!-- table区域-end -->
    <!--新增宗地-->
    <add-parcel-modal ref="addparcel" :flag="'personal'" :enterpriseid="enterpriseid"></add-parcel-modal>
  </div>
</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import { getAction,deleteAction} from '@/api/manage'
  import AddParcelModal from './modules/AddParcelModal'
    export default {
      name: "PersonnelCollection",
      mixins:[SupermapListMixin],
      components: {
        AddParcelModal
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
            projectlist: "/sys/user/zrzlist",
            delete: "/sys/user/enterprise/delete",
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
          param.type = "2";
          getAction(this.url.projectlist, param).then((res) => {
            if (res.success) {
              var result = res.result ;
              console.log( result.records);
              this.total = result.total;
              this.convert(result.records);
              this.data = result.records;
            }else{
              this.total = 0;
              this.data = [];
            }
            this.loading = false;
            this.enterpriseid =res.message;
          })
        },
        convert(data){
          for(var i in data){
            if(data[i].shzt == "0"){
              data[i].status_text ="未提交";
            }else if(data[i].shzt == "10"){
              data[i].status_text ="待审核";
            }else if(data[i].shzt == "30"){
              data[i].status_text ="驳回";
            }else{
              data[i].status_text ="未知状态";
            }
          }
        },
        searchQuery() {
          this.loadData();
        },
        handleListChange(page, pageSize) {
          this.loadData(page);
        },
        delProject(id) {
          var that = this;
          console.log("主页面"+this.enterpriseid);
          if (!this.url.delete) {
            this.$message.error("请设置url.delete属性!")
            return
          }
          var that = this;
          deleteAction(that.url.delete, {id: id}).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.loadData();
            } else {
              that.$message.warning(res.message);
            }
          });
        },
        handleAddParcel(){
          this.$refs.addparcel.visible = true;
          this.$refs.addparcel.title = '新增人员';
          this.$refs.addparcel.loadData("1");
        }
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
    text-align:center;
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