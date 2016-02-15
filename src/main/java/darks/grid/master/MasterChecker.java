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

package darks.grid.master;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import darks.grid.GridRuntime;
import darks.grid.beans.GridComponent;
import darks.grid.beans.GridNode;

public class MasterChecker extends GridComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -603020885408273059L;
	
	private static final Logger log = LoggerFactory.getLogger(MasterChecker.class);
	
	public MasterChecker()
	{
		setDelay(60000);
		setInterval(300000);
	}

	@Override
	protected void execute() throws Exception
	{
		List<String> nodeIds = new ArrayList<String>();
		List<GridNode> nodes = GridRuntime.nodes().getNodesList();
		for (GridNode node : nodes)
		{
			if (node.isAlive() && node.isMaster())
				nodeIds.add(node.getId());
		}
		log.info("Check master nodes " + nodeIds);
		if (nodeIds.size() > 1)
		{
			if (GridRuntime.master().checkMaster())
				Thread.sleep(5000);
		}
	}

}
