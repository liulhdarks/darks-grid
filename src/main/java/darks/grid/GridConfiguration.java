package darks.grid;

import java.io.Serializable;

public class GridConfiguration implements Serializable
{

	private static final long serialVersionUID = -7262144727436897611L;

	private int listenPort = 7800;
	
	private int listenTryCount = 10;
	
	private String clusterName;
	
	public GridConfiguration()
	{
		
	}

	public int getListenPort()
	{
		return listenPort;
	}

	public void setListenPort(int listenPort)
	{
		this.listenPort = listenPort;
	}

	public int getListenTryCount()
	{
		return listenTryCount;
	}

	public void setListenTryCount(int listenTryCount)
	{
		this.listenTryCount = listenTryCount;
	}

	public String getClusterName()
	{
		return clusterName;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}
	
}
