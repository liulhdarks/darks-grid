package darks.grid.beans;

import java.io.Serializable;
import java.util.UUID;

public class GridMessage implements Serializable, Cloneable
{

	private static final long serialVersionUID = -3272844069153062845L;
	
	public static final int MSG_JOIN = 1001;
	
	public static final int MSG_JOIN_REPLY = 1002;
	
	public static final int MSG_HEART_ALIVE = 1003;
	
	public static final int MSG_HEART_ALIVE_REPLY = 1004;
	
	private String id;
	
	private Object data;
	
	private int type;
	
	private boolean success;
	
	private String sourceId;
	
	private int sourceType;

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
	

	public GridMessage(Object data, int type, GridMessage source)
	{
		this.id = UUID.randomUUID().toString();
		this.data = data;
		this.type = type;
		this.sourceId = source.getId();
		this.sourceType = source.getType();
	}
	

	public GridMessage(String id, Object data, int type)
	{
		this.id = id;
		this.data = data;
		this.type = type;
	}


	@SuppressWarnings("unchecked")
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


	public String getSourceId()
	{
		return sourceId;
	}


	public void setSourceId(String sourceId)
	{
		this.sourceId = sourceId;
	}


	public int getSourceType()
	{
		return sourceType;
	}


	public void setSourceType(int sourceType)
	{
		this.sourceType = sourceType;
	}


	@Override
	public String toString()
	{
		return "GridMessage [id=" + id + ", data=" + data + ", type=" + type + ", success="
				+ success + ", sourceId=" + sourceId + ", sourceType=" + sourceType + "]";
	}

	
}
