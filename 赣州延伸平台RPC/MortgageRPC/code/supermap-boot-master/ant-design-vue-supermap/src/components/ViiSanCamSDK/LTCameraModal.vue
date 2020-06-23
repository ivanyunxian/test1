<template>
<a-modal
     :title="title"
     :width="800"
     :visible="visible"
     :confirmLoading="confirmLoading"
     :maskClosable="false"
     @ok="modelclose"
     @cancel="modelclose"
     okText="完成"
     cancelText="关闭">
  <div>
    <table border="0" style = "margin-left:5%;" width="800" height="500">
    <tr id="video1">
      <img id="bigPriDev" style="width:506px;height:380px" >
    </tr>
    <tr style="margin-top:10px;float:left;">
      <td>
        <label>设备</label>
        <select id="devicList" ></select>
      </td>
      <td>
        <label style="margin-left: 30px;">分辨率</label>
        <select id="reslutionList"></select>
      </td>
    </tr>

    <tr  style="display: none">
          <span>存储路径:</span>
          <input type="text"  id="saveText" value="D:\tmp" style="margin-left:5px;width: 150px;text-align: left;"/>
          <button @click="OpenFile()" style="margin-left: 10px;">查看文件</button>
    </tr>
      <tr>
        <td>
          <br>
        </td>
      </tr>
    <tr>
      <td>
        <button @click="Capture()">拍照</button>
        <button @click="HttpUpload(1)" style="margin-left: 20px">上传图片</button>
        <span id="capturetext" style="margin-left: 200px"></span>
      </td>
    </tr>
      <tr>
        <td>
          <br>
        </td>
      </tr>
      <tr>
        <span id="capturemsg"></span>
      </tr>
    <tr style="margin-top: 20px; margin-left: 20px; ">
      <ul id="parentUl"></ul>
    </tr>

    </table>
  </div>
</a-modal>
</template>
<script>


  //初始化并配置UI

  import SplitPanel from '../../views/jeecg/SplitPanel'
  import channel from '@/utils/qwebchannel.js';
  import { postAction } from '@/api/manage'
  var dialog="";
  var imgPath="";
  var imgArray = new Array();
  var msg ="";
  var baseArray = new Array();
let SingleCamera = {
    first:1,
    devIndex : 0,
    comparePath:"",
    compareBase64:"",
    previewDevIndex:0,
    realPicPath:"",
    encodeBase64Tag:0,
    //初始化UI
  }

  export default {
    name: "LTCameraModal",
    components: { SplitPanel },
    data() {
      return {
        title: "良田高拍仪",
        visible: false,
        confirmLoading: false,
        ws: "",
        m_isConnectWS:false,
        m_splitTag : "$*$",
        m_lastMessage : "",
        m_imageDataH : 0,
        m_imageDataW : 0,
        m_imageDataS : 0,
        m_stopWait : false, //同步等待
        m_closed : false, //是否被关闭了
        m_ComparePicBase64 : false, //是否是人脸比对base64
        m_compareShowID : "", //显示人脸模块base64的控件id
        m_idcardBase64 : false, //是否是身份证图片的base64
        retCapture : 1,
        ocrFile : "",
        ocrType : 0,
        timer:0,
        videoNum:0,
        ResolutionNum:0,
        AutoCaptureTime:5,
        count:0,
        nCount:0,
        g_obj:"",
        g_savePicDir:"D:/tmp/", //拍照的文件存储位置
        filesdata:"",
        upfileurl:"",
        autoCaptureIndex:1,
        batchUploadFileId:"",
      }
    },
    created() {

    },
    methods: {
      show(){
        this.visible = true;
      },
      load(url){
        //启动服务
        //window.open("CamServer://");
        this.StartWebSocket();
        this.upfileurl = url;
      },
      addlist(obj,path)
      {
        console.log(obj);
        var ul = document.getElementById(obj);
        var li = document.createElement("li");
        var img = document.createElement("img");
        img.setAttribute("width", "128");
        img.setAttribute("height", "96");
        img.setAttribute("id", "newImg");
        img.setAttribute("alt", "图片不存在");
        img.setAttribute("title", path);
        var vm = this;
        img.onclick = function(){
          vm.showpicture(path)
        };
        img.src = "data:image/jpg;base64," + msg;
        li.appendChild(img);
        ul.appendChild(li);
        //不知道有没有用

      },
      closeDev(){  //关闭设备
      var vm = this;
        vm.m_isConnectWS = true;
        dialog.get_actionType("closeSignal");
        var element = document.getElementById("bigPriDev");
        element.src = "";
        document.getElementById("parentUl").innerHTML = "";

      },
      CheckImgExists(imgurl) {
        var ret  = "file://" + imgurl ;
        var ImgObj = new Image(); //判断图片是否存在
        ImgObj.src = ret;
        //没有图片，则返回-1
        if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
          return true;
        } else {
          return false;
        }
      },
      Capture(){
        var vm = this;
        var filepath = document.getElementById("saveText").value;
        console.log(filepath);
        var objSelect = document.getElementById("devicList");
        this.savefilepath = filepath;
        dialog.set_configValue("set_savePath:"+this.savefilepath);
        dialog.photoBtnClicked("primaryDev_");
        dialog.get_actionType("savePhotoPriDev");
        document.getElementById("capturetext").innerText = "拍照完成";
        //showImageFile(filepath);

        this.timer = setTimeout(function(){
        vm.addlist("parentUl",filepath);},2000);




      },
      HttpUpload(type){
        if(type == 1)
        {
          var datap = {};
          datap.id = this.filesdata.id;
          datap.materdataindex = this.filesdata.materdataindex;
          datap.prolsh = this.filesdata.prolsh;
          datap.savepath = this.savefilepath;
          datap.imgarray = imgArray;
          datap.base64str = baseArray;
          var filepath =  this.savefilepath.replace(/\\/g, "//");
          postAction(this.upfileurl, datap).then((res) => {
            if (res=="上传成功") {
              document.getElementById("parentUl").innerHTML = "";
              imgArray = new Array();
              baseArray = new Array();
            } else {
              that.confirmLoading = false
              that.$message.warning(res.message)
            }
          })
        }else if(type ==2)
        {
          var filepath = document.getElementById("UploadsaveText").value;
          var FtpAddressText = document.getElementById("FtpAddressText").value;
          var user = document.getElementById("user").value;
          var pwd = document.getElementById("pwd").value;
          var iport = document.getElementById("iport").value;
          var floder = document.getElementById("floder").value;
          var jsonObj = {FuncName:'camUpdataFileFtp',argument:{strfilePath:filepath,strftpPath:FtpAddressText,struserName:user,struserPsd:pwd,iPort:parseInt(iport),strtargetName:floder}};
        }
        //this.sendWsMessage(jsonObj);
        document.getElementById("capturetext").innerText = "上传图片完成";
      },
      StartWebSocket(){
        var url = "ws://localhost:12345";
        var vm = this;
        if('WebSocket' in window){
          vm.ws = new WebSocket(url);
        }
        else if('MozWebSocket' in window){
          vm.ws = new MozWebSocket(url);
        }else{
          alert("浏览器版本过低，请升级您的浏览器。\r\n浏览器要求：IE10+/Chrome14+/FireFox7+/Opera11+");
        }
        vm.ws.onopen = function()
        {
                  vm.m_isConnectWS = true;
                  document.getElementById("reslutionList").options.length = 0;
                  document.getElementById("devicList").options.length = 0;
                  new channel.QWebChannel(channel,vm.ws, function(channel) {
                       dialog = channel.objects.dialog;
           						//网页关闭函数

           						//设备列表点击
           						document.getElementById("devicList").onchange = function() {
           							//清除展示信息
           							var resolutionList = document.getElementById("reslutionList");
           							resolutionList.options.length = 0;
           							var select = document.getElementById("devicList");
           							dialog.devChanged("primaryDev_:" + select.value);
           						};

           						//分辨率列表点击
           						document.getElementById("reslutionList").onchange = function() {
           							//清除展示信息
           							var select = document.getElementById("reslutionList");
           							dialog.devChanged("primaryDev_:" + select.value);
           						};


           						//服务器返回消息
                    dialog.sendPrintInfo.connect(function(message) {
           							//output(message);
           							//设备信息 priModel
           							if(message.indexOf("priDevName:") >= 0)
           							{
           								message = message.substr(11);
           								var select = document.getElementById("devicList");
           								//select.options.length=0;
           								if(select.options.length<2){
                            if(message.indexOf("USB") >= 0)
                            {
                              select.add(new Option(message));
                            } else{
                              select.add(new Option(message), 0);
                            }
           								}
           								select.selectedIndex=0;
           							}
           							//设备出图格式
           							//设备分辨率
           							if(message.indexOf("priResolution:") >= 0)
           							{
           								message = message.substr(14);
           								var select = document.getElementById("reslutionList");

           								//select.options.length=0;
           								  select.add(new Option(message));
           								if(select.options.length > 1)
           								{
           									select.selectedIndex = 1;
           								}
           							}
           							//图片保存后返回路径关键字savePhoto_success:
           							else if(message.indexOf("savePhoto_success:") >= 0)
           							{
           								imgPath = message.substr(18);
           								if(imgArray.indexOf(imgPath)<0){
           								  imgArray.push(imgPath);
           								}
           							}

                                   });
           						//接收图片流用来展示，多个，较小的base64数据
           						dialog.send_priImgData.connect(function(message) {
           						 var element = document.getElementById("bigPriDev");
                       	element.src = "data:image/jpg;base64," + message;

                                   });
           						//接收拍照base64
           						dialog.send_priPhotoData.connect(function(message) {
           							msg = message;
           							if(baseArray.indexOf(msg)<0){
                          baseArray.push(msg);
           							}


                                   });
                                   //output("ready to send/receive messages!");
           						//网页加载完成信号
           						dialog.html_loaded("one");
           						dialog.set_configValue("set_savePath:C:/eloamFile");
           						dialog.get_functionTypes("setImageProperty", "300", "", "");
                               });

          //vm.unload();
          //vm.sendInitMsg();//初始化
          //vm.sendGetPath(); //获取电脑中的路径
          vm.m_closed = false;
        };

        document.getElementById("capturetext").innerText = "拍照准备就绪";

      },
      isIE() {
        if (!!window.ActiveXObject || "ActiveXObject" in window) {
          return true;
        } else {
          return false;
        }
      },
      modelclose () {
        this.closeDev();
        this.$emit('close');
        this.visible = false;
      }
    }
  }
</script>

<style scoped>

</style>