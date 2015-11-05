package darks.grid.network.handler.msg;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.executor.job.JobExecutor;
import darks.grid.executor.job.RpcJobExecutor;
import darks.grid.network.GridSession;

public class RPC_JOB implements GridMessageHandler
{

//	private static final Logger log = LoggerFactory.getLogger(RPC_JOB.class);
	
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		JobExecutor executor = new RpcJobExecutor(session, msg);
		GridRuntime.jobs().addExecuteJob(executor);
	}

}
