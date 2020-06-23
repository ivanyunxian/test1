package com.supermap.internetbusiness.util;

import com.supermap.internetbusiness.model.XS24HZDBJ;

/**
 * 自定义异常，用于手动抛出异常
 */
public class ManualException extends RuntimeException{

    XS24HZDBJ xs24HZDBJ;
    String proinstId;

    public ManualException() {
        super();
    }

    public ManualException(String message) {
        super(message);
    }

    public ManualException(String message, XS24HZDBJ xs24HZDBJ) {
        super(message);
        this.xs24HZDBJ = xs24HZDBJ;
    }
    public ManualException(XS24HZDBJ xs24HZDBJ) {
        super(xs24HZDBJ.getDQSBYY());
        this.xs24HZDBJ = xs24HZDBJ;
    }

    // 用指定的详细信息和原因构造一个新的异常
    public ManualException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public ManualException(Throwable cause) {
        super(cause);
    }

    public ManualException(XS24HZDBJ bjlog, String zt, String msg, String errorcode) {
        bjlog.setDQZT(zt);
        bjlog.setDQSBYY(msg);
        bjlog.setErrorCode(errorcode);
        this.xs24HZDBJ = bjlog;
    }

    public ManualException(Throwable cause ,String proinstId ){
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
