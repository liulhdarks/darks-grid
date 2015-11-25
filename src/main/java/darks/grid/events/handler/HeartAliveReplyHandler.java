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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.HeartAliveMeta;
import darks.grid.events.EventsChannel;
import darks.grid.events.GridEventHandler;

public class HeartAliveReplyHandler extends GridEventHandler
{

	private static final Logger log = LoggerFactory.getLogger(HeartAliveReplyHandler.class);
	
	@Override
	public void handle(GridEvent event) throws Exception
	{
		GridMessage msg = event.getData();
		boolean valid = true;
		try
		{
		    GridRuntime.context().getMachineInfo().update();
			HeartAliveMeta replyMeta = new HeartAliveMeta(GridRuntime.context().getLocalNodeId(), GridRuntime.context());
			replyMeta.setTimestamp(System.currentTimeMillis());
			GridMessage replyMsg = new GridMessage(replyMeta, GridMessage.MSG_HEART_ALIVE_REPLY, msg);
			valid = msg.getSession().sendSyncMessage(replyMsg);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			valid = false;
		}
		if (!valid)
		{
			HeartAliveMeta meta = msg.getData();
			String nodeId = meta.getNodeId();
			GridNode node = GridRuntime.nodes().getNode(nodeId);
			log.error("Fail to reply heart alive. Maybe node " + nodeId + " lost.");
			GridRuntime.events().publish(EventsChannel.SYSTEM_CHANNEL, GridEvent.NODE_LEAVE, node);
		}
	}

}
