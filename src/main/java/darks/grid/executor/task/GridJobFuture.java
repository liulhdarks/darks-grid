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

import darks.grid.beans.JobStatus;
import darks.grid.beans.JobStatus.JobStatusType;
import darks.grid.executor.task.rpc.MethodJobReply;
import darks.grid.utils.ThreadUtils;

public class GridJobFuture<V> extends GridFuture<V>
{
    
	private static final Logger log = LoggerFactory.getLogger(GridJobFuture.class);
	
    protected Map<String, JobStatus> statusMap = new ConcurrentHashMap<>();
    
    private ReadWriteLock wrlock = new ReentrantReadWriteLock();
    
    private Lock rlock = wrlock.readLock();
    
    private Lock wlock = wrlock.writeLock();
    
    private GridTask<?> task;

    public GridJobFuture(GridTask<?> task)
    {
    	this.task = task;
    }
    
    public void addJobStatus(JobStatus status)
    {
    	status.setStatusType(JobStatusType.WAITING);
    	statusMap.put(status.getJob().getJobId(), status);
    }
    
    public void removeJobStatus(String jobId)
    {
    	statusMap.remove(jobId);
    }
    
    public void replyStatus(MethodJobReply reply)
    {
    	wlock.lock();
    	try
		{
    		String jobId = reply.getJobId();
        	JobStatus status = statusMap.get(jobId);
        	if (status == null)
        	{
        		log.error("Cannot find job " + jobId);
        		return;
        	}
        	status.setResult(reply.getResult());
        	if (reply.isSuccess())
        		status.setStatusType(JobStatusType.SUCCESS);
        	else
        	{
        		status.setStatusType(JobStatusType.FAIL);
        		status.setErrorCode(reply.getErrorCode());
        		status.setErrorMessage(reply.getErrorMessage());
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
			for (Entry<String, JobStatus> entry : statusMap.entrySet())
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
		// TODO Auto-generated method stub
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
		List<JobStatus> statuses = new ArrayList<>(statusMap.size());
		for (;;)
		{
			int finishedCount = 0;
			statuses.clear();
			statuses.addAll(statusMap.values());
			for (JobStatus status : statuses)
			{
				if (!status.getNode().isAlive())
				{
					log.info("Remove job " + status.getJob().getJobId() + ". failed in node " + status.getNode().getId());
					statusMap.remove(status.getJob().getJobId());
					if (status.getJob().isFailRedo())
					{
						task.failRedo(status.getJob());
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
			ThreadUtils.threadSleep(50);
			if (maxTime > 0 && System.currentTimeMillis() - st > maxTime)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public List<V> getList()
	{
		List<V> result = new ArrayList<>(statusMap.size());
		for (Entry<String, JobStatus> entry : statusMap.entrySet())
		{
			result.add((V) entry.getValue().getResult());
		}
		return result;
	}
	
	public String toSimpleString(String linePrefix)
	{
		StringBuilder buf = new StringBuilder();
		for (JobStatus status : statusMap.values())
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
