package darks.grid.network.discovery;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;

public class DiscoveryThread extends Thread
{
	
	private static final Logger log = LoggerFactory.getLogger(DiscoveryThread.class);

	private volatile boolean stoped = false;
	
	public DiscoveryThread()
	{
		
	}
	
	public void run()
	{
		try
		{
			while (!stoped && !isInterrupted())
			{
				LinkedList<GridDiscovery> discoveries = GridRuntime.getNetwork().getDiscoveries();
				if (discoveries != null)
				{
					for (GridDiscovery discovery : discoveries)
					{
						discovery.findNodes();
					}
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
