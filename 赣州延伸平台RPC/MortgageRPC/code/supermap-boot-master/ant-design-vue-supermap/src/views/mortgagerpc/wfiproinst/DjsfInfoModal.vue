<template>
  <a-modal
    :width="1200"
    :visible="visible"
    title="打印收费单"
    :style="modal.style"
    :confirmLoading="false"
    @ok="handleOk"
    @cancel="handleCancel"
    :footer="null"
    okText="确定"
    cancelText="关闭"
  >
    <div style="text-align: right">
      <a-button style="width: 100px;margin-left: 20px" type="primary" v-print="'#print'">打印</a-button>
    </div>
    <div id="print">
    <div  style="position: relative">
      <h2 style="width: 100%;text-align: center;font-weight: bold">不动产登记收费明细单</h2>
      <table class="sfxx" border="1" cellspacing="1" cellpadding="1"
             style="width: 97%;border:2px solid #000;margin-top: 20px">
        <tr>
          <td colspan="2" style="width: 30%">受理编号</td>
          <td colspan="4">{{prolsh}}</td>
        </tr>
        <tr>
          <td colspan="2">登记类型</td>
          <td colspan="4">{{qllx}}</td>
        </tr>
        <tr>
          <td colspan="2">申请人</td>
          <td colspan="4">权利人：{{qlrmc}} 义务人：{{ywrmc}}</td>
        </tr>
        <tr>
          <td colspan="2">不动产坐落</td>
          <td colspan="4">{{zl}}</td>
        </tr>
        <tr>
          <td>序号</td>
          <td>权利人</td>
          <td>收费项目</td>
          <td>计算公式</td>
          <td>金额</td>
          <td>备注</td>
        </tr>
        <tr :key="index" v-for="(item, index) in sfxxTableList">
          <td>{{++index}}</td>
          <td>{{item.qlrmc}}</td>
          <td>{{item.sfxlmc}}</td>
          <td>{{item.jsgs}}</td>
          <td>{{item.sfjs}}</td>
          <td></td>
        </tr>
        <tr>
          <td colspan="4">合计（元）</td>
          <td colspan="2">{{price}}</td>
        </tr>
        <tr>
          <td>开单人</td>
          <td colspan="2">{{user.username}}</td>
          <td>开单日期</td>
          <td colspan="2">{{nowtime}}</td>
        </tr>
      </table>
      <a-divider style="position: absolute;right: 4%;top:100px" dashed="dashed" type="vertical">第一联
      </a-divider>
    </div>
    <a-divider dashed="dashed">裁剪</a-divider>
    <div style="position: relative">
      <h2 style="width: 100%;text-align: center;font-weight: bold">不动产登记收费明细单</h2>
      <table class="sfxx" border="1" cellspacing="1" cellpadding="1"
             style="width: 97%;border:2px solid #000;margin-top: 20px">
        <tr>
          <td colspan="2" style="width: 30%">受理编号</td>
          <td colspan="4">{{prolsh}}</td>
        </tr>
        <tr>
          <td colspan="2">登记类型</td>
          <td colspan="4">{{qllx}}</td>
        </tr>
        <tr>
          <td colspan="2">申请人</td>
          <td colspan="4">权利人：{{qlrmc}} 义务人：{{ywrmc}}</td>
        </tr>
        <tr>
          <td colspan="2">不动产坐落</td>
          <td colspan="4">{{zl}}</td>
        </tr>
        <tr>
          <td>序号</td>
          <td>权利人</td>
          <td>收费项目</td>
          <td>计算公式</td>
          <td>金额</td>
          <td>备注</td>
        </tr>
        <tr :key="index" v-for="(item, index) in sfxxTableList">
          <td>{{++index}}</td>
          <td>{{item.qlrmc}}</td>
          <td>{{item.sfxlmc}}</td>
          <td>{{item.jsgs}}</td>
          <td>{{item.sfjs}}</td>
          <td></td>
        </tr>
        <tr>
          <td colspan="4">合计（元）</td>
          <td colspan="2">{{price}}</td>
        </tr>
        <tr>
          <td>开单人</td>
          <td colspan="2">{{user.username}}</td>
          <td>开单日期</td>
          <td colspan="2">{{nowtime}}</td>
        </tr>
      </table>
      <a-divider style="position: absolute;right: 4%;top:100px" dashed="dashed" type="vertical">第二联
      </a-divider>
    </div>
    </div>
  </a-modal>

</template>

<script type="text/ecmascript-6">
  import { getAction } from '@/api/manage'
  import { axios } from '@/utils/request'
  import {initDictOptions, filterDictText ,ajaxFilterDictText} from '@/components/dict/JDictSelectUtil'
  import moment from "moment"

  export default {
    name: 'DjsfInfoModal',
    data() {
      return {
        visible: false,
        modal: {
          title: '打印收费单',
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
        url:{
          projectMessage:"/modules/wfi_proinst/getProjectByProlsh"
        },
        form: this.$form.createForm(this),
        sfxxTableList:[],
        price:0,
        prolsh:'',
        qllx:'',
        qlrmc:'',
        ywrmc:'',
        zl:'',
        user:{username:""},
        qllxDictOptions:{},
        nowtime:''

      }
    },
    created() {
      initDictOptions('QLLX').then((res) => {
        if (res.success) {
          this.qllxDictOptions = res.result;
        }
      });
    },
    methods: {
      showdata(prolsh) {
        this.nowtime = moment(new Date()).format('YYYY-MM-DD');
        axios.get(`${window._CONFIG['domianURL']}/sfxx?ywlsh=`+prolsh)
          .then((res) => {
            let price = 0
            this.sfxxTableList;
            for (let i = 0; i < res.result.length; i++) {
              if (res.result[i].checked === true) {
                price += res.result[i].sfjs
                this.sfxxTableList.push(res.result[i])
              }
            }
            this.price = price;
          })

        getAction(this.url.projectMessage, {prolsh:prolsh}).then((res) => {
          if (res.success) {
            var data = res.result;
            var proinst = data.proinst;
            var dylist = data.dylist;
            var qlrlist = data.qlrlist;
            var ywrlist = data.ywrlist;
            this.user = data.sysUser;
            this.prolsh = proinst.lsh;
            this.qllx = filterDictText(this.qllxDictOptions,proinst.qllx)
            for(var key in dylist) {
              if(key<1) {
                this.zl += dylist[key].zl + ",";
              } else {
                this.zl=this.zl.substring(0,this.zl.length-1)
                this.zl += "等" + dylist.length + "处";
                break
              }
            }
            if(this.zl.endsWith(",")) {
              this.zl=this.zl.substring(0,this.zl.length-1)
            }

            for(var key in qlrlist) {
              if(key<3) {
                this.qlrmc += qlrlist[key].sqrxm + ",";
              } else {
                this.qlrmc=this.qlrmc.substring(0,this.qlrmc.length-1)
                this.qlrmc += "等" + qlrlist.length + "人";
                break
              }
            }
            if(this.qlrmc.endsWith(",")) {
              this.qlrmc=this.qlrmc.substring(0,this.qlrmc.length-1)
            }

            for(var key in ywrlist) {
              if(key<3) {
                this.ywrmc += ywrlist[key].sqrxm + ",";
              } else {
                this.ywrmc=this.ywrmc.substring(0,this.ywrmc.length-1)
                this.ywrmc += "等" + ywrlist.length + "人";
                break
              }
            }
            if(this.ywrmc.endsWith(",")) {
              this.ywrmc=this.ywrmc.substring(0,this.ywrmc.length-1)
            }


          } else {
            this.$message.error("获取收费单信息失败")
          }
        })



      },
      close() {
        this.sfxxTableList=[];
        this.price=0;
        this.prolsh='';
        this.qllx='';
        this.qlrmc='';
        this.ywrmc='';
        this.zl='';
        this.user={username:""};
        this.qllxDictOptions={};
        this.nowtime='';
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        this.close()
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>

<style scoped>
  @import './css/certifyInfo.css';

  .print_zs {
    border-spacing: 0;
    border-collapse: collapse;
  }
</style>
