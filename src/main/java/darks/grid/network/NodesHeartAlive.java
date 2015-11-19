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

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridComponent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.HeartAliveMeta;

public class NodesHeartAlive extends GridComponent
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5037403083950340100L;

	private static final Logger log = LoggerFactory.getLogger(NodesHeartAlive.class);
    
    private int expire = 600000;
	
	public NodesHeartAlive()
	{
		
	}

	@Override
	protected void execute() throws Exception
	{
		Map<String, GridNode> nodesMap = GridRuntime.nodes().getNodesMap();
		log.info("Start to check " + nodesMap.size() + " nodes heart alive.");
		for (Entry<String, GridNode> entry : nodesMap.entrySet())
		{
			GridNode node = entry.getValue();
			if (node.isLocal())
			{
			    node.context().getMachineInfo().update();
			    if (!node.getSession().isActive())
			    	GridRuntime.network().bindLocalNode();
                continue;
			}
			if (!node.isAlive())
			{
				log.info("Grid node " + node.getId() + " " + node.context().getServerAddress() + " miss alive.");
				GridRuntime.nodes().removeNode(node);
			}
			else
			{
				checkAlive(node);
			}
		}
	}
	
	private void checkAlive(GridNode node)
	{
		boolean valid = true;
		try
		{
			HeartAliveMeta meta = new HeartAliveMeta(GridRuntime.context().getLocalNodeId(), GridRuntime.context());
			meta.setTimestamp(System.nanoTime());
			GridMessage msg = new GridMessage(meta, GridMessage.MSG_HEART_ALIVE);
			valid = node.sendSyncMessage(msg);
		}
		catch (Exception e)
		{
			log.error("Fail to check alive " + node + ". Cause " + e.getMessage());
			valid = false;
		}
		if (!valid)
		{
			GridRuntime.nodes().removeNode(node);
		}
	}

	public int getExpire()
	{
		return expire;
	}

	public void setExpire(int expire)
	{
		this.expire = expire;
	}
	
}
