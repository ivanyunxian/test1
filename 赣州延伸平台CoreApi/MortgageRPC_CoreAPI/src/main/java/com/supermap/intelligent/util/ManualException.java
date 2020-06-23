package com.supermap.intelligent.util;

import com.supermap.intelligent.model.LOG_DECLARE_RECORD_LOG;

/**
 * 自定义异常，用于手动抛出异常
 */
public class ManualException extends RuntimeException{

    LOG_DECLARE_RECORD_LOG recordLog;

    public ManualException() {
        super();
    }

    public ManualException(String message) {
        super(message);
    }

    public ManualException(String message, LOG_DECLARE_RECORD_LOG recordLog) {
        super(message);
        this.recordLog = recordLog;
    }
    public ManualException(LOG_DECLARE_RECORD_LOG recordLog) {
        super(recordLog.getREMARK());
        this.recordLog = recordLog;
    }

    // 用指定的详细信息和原因构造一个新的异常
    public ManualException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public ManualException(Throwable cause) {
        super(cause);
    }

    public ManualException(LOG_DECLARE_RECORD_LOG record_log, String sbzt, String msg, String errorcode) {
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
