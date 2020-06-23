<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :style="modal.style"
    :confirmLoading="false"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="确定"
    cancelText="关闭">

    <a-card type="inner" title="权利人信息">

      <a-table
        :columns="qlrcolums"
        :dataSource="qlrlist"
        :pagination="false"
      >
      </a-table>
    </a-card>


    <a-card type="inner" title="单元信息" style="margin-top: 20px" >
      <detail-list :title="'坐落：'+house.zl" >
        <detail-list-item style="width:100%" term="不动产单元号">{{house.bdcdyh}}</detail-list-item>
        <detail-list-item term="权利性质">{{house.qlxz_dictText}}</detail-list-item>
        <detail-list-item term="单元用途">{{house.fwyt_dictText}}</detail-list-item>
        <detail-list-item term="单元类型">{{house.bdcdylx_dictText}}</detail-list-item>
        <detail-list-item term="房屋结构">{{house.fwjg_dictText}}</detail-list-item>
        <detail-list-item term="竣工时间">{{house.jgsj}}</detail-list-item>
        <detail-list-item term="预测建筑面积">{{house.ycjzmj}}</detail-list-item>
        <detail-list-item term="预测套内面积">{{house.yctnjzmj}}</detail-list-item>
        <detail-list-item term="实测面积">{{house.scjzmj}}</detail-list-item>
        <detail-list-item term="实测套内面积">{{house.sctnjzmj}}</detail-list-item>
        <detail-list-item term="宗地面积">{{house.zdmj}}</detail-list-item>
        <detail-list-item term="面积单位">{{house.mjdw_dictText}}</detail-list-item>
      </detail-list>
      <a-divider style="margin: 16px 0"/>
      <detail-list :title="'土地用途'+(house.tdytlist&&house.tdytlist.length>0?'':' : 无')"  :col="1">
        <template v-if="house.tdytlist&&house.tdytlist.length>0">
          <a-table
            :columns="ytcolums"
            :dataSource="house.tdytlist"
            :pagination="false"
          >
          </a-table>
        </template>

      </detail-list>
      <a-divider style="margin: 16px 0"/>
      <detail-list :title="'查封状态'+(house.cflist&&house.cflist.length>0?'':' : 无查封')"  :col="1">
        <template v-if="house.cflist&&house.cflist.length>0">
          <a-table
            :columns="cfcolums"
            :dataSource="house.cflist"
            :pagination="false"
          >
          </a-table>
        </template>

      </detail-list>
      <a-divider style="margin: 16px 0"/>
      <detail-list :title="'抵押状态'+(house.dylist&&house.dylist.length>0?'':' : 无抵押')"  :col="1">
        <template v-if="house.dylist&&house.dylist.length>0">
          <a-table
            :columns="dycolums"
            :dataSource="house.dylist"
            :pagination="false"
          >
          </a-table>
        </template>

      </detail-list>
      <a-divider style="margin: 16px 0"/>
      <detail-list :title="'异议状态'+(house.yylist&&house.yylist.length>0?'':' : 无异议')"  :col="1">
        <template v-if="house.yylist&&house.yylist.length>0">
          <div v-for="(yy, yyindex) in house.yylist">
            <detail-list-item term="异议登记原因">{{yy.yydjyy}}</detail-list-item>
            <detail-list-item term="异议登记时间">{{yy.yydjsj}}</detail-list-item>
          </div>
        </template>
      </detail-list>
    </a-card>


  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import { mixinDevice } from '@/utils/mixin.js'
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import {initDictOptions, filterDictText ,ajaxFilterDictText} from '@/components/dict/JDictSelectUtil'

  const DetailListItem = DetailList.Item
  export default {
    name: "HousemessageModal",
    components : {
      PageLayout,
      DetailList,
      DetailListItem
    },
    mixins: [mixinDevice],
    data () {
      return {
        title:"操作",
        visible: false,
        house:{},
        modal: {
          title: '业务申报',
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        form: this.$form.createForm(this),
        qlrlist : [],
        qlrcolums : [
          {
            title: '权利人名称',
            dataIndex: 'qlrmc',
            key: 'qlrmc'
          },
          {
            title: '证件种类',
            dataIndex: 'zjlx_dictText',
            key: 'zjlx_dictText'
          },
          {
            title: '证件号',
            dataIndex: 'zjh',
            key: 'zjh'
          },
          {
            title: '联系电话',
            dataIndex: 'lxdh',
            key: 'lxdh'
          },
          {
            title: '通讯地址',
            dataIndex: 'txdz',
            key: 'txdz'
          }
        ],
        ytcolums : [
          {
            title: '用途',
            dataIndex: 'tdytmc',
            key: 'tdytmc'
          },
          {
            title: '起始日期',
            dataIndex: 'qsrq',
            key: 'qsrq'
          },
          {
            title: '终止日期',
            dataIndex: 'zzrq',
            key: 'zzrq'
          }
        ],
        cfcolums : [
          {
            title: '查封机构',
            dataIndex: 'cfjg',
            key: 'cfjg'
          },
          {
            title: '查封类型',
            dataIndex: 'cflx_dictText',
            key: 'cflx'
          },
          {
            title: '查封文号',
            dataIndex: 'cfwh',
            key: 'cfwh'
          },
          {
            title: '查封起始时间',
            dataIndex: 'cfqssj',
            key: 'cfqssj'
          },
          {
            title: '查封结束时间',
            dataIndex: 'cfjssj',
            key: 'cfjssj'
          }
        ],
        dycolums : [
          {
            title: '抵押权人',
            dataIndex: 'dyqr',
            key: 'dyqr'
          },
          {
            title: '抵押人',
            dataIndex: 'dyr',
            key: 'dyr'
          },
          {
            title: '抵押方式',
            dataIndex: 'dyfs_dictText',
            key: 'dyfs'
          },
          {
            title: '抵押金额',
            dataIndex: 'zqse',
            key: 'zqse'
          },
          {
            title: '抵押起始时间',
            dataIndex: 'dyqlqssj',
            key: 'dyqlqssj'
          },
          {
            title: '抵押结束时间',
            dataIndex: 'dyqljssj',
            key: 'dyqljssj'
          }
        ]
      }
    },
    created () {
    },
    methods: {
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.close()
      },
      handleCancel () {
        this.close()
      },
      initData(house){
        if(house) {
          var housedata = JSON.parse(house.houseclob);
          this.house = housedata.house;
          var jgsj = this.house.jgsj;
          if(jgsj) {
            this.house.jgsj = this.formatTime(jgsj,"Y-M-D");
          }
          this.qlrlist = housedata.qlrlist;
        }
      },
      formatNumber (n) {
        n = n.toString()
        return n[1] ? n : '0' + n;
      },
      // 参数number为毫秒时间戳，format为需要转换成的日期格式
      formatTime (number, format) {
        let time = new Date(number)
        let newArr = []
        let formatArr = ['Y', 'M', 'D', 'h', 'm', 's']
        newArr.push(time.getFullYear())
        newArr.push(this.formatNumber(time.getMonth() + 1))
        newArr.push(this.formatNumber(time.getDate()))

        newArr.push(this.formatNumber(time.getHours()))
        newArr.push(this.formatNumber(time.getMinutes()))
        newArr.push(this.formatNumber(time.getSeconds()))

        for (let i in newArr) {
          format = format.replace(formatArr[i], newArr[i])
        }
        return format;
      }
    }
  }
</script>

<style lang="scss" scoped>

  .detail-layout {
    margin-left: 44px;
  }
  .text {
    color: rgba(0, 0, 0, .45);
  }

  .heading {
    color: rgba(0, 0, 0, .85);
    font-size: 20px;
  }

  .no-data {
    color: rgba(0, 0, 0, .25);
    text-align: center;
    line-height: 64px;
    font-size: 16px;

    i {
      font-size: 24px;
      margin-right: 16px;
      position: relative;
      top: 3px;
    }
  }

  .mobile {
    .detail-layout {
      margin-left: unset;
    }
    .text {

    }
    .status-list {
      text-align: left;
    }
  }
</style>