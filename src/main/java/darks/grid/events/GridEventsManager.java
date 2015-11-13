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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridManager;
import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.config.GridConfiguration;

public class GridEventsManager implements GridManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridEventsManager.class);

	BlockingQueue<GridEvent> eventQueue = null;
	
	ExecutorService threadPool = null;
	
	List<EventsConsumer> concumers = new LinkedList<>();
	
	public GridEventsManager()
	{
		
	}
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
		log.info("Start to initialize events manager.");
		eventQueue = new LinkedBlockingQueue<>(config.getEventsConfig().getBlockQueueMaxNumber());
		int threadSize = config.getEventsConfig().getEventConsumerNumber();
		threadPool = Executors.newFixedThreadPool(threadSize);
		for (int i = 0; i < threadSize; i++)
		{
			EventsConsumer consumer = new EventsConsumer(eventQueue);
			threadPool.execute(consumer);
			concumers.add(consumer);
		}
		return true;
	}

	@Override
	public void destroy()
	{
		for (EventsConsumer consumer : concumers)
		{
			consumer.destroy();
		}
		threadPool.shutdownNow();
	}
	
	public boolean publish(String type, Object obj)
	{
		return publish(new GridEvent(obj, type));
	}
	
	public boolean publish(GridEvent event)
	{
		return eventQueue.offer(event);
	}
	
	public boolean publish(GridNode node, GridEvent event, boolean sync)
	{
		GridMessage message = new GridMessage(event, GridMessage.MSG_EVENT);
		if (sync)
			return node.sendSyncMessage(message);
		else
			return node.sendMessage(message);
	}
	
	public boolean publishAll(String type, Object obj)
	{
		return publishAll(new GridEvent(obj, type));
	}
	
	public boolean publishAll(GridEvent event)
	{
		List<GridNode> nodes = GridRuntime.nodes().getNodesList();
		for (GridNode node : nodes)
		{
			if (node.isAlive())
				publish(node, event, true);
		}
		return eventQueue.offer(event);
	}
	
	public void addHandler(String eventType, GridEventHandler handler)
	{
		EventsHandlerFactory.addHandler(eventType, handler);
	}
	
	public void addHandler(String eventType, Class<? extends GridEventHandler> clazz)
	{
		EventsHandlerFactory.addHandler(eventType, clazz);
	}
		
}
