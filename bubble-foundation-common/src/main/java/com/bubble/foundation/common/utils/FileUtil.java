/**
 * Copyright [2015-2017]
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

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2015年12月23日
 */
public class FileUtil extends FileUtils {

	/**
	 * 重命名文件
	 * 
	 * @param srcFile
	 * @param dstFile
	 */
	public static boolean renameFile(String srcFile, String dstFile) {
		Assert.notNull(srcFile, "The param 'srcFile' can't be null.");
		Assert.notNull(srcFile, "The param 'dstFile' can't be null.");
		File sfile = new File(srcFile);
		File dfile = new File(dstFile);
		if (sfile.canRead() || sfile.canWrite()){
			return sfile.renameTo(dfile);
		}
		return false;
	}

}
