package darks.grid.balance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import darks.grid.GridRuntime;
import darks.grid.beans.GridNode;

public class RollPolingBalance extends GridBalance
{
	
	private static AtomicInteger globalIndex = new AtomicInteger(0);

	private List<GridNode> nodesList = null;
	
	public RollPolingBalance()
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
					float cpu1 = node1.getMachineInfo() == null ? 1.f : node1.getMachineInfo().getSystemCpuUsage();
					cpu1 = cpu1 < 0 ? 0.f : cpu1;
					float cpu2 = node2.getMachineInfo() == null ? 1.f : node2.getMachineInfo().getSystemCpuUsage();
					cpu2 = cpu2 < 0 ? 0.f : cpu2;
					return Float.compare(cpu1, cpu2);
				}
			});
		}
	}
}
