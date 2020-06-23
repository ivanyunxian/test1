<template>
  <div>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper" v-show="cardshow">
        <a-form layout="inline">
          <a-row :gutter="24">

            <a-col :md="6" :sm="8">
              <a-form-item label="不动产单元号">
                <a-input placeholder="请输入不动产单元号" v-model="queryParam.bdcdyh"></a-input>
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
        title="宗地补录列表">
        <a-list size="large"
                ref="list"
                :loading="loading"
                :pagination="{pageSize: 10, total: total, onChange:handleListChange}"
        >
          <a-list-item :key="index" v-for="(item, index) in data">
            <a-list-item-meta :description="item.zl">
               <a slot="title" >坐落</a>
            </a-list-item-meta>
            <div class="list-content">
              <div class="list-content-item">
                <span>不动产单元号</span>
                <p>{{ item.bdcdyh }}</p>
              </div>
              <div class="list-content-item">
                <span>查封状态</span>
                <p>{{ item.cfzt == 1 ? '已查封':'无' }}</p>
              </div>
              <div class="list-content-item">
                <span>抵押状态</span>
                <p>{{ item.dyzt == 1 ? '已抵押':'无' }}</p>
              </div>
              <div class="list-content-item">
                <span>异议状态</span>
                <p>{{ item.yyzt == 1 ? '存在异议':'无' }}</p>
              </div>
              <div class="list-content-item">
                <span>宗地面积</span>
                <p>{{ item.zdmj }}</p>
              </div>
              <div class="list-content-item">
                <span>提交时间</span>
                <p>{{ item.createtime }}</p>
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
    <add-parcel-modal ref="addparcel" :flag="flag" :enterpriseid="enterpriseid"></add-parcel-modal>
  </div>
</template>

<script>
    import { SupermapListMixin } from '@/mixins/SupermapListMixin'
    import { getAction,deleteAction} from '@/api/manage'
    import AddParcelModal from './modules/AddParcelModal'
    export default {
      name: "ParcelCollection",
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
            projectlist: "/modules/yspt/shyqzd/bllist",
            delete:'/modules/yspt/shyqzd/bdc_shyqzd/delete'
          },
          enterpriseid:'',
          flag:"parcel"
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
              this.convert(result.records);
              this.data = result.records;
            }else{
              this.total = 0;
              this.data = [];
            }
            this.enterpriseid =res.message;
            this.loading = false;
          })
        },
        convert(data){
          for(var i in data){
            if(data[i].status == "-1"){
              data[i].status_text ="未提交";
            }else if(data[i].status == "0"){
              data[i].status_text ="待审核";
            }else if(data[i].status == "2"){
              data[i].status_text ="驳回";
            }else{
              data[i].status_text ="未知状态";
            }
          }
        },
        searchQuery() {
          this.loadData();
          //this.enterpriseid = result.records[0].enterpriseid;
        },
        handleListChange(page, pageSize) {
          this.loadData(page);
        },
        delProject(id) {
          var that = this;
          //this.enterpriseid = id;
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
          this.$refs.addparcel.title = '新增宗地'
          this.$refs.addparcel.loadData("2");
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