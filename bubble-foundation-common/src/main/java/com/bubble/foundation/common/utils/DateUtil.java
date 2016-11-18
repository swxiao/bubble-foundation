/**
 * Copyright 2015-2017
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.bubble.foundation.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年2月5日
 */
public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final Map<String, DateFormat> map = new ConcurrentHashMap<String, DateFormat>();

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Create SimpleDateFormat object
	 * 
	 * @param pattern
	 * @return
	 */
	public static DateFormat getDateFormat(String pattern, Locale locale) {
		Assert.notNull(pattern, "pattern can't be null");
		Assert.notNull(locale, "locale can't be null");
		DateFormat dateFormat = map.get(pattern);
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(pattern, locale);
			map.put(pattern, dateFormat);
		}
		return dateFormat;
	}

	public static DateFormat getDateFormat(String pattern) {
		return getDateFormat(pattern, Locale.CHINESE);
	}

	/**
	 * Format date by pattern
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String format(String pattern, Date date) {
		return getDateFormat(pattern).format(date);
	}

	/**
	 * Parse date by string pattern
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static Date parse(String pattern, String date) {
		try {
			return getDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return new Date();
		}
	}

	/**
	 * get date string
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getDate() {
		return format(DATE_FORMAT, new Date());
	}

	/**
	 * get time string
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTime() {
		return format(DATETIME_FORMAT, new Date());
	}

	/**
	 * get date string by increase
	 * 
	 * @param increase
	 * @return yyyy-MM-dd
	 */
	public static String getDate(int increase) {
		Assert.notNull(increase, "increase can't be null");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, increase);
		return format(DATE_FORMAT, calendar.getTime());
	}

	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		smdate = parse(DATE_FORMAT, format(DATE_FORMAT, smdate));
		bdate = parse(DATE_FORMAT, format(DATE_FORMAT, bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetween(String smdate, String bdate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(DATE_FORMAT,smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(parse(DATE_FORMAT,bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
}
