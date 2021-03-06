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
package darks.grid.executor.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridFuture;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.job.GridJobStatus;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.job.JobStatusType;

public class GridJobFuture extends GridFuture<JobResult>
{
    
	private static final Logger log = LoggerFactory.getLogger(GridJobFuture.class);
	
    protected Map<String, GridJobStatus> statusMap = new ConcurrentHashMap<String, GridJobStatus>();
    
    private static final long DEFAULT_AWAIT_TIME = 100;
    
    private Lock lock = new ReentrantLock();
    
    private Condition statusCheck = lock.newCondition();
    
    private volatile boolean waitCheck = true;
    
    private TaskExecutor<?, ?> executor;
    
    private ExecuteConfig config;
    
    private AtomicInteger finishedCountAtomic = new AtomicInteger(0);

    public GridJobFuture(TaskExecutor<?, ?> executor)
    {
    	this.executor = executor;
    	this.config = executor.getConfig();
    }
    
    public void addJobStatus(GridJobStatus status)
    {
    	status.setStatusType(JobStatusType.WAITING);
    	statusMap.put(status.getJob().getJobId(), status);
    }
    
    public void removeJobStatus(String jobId)
    {
    	GridJobStatus status = statusMap.remove(jobId);
    	if (status != null)
    	{
            GridRuntime.jobs().removeNodeJob(status.getNode().getId(), jobId);
    	}
    }
    
    public void removeAllJobStatus()
    {
    	for (Entry<String, GridJobStatus> entry : statusMap.entrySet())
    	{
    		String jobId = entry.getKey();
    		GridJobStatus status = entry.getValue();
            GridRuntime.jobs().removeNodeJob(status.getNode().getId(), jobId);
    	}
    }
    
    public void replyStatus(GridJobReply reply)
    {
    	lock.lock();
    	try
		{
    		String jobId = reply.getJobId();
        	GridJobStatus status = statusMap.get(jobId);
        	if (status == null)
        	{
        		log.error("Cannot find job " + jobId);
        		return;
        	}
        	status.getResult().setObject(reply.getResult());
        	status.getResult().setSuccess(reply.isSuccess());
        	if (reply.isSuccess())
        		status.setStatusType(JobStatusType.SUCCESS);
        	else
        	{
        		status.setStatusType(JobStatusType.FAIL);
        		status.getResult().setErrorCode(reply.getErrorCode());
        		status.getResult().setErrorMessage(reply.getErrorMessage());
        	}
        	waitCheck = false;
//			log.info("signal..." + reply);
        	statusCheck.signal();
		}
		finally
		{
			lock.unlock();
		}
    }
    
    public void signalStatusCheck()
    {
    	lock.lock();
    	try
		{
    		waitCheck = false;
    		statusCheck.signalAll();
		}
		finally
		{
			lock.unlock();
		}
    }
    
	@Override
	public boolean isSuccess()
	{
		boolean success = true;
		for (Entry<String, GridJobStatus> entry : statusMap.entrySet())
		{
			success = success && entry.getValue().getStatusType() == JobStatusType.SUCCESS;
		}
		return success;
	}


	@Override
	public boolean await()
	{
		return await(0, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean await(int timeout, TimeUnit unit)
	{
		long maxTime = timeout > 0 ? unit.toMillis(timeout) : 0;
		long st = System.currentTimeMillis();
		List<GridJobStatus> statuses = new ArrayList<GridJobStatus>(statusMap.size());
		for (;;)
		{
			lock.lock();
			try
			{
				if (waitCheck)
				{
					long awaitTime = DEFAULT_AWAIT_TIME;
					if (maxTime > 0 && awaitTime > maxTime)
						awaitTime = maxTime / 2;
					statusCheck.await(awaitTime, TimeUnit.MILLISECONDS);
				}

				int finishedCount = 0;
				statuses.clear();
				statuses.addAll(statusMap.values());
				for (GridJobStatus status : statuses)
				{
					if (!status.getNode().isAlive())
					{
						log.info("Remove job " + status.getJob().getJobId() + ". failed in node " + status.getNode().getId());
						removeJobStatus(status.getJob().getJobId());
						if (status.getJob().isFailRedo())
						{
						    executor.failRedo(status.getJob());
						}
						continue;
					}
					if (status.getStatusType() == JobStatusType.SUCCESS
							|| status.getStatusType() == JobStatusType.FAIL)
					{
						finishedCount++;
					}
				}
				finishedCountAtomic.getAndSet(finishedCount);
				if (finishedCount == statusMap.size()
				        || (config.getResponseType() == ResponseType.SINGLE && finishedCount > 0))
					return true;
				waitCheck = true;
				if (maxTime > 0 && System.currentTimeMillis() - st > maxTime)
					return false;
			}
			catch (InterruptedException e)
			{
			    log.error("Interrupt job future. Cause " + e.getMessage(), e);
				return false;
			}
			finally
			{
				lock.unlock();
			}
		}
	}

	@Override
	public List<JobResult> getList()
	{
		List<JobResult> result = new ArrayList<JobResult>(statusMap.size());
		for (Entry<String, GridJobStatus> entry : statusMap.entrySet())
		{
		    GridJobStatus status = entry.getValue();
		    JobResult ret = status.getResult();
            ret.setSuccess(status.getStatusType() == JobStatusType.SUCCESS);
            if (config.getResponseType() == ResponseType.SINGLE)
            {
                if (status.getStatusType() == JobStatusType.SUCCESS 
                        || status.getStatusType() == JobStatusType.FAIL)
                {
                    result.add(ret);
                    break;
                }
            }
            else
            {
                result.add(ret);
            }
		}
		return result;
	}
	
	public boolean checkDeadJob()
	{
		lock.lock();
		try
		{
			boolean alive = true;
			for (Entry<String, GridJobStatus> entry : statusMap.entrySet())
			{
				GridJobStatus status = entry.getValue();
				alive = alive && status.getNode().isAlive();
			}
			if (!alive)
			{
				waitCheck = false;
	    		statusCheck.signalAll();
			}
			return alive;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public String toSimpleString(String linePrefix)
	{
		StringBuilder buf = new StringBuilder();
		for (GridJobStatus status : statusMap.values())
		{
			buf.append(linePrefix).append(status.getJob().getJobId()).append(' ')
				.append(status.getStatusType()).append(' ').append(status.getResult()).append(" node:").append(status.getNode().getId()).append('\n');
		}
		return buf.toString();
	}

	public int getTotalJobCount()
	{
		return statusMap.size();
	}
	
	public int getFinishedCount()
	{
		return finishedCountAtomic.get();
	}
}
