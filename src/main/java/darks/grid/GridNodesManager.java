package darks.grid;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import darks.grid.beans.GridNode;

public class GridNodesManager
{

	private Map<String, GridNode> nodesMap = new ConcurrentHashMap<String, GridNode>();
	
	private Map<SocketAddress, String> addressMap = new ConcurrentHashMap<SocketAddress, String>();
	
	private String localNodeId;
	
	public void addLocalNode(Channel channel)
	{
		GridNode node = new GridNode(channel, true);
		localNodeId = node.getId();
		nodesMap.put(localNodeId, node);
		addressMap.put(node.getChannel().remoteAddress(), localNodeId);
	}
	
	public GridNode getLocalNode()
	{
		if (localNodeId == null)
			return null;
		return nodesMap.get(localNodeId);
	}
	
	public boolean contains(SocketAddress address)
	{
		return addressMap.containsKey(address);
	}
	
	public boolean contains(String nodeId)
	{
		return nodesMap.containsKey(nodeId);
	}
	
	public GridNode getNode(String nodeId)
	{
		return nodesMap.get(nodeId);
	}
	
	public GridNode getNode(InetSocketAddress address)
	{
		String nodeId = addressMap.get(address);
		if (nodeId != null)
			return getNode(nodeId);
		return null;
	}
}
