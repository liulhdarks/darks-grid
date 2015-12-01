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
package darks.grid.network.handler.msg;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.HeartAliveMeta;
import darks.grid.events.EventsChannel;
import darks.grid.network.GridSession;

public class HEART_ALIVE implements GridMessageHandler
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		long arriveTime = System.currentTimeMillis();
		HeartAliveMeta meta = msg.getData();
		String nodeId = meta.getNodeId();
		GridNode node = GridRuntime.nodes().getNode(nodeId);
		if (node == null)
		{
			node = GridRuntime.nodes().addRemoteNode(nodeId, session, meta.context());
		}
		else
		{
			node.setHeartAliveTime(System.currentTimeMillis());
			node.context().setMachineInfo(meta.context().getMachineInfo());
		}
		node.setPingDelay(Math.abs(arriveTime - meta.getTimestamp()));
		//HEART ALIVE REPLY
		if (msg.getType() == GridMessage.MSG_HEART_ALIVE)
			GridRuntime.events().publish(EventsChannel.SYSTEM_CHANNEL, GridEvent.HEART_ALIVE_REPLY, msg);
	}
}
