package com.supermap.intelligent.util;

import com.supermap.intelligent.model.LOG_DECLARE_RECORD_LOG;
import com.supermap.wisdombusiness.workflow.service.common.Common;

import java.util.Date;

/**
 * 需要解决的异常，用于标识知道异常原因但需要人工解决的异常
 */
public class BeresolvException extends RuntimeException{

    LOG_DECLARE_RECORD_LOG recordLog;

    public BeresolvException() {
        super();
    }

    public BeresolvException(String message,String errorcode) {
        super(message);
        //不带实体类直接抛出的异常，赋值一条默认的日志记录错误原因
        LOG_DECLARE_RECORD_LOG record_log = new LOG_DECLARE_RECORD_LOG();
        record_log.setBSM(Common.CreatUUID());
        record_log.setSBLSH("isempty");
        record_log.setYWLY("3");
        record_log.setSBCS(1);
        record_log.setSBZT("1");
        record_log.setSFDB("0");
        record_log.setCREATEDATE(new Date());
        record_log.setMODIFYTIME(new Date());
        record_log.setYWLCID("");
        record_log.setERRORCODE(errorcode);
        record_log.setERRORLOG(message);
        this.recordLog = record_log;
    }

    public BeresolvException(String message, LOG_DECLARE_RECORD_LOG recordLog) {
        super(message);
        this.recordLog = recordLog;
    }

    public BeresolvException(LOG_DECLARE_RECORD_LOG recordLog) {
        super(recordLog.getREMARK());
        this.recordLog = recordLog;
    }

    // 用指定的详细信息和原因构造一个新的异常
    public BeresolvException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public BeresolvException(Throwable cause) {
        super(cause);
    }

    public BeresolvException(LOG_DECLARE_RECORD_LOG record_log, String sbzt, String msg, String errorcode) {
        record_log.setSBZT(sbzt);
        record_log.setREMARK(msg);
        record_log.setERRORCODE(errorcode);
        this.recordLog = record_log;
    }

    public LOG_DECLARE_RECORD_LOG getRecordLog() {
        return recordLog;
    }

    public void setRecordLog(LOG_DECLARE_RECORD_LOG recordLog) {
        this.recordLog = recordLog;
    }

}
