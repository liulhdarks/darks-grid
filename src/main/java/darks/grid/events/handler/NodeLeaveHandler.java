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

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.events.GridEventHandler;
import darks.grid.utils.ThreadUtils;

public class NodeLeaveHandler implements GridEventHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(NodeLeaveHandler.class);

	@Override
	public void handle(GridEvent event) throws Exception
	{
		GridNode node = event.getData();
		if (node != null)
		{	
			log.info("Grid node " + node.getId() + " " + node.context().getServerAddress() + " quit.");
			node.setQuit(true);
			GridRuntime.jobs().removeNodeAllJobs(node.getId());
			if (retryConnect(node))
				ThreadUtils.threadSleep(2000);
			if (node.isMaster())
				GridRuntime.master().checkMaster();
		}
		else
		{
			log.error("Unknown grid node quit." + event);
		}
	}
	
	private boolean retryConnect(GridNode node)
	{
		InetSocketAddress address = node.context().getServerAddress();
		int retryCount = GridRuntime.config().getNetworkConfig().getConnectFailRetry();
		for (int i = 0; i < retryCount; i++)
		{
			log.info("Reconnect remote address " + address);
			if (!GridRuntime.nodes().contains(address) 
					&& GridRuntime.network().tryJoinAddress(address))
				return true;
			ThreadUtils.threadSleep(1000);
		}
		return false;
	}

}
