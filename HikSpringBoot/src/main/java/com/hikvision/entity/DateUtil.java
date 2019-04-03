package com.hikvision.entity;

import java.sql.Timestamp;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/*
	 * author: haitao zhang des:获取时间 格式：2015-05-03 date:2015-07-27
	 */
	public static java.sql.Date getDate() {
		java.util.Date uDate = new java.util.Date();
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

	public static java.sql.Date getyMdDate(java.util.Date date) {

		java.sql.Date sDate = new java.sql.Date(date.getTime());
		return sDate;
	}

	/*
	 * author: haitao zhang des:获取时间 格式：2015-05-03 02:20:20 date:2015-07-27
	 */
	public static String getDateFormat() {
		java.util.Date uDate = new java.util.Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		String str = sf.format(uDate.getTime());
		return str;
	}//

	public static Date stringCovertDate(String dateString) {
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
			date = sdf.parse(dateString);

		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	/*
	 * author: haitao zhang des:获取时间 格式：2015-05-03 02:20:20 date:2015-07-27
	 */
	public static String getDateFormatSSS() {
		java.util.Date uDate = new java.util.Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		// java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		String str = sf.format(uDate.getTime());
		return str;
	}//
	
	/*
	 * author: haitao zhang des:获取时间 格式：2015-05-03 02:20:20 date:2015-07-27
	 */
	public static Date getDateFormatdae() {
		java.util.Date uDate = new java.util.Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		// java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		String str = sf.format(uDate.getTime());
		
		return stringCovertDate(str);
	}//
	
	
	public static String getDateFormatss() {
		java.util.Date uDate = new java.util.Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		String str = sf.format(uDate.getTime());
		return str;
	}//
	
	
	/*
	 * author: kang   li des:获取时间 格式：2016-03-23  date:2015-07-27没有分秒
	 */
	public static String getDateFormatWithoutMinutes() {
		java.util.Date uDate = new java.util.Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		// java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		String str = sf.format(uDate.getTime());
		return str;
	}
	

	/*
	 * author: haitao zhang des:获取时间戳 格式：1437938224000 date:2015-07-27
	 */
	public static long GetTimestamp() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date uDate = new java.util.Date();
		String str = sf.format(uDate.getTime());
		Timestamp date = java.sql.Timestamp.valueOf(str);
		long tmp = date.getTime();
		return tmp;
	}

	/**
	 * 对比时间的大小
	 * 
	 * @param date1
	 *            对比的时间1
	 * @param date2
	 *            对比的时间2
	 * @return 1,两个时间相等2,date1时间小于date2,date1时间大于date2
	 */
	public static int DateCompareTo(String date1, String date2) {
		int i = 0;
		try {

			String s1 = date1;
			String s2 = date2;
			java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar c1 = java.util.Calendar.getInstance();
			java.util.Calendar c2 = java.util.Calendar.getInstance();
			c1.setTime(df.parse(s1));
			c2.setTime(df.parse(s2));
			int result = c1.compareTo(c2);
			if (result == 0)
				i = 1;
			else if (result < 0)
				// System.out.println("c1小于c2");
				i = 2;
			else
				// System.out.println("c1大于c2");
				i = 3;
		} catch (java.text.ParseException e) {
			System.err.println("格式不正确");
		}
		return i;

	}

	/**
	 * 增加日期
	 * 
	 * @param format
	 *            "yyyy-MM-dd"
	 * @param StrDate
	 *            "2016-01-11"
	 * @param year
	 *            0
	 * @param month
	 *            1
	 * @param day
	 *            0
	 * @return
	 */
	public static String GetSysDate(Date StrDate) {
		
		String reStr="";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = StrDate;
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);

			rightNow.add(Calendar.MONTH, 1);
			Date dt1 = rightNow.getTime();
		     reStr = sdf.format(dt1);
			System.out.print(reStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return formateDate(reStr);
		
		

	}
	
	public static String formateDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);
        String da = "";
        if (date != null) {
            try {
                da = sdf.format(df.parse(date));
            } catch (ParseException e) {
                return "";
            }
        }
        if (da == null) da = "";
        return da;
    }
	public static String formateDateYYYYMMd(String date) {
    
       SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String da = "";
        if (date != null) {
            try {
                da = df.format(sdf.parse(date));
            } catch (ParseException e) {
                return "";
            }
        }
        if (da == null) da = "";
        return da;
    }


	/***
	 * 日期月份减一个月
	 * 
	 * @param datetime
	 *            日期(2014-11)
	 * @return 2014-10
	 */
	public static String dateFormat(String datetime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = sdf.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.MONTH, -1);
		date = cl.getTime();
		return sdf.format(date);
	}

	public static String dateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(date);
	}

	/****
	 * 传入具体日期 ，返回具体日期减一个月。
	 * 
	 * @param date
	 *            日期(2014-04-20)
	 * @return 2014-03-20
	 * @throws ParseException
	 */
	public static String subMonth(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = sdf.parse(date);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);

		rightNow.add(Calendar.MONTH, -1);
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);

		return reStr;
	}

	/****
	 * 获取月末最后一天
	 * 
	 * @param sDate
	 *            2014-11-24
	 * @return 30
	 */
	private static String getMonthMaxDay(String sDate) {
		SimpleDateFormat sdf_full = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf_full.parse(sDate + "-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(date);
		int last = cal.getActualMaximum(Calendar.DATE);
		return String.valueOf(last);
	}

	// 判断是否是月末
	public static boolean isMonthEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DATE) == cal.getActualMaximum(Calendar.DAY_OF_MONTH))
			return true;
		else
			return false;
	}

	/***
	 * 日期减一天、加一天
	 * 
	 * @param option
	 *            传入类型 pro：日期减一天，next：日期加一天
	 * @param _date
	 *            2014-11-24
	 * @return 减一天：2014-11-23或(加一天：2014-11-25)
	 */
	public static String checkOption(String option, String _date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		Date date = null;

		try {
			date = (Date) sdf.parse(_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cl.setTime(date);
		if ("pre".equals(option)) {
			// 时间减一天
			cl.add(Calendar.DAY_OF_MONTH, -1);

		} else if ("next".equals(option)) {
			// 时间加一天
			cl.add(Calendar.DAY_OF_YEAR, 1);
		} else {
			// do nothing
		}
		date = cl.getTime();
		return sdf.format(date);
	}

	/***
	 * 判断日期是否为当前月， 是当前月返回当月最小日期和当月目前最大日期以及传入日期上月的最大日和最小日
	 * 不是当前月返回传入月份的最大日和最小日以及传入日期上月的最大日和最小日
	 * 
	 * @param date
	 *            日期 例如：2014-11
	 * @return String[] 开始日期，结束日期，上月开始日期，上月结束日期
	 * @throws ParseException
	 */
	public static String[] getNow_Pre_Date(String date) throws ParseException {

		String[] str_date = new String[4];
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf_full = new SimpleDateFormat("yyyy-MM-dd");
		String stMonth = sdf.format(now);
		String stdate = "";// 开始日期
		String endate = "";// 结束日期
		String preDate_start = "";// 上月开始日期
		String preDate_end = "";// 上月结束日期

		// 当前月
		if (date.equals(stMonth)) {
			stdate = stMonth + "-01"; // 2014-11-01
			endate = sdf_full.format(now);// 2014-11-24
			preDate_start = subMonth(stdate);// 2014-10-01
			preDate_end = subMonth(endate);// 2014-10-24
		} else {
			// 非当前月
			String monthMaxDay = getMonthMaxDay(date);
			stdate = date + "-01";// 2014-10-01
			endate = date + "-" + monthMaxDay;// 2014-10-31
			preDate_start = subMonth(stdate);// 2014-09-01
			preDate_end = subMonth(endate);// 2014-09-30
		}
		str_date[0] = stdate;
		str_date[1] = endate;
		str_date[2] = preDate_start;
		str_date[3] = preDate_end;

		return str_date;
	}

	/**
	 * 增加日期
	 * 
	 * @param 增加的整数
	 * @param type
	 *            0,增加一天，1增加月份
	 * @return data
	 */
	public static Date addDay(int n, int type) {
		try {

			Calendar cd = Calendar.getInstance();
			cd.setTime(DateUtil.getDate());
			if (type > 0) {
				cd.add(Calendar.MONTH, n);// 增加一个月
			} else {
				cd.add(Calendar.DATE, n);// 增加一天
			}
			return cd.getTime();

		} catch (Exception e) {
			return null;
		}
	}

}
