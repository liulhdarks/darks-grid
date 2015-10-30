package darks.grid;

import darks.grid.beans.GridRuntime;
import darks.grid.network.GridNetworkCenter;
import darks.grid.utils.ThreadUtils;

public final class GridContext
{

	static GridConfiguration config;
	
	static GridNetworkCenter network;
	
	static GridNodesManager nodesManager;
	
	static GridRuntime runtime;
	
	private GridContext()
	{
		
	}
	
	public static void initialize(GridConfiguration config)
	{
		GridContext.config = config;
		runtime = new GridRuntime();
		network = new GridNetworkCenter();
		nodesManager = new GridNodesManager();
		runtime.initialize(config);
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
		return getRuntime().getLocalNodeId();
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

	public static GridRuntime getRuntime()
	{
		return runtime;
	}
	
}
