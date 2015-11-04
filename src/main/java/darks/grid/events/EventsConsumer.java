package darks.grid.events;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridEvent;

public class EventsConsumer extends Thread
{
	
	private static final Logger log = LoggerFactory.getLogger(EventsConsumer.class);
	
	private BlockingQueue<GridEvent> queue;
	
	private boolean destroyed = false;
	
	public EventsConsumer(BlockingQueue<GridEvent> queue)
	{
		this.queue = queue;
	}
	
	public void run()
	{
		try
		{
			while (!destroyed && !isInterrupted())
			{
				GridEvent event = queue.take();
				if (event != null)
					consumer(event);
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
	
	private void consumer(GridEvent event)
	{
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
