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
		this.nodesList = GridRuntime.nodes().getNodesList();
	}
    
    public RollPolingBalance(List<GridNode> nodesList)
    {
        this.nodesList = nodesList;
    }
	
	@Override
	public GridNode getBalanceNode()
	{
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
}
