package darks.grid.network.discovery;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.GridNodesManager;
import darks.grid.network.GridMessageClient;
import darks.grid.utils.ParamsUtils;
import darks.grid.utils.ThreadUtils;

public class TCPPING extends GridDiscovery
{
	
	private static final Logger log = LoggerFactory.getLogger(TCPPING.class);
	
	private static final String HOSTS = "hosts";
	
	private List<InetSocketAddress> tryAddressList = null;
	
	public TCPPING()
	{
		
	}
	
	@Override
	public void findNodes()
	{
		GridNodesManager nodesManager = GridRuntime.getNodesManager();
		if (tryAddressList != null)
		{
			log.info("TCPPING try to ping address " + tryAddressList.size());
			for (InetSocketAddress address : tryAddressList)
			{
				if (nodesManager.contains(address))
					continue;
				GridMessageClient client = new GridMessageClient(ThreadUtils.getThrealPool());
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
		params.put(HOSTS, "30.2.44.188:[7800-7803]");
		String hosts = params.get(HOSTS);
		if (hosts != null)
		{
			tryAddressList = ParamsUtils.parseAddress(hosts);
		}
	}
	
}
