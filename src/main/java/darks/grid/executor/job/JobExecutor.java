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

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.network.GridSession;
import darks.grid.utils.GridStatistic;

public abstract class JobExecutor extends Thread
{
	
	private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);
	
	private GridSession session;
	
	private GridMessage msg;
	
	private GridJob job;
	
	private JobStatusType statusType;
	
	private long timestamp;
	
	private long delay;
	
	private AtomicBoolean canceled = new AtomicBoolean(false);
	
	public JobExecutor(GridSession session, GridMessage msg)
	{
		GridStatistic.addWaitJobCount(1);
		this.session = session;
		this.msg = msg;
		this.job = msg.getData();
		this.statusType = JobStatusType.WAITING;
		this.timestamp = System.currentTimeMillis();
	}

	@Override
	public void run()
	{
		try
		{
			delay = System.currentTimeMillis() - timestamp;
			GridStatistic.incrementJobDelay(delay);
			GridStatistic.addWaitJobCount(-1);
			if (!isInvalid())
			{
				setStatusType(JobStatusType.DOING);
				execute(session, msg, job);
				setStatusType(JobStatusType.SUCCESS);
			}
			else
				setStatusType(JobStatusType.CANCEL);
		}
		catch (Exception e)
		{
			setStatusType(JobStatusType.FAIL);
			log.error(e.getMessage(), e);
		}
		GridRuntime.jobs().completeExecuteJob(this);
	}

	public abstract void execute(GridSession session, GridMessage msg, GridJob job) throws Exception;
	
	public boolean isTimeout()
	{
		if (job.getTimeout() > 0 
				&& System.currentTimeMillis() - timestamp > job.getTimeout())
			return true;
		return false;
	}
	
	public boolean isInvalid()
	{
		return canceled.get() || isInterrupted() || !session.isActive() || isTimeout();
	}
	
	public boolean isCanncel()
	{
		return canceled.get() || isInterrupted();
	}
	
	public void cancel()
	{
		canceled.getAndSet(true);
		setStatusType(JobStatusType.CANCEL);
		interrupt();
	}
	
	public String getTaskId()
	{
		return job.getTaskId();
	}
	
	public String getJobId()
	{
		return job.getJobId();
	}

	public synchronized JobStatusType getStatusType()
	{
		return statusType;
	}

	public synchronized void setStatusType(JobStatusType statusType)
	{
		this.statusType = statusType;
	}
	
	public GridJob getJob()
	{
		return job;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public long getDelay()
	{
		return delay;
	}

	public void setDelay(long delay)
	{
		this.delay = delay;
	}
	

	public GridSession getSession()
	{
		return session;
	}

	@Override
	public String toString()
	{
		return "JobExecutor [session=" + session + ", msg=" + msg + ", job=" + job
				+ ", statusType=" + statusType + ", timestamp=" + timestamp + ", delay=" + delay
				+ ", canceled=" + canceled + "]";
	}

	public String toSimpleString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append(getJobId()).append(' ')
			.append(getStatusType()).append('\t')
			.append(System.currentTimeMillis() - getTimestamp()).append('/')
			.append(job.getTimeout()).append(' ')
			.append(getDelay());
		if (isCanncel())
			buf.append(' ').append("CANCELED");
		return buf.toString();
	}
	
	
}
