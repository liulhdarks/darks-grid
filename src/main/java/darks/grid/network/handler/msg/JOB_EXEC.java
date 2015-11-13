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
package darks.grid.network.handler.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobExecutor;
import darks.grid.executor.job.RpcJobExecutor;
import darks.grid.executor.task.GridTask.TaskType;
import darks.grid.network.GridSession;

public class JOB_EXEC implements GridMessageHandler
{

	private static final Logger log = LoggerFactory.getLogger(JOB_EXEC.class);
	
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
	    GridJob job = msg.getData();
		JobExecutor executor = null;
		if (job.getTaskType() == TaskType.RPC)
		{
		    executor = new RpcJobExecutor(session, msg);
		}
		else
		{
		    log.error("Invalid task type " + job.getTaskType());
		}
		GridRuntime.jobs().addExecuteJob(executor);
	}

}
