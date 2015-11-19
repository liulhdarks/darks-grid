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
package darks.grid.manager;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridContext;
import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.beans.GridNode.GridNodeType;
import darks.grid.beans.NodeId;
import darks.grid.config.GridConfiguration;
import darks.grid.network.GridSession;

public class GridNodesManager implements GridManager
{

	private static final Logger log = LoggerFactory.getLogger(GridNodesManager.class);
    
    private List<GridNode> nodesList = new CopyOnWriteArrayList<GridNode>();
	
	private Map<String, GridNode> nodesMap = new ConcurrentHashMap<String, GridNode>();
	
	private ConcurrentSkipListSet<GridNode> nodesSet = new ConcurrentSkipListSet<>(new NodeComparator());
	
	private Map<SocketAddress, String> addressMap = new ConcurrentHashMap<SocketAddress, String>();
	
	private Map<String, String> sessionIdMap = new ConcurrentHashMap<String, String>();
	
	private AtomicBoolean snapshotChange = new AtomicBoolean(true);
	
	private List<GridNode> nodesSnapshotList = null;
	
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
		GridNode localNode = getLocalNode();
		if (localNode == null)
		{
			String localNodeId = NodeId.localId();
			GridRuntime.context().setLocalNodeId(localNodeId);
			localNode = new GridNode(localNodeId, session, GridRuntime.context(), GridNodeType.TYPE_LOCAL);
	        addNode(session, localNode);
		}
		else
		{
			localNode.setSession(session);
		}
	}
	
	public synchronized GridNode addRemoteNode(String nodeId, GridSession session, GridContext context)
	{
		GridNode oldNode = nodesMap.get(nodeId);
		if (oldNode != null)
		{
			if (oldNode.getSession().getId().equals(session.getId()))
				return oldNode;
			if (oldNode.getSession().isActive())
			{
				session.close();
				//TODO re-add node to remote
				return oldNode;
			}
			else
			{
				oldNode.getSession().close();
				nodesMap.remove(nodeId);
			}
		}
		GridNode node = new GridNode(nodeId, session, context, GridNodeType.TYPE_REMOTE);
		addNode(session, node);
		return node;
	}
	
	private synchronized void addNode(GridSession session, GridNode node)
	{
	    String nodeId = node.getId();
	    nodesMap.put(nodeId, node);
        nodesList.add(node);
        nodesSet.add(node);
        addressMap.put(node.context().getServerAddress(), nodeId);
        sessionIdMap.put(session.getId(), nodeId);
        GridRuntime.events().publish(GridEvent.NODE_JOIN, node);
        snapshotChange.set(true);
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
		for (Entry<String, GridNode> entry : nodesMap.entrySet())
		{
			buf.append(entry.getValue().toSimpleString()).append('\n');
		}
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
		    nodesList.remove(node);
		    nodesSet.remove(node);
			addressMap.remove(node.context().getServerAddress());
			sessionIdMap.remove(node.getSession().getId());
		}
        snapshotChange.set(true);
		return node;
	}
	
	public synchronized GridNode removeNode(GridNode node)
	{
		GridNode rNode = removeNode(node.getId());
		if (rNode == null)
		{
            nodesList.remove(node);
		    nodesSet.remove(node);
			addressMap.remove(node.context().getServerAddress());
			sessionIdMap.remove(node.getSession().getId());
			rNode = node;
		}
		if (rNode != null)
		{
			rNode.getSession().close();
			GridRuntime.events().publish(GridEvent.NODE_LEAVE, rNode);
		}
        snapshotChange.set(true);
		return rNode;
	}
	
	public synchronized GridNode removeNode(GridSession session)
	{
		GridNode node = null;
		String nodeId = getNodeId(session.getId());
		if (nodeId != null)
		{
			node = removeNode(nodeId);
			if (node != null)
			{
				node.getSession().close();
				GridRuntime.events().publish(GridEvent.NODE_LEAVE, node);
			}
		}
        snapshotChange.set(true);
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
	
	public List<GridNode> getNodesList()
    {
        return nodesList;
    }

    public synchronized List<GridNode> getSnapshotNodes()
	{
    	if (snapshotChange.get() || nodesSnapshotList == null || nodesSnapshotList.isEmpty())
    	{
        	nodesSnapshotList = new ArrayList<GridNode>(nodesSet.size());
    	    for (GridNode node : nodesSet)
    	    {
    	        if (node.isAlive())
    	        	nodesSnapshotList.add(node);
    	    }
    	    snapshotChange.getAndSet(false);
    	}
	    return nodesSnapshotList;
	}

	public Map<String, GridNode> getNodesMap()
	{
		return nodesMap;
	}

	public Map<SocketAddress, String> getAddressMap()
	{
		return addressMap;
	}
	
	class NodeComparator implements Comparator<GridNode>
	{

		@Override
		public int compare(GridNode o1, GridNode o2)
		{

			long t1 = o1.context().getStartupTime();
			long t2 = o2.context().getStartupTime();
			int ret = (int)(t1 - t2);
			if (ret == 0)
			{
				t1 = o1.context().getStartupNanoTime();
				t2 = o2.context().getStartupNanoTime();
				ret = (int) (t1 - t2);
				if (ret == 0)
					return o1.getId().compareTo(o2.getId());
				else
					return ret;
			}
			else
				return ret;
		}
		
	}
}
