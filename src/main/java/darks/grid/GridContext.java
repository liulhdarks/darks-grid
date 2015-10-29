package darks.grid;

import darks.grid.beans.GridRuntime;
import darks.grid.network.GridNetworkCenter;

public final class GridContext
{

	static GridConfiguration config;
	
	static GridNetworkCenter network;
	
	static GridNodesManager nodesManager;
	
	static GridRuntime runtime;
	
	private GridContext()
	{
		
	}
	
	public static void initialize()
	{
		runtime = new GridRuntime();
		config = new GridConfiguration();
		network = new GridNetworkCenter();
		nodesManager = new GridNodesManager();
		network.initialize();
		nodesManager.initialize();
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
	
}
