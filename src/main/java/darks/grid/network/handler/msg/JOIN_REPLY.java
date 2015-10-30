package darks.grid.network.handler.msg;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.beans.meta.JoinNodeMeta;

public class JOIN_REPLY implements GridMessageHandler
{
	private static final Logger log = LoggerFactory.getLogger(JOIN_REPLY.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(ChannelHandlerContext ctx, GridMessage msg) throws Exception
	{
		JoinNodeMeta meta = msg.getData();
		String nodeId = meta.getNodeId();
		synchronized (nodeId.intern())
		{
			Map<SocketAddress, JoinMeta> nodesMap = GridRuntime.network().getWaitJoin(nodeId);
			for (Entry<SocketAddress, JoinMeta> entry : nodesMap.entrySet())
			{
				try
				{
					Channel channel = entry.getValue().getChannel();
					if (channel.id().toString().equals(ctx.channel().id().toString()))
						continue;
					channel.close();
				}
				catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
			}
			nodesMap.clear();
			GridRuntime.nodes().addRemoteNode(meta.getNodeId(), ctx.channel(), meta.context());
		}
	}
}
