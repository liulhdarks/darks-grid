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
package darks.grid.events.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.events.EventsChannel;
import darks.grid.events.GridEventHandler;
import darks.grid.network.GridSession;

public class ConnectionActiveHandler extends GridEventHandler
{
    
    private static final Logger log = LoggerFactory.getLogger(ConnectionActiveHandler.class);
    
    private static final int AWAIT_READY_TIMEOUT = 5;
    
    @Override
    public void handle(GridEvent event)
        throws Exception
    {
        GridSession session = event.getData();
        if (GridRuntime.awaitReady(AWAIT_READY_TIMEOUT, TimeUnit.SECONDS))
        {
            JoinMeta meta = new JoinMeta(GridRuntime.getLocalId(), GridRuntime.context());
            GridEvent joinEvent = new GridEvent(meta, GridEvent.JOIN_REQUEST, EventsChannel.SYSTEM_CHANNEL);
            GridRuntime.events().publish(session, joinEvent, true);
        }
        else
        {
            log.warn("Wait for runtime ready timeout. Cancel to send join request.");
        }
    }
    
}
