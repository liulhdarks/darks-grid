package darks.grid.network.discovery;

import java.net.InetSocketAddress;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridNodesManager;
import darks.grid.GridRuntime;
import darks.grid.utils.ParamsUtils;

public class TCPPING extends GridDiscovery
{
	
    private static final long serialVersionUID = -2291696195096655837L;

    private static final Logger log = LoggerFactory.getLogger(TCPPING.class);
	
	private Collection<InetSocketAddress> tryAddressList = null;
	
	private String hosts = null;
	
	public TCPPING()
	{
		
	}
	
	@Override
	public void findNodes()
	{
		GridNodesManager nodesManager = GridRuntime.nodes();
		if (tryAddressList != null)
		{
			log.info("TCPPING try to ping address " + tryAddressList.size());
			for (InetSocketAddress address : tryAddressList)
			{
				if (nodesManager.contains(address))
					continue;
				GridRuntime.network().tryJoinAddress(address);
			}
		}
	}

    public String getHosts()
    {
        return hosts;
    }

    public void setHosts(String hosts)
    {
        this.hosts = hosts;
        if (hosts != null)
        {
            tryAddressList = ParamsUtils.parseAddress(hosts);
        }
    }
	
}
