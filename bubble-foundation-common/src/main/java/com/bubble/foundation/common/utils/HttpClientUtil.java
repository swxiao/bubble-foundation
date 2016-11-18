package com.bubble.foundation.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 下载文件
	 * 
	 * @param url
	 *            文件源
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param localDst
	 *            本地目的文件路径
	 */
	public static void downLoadFile(String url, String userName, String password, String localDst) {
		HttpMethod method = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			HttpClient client = new HttpClient();
			method = new GetMethod(url);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 60 * 5);//client超时时间
			method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 1000 * 60 * 5);//method超时时间
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));//重试次数
			if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(password)) {
				Credentials credentials = new UsernamePasswordCredentials(userName, password);
				client.getState().setCredentials(AuthScope.ANY, credentials);
			}
			
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.warn("WARN: download file error,status code is {}.", statusCode);
				return;
			}
			String path = method.getPath();
			String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
			InputStream is = method.getResponseBodyAsStream();
			bis = new BufferedInputStream(is);
			File parentFolder = new File(localDst);
			if (!parentFolder.exists()) {
				boolean mkresult = parentFolder.mkdirs();
				if (!mkresult) {
					logger.warn("WARN: download file error,can't establish local destination directory.", statusCode);
					return;
				}
			}
			File dstFile = new File(parentFolder, fileName);
			if (dstFile.exists()) {
				logger.warn("WARN: destination file '{}' has already exists.", dstFile.getAbsolutePath());
			}
			bos = new BufferedOutputStream(new FileOutputStream(dstFile));
			byte[] bytes = new byte[1024];
			while (bis.read(bytes) != -1) {
				bos.write(bytes);
			}
			bos.flush();
			logger.info("Download file successful,file path is:{}", dstFile.getAbsolutePath());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (method != null)
				method.releaseConnection();
		}
	}
}
