/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.network;

import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.config.GridConfiguration;
import darks.grid.config.NetworkConfig;
import darks.grid.utils.ThreadUtils;

public final class GridNetworkBuilder
{

	private static final Logger log = LoggerFactory.getLogger(GridNetworkBuilder.class);
	
	private GridNetworkBuilder()
	{
		
	}
	
	public static GridMessageClient buildMessageClient(GridConfiguration config)
	{
		GridMessageClient client = new GridMessageClient(ThreadUtils.getThrealPool());
		if (!client.initialize())
			return null;
		return client;
	}
	
	public static GridMessageServer buildMessageServer(GridConfiguration config)
	{
		log.info("Startup grid message server.");
		GridMessageServer server = new GridMessageServer();
		if (!server.initialize())
			return null;
		return server;
	}
	
	public static GridSession listenServer(GridMessageServer server)
	{
		GridSession session = null;
		NetworkConfig netConfig = GridRuntime.config().getNetworkConfig();
		String bindHost = netConfig.getBindHost();
		int maxTryPort = netConfig.getBindPort() + netConfig.getBindPortRange();
		for (int port = netConfig.getBindPort(); port <= maxTryPort; port++)
		{
			try
			{
				session = server.listen(bindHost, port);
				if (session == null || !session.isActive())
					log.error("Fail to bind host:" + bindHost + " port:" + port + ".Exception occured.");
				break;
			}
			catch (BindException e)
			{
				log.warn("Grid server try to listen host:" + bindHost + " port:" + port + "[max:" + maxTryPort + "] failed.");
			}
		}
		return session;
	}
	
}
