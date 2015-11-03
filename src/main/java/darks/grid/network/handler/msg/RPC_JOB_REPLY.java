package darks.grid.network.handler.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.executor.task.RpcTask;
import darks.grid.executor.task.rpc.MethodJobReply;

public class RPC_JOB_REPLY implements GridMessageHandler
{

	private static final Logger log = LoggerFactory.getLogger(RPC_JOB_REPLY.class);
	
	@Override
	public void handler(ChannelHandlerContext ctx, GridMessage msg) throws Exception
	{
		MethodJobReply replyBean = (MethodJobReply) msg.getData();
		RpcTask task = (RpcTask)GridRuntime.tasks().getTask(replyBean.getTaskId());
		if (task == null)
		{
			log.error("Cannot find task " + replyBean.getTaskId());
			return;
		}
		task.replyJob(replyBean);
	}

}
