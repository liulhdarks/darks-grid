package darks.grid.network;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridContext;
import darks.grid.beans.GridNode;

public class NodesMonitorThread extends Thread
{
	
	private static final Logger log = LoggerFactory.getLogger(NodesMonitorThread.class);
	
	private volatile boolean stoped = false;
	
	long st = System.currentTimeMillis();
	
	public NodesMonitorThread()
	{
		
	}

	public void run()
	{
		try
		{
			while (!stoped && !isInterrupted())
			{
				Map<String, GridNode> nodesMap = GridContext.getNodesManager().getNodesMap();
				log.info("Start to monitor nodes.size:" + nodesMap.size());
				for (Entry<String, GridNode> entry : nodesMap.entrySet())
				{
					//TODO heart-alive package
				}
				if (System.currentTimeMillis() - st > 60000)
				{
					log.info(GridContext.getNodesManager().getNodesInfo());
					st = System.currentTimeMillis();
				}
				Thread.sleep(30000);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public boolean isStoped()
	{
		return stoped;
	}

	public void setStoped(boolean stoped)
	{
		this.stoped = stoped;
	}
	
	
}
