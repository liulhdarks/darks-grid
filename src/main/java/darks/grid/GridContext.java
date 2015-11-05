package darks.grid;

import java.io.Serializable;
import java.net.InetSocketAddress;

import darks.grid.beans.MachineInfo;
import darks.grid.config.GridConfiguration;

public class GridContext implements Serializable, GridManager
{

	private static final long serialVersionUID = -4898402719806434910L;

	private long startupTime = 0;
	
	private long startupNanoTime = 0;
	
	private String localNodeId;
	
	private String clusterName;
	
	private InetSocketAddress serverAddress;
	
	private MachineInfo machineInfo = new MachineInfo();
	
	public GridContext()
	{
		
	}
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
		clusterName = config.getClusterName();
		startupTime = System.currentTimeMillis();
		startupNanoTime = System.nanoTime();
		return true;
	}
	
	@Override
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
	
	public MachineInfo getMachineInfo()
    {
        return machineInfo;
    }

    public void setMachineInfo(MachineInfo machineInfo)
    {
        this.machineInfo = machineInfo;
    }

    @Override
	public String toString()
	{
		return "GridContext [startupTime=" + startupTime + ", startupNanoTime=" + startupNanoTime
				+ ", localNodeId=" + localNodeId + ", clusterName=" + clusterName
				+ ", serverAddress=" + serverAddress+ ", machineInfo=" + machineInfo + "]";
	}
	
}
