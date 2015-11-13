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
package darks.grid.balance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import darks.grid.GridRuntime;
import darks.grid.beans.GridNode;

public class CpuUsageBalance extends GridBalance
{
	
	private static AtomicInteger globalIndex = new AtomicInteger(0);

	private List<GridNode> nodesList = null;
	
	public CpuUsageBalance()
	{
		
	}
	
	@Override
	public GridNode getBalanceNode()
	{
		if (nodesList == null)
		{
			initNodesList();
		}
		if (nodesList == null || nodesList.isEmpty())
			return null;
		int len = nodesList.size();
		int tryCount = 0;
		GridNode node = null;
		do
		{
			int i = globalIndex.getAndIncrement();
			node = nodesList.get(i % len);
			tryCount++;
		}
		while (tryCount < len && !node.isAlive());
		return node;
	}
	
	private synchronized void initNodesList()
	{
		if (nodesList == null)
		{
			nodesList = new ArrayList<GridNode>();
			nodesList.addAll(GridRuntime.nodes().getNodesMap().values());
			Collections.sort(nodesList, new Comparator<GridNode>()
			{

				@Override
				public int compare(GridNode node1, GridNode node2)
				{
					float cpu1 = node1.getMachineInfo().getSystemCpuUsage();
					cpu1 = cpu1 < 0 ? 0.f : cpu1;
					float cpu2 = node2.getMachineInfo().getSystemCpuUsage();
					cpu2 = cpu2 < 0 ? 0.f : cpu2;
					return Float.compare(cpu1, cpu2);
				}
			});
		}
	}
}
