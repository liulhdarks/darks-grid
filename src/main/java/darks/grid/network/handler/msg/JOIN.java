package darks.grid.network.handler.msg;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.beans.meta.JoinNodeMeta;

public class JOIN implements GridMessageHandler
{
	private static final Logger log = LoggerFactory.getLogger(JOIN.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(ChannelHandlerContext ctx, GridMessage msg) throws Exception
	{
		JoinMeta meta = msg.getData();
		meta.setChannel(ctx.channel());
		if (!GridRuntime.context().getClusterName().equals(meta.getClusterName()))
		{
			log.info("Refuse " + meta.getClusterName() + " cluster request join." + meta);
			meta.getChannel().close();
			return;
		}
		String nodeId = meta.getNodeId();
		synchronized (nodeId)
		{
			GridNode node = GridRuntime.getNodesManager().getNode(nodeId);
			if (node != null)
			{
				if (node.getChannel().isActive())
				{
					meta.getChannel().close();
					return;
				}
				else
					GridRuntime.getNodesManager().removeNode(nodeId);
			}
			int count = GridRuntime.getNetwork().addWaitJoin(nodeId, meta);
			if (count > 1)
			{
				long localTime = GridRuntime.context().getStartupTime();
				long remoteTime = meta.getStartupTime();
				if (localTime > remoteTime)
				{
					log.warn("Channel from " + nodeId + " has repeat " + count + " connections.Deal by local.");
					handleRepeatChannel(meta, msg);
				}
				else
				{
					log.warn("Channel from " + nodeId + " has repeat " + count + " connections.Deal by remote.");
				}
			}
			else if (count == 1)
			{
				handleNewChannel(meta, msg, false);
			}
		}
	}

	private void handleRepeatChannel(JoinMeta meta, GridMessage msg)
	{
		String nodeId = meta.getNodeId();
		Map<SocketAddress, JoinMeta> nodesMap = GridRuntime.getNetwork().getWaitJoin(nodeId);
		long keepJoinTime = 0;
		JoinMeta keepJoinMeta = null;
		for (Entry<SocketAddress, JoinMeta> entry : nodesMap.entrySet())
		{
			JoinMeta joinMeta = entry.getValue();
			if (keepJoinMeta == null || joinMeta.getJoinTime() < keepJoinTime)
			{
				if (keepJoinMeta != null)
				{
					keepJoinMeta.getChannel().close();
				}
				keepJoinMeta = joinMeta;
				keepJoinTime = joinMeta.getJoinTime();
			}
		}
		if (keepJoinMeta != null)
		{
			handleNewChannel(keepJoinMeta, msg, true);
		}
		nodesMap.clear();
	}
	
	private void handleNewChannel(JoinMeta meta, GridMessage msg, boolean autoJoin)
	{
		String nodeId = meta.getNodeId();
		JoinNodeMeta data = new JoinNodeMeta(GridRuntime.getLocalId(), GridRuntime.context().getStartupTime());
		GridMessage replyMsg = new GridMessage(data, GridMessage.MSG_JOIN_REPLY, msg);
		try
		{
			ChannelFuture future = meta.getChannel().writeAndFlush(replyMsg).sync();
			if (autoJoin)
			{
				if (future.isSuccess())
					GridRuntime.getNodesManager().addRemoteNode(nodeId, meta.getChannel());
				Map<SocketAddress, JoinMeta> nodesMap = GridRuntime.getNetwork().getWaitJoin(nodeId);
				nodesMap.clear();
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
}
