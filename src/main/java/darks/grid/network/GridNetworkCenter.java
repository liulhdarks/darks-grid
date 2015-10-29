package darks.grid.network;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import darks.grid.GridContext;
import darks.grid.network.discovery.GridDiscovery;

public class GridNetworkCenter
{

	
	private static LinkedList<GridDiscovery> discoveries = new LinkedList<GridDiscovery>();
	
	private GridMessageServer messageServer;
	
	private Map<String, Channel> waitChannel = new ConcurrentHashMap<String, Channel>();
	
	public GridNetworkCenter()
	{
		
	}
	
	public boolean initialize()
	{
		messageServer = GridNetworkBuilder.buildMessageServer(GridContext.getConfig());
		if (messageServer == null)
			return false;
		GridContext.getNodesManager().addLocalNode(messageServer.getChannel());
		return true;
	}

	public synchronized void addWaitChannel(Channel channel)
	{
		String nodeId = channel.id().asShortText();
		if (!waitChannel.containsKey(nodeId))
			waitChannel.put(nodeId, channel);
		else
			channel.close();
	}
	
	public Map<String, Channel> getWaitChannel()
	{
		return waitChannel;
	}
	
	public InetSocketAddress getBindAddress()
	{
		if (messageServer == null)
			return null;
		return (InetSocketAddress) messageServer.getAddress();
	}
}
