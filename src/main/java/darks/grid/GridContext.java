package darks.grid;

import darks.grid.network.GridNetworkCenter;

public final class GridContext
{

	static GridConfiguration config;
	
	static GridNetworkCenter network;
	
	static GridNodesManager nodesManager;
	
	private GridContext()
	{
		
	}
	
	public static void initialize()
	{
		config = new GridConfiguration();
		network = new GridNetworkCenter();
		nodesManager = new GridNodesManager();
		network.initialize();
		
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
