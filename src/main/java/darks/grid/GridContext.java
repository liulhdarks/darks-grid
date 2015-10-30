package darks.grid;

import java.io.Serializable;
import java.net.InetSocketAddress;

public class GridContext implements Serializable
{

	private static final long serialVersionUID = -4898402719806434910L;

	private long startupTime = 0;
	
	private long startupNanoTime = 0;
	
	private String localNodeId;
	
	private String clusterName;
	
	private InetSocketAddress serverAddress;
	
	public GridContext()
	{
		
	}
	
	public void initialize(GridConfiguration config)
	{
		clusterName = config.getClusterName();
		startupTime = System.currentTimeMillis();
		startupNanoTime = System.nanoTime();
	}
	
	public void destroy()
	{
		
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	public void setStartupTime(long startupTime)
	{
		this.startupTime = startupTime;
	}

	public long getStartupNanoTime()
	{
		return startupNanoTime;
	}

	public void setStartupNanoTime(long startupNanoTime)
	{
		this.startupNanoTime = startupNanoTime;
	}

	public String getLocalNodeId()
	{
		return localNodeId;
	}

	public void setLocalNodeId(String localNodeId)
	{
		this.localNodeId = localNodeId;
	}

	public String getClusterName()
	{
		return clusterName;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}

	public InetSocketAddress getServerAddress()
	{
		return serverAddress;
	}

	public void setServerAddress(InetSocketAddress serverAddress)
	{
		this.serverAddress = serverAddress;
	}
	
	
	
}
