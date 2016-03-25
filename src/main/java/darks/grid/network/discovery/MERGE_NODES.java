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
package darks.grid.network.discovery;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridAddress;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.events.EventsChannel;
import darks.grid.events.GridEventHandler;
import darks.grid.manager.GridNodesManager;

public class MERGE_NODES extends GridDiscovery implements GridEventHandler
{
	
	private static final long serialVersionUID = -8789361878660574408L;
	
	private static final Logger log = LoggerFactory.getLogger(MERGE_NODES.class);
	
	private static final int DEFAULT_DELAY = 5000;
	
	private boolean joinAutoMerge = true;
	
	public MERGE_NODES()
	{
		setDelay(DEFAULT_DELAY);
		GridRuntime.events().addHandler(GridEvent.NODE_JOIN, this);
	}
	
	@Override
	public void findNodes()
	{
		Map<String, GridAddress> nodeAddrMap = getAddressMap();
		log.info("Publish merge nodes event with " + nodeAddrMap.size() + " nodes.");
		GridRuntime.events().publishOthers(EventsChannel.SYSTEM_CHANNEL, GridEvent.MERGE_NODES, nodeAddrMap);
	}

    @Override
    public void handle(GridEvent event) throws Exception 
    {
        if (joinAutoMerge) 
        {
            GridNode node = event.getData();
            if (node != null)
            {
                Map<String, GridAddress> nodeAddrMap = getAddressMap();
                log.info("Publish merge nodes event to " + node.getId() + " with " + nodeAddrMap.size() + " nodes.");
                node.publishEvent(EventsChannel.SYSTEM_CHANNEL, GridEvent.MERGE_NODES, nodeAddrMap, false);
            }
        }
    }
	
    public Map<String, GridAddress> getAddressMap()
    {
        GridNodesManager nodesManager = GridRuntime.nodes();
        Map<String, GridAddress> nodeAddrMap = new HashMap<String, GridAddress>();
        for (Entry<String, GridNode> entry : nodesManager.getNodesMap().entrySet())
        {
            GridNode node = entry.getValue();
            nodeAddrMap.put(node.getId(), node.context().getServerAddress());
        }
        return nodeAddrMap;
    }

    public boolean isJoinAutoMerge() 
    {
        return joinAutoMerge;
    }

    public void setJoinAutoMerge(boolean joinAutoMerge) 
    {
        this.joinAutoMerge = joinAutoMerge;
    }
    
    
}
