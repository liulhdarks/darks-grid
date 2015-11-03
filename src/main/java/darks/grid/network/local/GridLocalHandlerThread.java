package darks.grid.network.local;

import io.netty.channel.Channel;

import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.network.GridSession;
import darks.grid.network.GridSessionFactory;
import darks.grid.network.handler.msg.GridMessageHandler;
import darks.grid.network.handler.msg.MessageHandlerFactory;

public class GridLocalHandlerThread extends Thread
{
	private static Logger log = LoggerFactory.getLogger(GridLocalHandlerThread.class);
	
	private Queue<Object> messageQueue;
	
	private volatile boolean destroyed = false;
	
	public GridLocalHandlerThread(Queue<Object> messageQueue)
	{
		this.messageQueue = messageQueue;
	}

	public void run()
	{
		try
		{
			while (!destroyed && !isInterrupted())
			{
				long st = System.currentTimeMillis();
				Object obj = null;
				while ((obj = messageQueue.poll()) != null)
				{
					handlerMessage(obj);
					if (System.currentTimeMillis() - st > 5000)
					{
						st = System.currentTimeMillis();
						Thread.sleep(10);
					}
				}
				Thread.sleep(100);
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
			GridMessage message = (GridMessage) obj;
			if (log.isDebugEnabled())
				log.debug("Local read:" + message);
			GridMessageHandler handler = MessageHandlerFactory.getHandler(message);
			if (handler != null)
			{
				GridNode localNode = GridRuntime.nodes().getLocalNode();
				Channel channel = GridRuntime.network().getServerChannel();
				GridSession session = localNode == null ? GridSessionFactory.getLocalSession(channel) : localNode.getSession();
				handler.handler(session, message);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	
}
