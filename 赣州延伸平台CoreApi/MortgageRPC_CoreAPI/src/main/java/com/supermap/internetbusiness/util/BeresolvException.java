package com.supermap.internetbusiness.util;

import com.supermap.internetbusiness.model.XS24HZDBJ;
import com.supermap.wisdombusiness.workflow.service.common.Common;

import java.util.Date;

/**
 * 需要解决的异常，用于标识知道异常原因但需要人工解决的异常
 */
public class BeresolvException extends RuntimeException{

    XS24HZDBJ xs24HZDBJ;
    String proinstId;

    public BeresolvException() {
        super();
    }

    public BeresolvException(String message,String errorcode) {
        super(message);
        //不带实体类直接抛出的异常，赋值一条默认的日志记录错误原因
        XS24HZDBJ onlinelog = new XS24HZDBJ();
        onlinelog.setId(Common.CreatUUID());
        onlinelog.setZWLSH("isempty");
        onlinelog.setDQSJ(new Date());
        onlinelog.setSCDQSJ(new Date());
        onlinelog.setDQCS(1);
        onlinelog.setDQZT("1");
        onlinelog.setDJCODE("");
        onlinelog.setSFDB("0");
        onlinelog.setErrorCode(errorcode);
        this.xs24HZDBJ = onlinelog;
    }

    public BeresolvException(String message, XS24HZDBJ xs24HZDBJ) {
        super(message);
        this.xs24HZDBJ = xs24HZDBJ;
    }

    public BeresolvException(XS24HZDBJ xs24HZDBJ) {
        super(xs24HZDBJ.getDQSBYY());
        this.xs24HZDBJ = xs24HZDBJ;
    }

    // 用指定的详细信息和原因构造一个新的异常
    public BeresolvException(String message, Throwable cause){
        super(message,cause);
    }

    public BeresolvException(Throwable cause ,String proinstId ){
        super(cause);
        this.proinstId = proinstId;
    }


    //用指定原因构造一个新的异常
    public BeresolvException(Throwable cause) {
        super(cause);
    }

    public BeresolvException(XS24HZDBJ bjlog, String zt, String msg, String errorcode) {
        bjlog.setDQZT(zt);
        bjlog.setDQSBYY(msg);
        bjlog.setErrorCode(errorcode);
        this.xs24HZDBJ = bjlog;
    }

    public XS24HZDBJ getXs24HZDBJ() {
        return xs24HZDBJ;
    }

    public void setXs24HZDBJ(XS24HZDBJ xs24HZDBJ) {
        this.xs24HZDBJ = xs24HZDBJ;
    }

    public String getProinstId() {
        return proinstId;
    }

    public void setProinstId(String proinstId) {
        this.proinstId = proinstId;
    }
}
