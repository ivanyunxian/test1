<template>
  <a-card :loading="loading">
    <a-spin :spinning="confirmLoading">
      <a-row :gutter="10">
        <a-col :md="7" :sm="24">
          <a-card :bordered="false">
            <div style="background: #fff;height: 100%; margin-top: 5px">
              <!--<span style="font-size:18px">单元列表</span>-->
              <!-- 树-->
              <a-form  :form="form" class="form">
                    <a-form-item label="区域" :labelCol="labelCol"
                                              :wrapperCol="wrapperCol" >
                     <a-select :defaultValue='360702'  v-decorator="['divisioncode',{rules: [{ required: true, message: '请选择区域'}]}]"
                      placeholder="请选择区域" style="margin-bottom:10px" @change="handleChangeSelect" >
                                <a-select-option value="360702">市本级</a-select-option>
                                <a-select-option value="3607029">蓉江新区</a-select-option>
                                <a-select-option value="3607024">开发区</a-select-option>
                      </a-select>
                      </a-form-item>
               </a-form>

              <template>
                <a-card title="单元列表" :bordered="true" style="height: 700px">

                  <a-list size="large"
                          ref="list"
                          :loading="loading"
                          :pagination="false"
                          style="height:600px;overflow-y:auto;"
                  >
                    <a-list-item :key="index" v-for="(item, index) in housedata" @click="chooseHouse(item,index)">
                      <div class="ant-alert " style="width: 100%" :class="item.thisselected">
                        <i class="anticon anticon-info-circle ant-alert-icon"></i> <a
                        style="font-weight: 600;color:#1e1e1e">{{++index}}. {{item.zl}}</a>
                      </div>
                    </a-list-item>
                  </a-list>

                </a-card>
              </template>

            </div>
          </a-card>
        </a-col>
        <a-col :md="17" :sm="24">
          <a-card :bordered="false">
            <a-tabs defaultActiveKey="1">
              <a-tab-pane tab="变更前信息" key="0" v-if="djlx=='300'&&qllx=='23'" forceRender>
                <!--<fsqlmessage-form ref="fsqlmessage" :showSubmit="false" :disabled="disabled"/>
                <qlmessage-form ref="qlmessage" :showSubmit="false" :disabled="disabled" :djlx="djlx" :qllx="qllx"/>-->
                <bg-qlmessage-form ref="bgqlmessage" :showSubmit="false" :disabled="true" :prolsh="prolsh"/>


              </a-tab-pane>

              <a-tab-pane tab="申报信息" key="1">
                <!--<fsqlmessage-form ref="fsqlmessage" :showSubmit="false" :disabled="disabled"/>
                <qlmessage-form ref="qlmessage" :showSubmit="false" :disabled="disabled" :djlx="djlx" :qllx="qllx"/>-->
                <qlfsqlmessage-form ref="qlfsqlmessage" :showSubmit="false" :disabled="disabled" :djlx="djlx"
                                    :qllx="qllx"/>


              </a-tab-pane>
              <a-tab-pane tab="单元详情" key="2" forceRender>
                <a-card type="inner" title="单元信息" style="margin-top: 20px">
                  <detail-list>
                    <detail-list-item style="width:50%;" term="不动产单元号">{{housedetail.bdcdyh}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="房屋编码">{{housedetail.fwbm}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <detail-list-item style="width:50%;" term="坐落">{{housedetail.zl}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="房号">{{housedetail.fh}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <!--<detail-list-item style="width:50%" term="房屋不动产权证号">{{}}</detail-list-item>-->
                    <detail-list-item style="width:50%" term="实测建筑面积">{{housedetail.mj}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="分摊土地面积">{{housedetail.fttdmj}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <detail-list-item style="width:50%;" term="所在层">{{housedetail.szc}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="总层数">{{housedetail.zcs}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <detail-list-item style="width:50%;" term="房屋结构">{{housedetail.fwjg_dictText}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="规划用途">{{housedetail.fwyt_dictText}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <detail-list-item style="width:50%;" term="房屋性质">{{housedetail.fwxz_dictText}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="竣工时间">{{housedetail.jgsj}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <detail-list-item style="width:50%;" term="房屋产别">{{housedetail.fwcb_dictText}}</detail-list-item>
                    <detail-list-item style="width:50%;" term="权利性质">{{housedetail.qlxz_dictText}}</detail-list-item>
                    <div style="width:0px;clear:both;overflow:hidden"></div>
                    <detail-list-item style="width:50%;" term="房屋土地用途">{{housedetail.fwtdyt_dictText}}</detail-list-item>



                    <!--<detail-list-item style="width:100%" term="不动产单元号">{{housedetail.bdcdyh}}</detail-list-item>
                    <detail-list-item term="权利性质">{{housedetail.qlxz_dictText}}</detail-list-item>
                    <detail-list-item term="单元用途">{{housedetail.fwyt_dictText}}</detail-list-item>
                    <detail-list-item term="房屋结构">{{housedetail.fwjg_dictText}}</detail-list-item>
                    <detail-list-item term="竣工时间">{{housedetail.jgsj}}</detail-list-item>
                    <detail-list-item term="预测建筑面积">{{housedetail.ycjzmj}}</detail-list-item>
                    <detail-list-item term="预测套内面积">{{housedetail.yctnjzmj}}</detail-list-item>
                    <detail-list-item term="实测面积">{{housedetail.mj}}</detail-list-item>
                    <detail-list-item term="实测套内面积">{{housedetail.tnjzmj}}</detail-list-item>
                    <detail-list-item term="宗地面积">{{housedetail.dytdmj}}</detail-list-item>
                    <detail-list-item term="面积单位">{{housedetail.mjdw_dictText}}</detail-list-item>-->
                  </detail-list>
                  <!--<a-divider style="margin: 16px 0"/>-->
                  <!--<detail-list-->
                    <!--:title="'土地用途'+(housedetail.house&&housedetail.house.tdytlist&&housedetail.house.tdytlist.length>0?'':' : 无')"-->
                    <!--:col="1">-->
                    <!--<template v-if="housedetail.house&&housedetail.house.tdytlist&&housedetail.house.tdytlist.length>0">-->
                      <!--<a-table-->
                        <!--:columns="ytcolums"-->
                        <!--:dataSource="housedetail.house.tdytlist"-->
                        <!--:pagination="false"-->
                      <!--&gt;-->
                      <!--</a-table>-->
                    <!--</template>-->

                  <!--</detail-list>-->
                  <!--<a-divider style="margin: 16px 0"/>-->
                  <!--<detail-list-->
                    <!--:title="'查封状态'+(housedetail.house&&housedetail.house.cflistt&&housedetail.house.cflist.length>0?'':' : 无查封')" :col="1">-->
                    <!--<template v-if="housedetail.house&&housedetail.house.cflist&&housedetail.house.cflist.length>0">-->
                      <!--<a-table-->
                        <!--:columns="cfcolums"-->
                        <!--:dataSource="housedetail.house.cflist"-->
                        <!--:pagination="false"-->
                      <!--&gt;-->
                      <!--</a-table>-->
                    <!--</template>-->

                  <!--</detail-list>-->
                  <!--<a-divider style="margin: 16px 0"/>-->
                  <!--<detail-list :title="'抵押状态'+(housedetail.house&&housedetail.house.dylist&&housedetail.house.dylist.length>0?'':' : 无抵押')"-->
                               <!--:col="1">-->
                    <!--<template v-if="housedetail.house&&housedetail.house.dylist&&housedetail.house.dylist.length>0">-->
                      <!--<a-table-->
                        <!--:columns="dycolums"-->
                        <!--:dataSource="housedetail.house.dylist"-->
                        <!--:pagination="false"-->
                      <!--&gt;-->
                      <!--</a-table>-->
                    <!--</template>-->

                  <!--</detail-list>-->
                  <!--<a-divider style="margin: 16px 0"/>-->
                  <!--<detail-list :title="'异议状态'+(housedetail.house&&housedetail.house.yylist&&housedetail.house.yylist.length>0?'':' : 无异议')"-->
                               <!--:col="1">-->
                    <!--<template v-if="housedetail.house&&housedetail.house.yylist&&housedetail.house.yylist.length>0">-->
                      <!--<div v-for="(yy, yyindex) in housedetail.house.yylist">-->
                        <!--<detail-list-item term="异议登记原因">{{yy.yydjyy}}</detail-list-item>-->
                        <!--<detail-list-item term="异议登记时间">{{yy.yydjsj}}</detail-list-item>-->
                      <!--</div>-->
                    <!--</template>-->
                  <!--</detail-list>-->
                </a-card>
              </a-tab-pane>
              <a-tab-pane tab="申请人信息" key="3" forceRender>
                <ywrmessage-form ref="ywrmessage" :prolsh="prolsh" :disabled="disabled" :dyid="dyid"/>

                <qlrmessage-form ref="qlrmessage" :prolsh="prolsh" :disabled="disabled" :dyid="dyid"/>
              </a-tab-pane>
              <a-tab-pane tab="收费信息" key="4" forceRender>
                <div>
                  <a-button type="primary" style="width: 100px" @click="showModalPrice" v-show="!disabled">添加收费</a-button>
                  <!--<a-button style="width: 100px;margin-left: 20px">打印</a-button>-->
                </div>
                <!--<div style="position: relative">-->
                <!--<h2 style="width: 100%;text-align: center;font-weight: bold">不动产登记收费明细单</h2>-->
                <!--<table class="sfxx" border="1" cellspacing="1" cellpadding="1"-->
                <!--style="width: 97%;border:2px solid #000;margin-top: 20px">-->
                <!--<tr>-->
                <!--<td colspan="2">受理编号</td>-->
                <!--<td colspan="4">201904323</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="2">登记类型</td>-->
                <!--<td colspan="4">一般抵押权</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="2">申请人</td>-->
                <!--<td colspan="4">权利人：中国人建设银行 义务人：131231</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="2">不动产坐落</td>-->
                <!--<td colspan="4">楚雄是的撒会计恒等式按客户的考生啊哈即可获得是刷卡</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td>序号</td>-->
                <!--<td>权利人</td>-->
                <!--<td>收费项目</td>-->
                <!--<td>计算公式</td>-->
                <!--<td>金额</td>-->
                <!--<td>备注</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td>1</td>-->
                <!--<td>中国农业银行股份有限公司</td>-->
                <!--<td>登记费</td>-->
                <!--<td>SFJS*TS</td>-->
                <!--<td>80</td>-->
                <!--<td></td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="4">合计（元）</td>-->
                <!--<td colspan="2">160</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td>开单人</td>-->
                <!--<td colspan="2"></td>-->
                <!--<td>开单日期</td>-->
                <!--<td colspan="2"></td>-->
                <!--</tr>-->
                <!--</table>-->
                <!--<a-divider style="position: absolute;right: 3%;top:100px" dashed="dashed" type="vertical">第一联-->
                <!--</a-divider>-->
                <!--</div>-->
                <!--<a-divider dashed="dashed">裁剪</a-divider>-->
                <!--<div style="position: relative">-->
                <!--<h2 style="width: 100%;text-align: center;font-weight: bold">不动产登记收费明细单</h2>-->
                <!--<table class="sfxx" border="1" cellspacing="1" cellpadding="1"-->
                <!--style="width: 97%;border:2px solid #000;margin-top: 20px">-->
                <!--<tr>-->
                <!--<td colspan="2">受理编号</td>-->
                <!--<td colspan="4">201904323</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="2">登记类型</td>-->
                <!--<td colspan="4">一般抵押权</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="2">申请人</td>-->
                <!--<td colspan="4">权利人：中国人建设银行 义务人：131231</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="2">不动产坐落</td>-->
                <!--<td colspan="4">楚雄是的撒会计恒等式按客户的考生啊哈即可获得是刷卡</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td>序号</td>-->
                <!--<td>权利人</td>-->
                <!--<td>收费项目</td>-->
                <!--<td>计算公式</td>-->
                <!--<td>金额</td>-->
                <!--<td>备注</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td>1</td>-->
                <!--<td>中国农业银行股份有限公司</td>-->
                <!--<td>登记费</td>-->
                <!--<td>SFJS*TS</td>-->
                <!--<td>80</td>-->
                <!--<td></td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td colspan="4">合计（元）</td>-->
                <!--<td colspan="2">160</td>-->
                <!--</tr>-->
                <!--<tr>-->
                <!--<td>开单人</td>-->
                <!--<td colspan="2"></td>-->
                <!--<td>开单日期</td>-->
                <!--<td colspan="2"></td>-->
                <!--</tr>-->
                <!--</table>-->
                <!--<a-divider style="position: absolute;right: 3%;top:100px" dashed="dashed" type="vertical">第二联-->
                <!--</a-divider>-->
                <!--</div>-->
                <a-table :columns="columns" :dataSource="sfxxTableList" bordered style="margin-top: 20px">
                  <template slot="name" slot-scope="text">
                    <a href="javascript:;">{{text}}</a>
                  </template>
                  <template slot="operation" slot-scope="text, record" >
                    <a-popconfirm v-show="!disabled"
                      title="确定删除吗?"
                      @confirm="() => onDelete(record.key)">
                      <a href="javascript:;">删除</a>
                    </a-popconfirm>
                  </template>
                  <template slot="footer" slot-scope="currentPageData">
                    <div style="text-align: right;padding-right: 100px">合计:￥{{price}}元</div>
                  </template>
                </a-table>
              </a-tab-pane>
              <a-tab-pane tab="附件材料" key="5" forceRender>
                <div>
                  <a-button v-if="false" type="primary" style="width: 100px; margin-bottom:10px" @click="showModalCollect">高拍仪采集</a-button>
                  <!--<a-row>
                     <a-col :md="{ span: 3, offset: 21 }">
                        <a-button v-if="!disabled" type="primary" style="width: 100px; margin-bottom:10px" @click="showModalCollect">高拍仪采集</a-button>
                     </a-col>
                  </a-row>-->
                </div>
                <div>
                  <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="materlistcolnums"
                    :dataSource="materclassdata"
                    :pagination="false"
                    :loading="loadingfj"
                  >

                <span slot="action" slot-scope="text, record">
                  <a @click="getEDM(record)">采集</a>
                   <a-divider type="vertical"/>
                 <a-upload
                   v-if="!disabled"
                   class="avatar-uploader"
                   :showUploadList="false"
                   :multiple="true"
                   :action="uploadAction"
                   :supportServerRender="false"
                   :data="Object.assign(record,{materdataindex: longtime})"
                   :beforeUpload="beforeUpload"
                   @change="handleChange"
                 >
                      <a>上传</a>
                  <a-divider type="vertical"/>
                 </a-upload>
                   <a @click="showfiles(record)">预览</a>
                   <a-divider type="vertical" v-if="!disabled"/>
                   <a v-if="record.ecert==1" @click="getecert(record)">电子证照</a>
                  <a-divider type="vertical"  v-if="record.ecert==1"/>
                   <a v-if="!disabled" @click="removeAll(record)">清空</a>
                </span>
                    <template slot="customRenderStatus" slot-scope="required">
                      <a-tag v-if="required==0" color="orange">否</a-tag>
                      <a-tag v-if="required==1" color="red">是</a-tag>
                    </template>
                  </a-table>
                </div>

                <imag-preview ref="imagpreview" :materclass="materclass" @loadmaterclasslist="loadmaterclasslist"
                              :disabled="disabled"/>

              </a-tab-pane>
            </a-tabs>
          </a-card>
        </a-col>
      </a-row>
      <div class="action" v-show="actionshow">
        <a-button style="margin-right: 8px" @click="prevStep">上一步</a-button>
        <a-button type="primary" @click="submitProject">提交</a-button>
        <a-button style="margin-left: 8px" @click="saveProject">暂存</a-button>

      </div>
    </a-spin>
    <a-modal
      title="收费项目"
      :visible="visible"
      :maskClosable="false"
      width="900px"
      @ok="hideModal"
      @cancel="close"
      okText="确认"
      cancelText="取消"
    >
      <a-spin :spinning="isShowModalSpin">
        <div style="height: 600px">
          <a-table :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                   :columns="sfxxcolumns"
                   :pagination="false"
                   :dataSource="sfxxdata"
                   :scroll="{ y: 500 }"
          />
        </div>
      </a-spin>
    </a-modal>

    <a-modal
      title="高拍仪采集"
      :visible="cjvisible"
      :maskClosable="false"
      width="1400px"
      height="700px"
      @ok="closeCollect"
      @cancel="closeCollect"
      okText="确认"
      cancelText="取消"
    >
      <div id="components-layout-demo-basic">
        <a-row>
          <a-col :span="10">
            <a-card title="附件目录" :bordered="true">
              <a-list class="demo-loadmore-list" :loading="loading" itemLayout="horizontal" :dataSource="materclassdata">
                <a-list-item slot="renderItem" slot-scope="item, index">
                  <a slot="actions">上传</a>
                  <a slot="actions" @click="removeAll(item)">删除</a>
                  <a-list-item-meta><a slot="title">{{item.name}}</a></a-list-item-meta>
                  <div><a-tag color="#f50">{{item.count}}</a-tag></div>
                </a-list-item>
              </a-list>
            </a-card>
          </a-col>
          <a-col :span="14">
            <a-card title="内容预览" :bordered="false">
              <a-form>
                <a-form-item
                  style="margin-bottom:10px"
                  :label-col="{ span: 3 }"
                  :wrapper-col="{ span: 10 }"
                  label="支持的分辨率">
                  <a-select default-value="1">
                    <a-select-option value="1">
                      Option 1
                    </a-select-option>
                    <a-select-option value="2">
                      Option 2
                    </a-select-option>
                    <a-select-option value="3">
                      Option 3
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-form>
              <a-button-group>
                <a-button>启动扫描</a-button>
                <a-button>扫描资料</a-button>
                <a-button>停止扫描</a-button>
              </a-button-group>
            </a-card>
          </a-col>
        </a-row>
      </div>
    </a-modal>

      <!-- 高拍仪组件 -->
      <LTCamera-modal ref="scmodel"></LTCamera-modal>

  </a-card>



</template>

<script>

  import LTCameraModal from '@/components/ViiSanCamSDK/LTCameraModal'
  import { getAction, postAction } from '@/api/manage'
  import YwrmessageForm from '../../wfiproinst/modules/form/YwrmessageForm'
  import QlrmessageForm from '../../wfiproinst/modules/form/QlrmessageForm'
  import QlfsqlmessageForm from '../../wfiproinst/modules/form/QlfsqlmessageForm'
  import DetailList from '@/components/tools/DetailList'
  import { mixinDevice } from '@/utils/mixin.js'
  import PageLayout from '@/components/page/PageLayout'
  import moment from 'moment'
  import ImagPreview from '@/views/mortgagerpc/acceptProject/modalForm/ImagPreview'
  import ajaxaciton from '@/api/ajaxAction'
  import BgQlmessageForm from '../../wfiproinst/modules/form/BgQlmessageForm'
  import { axios } from '../../../../utils/request'
  import qs from 'qs'


  const DetailListItem = DetailList.Item
  export default {
    name: 'Step2_AcceptProject',
    components: {
      YwrmessageForm,
      QlrmessageForm,
      QlfsqlmessageForm,
      DetailList,
      PageLayout,
      DetailListItem,
      ImagPreview,
      BgQlmessageForm,
      LTCameraModal
    },
    mixins: [mixinDevice],
    data() {
      return {
      form: this.$form.createForm(this),
       labelCol: {
                xs: {span: 24},
                sm: {span: 4},
              },
              wrapperCol: {
                xs: {span: 24},
                sm: {span: 20},
              },
        sfxxcolumns: [{
          title: '收费小类名称',
          dataIndex: 'sfxlmc'
        }, {
          title: '收费科目名称',
          dataIndex: 'sfkmmc'
        }],
        isShowModalSpin: false,
        price: '0.00',
        sfxxdata: [],
        sfxxTableList: [],
        columns: [{
          title: '权利人',
          dataIndex: 'qlrmc'
        }, {
          title: '收费项目',
          dataIndex: 'sfxlmc'
        }, {
          title: '计算公式',
          dataIndex: 'jsgs'
        }, {
          title: '金额',
          dataIndex: 'sfjs'
        }, {
          title: '备注',
          dataIndex: 'tjbz'
        }, {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' }
        }],
        selectedRowKeys: ['b0006aaa55544121bca782122cd4347f', 'bff2bc4617734e1ea07d35d30fced667', 'f0e8837a8adb46488145ea6580c72619', '7a744e4ddc8f478abc3a53cb6b39d40e'],
        loading: false,
        loadingfj:false,
        housedata: [],//记录上一步选择的单元
        qlrdata: [],//记录上一步选择的单元的权利人
        qldata : {},
        divisioncode:'',
        url: {
          projectMessage: '/modules/wfi_proinst/projectMessage',
          saveProject: '/modules/wfi_proinst/saveProjectForStep2',
          submitProject: '/modules/wfi_proinst/submitProject',
          getMaterlist: '/mongofile/getMaterlist',
          queryByLsh: '/modules/bdc_dy/queryByLsh',
          removeAll: '/mongofile/removeAll',
          ecerturl:'/modules/certificate/getEcert'
        },
        qlrshow: true,
        ywrshow: true,
        dyqrshow: true,
        dyid: '',
        qlid: '',
        fsqlid: '',
        materclass: {},
        confirmLoading: false,
        housedetail: {},
        materclassdata: [],
        materlistcolnums: [{
          title: '目录序号',
          align: 'center',
          dataIndex: 'id',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
          {
            title: '申请材料清单',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: 'matedesc'
          },
          {
            title: '必要材料',
            align: 'center',
            dataIndex: 'required',
            scopedSlots: { customRender: 'customRenderStatus' },
            //filterMultiple: false,
            /*filters: [
              { text: '否', value: '0' },
              { text: '是', value: '1' }
            ]*/
          },
          {
            title: '文件数',
            align: 'center',
            dataIndex: 'count',
            customRender: function(t, r, index) {
              return t ? t : 0
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }],
        ytcolums: [
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
        cfcolums: [
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
        dycolums: [
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
        ],
        fileindex: 0,
        longtime: '',
        djlx: '',
        qllx: '',
        visible: false,
        scmvisible: false,
        scmtitle: "信息採集",
        cjvisible:false
      }
    },
    created() {
      this.longtime = new Date().getTime()
      //初始化加载页面数据
      this.$nextTick(() => {
        console.log('create:' + this.prolsh)
        this.loadDyData()
      })
      this.sfxxList()
    },
    props: ['prolsh', 'actionshow', 'disabled'],
    methods: {
     handleChangeSelect(value){
            console.log(value);
            this.divisioncode = value;
          },
      getecert(record){
        var that = this;
        console.log(record);
        this.loadingfj = true
        let formData = Object.assign(record, {prolsh:this.prolsh});
        postAction(this.url.ecerturl, formData).then((res) => {
           this.loadingfj = false
          if(res.success) {
            this.loadmaterclasslist(false)
          } else {
            this.$message.error('电子证照接口异常');
          }
        })
        //that.$message.warning('接口暂未开放')
      },
      /*文件上传*/
      removeAll(record) {
        var that = this
        this.$confirm({
          title: '确认清空？',
          content: '此操作将清空该目录下所有附件，是否确定清空?',
          onOk: function() {
            getAction(that.url.removeAll, { id: record.id }).then((res) => {
              if (res.success) {
                that.$message.success('清空成功')
                that.loadmaterclasslist(true)
              } else {
                that.$message.warning(res.message)
              }
            })

          }
        })
      },
      uploadAction: function() {
        //获取地址
        return window._CONFIG['domianURL'] + '/mongofile/upload'
      },
      getFileUploadData: function() {
        /*获取文件参数*/
      },
      beforeUpload: function(file) {
        var fileType = file.type
        if (fileType.indexOf('image') < 0&&fileType.indexOf('pdf')<0) {
          this.$message.warning('请上传图片')
          return false
        }
      },
      handleChange(info) {
        this.picUrl = ''
        if (info.file.status === 'uploading') {
          this.fileindex++
          this.longtime = this.longtime + this.fileindex
          this.uploadLoading = true
          return
        }
        if (info.file.status === 'done') {
          var response = info.file.response
          this.uploadLoading = false
          if (response.success) {
            this.loadmaterclasslist(false)
          } else {
            this.$message.warning(response.message)
          }
        }
      },
      loadmaterclasslist(nomessage) {
        getAction(this.url.getMaterlist, { prolsh: this.prolsh }).then((res) => {
          if (res.success) {
            this.materclassdata = res.result
            if (!nomessage) {
              this.$message.success('上传成功')
            }
          } else {
            this.$message.warning(res.message)
          }
        })
      },
      showfiles(record) {
        this.$nextTick(() => {
          this.materclass = record
          this.$refs.imagpreview.loadFileData(record.name)
        })
      },
      nextStep() {
        let that = this
        that.$emit('nextStep')
      },
      chooseHouse(item, index) {
        this.selectedhouse(index - 1)
      },
      submitProject() {
        var that = this
        this.$confirm({
          title: '确认提交',
          content: '提交前请确保填写的信息正确并已保存，是否确认提交?',
          onOk: function() {
            that.confirmLoading = true
            var param = {}
            param.prolsh = that.prolsh
            var flag = true;
            param.code = that.divisioncode;

            // 权利、附属权利信息
            that.$refs.qlfsqlmessage.form.validateFields((err, values) => {
              if (!err) {
                param = Object.assign(param, values)
                if (param.ql.qlsj) {
                  //转换一下时间格式
                  var qlqssj = param.ql.qlsj[0].format('YYYY-MM-DD')
                  var qljssj = param.ql.qlsj[1].format('YYYY-MM-DD')
                  param.ql.qlqssj = qlqssj
                  param.ql.qljssj = qljssj
                }
                param.dyarrys = that.housedata
                param.dyid = that.dyid
                param.fsqlid = that.fsqlid
                param.qlid = that.qlid
              } else {
                for (var obj in err.ql) {
                  that.$notification['error']({
                    message: '请填写必填字段',
                    description: err.ql[obj].errors[0].message
                  })
                  flag = false
                  return false
                }
                for (var obj in err.fsql) {
                  that.$notification['error']({
                    message: '请填写必填字段',
                    description: err.fsql[obj].errors[0].message
                  })
                  flag = false
                  return false
                }
              }
            })

            if (!flag) {
              that.confirmLoading = false
              return
            }
            //先保存
            //postAction(that.url.saveProject, param).then((res) => {
              //if (res.success) {
                //成功后提交（此处改为直接提交）
                ajaxaciton.get(window._CONFIG['domianURL'] + that.url.submitProject, { prolsh: param.prolsh }, function(response) {
                    if (response) {
                      if (response.success) {
                        that.nextStep()
                      } else {
                        that.$message.error(response.message)
                      }
                    } else {
                      that.$message.error(response)
                    }
                    that.confirmLoading = false
                  },
                  function(status) {
                    that.$message.error('网络异常，请求失败')
                  })

              // } else {
              //  that.confirmLoading = false
              //  that.$message.warning(res.message)
              // }
            //})

          }
        })

      },
      saveProject() {
        var that = this
        var param = {}
        param.prolsh = this.prolsh
        param.code = that.divisioncode;

        param = Object.assign(param, that.$refs.qlfsqlmessage.form.getFieldsValue())
        if (param.ql.qlsj) {
          //转换一下时间格式
          var qlqssj = param.ql.qlsj[0].format('YYYY-MM-DD')
          var qljssj = param.ql.qlsj[1].format('YYYY-MM-DD')
          param.ql.qlqssj = qlqssj
          param.ql.qljssj = qljssj
        }
        var qlid = param.ql.qlid
        param.dyid = this.dyid
        param.fsqlid = this.fsqlid
        param.qlid = this.qlid
        postAction(that.url.saveProject, param).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
          } else {
            that.$message.warning(res.message)
          }
        })

      },
      loadDyData() {
        this.loading = true
        this.loadingfj = true
        getAction(this.url.queryByLsh, { prolsh: this.prolsh }).then((res) => {
          if (res.success) {
            this.housedata = res.result
            this.selectedhouse(0)
          } else {
            this.$notification['error']({
              message: '数据加载失败：' + res.message
            })
          }
          this.loading = false
          this.loadingfj = false
        })
      },
      loadProjectData(prolsh, dyid) {
        getAction(this.url.projectMessage, { prolsh: prolsh, dyid: dyid }).then((res) => {
          this.dyid = dyid
          if (res.success) {
            var project = res.result
            var bdcql = {};
            var bdcfsql = {};
            if (project.bdcql && project.bdcql.length > 0) {
              bdcql = project.bdcql[0]
            }
            if (project.bdcfsql && project.bdcfsql.length > 0) {
              bdcfsql = project.bdcfsql[0]
              bdcfsql.zl = this.housedetail.zl
              this.fsqlid = bdcfsql.fsqlid
            }
            var proinst = project.proinst
            var prodef = project.prodef
            this.initpageview(prodef)
            var materlist = project.materclass
            this.materclassdata = materlist
            this.divisioncode = proinst.divisionCode;
            this.form.setFieldsValue({divisioncode:proinst.divisionCode});

            var qlsj = []
            if (bdcql && bdcql.qlqssj) {
              var qsmoment = moment(bdcql.qlqssj, 'YYYY-MM-DD')
              var jsmoment = moment(bdcql.qljssj, 'YYYY-MM-DD')
              qlsj.push(qsmoment)
              qlsj.push(jsmoment)
              bdcql.qlsj = qlsj
            }
            if (bdcql) {
              bdcql.sfhbzs = proinst.sfhbzs

            }
            bdcql.sfhbzs = "0";
            var qlobj = {}
            qlobj.ql = bdcql
            qlobj.fsql = bdcfsql

            //抵押变更前权利信息
            var lastql = project.lastql
            if (lastql) {
              var lastqlsj = []
              if (lastql && lastql.qlqssj) {
                var qsmoment = moment(lastql.qlqssj, 'YYYY-MM-DD')
                var jsmoment = moment(lastql.qljssj, 'YYYY-MM-DD')
                lastqlsj.push(qsmoment)
                lastqlsj.push(jsmoment)
                lastql.qlsj = lastqlsj
              }
            }
            this.$nextTick(() => {
              this.$refs.qlfsqlmessage.form.setFieldsValue(qlobj)
              if (lastql) {
                this.$refs.bgqlmessage.form.setFieldsValue(lastql)
              }
              this.$refs.ywrmessage.loadData()
              this.$refs.qlrmessage.loadData()
            })

          } else {
            this.$notification['error']({
              message: '数据加载失败：' + res.message
            })
          }

        })
      },
      initpageview(prodef) {
        if (prodef) {
          this.qlrshow = prodef.qlrflage && prodef.qlrflage == '1' ? true : false
          this.ywrshow = prodef.ywrflage && prodef.ywrflage == '1' ? true : false
          this.dyqrshow = prodef.dyqrflage && prodef.dyqrflage == '1' ? true : false
          this.djlx = prodef.djlx
          this.qllx = prodef.qllx
        }
      },
      prevStep() {
        var that = this
        that.$confirm({
          title: '确认重新选择？',
          content: '您已选择了单元并创建了业务，是否重新选择单元？',
          onOk: function() {
            that.$emit('prevStep')
          }
        })
      },
      //第几个房屋被选择
      selectedhouse(index) {
        var newhousedata = []
        for (var key in this.housedata) {
          if (key == index) {
            //改变该单元的样式
            this.housedata[key].thisselected = 'ant-alert-info'
            //显示右边单元详细信息
            this.housedetail = this.housedata[key]
            this.housedetail.house = ''
            if (this.housedata[key].houseclob) {
              var housedata = JSON.parse(this.housedata[key].houseclob)
              if (housedata) {
                this.housedetail.house = housedata.house
              }
            }
            this.loadProjectData(this.prolsh, this.housedata[key].id)
          } else {
            this.housedata[key].thisselected = ''
          }
          newhousedata.push(this.housedata[key])
        }
        this.housedata = newhousedata
      },
      //显示高拍仪采集弹窗
      showModalCollect(){
        this.cjvisible = true
        console.log(this.materclassdata);
      },
      closeCollect(){
        this.cjvisible = false
      },
      // 显示收费信息弹框
      showModalPrice() {
        this.visible = true
      },
      onSelectChange(selectedRowKeys) {
        this.selectedRowKeys = selectedRowKeys
      },
      hideModal() {
        this.isShowModalSpin = true
        let data = qs.stringify({
          ids: this.selectedRowKeys.join(','),
          ywlsh: this.prolsh
        })
        axios({
          method: 'post',
          url: `${window._CONFIG['domianURL']}/sfxx`,
          data: data
        }).then((res) => {
          console.log(res)
          this.visible = false
          this.sfxxList()
          this.isShowModalSpin = false
          this.$message.success('保存成功')
        }).catch((err) => {
          console.log(err)
          this.$message.error('保存数据失败！')
          this.isShowModalSpin = false
        })
      },
      sfxxList() {
        axios.get(`${window._CONFIG['domianURL']}/sfxx?ywlsh=${this.prolsh}`)
          .then((res) => {
            this.sfxxdata = res.result
            let price = 0
            this.selectedRowKeys = []
            this.sfxxTableList = []
            for (let i = 0; i < res.result.length; i++) {
              if (res.result[i].checked === true) {
                price += res.result[i].sfjs
                this.selectedRowKeys.push(res.result[i].key)
                this.sfxxTableList.push(res.result[i])
              }
            }
            this.price = price.toFixed(2)
            console.log(res)
          }).catch((err) => {
          // handle error
          console.log(err)
        })
      },
      onDelete(key) {
        axios.delete(`${window._CONFIG['domianURL']}/sfxx?id=${key}&ywlsh=${this.prolsh}`)
          .then((res) => {
            this.isShowModalSpin = false
            this.$message.success('删除成功')
            this.sfxxList()
          }).catch((err) => {
          this.$message.error('删除失败！')
          this.isShowModalSpin = false
        })
      },
      close() {
        this.visible = false
        this.sfxxList()
      },
      getEDM(record){
        //获取拍照组件
        this.$refs.scmodel.visible=true;
        this.$refs.scmodel.filesdata = record;
        //this.$refs.scmodel.upfileurl = window._CONFIG['domianURL']+"/mongofile/uploadFileWiiSan";
        this.$refs.scmodel.upfileurl = window._CONFIG['domianURL']+"/mongofile/uploadFileLT";
        //this.$refs.scmodel.load(window._CONFIG['domianURL']+"/mongofile/uploadFileWiiSan");
        this.$refs.scmodel.load(window._CONFIG['domianURL']+"/mongofile/uploadFileLT");
      },

    }
  }
</script>

<style lang="scss" scoped>
  .stepFormText {
    margin-bottom: 24px;

    .ant-form-item-label,
    .ant-form-item-control {
      line-height: 22px;
    }
  }
  .detailListItem{
    width:50%;
    clear:both;
  }
  .action {
    text-align: center;
    margin: 0 auto;
    padding: 24px 0 8px;
    position: fixed;
    width: 100%;
    bottom: 0;
    right: 0;
    height: 70px;
    line-height: 56px;
    box-shadow: 0 -1px 2px rgba(0, 0, 0, 0.03);
    background: #fff;
    border-top: 1px solid #e8e8e8;
    padding: 0 24px;
    z-index: 9;
  }

  .sfxx td {
    border: 2px solid #000;
    font-weight: bold;
    height: 32px;
    padding-left: 4px;
  }

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