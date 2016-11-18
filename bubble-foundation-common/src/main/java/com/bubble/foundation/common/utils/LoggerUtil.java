/**
 * Copyright [2015-2017]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bubble.foundation.common.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;


/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年7月12日
 */
public class LoggerUtil {

	public static long timeStart(Logger logger,Object... obj){
		Throwable throwable = new Throwable();
		StackTraceElement[] ste = throwable.getStackTrace();
	    String method = "";
	    int lineNumber = 0;
		if(ste.length > 1){			
			method = ste[1].getMethodName();
			lineNumber  = ste[1].getLineNumber();
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now = df.format(new Date());
		logger.info("耗时开始: {}, "+ "方法名: "+ method + ", 行号: "+ lineNumber + ", 时间："+ now, Arrays.toString(obj), now);
		return System.currentTimeMillis();  //返回当前的秒数，方便计算耗时
	}
	
	public static void timeEnd(Logger logger,long startTime, Object... obj){
		Throwable throwable = new Throwable();
		StackTraceElement[] ste = throwable.getStackTrace();
	    String method = "";
	    int lineNumber = 0;
		if(ste.length > 1){			
			method = ste[1].getMethodName();
			lineNumber  = ste[1].getLineNumber();
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now = df.format(new Date());
		logger.info("耗时结束 : {}, "+ "方法名: "+ method + ", 行号: "+ lineNumber + ", 时间："+ now + ", 耗时：" + (System.currentTimeMillis()-startTime)/1000f +"秒", Arrays.toString(obj), now);
	}
	
	public static void logParams(Logger logger, Object... obj){
		
		Throwable throwable = new Throwable();
		StackTraceElement[] ste = throwable.getStackTrace();
	    String method = "";
	    int lineNumber = 0;
		if(ste.length > 1){			
			method = ste[1].getMethodName();
			lineNumber  = ste[1].getLineNumber();
		}	 
		logger.info("方法名: "+ method + ", 行号: "+ lineNumber +", 参数: "+  Arrays.toString(obj) );
	}
	
	
}
