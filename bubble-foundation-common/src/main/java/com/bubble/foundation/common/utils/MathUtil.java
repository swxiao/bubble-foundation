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

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年3月22日
 */
public class MathUtil{
	
	/**
	 * 除法（无小数位的四舍五入）
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> T  devide(T a,T b){
		  return devide(a,b,0,RoundingMode.HALF_UP);
	}
	/**
	 * 除法（小数位的四舍五入）
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static <T> T devide(T a,T b,int scale){
		return devide(a,b,scale,RoundingMode.HALF_UP);
	}
	
	/**
	 * 除法(无小数位的取舍)
	 * @param a
	 * @param b
	 * @param m
	 * @return
	 */
	public static <T> T devide(T a,T b,RoundingMode m){
		return devide(a,b,0,m);
	}
	
	private static BigDecimal calculate(BigDecimal a,BigDecimal b,int scale,RoundingMode m){
		if(b.compareTo(BigDecimal.ZERO)==0){
			return a;
		}
		return a.divide(b,scale,m);
	}
	
	public static <T> T devide(T a,T b,int scale,RoundingMode m){
			if(a==null||b==null){
				return null;
			}
			String clz=a.getClass().getName();
			if("java.lang.Long".equalsIgnoreCase(clz)){
				BigDecimal result=calculate(new BigDecimal((Long)a),new BigDecimal((Long)b),scale,m);
				return (T)Long.valueOf(result.longValue());
			}else if("java.lang.Short".equalsIgnoreCase(clz)){
				BigDecimal result=calculate(new BigDecimal((Short)a),new BigDecimal((Short)b),scale,m);
				return (T)Short.valueOf(result.shortValue());
			}else if("java.lang.Double".equalsIgnoreCase(clz)){
				BigDecimal result=calculate(new BigDecimal((Double)a),new BigDecimal((Double)b),scale,m);
				return (T)Double.valueOf(result.doubleValue());
			}else if("java.lang.Float".equalsIgnoreCase(clz)){
				BigDecimal result=calculate(new BigDecimal((Float)a),new BigDecimal((Float)b),scale,m);
				return (T)Float.valueOf(result.floatValue());
			}else if("java.lang.Integer".equalsIgnoreCase(clz)){
				BigDecimal result=calculate(new BigDecimal((Integer)a),new BigDecimal((Integer)b),scale,m);
				return (T)Integer.valueOf(result.intValue());
			}else if("java.math.BigDecimal".equalsIgnoreCase(clz)){
				BigDecimal result=calculate((BigDecimal)a,(BigDecimal)b,scale,m);
				return (T)result;
			}
		  return null;
	}
}
