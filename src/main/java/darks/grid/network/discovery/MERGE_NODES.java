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

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.events.EventsChannel;
import darks.grid.manager.GridNodesManager;

public class MERGE_NODES extends GridDiscovery
{
	
	private static final long serialVersionUID = -8789361878660574408L;
	
	private static final Logger log = LoggerFactory.getLogger(MERGE_NODES.class);
	
	private static final int DEFAULT_DELAY = 5000;
	
	public MERGE_NODES()
	{
		setDelay(DEFAULT_DELAY);
	}
	
	@Override
	public void findNodes()
	{
		GridNodesManager nodesManager = GridRuntime.nodes();
		Map<String, InetSocketAddress> nodeAddrMap = new HashMap<String, InetSocketAddress>();
		for (Entry<String, GridNode> entry : nodesManager.getNodesMap().entrySet())
		{
			GridNode node = entry.getValue();
			nodeAddrMap.put(node.getId(), node.context().getServerAddress());
		}
		log.info("Publish merge nodes event with " + nodeAddrMap.size() + " nodes.");
		GridRuntime.events().publishOthers(EventsChannel.SYSTEM_CHANNEL, GridEvent.MERGE_NODES, nodeAddrMap);
	}

	
}
