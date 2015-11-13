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

import darks.grid.beans.GridMessage;
import darks.grid.executor.task.GridJobReply;
import darks.grid.executor.task.rpc.GridRpcJob;
import darks.grid.network.GridSession;

public class RpcJobExecutor extends JobExecutor
{

	public RpcJobExecutor(GridSession session, GridMessage msg)
	{
		super(session, msg);
	}

	@Override
	public void execute(GridSession session, GridMessage msg, GridJob job) throws Exception
	{
		GridRpcJob jobBean = (GridRpcJob) job;
		GridJobReply resp = (GridJobReply) jobBean.execute();
		if (jobBean.isCallback())
		{
	        GridMessage replyMsg = new GridMessage(resp, GridMessage.MSG_MR_RESPONSE, msg);
	        session.sendSyncMessage(replyMsg);
		}
	}

}
