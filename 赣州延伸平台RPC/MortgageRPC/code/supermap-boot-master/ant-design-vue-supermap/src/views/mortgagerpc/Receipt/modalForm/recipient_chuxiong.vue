<template>
  <a-modal
    :width="1000"
    :visible="visible"
    title="打印回执单"
    :style="modal.style"
    :confirmLoading="false"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="确定"
    cancelText="关闭"
  >
    <div style="text-align: right">
      <a-button type="primary" v-print="'#print'">打印</a-button>
    </div>
    <div id="print" style="width: 100%;height: 1000px;overflow-y:scroll;margin-left: 20px;position: relative">
      <div class="djhz">
        <div class="title">
          <div class="txm">
            <svg id="barcode"></svg>
          </div>
          <div class="title-info">赣州市不动产登记回执</div>
          <div class="slbh">登记受理编号: {{this.proinst.lsh}}</div>
        </div>
        <div class="sl-info">
          <div class="sjrq">收件日期: {{this.proinst.proinstStart}}</div>
          <div class="user">经办人: {{this.user.realname}}</div>
          <div class="bzqx">办证期限: 1个工作日</div>
        </div>
        <table border="1px solid #ccc" cellspacing="0" style="width: 100%;" cellpadding="0">
          <tr>
            <td colspan="2">申请人</td>
            <td colspan="3">
              <span v-for="(item,index) in sqrs">{{item.sqrxm}}&nbsp;</span>
            </td>
          </tr>
          <tr>
            <td colspan="2">不动产坐落</td>
            <td colspan="3">{{this.bdczl}}</td>
          </tr>
          <tr>
            <td colspan="1" style="text-align: center">材<br>料<br>名<br>称</td>
            <td colspan="4" style="text-align: left">
              <div v-for="(item,index) in materclass"> {{item.name}} 共{{item.count}}份<br></div>
            </td>
            <!-- <td colspan="2" style="text-align: center">
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
               不动产登记申请表（原件） 共1份<br>
             </td>-->
          </tr>
          <tr>
            <td>序号</td>
            <td>收费项目</td>
            <td>计算公式</td>
            <td>金额（元）</td>
            <td>备注</td>
          </tr>
          <tr>
            <td colspan="2">合计（元）</td>
            <td colspan="3">0</td>
          </tr>
          <tr>
            <td colspan="2" style="position:relative;text-align: center">
              <div>面积</div>
              <div style="margin-top: 30px;">不动产中心（盖章）</div>
            </td>
            <td colspan="3" style="position:relative;text-align: center">
              <div>产权收费小计</div>
              <div style="margin-top: 30px;">不动产登记中心（盖章）</div>
            </td>
          </tr>
        </table>
        <div>
          <p style="text-indent:34px">提示：请您于1个工作日后凭受理单、经办人身份证，到颁证窗口缴纳相关费用并领证。（代申请人领证的，需要提供代领人身份证复印件及委托书。</p>
          <p style="text-indent:60px">1.中华人民共和国税收完税证明（第四联）（契税）;</p>
          <p style="text-indent:60px">2.房地产出售（转让）已税通知单（第二联）;</p>
          <p style="text-indent:60px">3.销售不动产统一发票（代开）办证联（第二联）;</p>
          <p style="text-indent:60px">4.中华人民共和国税收完税证明（第四联）（营业税、个人所得税等);</p>
        </div>
        <div class="ft" style="position: absolute;bottom: 0;width: 90%">
          <hr style="border:1px dashed #000;margin: 20px 0"/>
          <div class="footer">
            <div class="txm">
              <svg id="footer-txm"></svg>
            </div>
            <div class="footer-info">收件回执存根</div>
          </div>
          <div class="footer">
            <div class="footer-left">
              <div><span class="footer-title">申请人:</span><span v-for="(item,index) in sqrs">{{item.sqrxm}}&nbsp;</span>
              </div>
              <div><span class="footer-title">登记类型:</span>
                <span v-if="this.proinst.djlx=='100'">首次登记</span>
                <span v-if="this.proinst.djlx=='200'">转移登记</span>
                <span v-if="this.proinst.djlx=='300'">变更登记</span>
                <span v-if="this.proinst.djlx=='400'">注销登记</span>
                <span v-if="this.proinst.djlx=='500'">更正登记</span>
                <span v-if="this.proinst.djlx=='600'">异议登记</span>
                <span v-if="this.proinst.djlx=='700'">预告登记</span>
                <span v-if="this.proinst.djlx=='800'">查封登记</span>
              </div>
              <div><span class="footer-title">业务类型:</span><span>{{this.proinst.prodefName}}</span></div>
              <div><span class="footer-title">坐落:</span><span>{{this.bdczl}}</span></div>
            </div>
            <div class="footer-right">
              <div><span class="footer-title">受理编号:</span><span>{{this.proinst.lsh}}</span></div>
              <div><span class="footer-title">经办人:</span><span>{{this.user.realname}}</span></div>
              <div><span class="footer-title">收件日期:</span>{{this.proinst.proinstStart}}</div>
            </div>
          </div>
          <table style="width: 100%;margin-top: 10px" border="1px solid #000000" cellspacing="0">
            <tr>
              <td colspan="2">合计（元）</td>
              <td colspan="1">0</td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script type="text/ecmascript-6">
  import { getAction } from '@/api/manage'
  import JsBarcode from 'jsbarcode'

  export default {
    name: 'recipientModal',
    data() {
      return {
        visible: false,
        data: {},
        proinst: {},
        prodef: {},
        bdczl: '',
        user: {},
        prolsh:'',
        sqrs: [],
        materclass: [],
        modal: {
          title: '打印回执单',
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
        url:{
          recipientlist:"/modules/wfi_proinst/getRecipientData"
        },
        form: this.$form.createForm(this)
      }
    },
    props: [ 'recipientData'],
    created() {

    },
    methods: {
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        this.close()
      },
      handleCancel() {
        this.close()
      },
      printRecipient(prolsh) {
        getAction(this.url.recipientlist,{prolsh:prolsh}).then((res)=> {
          if (res.success) {
            this.data = res.result;
            this.user = this.data.user
            this.proinst = this.data.proinst
            this.prolsh = this.proinst.lsh
            this.prodef = this.data.prodef
            this.bdczl = this.data.bdczl
            this.sqrs = this.data.sqrs
            this.materclass = this.data.materclass

            JsBarcode('#barcode', this.prolsh, {
              lineColor: '#000000',
              width: 1.3,
              height: 40
            })
            JsBarcode('#footer-txm', this.prolsh, {
              lineColor: '#000000',
              width: 1.3,
              height: 40
            })

          } else {
            that.$message.error(res.message);
          }
        });
      }
    }
  }
</script>

<style scoped>
  .djhz {
    margin: auto 0;
    width: 842px;
    height: 595px;
  }

  .title, .sl-info {
    display: flex;
  }

  .title .txm {
    flex: 1;
  }

  .title .title-info {
    flex: 1;
    padding-top: 20px;
    font-size: 20px;
    font-weight: bold;
    text-align: center
  }

  .title .slbh {
    flex: 1;
    padding-top: 4px;
  }

  .sl-info .sjrq {
    flex: 1;
  }

  .sl-info .user {
    flex: 1;
  }

  .sl-info .bzqx {
    flex: 1;
  }

  table td {
    padding: 2px;
  }

  .footer .footer-title {
    font-weight: bold;
  }

  .footer {
    display: flex;

  }

  .footer .txm {
    flex: 1;
    bottom: 0;
  }

  .footer .footer-info {
    flex: 2;
    margin-top: 20px;
    font-size: 20px;
    font-weight: bold;
    /*text-align: center*/
  }

  .footer .footer-left {
    flex: 2;
  }

  .footer .footer-right {
    flex: 1;
  }


</style>
