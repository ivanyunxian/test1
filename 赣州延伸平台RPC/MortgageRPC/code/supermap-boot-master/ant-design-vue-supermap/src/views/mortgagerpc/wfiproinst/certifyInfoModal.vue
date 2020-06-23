<template>
  <a-modal
    :width="1200"
    :visible="visible"
    title="不动产登记证明"
    :style="modal.style"
    :confirmLoading="false"
    @ok="handleOk"
    @cancel="handleCancel"
    :footer="null"
    okText="确定"
    cancelText="关闭"
  >
    <div style="width: 100%;height: 600px;margin-left: 20px">
      <div id="mylayoutinner"  data-options="region:'center'" title="不动产登记证明" class="fm_div">
        <div class='print A4'>
          <div class=" fm_font center" style="width: 49%; float: left;">
            <img id="qrcode" :src="getqrcode" alt="二维码" />
            <div class="fm_QRCODE center">不动产登记证明</div>
            <div class="fm_declare">根据《中华人民共和国物权法》等法律法规，为保护申请人合法权益，对申请人申请登记的本证明所列不动产权利或登记事项，经审查核实，准予登记，颁发此证明。</div>
            <div class="fm_register_div">
              <div class="fm_register">登记机构（章）</div>
              <div class="fm_year_div">
                <span id="fm_year" contenteditable="true">{{data.nd}}</span> 年 <span
                id="fm_month" contenteditable="true">{{data.month}}</span> 月 <span
                contenteditable="true" id="fm_day">{{data.day}}</span> 日
              </div>
            </div>
            <div class="fm_bottom_div">
              <div class="fm_bottom_declare">中华人民共和国国土资源部监制</div>
              <div class="fm_bottom_num">编号<span
                style="margin-left: 10px; font-family: 宋体; font-size: 22px">
								NO.</span>
                <span>
									<input id="id_zsbh" :value="data.zsbh" disabled
                         style="font-size: 22px; outline: none; width: 145px; height: 25px; line-height: 15px; padding-bottom: 2px; border-top: none; border-left: none; border-right: none; border-bottom: 1px solid black">
									<!--<a href="javascript:void(0)" id="zsbh_search"
                     class="easyui-linkbutton btn btn-primary" iconCls="icon-search"
                     plain="true" style="display: none"> </a>
									 <input type="hidden"id="qzlx_page" value='1'>-->
								 </span>
              </div>

            </div>
          </div>
          <div style="width: 50%; height: 100%; float: left;">
            <div class="print_zs_div center">
              <div class="print_header header_font">
							<span name="qhjc" class="print_state" id='qhjc'>{{data.sjc}}</span>(<span name="nd" id='nd' class="print_year">{{data.nd}}</span>)<span name="qhmc" id='qhmc'
                                                                             class="print_city">{{data.qhmc}}</span>不动产证明第<span
                name="cqzh" class="print_num" id='cqzh'>{{data.sxh}}</span>
                号
              </div>
              <table class="print_zs " border style="width: 100%;">
                <tr>
                  <td class="print_title">证明权利或事项</td>
                  <td class="print_content"><span contenteditable="true"
                                                  name="zmqlhsx" id="zmqlhsx">{{data.zmqlhsx}}</span></td>
                </tr>
                <tr>
                  <td class="print_title">权利人(申请人)</td>
                  <td class="print_content"><span contenteditable="true"
                                                  name="qlr" id="qlr">{{data.qlr}}</span></td>
                </tr>
                <tr>
                  <td class="print_title">义务人</td>
                  <td class="print_content"><span contenteditable="true"
                                                  name="ywr" id='ywr'>{{data.ywr}}</span></td>
                </tr>
                <tr>
                  <td class="print_title">坐落</td>
                  <td class="print_content"><span contenteditable="true"
                                                  name="zl" id='zl'>{{data.zl}}</span></td>
                </tr>
                <tr>
                  <td class="print_title">不动产单元号</td>
                  <td class="print_content"><span contenteditable="true"
                                                  name="bdcdyh" id='bdcdyh'>{{data.bdcdyh}}</span></td>
                </tr>
                <tr>
                  <td class="print_title" style="height: 100px">其 他</td>
                  <td class="print_content"><span contenteditable="true"
                                                  name="qt" id='qt' v-html="data.qt"></span></td>
                </tr>
                <tr>
                  <td class="print_title" style="height: 100px">附 记</td>
                  <td class="print_content"><span contenteditable="true" data-options="multiline:true" style="white-space: pre-wrap;"
                                                  name="fj" id='fj' v-html="data.fj"></span></td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>

    </div>
  </a-modal>

</template>

<script type="text/ecmascript-6">
  import { getAction } from '@/api/manage'


  export default {
    name: 'certifyInfoModal',
    data() {
      return {
        visible: false,
        data: {
          zsbh:"",
          qhjc:"",
          nd:"",
          qhmc:"",
          cqzh:"",
          zmqlhsx:"",
          qlr:"",
          ywr:"",
          zl:"",
          bdcdyh:"",
          qt:"",
          fj:"",
          day:"",
          month:""
        },
        modal: {
          title: '不动产登记证明',
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
        form: this.$form.createForm(this),
        getqrcode:''

      }
    },
    created() {

    },
    methods: {
      showdata(record) {
        this.data = record;
        var content = record.zl+"%26"+record.nd+"-"+record.qhmc+"-"+record.cqzh+"%26"+record.bdcdyh+"%26"+record.zsbh;
        this.getqrcode =  '/mrpc/mongofile/getqrcode?content='+content;

      },
      close() {
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
