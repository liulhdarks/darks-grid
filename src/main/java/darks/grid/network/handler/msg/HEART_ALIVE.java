package darks.grid.network.handler.msg;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.HeartAliveMeta;

public class HEART_ALIVE implements GridMessageHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(HEART_ALIVE.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(ChannelHandlerContext ctx, GridMessage msg) throws Exception
	{
		HeartAliveMeta meta = msg.getData();
		String nodeId = meta.getNodeId();
		GridNode node = GridRuntime.nodes().getNode(nodeId);
		if (node == null)
		{
			GridRuntime.nodes().addRemoteNode(nodeId, ctx.channel(), meta.context());
		}
		else
		{
			node.setHeartAliveTime(System.currentTimeMillis());
		}
		//HEART ALIVE REPLY
		if (msg.getType() == GridMessage.MSG_HEART_ALIVE)
		{
			boolean valid = true;
			try
			{
				HeartAliveMeta replyMeta = new HeartAliveMeta(GridRuntime.context().getLocalNodeId(), GridRuntime.context());
				GridMessage replyMsg = new GridMessage(replyMeta, GridMessage.MSG_HEART_ALIVE_REPLY, msg);
				ChannelFuture future = ctx.channel().writeAndFlush(replyMsg).sync();
				valid = future.isSuccess();
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
				valid = false;
			}
			if (!valid)
			{
				GridRuntime.nodes().removeNode(node);
			}
		}
	}
}
