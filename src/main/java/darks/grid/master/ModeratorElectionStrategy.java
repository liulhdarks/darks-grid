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

package darks.grid.master;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.MasterMeta;
import darks.grid.events.EventsChannel;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.rpc.RpcResult;


public class ModeratorElectionStrategy implements ElectionStrategy
{
	
	private static final Logger log = LoggerFactory.getLogger(ModeratorElectionStrategy.class);

	@Override
	public String elect()
	{
		int nodeSize = GridRuntime.nodes().getNodesList().size();
		if (nodeSize == 1)
			return confirmMaster(GridRuntime.getLocalId());
		List<NodeStatus> nodesStatus = requestNodesStatus();
		if (nodesStatus == null || nodesStatus.isEmpty())
			return null;
		else
		{
			NodeStatus status = nodesStatus.get(0);
			String nodeId = status.getNodeId();
			return confirmMaster(nodeId);
		}
	}
	
	@Override
	public String checkElect()
	{
		List<GridNode> nodes = GridRuntime.nodes().getSnapshotNodes();
		GridNode node = nodes.get(0);
		if (node.isLocal())
		{
			log.info("Local node moderate master's election.");
			return elect();
		}
		else
		{
			log.info("Waiting node " + node.getId() + " moderate master's election.");
			return null;
		}
	}



	private String confirmMaster(String nodeId)
	{
		log.info("Elect node " + nodeId);
		if (nodeId == null)
			return null;
		GridNode node = GridRuntime.nodes().getNode(nodeId);
		if (node == null || !node.isAlive())
			return null;
		MasterMeta meta = new MasterMeta(nodeId, node.context().getServerAddress());
		if (GridRuntime.events().publishAll(EventsChannel.SYSTEM_CHANNEL, GridEvent.CONFIRM_MASTER, meta))
			return nodeId;
		else
			return null;
	}

	private List<NodeStatus> requestNodesStatus()
	{
		ExecuteConfig config = new ExecuteConfig();
		config.setTimeout(5000);
		RpcResult result = RpcExecutor.callMethod(ElectRpcInvoker.class, "getNodeStatus", null, config);
		List<NodeStatus> list = result.getGenericResult();
		if (list == null || list.isEmpty())
		{
			return null;
		}
		else
		{
			Collections.sort(list, new Comparator<NodeStatus>()
			{
				@Override
				public int compare(NodeStatus o1, NodeStatus o2)
				{
					int ret = o2.getInfo().getHealthyScore() - o1.getInfo().getHealthyScore();
					if (ret == 0)
					{
						ret = (int)((o2.getInfo().getSystemCpuUsage() - o1.getInfo().getSystemCpuUsage()) * 100.f);
						if (ret == 0)
						{
							ret = Float.compare(o2.getInfo().getUsedTotalMemoryUsage(), o1.getInfo().getUsedTotalMemoryUsage());
							if (ret == 0)
								return o2.getNodeId().compareTo(o1.getNodeId());
						}
					}
					return ret;
				}
			});
		}
		return list;
	}
}
