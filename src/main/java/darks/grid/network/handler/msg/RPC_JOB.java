package darks.grid.network.handler.msg;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import darks.grid.beans.GridMessage;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.rpc.MethodJob;
import darks.grid.executor.task.rpc.MethodJobReply;

public class RPC_JOB implements GridMessageHandler
{

	@Override
	public void handler(ChannelHandlerContext ctx, GridMessage msg) throws Exception
	{
		MethodJob jobBean = (MethodJob) msg.getData();
		MethodJobReply resp = RpcExecutor.executeMethod(jobBean);
		GridMessage replyMsg = new GridMessage(resp, GridMessage.MSG_RPC_RESPONSE, msg);
		for (int i = 0; i < 3; i++)
		{
			ChannelFuture future = ctx.channel().writeAndFlush(replyMsg).sync();
			if (future.isSuccess())
				break;
		}
	}

}
