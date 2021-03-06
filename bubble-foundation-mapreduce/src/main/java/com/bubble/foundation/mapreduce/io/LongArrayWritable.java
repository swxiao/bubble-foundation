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
package com.bubble.foundation.mapreduce.io;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;


/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年3月22日
 */
public class LongArrayWritable extends ArrayWritable {

	public LongArrayWritable() {
		super(LongWritable.class);
	}

	public String toString() {
		StringBuilder sb=new StringBuilder("value={");
		for(Writable o:this.get()){
			sb.append(((LongWritable)o).get()+" ; ");
		}
		sb.append("}");
		return sb.toString();
	}
	
}
