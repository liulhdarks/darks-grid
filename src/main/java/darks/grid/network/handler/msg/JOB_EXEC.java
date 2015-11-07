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
