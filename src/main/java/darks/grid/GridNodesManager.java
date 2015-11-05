package darks.grid;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.beans.GridNodeType;
import darks.grid.beans.NodeId;
import darks.grid.config.GridConfiguration;
import darks.grid.network.GridSession;
import darks.grid.utils.MachineUtils;

public class GridNodesManager implements GridManager
{

	private static final Logger log = LoggerFactory.getLogger(GridNodesManager.class);
	
	private Map<String, GridNode> nodesMap = new ConcurrentHashMap<String, GridNode>();
	
	private Map<SocketAddress, String> addressMap = new ConcurrentHashMap<SocketAddress, String>();
	
	private Map<String, String> sessionIdMap = new ConcurrentHashMap<String, String>();
	
	@Override
	public synchronized boolean initialize(GridConfiguration config)
	{
		return true;
	}

	@Override
	public synchronized void destroy()
	{
	}
	
	public synchronized void addLocalNode(GridSession session)
	{
		String localNodeId = NodeId.localId();
		GridRuntime.context().setLocalNodeId(localNodeId);
		GridNode node = new GridNode(localNodeId, session, GridRuntime.context(), GridNodeType.TYPE_LOCAL);
		nodesMap.put(localNodeId, node);
		addressMap.put(node.context().getServerAddress(), localNodeId);
		sessionIdMap.put(session.getId(), localNodeId);
		GridRuntime.events().publish(GridEvent.NODE_JOIN, node);
		log.info("Join local node " + node.toSimpleString());
	}
	
	public synchronized void addRemoteNode(String nodeId, GridSession session, GridContext context)
	{
		GridNode oldNode = nodesMap.get(nodeId);
		if (oldNode != null)
		{
			if (oldNode.getSession().getId().equals(session.getId()))
				return;
			if (oldNode.getSession().isActive())
			{
				session.close();
				//TODO re-add node to remote
				return;
			}
			else
			{
				oldNode.getSession().close();
				nodesMap.remove(nodeId);
			}
		}
		GridNode node = new GridNode(nodeId, session, context, GridNodeType.TYPE_REMOTE);
		nodesMap.put(nodeId, node);
		addressMap.put(node.context().getServerAddress(), nodeId);
		sessionIdMap.put(session.getId(), nodeId);
		GridRuntime.events().publish(GridEvent.NODE_JOIN, node);
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
		buf.append("-------------------------------------------------------------\n");
		buf.append(GridRuntime.tasks().toSimgleString()).append('\n');
		buf.append("-------------------------------------------------------------\n");
		buf.append(GridRuntime.jobs().toRemoteJobsString()).append('\n');
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
			sessionIdMap.remove(node.getSession().getId());
		}
		return node;
	}
	
	public synchronized GridNode removeNode(GridNode node)
	{
		GridNode rNode = GridRuntime.nodes().removeNode(node.getId());
		if (rNode == null)
		{
			addressMap.remove(node.context().getServerAddress());
			sessionIdMap.remove(node.getSession().getId());
			rNode = node;
		}
		if (rNode != null)
		{
			rNode.getSession().close();
			GridRuntime.events().publish(GridEvent.NODE_LEAVE, rNode);
		}
		return rNode;
	}
	
	public synchronized GridNode removeNode(GridSession session)
	{
		GridNode node = null;
		String nodeId = GridRuntime.nodes().getNodeId(session.getId());
		if (nodeId != null)
		{
			node = GridRuntime.nodes().removeNode(nodeId);
			if (node != null)
			{
				node.getSession().close();
				GridRuntime.events().publish(GridEvent.NODE_LEAVE, node);
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
	
	public String getNodeId(String sessionId)
	{
		return sessionIdMap.get(sessionId);
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
