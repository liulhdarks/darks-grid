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

import java.net.SocketAddress;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.beans.meta.JoinNodeMeta;
import darks.grid.network.GridSession;

public class JOIN_REPLY implements GridMessageHandler
{
	private static final Logger log = LoggerFactory.getLogger(JOIN_REPLY.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		JoinNodeMeta meta = msg.getData();
		String nodeId = meta.getNodeId();
		synchronized (nodeId.intern())
		{
			Map<SocketAddress, JoinMeta> nodesMap = GridRuntime.network().getWaitJoin(nodeId);
			for (Entry<SocketAddress, JoinMeta> entry : nodesMap.entrySet())
			{
				try
				{
					GridSession entrySession = entry.getValue().getSession();
					if (entrySession.getId().equals(session.getId()))
						continue;
					entrySession.close();
				}
				catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
			}
			nodesMap.clear();
			GridRuntime.nodes().addRemoteNode(meta.getNodeId(), session, meta.context());
		}
	}
}
