<template>
  <a-modal
    :width="800"
    :visible="visible"
    title="检索宗地不动产单元号"
    :confirmLoading="false"
    @ok="close"
    @cancel="close"
    :footer="null"
    okText="确定"
    cancelText="关闭"
  >
    <div class="table-page-search-wrapper" >
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="18" :sm="16">
            <a-form-item label="不动产单元号">
              <a-input placeholder="不动产单元号(模糊检索)" v-model="queryParam.bdcdyh"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="loadData" icon="search">检索</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"

      >

        <span slot="action" slot-scope="text, record">
          <a @click="seleted(record)">选择</a>
        </span>

      </a-table>
    </div>
  </a-modal>
</template>

<script>
    import { getAction } from '@/api/manage'
    export default {
        name: "ZdbdcListModal",
        data (){
          return {
            visible:false,
            /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
            queryParam: {},
            /* 数据源 */
            dataSource:[],
            loading : false,
            // 表头
            columns: [
              {
                title: '序号',
                key:'rowIndex',
                align:"center",
                customRender:function (t,r,index) {
                  return parseInt(index)+1;
                }
              },
              {
                title: '不动产单元号',
                align:"center",
                dataIndex: 'BDCDYH'
              },
              {
                title: '操作',
                dataIndex: 'action',
                align:"center",
                scopedSlots: { customRender: 'action' },
              }
            ],
            url: {
              list: "/modules/yspt/searchBdcdyh"
            }
          }
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
            var params = this.queryParam;
            this.loading = true;
            getAction(this.url.list, params).then((res) => {
              if (res.success) {
                this.dataSource = res.result.data;
              }
              this.loading = false;
            })
          },
          seleted(record) {
            var thisBdcdyh = record.BDCDYH;
            this.close();
            this.$emit('selectedDyh',thisBdcdyh);
          },
          close() {
            this.$emit('close')
            this.visible = false
          }
        }

    }
</script>

<style scoped>

</style>