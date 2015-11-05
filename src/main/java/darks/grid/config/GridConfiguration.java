package darks.grid.config;

import java.io.Serializable;

public class GridConfiguration implements Serializable
{

	private static final long serialVersionUID = -7262144727436897611L;
	
	private NetworkConfig networkConfig = new NetworkConfig();
	
	private ComponentConfig componentConfig = new ComponentConfig();
	
	private EventsConfig eventsConfig = new EventsConfig();
	
	private String clusterName = "DEFAULT";
    
    private int printNodesInterval = 180000;
	
	public GridConfiguration()
	{
		
	}

	public String getClusterName()
	{
		return clusterName;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}

    public NetworkConfig getNetworkConfig()
    {
        return networkConfig;
    }
    
    public ComponentConfig getComponentConfig()
    {
        return componentConfig;
    }

    
    
    public EventsConfig getEventsConfig()
	{
		return eventsConfig;
	}
    
	public int getPrintNodesInterval()
	{
		return printNodesInterval;
	}

	public void setPrintNodesInterval(int printNodesInterval)
	{
		this.printNodesInterval = printNodesInterval;
	}

	@Override
	public String toString()
	{
		return "GridConfiguration [networkConfig=" + networkConfig + ", componentConfig="
				+ componentConfig + ", eventsConfig=" + eventsConfig + ", clusterName="
				+ clusterName + "]";
	}

    
	
}
