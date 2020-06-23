package com.supermap.realestate_gx.registration.service;

import java.util.List;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.wisdombusiness.web.Message;


public abstract interface ConverterService
{
  public void ImportZRZ(HttpServletRequest request, HttpServletResponse response);
  public void UpdateZRZ(HttpServletRequest request, HttpServletResponse response);
  public void ImportHouse(HttpServletRequest request, HttpServletResponse response);
  public void UpdateHouse(HttpServletRequest request, HttpServletResponse response);
  public void ImportQL(HttpServletRequest request, HttpServletResponse response);
  public void UpdateQL(HttpServletRequest request, HttpServletResponse response);
  public void ImportZS(HttpServletRequest request, HttpServletResponse response);
  public void UpdateZS(HttpServletRequest request, HttpServletResponse response);
  public void ImportQLR(HttpServletRequest request, HttpServletResponse response);
  public void UpdateQLR(HttpServletRequest request, HttpServletResponse response);
  public void ImportQDZR(HttpServletRequest request, HttpServletResponse response);
  public void UpdateQDZR(HttpServletRequest request, HttpServletResponse response);
  public void CreateZDToFcMap(HttpServletRequest request, HttpServletResponse response);
  
}