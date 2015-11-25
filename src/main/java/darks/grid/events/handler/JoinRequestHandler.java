/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.events.handler;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.beans.meta.JoinNodeMeta;
import darks.grid.events.GridEventHandler;
import darks.grid.network.GridSession;

public class JoinRequestHandler extends GridEventHandler
{

	private static final Logger log = LoggerFactory.getLogger(JoinRequestHandler.class);
	
	@Override
	public void handle(GridEvent event) throws Exception
	{
		GridSession session = event.getSession();
		JoinMeta meta = event.getData();
		meta.setSession(session);
        String nodeId = meta.getNodeId();
        if (nodeId == null)
        {
            log.error("Invalid node id from " + meta);
            return;
        }
		if (!GridRuntime.context().getClusterName().equals(meta.context().getClusterName()))
		{
			log.warn("Refuse " + meta.context().getClusterName() + " cluster request join." + meta);
			meta.getSession().close();
			return;
		}
		synchronized (nodeId.intern())
		{
			GridNode node = GridRuntime.nodes().getNode(nodeId);
			if (node != null)
			{
	            if (node.getSession().getId().equals(session.getId()))
	            {
	                log.warn("Ignore repeat join request from " + node.getId());
	                handleNewChannel(meta, msg, false);
	                return;
	            }
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
				if (localTime < remoteTime)
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
