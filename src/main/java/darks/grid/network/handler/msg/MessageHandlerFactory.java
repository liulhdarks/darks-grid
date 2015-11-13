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

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridMessage;

public final class MessageHandlerFactory
{
	
	private static final Logger log = LoggerFactory.getLogger(MessageHandlerFactory.class);
	
	private static Map<Integer, GridMessageHandler> handlersMap = new ConcurrentHashMap<Integer, GridMessageHandler>();
	
	private static Map<Integer, Constructor<? extends GridMessageHandler>> handlersClassMap = new ConcurrentHashMap<Integer, Constructor<? extends GridMessageHandler>>();
	
	static
	{
		handlersMap.put(GridMessage.MSG_JOIN, new JOIN());
		handlersMap.put(GridMessage.MSG_JOIN_REPLY, new JOIN_REPLY());
		handlersMap.put(GridMessage.MSG_HEART_ALIVE, new HEART_ALIVE());
		handlersMap.put(GridMessage.MSG_HEART_ALIVE_REPLY, new HEART_ALIVE());
		handlersMap.put(GridMessage.MSG_MR_REQUEST, new JOB_EXEC());
		handlersMap.put(GridMessage.MSG_MR_RESPONSE, new JOB_EXEC_REPLY());
		handlersMap.put(GridMessage.MSG_EVENT, new EVENT_TRIGGER());
	}
	
	private MessageHandlerFactory()
	{
		
	}
	
	private static void addHandlerClass(int type, Class<? extends GridMessageHandler> clazz)
	{
		try
		{
			Constructor<? extends GridMessageHandler> cst = clazz.getConstructor();
			if (cst != null)
				handlersClassMap.put(type, cst);
			else
				throw new NoSuchMethodException("Cannot find constructor of " + clazz);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	public static GridMessageHandler getHandler(GridMessage message)
	{
		GridMessageHandler handler = handlersMap.get(message.getType());
		if (handler == null)
		{
			Constructor<? extends GridMessageHandler> cst = handlersClassMap.get(message.getType());
			if (cst != null)
			{
				try
				{
					handler = cst.newInstance();
				}
				catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
			}
		}
		return handler;
	}
	
}
