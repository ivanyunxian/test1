package com.supermap.internetbusiness.util;

import com.supermap.internetbusiness.model.XS24HZDBJ;

/**
 * 登簿后出现的异常，此时已完成登簿，单独捕获此异常，否则将把登簿的项目删除
 */
public class AfterBoardException extends RuntimeException{

    XS24HZDBJ xs24HZDBJ;
    String proinstId;

    public AfterBoardException() {
        super();
    }

    public AfterBoardException(String message) {
        super(message);
    }

    public AfterBoardException(String message, XS24HZDBJ xs24HZDBJ) {
        super(message);
        this.xs24HZDBJ = xs24HZDBJ;
    }
    public AfterBoardException(XS24HZDBJ xs24HZDBJ) {
        super(xs24HZDBJ.getDQSBYY());
        this.xs24HZDBJ = xs24HZDBJ;
    }

    // 用指定的详细信息和原因构造一个新的异常
    public AfterBoardException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public AfterBoardException(Throwable cause) {
        super(cause);
    }

    public AfterBoardException(XS24HZDBJ bjlog, String zt, String msg, String errorcode) {
        bjlog.setDQZT(zt);
        bjlog.setDQSBYY(msg);
        bjlog.setErrorCode(errorcode);
        this.xs24HZDBJ = bjlog;
    }

    public AfterBoardException(Throwable cause , String proinstId ){
        super(cause);
        this.proinstId = proinstId;
    }

    public XS24HZDBJ getXs24HZDBJ() {
        return xs24HZDBJ;
    }

    public void setXs24HZDBJ(XS24HZDBJ xs24HZDBJ) {
        this.xs24HZDBJ = xs24HZDBJ;
    }
}
