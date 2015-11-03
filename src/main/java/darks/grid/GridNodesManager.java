package darks.grid;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridNode;
import darks.grid.beans.GridNodeType;
import darks.grid.beans.NodeId;
import darks.grid.config.GridConfiguration;
import darks.grid.utils.MachineUtils;

public class GridNodesManager
{

	private static final Logger log = LoggerFactory.getLogger(GridNodesManager.class);
	
	private Map<String, GridNode> nodesMap = new ConcurrentHashMap<String, GridNode>();
	
	private Map<SocketAddress, String> addressMap = new ConcurrentHashMap<SocketAddress, String>();
	
	private Map<ChannelId, String> channelIdMap = new ConcurrentHashMap<ChannelId, String>();
	
	public synchronized boolean initialize(GridConfiguration config)
	{
		return true;
	}
	
	public synchronized void destroy()
	{
	}
	
	public synchronized void addLocalNode(Channel channel)
	{
		String localNodeId = NodeId.localId();
		GridRuntime.context().setLocalNodeId(localNodeId);
		GridNode node = new GridNode(localNodeId, channel, GridRuntime.context(), GridNodeType.TYPE_LOCAL);
		nodesMap.put(localNodeId, node);
		addressMap.put(node.context().getServerAddress(), localNodeId);
		channelIdMap.put(channel.id(), localNodeId);
		log.info("Join local node " + node.toSimpleString());
	}
	
	public synchronized void addRemoteNode(String nodeId, Channel channel, GridContext context)
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
		GridNode node = new GridNode(nodeId, channel, context, GridNodeType.TYPE_REMOTE);
		nodesMap.put(nodeId, node);
		addressMap.put(node.context().getServerAddress(), nodeId);
		channelIdMap.put(channel.id(), nodeId);
		log.info("Join remote node " + node.toSimpleString());
	}
	
	public GridNode getLocalNode()
	{
		String localNodeId = GridRuntime.context().getLocalNodeId();
		if (localNodeId == null)
			return null;
		return nodesMap.get(localNodeId);
	}
	
	public String getNodesInfo()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("\n===========================================================\n");
		for (Entry<String, GridNode> entry : nodesMap.entrySet())
		{
			buf.append(entry.getValue().toSimpleString()).append('\n');
		}
		buf.append("-------------------------------------------------------------\n");
		buf.append("Direct Memory:").append(MachineUtils.getReservedDirectMemory())
				.append('/').append(MachineUtils.getMaxDirectMemory()).append('\n');
		buf.append("=============================================================\n");
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
	
	public synchronized GridNode removeNode(String nodeId)
	{
		GridNode node = nodesMap.remove(nodeId);
		if (node !=null)
		{
			addressMap.remove(node.context().getServerAddress());
			channelIdMap.remove(node.getChannel().id());
		}
		return node;
	}
	
	public synchronized GridNode removeNode(GridNode node)
	{
		GridNode rNode = GridRuntime.nodes().removeNode(node.getId());
		if (rNode == null)
		{
			addressMap.remove(node.context().getServerAddress());
			channelIdMap.remove(node.getChannel().id());
			rNode = node;
		}
		if (rNode != null)
		{
			log.info("Grid node " + rNode.getId() + " " + rNode.context().getServerAddress() + " quit.");
			rNode.getChannel().close();
		}
		return rNode;
	}
	
	public synchronized GridNode removeNode(Channel channel)
	{
		GridNode node = null;
		String nodeId = GridRuntime.nodes().getNodeId(channel.id());
		if (nodeId != null)
		{
			node = GridRuntime.nodes().removeNode(nodeId);
			if (node != null)
			{
				log.info("Grid node " + node.getId() + " " + node.context().getServerAddress() + " quit.");
				node.getChannel().close();
			}
		}
		return node;
	}
	
	public GridNode getNode(InetSocketAddress address)
	{
		String nodeId = addressMap.get(address);
		if (nodeId != null)
			return getNode(nodeId);
		return null;
	}
	
	public String getNodeId(InetSocketAddress address)
	{
		return addressMap.get(address);
	}
	
	public String getNodeId(ChannelId channelId)
	{
		return channelIdMap.get(channelId);
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
