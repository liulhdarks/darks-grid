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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridNode;

public abstract class MasterTask extends Thread
{
	private static final Logger log = LoggerFactory.getLogger(MasterTask.class);
	
	private volatile boolean destroyed = false;
	
	private int interval = 1000;
	
	public MasterTask()
	{
		
	}
	
	public MasterTask(int interval)
	{
		this.interval = interval;
	}

	@Override
	public void run()
	{
		try
		{
			GridRuntime.master().awaitMaster();
			GridNode localNode = GridRuntime.nodes().getLocalNode();
			while (!destroyed && !isInterrupted())
			{
				if (!localNode.isMaster())
					GridRuntime.master().awaitMaster();
				if (!execute())
					break;
				if (interval > 0)
					Thread.sleep(interval);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public abstract boolean execute();
	
	public void destroy()
	{
		destroyed = true;
		interrupt();
	}
}
