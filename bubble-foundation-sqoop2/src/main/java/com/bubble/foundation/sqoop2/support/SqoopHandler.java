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
package com.bubble.foundation.sqoop2.support;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MConfig;
import org.apache.sqoop.model.MDriverConfig;
import org.apache.sqoop.model.MFromConfig;
import org.apache.sqoop.model.MInput;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MSubmission;
import org.apache.sqoop.model.MToConfig;
import org.apache.sqoop.submission.counter.Counter;
import org.apache.sqoop.submission.counter.CounterGroup;
import org.apache.sqoop.submission.counter.Counters;
import org.apache.sqoop.validation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import com.bubble.foundation.common.utils.StringUtil;

/**
 * @author Vincent xiao<xiaosw@msn.cn>
 * @since 2016年1月29日
 */
@Repository
public class SqoopHandler implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(SqoopHandler.class);

	private SqoopClient sqoopClient;
	
	private String sqoopUrl;

	/**
	 * @param client
	 */

	public long createJob(String name, String creationUser, int fromLink, int toLink, Map<String, String> fromCfg, Map<String, String> toCfg) {
		long jobId = 0;
		MJob job = sqoopClient.createJob(fromLink, toLink);
		// MJob job=client.getJob(6);
		job.setName(name);
		job.setCreationUser(creationUser);

		// set the "FROM" link job config values
		MFromConfig fromJobConfig = job.getFromJobConfig();
		for (String key : fromCfg.keySet()) {
			fromJobConfig.getStringInput(key).setValue(fromCfg.get(key));
		}

		// set the "TO" link job config values
		MToConfig toJobConfig = job.getToJobConfig();
		for (String key : toCfg.keySet()) {
			toJobConfig.getStringInput(key).setValue(toCfg.get(key));
		}

		MDriverConfig driverConfig = job.getDriverConfig();
		driverConfig.getIntegerInput("throttlingConfig.numExtractors").setValue(3);

		Status status = sqoopClient.saveJob(job);
		if (status.canProceed()) {
			logger.info("Created Job with Job Id: " + job.getPersistenceId());
			jobId = job.getPersistenceId();
		} else {
			logger.error("Something went wrong creating the job");
		}
		return jobId;
	}

	/**
	 * @param client
	 */
	public boolean startJob(long id) {
		MSubmission status = sqoopClient.getJobStatus(id);
		if (status.getStatus().isRunning() && status.getProgress() != -1) {
			logger.info("Progress : " + String.format("%.2f %%", status.getProgress() * 100));
		}
		MSubmission submission = sqoopClient.startJob(id);
		logger.info("Job Submission Status : " + submission.getStatus());
		logger.info("Hadoop job id :" + submission.getExternalJobId());
		logger.info("Job link : " + submission.getExternalLink());
		Counters counters = submission.getCounters();
		if (counters != null) {
			logger.info("Counters:");
			for (CounterGroup group : counters) {
				logger.info("\t" + group.getName());
				for (Counter counter : group) {
					logger.info("\t\t" + counter.getName() + ": " + counter.getValue());
				}
			}
		}
		if (submission.getError().getErrorSummary() != null) {
			logger.error("Exception info : " + submission.getError());
			return false;
		}
		return true;
	}

	public MSubmission getJobStatus(long id) {
		return sqoopClient.getJobStatus(id);
	}

	public boolean updateJob(long jid, String dirInput, String dirOutput) {
		MJob job = sqoopClient.getJob(jid);

		if (!StringUtil.isEmpty(dirInput)) {
			MFromConfig fromJobConfig = job.getFromJobConfig();
			fromJobConfig.getStringInput("fromJobConfig.inputDirectory").setValue(dirInput);
		}

		if (!StringUtil.isEmpty(dirOutput)) {
			MToConfig toJobConfig = job.getToJobConfig();
			toJobConfig.getStringInput("toJobConfig.outputDirectory").setValue(dirOutput);
		}

		MDriverConfig driverConfig = job.getDriverConfig();
		driverConfig.getIntegerInput("throttlingConfig.numExtractors").setValue(3);

		Status status = sqoopClient.updateJob(job);
		if (status.canProceed()) {
			logger.info("Created Job with Job Id: " + job.getPersistenceId());
			return true;
		} else {
			logger.error("Something went wrong creating the job");
		}
		return false;
	}

	/**
	 * @param client
	 * @param i
	 */
	public MSubmission stopJob(SqoopClient client, int id) {
		MSubmission status = client.getJobStatus(id);
		if (status.getStatus().isRunning() && status.getProgress() != -1) {
			logger.info("Progress : " + String.format("%.2f %%", status.getProgress() * 100));
		}
		MSubmission submission = client.stopJob(id);
		logger.info("Job Submission Status : " + submission.getStatus());
		if (submission.getStatus().isRunning() && submission.getProgress() != -1) {
			logger.info("Progress : " + String.format("%.2f %%", submission.getProgress() * 100));
		}
		logger.info("Hadoop job id :" + submission.getExternalJobId());
		logger.info("Job link : " + submission.getExternalLink());
		Counters counters = submission.getCounters();
		if (counters != null) {
			logger.info("Counters:");
			for (CounterGroup group : counters) {
				logger.info("\t" + group.getName());
				for (Counter counter : group) {
					logger.info("\t\t" + counter.getName() + ": " + counter.getValue());
				}
			}
		}
		if (submission.getError().getErrorSummary() != null) {
			logger.error("Exception info : " + submission.getError());
		}
		return status;
	}

	static void describe(List<MConfig> configs, ResourceBundle resource) {
		for (MConfig config : configs) {
			logger.info(resource.getString(config.getLabelKey()) + ":");
			List<MInput<?>> inputs = config.getInputs();
			for (MInput input : inputs) {
				logger.info(input.getName());
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.sqoopClient = new SqoopClient(this.sqoopUrl);
	}

	
	public void setSqoopUrl(String sqoopUrl) {
		this.sqoopUrl = sqoopUrl;
	}

}
