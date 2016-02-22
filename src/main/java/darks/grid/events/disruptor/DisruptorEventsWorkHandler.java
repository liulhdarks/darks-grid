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

package darks.grid.events.disruptor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.WorkHandler;

import darks.grid.beans.GridEvent;
import darks.grid.events.EventsHandlerFactory;
import darks.grid.events.GridEventHandler;
import darks.grid.utils.GridStatistic;

public class DisruptorEventsWorkHandler implements WorkHandler<GridEvent> 
{

    private static final Logger log = LoggerFactory.getLogger(DisruptorEventsWorkHandler.class);
    
    String channelName = null;
    
    public DisruptorEventsWorkHandler(String channelName)
    {
        this.channelName = channelName;
    }
    
    @Override
    public void onEvent(GridEvent event) throws Exception 
    {
        long delay = System.currentTimeMillis() - event.getEnqueueTimestamp();
        GridStatistic.incrementEventDelay(channelName, delay);
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
