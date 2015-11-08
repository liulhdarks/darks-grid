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
