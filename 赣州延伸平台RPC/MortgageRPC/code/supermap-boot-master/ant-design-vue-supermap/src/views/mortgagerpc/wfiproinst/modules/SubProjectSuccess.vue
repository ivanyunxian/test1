<template>
  <a-card :bordered="false">
    <result :is-success="true" :description="description" :title="title">
      <template slot="action">
        <a-button type="primary" @click="nextProject"  v-show="nextbox">继续申请</a-button>
        <a-button type="primary" @click="showeditbox"  v-show="editbox">继续申请</a-button>
        <a-button style="margin-left: 8px" @click="jump">已办箱</a-button>
      </template>
      <div>
        <div style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">项目名称 : {{data.PROJECT_NAME}}</div>
        <a-row style="margin-bottom: 16px">
          <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="6">
            <span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">项目流水号 : {{data.PROLSH}}</span>
          </a-col>
          <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="6">
            <span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">申请人：{{data.ACCEPTOR}}</span>
          </a-col>
          <a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="12">
            <span style="font-size: 16px; color: rgba(0, 0, 0, 0.85); font-weight: 500; margin-bottom: 20px;">申请时间： {{data.CREAT_DATE}}</span>
          </a-col>
        </a-row>

      </div>
    </result>
  </a-card>
</template>

<script>
  import Result from '@/views/result/Result'
  import { mixinDevice } from '@/utils/mixin.js'
  import {getAction} from '@/api/manage'

  const directionType = {
    horizontal: 'horizontal',
    vertical: 'vertical'
  }

  export default {
    name: "SubProjectSuccess",
    components: {
      Result
    },
    mixins: [mixinDevice],
    data () {
      return {
        title: '提交成功',
        description: '尊敬的用户，您的不动产登记业务申报已经提交成功',
        directionType,
        url:{
          projectlist: "/modules/wfi_proinst/projectlist"
        },
        data:{},
        nextbox : false,
        editbox : false,
        urlpath : ''
      }
    },
    methods :{
      initsbuPage(prolsh,urlpath) {
        if(urlpath && urlpath.indexOf("Wfi_proinstList") != -1){
          this.nextbox = false;
          this.editbox = true
        } else {
          this.nextbox = true;
          this.editbox = false
        }

        getAction(this.url.projectlist,{prolsh:prolsh}).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.data = res.result.records[0];
            console.log(this.data);

          } else {
            this.$message.error(res.message);
          }
        });
      },
      jump() {
        this.$router.push({path: '/mortgagerpc/wfi_proinst/projectEndlist'})
      },
      nextProject() {
        this.$parent.nextProject();
      },
      showeditbox() {
        this.$router.push({path: '/mortgagerpc/wfiprodef/prodefCardList'})
      }

    }

  }
</script>

<style scoped>

</style>