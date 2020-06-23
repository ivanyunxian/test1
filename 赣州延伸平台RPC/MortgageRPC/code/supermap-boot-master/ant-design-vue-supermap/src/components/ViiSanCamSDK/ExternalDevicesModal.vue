<template>
 <!-- <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @ok="modelclose"
    @cancel="modelclose"
    okText="完成"
    cancelText="关闭"> -->
<div>
    <table border="0" style = "margin-top:5%;width: 50%;" width="800" height="250">
      <tr height="20" style="display: none">
        <td colspan="3" >
          存储路径：
          <input style="margin-left:50px; width:280px" type = "text" name="path" id="path" value="D:\tmp" onchange="SetSaveFolder()" >
        </td>
      </tr>


      <tr height="20">
        <td colspan="4">
          身份证读卡器
        </td>
      </tr>
      <tr width="80%" height="150" >
        <td colspan="2" >
          <textarea id="idMsg" style="width:100%;height:100%;">
          </textarea>
        </td>
        <td width="150" height="150">
          <img id="imgPreview" border="1" src="" width="100%" height="100%"/>
        </td>
        <td width="100" style="display:none;">
			    <textarea id="IDCardFP" style="width:100%;height:100%;">
          </textarea>
        </td>
      </tr>
      <tr height="50" >
        <td>
          <input type = "button" value = "读卡" @click="ReadIDCard()" style = "width:100px; text-align:center">
          <input type = "button" value = "获取指纹" @click="ReadIDCardFingerprint()" style = "width:100px; text-align:center;display: none">
          <input type = "button" value = "读取银行卡" @click="ReadBank()" style = "width:100px; text-align:center;display: none">
          <input type = "button" value = "读取社保卡" @click="ReadSse()" style = "width:100px; text-align:center;display: none">

          <label style="display:none">身份证合并类型</label>
          <select id = "CombineType" style="display:none">
            <option value = "0">不合并</option>
            <option value = "1">左右合并</option>
            <option value = "2">上下合并</option>
            <option value = "3">上下合并有空隙</option>
          </select>
          <input type = "button" value = "身份证正反面合并" @click="CombineIDCardImage()" style = "width:150px; text-align:center;display:none">
          <img id ="CombinePic" style="display:none" width = "200px" height="180px"/>
          <img id ="CombinePic1" style="display:none" width = "200px" height="180px"/>
        </td>
      </tr>

      <tr height="20"  style="display: none">
        <td colspan="4">
          指纹采集器
        </td>
      </tr>

      <tr  style="display: none">
        <td width="200">
          <table>
            <tr>
              <td>
                指纹1
              </td>
            </tr>
            <tr>
              <td>
                <img ID="fingerprint1" src="" border="1" width="100" height="100"/>
              </td>
            </tr>
            <tr>
              <td>
                <input type = "button" value = "采集" @click="GetFingerprint(1)" style = "width:100px; text-align:center">
              </td>
            </tr>
          </table>
        </td>
        <td width="200">
          <table>
            <tr>
              <td>
                指纹2
              </td>
            </tr>
            <tr>
              <td>
                <img ID="fingerprint2" src="" border="1" width="100" height="100"/>
              </td>
            </tr>
            <tr>
              <td>
                <input type = "button" value = "采集" @click="GetFingerprint(2)" style = "width:100px; text-align:center">
              </td>
            </tr>
          </table>
        </td>
        <td colspan="2">
          <table>
            <tr><td>
              <input type = "button" value = "指纹1与2对比" @click="Contrast(1)" style = "width:200px; text-align:center">
            </td></tr>
            <tr><td>
              <input type = "button" value = "base64指纹1与2对比" @click="Contrast(2)" style = "width:200px; text-align:center">
            </td></tr>
            <tr><td>
              <input type = "button" value = "身份证指纹与指纹1对比" @click="Contrast(3)" style = "width:200px; text-align:center">
            </td></tr>
            <tr><td>
              <input type = "button" value = "Base64身份证指纹与指纹1对比" @click="Contrast(4)" style = "width:200px; text-align:center">
            </td></tr>
          </table>
        </td>
      </tr>
    </table>
</div>
<!--  </a-modal> -->
</template>

<script>


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
        this.StartWebSocket();
        this.first = 0;
      }
    },
//反初始化
    UInitCamera:function (obj) {
      this.unload();
    },
  }


  export default {
    name: "ExternalDevicesModal",
    data() {
      return {
        title: "信息採集",
        visible: false,
        confirmLoading:false,
        ws: "",
        m_isConnectWS:false,
        m_splitTag : "$*$",
        m_closed : false, //是否被关闭了
        m_isShowFileFront : false,
        m_isShowFileBack : false,
        m_isIDCardHeadBs : false,
        m_strFileFront:"",
        m_strFileBack:"",
        m_fingerIndex:"",
        m_finger1Bmp:"",
        m_finger1File:"",
        m_finger1Base64:"",
        m_finger2Bmp:"",
        m_finger2File:"",
        m_finger2Base64:"",
        m_IDcardFPFile:"",
        m_IDcardFPBase64:"",
        m_IDcardHeaderPic:"",

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
      sendInitMsg(){

        var jsonObj = {FuncName:'camInitCameraLib'};
        this.sendWsMessage(jsonObj);
      },
      showpicture(path)
      {
        var jsonObj = {FuncName:'camShowImage',argument:{FilePath:path}};
        this.sendWsMessage(jsonObj);
      },
      GetIDCardImage(typeNum,strFileFront,strFileBack)
      {

        var jsonObj = {FuncName:'idcardrfidGetIDCardImage',argument:{pFileFront:strFileFront,pFileBack:strFileBack,nType:typeNum}};
        this.sendWsMessage(jsonObj);
      },
      GetIDCardOneFingerData(filePath)
      {
        var jsonObj = {FuncName:'idcardrfidGetOneFingerprintMsg',argument:{nIndex:0,filePath:filePath}};
        this.sendWsMessage(jsonObj);
      },
      showIDcardFingerInfo(bs64,nType)
      {
        /*获取指纹指拇类型*/

        if((nType >= 11 && nType <= 20) || (nType >= 97 && nType <= 99))
        {
          alert("指姆类型："+nType+" 11 右手拇指，12 右手食指，13 右手中指 ，14 右手环指 ，15 右手小指，16 左手拇指 ，17 左手食指 ，18 左手中指 ，19 左手环指 ，20 左手小指 ，97 右手不确定指位 ，98 左手不确定指位，99 其他不确定指位");
        }
        this.m_IDcardFPBase64 = bs64;
        IDCardFP.value = this.m_IDcardFPBase64;

      },
      ReadIDCardPath(filepath)
      {
        var jsonObj = {FuncName:'idcardrfidReadIDCard',argument:{HeadPath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      ReadIDCardEx()
      {
        var jsonObj = {FuncName:'idcardrfidReadIDCardEx',argument:{}};
        this.sendWsMessage(jsonObj);
      },
      showIDCardImageFront(){

        this.m_isShowFileFront = true;
        this.m_isShowFileBack = false;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:this.m_strFileFront}};
        this.sendWsMessage(jsonObj);


        CombinePic.style.display="inline";
        CombinePic1.style.display="none";
      },
      showIDCardImageBack()
      {
        var nType = document.getElementById("CombineType").value;
        if(nType== 0)
        {
          this.m_isShowFileBack = true;
          CombinePic1.style.display="inline";
          var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:this.m_strFileBack}};
          this.sendWsMessage(jsonObj);
        }
      },
      showFingerPic(bs,picBs)
      {

        if(this.m_fingerIndex == 1)
        {
          this.m_finger1Base64 = picBs;
          fingerprint1.src = "data:image/png;base64,"+bs;

        }else{
          this.m_finger2Base64 = picBs;
          fingerprint2.src = "data:image/png;base64,"+bs;
        }

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
      GetFingerPicFromeMsg(bmpFilePath,FeatureFilePath){

        var jsonObj = {FuncName:'GetFingerprint',argument:{bmpPath:bmpFilePath,FeaturePath:FeatureFilePath}};
        this.sendWsMessage(jsonObj);

      },
      ComperaFingerPic(file1,file2){

        var jsonObj = {FuncName:'ContrastFingerprint',argument:{FeaturePath1:file1,FeaturePath2:file2}};
        this.sendWsMessage(jsonObj);

      },
      ContrastFingerprintBase64(bs1,bs2)
      {

        var jsonObj = {FuncName:'ContrastFingerprintBase64',argument:{Base64FPFeature1:bs1,Base64FPFeature2:bs2}};
        this.sendWsMessage(jsonObj);
      },
      CaptureBase64()
      {
        var filepath = document.getElementById("saveText").value;
        var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:filepath}};
        this.sendWsMessage(jsonObj);
      },
      showBase64info(str)
      {
        alert("Base64数据为："+ str);
      }, StartWebSocket(){
        var url = "ws://localhost:9000/";
        var vm = this;
        if('WebSocket' in window){
          this.ws = new WebSocket(url);
        }
        else if('MozWebSocket' in window){
          this.ws = new MozWebSocket(url);
        }else{
          alert("浏览器版本过低，请升级您的浏览器。\r\n浏览器要求：IE10+/Chrome14+/FireFox7+/Opera11+");
        }

        this.ws.onopen = function()
        {
          vm.m_isConnectWS = true;
          vm.unload();
          vm.sendInitMsg();//初始化
          vm.m_closed = false;
        };

        this.ws.onmessage = function (evt)
        {
          if(typeof(evt.data)=="string"){
            var str = evt.data;
            if(str.length <= 0){
              return;
            }
            if(str.indexOf("FileEncodeBase64") >=0){
              var strs= new Array();
              strs=str.split(vm.m_splitTag);
              var bas = strs[2];
              if(this.m_isShowFileFront)
              {
                CombinePic.src = "data:image/png;base64,"+bas;
                vm.m_isShowFileFront = false;
                vm.showIDCardImageBack();
              }
              else if(vm.m_isShowFileBack)
              {
                CombinePic1.src = "data:image/png;base64,"+bas;
                vm.m_isShowFileBack = false;
              }
              else if(vm.m_isIDCardHeadBs)
              {
                imgPreview.src = "data:image/png;base64,"+bas;;
                this.m_isIDCardHeadBs = false;
              }
              return;
            }
            else if(str.indexOf("GetFingerprint") >=0)
            {
              //采集指纹
              var strs= new Array();
              strs=str.split(vm.m_splitTag);
              var bas = strs[2];
              if(bas.length < 10)
              {
                alert("采集失败");
                return;
              }
              var picBs = strs[3];
              vm.showFingerPic(bas,picBs);
              return;
            }
            else if(str.indexOf("idcardrfidGetOneFingerprintMsg") >=0)
            {
              //身份证指纹
              var strs= new Array();
              strs=str.split(vm.m_splitTag);
              var bas = strs[2];
              if(bas.length < 10)
              {
                alert("无指纹数据");
                return;
              }
              var ntype = strs[3];
              vm.showIDcardFingerInfo(bas,ntype);
            }
            if(str.indexOf(vm.m_splitTag)>=0){

            }else{
              //处理其他请求
              console.log(str);
              vm.handleJsonStrMessage(str);
            }
          }
        };
        this.ws.onclose = function()
        {
          vm.m_isConnectWS = false;
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
          if (re == 0){
          }else{
            //alert("初始化失败" + re);
          }
        }
        //二代证
        else if(name == "idcardrfidReadIDCard"){
          idMsg.value = re;
          this.m_isIDCardHeadBs = true;
          var jsonObj = {FuncName:'FileEncodeBase64',argument:{filePath:this.m_IDcardHeaderPic}};
          this.sendWsMessage(jsonObj);

        }
        else if (name == "idcardrfidReadIDCardEx")
        {
          idMsg.value = re;
          var strs=re.split("|");
          imgPreview.src = "data:image/png;base64,"+strs[9];
        }
        else if(name == "idcardrfidGetIDCardImage")
        {
          if(re == 0)
          {
            showIDCardImageFront();
          }
        }
        //社保卡
        else if(name == "ReadSBKCaard"){

          alert(re);
        }

        //读取的银行卡
        else if(name == "ReadBankCard"){

          alert(re);
        }
        //读取的磁条卡
        else if(name == "ReadMagneticCard"){
          alert(re);
        }
        //初始化指纹仪
        else if(name == "fingerprintInit"){
          if (re == "0"){

            alert("初始化指纹仪成功");

          }else{

            alert("初始化指纹仪失败");
          }

        }
        //反初始化指纹仪
        else if(name == "fingerprintUnInit"){
          if (re == "0"){

            alert("反初始化指纹仪成功");

          }else{

            alert("反初始化指纹仪失败");
          }
        }

        //比对指纹
        else if(name == "ContrastFingerprint"){

          if(re == "-100"){

            alert("请先采集指纹");

          }else if(re == "0"){

            alert("指纹匹配成功");
          }else {

            alert("匹配失败，错误码"+re);
          }

        }
        else if(name == "ContrastFingerprintBase64")
        {
          if(re == "-100"){

            alert("请先采集指纹");

          }else if(re == "0"){

            alert("指纹匹配成功");
          }else {

            alert("匹配失败，错误码"+re);
          }
        }
      },SetSaveFolder()
      {
        saveFolder.href = path.value;
      },
      ReadIDCard()
      {

        var myDate = new Date();
        var myName = "IDcardHeaderPic"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
        this.m_IDcardHeaderPic = path.value + "\\" + myName + ".bmp" ;

        this.ReadIDCardPath(this.m_IDcardHeaderPic);
        // var carddata = "1231";
        // //idMsg
        // setTimeout(function(){
        //   carddata = document.getElementById("idMsg").value;
        //   //var headbase = document.getElementById("imgPreview").src;
        //   alert(carddata);
        //   //alert(headbase);
        // },1500);

        // return carddata;
      },
      ReadBank()
      {
        this.ReadBankCard();
      },
      ReadSse()
      {
        this.ReadSBKCard();
      },
      CombineIDCardImage()
      {

        var m_width ,m_height;
        var myDate = new Date();
        var myNameF = "Front"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
        var myNameB = "Back"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
        var nType = document.getElementById("CombineType").value;
        this.m_strFileFront = path.value + "\\" + myNameF + ".jpg";
        this.m_strFileBack = path.value + "\\" + myNameB +".jpg";


        GetIDCardImage(nType,this.m_strFileFront,this.m_strFileBack);



      },
      ReadIDCardFingerprint()
      {
        this.m_IDcardFPBase64 = "";
        var myDate = new Date();
        var myName = "IDcardFPFile_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
        this.m_IDcardFPFile = path.value + "\\" + myName + ".bin" ;
        GetIDCardOneFingerData(this.m_IDcardFPFile);

      },
      UnInitIDCardRFID()
      {
        CamSDKOCX.UnInitIDCardRFID();
      },
      GetFingerprint(index)
      {

        var myDate = new Date();
        var myName = "_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
        this.m_fingerIndex = index;
        if(index == 1)
        {
          this.m_finger1Base64 = "";
          this.m_finger1Bmp = path.value + "\\finger1" + myName + ".bmp" ;
          this.m_finger1File = path.value + "\\finger1" + myName + ".bin" ;

          GetFingerPicFromeMsg(this.m_finger1Bmp,this.m_finger1File);

        }
        else if(index == 2)
        {
          this.m_finger2Base64 = "";
          this.m_finger2Bmp = path.value + "\\finger2" + myName + ".bmp" ;
          this.m_finger2File = path.value + "\\finger2" + myName + ".bin" ;
          GetFingerPicFromeMsg(this.m_finger2Bmp,this.m_finger2File);

        }
      },
      Contrast(n)
      {
        var ret = -1;

        if(1 == n)
        {

          ComperaFingerPic(this.m_finger1File,this.m_finger2File);
        }
        else if(2 == n)
        {

          ContrastFingerprintBase64(this.m_finger1Base64,this.m_finger2Base64);
        }
        else if(3 == n)
        {
          //alert(IDcardFPFile+"\n"+finger1File);
          ComperaFingerPic(this.m_IDcardFPFile,this.m_finger1File);
        }
        else if(4 == n)
        {
          //alert(IDcardFPBase64+"\n"+finger1Base64);
          ContrastFingerprintBase64(this.m_IDcardFPBase64,this.m_finger1Base64);
        }
        else
        {
          alert("错误，Contrast("+n+")没有可执行代码");
          return;
        }

      },
      UnInitFingerprint()
      {
        if(IsInitFingerprint == 1)
        {
          CamSDKOCX.UnInitFingerprint();
          IsInitFingerprint = 0;
        }
      },
      modelclose () {
        this.closeDev();
        this.$emit('close');
        this.visible = false;
      },



    }
  }
</script>

<style scoped>

</style>