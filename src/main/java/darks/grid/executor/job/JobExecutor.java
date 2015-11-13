/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
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
package darks.grid.executor.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.network.GridSession;
import darks.grid.utils.GridStatistic;

public abstract class JobExecutor implements Runnable
{
	
	private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);
	
	private GridSession session;
	
	private GridMessage msg;
	
	private GridJob job;
	
	private JobStatusType statusType;
	
	private long timestamp = System.currentTimeMillis();
	
	public JobExecutor(GridSession session, GridMessage msg)
	{
		this.session = session;
		this.msg = msg;
		this.job = msg.getData();
		GridStatistic.addWaitJobCount(1);
	}

	@Override
	public void run()
	{
		try
		{
			long delay = System.currentTimeMillis() - timestamp;
			GridStatistic.incrementJobDelay(delay);
			GridStatistic.addWaitJobCount(-1);
			statusType = JobStatusType.DOING;
			execute(session, msg, job);
			statusType = JobStatusType.SUCCESS;
		}
		catch (Exception e)
		{
			statusType = JobStatusType.FAIL;
			log.error(e.getMessage(), e);
		}
		GridRuntime.jobs().completeExecuteJob(this);
	}

	public abstract void execute(GridSession session, GridMessage msg, GridJob job) throws Exception;
	
	public String getTaskId()
	{
		return job.getTaskId();
	}
	
	public String getJobId()
	{
		return job.getJobId();
	}

	public JobStatusType getStatusType()
	{
		return statusType;
	}

	public void setStatusType(JobStatusType statusType)
	{
		this.statusType = statusType;
	}
	
}
