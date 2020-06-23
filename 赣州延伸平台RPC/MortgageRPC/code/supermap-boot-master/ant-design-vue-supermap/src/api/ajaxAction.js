import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"

var xmlhttp = () =>{
  if (typeof XMLHttpRequest !== 'undefined') {
    return new XMLHttpRequest();
  }
  var versions = [
    "MSXML2.XmlHttp.6.0",
    "MSXML2.XmlHttp.5.0",
    "MSXML2.XmlHttp.4.0",
    "MSXML2.XmlHttp.3.0",
    "MSXML2.XmlHttp.2.0",
    "Microsoft.XmlHttp"
  ];
  var xhr;
  for (var i = 0; i < versions.length; i++) {
    try {
      xhr = new ActiveXObject(versions[i]);
      break;
    } catch (e) {
    }
  }
  return xhr;
};
var send = (url, method, data, success,fail,async) => {
  const token = Vue.ls.get(ACCESS_TOKEN);
  if (async === undefined) {
    async = true;
  }
  var x = xmlhttp();
  x.open(method, url, async);
  x.onreadystatechange = function () {
    if (x.readyState == 4) {
      var status = x.status;
      if (status >= 200 && status < 300) {
        var res = {};
        if(x.responseText) {
          res = JSON.parse(x.responseText);
          success && success(res);
        } else {
          success && success("暂无数据");
        }
      } else {
        fail && fail("请求失败，错误代码："+status);
      }
    }
  };
  if (method == 'POST') {
    x.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  }
  x.setRequestHeader('X-Access-Token', token);
  x.send(data)
};
var get = (url, data, callback, fail, async) => {
  var query = [];
  for (var key in data) {
    query.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
  }
  send(url + (query.length ? '?' + query.join('&') : ''), 'GET', null, callback, fail, async)
};
var post = (url, data, callback, fail, async) =>{
  var query = [];
  for (var key in data) {
    query.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
  }
  send(url,'POST', query.join('&'), callback, fail, async)
};

export default  {
  get,
  post
}
