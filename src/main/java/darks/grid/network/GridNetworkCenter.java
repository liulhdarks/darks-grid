package darks.grid.network;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import darks.grid.GridConfiguration;
import darks.grid.GridRuntime;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.network.discovery.DiscoveryThread;
import darks.grid.network.discovery.GridDiscovery;
import darks.grid.network.discovery.TCPPING;
import darks.grid.utils.ThreadUtils;

public class GridNetworkCenter
{

	
	private LinkedList<GridDiscovery> discoveries = new LinkedList<GridDiscovery>();
	
	private GridMessageServer messageServer;
	
	private Map<String, Map<SocketAddress, JoinMeta>> waitJoin = new ConcurrentHashMap<String, Map<SocketAddress, JoinMeta>>();
	
	private Object mutex = new Object();
	
	private DiscoveryThread discoveryThread;
	
	public GridNetworkCenter()
	{
		
	}
	
	public boolean initialize(GridConfiguration config)
	{
		messageServer = GridNetworkBuilder.buildMessageServer(config);
		if (messageServer == null)
			return false;
		GridRuntime.context().setServerAddress(getBindAddress());
		GridRuntime.nodes().addLocalNode(messageServer.getChannel());
		discoveries.add(new TCPPING());
		for (GridDiscovery discovery : discoveries)
		{
			discovery.setConfig(new HashMap<String, String>());
		}
		discoveryThread = new DiscoveryThread();
		ThreadUtils.executeThread(discoveryThread);
		return true;
	}
	
	public void destroy()
	{
		discoveryThread.setStoped(true);
		discoveryThread.interrupt();
		messageServer.destroy();
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
			channelMap.put(meta.getChannel().remoteAddress(), meta);
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

	public LinkedList<GridDiscovery> getDiscoveries()
	{
		return discoveries;
	}
	
}
