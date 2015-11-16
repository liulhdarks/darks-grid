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

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import darks.grid.GridRuntime;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.config.GridConfiguration;
import darks.grid.manager.GridManager;
import darks.grid.utils.SyncPool;
import darks.grid.utils.ThreadUtils;

public class GridNetworkManager implements GridManager
{
	
	private GridMessageServer messageServer;
	
	private Map<String, Map<SocketAddress, JoinMeta>> waitJoin = new ConcurrentHashMap<String, Map<SocketAddress, JoinMeta>>();
	
	private Object mutex = new Object();
	
	private ThreadLocal<GridMessageClient> clientLocal = new ThreadLocal<>();
	
	public GridNetworkManager()
	{
		
	}
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
		messageServer = GridNetworkBuilder.buildMessageServer(config);
		if (messageServer == null)
			return false;
		GridRuntime.context().setServerAddress(getBindAddress());
		GridRuntime.nodes().addLocalNode(GridSessionFactory.getLocalSession(messageServer.getChannel()));
		return true;
	}

	@Override
	public void destroy()
	{
		messageServer.destroy();
	}
	
	public boolean tryJoinAddress(InetSocketAddress address)
	{
		Lock lock = SyncPool.lock(address);
		if (!lock.tryLock())
			return false;
		try
		{
			if (GridRuntime.nodes().contains(address))
				return true;
			GridMessageClient client = clientLocal.get();
			if (client == null)
			{
				client = new GridMessageClient(ThreadUtils.getThrealPool());
				client.initialize();
				clientLocal.set(client);
			}
			return client.connect(address) != null;
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
			Map<SocketAddress, JoinMeta> channelMap = waitJoin.get(nodeId);
			if (channelMap == null)
			{
				channelMap = new ConcurrentHashMap<>();
				waitJoin.put(nodeId, channelMap);
			}
			meta.setJoinTime(System.currentTimeMillis());
			channelMap.put(meta.getSession().remoteAddress(), meta);
			return channelMap.size();
		}
	}
	
	public synchronized Map<SocketAddress, JoinMeta> getWaitJoin(String nodeId)
	{
		synchronized (mutex)
		{
			Map<SocketAddress, JoinMeta> channelMap = waitJoin.get(nodeId);
			if (channelMap == null)
			{
				channelMap = new ConcurrentHashMap<>();
				waitJoin.put(nodeId, channelMap);
			}
			return channelMap;
		}
	}
	
	public InetSocketAddress getBindAddress()
	{
		if (messageServer == null)
			return null;
		return messageServer.getAddress();
	}
	
	
	public Channel getServerChannel()
	{
		if (messageServer == null)
			return null;
		return messageServer.getChannel();
	}
}
