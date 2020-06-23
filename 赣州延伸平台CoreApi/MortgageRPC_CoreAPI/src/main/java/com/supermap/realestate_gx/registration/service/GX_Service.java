package com.supermap.realestate_gx.registration.service;

import java.util.List;



import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.supermap.wisdombusiness.web.Message;


public abstract interface GX_Service
{
  public abstract Message GetFZList(String paramString);
  
  @SuppressWarnings("rawtypes")
  public List matchFcMisk(String id,String zl,String type);
  @SuppressWarnings("rawtypes")
  public  List GetRegisterZRZ(String id,String type);
  public  Object GetImportZRZ(String id,String type,HttpServletRequest request);
  public  Message SaveImportZRZ(String id,String paramString,HttpServletRequest request);
  public  Message insertZRZ(String id,HttpServletRequest request);
  public  List getDasjZRZ(String id,String zl,String type);
  public  List<Map<String,String>> readFileData(String id, HttpServletRequest request);
  public String updateZRZ(String id,String cqbs,HttpServletRequest request);
  public String update_bdck_ZRZ(String bdck_zrz,HttpServletRequest request);
  public String getTdStatu(String _bdcdyh);
  public  Message cfControl();
}