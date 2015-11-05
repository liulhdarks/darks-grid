package darks.grid.executor.job;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import darks.grid.GridManager;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.GridJob;
import darks.grid.config.GridConfiguration;

public class GridJobManager implements GridManager
{

	private Map<String, Map<String, GridJob>> nodesJobsMap = new ConcurrentHashMap<>();

	private Map<String, Map<String, GridJob>> remoteJobsMap = new ConcurrentHashMap<>();
	
	private Lock lock = new ReentrantLock();
	
	public GridJobManager()
	{
		
	}

	@Override
	public boolean initialize(GridConfiguration config)
	{
		return true;
	}

	@Override
	public void destroy()
	{
		nodesJobsMap.clear();
	}
	
	public void addRemoteJob(GridJob job)
	{
		Map<String, GridJob> jobsMap = getRemoteJobs(job.getTaskId());
		jobsMap.put(job.getJobId(), job);
	}
	
	public void completeRemoteJob(GridJob job)
	{
		Map<String, GridJob> jobsMap = getRemoteJobs(job.getTaskId());
		jobsMap.remove(job.getJobId());
		if (jobsMap.isEmpty())
			remoteJobsMap.remove(job.getTaskId());
	}
	
	public void addNodeJob(GridNode node, GridJob job)
	{
		Map<String, GridJob> jobsMap = getNodeJobs(node.getId());
		jobsMap.put(job.getJobId(), job);
	}
	
	public GridJob removeNodeJob(String nodeId, String jobId)
	{
		Map<String, GridJob> jobsMap = getNodeJobs(nodeId);
		return jobsMap.remove(jobId);
	}
	
	public void removeNodeAllJobs(String nodeId)
	{
		Map<String, GridJob> jobsMap = getNodeJobs(nodeId);
		jobsMap.clear();
	}
	
	public Map<String, GridJob> getNodeJobs(String nodeId)
	{
		Map<String, GridJob> jobsMap = nodesJobsMap.get(nodeId);
		if (jobsMap == null)
		{
			lock.lock();
			try
			{
				jobsMap = nodesJobsMap.get(nodeId);
				if (jobsMap == null)
				{
					jobsMap = new ConcurrentHashMap<>();
					nodesJobsMap.put(nodeId, jobsMap);
				}
			}
			finally
			{
				lock.unlock();
			}
		}
		return jobsMap;
	}
	
	public Map<String, GridJob> getRemoteJobs(String taskId)
	{
		Map<String, GridJob> jobsMap = remoteJobsMap.get(taskId);
		if (jobsMap == null)
		{
			lock.lock();
			try
			{
				jobsMap = remoteJobsMap.get(taskId);
				if (jobsMap == null)
				{
					jobsMap = new ConcurrentHashMap<>();
					remoteJobsMap.put(taskId, jobsMap);
				}
			}
			finally
			{
				lock.unlock();
			}
		}
		return jobsMap;
	}
	
	public String toRemoteJobsString()
	{
		lock.lock();
		try
		{
			StringBuilder buf = new StringBuilder();
			for (Entry<String, Map<String, GridJob>> entry : remoteJobsMap.entrySet())
			{
				buf.append(entry.getKey()).append('\n');
				for (GridJob job : entry.getValue().values())
				{
					buf.append("     ").append(job.getJobId()).append('\n');
				}
			}
			return buf.toString().trim();
		}
		finally
		{
			lock.unlock();
		}
	}
}
