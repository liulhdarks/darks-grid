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
package darks.grid.events.handler;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridAddress;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.events.GridEventHandler;
import darks.grid.manager.GridNodesManager;

public class MergeNodesHandler implements GridEventHandler
{

	private static final Logger log = LoggerFactory.getLogger(MergeNodesHandler.class);
	
	@Override
	public void handle(GridEvent event) throws Exception
	{
		GridNodesManager nodesManager = GridRuntime.nodes();
		Map<String, GridAddress> nodeAddrMap = event.getData();
		log.info("Recv merge nodes " + nodeAddrMap);
		for (Entry<String, GridAddress> entry : nodeAddrMap.entrySet())
		{
			GridNode node = nodesManager.getNode(entry.getKey());
			if (node != null)
			{
				if (node.isLocal() || node.isAlive())
					continue;
				nodesManager.removeNode(node);
			}
			GridRuntime.network().tryJoinAddress(entry.getValue());
		}
	}

}
