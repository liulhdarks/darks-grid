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
package darks.grid.network.discovery;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridException;
import darks.grid.GridRuntime;
import darks.grid.beans.GridAddress;
import darks.grid.manager.GridNodesManager;
import darks.grid.utils.ParamsUtils;

public class TCPPING extends GridDiscovery
{
	
    private static final long serialVersionUID = -2291696195096655837L;

    private static final Logger log = LoggerFactory.getLogger(TCPPING.class);
	
	private Collection<GridAddress> tryAddressList = null;
	
	private String hosts = null;
	
	public TCPPING()
	{
		
	}
	
	@Override
	public void findNodes()
	{
		Set<GridAddress> tryAddrs = new LinkedHashSet<GridAddress>();
		GridNodesManager nodesManager = GridRuntime.nodes();
		if (tryAddressList != null)
		{
			for (GridAddress address : tryAddressList)
			{
				if (nodesManager.contains(address))
					continue;
				tryAddrs.add(address);
			}
		}
		Collection<GridAddress> cacheAddrs = GridRuntime.storage().getCacheHistoryNodes();
		if (cacheAddrs != null)
		{
			for (GridAddress address : cacheAddrs)
			{
				if (nodesManager.contains(address))
					continue;
				tryAddrs.add(address);
			}
		}
		if (!tryAddrs.isEmpty())
		{
			log.info("TCPPING try to ping address " + tryAddrs.size());
			for (GridAddress address : tryAddrs)
			{
				if (nodesManager.contains(address))
					continue;
				GridRuntime.network().tryJoinAddress(address);
			}
		}
		else
			log.info("TCPPING's ping address is empty.");
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
            try 
            {
                tryAddressList = ParamsUtils.parseAddress(hosts);
            } 
            catch (Exception e) 
            {
                throw new GridException("Invalid TCPPING hosts:" + hosts, e);
            }
        }
    }
	
}
