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
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.config.EventsConfig;
import darks.grid.config.EventsConfig.EventsChannelConfig;
import darks.grid.config.GridConfiguration;
import darks.grid.manager.GridManager;

public class GridEventsManager implements GridManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridEventsManager.class);
	
	private Map<String, EventsChannel> channels = new ConcurrentHashMap<>();
	
	EventsChannel systemChannel = null;
	
	EventsChannel defaultChannel = null;
	
	public GridEventsManager()
	{
		
	}
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
		log.info("Start to initialize events manager.");
		EventsConfig eventsConfig = config.getEventsConfig();
		Map<String, EventsChannelConfig> map = eventsConfig.getChannelsConfig();
		EventsChannelConfig defaultConfig = new EventsChannelConfig(EventsChannel.DEFAULT_CHANNEL, 
				eventsConfig.getDefaultBlockQueueMaxNumber(), eventsConfig.getDefaultEventConsumerNumber());
		EventsChannelConfig systemConfig = new EventsChannelConfig(EventsChannel.SYSTEM_CHANNEL, 
				eventsConfig.getSystemBlockQueueMaxNumber(), eventsConfig.getSystemEventConsumerNumber());
		map.put(EventsChannel.DEFAULT_CHANNEL, defaultConfig);
		map.put(EventsChannel.SYSTEM_CHANNEL, systemConfig);
		for (Entry<String, EventsChannelConfig> entry : map.entrySet())
		{
			EventsChannel channel = new EventsChannel();
			channel.initialize(entry.getValue());
			channels.put(entry.getKey(), channel);
			if (EventsChannel.DEFAULT_CHANNEL.equals(entry.getKey()))
				defaultChannel = channel;
			if (EventsChannel.SYSTEM_CHANNEL.equals(entry.getKey()))
				systemChannel = channel;
		}
		return defaultChannel != null && systemChannel != null;
	}

	@Override
	public void destroy()
	{
		for (Entry<String, EventsChannel> entry : channels.entrySet())
		{
			entry.getValue().destroy();
		}
	}
	
	public boolean publish(String type, Object obj)
	{
		return publish(new GridEvent(obj, type));
	}
	
	public boolean publish(String channel, String type, Object obj)
	{
		return publish(new GridEvent(obj, type, channel));
	}
	
	public boolean publish(GridEvent event)
	{
		EventsChannel channel = channels.get(event.getChannel());
		if (channel != null)
			return channel.publish(event);
		return false;
	}
	
	public boolean publish(GridNode node, GridEvent event, boolean sync)
	{
		EventsChannel channel = channels.get(event.getChannel());
		if (channel != null)
			return channel.publish(node, event, sync);
		return false;
	}
	
	public boolean publishAll(String type, Object obj)
	{
		return publishAll(new GridEvent(obj, type));
	}
	
	public boolean publishAll(String channel, String type, Object obj)
	{
		return publishAll(new GridEvent(obj, type, channel));
	}
	
	public boolean publishAll(GridEvent event)
	{
		EventsChannel channel = channels.get(event.getChannel());
		if (channel != null)
			return channel.publishAll(event);
		return false;
	}
	
	public boolean publishOthers(String type, Object obj)
	{
		return publishOthers(new GridEvent(obj, type));
	}
	
	public boolean publishOthers(String channel, String type, Object obj)
	{
		return publishOthers(new GridEvent(obj, type, channel));
	}
	
	public boolean publishOthers(GridEvent event)
	{
		List<GridNode> nodes = GridRuntime.nodes().getNodesList();
		for (GridNode node : nodes)
		{
			if (!node.isLocal() && node.isAlive())
				publish(node, event, true);
		}
		return true;
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
