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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridEvent;
import darks.grid.events.handler.ConfirmMasterHandler;
import darks.grid.events.handler.ConnectionActiveHandler;
import darks.grid.events.handler.HeartAliveReplyHandler;
import darks.grid.events.handler.JoinReplyHandler;
import darks.grid.events.handler.JoinRequestHandler;
import darks.grid.events.handler.MergeNodesHandler;
import darks.grid.events.handler.NodeJoinHandler;
import darks.grid.events.handler.NodeLeaveHandler;

public final class EventsHandlerFactory
{
	
	private static final Logger log = LoggerFactory.getLogger(EventsHandlerFactory.class);
	
	private static Map<String, List<GridEventHandler>> handlersMap = new ConcurrentHashMap<String, List<GridEventHandler>>();
	
	private static Map<String, List<Constructor<? extends GridEventHandler>>> handlersClassMap = new ConcurrentHashMap<String, List<Constructor<? extends GridEventHandler>>>();
	
	private static Lock mutex = new ReentrantLock();
	
	static
	{
        addHandler(GridEvent.CONNECT_ACTIVE, new ConnectionActiveHandler());
		addHandler(GridEvent.NODE_JOIN, new NodeJoinHandler());
		addHandler(GridEvent.NODE_LEAVE, new NodeLeaveHandler());
		addHandler(GridEvent.MERGE_NODES, new MergeNodesHandler());
		addHandler(GridEvent.CONFIRM_MASTER, new ConfirmMasterHandler());
		addHandler(GridEvent.HEART_ALIVE_REPLY, new HeartAliveReplyHandler());
		addHandler(GridEvent.JOIN_REQUEST, new JoinRequestHandler());
		addHandler(GridEvent.JOIN_REQUEST_REPLY, new JoinReplyHandler());
	}
	
	private EventsHandlerFactory()
	{
		
	}
	
	public static void addHandler(String String, GridEventHandler handler)
	{
		List<GridEventHandler> handlers = getInternHandlers(String);
		handlers.add(handler);
	}
	
	public static void addHandler(String String, Class<? extends GridEventHandler> clazz)
	{
		try
		{
			Constructor<? extends GridEventHandler> cst = clazz.getConstructor();
			if (cst != null)
			{
				List<Constructor<? extends GridEventHandler>> list = getInternHandlerClass(String);
				list.add(cst);
			}
			else
				throw new NoSuchMethodException("Cannot find constructor of " + clazz);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	public static List<GridEventHandler> getHandler(String String)
	{
		List<GridEventHandler> handlers = getInternHandlers(String);
		if (handlers == null)
		{
			List<Constructor<? extends GridEventHandler>> classList = getInternHandlerClass(String);
			if (classList != null && !classList.isEmpty())
			{
				handlers = new ArrayList<GridEventHandler>(classList.size());
				for (Constructor<? extends GridEventHandler> cst : classList)
				{
					if (cst != null)
					{
						try
						{
							handlers.add(cst.newInstance());
						}
						catch (Exception e)
						{
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		return handlers;
	}
	
	private static List<GridEventHandler> getInternHandlers(String String)
	{
		List<GridEventHandler> handlers = handlersMap.get(String);
		if (handlers == null)
		{
			mutex.lock();
			try
			{
				handlers = handlersMap.get(String);
				if (handlers == null)
				{
					handlers = new CopyOnWriteArrayList<GridEventHandler>();
					handlersMap.put(String, handlers);
				}
			}
			finally
			{
				mutex.unlock();
			}
		}
		return handlers;
	}
	
	private static List<Constructor<? extends GridEventHandler>> getInternHandlerClass(String String)
	{
		List<Constructor<? extends GridEventHandler>> handlers = handlersClassMap.get(String);
		if (handlers == null)
		{
			mutex.lock();
			try
			{
				handlers = handlersClassMap.get(String);
				if (handlers == null)
				{
					handlers = new CopyOnWriteArrayList<Constructor<? extends GridEventHandler>>();
					handlersClassMap.put(String, handlers);
				}
			}
			finally
			{
				mutex.unlock();
			}
		}
		return handlers;
	}
}
