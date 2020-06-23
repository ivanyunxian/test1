package com.supermap.wisdombusiness.workflow.service.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Holiday;

public class WeekdayUtil {
    private  static boolean holidayFlag;
    @Autowired
    private static CommonDao commonDao;
    /**
     * 计算某一天加几天后的日期（排除节假日）
     * @param src 开始日期
     * @param adddays 增加天数
     * @return 实际日期
     */
	
}
