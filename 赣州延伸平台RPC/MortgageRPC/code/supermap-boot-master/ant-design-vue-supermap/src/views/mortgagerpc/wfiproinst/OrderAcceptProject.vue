<template>
  <div>
    <a-spin fix :spinning="confirmLoading">
    <a-card class="card" title="申报机构信息" :bordered="false">
      <deptmessage-form ref="dept" :showSubmit="false" />
    </a-card>
    <a-card class="card" title="权利人信息" :bordered="false"  v-show="qlrshow">
      <qlrmessage-form ref="qlrmessage" :showSubmit="false" :prolsh="prolsh" />
    </a-card>
    <a-card class="card" title="义务人信息信息" :bordered="false" v-show="ywrshow">
      <ywrmessage-form ref="ywrmessage" :showSubmit="false" :prolsh="prolsh"/>
    </a-card>
    <a-card class="card" title="抵押权人信息" :bordered="false" v-show="dyqrshow">
      <dyqrmessage-form ref="dyqrmessage" :showSubmit="false" :prolsh="prolsh"/>
    </a-card>
    <a-card class="card" title="权利信息" :bordered="false">
      <qlmessage-form ref="ql" :showSubmit="false" />
    </a-card>

    <!-- table -->
    <a-card>
      <form :autoFormCreate="(form) => this.form = form">
        <a-table
          :columns="columns"
          :dataSource="dyarrys"
          :pagination="false"
        >
          <template v-for="(col, i) in [ 'bdcdyh', 'zl', 'mj', 'dgbdbzzqse']" :slot="col" slot-scope="text, record, index">
            <a-input
              :key="col"
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              :placeholder="columns[i].title"
              @change="e => handleChange(e.target.value, record.key, col)"
            />
            <template v-else>{{ text }}</template>
          </template>
          <template slot="operation" slot-scope="text, record, index">
            <template v-if="record.editable">
              <span v-if="record.isNew">
                <a @click="saveRow(record.key)">添加</a>
                <a-divider type="vertical" />
                <a-popconfirm title="是否要删除此行？" @confirm="remove(record.key)">
                  <a>删除</a>
                </a-popconfirm>
              </span>
              <span v-else>
                <a @click="saveRow(record.key)">保存</a>
                <a-divider type="vertical" />
                <a @click="cancel(record.key)">取消</a>
              </span>
            </template>
            <span v-else>
              <a @click="toggle(record.key)"  >编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="是否要删除此行？" @confirm="remove(record.key)" class="setButton" >
                <a>删除</a>
              </a-popconfirm>
            </span>
          </template>
        </a-table>
        <a-button class="setButton"  style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newMember">添加单元</a-button>
      </form>
    </a-card>

    <!-- fixed footer toolbar -->
    <footer-tool-bar>
      <a-button class="cardButton" type="primary" style="margin-left: 10px;display: none" @click="nextPage" :loading="nextloading">上一页</a-button>
      <!-- 在办箱返回上一步用 -->
      <a-button class="reBoxButton" type="primary" style="margin-left: 10px;display: none"  @click="topBoxPage">上一页</a-button>
      <a-button class="setButton" type="primary" @click="save" style="margin-left: 10px;" :loading="saveloading">保存</a-button>
      <a-button class="setButton" type="primary" @click="subProject" style="margin-left: 10px;" :loading="subloading">提交</a-button>
      <!-- 已办箱返回上一步用 -->
      <a-button class="reEndBoxButton" type="primary" style="margin-left: 10px;display: none"  @click="topEndBoxPage">上一页</a-button>

    </footer-tool-bar>
    </a-spin>
  </div>
</template>

<script>
  import DeptmessageForm from './modules/form/DeptmessageForm'
  import QlrmessageForm from './modules/form/QlrmessageForm'
  import DyqrmessageForm from './modules/form/DyqrmessageForm'
  import YwrmessageForm from './modules/form/YwrmessageForm'
  import QlmessageForm from './modules/form/QlmessageForm'
  import FooterToolBar from '@/components/tools/FooterToolBar'
  import {httpAction,getAction} from '@/api/manage'
  import moment from "moment"

  export default {
    name: "OrderAcceptProject",
    components: {
      FooterToolBar,
      DeptmessageForm,
      QlrmessageForm,
      YwrmessageForm,
      DyqrmessageForm,
      QlmessageForm,
      moment
    },
    data () {
      return {
        // description: '高级表单常见于一次性输入和提交大批量数据的场景。',
        nextloading : false,
        saveloading: false,
        subloading : false,
        confirmLoading : false,
        columns: [
          {
            title: '不动产单元号',
            dataIndex: 'bdcdyh',
            key: 'bdcdyh',
            width: '20%',
            scopedSlots: { customRender: 'bdcdyh' }
          },
          {
            title: '坐落',
            dataIndex: 'zl',
            key: 'zl',
            width: '20%',
            scopedSlots: { customRender: 'zl' }
          },
          {
            title: '面积',
            dataIndex: 'mj',
            key: 'mj',
            width: '20%',
            scopedSlots: { customRender: 'mj' }
          },
          {
            title: '该单元债权数额',
            dataIndex: 'dgbdbzzqse',
            key: 'dgbdbzzqse',
            width: '20%',
            scopedSlots: { customRender: 'dgbdbzzqse' }
          },
          {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'operation' }
          }
        ],
        dyarrys: [
        ],
        prolsh:'',
        url:{
          saveproject : '/modules/wfi_proinst/saveproejct',
          projectMessage : '/modules/wfi_proinst/projectMessage',
          submitProject : '/modules/wfi_proinst/submitProject'
        },
        qlrshow: true,
        ywrshow: true,
        dyqrshow:false,
        isdisabled: false,
        urlpath : ''
      }
    },
    created(){
    },
    methods: {
      initPage() {
        this.urlpath = this.$parent.$el.baseURI;
        this.$refs.qlrmessage.setProlsh(this.prolsh,this.urlpath);
        this.$refs.ywrmessage.setProlsh(this.prolsh,this.urlpath);
        this.$refs.dyqrmessage.setProlsh(this.prolsh,this.urlpath);
        this.$refs.qlrmessage.loadData();
        this.$refs.ywrmessage.loadData();
        this.$refs.dyqrmessage.loadData();

        getAction(this.url.projectMessage,{prolsh:this.prolsh}).then((res)=>{
          if(res.success){
            var project = res.result;
            var dept = project.dept;
            var dylist = project.bdcdys;
            var bdcql = project.bdcql;
            var proinst = project.proinst;
            var prodef = project.prodef;
            //解析成前端展示数据
            for(var key in dylist) {
              this.dyarrys.push({
                key: dylist[key].id,
                bdcqzh: dylist[key].bdcqzh,
                bdcdyh: dylist[key].bdcdyh,
                zl: dylist[key].zl,
                mj: dylist[key].mj,
                dgbdbzzqse: dylist[key].dgbdbzzqse,
                editable: false,
                isNew: false
              })
            }
            var qlsj = [];
            if(bdcql && bdcql.qlqssj) {
              var qsmoment = moment(bdcql.qlqssj,'YYYY-MM-DD');
              var jsmoment = moment(bdcql.qljssj,'YYYY-MM-DD');
              qlsj.push(qsmoment);
              qlsj.push(jsmoment);
              bdcql.qlsj = qlsj;
            }
            if(bdcql) {
              bdcql.sfhbzs = proinst.sfhbzs;
            }
            var qlobj = {};
            qlobj.ql = bdcql;
            var deptobj = {};
            deptobj.dept = dept;
            //控制权利人，义务人，抵押权人的显示
            if(prodef) {
              this.qlrshow = prodef.qlrflage && prodef.qlrflage=='1' ? true:false;
              this.ywrshow = prodef.ywrflage && prodef.ywrflage=='1' ? true:false;
              this.dyqrshow = prodef.dyqrflage && prodef.dyqrflage=='1' ? true:false;
            }
            this.$refs.ql.form.setFieldsValue(qlobj);
            this.$refs.dept.form.setFieldsValue(deptobj);
          } else {
            this.$message.warning(res.message);
          }
        });
      },
      handleSubmit (e) {
        e.preventDefault()
      },
      newMember () {
        var key = new Date().getTime();
        this.dyarrys.push({
          key: key,
          bdcqzh: '',
          bdcdyh: '',
          zl: '',
          mj: '',
          dywjz: '',
          editable: true,
          isNew: true
        })
      },
      remove (key) {
        const newData = this.dyarrys.filter(item => item.key !== key)
        this.dyarrys = newData
      },
      saveRow (key) {
        let target = this.dyarrys.filter(item => item.key === key)[0]
        target.editable = false
        target.isNew = false
      },
      toggle (key) {
        let target = this.dyarrys.filter(item => item.key === key)[0]
        target.editable = !target.editable
      },
      getRowByKey (key, newData) {
        const dyarrys = this.dyarrys
        return (newData || dyarrys).filter(item => item.key === key)[0]
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

      // 验证并返回数据,参数，true or false ，是否校验数据
      validate (check) {
        var projectData = {};
        var result = true;
        if(check) {
          this.$refs.dept.form.validateFields((err, values) => {
            if (!err) {
              projectData =  Object.assign(projectData, values);
            } else {
              for(var obj in err.dept){
                this.$notification['error']({
                  message: '请填写必填字段',
                  description: err.dept[obj].errors[0].message
                })
                result = false;
                return true ;
              }
            }
          })
        } else {
          projectData =  Object.assign(projectData, this.$refs.dept.form.getFieldsValue());
        }

        if(check) {
          this.$refs.ql.form.validateFields((err, values) => {
            if (!err) {
              projectData =  Object.assign(projectData, values);
            } else {
                for(var obj in err.ql){
                  this.$notification['error']({
                    message: '请填写必填字段',
                    description: err.ql[obj].errors[0].message
                  })
                  result = false;
                  return true ;
                }
            }
          })
        } else {
          projectData =  Object.assign(projectData, this.$refs.ql.form.getFieldsValue());
        }
        if(result) {
          projectData.dyarrys = this.dyarrys;
          projectData.prolsh =  this.prolsh;
          if(projectData.ql.qlsj) {
            //转换一下时间格式
            var qlqssj = projectData.ql.qlsj[0].format('YYYY-MM-DD');
            var qljssj = projectData.ql.qlsj[1].format('YYYY-MM-DD');
            projectData.ql.qlqssj = qlqssj;
            projectData.ql.qljssj = qljssj;
          }
          return projectData;
        } else {
          return null;
        }
      },
      //保存
      save() {
        var that = this;
        var projectData = this.validate(false);
        httpAction(this.url.saveproject,{projectdata: JSON.stringify(projectData)},'post').then((res)=>{
          if(res.success){
            that.$message.success(res.message);
          }else{
            that.$message.warning(res.message);
          }
        });
      },
      subProject() {
        var that = this;
        that.subloading = true;
        that.confirmLoading = true;
        var projectData = this.validate(true);
        if(projectData) {
          console.log(projectData);
          httpAction(this.url.saveproject,{projectdata: JSON.stringify(projectData)},'post').then((res)=>{
            if(res.success){
              getAction(this.url.submitProject,{prolsh:this.prolsh}).then((subres)=>{
                if(subres.success){
                  that.$message.success(subres.message);
                  that.$parent.subsuccess(this.prolsh,this.urlpath);
                } else {
                  that.$message.warning(subres.message);
                }
                that.subloading = false;
                that.confirmLoading = false;


              });
            }else{
              that.$message.warning(res.message);
              that.subloading = false;
              that.confirmLoading = false;
            }

          });

        } else {
          console.log('校验不通过');
          that.subloading = false;
          that.confirmLoading = false;
        }

      }, nextPage() {
        this.$parent.showCard();
      },topEndBoxPage() {
        document.getElementsByClassName("reEndBoxButton")[0].style = "none"
        this.$parent.showendbox();
      }, getprolsh(lsh){
        this.prolsh = lsh;
        this.initPage();

        if(this.urlpath.indexOf("prodefCardList") != -1){
          document.getElementsByClassName("cardButton")[0].style = "block"
        }else if(this.urlpath.indexOf("Wfi_proinstList") != -1){
          document.getElementsByClassName("reBoxButton")[0].style = "block"
        }
      },setdisabled(lsh){
        //已办箱只读方法
        this.isdisabled = true;
        this.prolsh = lsh;
        this.initPage();
        var inputdatas = document.getElementsByTagName("input");
        for(var i= 0;i<inputdatas.length;i++){
          inputdatas[i].disabled = "disabled";
        };

        // var buttondatas = document.getElementsByTagName("button");
        // for(var i= 0;i<buttondatas.length;i++){
        //   buttondatas[i].disabled = "disabled";
        // };

        var setbuttons =  document.getElementsByClassName("setButton");
        var buttonindex = setbuttons.length;
        for(var i= 0;i<buttonindex;i++){
            setbuttons[0].remove();
        }

        //单元列表把操作那列干掉
        this.columns =  [
          {
            title: '不动产单元号',
            dataIndex: 'bdcdyh',
            key: 'bdcdyh',
            width: '20%',
            scopedSlots: { customRender: 'bdcdyh' }
          },
          {
            title: '坐落',
            dataIndex: 'zl',
            key: 'zl',
            width: '20%',
            scopedSlots: { customRender: 'zl' }
          },
          {
            title: '面积',
            dataIndex: 'mj',
            key: 'mj',
            width: '20%',
            scopedSlots: { customRender: 'mj' }
          },
          {
            title: '该单元债权数额',
            dataIndex: 'dgbdbzzqse',
            key: 'dgbdbzzqse',
            width: '20%',
            scopedSlots: { customRender: 'dgbdbzzqse' }
          }
        ]


        document.getElementsByClassName("reEndBoxButton")[0].style = "block"
      },topBoxPage() {
        document.getElementsByClassName("reBoxButton")[0].style = "none"
        this.$parent.showproinstbox();
      }
    }
  }
</script>

<style lang="scss" scoped>
  .card{
    margin-bottom: 24px;
  }
</style>