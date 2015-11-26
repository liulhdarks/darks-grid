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

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridComponent;

public class JobExecuteGuard extends GridComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1453116589685932315L;
	
	private static final Logger log = LoggerFactory.getLogger(JobExecuteGuard.class);

	public JobExecuteGuard()
	{
		setDaemon(true);
		setInterval(30000);
	}

	@Override
	protected void execute() throws Exception
	{
		log.info("Job execute guard check job status.");
		Map<String, Map<String, JobExecutor>> tasks = GridRuntime.jobs().getExecJobsMap();
		for (Entry<String, Map<String, JobExecutor>> entry : tasks.entrySet())
		{
			for (JobExecutor job : entry.getValue().values())
			{
				if (!job.isCanncel() && (job.isTimeout() || !job.getSession().isActive()))
					job.cancel();
			}
		}
	}

}
