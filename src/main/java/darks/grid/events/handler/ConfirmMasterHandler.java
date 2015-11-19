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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.MasterMeta;
import darks.grid.events.GridEventHandler;
import darks.grid.utils.ThreadUtils;

public class ConfirmMasterHandler extends GridEventHandler
{

	private static final Logger log = LoggerFactory.getLogger(ConfirmMasterHandler.class);
	
	@Override
	public void handle(GridEvent event) throws Exception
	{
		MasterMeta meta = event.getData();
		log.info("Confirm master " + meta.getNodeId() + " addr:" + meta.getAddress());
		GridNode node = GridRuntime.nodes().getNode(meta.getNodeId());
		if (node != null && !node.isAlive())
		{
			if (GridRuntime.network().tryJoinAddress(meta.getAddress()))
			{
				ThreadUtils.threadSleep(3000);
				node = GridRuntime.nodes().getNode(meta.getNodeId());
			}
		}
		if (node == null)
		{
			log.error("Master node " + meta.getNodeId() + " doesn't exist.");
			return;
		}
		for (GridNode nd : GridRuntime.nodes().getNodesList())
		{
			if (nd.isMaster() && !nd.equals(node))
				nd.setMaster(false);
		}
		node.setMaster(true);
		if (node.isLocal())
			GridRuntime.master().notifyTask();
	}

}
