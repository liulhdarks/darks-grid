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
	
	private Map<String, EventsChannelConfig> channelsConfig = new HashMap<String, EventsChannelConfig>();
	
	public EventsConfig()
	{
		
	}

    public Map<String, EventsChannelConfig> getChannelsConfig()
	{
		return channelsConfig;
	}
	

    @Override
	public String toString()
	{
		return "EventsConfig [" + channelsConfig + "]";
	}


	public static class EventsChannelConfig
	{
		String name;

		int blockQueueMaxNumber = Integer.MAX_VALUE;
		
		int eventConsumerNumber = Runtime.getRuntime().availableProcessors();
		
		String channelType = EventChannelType.DEFAULT.toString();

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
		

		public String getChannelType() 
		{
            return channelType;
        }

        public void setChannelType(String channelType) 
        {
            this.channelType = channelType;
        }

        @Override
		public String toString()
		{
			return "EventsChannelConfig [name=" + name + ", blockQueueMaxNumber="
					+ blockQueueMaxNumber + ", eventConsumerNumber=" + eventConsumerNumber + "]";
		}
		
	}
	
	public static enum EventChannelType
	{
	    DEFAULT("default"), 
	    DISRUPTOR("disruptor");
	    
	    String name;
	    
	    EventChannelType(String name) 
	    {
	        this.name = name;
	    }
	    
	    public static EventChannelType typeOf(String name)
	    {
	        name = name.toLowerCase().trim();
	        if ("disruptor".equals(name))
	            return DISRUPTOR;
	        else if ("default".equals(name))
                return DEFAULT;
	        else 
	            return null;
	    }
	    
	    public String toString()
	    {
	        return name;
	    }
	}
	
}
