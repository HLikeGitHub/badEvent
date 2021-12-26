package com.yjy.web.comm.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateUtil {
	public static Date getStrtoDate(String dateStr) throws ParseException {
		DateFormat df;
		if(dateStr.equals("")) {
			//dateStr=DateUtil.getFormatStr(DateUtils.addDays(new Date(), -30),"yyyy-MM-dd");
			return DateUtils.addDays(new Date(), -30);
		}
		if(dateStr.length()<12) {
			df = new SimpleDateFormat("yyyy-MM-dd");
		}else {
			df = new SimpleDateFormat(dateStr.length() < 19 ? "yyyy-MM-dd hh:mm" : "yyyy-MM-dd hh:mm:ss");
			
		}
		 
		Date date=df.parse(dateStr);
		return date;
	}


	
  public static String getFormatStr(Date date,String dateFormat) {
	   
		
		if(dateFormat.equals("")) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EE");
			return df.format(date);
		}else {
			DateFormat df = new SimpleDateFormat(dateFormat);
			return df.format(date);
		}
//		DateFormat df1 = DateFormat.getInstance();
//		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EE");
//		DateFormat df3 = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒  EE",Locale.CHINA);
//		DateFormat df4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EE",Locale.US);
//		DateFormat df5 = new SimpleDateFormat("yyyy-MM-dd");
//		DateFormat df6 = new SimpleDateFormat("yyyy年MM月dd日");
//		System.out.println("-------将日期按不同格式进行输出-------");
//		System.out.println("按照java默认的日期格式:"+df1.format(date));
//		System.out.println("按照指定格式 yyyy-MM-dd hh:mm:ss EE,系统默认区域:"+df2.format(date));
//		System.out.println("按照指定格式 yyyy年MM月dd日  hh时mm分ss秒  EE,区域为中国:"+df3.format(date));
//		System.out.println("按照指定格式 yyyy-MM-dd hh:mm:ss EE,区域为美国:"+df4.format(date));
//		System.out.println("按照指定格式 yyyy-MM-dd :"+df5.format(date));
//		System.out.println("按照指定格式 yyyy年MM月dd日 :"+df6.format(date));
	    
	  
  }
}
