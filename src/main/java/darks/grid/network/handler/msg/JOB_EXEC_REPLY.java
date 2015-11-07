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
