package darks.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.config.GridConfiguration;
import darks.grid.network.GridNetworkCenter;
import darks.grid.utils.ThreadUtils;

public final class GridRuntime
{
    
    private static final Logger log = LoggerFactory.getLogger(GridRuntime.class);

	static GridConfiguration config;
	
	static GridNetworkCenter network;
	
	static GridNodesManager nodesManager;
	
	static GridContext context;
	
	static GridComponentManager components;
	
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
		boolean ret = context.initialize(config);
		if (!ret)
		{
            log.error("Fail to initialize grid context.");
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
		components.setupComponents();
		return true;
	}
	
	public static void destroy()
	{
	    components.destroy();
		network.destroy();
		nodesManager.destroy();
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

	public static GridContext context()
	{
		return context;
	}
	
}
