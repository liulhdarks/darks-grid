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
import darks.grid.executor.task.GridJobReply;
import darks.grid.executor.task.TaskExecutor;
import darks.grid.network.GridSession;

public class JOB_EXEC_REPLY implements GridMessageHandler
{

	private static final Logger log = LoggerFactory.getLogger(JOB_EXEC_REPLY.class);
	
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		GridJobReply replyBean = (GridJobReply) msg.getData();
		TaskExecutor<?, ?> taskExec = (TaskExecutor<?, ?>)GridRuntime.tasks().getTaskExecutor(replyBean.getTaskId());
		if (taskExec == null)
		{
			log.error("Cannot find task executor " + replyBean.getTaskId());
			return;
		}
		taskExec.replyJob(replyBean);
	}

}
