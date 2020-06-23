package com.supermap.intelligent.service;

import com.supermap.intelligent.util.ResultData;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface IntelligentCoreService {

    ResultData declare(HttpServletRequest req) throws UnsupportedEncodingException;

	ResultData enterpriseDeclare(HttpServletRequest req) throws UnsupportedEncodingException;

}
