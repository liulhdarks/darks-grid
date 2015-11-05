package darks.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.config.GridConfiguration;
import darks.grid.events.GridEventsManager;
import darks.grid.executor.job.GridJobManager;
import darks.grid.executor.task.GridTaskManager;
import darks.grid.network.GridNetworkCenter;
import darks.grid.network.local.GridLocalMessageManager;
import darks.grid.utils.ThreadUtils;

public final class GridRuntime
{
    
    private static final Logger log = LoggerFactory.getLogger(GridRuntime.class);

	static GridConfiguration config;
	
	static GridNetworkCenter network;
	
	static GridNodesManager nodesManager;
	
	static GridContext context;
	
	static GridComponentManager componentsManager;
	
	static GridTaskManager tasksManager;
	
	static GridJobManager jobsManager;
	
	static GridLocalMessageManager localManager;
	
	static GridEventsManager eventsManager;
	
	static Thread printThread = null;
	
	private GridRuntime()
	{
		
	}
	
	public static boolean initialize(GridConfiguration config)
	{
		GridRuntime.config = config;
		context = new GridContext();
		network = new GridNetworkCenter();
		nodesManager = new GridNodesManager();
		componentsManager = new GridComponentManager();
		tasksManager = new GridTaskManager();
		jobsManager = new GridJobManager();
		localManager = new GridLocalMessageManager();
		eventsManager = new GridEventsManager();
		boolean ret = context.initialize(config);
		if (!ret)
		{
            log.error("Fail to initialize grid context.");
            return false;
		}
		ret = eventsManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize events manager.");
            return false;
        }
		ret = componentsManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize grid components.");
            return false;
        }
        ret = nodesManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize node manager.");
            return false;
        }
        ret = network.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize network.");
            return false;
        }
        ret = localManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize local message manager.");
            return false;
        }
        componentsManager.setupComponents();
		ret = tasksManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize tasks manager.");
            return false;
        }
		ret = jobsManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize jobs manager.");
            return false;
        }
        startupPrintThread(config);
		return true;
	}
	
	public static void destroy()
	{
		if (printThread != null)
			printThread.interrupt();
		jobsManager.destroy();
		tasksManager.destroy();
		componentsManager.destroy();
	    localManager.destroy();
		network.destroy();
		nodesManager.destroy();
		eventsManager.destroy();
		ThreadUtils.shutdownAll();
	}
	
	private static void startupPrintThread(final GridConfiguration config)
	{
		printThread = new Thread()
		{
			
			public void run()
			{
				int interval = config.getPrintNodesInterval();
				try
				{
					while (!isInterrupted())
					{
						log.info(nodes().getNodesInfo());
						Thread.sleep(interval);
					}
				}
				catch (InterruptedException e)
				{
				}
				catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
			}
			
		};
		ThreadUtils.executeThread(printThread);
	}
	
	public static String getLocalId()
	{
		return context().getLocalNodeId();
	}

	public static GridConfiguration config()
	{
		return config;
	}

	public static GridNetworkCenter network()
	{
		return network;
	}

	public static GridNodesManager nodes()
	{
		return nodesManager;
	}

	public static GridTaskManager tasks()
	{
		return tasksManager;
	}

	public static GridContext context()
	{
		return context;
	}

	public static GridLocalMessageManager local()
	{
		return localManager;
	}

	public static GridEventsManager events()
	{
		return eventsManager;
	}

	public static GridJobManager jobs()
	{
		return jobsManager;
	}
	
	
}
