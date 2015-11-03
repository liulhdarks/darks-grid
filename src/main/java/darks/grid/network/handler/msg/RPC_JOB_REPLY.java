package darks.grid.network.handler.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.executor.task.GridRpcTask;
import darks.grid.executor.task.rpc.MethodJobReply;
import darks.grid.network.GridSession;

public class RPC_JOB_REPLY implements GridMessageHandler
{

	private static final Logger log = LoggerFactory.getLogger(RPC_JOB_REPLY.class);
	
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		MethodJobReply replyBean = (MethodJobReply) msg.getData();
		GridRpcTask task = (GridRpcTask)GridRuntime.tasks().getTask(replyBean.getTaskId());
		if (task == null)
		{
			log.error("Cannot find task " + replyBean.getTaskId());
			return;
		}
		task.replyJob(replyBean);
	}

}
