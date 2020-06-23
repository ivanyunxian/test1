<template>
  <a-modal
    :width="modal.width"
    :style="modal.style"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="false"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-tabs defaultActiveKey="1" >
      <a-tab-pane tab="项目信息" key="1">

        <step2 ref="orderaccept" :prolsh="prolsh" :actionshow="false" :disabled="true"/>

      </a-tab-pane>
      <a-tab-pane tab="进度信息" key="2" forceRender>
        <template slot="action">
          <a-button type="primary" @click=""  v-show="true">继续申请</a-button>
          <a-button type="primary" @click=""  v-show="true">继续申请</a-button>
          <a-button style="margin-left: 8px" @click="">已办箱</a-button>
        </template>
        <result  :is-success="progressresult" :description="description" :title="title">
          <div>
            <!--<div style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">项目名称 : {{data.projectName}}</div>-->
            <!--<a-row style="margin-bottom: 16px">-->
              <!--<a-col :xs="32" :sm="24" :md="24" :lg="24" :xl="12">-->
                <!--<span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">项目流水号 : {{data.prolsh}}</span>-->
              <!--</a-col>-->
              <!--<a-col  :xs="24" :sm="12" :md="12" :lg="12" :xl="6">-->
                <!--<span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">申请人：{{data.acceptor}}</span>-->
              <!--</a-col>-->
              <!--<a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="6">-->
                <!--<span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">申请时间： {{data.creatDate}}</span>-->
              <!--</a-col>-->
            <!--</a-row>-->
            <a-steps :current="current" :direction=" isMobile() &&directionType.vertical || directionType.horizontal" progressDot>
              <a-step :key="index" v-for="(item, index) in progress">
                <template v-if="item.actinsttype != 2">
                  <span style="font-size: 16px" slot="title">{{item.actdef_name}}</span>
                  <template slot="description">
                    <div style="fontSize: 12px; color: rgba(0, 0, 0, 0.45); position: relative; left: 42px;" slot="description" >
                      <div style="margin: 8px 0 4px">
                        {{item.blry}}
                        <a-icon style="margin-left: 8px; color: #00A0E9" type="dingding-o" />
                      </div>
                      <div>{{formatTime(item.actinst_start,'Y-M-D h:m:s')}}</div>
                    </div>
                  </template>
                </template>
                <template v-else>
                  <span style="font-size: 16px" slot="title">{{item.actdef_name}}</span>
                  <div>未开始</div>
                </template>
              </a-step>
            </a-steps>
          </div>
        </result>
      </a-tab-pane>
      <!--<a-tab-pane tab="登记信息" key="3" forceRender>
          <bdc_zs-list ref="bdczs"  :prolsh="prolsh"/>
      </a-tab-pane>-->
    </a-tabs>

  </a-modal>
</template>

<script>
  import FooterToolBar from '@/components/tools/FooterToolBar'
  import {httpAction,getAction} from '@/api/manage'
  import moment from "moment"
  import Bdc_zsList from "../bdczs/Bdc_zsList";
  import Result from '@/views/result/Result'
  import { mixinDevice } from '@/utils/mixin.js'
  import Step2 from '@/views/mortgagerpc/acceptProject/stepForm/Step2_AcceptProject'

  export default {
    name: "OrderacceptprojectModal",
    components: {
      Bdc_zsList,
      FooterToolBar,
      moment,
      Result,
      Step2
    },
    mixins: [mixinDevice],
    data () {
      return {
        visible: false,
        title: '项目详情',
        description : '业务流程正常审批中',
        confirmLoading : false,
        modal: {
          width: '95%',
          style: { top: '20px' },
          fullScreen: true
        },
        directionType:{
          horizontal: 'horizontal',
          vertical: 'vertical'
        },
        data:{},
        prolsh:'',
        url:{
          projectMessage : '/modules/wfi_proinst/projectMessage',
          projectlist: "/modules/wfi_proinst/projectlist",
          projectProgress:"/modules/wfi_proinst/getProjectProgress"
        },
        isdisabled: false,
        urlpath : '',
        progress:[],
        current:0,
        progressresult:true
      }
    },
    created(){

    },
    methods: {
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      initProgressPage(prolsh) {
        this.prolsh= prolsh;
        this.visible = true;
        this.$nextTick(() => {
          //初始化进度数据
          getAction(this.url.projectProgress,{prolsh:this.prolsh}).then((res)=>{
            if (res.success) {
              if(res.result && res.result.data[0] && res.result.data[0].length>0) {
                this.progress = res.result.data[0];
                console.log( "进度"+this.progress);
                this.progressresult = true;
                for(var index in this.progress) {
                  if(this.progress[index].actinsttype == 1) {
                    this.current = index;
                  }
                }
              } else {
                this.progressresult = false;
                this.progress ="";
                this.description = "该业务暂无办理进度信息"
              }
            } else {
                this.progressresult = false;
                this.progress ="";
              this.$message.error("进度信息查询失败："+res.message);
            }
          })
        })

      },
      cancel (key) {
        let target = this.dyarrys.filter(item => item.key === key)[0]
        target.editable = false
      },
      handleChange (value, key, column) {
        const newData = [...this.dyarrys]
        const target = newData.filter(item => key === item.key)[0]
        if (target) {
          target[column] = value
          this.dyarrys = newData
        }
      },
      setdisabled(lsh){
        //已办箱只读方法
        this.isdisabled = true;
        this.prolsh = lsh;
        this.initProgressPage(lsh);
        var that = this;
        this.$nextTick(() => {
          this.$refs.orderaccept.loadDyData();
          this.$refs.orderaccept.selectedhouse(0);
          this.$refs.bdczs.loadData();

        })
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
  .card{
    margin-bottom: 24px;
  }
</style>