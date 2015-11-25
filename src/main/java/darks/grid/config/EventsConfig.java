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
package darks.grid.config;

import java.util.HashMap;
import java.util.Map;

public class EventsConfig
{

	private int defaultBlockQueueMaxNumber = Integer.MAX_VALUE;
	
	private int defaultEventConsumerNumber = Runtime.getRuntime().availableProcessors();

	private int systemBlockQueueMaxNumber = Integer.MAX_VALUE;
	
	private int systemEventConsumerNumber = Runtime.getRuntime().availableProcessors();
	
	private Map<String, EventsChannelConfig> channelsConfig = new HashMap<>();
	
	public EventsConfig()
	{
		
	}

	public int getDefaultBlockQueueMaxNumber()
	{
		return defaultBlockQueueMaxNumber;
	}

	public void setDefaultBlockQueueMaxNumber(int defaultBlockQueueMaxNumber)
	{
		this.defaultBlockQueueMaxNumber = defaultBlockQueueMaxNumber;
	}

	public int getDefaultEventConsumerNumber()
	{
		return defaultEventConsumerNumber;
	}

	public void setDefaultEventConsumerNumber(int defaultEventConsumerNumber)
	{
		this.defaultEventConsumerNumber = defaultEventConsumerNumber;
	}

	public int getSystemBlockQueueMaxNumber()
	{
		return systemBlockQueueMaxNumber;
	}

	public void setSystemBlockQueueMaxNumber(int systemBlockQueueMaxNumber)
	{
		this.systemBlockQueueMaxNumber = systemBlockQueueMaxNumber;
	}

	public int getSystemEventConsumerNumber()
	{
		return systemEventConsumerNumber;
	}

	public void setSystemEventConsumerNumber(int systemEventConsumerNumber)
	{
		this.systemEventConsumerNumber = systemEventConsumerNumber;
	}
	
	

	public Map<String, EventsChannelConfig> getChannelsConfig()
	{
		return channelsConfig;
	}


	@Override
	public String toString()
	{
		return "EventsConfig [defaultBlockQueueMaxNumber=" + defaultBlockQueueMaxNumber
				+ ", defaultEventConsumerNumber=" + defaultEventConsumerNumber
				+ ", systemBlockQueueMaxNumber=" + systemBlockQueueMaxNumber
				+ ", systemEventConsumerNumber=" + systemEventConsumerNumber + ", channelsConfig="
				+ channelsConfig + "]";
	}


	public static class EventsChannelConfig
	{
		String name;

		int blockQueueMaxNumber = Integer.MAX_VALUE;
		
		int eventConsumerNumber = Runtime.getRuntime().availableProcessors();

		public EventsChannelConfig()
		{
		}

		public EventsChannelConfig(String name, int blockQueueMaxNumber, int eventConsumerNumber)
		{
			this.name = name;
			this.blockQueueMaxNumber = blockQueueMaxNumber;
			this.eventConsumerNumber = eventConsumerNumber;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public int getBlockQueueMaxNumber()
		{
			return blockQueueMaxNumber;
		}

		public void setBlockQueueMaxNumber(int blockQueueMaxNumber)
		{
			this.blockQueueMaxNumber = blockQueueMaxNumber;
		}

		public int getEventConsumerNumber()
		{
			return eventConsumerNumber;
		}

		public void setEventConsumerNumber(int eventConsumerNumber)
		{
			this.eventConsumerNumber = eventConsumerNumber;
		}

		@Override
		public String toString()
		{
			return "EventsChannelConfig [name=" + name + ", blockQueueMaxNumber="
					+ blockQueueMaxNumber + ", eventConsumerNumber=" + eventConsumerNumber + "]";
		}
		
	}
	
}
