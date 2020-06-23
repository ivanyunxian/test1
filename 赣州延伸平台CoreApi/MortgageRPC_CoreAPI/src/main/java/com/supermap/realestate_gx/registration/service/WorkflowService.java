package com.supermap.realestate_gx.registration.service;

import java.io.UnsupportedEncodingException;
import java.util.List;






import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;

import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;


public abstract interface WorkflowService
{
  public Map acceptprojectdetails(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
  public Message search(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
  public Message guilinsearch(String pageindex,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
  public Message acceptproject(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception;
  public Map getDYQInfo(String xmbh) throws  Exception;
  public List<Map> modeldetalis(String ywlx, String xmbh, String modify);
  public Page fjylpage(String pagein, String id, String ywlxid,int count);
  public void showImg(String id,HttpServletResponse response);
  public Message fjbh(String xmbh,String bhly);

  
}