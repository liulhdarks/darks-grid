package darks.grid.network;

import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.config.GridConfiguration;
import darks.grid.config.NetworkConfig;

public final class GridNetworkBuilder
{

	private static final Logger log = LoggerFactory.getLogger(GridNetworkBuilder.class);
	
	private GridNetworkBuilder()
	{
		
	}
	
	public static GridMessageServer buildMessageServer(GridConfiguration config)
	{
		log.info("Startup grid message server.");
		GridMessageServer server = new GridMessageServer();
		if (!server.initialize())
			return null;
		NetworkConfig netConfig = config.getNetworkConfig();
		String bindHost = netConfig.getBindHost();
		int maxTryPort = netConfig.getBindPort() + netConfig.getBindPortRange();
		for (int port = netConfig.getBindPort(); port <= maxTryPort; port++)
		{
			try
			{
				if (!server.listen(bindHost, port))
					log.error("Fail to bind host:" + bindHost + " port:" + port + ".Exception occured.");
				break;
			}
			catch (BindException e)
			{
				log.warn("Grid server try to listen host:" + bindHost + " port:" + port + "[max:" + maxTryPort + "] failed.");
			}
		}
		return server.isBinded() ? server : null;
	}
	
}
