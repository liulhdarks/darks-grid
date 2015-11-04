package darks.grid.events;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridEvent;
import darks.grid.config.GridConfiguration;

public class GridEventsManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridEventsManager.class);

	BlockingQueue<GridEvent> eventQueue = null;
	
	ExecutorService threadPool = null;
	
	List<EventsConsumer> concumers = new LinkedList<>();
	
	public GridEventsManager()
	{
		
	}
	
	public boolean initialize(GridConfiguration config)
	{
		log.info("Start to initialize events manager.");
		eventQueue = new LinkedBlockingQueue<>(config.getEventsConfig().getBlockQueueMaxNumber());
		int threadSize = config.getEventsConfig().getEventConsumerNumber();
		threadPool = Executors.newFixedThreadPool(threadSize);
		for (int i = 0; i < threadSize; i++)
		{
			EventsConsumer consumer = new EventsConsumer(eventQueue);
			threadPool.execute(consumer);
			concumers.add(consumer);
		}
		return true;
	}
	
	public void destroy()
	{
		for (EventsConsumer consumer : concumers)
		{
			consumer.destroy();
		}
		threadPool.shutdownNow();
	}
	
	public boolean publish(String type, Object obj)
	{
		return publish(new GridEvent(obj, type));
	}
	
	public boolean publish(GridEvent event)
	{
		return eventQueue.offer(event);
	}
	
	public void addHandler(String eventType, GridEventHandler handler)
	{
		EventsHandlerFactory.addHandler(eventType, handler);
	}
	
	public void addHandler(String eventType, Class<? extends GridEventHandler> clazz)
	{
		EventsHandlerFactory.addHandler(eventType, clazz);
	}
		
}
