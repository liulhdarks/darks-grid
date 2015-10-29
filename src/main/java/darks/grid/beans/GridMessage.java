package darks.grid.beans;

import java.io.Serializable;
import java.util.UUID;

public class GridMessage implements Serializable, Cloneable
{

	private static final long serialVersionUID = -3272844069153062845L;
	
	public static final int MSG_JOIN = 1001;
	
	public static final int MSG_JOIN_REPLY = 1002;
	
	private String id;
	
	private Object data;
	
	private int type;
	
	private boolean success;

	public GridMessage()
	{
		this.id = UUID.randomUUID().toString();
	}
	

	public GridMessage(Object data, int type)
	{
		this.id = UUID.randomUUID().toString();
		this.data = data;
		this.type = type;
	}
	

	public GridMessage(String id, Object data, int type)
	{
		this.id = id;
		this.data = data;
		this.type = type;
	}



	public Object getData()
	{
		return data;
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}


	public boolean isSuccess()
	{
		return success;
	}


	public void setSuccess(boolean success)
	{
		this.success = success;
	}
	
}
