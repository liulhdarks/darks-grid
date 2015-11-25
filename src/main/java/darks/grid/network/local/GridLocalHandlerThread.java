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
package darks.grid.network.local;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.network.GridSession;
import darks.grid.network.handler.msg.GridMessageHandler;
import darks.grid.network.handler.msg.MessageHandlerFactory;
import darks.grid.utils.GridStatistic;

public class GridLocalHandlerThread extends Thread
{
	private static Logger log = LoggerFactory.getLogger(GridLocalHandlerThread.class);
	
	private BlockingQueue<Object> messageQueue;
	
	private volatile boolean destroyed = false;
	
	public GridLocalHandlerThread(BlockingQueue<Object> messageQueue)
	{
		this.messageQueue = messageQueue;
	}

	public void run()
	{
		try
		{
			long st = System.currentTimeMillis();
			while (!destroyed && !isInterrupted())
			{
				Object obj = messageQueue.take();
				handlerMessage(obj);
				if (System.currentTimeMillis() - st > 10000)
				{
					st = System.currentTimeMillis();
					Thread.sleep(0);
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	public void destroy()
	{
		destroyed = true;
		interrupt();
	}
	
	private void handlerMessage(Object obj)
	{
		try
		{
			long arriveTime = System.currentTimeMillis();
			GridMessage message = (GridMessage) obj;
			if (log.isDebugEnabled())
				log.debug("Local read:" + message);
			GridMessageHandler handler = MessageHandlerFactory.getHandler(message);
			if (handler != null)
			{
				GridSession session = GridRuntime.network().getServerSession();
				message.setSession(session);
				if (session == null)
					log.error("Invalid server session.");
				else
					handler.handler(session, message);
			}
			GridStatistic.incrementLocalDelay(arriveTime - message.getTimestamp());
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	
}
