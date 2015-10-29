package darks.grid.network;

import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridConfiguration;

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
		int maxTryPort = config.getListenPort() + config.getListenTryCount();
		for (int port = config.getListenPort(); port <= maxTryPort; port++)
		{
			try
			{
				if (!server.listen(port))
					log.error("Fail to bind port " + port + ".Exception occured.");
				break;
			}
			catch (BindException e)
			{
				log.warn("Grid server try to listen port " + port + "[max:" + maxTryPort + "] failed.");
			}
		}
		return server.isBinded() ? server : null;
	}
	
}
