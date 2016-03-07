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
package darks.grid.network;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridAddress;
import darks.grid.beans.GridNode;
import darks.grid.beans.TimerObject;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.config.GridConfiguration;
import darks.grid.manager.GridManager;
import darks.grid.utils.SyncPool;

public class GridNetworkManager implements GridManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridNetworkManager.class);
	
	private static final long LOCK_EXPIRE_TIME = 60000;
	
	private static final long ACTIVE_EXPIRE_TIME = 120000;
	
	private GridMessageServer messageServer;
	
	private GridMessageClient messageClient;
	
	private Map<GridAddress, TimerObject<GridSession>> waitActive = new ConcurrentHashMap<GridAddress, TimerObject<GridSession>>();
	
	private Map<String, Map<GridAddress, JoinMeta>> waitJoin = new ConcurrentHashMap<String, Map<GridAddress, JoinMeta>>();
	
	private Object mutex = new Object();
	
	private GridSession serverSession;
	
	public GridNetworkManager()
	{
		
	}
	
	@Override
	public synchronized boolean initialize(GridConfiguration config)
	{
		messageServer = GridNetworkBuilder.buildMessageServer(config);
		if (messageServer == null)
			return false;
		if (bindLocalNode())
		{
			messageClient = GridNetworkBuilder.buildMessageClient(config);
			return messageClient != null;
		}
		else
		{
			return false;
		}
	}
	
	public synchronized boolean bindLocalNode()
	{
		if (messageServer == null)
			return false;
		serverSession = GridNetworkBuilder.listenServer(messageServer);
		if (serverSession != null)
		{
			GridRuntime.context().setServerAddress(getBindAddress());
			GridRuntime.nodes().addLocalNode(serverSession);
			return true;
		}
		else
			return false;
	}

	@Override
	public void destroy()
	{
		messageClient.destroy();
		messageServer.destroy();
	}
	
	public void sendMessageToAll(Object obj)
	{
		List<GridNode> nodes = GridRuntime.nodes().getNodesList();
		for (GridNode node : nodes)
		{
			if (node.isAlive())
				node.sendSyncMessage(obj);
		}
	}
	
	public void sendMessageToOthers(Object obj)
	{
		List<GridNode> nodes = GridRuntime.nodes().getNodesList();
		for (GridNode node : nodes)
		{
			if (!node.isLocal() && node.isAlive())
				node.sendSyncMessage(obj);
		}
	}
    
    public boolean tryJoinAddress(GridAddress address)
    {
        return tryJoinAddress(address, true);
    }
	
	public boolean tryJoinAddress(InetSocketAddress address)
	{
		return tryJoinAddress(new GridAddress(address), true);
	}
	
	public boolean tryJoinAddress(GridAddress address, boolean sync)
	{
		if (address == null)
		{
			log.error("Address is null when trying to join.");
			return false;
		}
		Lock lock = SyncPool.lock(address, LOCK_EXPIRE_TIME);
		if (!lock.tryLock())
		{
			log.error("Address " + address + " locked when trying to join.");
			return false;
		}
		try
		{
			if (GridRuntime.nodes().contains(address))
			{
				log.warn("Address " + address + " has joined when trying to join.");
				return true;
			}
			if (messageClient == null)
			{
				log.error("Message client is null when trying to join.");
				return false;
			}
			return messageClient.connect(address, sync) != null;
		}
		finally
		{
			lock.unlock();
		}
		
	}

	public int addWaitJoin(String nodeId, JoinMeta meta)
	{
		synchronized (mutex)
		{
			Map<GridAddress, JoinMeta> channelMap = waitJoin.get(nodeId);
			if (channelMap == null)
			{
				channelMap = new ConcurrentHashMap<GridAddress, JoinMeta>();
				waitJoin.put(nodeId, channelMap);
			}
			meta.setJoinTime(System.currentTimeMillis());
			channelMap.put(meta.getSession().remoteAddress(), meta);
			return channelMap.size();
		}
	}
	
	public void addWaitActive(GridSession session)
	{
		waitActive.put(session.remoteAddress(), new TimerObject<GridSession>(session, ACTIVE_EXPIRE_TIME));
	}
	
	public void removeWaitActive(GridSession session)
	{
		waitActive.remove(session.remoteAddress());
	}
	
	public synchronized Map<GridAddress, JoinMeta> getWaitJoin(String nodeId)
	{
		synchronized (mutex)
		{
			Map<GridAddress, JoinMeta> channelMap = waitJoin.get(nodeId);
			if (channelMap == null)
			{
				channelMap = new ConcurrentHashMap<GridAddress, JoinMeta>();
				waitJoin.put(nodeId, channelMap);
			}
			return channelMap;
		}
	}
	
	public synchronized GridAddress getBindAddress()
	{
		if (messageServer == null || serverSession == null)
			return null;
		return serverSession.localAddress();
	}
	
	
	public GridSession getServerSession()
	{
		return serverSession;
	}
}
