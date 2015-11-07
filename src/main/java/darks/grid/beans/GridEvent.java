package darks.grid.beans;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class GridEvent implements Serializable, Cloneable
{

    public static final String CONNECT_ACTIVE = "connect_active";
	public static final String NODE_JOIN = "node_join";
	public static final String NODE_LEAVE = "node_leave";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -58960549212644997L;
	
	private static AtomicLong eventIdSeed = new AtomicLong(0);
	
	private long id;
	
	private Object data;
	
	private String type;
	
	public GridEvent()
	{
		this.id = eventIdSeed.incrementAndGet();
	}

	public GridEvent(Object data, String type)
	{
		this.id = eventIdSeed.incrementAndGet();
		this.data = data;
		this.type = type;
	}

	public <T> T getData()
	{
		return (T) data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return "GridEvent [id=" + id + ", data=" + data + ", type=" + type + "]";
	}

	
}
