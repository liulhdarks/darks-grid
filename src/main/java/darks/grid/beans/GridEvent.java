package darks.grid.beans;

import io.netty.channel.Channel;

import java.io.Serializable;

public class GridEvent implements Serializable, Cloneable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -58960549212644997L;

	public static final int EVT_NETWORK = 1001;
	
	private String id;
	
	private Object data;
	
	private int type;
	
	private Channel channel;
	
	public GridEvent()
	{
		
	}

	public GridEvent(Object data, int type)
	{
		super();
		this.data = data;
		this.type = type;
	}

	public GridEvent(Object data, int type, Channel channel)
	{
		super();
		this.data = data;
		this.type = type;
		this.channel = channel;
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public <T> T getData()
	{
		return (T) data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}
	
	
}
