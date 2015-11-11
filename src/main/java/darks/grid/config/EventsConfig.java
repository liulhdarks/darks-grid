package darks.grid.config;

public class EventsConfig
{

	private boolean useBlockQueue = true;
	
	private int blockQueueMaxNumber = Integer.MAX_VALUE;
	
	private int eventConsumerNumber = Runtime.getRuntime().availableProcessors();
	
	public EventsConfig()
	{
		
	}

	public boolean isUseBlockQueue()
	{
		return useBlockQueue;
	}

	public void setUseBlockQueue(boolean useBlockQueue)
	{
		this.useBlockQueue = useBlockQueue;
	}

	public int getBlockQueueMaxNumber()
	{
		return blockQueueMaxNumber;
	}

	public void setBlockQueueMaxNumber(int blockQueueMaxNumber)
	{
		this.blockQueueMaxNumber = blockQueueMaxNumber;
	}

	public int getEventConsumerNumber()
	{
		return eventConsumerNumber;
	}

	public void setEventConsumerNumber(int eventConsumerNumber)
	{
		this.eventConsumerNumber = eventConsumerNumber;
	}

	@Override
	public String toString()
	{
		return "EventsConfig [useBlockQueue=" + useBlockQueue + ", blockQueueMaxNumber="
				+ blockQueueMaxNumber + ", eventConsumerNumber=" + eventConsumerNumber + "]";
	}
	
}
