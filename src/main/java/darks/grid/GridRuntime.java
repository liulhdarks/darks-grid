package darks.grid;

import darks.grid.network.GridNetworkCenter;
import darks.grid.utils.ThreadUtils;

public final class GridRuntime
{

	static GridConfiguration config;
	
	static GridNetworkCenter network;
	
	static GridNodesManager nodesManager;
	
	static GridContext context;
	
	private GridRuntime()
	{
		
	}
	
	public static void initialize(GridConfiguration config)
	{
		GridRuntime.config = config;
		context = new GridContext();
		network = new GridNetworkCenter();
		nodesManager = new GridNodesManager();
		context.initialize(config);
		nodesManager.initialize(config);
		network.initialize(config);
	}
	
	public static void destroy()
	{
		network.destroy();
		nodesManager.destroy();
		ThreadUtils.shutdownAll();
	}
	
	public static String getLocalId()
	{
		return context().getLocalNodeId();
	}

	public static GridConfiguration getConfig()
	{
		return config;
	}

	public static GridNetworkCenter getNetwork()
	{
		return network;
	}

	public static GridNodesManager getNodesManager()
	{
		return nodesManager;
	}

	public static GridContext context()
	{
		return context;
	}
	
}
