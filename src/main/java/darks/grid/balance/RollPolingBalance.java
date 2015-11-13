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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import darks.grid.GridRuntime;
import darks.grid.beans.GridNode;

public class RollPolingBalance extends GridBalance
{
	
	private AtomicInteger index = new AtomicInteger(0);

	private List<GridNode> nodesList = null;
	
	public RollPolingBalance()
	{
	}
    
    public RollPolingBalance(List<GridNode> nodesList)
    {
        this.nodesList = nodesList;
    }
	
	@Override
	public GridNode getBalanceNode()
	{
		if (nodesList == null)
			initNodes();
		if (nodesList == null || nodesList.isEmpty())
			return null;
		int len = nodesList.size();
		int tryCount = 0;
		GridNode node = null;
		do
		{
			int i = index.getAndIncrement();
			node = nodesList.get(i % len);
			tryCount++;
		}
		while (tryCount < len && !node.isAlive());
		return node;
	}
	
	private synchronized boolean initNodes()
	{
		if (nodesList == null)
		{
			this.nodesList = GridRuntime.nodes().getNodesList();
		}
		return nodesList != null;
	}
}
