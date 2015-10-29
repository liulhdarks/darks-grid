package darks.grid;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridNode;
import darks.grid.beans.NodeId;

public class GridNodesManager
{

	private static final Logger log = LoggerFactory.getLogger(GridNodesManager.class);
	
	private Map<String, GridNode> nodesMap = new ConcurrentHashMap<String, GridNode>();
	
	private Map<SocketAddress, String> addressMap = new ConcurrentHashMap<SocketAddress, String>();
	
	private String localNodeId = null;
	
	public synchronized void initialize()
	{
		if (localNodeId == null)
			localNodeId = NodeId.localId();
	}
	
	public synchronized void addLocalNode(Channel channel)
	{
		if (localNodeId == null)
			localNodeId = NodeId.localId();
		GridNode node = new GridNode(localNodeId, channel, true);
		nodesMap.put(localNodeId, node);
		addressMap.put(node.getIpAddress(), localNodeId);
		log.info("Join local node " + localNodeId + " " + node);
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

	public Map<String, GridNode> getNodesMap()
	{
		return nodesMap;
	}

	public Map<SocketAddress, String> getAddressMap()
	{
		return addressMap;
	}
	
	
}
