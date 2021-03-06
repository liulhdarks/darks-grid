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

package darks.grid.events.generic;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridEvent;
import darks.grid.config.EventsConfig.EventsChannelConfig;
import darks.grid.events.EventsChannel;
import darks.grid.utils.GridStatistic;
import darks.grid.utils.GridStatistic.EventStatistic;

public class GenericEventsChannel extends EventsChannel
{
	
	private static final Logger log = LoggerFactory.getLogger(GenericEventsChannel.class);

	public static final String DEFAULT_CHANNEL = "default";
	public static final String SYSTEM_CHANNEL = "system";
	
	BlockingQueue<GridEvent> eventQueue = null;
	
	List<EventsConsumer> concumers = new LinkedList<EventsConsumer>();
	
	ExecutorService threadPool = null;
	
	public GenericEventsChannel()
	{
		
	}

    @Override
	public boolean initialize(EventsChannelConfig config)
	{
		super.initialize(config);
		eventQueue = new LinkedBlockingQueue<GridEvent>(config.getBlockQueueMaxNumber());
		int threadSize = config.getEventConsumerNumber();
		threadPool = Executors.newFixedThreadPool(threadSize);
		log.info("Initialize generic events channel " + config.getName() + " with " + config);
		for (int i = 0; i < threadSize; i++)
		{
			EventsConsumer consumer = new EventsConsumer(name, eventQueue);
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

    @Override
    protected boolean offerQueue(GridEvent event, boolean sync) {
        if (sync)
        {
            try
            {
                eventQueue.put(event);
                return true;
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                return false;
            }
        }
        else
            return eventQueue.offer(event);
    }

    @Override
	public String getChannelInfo()
	{
		EventStatistic stat = GridStatistic.getEventStat(name);
		StringBuilder buf = new StringBuilder();
		buf.append(name)
	    	.append("\tcount:").append(stat.getEventCount())
		    .append(" remain:").append(eventQueue.size())
			.append(" avg-delay:").append(stat.getAvgEventDelay())
			.append(" max-delay:").append(stat.getMaxEventDelay());
		return buf.toString();
	}
}
