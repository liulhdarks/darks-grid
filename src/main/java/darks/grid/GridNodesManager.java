package darks.grid;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridNode;
import darks.grid.beans.NodeId;
import darks.grid.network.NodesMonitorThread;
import darks.grid.utils.ThreadUtils;

public class GridNodesManager
{

	private static final Logger log = LoggerFactory.getLogger(GridNodesManager.class);
	
	private Map<String, GridNode> nodesMap = new ConcurrentHashMap<String, GridNode>();
	
	private Map<SocketAddress, String> addressMap = new ConcurrentHashMap<SocketAddress, String>();
	
	private NodesMonitorThread monitorThread;
	
	public synchronized void initialize(GridConfiguration config)
	{
		monitorThread = new NodesMonitorThread();
		ThreadUtils.executeThread(monitorThread);
	}
	
	public synchronized void destroy()
	{
		monitorThread.setStoped(true);
		monitorThread.interrupt();
	}
	
	public synchronized void addLocalNode(Channel channel)
	{
		String localNodeId = NodeId.localId();
		GridContext.getRuntime().setLocalNodeId(localNodeId);
		GridNode node = new GridNode(localNodeId, channel, true);
		nodesMap.put(localNodeId, node);
		addressMap.put(node.getIpAddress(), localNodeId);
		log.info("Join local node " + node.toSimpleString());
	}
	
	public synchronized void addRemoteNode(String nodeId, Channel channel)
	{
		GridNode oldNode = nodesMap.get(nodeId);
		if (oldNode != null)
		{
			if (oldNode.getChannel().id().toString().equals(channel.id().toString()))
				return;
			if (oldNode.getChannel().isActive())
			{
				channel.close();
				//TODO re-add node to remote
				return;
			}
			else
			{
				oldNode.getChannel().close();
				nodesMap.remove(nodeId);
			}
		}
		GridNode node = new GridNode(nodeId, channel, false);
		nodesMap.put(nodeId, node);
		addressMap.put(node.getIpAddress(), nodeId);
		log.info("Join remote node " + node.toSimpleString());
	}
	
	public GridNode getLocalNode()
	{
		String localNodeId = GridContext.getRuntime().getLocalNodeId();
		if (localNodeId == null)
			return null;
		return nodesMap.get(localNodeId);
	}
	
	public String getNodesInfo()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("===================================================\n");
		for (Entry<String, GridNode> entry : nodesMap.entrySet())
		{
			buf.append(entry.getValue().toSimpleString()).append('\n');
		}
		buf.append("---------------------------------------------------\n");
		return buf.toString();
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
	
	public void removeNode(String nodeId)
	{
		nodesMap.remove(nodeId);
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
