package darks.grid.executor.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.executor.job.GridJobStatus;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.job.JobStatusType;
import darks.grid.executor.task.mapred.MapReduceTask;
import darks.grid.utils.ThreadUtils;

public class GridJobFuture extends GridFuture<JobResult>
{
    
	private static final Logger log = LoggerFactory.getLogger(GridJobFuture.class);
	
    protected Map<String, GridJobStatus> statusMap = new ConcurrentHashMap<>();
    
    private ReadWriteLock wrlock = new ReentrantReadWriteLock();
    
    private Lock rlock = wrlock.readLock();
    
    private Lock wlock = wrlock.writeLock();
    
    private TaskExecutor<?, ?> executor;

    public GridJobFuture(TaskExecutor<?, ?> executor)
    {
    	this.executor = executor;
    }
    
    public void addJobStatus(GridJobStatus status)
    {
    	status.setStatusType(JobStatusType.WAITING);
    	statusMap.put(status.getJob().getJobId(), status);
    }
    
    public void removeJobStatus(String jobId)
    {
    	statusMap.remove(jobId);
    }
    
    public void replyStatus(GridJobReply reply)
    {
    	wlock.lock();
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
        	if (reply.isSuccess())
        		status.setStatusType(JobStatusType.SUCCESS);
        	else
        	{
        		status.setStatusType(JobStatusType.FAIL);
        		status.getResult().setErrorCode(reply.getErrorCode());
        		status.getResult().setErrorMessage(reply.getErrorMessage());
        	}
		}
		finally
		{
			wlock.unlock();
		}
    }
    
	@Override
	public boolean isSuccess()
	{
		boolean success = true;
		rlock.lock();
		try
		{
			for (Entry<String, GridJobStatus> entry : statusMap.entrySet())
			{
				success = success && entry.getValue().getStatusType() == JobStatusType.SUCCESS;
			}
		}
		finally
		{
			rlock.unlock();
		}
		return success;
	}

	@Override
	public boolean isCanceled()
	{
		return false;
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
		List<GridJobStatus> statuses = new ArrayList<>(statusMap.size());
		for (;;)
		{
			int finishedCount = 0;
			statuses.clear();
			statuses.addAll(statusMap.values());
			for (GridJobStatus status : statuses)
			{
				if (!status.getNode().isAlive())
				{
					log.info("Remove job " + status.getJob().getJobId() + ". failed in node " + status.getNode().getId());
					statusMap.remove(status.getJob().getJobId());
					if (status.getJob().isFailRedo())
					{
					    executor.failRedo(status.getJob());
					}
					continue;
				}
				rlock.lock();
				try
				{
					if (status.getStatusType() == JobStatusType.SUCCESS
							|| status.getStatusType() == JobStatusType.FAIL)
					{
						finishedCount++;
					}
				}
				finally
				{
					rlock.unlock();
				}
			}
			if (finishedCount == statusMap.size())
				break;
			ThreadUtils.threadSleep(20);
			if (maxTime > 0 && System.currentTimeMillis() - st > maxTime)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public List<JobResult> getList()
	{
		List<JobResult> result = new ArrayList<>(statusMap.size());
		for (Entry<String, GridJobStatus> entry : statusMap.entrySet())
		{
		    GridJobStatus status = entry.getValue();
		    JobResult ret = status.getResult();
		    ret.setSuccess(status.getStatusType() == JobStatusType.SUCCESS);
			result.add(ret);
		}
		return result;
	}
	
	public String toSimpleString(String linePrefix)
	{
		StringBuilder buf = new StringBuilder();
		for (GridJobStatus status : statusMap.values())
		{
			rlock.lock();
			try
			{
				buf.append(linePrefix).append(status.getJob().getJobId()).append(' ')
					.append(status.getStatusType()).append(' ').append(status.getResult()).append(" node:").append(status.getNode().getId()).append('\n');
			}
			finally
			{
				rlock.unlock();
			}
		}
		return buf.toString();
	}

}
