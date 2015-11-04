package darks.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.config.GridConfiguration;
import darks.grid.events.GridEventsManager;
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
	
	static GridComponentManager components;
	
	static GridTaskManager tasks;
	
	static GridLocalMessageManager localManager;
	
	static GridEventsManager eventsManager;
	
	private GridRuntime()
	{
		
	}
	
	public static boolean initialize(GridConfiguration config)
	{
		GridRuntime.config = config;
		context = new GridContext();
		network = new GridNetworkCenter();
		nodesManager = new GridNodesManager();
		components = new GridComponentManager();
		tasks = new GridTaskManager();
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
		ret = components.initialize(config);
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
		components.setupComponents();
		ret = tasks.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize tasks manager.");
            return false;
        }
		return true;
	}
	
	public static void destroy()
	{
		tasks.destroy();
	    components.destroy();
	    localManager.destroy();
		network.destroy();
		nodesManager.destroy();
		eventsManager.destroy();
		ThreadUtils.shutdownAll();
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
		return tasks;
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
	
	
}
