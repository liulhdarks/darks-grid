package darks.grid.network.discovery;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import darks.grid.GridContext;
import darks.grid.GridNodesManager;
import darks.grid.network.GridMessageClient;
import darks.grid.utils.NetworkUtils;

public class TCPPING extends GridDiscovery
{
	
	private static final String HOSTS = "hosts";
	
	private List<InetSocketAddress> tryAddressList = null;
	
	public TCPPING()
	{
		
	}
	
	public void findNodes()
	{
		GridNodesManager nodesManager = GridContext.getNodesManager();
		if (tryAddressList != null)
		{
			for (InetSocketAddress address : tryAddressList)
			{
				if (nodesManager.contains(address))
					continue;
				GridMessageClient client = new GridMessageClient();
				client.initialize();
				if (client.connect(address))
				{
					
				}
			}
		}
	}
	
	@Override
	public void setConfig(Map<String, String> params)
	{
		String hosts = params.get(HOSTS);
		if (hosts != null)
		{
			tryAddressList = NetworkUtils.parseAddress(hosts);
		}
	}
	
}
