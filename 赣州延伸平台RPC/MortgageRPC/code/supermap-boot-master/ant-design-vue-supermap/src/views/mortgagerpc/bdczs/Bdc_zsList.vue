<template>
  <a-card :bordered="false">

    <!-- table区域-begin -->
    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="certifyInfo(record)">详情</a>
          &nbsp; &nbsp;
          <a v-if="record.pdf!=null||record.mongoid!=null" @click="show(record)">电子证明</a>
        </span>


      </a-table>
    </div>
    <!-- table区域-end -->
    <certifyInfo-modal ref="certifyinfomodal" @ok="modalFormOk"></certifyInfo-modal>
    <imag-preview ref="imagpreview"
                                  :disabled="disabled"/>
  </a-card>
</template>

<script>
  import { SupermapListMixin } from '@/mixins/SupermapListMixin'
  import { getAction } from '@/api/manage'
  import certifyInfoModal from '../wfiproinst/certifyInfoModal'
  import ImagPreview from '@/views/mortgagerpc/acceptProject/modalForm/ImagPreview'

  export default {
    name: "Bdc_zsList",
    mixins:[SupermapListMixin],
    components: {
      certifyInfoModal,
      ImagPreview
    },
    data () {
      return {
        description: '不动产抵押证明',
        // 表头
        columns: [
          {
            dataIndex: 'zsid',
            key:'rowIndex',
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '证明权利或事项',
            align:"center",
            dataIndex: 'zmqlhsx'
          },
          {
            title: '义务人',
            align:"center",
            dataIndex: 'ywr'
          },
          {
            title: '权利人',
            align:"center",
            dataIndex: 'qlr'
          },
          {
            title: '抵押证明号',
            align:"center",
            dataIndex: 'bdcqzh'
          },
          {
            title: '坐落',
            align:"center",
            dataIndex: 'zl'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/modules/bdc_zs/list",

        },
      }
    },
    props:['prolsh'],
    created() {

    },
    methods: {
      loadData(arg){
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = {prolsh:this.prolsh};
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },
      certifyInfo(record) {
        this.$refs.certifyinfomodal.visible = true;
        this.$refs.certifyinfomodal.showdata(record);
      },
      show(record){
        this.$refs.imagpreview.loadFileData_cert(record.zsid);
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>