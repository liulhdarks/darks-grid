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

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.config.EventsConfig.EventsChannelConfig;
import darks.grid.utils.GridStatistic;

public abstract class EventsChannel
{
	
	public static final String DEFAULT_CHANNEL = "default";
	public static final String SYSTEM_CHANNEL = "system";
	
	protected String name;
	
	public EventsChannel()
	{
		
	}
	
	public boolean initialize(EventsChannelConfig config)
	{
		this.name = config.getName();
		return true;
	}
	
	public void destroy()
	{
		
	}
	
	public boolean publish(GridNode node, GridEvent event, boolean sync)
	{
		if (node.isLocal())
		{
			return enqueueEvent(event, sync);
		}
		else
		{
			GridMessage message = new GridMessage(event, GridMessage.MSG_EVENT);
			if (sync)
				return node.sendSyncMessage(message);
			else
				return node.sendMessage(message);
		}
	}
	
	public boolean publish(GridEvent event)
	{
		return enqueueEvent(event, false);
	}
	
	private boolean enqueueEvent(GridEvent event, boolean sync)
	{
	    if (event.getSession() == null) {
	        event.setSession(GridRuntime.network().getServerSession());
	    }
		GridStatistic.incrementEventCount(name);
		event.setEnqueueTimestamp(System.currentTimeMillis());
		return offerQueue(event, sync);
	}
	
	protected abstract boolean offerQueue(GridEvent event, boolean sync);
	
	public abstract String getChannelInfo();

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
	
}
