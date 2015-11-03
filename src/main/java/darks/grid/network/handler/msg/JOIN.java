package darks.grid.network.handler.msg;

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
import darks.grid.network.GridSession;

public class JOIN implements GridMessageHandler
{
	private static final Logger log = LoggerFactory.getLogger(JOIN.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		JoinMeta meta = msg.getData();
		meta.setSession(session);
		if (!GridRuntime.context().getClusterName().equals(meta.context().getClusterName()))
		{
			log.info("Refuse " + meta.context().getClusterName() + " cluster request join." + meta);
			meta.getSession().close();
			return;
		}
		String nodeId = meta.getNodeId();
		synchronized (nodeId)
		{
			GridNode node = GridRuntime.nodes().getNode(nodeId);
			if (node != null)
			{
				if (node.getSession().isActive())
				{
					meta.getSession().close();
					return;
				}
				else
					GridRuntime.nodes().removeNode(nodeId);
			}
			int count = GridRuntime.network().addWaitJoin(nodeId, meta);
			if (count > 1)
			{
				long localTime = GridRuntime.context().getStartupTime();
				long remoteTime = meta.context().getStartupTime();
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
		Map<SocketAddress, JoinMeta> nodesMap = GridRuntime.network().getWaitJoin(nodeId);
		long keepJoinTime = 0;
		JoinMeta keepJoinMeta = null;
		for (Entry<SocketAddress, JoinMeta> entry : nodesMap.entrySet())
		{
			JoinMeta joinMeta = entry.getValue();
			if (keepJoinMeta == null || joinMeta.getJoinTime() < keepJoinTime)
			{
				if (keepJoinMeta != null)
				{
					keepJoinMeta.getSession().close();
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
		JoinNodeMeta data = new JoinNodeMeta(GridRuntime.getLocalId(), GridRuntime.context());
		GridMessage replyMsg = new GridMessage(data, GridMessage.MSG_JOIN_REPLY, msg);
		try
		{
			boolean success = meta.getSession().sendSyncMessage(replyMsg);
			if (autoJoin)
			{
				if (success)
					GridRuntime.nodes().addRemoteNode(nodeId, meta.getSession(), meta.context());
				Map<SocketAddress, JoinMeta> nodesMap = GridRuntime.network().getWaitJoin(nodeId);
				nodesMap.clear();
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
}
