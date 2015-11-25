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

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridNode;
import darks.grid.config.GridConfiguration;
import darks.grid.executor.task.TaskExecutor;
import darks.grid.manager.GridManager;
import darks.grid.utils.GridStatistic;

public class GridJobManager implements GridManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridJobManager.class);

	private Map<String, Map<String, GridJob>> nodesJobsMap = new ConcurrentHashMap<>();

	private Map<String, Map<String, JobExecutor>> execJobsMap = new ConcurrentHashMap<>();
	
	private Lock lock = new ReentrantLock();
	
	private ExecutorService threadPool = null;
	
	public GridJobManager()
	{
		
	}

	@Override
	public boolean initialize(GridConfiguration config)
	{
		threadPool = Executors.newFixedThreadPool(config.getTaskConfig().getJobsExecCount());
		return true;
	}

	@Override
	public void destroy()
	{
		nodesJobsMap.clear();
		threadPool.shutdownNow();
	}
	
	public void addExecuteJob(JobExecutor job)
	{
		GridStatistic.incrementJobCount();
		Map<String, JobExecutor> jobsMap = getExecuteJobs(job.getTaskId());
		jobsMap.put(job.getJobId(), job);
		threadPool.execute(job);
	}
	
	public void completeExecuteJob(JobExecutor job)
	{
		Map<String, JobExecutor> jobsMap = getExecuteJobs(job.getTaskId());
		jobsMap.remove(job.getJobId());
		if (jobsMap.isEmpty())
			execJobsMap.remove(job.getTaskId());
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
		if (jobsMap != null && !jobsMap.isEmpty())
		{
			Set<String> uniqueTask = new HashSet<>();
			for (Entry<String, GridJob> entry : jobsMap.entrySet())
			{
				GridJob job = entry.getValue();
				uniqueTask.add(job.getTaskId());
			}
			log.info("Remove node " + nodeId + " jobs:" + jobsMap.size() + " tasks:" + uniqueTask.size());
			jobsMap.clear();
			for (String taskId : uniqueTask)
			{
				TaskExecutor<?, ?> taskExec = (TaskExecutor<?, ?>)GridRuntime.tasks().getTaskExecutor(taskId);
				if (taskExec != null)
					taskExec.signalStatusCheck();
			}
		}
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
	
	public Map<String, JobExecutor> getExecuteJobs(String taskId)
	{
		Map<String, JobExecutor> jobsMap = execJobsMap.get(taskId);
		if (jobsMap == null)
		{
			lock.lock();
			try
			{
				jobsMap = execJobsMap.get(taskId);
				if (jobsMap == null)
				{
					jobsMap = new ConcurrentHashMap<>();
					execJobsMap.put(taskId, jobsMap);
				}
			}
			finally
			{
				lock.unlock();
			}
		}
		return jobsMap;
	}
	
	public String toExecuteJobsString()
	{
		lock.lock();
		try
		{
			StringBuilder buf = new StringBuilder();
			for (Entry<String, Map<String, JobExecutor>> entry : execJobsMap.entrySet())
			{
				buf.append(entry.getKey()).append('\n');
				for (JobExecutor job : entry.getValue().values())
				{
					buf.append("     ").append(job.toSimpleString()).append('\n');
				}
			}
			return buf.toString().trim();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public int getRunningJobsCount()
	{
		return execJobsMap.size();
	}
}
