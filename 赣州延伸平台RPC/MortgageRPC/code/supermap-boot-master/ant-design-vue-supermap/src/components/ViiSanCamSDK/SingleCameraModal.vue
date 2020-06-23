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
      <img id="video" style="width:506px;height:380px" >
    </tr>

    <tr style="display: none">
      <button @click="ZoonMin()">+</button>
      <button @click="ZoonMOut()">-</button>
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

      <tr>
        <td>
          <br>
        </td>
      </tr>

    <tr  style="display: none">
      <label >视频格式</label>
      <select id="videoStyle" ></select>
    </tr>

    <tr  style="display: none">
      <label >蜂鸣器设置</label>
    </tr>

    <tr  style="display: none">
      <button @click="Buzzer()" style="margin-left: 10px;">蜂鸣器</button>
      <button @click="AutoFoucs()" style="margin-left: 10px;">自动对焦</button>
      <button @click="ShowDevSettingWindow()" style="margin-left: 10px;">设备设置</button>
      <button @click="ShowImageSettingWindow()" style="margin-left: 10px;">图像设置</button>
    </tr>

    <tr  style="display: none">
      <span>存储路径:</span>
      <input type="text"  id="saveText" value="D:\tmp\" style="margin-left:5px;width: 150px;text-align: left;"/>
      <button @click="OpenFile()" style="margin-left: 10px;">查看文件</button>
    </tr>

    <tr  style="display: none">
      <span >颜色类型</span>
      <select onchange="SetColorStyle()" id="colorStyle">
        <option >彩色</option>
        <option >灰度</option>
        <option >黑白</option>
      </select>
      <span>PS特效</span>
      <select onchange="setImageAdjust()" id ="imageAdjust">
        <option >无效果</option>
        <option >文档增强</option>
        <option >彩色增强</option>
        <option >灰色</option>
        <option >黑白</option>
        <option >油画</option>
        <option >怀旧</option>
        <option >素描</option>
        <option >边缘照亮</option>
        <option >蓝冷</option>
        <option >马赛克</option>
        <option >模糊</option>
      </select>
    </tr>

    <tr  style="display: none">
      <span >图像类型</span>
      <select onchange="SetImageType()" id="imageType">
        <option >jpg</option>
        <option >bmp</option>
        <option >png</option>
        <option >tif</option>
        <option >ico</option>
        <option >pdf</option>
      </select>
      <span style="margin-left: 0px;">JPG压缩值:</span>
      <input type="text" id="jpg" value="75" />
      <button @click="JPGQSet()">设置JPG压缩率</button>
    </tr>

    <tr  style="display: none">
      <span >裁切类型</span>
      <select onchange="SetCutStyle()" id="cutStyle" >
        <option >不裁切</option>
        <option >单图裁切</option>
        <option >多图裁切</option>
      </select>
      <span >旋转类型</span>
      <select onchange="SetRotationStyle()" id="rotationStyle">
        <option >不旋转</option>
        <option >90</option>
        <option >180</option>
        <option >270</option>
      </select>
    </tr>

    <tr  style="display: none">
      <span>设置DPI</span>
      <input type="text" id="dpix" value="300" style="width: 50px;"/>
      <button @click="DPISet()" style="margin-left: 0px;">设置DPI</button>
      <input type = "checkbox" name = "Denoise" id = "Denoise" @click="SetDenoise(Denoise)"/> 去噪
    </tr>


    <tr class="sideDiv"  style="display: none">
      <span>手动裁切</span>
      <span style="margin-left: 10px;">左</span>
      <input type="text" value="0" id="left"  style="margin-left:5px;width:50px;"/>
      <span style="margin-left: 10px;">上</span>
      <input type="text" value="0" id="top" style="margin-left:5px;width: 50px;"/>
      <span style="margin-left: 10px;">右</span>
      <input type="text" value="100" id="right" style="margin-left:5px;width: 50px;"/>
      <span style="margin-left: 10px;">下</span>
      <input type="text" value="100" id="bottom" style="margin-left:5px;width: 50px;"/>
      <button @click="CropZoneSet()" style="margin-left: 10px;">设置裁切区域</button>
    </tr>

    <tr>
      <td>
        <button @click="Capture()">拍照</button>
        <!--   <button @click="CaptureBase64Ex()">拍照（Base64）</button>
           <button @click="CaptureBarcode()">拍照（条码识别）</button> -->
        <button @click="HttpUpload(1)" style="margin-left: 20px">上传图片</button>
        <span id="capturetext" style="margin-left: 200px"></span>
      </td>

    </tr>

      <tr>
        <td>
          <br>
        </td>
      </tr>

    <tr style="margin-left: 20px;">
      <td>
        <!-- <button @click="continuCapture()">连拍</button> -->
        <button @click="timeCapture()" >定时连拍上传</button>
        <button @click="StopAutoCapture()"  style="margin-left: 20px">停止连拍</button>

      </td>
    </tr>

      <tr>
        <td>
          <progress id="autoCaptureProgress" value="0" max="100" ></progress>
          <span style="margin-left: 20px">连拍时间间隔(秒):</span>
          <input readonly="readonly"  style="width:50px " type = "number" name="captureTime" id="captureTime" value="5" @change="setCaptureTime()" >
        </td>
      </tr>

      <tr>
        <span id="capturemsg"></span>
      </tr>

    <tr style="margin-top: 20px; margin-left: 20px;display: none ">
      <span>麦克风</span>
      <select id = "Microphone">
      </select>
      <span>录像格式</span>
      <select id="VideoType">
      </select>
      <br/>
      <br/>
      <span>存储路径</span>
      <input type="text"  id="SaveVieoText" value = "D:\tmp\Video.AVI" style="margin-left:5px;width: 150px;text-align: left;"/>
      <button  @click="StartVideo()">开始录像</button>
      <button @click="StopVideo()">停止录像</button>
      <ul id="parentUl"></ul>
    </tr>

    </table>
  </div>
</a-modal>
</template>

<script>


  //初始化并配置UI

  import SplitPanel from '../../views/jeecg/SplitPanel'
  let SingleCamera = {
    first:1,
    devIndex : 0,
    comparePath:"",
    compareBase64:"",
    previewDevIndex:0,
    realPicPath:"",
    encodeBase64Tag:0,
    //初始化UI
    InitSDKAndInitUI:function (obj){
      if (this.first == 1)
      {
        StartWebSocket();
        this.first = 0;
      }

    },
//反初始化
    UInitCamera:function (obj) {
      unload();
    },
  }

  export default {
    name: "SingleCameraModal",
    components: { SplitPanel },
    data() {
      return {
        title: "维山高拍仪",
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
      unload(){
        if(this.m_closed){
          return;
        }
        //反初始化
        var jsonObj = {FuncName:'camUnInitCameraLib'};
        this.sendWsMessage(jsonObj);
      },
      configureUIPath(path)//配置UI中的存储路径
      {
        document.getElementById("saveText").value = path;
        document.getElementById("Distory").value = path;
        //录像地址
        var filePath = path + "\\Video.AVI";
        document.getElementById("SaveVieoText").value = filePath;
        //读卡的头像地址
        filePath = path + "\\Head.bmp";
        document.getElementById("HeadPath").value = filePath;
        //图像合并的生成路径
        document.getElementById("pdftext").value = path;
        document.getElementById("UploadsaveText").value = path;
      },
      sendGetPath()//获取电脑中的特殊路径setImageWithBase64
      {
        var jsonObj = {FuncName:'GetSpecialFolderPath',argument:{nFolder:5}};
        this.sendWsMessage(jsonObj);
      },
      sendInitMsg(){ //初始化
        var jsonObj = {FuncName:'camInitCameraLib'};
        this.sendWsMessage(jsonObj);
      },
      sendPreZoneSize(){  //设置预览区域
        var w = document.getElementById("myCanvas").width;
        var h = document.getElementById("myCanvas").height;
        var jsonObj = {FuncName:'PreZoneSize',argument:{width:w,height:h}};
        this.sendWsMessage(jsonObj);
      },
      openDev(){  //打开设备
        var vm = this;
        if(this.m_closed){
          vm.StartWebSocket();
        }
        //打开摄像头
        var jsonObj = {FuncName:'camOpenDev',argument:{devIndex:SingleCamera.devIndex, subtype:0,width:0,height:0}};
        this.sendWsMessage(jsonObj);
        //开始预览
        jsonObj = {FuncName:'camStartPreview'};
        this.sendWsMessage(jsonObj);
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
        img.src = "file://" + path ;
        li.appendChild(img);
        ul.appendChild(li);
        //不知道有没有用
        clearTimeout(this.timer);
      },
      showpicture(path)
      {
        var jsonObj = {FuncName:'camShowImage',argument:{FilePath:path}};
        console.log(jsonObj);
        this.sendWsMessage(jsonObj);
      },
      DevSetting(){
        var objSelect = document.getElementById("devicList");
        var jsonObj = {FuncName:'camShowImageSettingWindow',argument:{devIndex:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      closeDev(){  //关闭设备
        var objSelect = document.getElementById("devicList");
        var jsonObj = {FuncName:'camCloseDev',argument:{devIndex:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      configureDevInfo(names){ //显示设备名
        //设备名字
        var vm = this;
        var objSelect = document.getElementById("devicList");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        objSelect[SingleCamera.devIndex].selected = true;
        //设置设备
        objSelect.onchange = function(){
          //打开摄像头
          var jsonObj = {FuncName:'camOpenDev',argument:{devIndex:objSelect.selectedIndex, subtype:0,width:0,height:0}};
          vm.sendWsMessage(jsonObj);
        }
      },
      configureVideoFormate(names) //显示视频格式
      {
        var objSelect = document.getElementById("videoStyle");
        objSelect.options.length = 0;

        for (var i = 0; i < names.length;i++ ) {
          if(names[i].length <=0){
            continue;
          }
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        objSelect[this.videoNum].selected = true;
        //设置分辨率
        objSelect.onchange = function(){

          var jsonObj = {FuncName:'camSetMediaType',argument:{index:objSelect.selectedIndex}};
          this.ResolutionNum = objSelect.selectedIndex;
          this.sendWsMessage(jsonObj);
        }
      },
      configureRestionInfo(names){
        var vm = this;
        var objSelect = document.getElementById("reslutionList");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          if(names[i].length <=0){
            continue;
          }
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        objSelect[this.ResolutionNum].selected = true;
        //设置分辨率
        objSelect.onchange = function(){
          var jsonObj = {FuncName:'camSetResolution',argument:{index:objSelect.selectedIndex}};
          this.ResolutionNum = objSelect.selectedIndex;
          vm.sendWsMessage(jsonObj);
        }
      },
      configureVideoStyle(names){
        var objSelect = document.getElementById("videoStyle");
        objSelect.options.length = 0;
        for (var i = 1; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        //设置视频格式
        objSelect.onchange = function(){
          //sendWsMessage("SetMediaType"+m_splitTag+String(objSelect.selectedIndex));
        }
      },
      ResizeImage(imageDest, W, H)
      {
//显示框宽度W,高度H
        var image = new Image();
        image.src = imageDest.src;
        image.width = 2592;
        image.height = 1944;
        if(image.width>0 && image.height>0)
        {
          //比较纵横比
          if(image.width/image.height >= W/H)//相对显示框：宽>高
          {
            if(image.width > W) //宽度大于显示框宽度W，应压缩高度
            {
              imageDest.width = W;
              imageDest.height = (image.height*W)/image.width;
            }
            else //宽度少于或等于显示框宽度W，图片完全显示
            {
              imageDest.width = image.width;
              imageDest.height = image.height;
            }
          }
          else//同理
          {
            if(image.height > H)
            {
              imageDest.height = H;
              imageDest.width = (image.width*H)/image.height;
            }
            else
            {
              imageDest.width = image.width;
              imageDest.height = image.height;
            }
          }
        }
      },
      setImageWithBase64(str){
        var myimg = document.getElementById("video");
        myimg.src = "data:image/png;base64,"+str;
        //ResizeImage(myimg, myimg.width, myimg.height);
      },
      SetRotationStyle(){
        var objSelect = document.getElementById("rotationStyle");
        var jsonObj = {FuncName:'camSetImageRotateMode',argument:{rotateMode:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      SetCutStyle(){
        var objSelect = document.getElementById("cutStyle");
        var jsonObj = {FuncName:'camSetImageAutoCrop',argument:{CropType:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      setImageAdjust(){
        var objectSelect = document.getElementById("imageAdjust");
        var jsonObj = {FuncName:'camSetImageAdjust',argument:{Type:objectSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      SetColorStyle(){
        var objectSelect = document.getElementById("colorStyle");
        var jsonObj = {FuncName:'camSetImageColorStyle',argument:{itype:objectSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      SetImageType(){
        var objectSelect = document.getElementById("imageType").value;
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
        var time = new Date();
        var checktime = time.getHours();
        var filepath = document.getElementById("saveText").value  + time.getYear() + time.getMonth ()+ time.getDate() + time.getDate ()+ time.getTime()+"." + document.getElementById("imageType").value;
        console.log(filepath);
        var objSelect = document.getElementById("devicList");
        this.savefilepath = filepath;
        if(objSelect.value == 1){
          //摄像头拍摄
          //jsonObj = {FuncName:'camMatchFaceByBase64Video',argument:{personFace:"1",videoFilePath:filepath,ldelayTime:0}};
        }
        var jsonObj = {FuncName:'camCaptureImageFile',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
        document.getElementById("capturetext").innerText = "拍照完成";

        //showImageFile(filepath);
        //if (t1 == 0) {
        this.timer = setTimeout(function(){
          vm.addlist("parentUl",filepath);},2000);
        //}
        //var t1 = window.setTimeout(addlist("parentUl",filepath),2000);
        //去掉定时器的方法
        //window.clearTimeout(t1);
      },
      CaptureBase64Ex(){
        var jsonObj = {FuncName:'CaptureEncodeBase64'};
        this.sendWsMessage(jsonObj);
      },
      CaptureBarcode(){
        var time = new Date();
        var checktime = time.getHours();
        var filepath = document.getElementById("saveText").value  + time.getYear() + time.getMonth ()+ time.getDate() + time.getDate ()+ time.getTime()+"." + document.getElementById("imageType").value;
        var jsonObj = {FuncName:'camCaptureImageFile',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
        var jsonObjCode = {FuncName:'coderRecognizeBarcode',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObjCode);
      },
      HttpUpload(type){
        if(type == 1)
        {
          var datap = {};
          datap.id = this.filesdata.id;
          datap.materdataindex = this.filesdata.materdataindex;
          datap.prolsh = this.filesdata.prolsh;
          var filepath =  this.savefilepath.replace(/\\/g, "//");
          var jsonObj = {FuncName:'camUpdataFileHttp',argument:{filePath:filepath, url:this.upfileurl,param:""+JSON.stringify(datap),response:""}};
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
        this.sendWsMessage(jsonObj);
        //document.getElementById("capturetext").innerText = "上传图片完成";
      },
      CaptureBase64()
      {
        var filepath = document.getElementById("saveText").value;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      DeleteFile(){
        var filepath = document.getElementById("saveText").value;
        var jsonObj = {FuncName:'camDeleteFile',argument:{FilePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      OpenFile(){
        var filepath = document.getElementById("saveText").value;
        var jsonObj = {FuncName:'camShowImage',argument:{FilePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      FindJPGFile()
      {
        var filepath = document.getElementById("Distory").value;
        var jsonObj = {FuncName:'getFolderDayFileA',argument:{Dictpry:filepath}};
        this.sendWsMessage(jsonObj);
      },
      DPISet(){
        var xdpi = document.getElementById("dpix").value;
        var jsonObj = {FuncName:'camSetImageDPI',argument:{xDPI:parseInt(xdpi),xDPI:parseInt(xdpi)}};
        this.sendWsMessage(jsonObj);
      },
      JPGQSet()
      {
        var JPGQ = document.getElementById("jpg").value;
        var jsonObj = {FuncName:'camSetImageJPGQuanlity',argument:{quanlity:parseInt(JPGQ)}};
        this.sendWsMessage(jsonObj);
      },
      SetCusCrop(obj){
        if(obj.checked)
        {
          var left = 0;
          var right = 0;
          var top = 100;
          var bottom = 100;
          var jsonObj = {FuncName:'camSetImageCusCropRect',argument:{left:parseInt(left),right:parseInt(right),top:parseInt(top),bottom:parseInt(bottom)}};
          this.sendWsMessage(jsonObj);
        }
        else{
          var left = 0;
          var right = 0;
          var top = 0;
          var bottom = 0;
          var jsonObj = {FuncName:'camSetImageCusCropRect',argument:{left:parseInt(left),right:parseInt(right),top:parseInt(top),bottom:parseInt(bottom)}};
          this.sendWsMessage(jsonObj);
        }
      },
      CropZoneSet(){
        var left = document.getElementById("left").value;
        var right = document.getElementById("right").value;
        var top = document.getElementById("top").value;
        var bottom = document.getElementById("bottom").value;
        var jsonObj = {FuncName:'camSetImageCusCropRect',argument:{left:parseInt(left),right:parseInt(right),top:parseInt(top),bottom:parseInt(bottom)}};
        this.sendWsMessage(jsonObj);
      },
      SetDenoise(obj)
      {
        var jsonObj = {FuncName:'camSetImageDenoise',argument:{IsAvailabel:obj.checked}};
        this.sendWsMessage(jsonObj);
      },
      showBase64info(str)
      {
        alert("Base64数据为："+ str);
      },
      continuCapture(){
        var filepath = document.getElementById("saveText").value + "\\autoCapture.jpg";
        var jsonObj = {FuncName:'camStartAutoCapture',argument:{type:0,param:4,filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      timeCapture(){
        this.batchUploadFileId = this.filesdata.id;
        var filepath = document.getElementById("saveText").value + this.filesdata.id+"AUTO.jpg";
        var jsonObj = {FuncName:'camStartAutoCapture',argument:{type:1,param:parseInt(this.AutoCaptureTime),filePath:filepath}};
        this.sendWsMessage(jsonObj);

      },
      StopAutoCapture(){
        var jsonObj = {FuncName:'camStopAutoCapture',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      AutoCaptureBack(re){
        var progress0 = document.getElementById("autoCaptureProgress");
        progress0.value = re * 100 / this.AutoCaptureTime ;
      },
      MicrophoneName(names){
        //设备名字
        var objSelect = document.getElementById("Microphone");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
      },
      VideoName(names){
        //设备名字
        var objSelect = document.getElementById("VideoType");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        objSelect.onchange = function(){
          var pathobj = document.getElementById("SaveVieoText");
          var savepath = pathobj.value ;
          var index = objSelect.selectedIndex;
          var text = objSelect.options[index].text;
          var str = savepath.indexOf(".") + 1;
          str = savepath.substr(0,str) + text;
          //pathobj.setAttribute("value",str);
          pathobj.value = str;
        }
      },
      StartVideo(){
        var savepath = document.getElementById("SaveVieoText").value;
        var objSelect = document.getElementById("devicList");
        var CurMicphone = document.getElementById("Microphone");
        var CurVideoFormat = document.getElementById("VideoType");
        //console.log(savepath,CurMicphone.selectedIndex,CurVideoFormat.selectedIndex);
        var jsonObj = {FuncName:'camStartRecord',argument:{filePath:savepath,micphone:CurMicphone.selectedIndex,videoFormat:CurVideoFormat.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      StopVideo(){
        var objSelect = document.getElementById("devicList");
        var jsonObj = {FuncName:'camStopRecord',argument:{devIndex:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
        var objSelect = document.getElementById("voice");
        objSelect.setAttribute("Value",0);
      },
      GetVoice(){
        var objSelect = document.getElementById("voice");
        var jsonObj = {FuncName:'camGetMicrophoneVolumeLevel',argument:{devIndex:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      ShowVioce(volume){
        var objSelect = document.getElementById("voice");
        //console.log(objSelect.value);
        objSelect.setAttribute("Value",volume);
      },
      addFile()
      {
        this.count++;

        var newDiv = "<div id=divUpload" + this.count + ">"
          + " <input  id=file"+this.count+" type=text width=1000 size=50 name=upload/>"
          +"<a href=javascript:delUpload('divUpload" + this.count+"')>删除</a>"
          + " </div>";

        var newDiv2 = "<div id=index" + this.count + ">" + this.count + " </div>";
        document.getElementById("uploadContent").insertAdjacentHTML("beforeEnd", newDiv);
        document.getElementById("Div2").insertAdjacentHTML("beforeEnd", newDiv2);
      },
      delUpload(diva) {
        document.getElementById("Div2").removeChild(document.getElementById("index"+this.count));
        this.count--;
        document.getElementById(diva).parentNode.removeChild(document.getElementById(diva));
      },
      CombineFile()
      {
        for(var i = 1;i<this.count+1;i++)
        {
          var path = document.getElementById("file" + i).value;
          if(path==null)
          {
            continue;
          }
          if(path.value=="")
          {
            continue;
          }
          var ret = AddFileToPDFList(path);
        }
        var fileext =".pdf";
        var strFolder = document.getElementById("pdftext").value;
        var myDate = new Date();
        var myName = "Image_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();

        var newFile = strFolder + "\\" + myName + fileext ;
        var result = CombinePDF(newFile, 50);
      },
      AddFileToPDFList(path)
      {
        var filepath = path;
        var jsonObj = {FuncName:'camAddFileToPDFList',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      CombinePDF(path,JQ)
      {
        var JpegQuaility = JQ;
        var filepath = path;
        var jsonObj = {FuncName:'camCombinePDF',argument:{filePath:filepath,JpegQuality:JpegQuaility}};
        this.sendWsMessage(jsonObj);
      },
      CombinePicture()
      {
        var path1 = document.getElementById("file1").value;
        var path2 = document.getElementById("file2").value;
        var fileext =".jpg";
        var strFolder = document.getElementById("pdftext").value;
        var myDate = new Date();
        var myName = "Image_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
        var newFile = strFolder + "\\" + myName + fileext ;

        var objectSelect = document.getElementById("CombineType").selectedIndex;
        var iType = 0;
        if(objectSelect == 0){
          iType = 7;
        }else{
          iType = 4;
        }

        var ioffsetX = 0;
        var ioffsetY = 0;

        var jsonObj = {FuncName:'camCombineImage',argument:{filePath1:path1,filePath2:path2,PdfPath:newFile,Type:iType,offsetX:ioffsetX,offsetY:ioffsetY}};
        this.sendWsMessage(jsonObj);
      },
      imgFormatter(value,row,index){
        if('' != value && null != value){
          var strs = new Array(); //定义一数组
          if(value.substr(value.length-1,1)==","){
            value=value.substr(0,value.length-1)
          }
          strs = value.split(","); //字符分割
          var rvalue ="";
          for (i=0;i<strs.length ;i++ ){
            rvalue += "<img onclick=download(\""+strs[i]+"\") style='width:66px; height:60px;margin-left:3px;' src='<%=path%>" + strs[i] + "' title='点击查看图片'/>";
          }
          return  rvalue;
        }
      },
      SetVideoParameter()
      {
        var vp1 = document.getElementById("VideoSetPara1").value;
        var vp2 = document.getElementById("VideoSetPara2").value;
        var vSettingValue = document.getElementById("VideoSetting").value;
        var vIsVideoSetAuto = document.getElementById("IsVideoSetAuto").value;
        var jsonObj = {FuncName:'camSetVideoParameter',argument:{ipara1:parseInt(vp1),ipara2:parseInt(vp2),ilvalue:parseInt(vSettingValue),iflag:parseInt(vIsVideoSetAuto)}};
        this.sendWsMessage(jsonObj);
      },
      showImageFile(szPath)
      {
        var szCount = "video" + this.nCount;
        document.getElementById(szCount).src = szPath;
        this.nCount = this.nCount + 1;
        if(this.nCount==14)
        {
          this.nCount = 0;
        }
      },
      ZoonMin()
      {
        var jsonObj = {FuncName:'camZooIn'};
        this.sendWsMessage(jsonObj);
      },
      ZoonMOut()
      {
        var jsonObj = {FuncName:'camZoomOut'};
        this.sendWsMessage(jsonObj);
      },
      OriginalPreview()
      {
        var jsonObj = {FuncName:'camOptimalPreview'};
        this.sendWsMessage(jsonObj);
      },
      OptimalPreview()
      {
        var jsonObj = {FuncName:'camOriginalPreview'};
        this.sendWsMessage(jsonObj);
      },
      ShowDevSettingWindow()
      {
        var jsonObj = {FuncName:'camShowDevSettingWindow'};
        this.sendWsMessage(jsonObj);
      },
      ShowImageSettingWindow()
      {
        var jsonObj = {FuncName:'camShowImageSettingWindow'};
        this.sendWsMessage(jsonObj);
      },
      Buzzer()
      {
        var jsonObj = {FuncName:'EnableBuzzer',argument:{nCount:parseInt(2),Duration:parseInt(300),Interval:parseInt(500)}};
        this.sendWsMessage(jsonObj);
      },
      AutoFoucs()
      {
        var jsonObj = {FuncName:'camAutoFocus',argument:{camAutoFocus:parseInt(0)}};
        this.sendWsMessage(jsonObj);
      },
      StartWebSocket(){
        var url = "ws://localhost:9000/";
        var vm = this;
        if('WebSocket' in window){
          vm.ws = new WebSocket(url);
        }
        else if('MozWebSocket' in window){
          vm.ws = new MozWebSocket(url);
        }else{
          alert("浏览器版本过低，请升级您的浏览器。\r\n浏览器要求：IE10+/Chrome14+/FireFox7+/Opera11+");
        }
        this.ws.onopen = function()
        {
          vm.m_isConnectWS = true;
          vm.unload();
          vm.sendInitMsg();//初始化
          vm.sendGetPath(); //获取电脑中的路径
          vm.m_closed = false;
        };
        vm.ws.onmessage = function (evt)
        {
          if(typeof(evt.data)=="string"){
            var str = evt.data;
            if(str.length <= 0){
              return;
            }
            if(str.indexOf("FileEncodeBase64") >=0){
              return;
            }
            if(str.indexOf(vm.m_splitTag)>=0){
              //视频的每一帧
              var strs= new Array();
              strs=str.split(vm.m_splitTag);
              vm.setImageWithBase64(strs[1]);
            }else{
              //处理其他请求
              var indexact = vm.AutoCaptureTime;
              if(str == "{\"FuncName\":\"AutoCaptureBack\",\"result\":\""+indexact+"\"}"){
                document.getElementById("capturemsg").innerText = "连拍开启成功!已拍摄第"+ vm.autoCaptureIndex+"张图片。";
                var filepathname = document.getElementById("saveText").value + vm.filesdata.id+"AUTO"+vm.autoCaptureIndex+".jpg";
                vm.HttpBatchUpload(filepathname);
                vm.autoCaptureIndex++;
              }
              console.log(str);
              vm.handleJsonStrMessage(str);
            }
          }
        };
        vm.ws.onclose = function()
        {
          vm.m_isConnectWS = false;
          var myimg = document.getElementById("video");
          myimg.src = "images/load1.gif";
          vm.StartWebSocket();
        };

        document.getElementById("capturetext").innerText = "拍照准备就绪";

      },
      sendWsMessage(jsonObj){
        var jsonStr = JSON.stringify(jsonObj);
        console.log(jsonStr);
        this.ws.send(jsonStr);
      },
      handleJsonStrMessage(str){
        if(str.indexOf("上传失败") != -1){
          document.getElementById("capturetext").innerText = "上传失败";
        }else if(str.indexOf("上传成功") != -1){
          document.getElementById("capturetext").innerText = "上传成功";
        }
        var jsonOBJ = JSON.parse(str);
        var name = jsonOBJ.FuncName;
        var re = jsonOBJ.result;
        var vm = this;
        //初始化
        if( name == "camInitCameraLib"){
          vm.openDev();
          //var jsonObj = {FuncName:'camInitFace',argument:{}};
          //this.sendWsMessage(jsonObj);
          //获取设备名
          var jsonObj = {FuncName:'camGetDevName'};
          vm.sendWsMessage(jsonObj);
          //获得麦克风
          jsonObj = {FuncName:'camGetAudioDevName'};
          vm.sendWsMessage(jsonObj);
          //获得录像格式
          jsonObj = {FuncName:'camGetVideoEncodeName'};
          vm.sendWsMessage(jsonObj);
        }
        //打开设备
        else if(name == "camOpenDev"){
          if(re == 0){
            //获取分辨率
            var jsonObj = {FuncName:'camGetResolution'};
            vm.sendWsMessage(jsonObj);
            //获取视频格式
            var jsonObj = {FuncName:'camGetMediaTypeName'};
            vm.sendWsMessage(jsonObj);
            jsonObj = {FuncName:'camSetImageAutoCrop',argument:{CropType:0}};
            vm.sendWsMessage(jsonObj);

            jsonObj = {FuncName:'camSetLivingBodyState',argument:{bOpen:1}};
            vm.sendWsMessage(jsonObj);
          }else{
            alert("打开失败" + re);
          }
        }
        //获取设备名
        else if(name == "camGetDevName"){
          vm.configureDevInfo(re);
        }
        //视频格式
        else if(name == "camGetMediaTypeName")
        {
          vm.configureVideoFormate(re);
          //configureVideoStyle(re);
        }
        //获取分辨率
        else if(name == "camGetResolution"){
          vm.configureRestionInfo(re);
        }
        //设置分辨率
        else if(name == "camSetResolution"){
          if(re !=0){
            alert("设置分辨率失败");
          }
        }
        //拍照
        else if(name == "camCaptureImageFile"){
          if(re != 0){
            alert("拍照失败");
          }
          else
          {
            vm.retCapture = re;
          }
        }
        //自动裁切
        else if(name == "camSetImageAutoCrop"){
          if(re != 0){
            alert("自动裁切失败");
          }
        }
        else if(name == "camZooIn"){
          if(re == 0){
            alert("视频放大成功");
          }
        }
        //Base64
        else if(name == "CaptureEncodeBase64"){
          alert(re);
        }
        //条码识别
        else if(name == "coderRecognizeBarcode"){
          alert(re);
        }
        //连拍
        else if(name == "camStartAutoCapture"){
          if(re == "0"){
            //alert("连拍开启成功");
            document.getElementById("capturemsg").innerText = "连拍开启成功!";
          }
        }
        //停止连拍
        else if(name == "camStopAutoCapture"){
          if(re == "0"){
            //alert("停止连拍成功");
            document.getElementById("capturemsg").innerText = "停止连拍成功!";
            vm.autoCaptureIndex = 1;
            vm.AutoCaptureBack("-1000");
          }

        }
        //连拍回调
        else if(name == "AutoCaptureBack"){
          //if(re == "0"){
          vm.AutoCaptureBack(re);
          //}else {
          //	AutoCaptureBack("-1000");
          //alert("连拍回调出错"+String(re));
          //}
        }
        //选择麦克风
        else if(name == "camGetAudioDevName"){
          vm.MicrophoneName(re);
          //console.log(re);
        }
        //录像格式
        else if (name == "camGetVideoEncodeName"){
          //console.log(re);
          vm.VideoName(re);
        }
        //开始录像
        else if(name == "camStartRecord"){
          if(re == 0) {
            alert("开始录像");
            vm.GetVoice();
          }
          else {
            alert("录像失败");
          }
        }
        //关闭录像
        else if(name == "camStopRecord"){
          if(re == 0) alert("录制成功");
          else alert("录制失败");
        }
        else if (name == "camGetMicrophoneVolumeLevel"){
          vm.ShowVioce(re);
        }
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
      },
      setCaptureTime(){
        this.AutoCaptureTime = captureTime.value;
      },
      HttpBatchUpload(filename){
        var vm = this;
        setTimeout(function(){
            var datap = {};
            datap.id = vm.filesdata.id;
            datap.materdataindex = vm.filesdata.materdataindex;
            datap.prolsh = vm.filesdata.prolsh;
            var filepath =  filename;
            var jsonObj = {FuncName:'camUpdataFileHttp',argument:{filePath:filepath, url:vm.upfileurl,param:""+JSON.stringify(datap),response:""}};
            vm.sendWsMessage(jsonObj);
          },2000);

      },

    }
  }
</script>

<style scoped>

</style>