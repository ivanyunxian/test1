<template>
  <a-card>
    <form :autoFormCreate="(form) => this.form = form">
      <a-table
        :columns="columns"
        :dataSource="data"
        :pagination="false"
      >
        <template slot="operation" slot-scope="text, record, index">
          <span class="setButton" v-if="!disabled" >
              <a  @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <!--<a-popconfirm title="是否要删除此行？" @confirm="remove(record.id)">
                <a>删除</a>
              </a-popconfirm>-->
          </span>
          <span class="setButton" v-else >
              <a  @click="handleEdit(record)">详情</a>
              <a-divider type="vertical" />
          </span>
        </template>
      </a-table>
      <!--<a-button v-if="!disabled" class="setButton" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" @click="handleAdd">添加</a-button>-->
    </form>
    <sqrmessage-modal ref="modalForm" @ok="modalFormOk" :disabled="disabled" :qlpageStatus="qlpageStatus" :dyid="dyid" :prolsh="prolsh"></sqrmessage-modal>
  </a-card>
</template>

<script>
  import SqrmessageModal from '../SqrmessageModal'
  import { getAction } from '@/api/manage'

  export default {
    name: "QlrmessageForm",
    components: {
      SqrmessageModal
    },
    data () {
      return {
        form: this.$form.createForm(this),
        qlpageStatus:true,
        data : [],
        columns: [
          {
            title: '权利人名称',
            dataIndex: 'sqrxm',
            key: 'sqrxm',
            width: '12%',
            scopedSlots: { customRender: 'sqrxm' }
          },
          {
            title: '权利人证件号',
            dataIndex: 'zjh',
            key: 'sqrzjh',
            width: '15%',
            scopedSlots: { customRender: 'sqrzjh' }
          },
          {
            title: '不动产权证号/证明',
            dataIndex: 'bdcqzh',
            key: 'bdcqzh',
            width: '18%',
            scopedSlots: { customRender: 'bdcqzh' }
          },
          {
            title: '共有情况',
            dataIndex: 'gyfs_dictText',
            key: 'gyfs',
            width: '10%',
            scopedSlots: { customRender: 'gyfs' }
          },
          {
            title: '联系电话',
            dataIndex: 'lxdh',
            key: 'lxdh',
            width: '10%',
            scopedSlots: { customRender: 'lxdh' }
          },
          {
            title: '代理人',
            dataIndex: 'dlrxm',
            key: 'dlrxm',
            width: '10%',
            scopedSlots: { customRender: 'dlrxm' }
          },
          {
            title: '代理人证件号',
            dataIndex: 'dlrzjhm',
            key: 'dlrzjhm',
            width: '13%',
            scopedSlots: { customRender: 'dlrzjhm' }
          },
          {
            title: '操作',
            key: 'action',
            width: '17%',
            scopedSlots: { customRender: 'operation' }
          }
        ],
        url:{
          sqrlist : "/modules/bdc_sqr/sqrlist",
          delete : "/modules/bdc_sqr/delete"
        },
        urlpath : ''
      }
    },
    created(){
    },
    props:['prolsh','disabled','dyid'],
    methods: {
      loadData() {
        var that = this;
        if (!this.url.sqrlist) {
          this.$message.error("请设置url.projectlist属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        var param = {'prolsh':this.prolsh,'sqrlb':'1','dyid':this.dyid};
        getAction(this.url.sqrlist, param).then((res) => {
          if (res.success) {
            this.data = res.result ;
            if(this.urlpath.indexOf("projectEndlist") != -1){
              that.columns = [
                {
                  title: '权利人名称',
                  dataIndex: 'sqrxm',
                  key: 'sqrxm',
                  width: '12%',
                  scopedSlots: { customRender: 'sqrxm' }
                },
                {
                  title: '权利人证件号',
                  dataIndex: 'zjh',
                  key: 'sqrzjh',
                  width: '15%',
                  scopedSlots: { customRender: 'sqrzjh' }
                },
                {
                  title: '不动产权证号/证明',
                  dataIndex: 'bdcqzh',
                  key: 'bdcqzh',
                  width: '18%',
                  scopedSlots: { customRender: 'bdcqzh' }
                },
                {
                  title: '共有情况',
                  dataIndex: 'gyfs_dictText',
                  key: 'gyfs',
                  width: '10%',
                  scopedSlots: { customRender: 'gyfs' }
                },
                {
                  title: '联系电话',
                  dataIndex: 'lxdh',
                  key: 'lxdh',
                  width: '10%',
                  scopedSlots: { customRender: 'lxdh' }
                },
                {
                  title: '代理人',
                  dataIndex: 'dlrxm',
                  key: 'dlrxm',
                  width: '10%',
                  scopedSlots: { customRender: 'dlrxm' }
                },
                {
                  title: '代理人证件号',
                  dataIndex: 'dlrzjhm',
                  key: 'dlrzjhm',
                  width: '15%',
                  scopedSlots: { customRender: 'dlrzjhm' }
                }
              ]
            }
          }
        })
      },
      handleAdd: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.show();
        this.$refs.modalForm.sqrlb = '1';
        this.$refs.modalForm.sqrlxdesc = '权利人';
        this.$refs.modalForm.title = "添加权利人";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.qlpageStatus = true;
      },
      handleEdit: function (record) {
        this.$refs.modalForm.show();
        this.$refs.modalForm.sqrlxdesc = '权利人';
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "修改权利人";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.qlpageStatus = true;
      },
      remove(id){
        if(!id) {
          this.$message.warning("删除失败，找不到该数据");
          return ;
        }
        getAction(this.url.delete, {id:id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning("删除失败:"+res.message);
          }
        })
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
      },
      setProlsh(prolsh,urlpath) {
        this.prolsh = prolsh;
        this.urlpath = urlpath;
        this.$refs.modalForm.setProlsh(prolsh);
      }

    }
  }
</script>

<style scoped>

</style>