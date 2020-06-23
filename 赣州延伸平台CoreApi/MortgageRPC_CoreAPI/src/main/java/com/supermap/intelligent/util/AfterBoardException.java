package com.supermap.intelligent.util;

import com.supermap.intelligent.model.LOG_DECLARE_RECORD_LOG;

/**
 * 登簿后出现的异常，此时已完成登簿，单独捕获此异常，否则将把登簿的项目删除
 */
public class AfterBoardException extends RuntimeException{

    LOG_DECLARE_RECORD_LOG recordLog;

    public AfterBoardException() {
        super();
    }

    public AfterBoardException(String message) {
        super(message);
    }

    public AfterBoardException(String message, LOG_DECLARE_RECORD_LOG recordLog) {
        super(message);
        this.recordLog = recordLog;
    }
    public AfterBoardException(LOG_DECLARE_RECORD_LOG recordLog) {
        super(recordLog.getREMARK());
        this.recordLog = recordLog;
    }

    // 用指定的详细信息和原因构造一个新的异常
    public AfterBoardException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public AfterBoardException(Throwable cause) {
        super(cause);
    }

    public AfterBoardException(LOG_DECLARE_RECORD_LOG record_log, String sbzt, String msg, String errorcode) {
        record_log.setSBZT(sbzt);
        record_log.setREMARK(msg);
        record_log.setERRORCODE(errorcode);
        this.recordLog = record_log;
    }

    public AfterBoardException(Throwable cause , String proinstId ){
        super(cause);
    }

    public LOG_DECLARE_RECORD_LOG getRecordLog() {
        return recordLog;
    }

    public void setRecordLog(LOG_DECLARE_RECORD_LOG recordLog) {
        this.recordLog = recordLog;
    }
}
