<template>

  <div>
    <table border="0" style = "margin-left:5%; margin-top:5%;" width="800" height="500">

      <tr style="float:left">
        <td id="video1">
          <img id="video" style="width:506px;height:380px" >
        </td>
        <td style="margin-left:5px;margin-top:10px;float:left">
          <label >设备</label>
          <select id="devicList" ></select>
        </td>
        <td style="margin-left:280px;margin-top:10px;">
          <label >分辨率</label>
          <select id="reslutionList" ></select>
        </td>
      </tr>

      <tr style="margin-left:560px">
        <td>
          <label >比对方式</label>
          <select id="compareType" style="width:150px" @onchange="SelectCompareType()">
            <option>身份证</option>
            <!--option>视频</option-->
          </select>
          <button type="button"  style="margin-left: 20px" @click="Capture()">采集</button>
        </td>
        <td  id="compareGroupImg" style="margin-top:10px;">
          <img style="width:200px;height:180px;background:black" id="comparePicImg">
          <img style="width:200px;height:180px;background:black" id="realPicImg">
        </td>
        <td>
          <button style="width:200px;height:180px;text-align:center;" @click="StartCompare()">开始比对</button>
          <input type="text" style="width:200px;height:180px;text-align:center;" disabled="disabled" value="比对分数" id="compareReslut"></input>
        </td>

      </tr>
    </table>
  </div>

</template>

<script>


  let IDCardFaceRecogin = {
    first:1,
    devIndex : 1,
    comparePath:"",
    compareBase64:"",
    previewDevIndex:0,
    realPicPath:"",
    encodeBase64Tag:0,
    //初始化UI
    InitSDKAndInitUI:function (obj){
      if (this.first == 1)
      {
        this.StartWebSocket();
        this.first = 0;
      }
    },
//反初始化
    UInitCamera:function (obj) {
      this.unload();
    },

    //比对方式发生了变化
    SelectCompareType:function()
    {
    },
    //采集
    Capture:function()
    {
      let selectIndex =document.getElementById("compareType").selectedIndex;
      IDCardFaceRecogin.ShowComparePic("",true);
      IDCardFaceRecogin.ShowCompareReslut("比对分数：",false);
      this.compareBase64 = "";
      this.comparePath = "";
      if (selectIndex == 0)
      {
        //采集身份证
        this.CaptureIDCard();
      }else if(selectIndex == 1){
        //文件
        this.CaptureFile();
      }
      //  this.compareBase64 = g_obj.EncodeBase64(0, this.comparePath);
    },
    //开始比对
    StartCompare:function()
    {
      var ret = -1;
      this.ShowRealPic("",true);
      if (this.compareBase64.length > 10)
      {
        this.realPicPath = this.g_savePicDir + "realPic.bmp";
        var jsonObj = {FuncName:'camMatchFaceByBase64Video',argument:{personFace:this.compareBase64,videoFilePath:this.realPicPath,ldelayTime:0}};
        this.sendWsMessage(jsonObj);
      }else
      {
        this.ShowCompareReslut("比对失败！",true);
      }
    },
    //采集文件
    CaptureFile:function()
    {
      if (!this.isIE()) {
        // alert("无法获取本地路径，请使用IE演示");
        this.comparePath = prompt("请输入路径：");
      }else{
        var inputObj=document.createElement('input')
        inputObj.setAttribute('id','_ef');
        inputObj.setAttribute('type','file');
        inputObj.setAttribute("style",'visibility:hidden');
        document.body.appendChild(inputObj);
        inputObj.click();
        this.comparePath = inputObj.value;
      }
      this.EncodeBase64(this.comparePath,0);
    },
    //转换Base64
    EncodeBase64:function(filePath,tag)
    {
      this.encodeBase64Tag = tag;
      var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:filePath}};
      this.sendWsMessage(jsonObj);
    },
    //采集身份证
    CaptureIDCard:function()
    {
      this.ReadIDCard();
    },
    //显示比对结果
    ShowCompareReslut:function(info,error)
    {
      let labe = document.getElementById("compareReslut");
      if(info.indexOf("比对分数") != -1){
        var data = parseInt(info.split("比对分数：")[1]);
        if(data > 50 && data < 70){
          labe.value = "核验通过";
        }else{
          labe.value = "核验不通过";
        }
      }
      labe.style.background = error ? "red" : "white";
    },

    //显示比对图片
    ShowComparePic:function(info,isBase64)
    {
      let im = document.getElementById("comparePicImg");
      if(im == null)
      {
        return;
      }
      if (info == "")
      {
        im.style.background = "white";
        // return;
      }

      if(isBase64)
      {
        im.src = "data:image/png;base64," + info;
      }else
      {
        im.src = "file://" + info;
      }

    },
    //显示现场图片
    ShowRealPic:function(info,isBase64)
    {
      let im = document.getElementById("realPicImg");
      if(im == null)
      {
        return;
      }
      if (info == "")
      {
        im.style.background = "white";
        // return;
      }
      if(isBase64)
      {
        im.src = "data:image/png;base64," + info;
      }else
      {
        im.src = "file:///" + info;
      }
    },
  }



  export default {
    name: "IDCardFaceRecoginModal",
    data() {
      return {
        title: "人脸核验",
        visible: false,
        ws: "",
        m_isConnectWS:false,
        m_splitTag : "$*$",
        m_closed : false, //是否被关闭了
        m_ComparePicBase64 : false,
        m_idcardBase64 : false,
        m_isIDCardHeadBs : false,
        m_compareShowID:"",
        retCapture:1,
        ocrFile:"",
        ocrType:0,
        g_savePicDir:"d:/tmp/",//文件存储位置
        g_obj:"",
        timer:0,
        videoNum:0,
        ResolutionNum:0,
        AutoCaptureTime:0,
        count:0,

      }
    },
    created() {
      this.load();
    },
    methods: {
      show(){
        this.visible = true;
      },
      load(){
        this.StartWebSocket();
      },
      unload(){
        if(this.m_closed){
          return;
        }
        //反初始化
        var jsonObj = {FuncName:'camUnInitCameraLib'};
        this.sendWsMessage(jsonObj);
      },
      configureUIPath(path)
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
      sendGetPath()
      {
        var jsonObj = {FuncName:'GetSpecialFolderPath',argument:{nFolder:5}};
        this.sendWsMessage(jsonObj);
      },
      sendPreZoneSize(){

        var w = document.getElementById("myCanvas").width;
        var h = document.getElementById("myCanvas").height;

        var jsonObj = {FuncName:'PreZoneSize',argument:{width:w,height:h}};
        this.sendWsMessage(jsonObj);

      },
      openDev(){
        if(this.m_closed){
          this.StartWebSocket();
        }
        //打开摄像头
        var jsonObj = {FuncName:'camOpenDev',argument:{devIndex:IDCardFaceRecogin.devIndex, subtype:0,width:0,height:0}};
        this.sendWsMessage(jsonObj);
        //开始预览
        jsonObj = {FuncName:'camStartPreview'};
        this.sendWsMessage(jsonObj);
      },
      addlist(obj,path)
      {
        var ul = document.getElementById(obj);
        var li = document.createElement("li");
        var img = document.createElement("img");
        img.setAttribute("width", "128");
        img.setAttribute("height", "96");
        img.setAttribute("id", "newImg");
        img.setAttribute("alt", "图片不存在");
        img.setAttribute("title", path);
        var vm = this;
        img.onclick = function(){vm.showpicture(path)};
        img.src = "file://" + path ;
        li.appendChild(img);
        ul.appendChild(li);
        //不知道有没有用
        clearTimeout(this.timer); ;
      },
      showpicture(path)
      {
        var jsonObj = {FuncName:'camShowImage',argument:{FilePath:path}};
        this.sendWsMessage(jsonObj);
      },
      DevSetting(){
        var objSelect = document.getElementById("device");
        var jsonObj = {FuncName:'camShowImageSettingWindow',argument:{devIndex:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      closeDev(){

        var objSelect = document.getElementById("device");
        var jsonObj = {FuncName:'camCloseDev',argument:{devIndex:objSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      configureDevInfo(names){
        var vm = this;
        //设备名字
        var objSelect = document.getElementById("devicList");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        objSelect[IDCardFaceRecogin.devIndex].selected = true;
        //设置设备
        objSelect.onchange = function(){
          //打开摄像头
          var jsonObj = {FuncName:'camOpenDev',argument:{devIndex:objSelect.selectedIndex, subtype:0,width:0,height:0}};
          vm.sendWsMessage(jsonObj);
        }
      },
      configureVideoFormate(names)
      {
        var vm = this;
        var objSelect = document.getElementById("videoStyle");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        //objSelect.Option.selected = videoNum;
        objSelect[this.videoNum].selected = true;
        objSelect.onchange = function(){
          //切换视频格式
          vm.closeDev();
          var objDev = document.getElementById("device");
          var jsonObj = {FuncName:'camOpenDev',argument:{devIndex:objDev.selectedIndex, subtype:objSelect.selectedIndex,width:0,height:0}};
          this.videoNum = objSelect.selectedIndex;//var jsonObj = {FuncName:'camOpenDev',argument:{index:objSelect.selectedIndex}};
          vm.sendWsMessage(jsonObj);
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
        //objSelect.Option.selected = ResolutionNum;
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
        if(objSelect.selectedIndex > 4)
        {
          SavePara();
        }
      },
      setImageAdjust(){
        var objectSelect = document.getElementById("imageAdjust");
        var jsonObj = {FuncName:'camSetImageAdjust',argument:{Type:objectSelect.selectedIndex}};
        this.sendWsMessage(jsonObj);
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
        let selectIndex =document.getElementById("compareType").selectedIndex;
        IDCardFaceRecogin.ShowComparePic("",true);
        IDCardFaceRecogin.ShowCompareReslut("比对分数：",false);
        IDCardFaceRecogin.compareBase64 = "";
        IDCardFaceRecogin.comparePath = "";
        if (selectIndex == 0)
        {
          //采集身份证
          this.CaptureIDCard();
        }else if(selectIndex == 1){
          //文件
          IDCardFaceRecogin.CaptureFile();
        }
        //  this.compareBase64 = g_obj.EncodeBase64(0, this.comparePath);
      },
      HttpUpload(type){

        if(type == 1)
        {
          var filepath = document.getElementById("UploadsaveText").value;
          var urlpath = document.getElementById("urlText").value;
          var jsonObj = {FuncName:'camUpdataFileHttp',argument:{filePath:filepath, url:urlpath,param:"param:123",response:""}};
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
      },
      ReadIDCard()
      {
        var jsonObj = {FuncName:'idcardrfidReadIDCardEx',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      showHead(){
        this.m_idcardBase64 = true;
        var filepath = document.getElementById("HeadPath").value;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      ReadBankCard(){
        var jsonObj = {FuncName:'ReadBankCard'};
        this.sendWsMessage(jsonObj);
      },
      ReadMagneticCard(){
        var jsonObj = {FuncName:'ReadMagneticCard'};
        this.sendWsMessage(jsonObj);
      },
      ReadSBKCard()
      {
        var jsonObj = {FuncName:'ReadSBKCaard'};
        this.sendWsMessage(jsonObj);
      },
      InitFingerData(){
        var jsonObj = {FuncName:'fingerprintInit',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      UinitFingerData(){
        var jsonObj = {FuncName:'fingerprintUnInit',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      GetFingerPic(num){
        var jsonObj = {FuncName:'CollectFingerFeature',argument:{number:num}};
        this.sendWsMessage(jsonObj);
      },
      ComperaFingerPic(){
        var jsonObj = {FuncName:'CompareFingerFeature',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      CaptureBase64()
      {
        var filepath = document.getElementById("saveText").value;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      CaptureBase64Ex()
      {
        //var Index = document.getElementById("resoultion").selectedIndex;
        var jsonObj = {FuncName:'CaptureEncodeBase64'};
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
      RemoveDictory()
      {
        var filepath = document.getElementById("Distory").value;
        var jsonObj = {FuncName:'DeleteFolderDayFileA',argument:{Dictpry:filepath}};
        this.sendWsMessage(jsonObj);
      },
      DPISet(){
        var xdpi = document.getElementById("dpix").value;
        var ydpi = document.getElementById("dpiy").value;
        var jsonObj = {FuncName:'camSetImageDPI',argument:{xDPI:parseInt(xdpi),yDPI:parseInt(ydpi)}};
        this.sendWsMessage(jsonObj);
      },
      JPGQSet()
      {
        var JPGQ = document.getElementById("jpg").value;
        var jsonObj = {FuncName:'camSetImageJPGQuanlity',argument:{quanlity:parseInt(JPGQ)}};
        this.sendWsMessage(jsonObj);
      },
      CropZoneSet(){
        var left = document.getElementById("left").value;
        var right = document.getElementById("right").value;
        var top = document.getElementById("top").value;
        var bottom = document.getElementById("bottom").value;
        var jsonObj = {FuncName:'camSetImageCusCropRect',argument:{left:parseInt(left),right:parseInt(right),top:parseInt(top),bottom:parseInt(bottom)}};
        this.sendWsMessage(jsonObj);
      },
      showBase64info(str)
      {
        alert("Base64数据为："+ str);
      },
      continuCapture(){
        var filepath = document.getElementById("saveText").value + "\\autoCapture.jpg";
        var jsonObj = {FuncName:'camStartAutoCapture',argument:{type:0,param:4,filePath:filepath}};
        this.AutoCaptureTime = 4;
        this.sendWsMessage(jsonObj);
      },
      timeCapture(){
        var filepath = document.getElementById("saveText").value + "\\autoCapture.jpg";
        var jsonObj = {FuncName:'camStartAutoCapture',argument:{type:1,param:4,filePath:filepath}};
        this.AutoCaptureTime = 4;
        this.sendWsMessage(jsonObj);
      },
      StopAutoCapture(){
        var jsonObj = {FuncName:'camStopAutoCapture',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      AutoCaptureBack(re){
        var progress0 = document.getElementById("autoCaptureProgress");
        if(re == "-1000"){
          progress0.value = "0";
        }else {
          progress0.value = "100";
          setTimeout(function(){
            progress0.value = "0";
          },this.AutoCaptureTime/2 *1000);
        }
      },
      InitFaceSDK(){
        var jsonObj = {FuncName:'camInitFace',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      UinitFaceSDK(){
        var jsonObj = {FuncName:'camUnInitFace',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      compareVideo(){
        var url1 = document.getElementById("imgOne").value;
        var jsonObj = {FuncName:'camMatchFaceByFileVideo',argument:{filePath:url1,videoFilePath:"",ldelayTime:0}};
        this.sendWsMessage(jsonObj);
      },
      compareAsyVideo()
      {
        this.m_ComparePicBase64 = true;
        var url1 = document.getElementById("imgOne").value;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:url1}};
        this.sendWsMessage(jsonObj);
      },
      compareAsyBase64Video(base64Str)
      {
        var jsonObj = {FuncName:'camStartAsyMatchFaceBase64Video',argument:{personFace:base64Str,
            videoFilePath:"",
            score:65,
            ldelayTime:10000}};
        this.sendWsMessage(jsonObj);
      },
      comparePic()
      {
        var url1 = document.getElementById("imgOne").value;
        var url2 = document.getElementById("imgTwo").value;
        var jsonObj = {FuncName:'camMatchFaceByFile',argument:{filePath1:url1,filePath2:url2}};
        this.sendWsMessage(jsonObj);
      },
      FaceCropChanged()
      {
        var num = document.getElementById("FaceCrop").checked ? 5 : 0;
        var jsonObj = {FuncName:'camSetImageAutoCrop',argument:{CropType:num}};
        this.sendWsMessage(jsonObj);
      },
      LiveBodyChanged()
      {
        var num = document.getElementById("LiveBody").checked ? 1 : 0;
        var jsonObj = {FuncName:'camSetLivingBodyState',argument:{bOpen:num}};
        this.sendWsMessage(jsonObj);
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
        var objSelect = document.getElementById("device");
        var CurMicphone = document.getElementById("Microphone");
        var CurVideoFormat = document.getElementById("VideoType");
        //console.log(savepath,CurMicphone.selectedIndex,CurVideoFormat.selectedIndex);
        var jsonObj = {FuncName:'camStartRecord',argument:{filePath:savepath,micphone:CurMicphone.selectedIndex,videoFormat:CurVideoFormat.selectedIndex}};
        this.sendWsMessage(jsonObj);
      },
      StopVideo(){
        var objSelect = document.getElementById("device");
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
      AddWaterMark()
      {
        var wm_Msg = document.getElementById("WaterMarkMsg").value;
        var wm_type = document.getElementById("WaterMarktype").value;
        var wm_fontSize = document.getElementById("WaterMarkfontSize").value;
        var wm_font = document.getElementById("WaterMarkfont").value;
        var wm_fItalic = document.getElementById("WaterMarkfItalic").value;
        var wm_fUnderline = document.getElementById("WaterMarkfUnderline").value;
        var wm_fWeight = document.getElementById("WaterMarkfWeight").value;
        var wm_angle = document.getElementById("WaterMarkangle").value;
        var wm_transparent = document.getElementById("WaterMarktransparent").value;
        var wm_R = document.getElementById("WaterMarkcolorR").value;
        var wm_G = document.getElementById("WaterMarkcolorG").value;
        var wm_B = document.getElementById("WaterMarkcolorB").value;
        var wm_isAvailabel = document.getElementById("WaterMarkisAvailabel").value;
        var jsonObj = {FuncName:'camSetImageWaterMark',argument:{strMsg:wm_Msg,itype:parseInt(wm_type),ifontSize:parseInt(wm_fontSize),strfont:wm_font,iItalic:parseInt(wm_fItalic),iUnderline:parseInt(wm_fUnderline),iWeight:parseInt(wm_fWeight),fangle:parseFloat(wm_angle),ftransparent:parseFloat(wm_transparent),icolorR:parseInt(wm_R),icolorG:parseInt(wm_G),icolorB:parseInt(wm_B),isAvailabel:parseInt(wm_isAvailabel),
          }};
        this.sendWsMessage(jsonObj);
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
      coderRecBarcode()
      {
        var BarcodePath = document.getElementById("OCR_Pathtext").value;
        var jsonObj = {FuncName:'coderRecognizeBarcode',argument:{strBarcodePath:BarcodePath}};
        this.sendWsMessage(jsonObj);
      },
      RecBarcode()
      {
        var BarcodePath = document.getElementById("OCR_Pathtext").value;
        var nCodeType = codetype.options[codetype.selectedIndex].value;
        var img = new Image();
        // 改变图片的src
        img.src = BarcodePath;
        // 加载完成执行
        img.onload = function(){
          // 打印
          var w,h;
          w = img.width;
          h = img.height;
          var jsonObj ;
          if(nCodeType == 1)
          {
            jsonObj = {FuncName:'ocrRecognizeBarcodeRect',argument:{strBarcodePath:BarcodePath,nCodeType:parseInt(nCodeType),ileft:parseInt(w/2),itop:0,iright:parseInt(w),ibottom:parseInt(h/2)}};
          }else if(nCodeType == 2){
            jsonObj = {FuncName:'ocrRecognizeBarcodeRect',argument:{strBarcodePath:BarcodePath,nCodeType:parseInt(nCodeType),ileft:0,itop:0,iright:parseInt(w/2),ibottom:parseInt(h/2)}};
          }
          this.sendWsMessage(jsonObj);
        }

      },
      ShowOCRResult(msg)
      {
        var myDate = new Date();
        var Ret = "\r\n" + myDate.toLocaleString()+ "\r\n";
        Ret += msg;
        Msg.value = Ret + Msg.value;
      },
      AddLanguage(names)
      {
        //设备名字
        var objSelect = document.getElementById("language");
        objSelect.options.length = 0;
        for (var i = 0; i < names.length;i++ ) {
          var op = new Option(names[i],i);
          objSelect.options[objSelect.length] = op;
        }
        objSelect.options[102].selected = true;
      },
      RecToFile(type)
      {
        var fileext = document.getElementById("filetype").value;
        var strFolder = document.getElementById("OCR_SavePathtext").value;
        var myDate = new Date();
        var myName = "OCR_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();

        this.ocrFile = strFolder + "\\" + myName + fileext ;

        var languageID = language.options[language.selectedIndex].value;
        if(type == 1)
        {
          var jsonObj = {FuncName:'ocrCombineSingleToFile',argument:{strImagePath:OCR_Pathtext.value,strFilePath:this.ocrFile,language:parseInt(languageID)}};
          this.sendWsMessage(jsonObj);
        }
        else if(type == 2)
        {
          var jsonObj = {FuncName:'ocrCombineSingleToString',argument:{strImagePath:OCR_Pathtext.value,language:parseInt(languageID)}};
          this.sendWsMessage(jsonObj);
        }
      },
      InitOCR()
      {
        //识别语言
        var jsonObj = {FuncName:'ocrGetLanguageName'};
        this.sendWsMessage(jsonObj);
      },
      InitSign()
      {
        var left = 680;
        var right = 2;
        var ntop = 440;
        var bottom = 2;
        var BR=255 ;
        var BG=255 ;
        var BB=255 ;
        var BT=255 ;
        var R=0 ;
        var G=0 ;
        var B=0 ;
        var jsonObj = {FuncName:'StartSignDevice',argument:{left:parseInt(left),right:parseInt(right),ntop:parseInt(ntop),bottom:parseInt(bottom),BR:parseInt(BR),BG:parseInt(BG),BB:parseInt(BB),BT:parseInt(BT),R:parseInt(R),G:parseInt(G),B:parseInt(B)}};
        this.sendWsMessage(jsonObj);
      },
      UinitSign()
      {

        var jsonObj = {FuncName:'StopSignMode'};
        this.sendWsMessage(jsonObj);
      },
      BeginSign()
      {

        var jsonObj = {FuncName:'BeginSignMode'};
        this.sendWsMessage(jsonObj);

      },
      GetSign()
      {
        var strFolder = document.getElementById("OCR_SavePathtext").value;
        var myDate = new Date();
        var myName = "OCR_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();

        var filePath = strFolder + "\\" + myName + "。jpg" ;
        var jsonObj = {FuncName:'CmOutputImageBase64',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      EndSign()
      {
        var jsonObj = {FuncName:'EndSignMode'};
        this.sendWsMessage(jsonObj);
      },
      OcrAction(strResult)
      {

        var myDate = new Date();

        Msg.value =  "\r\n" + myDate.toLocaleString() + "\r\n" + "- 识别结果：" +  strResult + "\r\n" + Msg.value;

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

        vm.ws.onopen = function()
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
              var strs= new Array();
              strs=str.split(vm.m_splitTag);
              var baseStr = strs[2];
              if(IDCardFaceRecogin.encodeBase64Tag == 1){
                IDCardFaceRecogin.ShowRealPic(baseStr,true);
                var jsonObj = {FuncName:'camDeleteFile',argument:{FilePath:IDCardFaceRecogin.realPicPath}};
                vm.sendWsMessage(jsonObj);
              }else if(IDCardFaceRecogin.encodeBase64Tag == 0){
                IDCardFaceRecogin.ShowComparePic(baseStr,true);
                IDCardFaceRecogin.compareBase64 = baseStr;
              }
              return;
            }
            if(str.indexOf(vm.m_splitTag)>=0){
              //视频的每一帧
              var strs= new Array();
              strs=str.split(vm.m_splitTag);
              vm.setImageWithBase64(strs[1]);
            }else{
              //处理其他请求
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

      },
      sendWsMessage(jsonObj){
        var jsonStr = JSON.stringify(jsonObj);
        this.ws.send(jsonStr);
      },
      handleJsonStrMessage(str){
        var jsonOBJ = JSON.parse(str);
        var name = jsonOBJ.FuncName;
        var re = jsonOBJ.result;
        //初始化
        if( name == "camInitCameraLib"){
          //openDev();
          var jsonObj = {FuncName:'camInitFace',argument:{}};
          this.sendWsMessage(jsonObj);
          jsonObj = {FuncName:'camGetDevCount',argument:{}};
          this.sendWsMessage(jsonObj);
          //获取设备名
          jsonObj = {FuncName:'camGetDevName'};
          this.sendWsMessage(jsonObj);
        }
        //打开设备
        else if(name == "camOpenDev"){

          if(re == 0){

            //获取分辨率
            var jsonObj = {FuncName:'camGetResolution'};
            this.sendWsMessage(jsonObj);

            jsonObj = {FuncName:'camSetImageAutoCrop',argument:{CropType:5}};
            this.sendWsMessage(jsonObj);

            jsonObj = {FuncName:'camSetLivingBodyState',argument:{bOpen:1}};
            this.sendWsMessage(jsonObj);

          }else{
            alert("打开失败" + re);
          }

        }
        //获取设备名
        else if(name == "camGetDevName"){

          this.configureDevInfo(re);

        }
        //设备个数
        else if(name == "camGetDevCount")
        {

          if(re <= 1)
          {
            IDCardFaceRecogin.devIndex = 0;
          }
          else{
            IDCardFaceRecogin.devIndex = 0;
          }
          this.openDev();
        }

        //获取分辨率
        else if(name == "camGetResolution"){

          this.configureRestionInfo(re);
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
            this.retCapture = re;

          }


        }
        //自动裁切
        else if(name == "camSetImageAutoCrop"){
          if(re != 0){

            alert("自动裁切失败");
          }
        }


        //初始化人脸识别模块
        else if(name == "camInitFace"){

          if(re == "0"){
            //alert("初始化人脸识别成功");
          }else {
            alert("初始化人脸识别失败");
          }

        }
        //反初始化人脸识别模块
        else if(name == "camUnInitFace"){

          alert(re);
        }
//比对视频
        else if(name == "camMatchFaceByFileVideo"){
          alert(re);

        }
        //异步base64比对
        else if(name == "camStartAsyMatchFaceBase64Video")
        {
          alert(re);
        }

        //匹配图片
        else if(name == "camMatchFaceByFile") {

          var score = parseInt(re);
          if(score>=0 && score<=100){

            alert("分数：" + re);
          }else{

            alert("error：" + re);
          }

        }


        else if (name == "idcardrfidReadIDCardEx")
        {
          //alert(re);
          var strs= new Array();
          strs=re.split("|");
          IDCardFaceRecogin.compareBase64 = strs[9];
          IDCardFaceRecogin.ShowComparePic(IDCardFaceRecogin.compareBase64,true);

        }
        else if(name == "camMatchFaceByBase64Video")
        {
          this.EncodeBase64(IDCardFaceRecogin.realPicPath,1);

          if (re > 100 || re < 0) {

            IDCardFaceRecogin.ShowCompareReslut(re == 10014 ? "非活体":"比对失败ErrorCode：" + re,true);

          }else {

            IDCardFaceRecogin.ShowCompareReslut("比对分数：" + re,false);
          }

        }

      },
      isIE() {
        if (!!window.ActiveXObject || "ActiveXObject" in window)
          return true;
        else
          return false;
      },
      sendInitMsg(){
        var jsonObj = {FuncName:'camInitCameraLib'};
        this.sendWsMessage(jsonObj);
      },
      CaptureIDCard()
      {
        this.ReadIDCard();
      },
      //开始比对
      StartCompare()
      {
        var ret = -1;
        var vm = this;
        this.ShowRealPic("",true);
        if (IDCardFaceRecogin.compareBase64.length > 10)
        {
          IDCardFaceRecogin.realPicPath = this.g_savePicDir + "realPic.bmp";
          var jsonObj = {FuncName:'camMatchFaceByBase64Video',argument:{personFace:IDCardFaceRecogin.compareBase64,videoFilePath:IDCardFaceRecogin.realPicPath,ldelayTime:0}};
          vm.sendWsMessage(jsonObj);
        }else
        {
          this.ShowCompareReslut("比对失败！",true);
        }
      },
      //显示现场图片
      ShowRealPic(info,isBase64)
      {
        let im = document.getElementById("realPicImg");
        if(im == null)
        {
          return;
        }
        if (info == "")
        {
          im.style.background = "white";
          // return;
        }
        if(isBase64)
        {
          im.src = "data:image/png;base64," + info;
        }else
        {
          im.src = "file:///" + info;
        }
      },
      EncodeBase64(filePath,tag)
      {
        IDCardFaceRecogin.encodeBase64Tag = tag;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:filePath}};
        this.sendWsMessage(jsonObj);
      },







    }
  }
</script>

<style scoped>

</style>