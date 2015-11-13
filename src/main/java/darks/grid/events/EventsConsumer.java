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
package darks.grid.events;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridEvent;

public class EventsConsumer extends Thread
{
	
	private static final Logger log = LoggerFactory.getLogger(EventsConsumer.class);
	
	private BlockingQueue<GridEvent> queue;
	
	private boolean destroyed = false;
	
	public EventsConsumer(BlockingQueue<GridEvent> queue)
	{
		this.queue = queue;
	}
	
	public void run()
	{
		try
		{
			while (!destroyed && !isInterrupted())
			{
				GridEvent event = queue.take();
				if (event != null)
					consumer(event);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	public void destroy()
	{
		destroyed = true;
		interrupt();
	}
	
	private void consumer(GridEvent event)
	{
		List<GridEventHandler> handlers = EventsHandlerFactory.getHandler(event.getType());
		if (handlers == null)
		{
			log.error("Cannot find event handler for " + event);
		}
		else
		{
			try
			{
				for (GridEventHandler handler : handlers)
				{
					handler.handle(event);
				}
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
			}
		}
	}

}
