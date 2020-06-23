package com.supermap.wisdombusiness.workflow.service.wfd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Holiday;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;

@Service("smHoliday")
public class SmHoliday {
	private boolean holidayFlag;
	private List Weekend = null;
	private List Weekday = null;
	@Autowired
	private CommonDao _CommonDao;

	private String[] weekStrings = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六" };

	// 0代表周日，6代表周六
	private int isWeekend(Calendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	public Message GetHolidayList(int pageIndex, int pageSize, String name,
			String startDate, String endDate) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		if (!name.equals("")) {
			str.append(" and Holiday_Name like '%");
			str.append(name);
			str.append("%'");
		}
		if (startDate != null) {
			if (!startDate.equals("")) {
				str.append(" and to_char(Holiday_Startdate,'yyyy-MM-dd')>=to_char(to_date('");
				str.append(startDate);
				str.append("','yyyy-MM-dd'),'yyyy-MM-dd')");
			}
		}
		if (endDate != null) {
			if (!endDate.equals("")) {
				str.append(" and to_char(Holiday_Startdate,'yyyy-MM-dd')<=to_char(to_date('");
				str.append(endDate);
				str.append("','yyyy-MM-dd'),'yyyy-MM-dd')");
			}
		}
		if (str.toString().equals("")) {
			str.append(" 1=1 ");
		} else {
			str.delete(0, 4);
		}
		str.append("  order by Holiday_Startdate");
		Page page = _CommonDao.GetPagedData(Wfd_Holiday.class, pageIndex,
				pageSize, str.toString());
		msg.setRows(page.getResult());
		msg.setTotal(page.getTotalCount());
		YwLogUtil.addYwLog("访问：假期管理", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}

	public Message GetHolidayInfo(String id) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append(" Holiday_Id='");
		str.append(id);
		str.append("'");
		msg.setRows(_CommonDao.findList(Wfd_Holiday.class, str.toString()));
		return msg;
	}

	public Wfd_Holiday GetHolidayById(String id) {
		return _CommonDao.get(Wfd_Holiday.class, id);
	}

	public SmObjInfo DeleteHolidayById(String id) {
		SmObjInfo info = new SmObjInfo();
		_CommonDao.delete(Wfd_Holiday.class, id);
		_CommonDao.flush();
		info.setDesc("删除成功！");
		info.setID("0");
		YwLogUtil.addYwLog("假期管理-删除操作删除成功", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return info;
	}

	public SmObjInfo DeleteHolidayByIds(JSONArray array) {
		SmObjInfo info = new SmObjInfo();

		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			Wfd_Holiday Holiday = _CommonDao.get(Wfd_Holiday.class,
					object.get("id").toString());
			if (Holiday != null) {
				_CommonDao.delete(Holiday);
			}
		}
		_CommonDao.flush();
		info.setDesc("删除成功！");
		info.setID("0");
		YwLogUtil.addYwLog("假期管理-删除假期", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return info;
	}

	public void SaveOrUpdate(Wfd_Holiday Holidy) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		cl.setTime(Holidy.getHoliday_Startdate());
		StringBuilder str = new StringBuilder();
		str.append("to_char(Holiday_Startdate,'yyyy-MM-dd')='");
		str.append(format.format(Holidy.getHoliday_Startdate()));
		str.append("'");
		List<Wfd_Holiday> list = _CommonDao.findList(Wfd_Holiday.class,
				str.toString());
		if (list.size() > 0) {
			Wfd_Holiday OldHoliday = list.get(0);
			OldHoliday.setHoliday_Name(Holidy.getHoliday_Name());
			OldHoliday.setHoliday_Desc(weekStrings[isWeekend(cl)]);
			OldHoliday.setHoliday_Status(Holidy.getHoliday_Status());
			OldHoliday.setHoliday_Type(Holidy.getHoliday_Type());
			_CommonDao.saveOrUpdate(OldHoliday);
		} else {
			Holidy.setHoliday_Enddate(Holidy.getHoliday_Startdate());
			Holidy.setHoliday_Desc(weekStrings[isWeekend(cl)]);
			_CommonDao.saveOrUpdate(Holidy);
		}
		_CommonDao.flush();
		YwLogUtil.addYwLog("假期管理-更新假期", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
	}

	// 去除假期后的日期
	public Date FactDate(Date start, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		cl.setTime(DateUtil.addDay(start, day));
		StringBuilder str = new StringBuilder();
		str.append(" Holiday_Status=1 and to_char(Holiday_Startdate,'yyyy-MM-dd')>='");
		str.append(format.format(start));
		str.append("' and to_char(Holiday_Startdate,'yyyy-MM-dd')<='");
		str.append(format.format(cl.getTime()));
		str.append("'");
		List<Wfd_Holiday> list = _CommonDao.findList(Wfd_Holiday.class,
				str.toString());
		if (list.size() > 0) {
			cl.setTime(DateUtil.addDay(cl.getTime(), list.size()));
		}
		return cl.getTime();
	}

	public SmObjInfo InsertHoliday(String startDate, String endDate,
			String name, String type) {
		SmObjInfo info = new SmObjInfo();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
		try {

			Date _startDate = format2.parse(startDate.replace("-", "/"));
			Date _endDate = format2.parse(endDate.replace("-", "/"));
			if (_endDate.getTime() < _startDate.getTime()) {
				info.setDesc("结束时间不能早于开始时间!");
			} else {
				int insertNum = 0, updateNum = 0;
				// 获取天数
				long day = (_endDate.getTime() - _startDate.getTime())
						/ (24 * 60 * 60 * 1000);
				for (int i = 0; i <= day; i++) {
					Calendar cstart = Calendar.getInstance();
					cstart.setTime(DateUtil.addDay(_startDate, i));
					int wk = isWeekend(cstart);
					StringBuilder str = new StringBuilder();
					str.append("to_char(Holiday_Startdate,'yyyy-MM-dd')='");
					str.append(format.format(cstart.getTime()));
					str.append("'");
					List<Wfd_Holiday> list = _CommonDao.findList(
							Wfd_Holiday.class, str.toString());
					// 固定假期
					if (type.equals("1")) {
						switch (wk) {
						case 0:
						case 6:
							Wfd_Holiday Holiday = new Wfd_Holiday();
							if (list.size() > 0) {
								Holiday = list.get(0);
								updateNum++;
							} else {
								insertNum++;
							}
							Holiday.setHoliday_Startdate(cstart.getTime());
							Holiday.setHoliday_Enddate(cstart.getTime());
							Holiday.setHoliday_Status(1);
							Holiday.setHoliday_Type(1);
							Holiday.setHoliday_Name(weekStrings[wk]);
							Holiday.setHoliday_Desc(weekStrings[wk]);
							_CommonDao.saveOrUpdate(Holiday);
							break;
						default:
							break;
						}
					} else {
						Wfd_Holiday Holiday = new Wfd_Holiday();
						if (list.size() > 0) {
							Holiday = list.get(0);
							updateNum++;
						} else {
							insertNum++;
						}
						Holiday.setHoliday_Startdate(cstart.getTime());
						Holiday.setHoliday_Enddate(cstart.getTime());
						Holiday.setHoliday_Status(1);
						Holiday.setHoliday_Type(Integer.parseInt(type));
						Holiday.setHoliday_Name(name);
						Holiday.setHoliday_Desc(weekStrings[wk]);
						_CommonDao.saveOrUpdate(Holiday);
					}

				}
				_CommonDao.flush();
				info.setDesc("批量添加了" + insertNum + "天假期,批量更新了" + updateNum
						+ "天假期!");
				YwLogUtil.addYwLog("假期管理-批量添加假期", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			info.setDesc("保存出现异常!");
			YwLogUtil.addYwLog("添加假期", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}
		return info;
	}

	// ------------------------------------------
	/**
	 * 计算两个时间直接有几个工作日
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public long ComputerDiff(Date start, Date end) {
		long result=0;
		try{
			long milseconds=daysBetween(start,end);
			holidayFlag = false;			
			GregorianCalendar cal = new GregorianCalendar();
			if(milseconds>0){
				cal.setTime(start);
			}
			else{
				cal.setTime(end);
			}
			long days=(long) Math.ceil((double)(Math.abs(milseconds)/(1000*3600*24)));
			for (int i = 0; i < days; i++) {
				// 把源日期加一天
				cal.add(Calendar.DAY_OF_MONTH, 1);
				holidayFlag = isWeekday(cal);
				if (holidayFlag) {
					if(milseconds<0){
						milseconds +=1000*3600*24;
					}
					else{
						milseconds -=1000*3600*24;
					}
				
				}
				// System.out.println(src.getTime());
			}
			result=milseconds;
		
		}
		catch(Exception e){
			
		}
		return result;
	}
	public long daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1);  
            
       return between_days;           
    }    


	public Date addDateByWorkDay2(GregorianCalendar src, double adddays) {
		Weekend = null;
		Weekday = null;
		// Calendar result = null;
		holidayFlag = false;
		Double nowDate = 0.0;
		if(adddays > 1.0){
			nowDate = 1.0;
		}
		double floor=Math.floor(nowDate);
		for (int i = 0; i < floor; i++) {
			// 把源日期加一天
			src.add(Calendar.DAY_OF_MONTH, 1);
			
			holidayFlag = isWeekday(src);
			if (holidayFlag) {
				i--;
			}
			// System.out.println(src.getTime());
		}
		if(nowDate ==0.0){
			if(adddays>floor){
				int  dd=(int)Math.rint(24*(adddays-floor));
				src.add(java.util.Calendar.HOUR_OF_DAY,dd);
				if(src.getInstance().SATURDAY !=7 || src.getInstance().SUNDAY !=1){//目前测试只有周末时间，假期放假或者补班未排除
				holidayFlag = isWeekday(src);
					while(holidayFlag){
						src.add(Calendar.DAY_OF_MONTH, 1);
						holidayFlag = isWeekday(src);
					}
				}
			}
		}
		
		
		// System.out.println("Final Result:" + src.getTime());

		return src.getTime();
	}
	
	public Date addDateByWorkDay(GregorianCalendar src, double adddays) {
		Weekend = null;
		Weekday = null;
		// Calendar result = null;
		holidayFlag = false;
		double floor=Math.floor(adddays);
		for (int i = 0; i < floor; i++) {
			// 把源日期加一天
			src.add(Calendar.DAY_OF_MONTH, 1);
			
			holidayFlag = isWeekday(src);
			if (holidayFlag) {
				i--;
			}
			// System.out.println(src.getTime());
		}
		if(adddays>floor){
			int  dd=(int)Math.rint(24*(adddays-floor));
			src.add(java.util.Calendar.HOUR_OF_DAY,dd);
			holidayFlag = isWeekday(src);
			while(holidayFlag){
				src.add(Calendar.DAY_OF_MONTH, 1);
				holidayFlag = isWeekday(src);
			}
		}
		
		// System.out.println("Final Result:" + src.getTime());

		return src.getTime();
	}

	/**
	 * @title 判断两个日期是否在指定工作日内
	 * @detail (只计算周六和周日) 例如：前时间2008-12-05，后时间2008-12-11
	 * @author yizy
	 * @param beforeDate
	 *            前时间
	 * @param afterDate
	 *            后时间
	 * @param deadline
	 *            最多相隔时间
	 * @return 是的话，返回true，否则返回false
	 */
	public boolean compareWeekday(String beforeDate, String afterDate,
			int deadline) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = sdf.parse(beforeDate);
			Date d2 = sdf.parse(afterDate);

			// 工作日
			int workDay = 0;
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d1);
			// 两个日期相差的天数
			long time = d2.getTime() - d1.getTime();
			long day = time / 3600000 / 24 + 1;
			if (day < 0) {
				// 如果前日期大于后日期，将返回false
				return false;
			}
			for (int i = 0; i < day; i++) {
				if (isWeekday(gc)) {
					workDay++;
					// System.out.println(gc.getTime());
				}
				// 往后加1天
				gc.add(GregorianCalendar.DATE, 1);
			}
			return workDay <= deadline;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @title 判断是否为工作日
	 * @detail 工作日计算: 1、正常工作日，并且为非假期 2、周末被调整成工作日
	 * @author yizy
	 * @param date
	 *            日期
	 * @return 是工作日返回true，非工作日返回false
	 */
	public boolean isWeekday(GregorianCalendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY
				&& calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {
			// 平时
			return getWeekdayIsHolidayList().contains(
					sdf.format(calendar.getTime()));
		} else {
			// 周末
			return !getWeekendIsWorkDateList().contains(
					sdf.format(calendar.getTime()));
		}
	}

	/**
	 * @title 获取周六和周日是工作日的情况（手工维护） 注意，日期必须写全： 2009-1-4必须写成：2009-01-04
	 * @author yizy
	 * @return 周末是工作日的列表
	 */
	public List getWeekendIsWorkDateList() {
		if (Weekend == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Wfd_Holiday> list = _CommonDao
					.getDataList(
							Wfd_Holiday.class,
							"select * from "
									+ Common.WORKFLOWDB
									+ "Wfd_Holiday where holiday_type=1 and holiday_status=1");
			Weekend = new ArrayList();
			for (Wfd_Holiday h : list) {
				Weekend.add(sdf.format(h.getHoliday_Startdate()));
			}

		}
		return Weekend;
	}

	/**
	 * @title 获取周一到周五是假期的情况（手工维护） 注意，日期必须写全： 2009-1-4必须写成：2009-01-04
	 * @author yizy
	 * @return 平时是假期的列表
	 */
	public List getWeekdayIsHolidayList() {
		if (Weekday == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Wfd_Holiday> list = _CommonDao
					.getDataList(
							Wfd_Holiday.class,
							"select * from "
									+ Common.WORKFLOWDB
									+ "Wfd_Holiday where holiday_type=2 and holiday_status=1");
			Weekday = new ArrayList();
			for (Wfd_Holiday h : list) {
				Weekday.add(sdf.format(h.getHoliday_Startdate()));
			}
		}

		return Weekday;
	}

}
