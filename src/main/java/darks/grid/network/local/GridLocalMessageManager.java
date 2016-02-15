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
package darks.grid.network.local;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.config.GridConfiguration;
import darks.grid.manager.GridManager;
import darks.grid.utils.ThreadUtils;

public class GridLocalMessageManager implements GridManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridLocalMessageManager.class);

	private BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<Object>();
	
	private List<GridLocalHandlerThread> handlers = new ArrayList<GridLocalHandlerThread>();
	
	public GridLocalMessageManager()
	{
		
	}
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
		log.info("Start to initialize grid local message mananger");
		messageQueue.clear();
		int threadSize = config.getNetworkConfig().getLocalMsgHandlerNumber();
		for (int i = 0; i < threadSize; i++)
		{
			GridLocalHandlerThread handler = new GridLocalHandlerThread(messageQueue);
			ThreadUtils.executeThread(handler);
			handlers.add(handler);
		}
		return true;
	}

	@Override
	public void destroy()
	{
		log.info("Destroy grid local message mananger.");
		for (GridLocalHandlerThread handler : handlers)
		{
			handler.destroy();
			handler.interrupt();
		}
	}
	
	public boolean offerMessage(Object obj)
	{
		return messageQueue.offer(obj);
	}
	
	public boolean offerSyncMessage(Object obj)
	{
		try
		{
			messageQueue.put(obj);
			return true;
		}
		catch (InterruptedException e)
		{
			return false;
		}
	}
	
}
