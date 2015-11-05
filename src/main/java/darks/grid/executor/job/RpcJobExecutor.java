package darks.grid.executor.job;

import darks.grid.beans.GridMessage;
import darks.grid.beans.meta.GridJob;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.rpc.MethodJob;
import darks.grid.executor.task.rpc.MethodJobReply;
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
		MethodJob jobBean = (MethodJob) job;
		MethodJobReply resp = RpcExecutor.executeMethod(jobBean);
		GridMessage replyMsg = new GridMessage(resp, GridMessage.MSG_RPC_RESPONSE, msg);
		session.sendSyncMessage(replyMsg);
	}

}
