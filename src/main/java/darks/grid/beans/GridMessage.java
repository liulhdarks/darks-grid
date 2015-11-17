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
package darks.grid.beans;

import io.netty.util.ReferenceCounted;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import darks.grid.utils.UUIDUtils;

public class GridMessage implements Serializable, Cloneable, ReferenceCounted
{

	private static final long serialVersionUID = -3272844069153062845L;
	
	public static final int MSG_JOIN = 1001;
	
	public static final int MSG_JOIN_REPLY = 1002;
	
	public static final int MSG_HEART_ALIVE = 1003;
	
	public static final int MSG_HEART_ALIVE_REPLY = 1004;
    
    public static final int MSG_MR_REQUEST = 2001;
    
    public static final int MSG_MR_RESPONSE = 2002;
    
    public static final int MSG_EVENT = 3001;
	
	private String id;
	
	private Object data;
	
	private int type;
	
	private boolean success = true;
	
	private String sourceId;
	
	private int sourceType;
	
	private long timestamp = System.nanoTime();
	
	AtomicInteger cnt = new AtomicInteger(0);

	public GridMessage()
	{
		this.id = UUIDUtils.newUUID();
		retain();
	}
	

	public GridMessage(Object data, int type)
	{
		this.id = UUIDUtils.newUUID();
		this.data = data;
		this.type = type;
		retain();
	}
	

	public GridMessage(Object data, int type, GridMessage source)
	{
		this.id = UUIDUtils.newUUID();
		this.data = data;
		this.type = type;
		this.sourceId = source.getId();
		this.sourceType = source.getType();
		retain();
	}
	

	public GridMessage(String id, Object data, int type)
	{
		this.id = id;
		this.data = data;
		this.type = type;
		retain();
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

	public long getTimestamp()
	{
		return timestamp;
	}


	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}


	@Override
	public String toString()
	{
		return "GridMessage [id=" + id + ", data=" + data + ", type=" + type
				+ ", success=" + success + ", sourceId=" + sourceId
				+ ", sourceType=" + sourceType + ", timestamp=" + timestamp
				+ "]";
	}


	@Override
	public int refCnt()
	{
		return cnt.get();
	}


	@Override
	public ReferenceCounted retain()
	{
		cnt.incrementAndGet();
		return this;
	}


	@Override
	public ReferenceCounted retain(int increment)
	{
		cnt.addAndGet(increment);
		return this;
	}

	@Override
	public boolean release()
	{
		return cnt.decrementAndGet() == 0;
	}


	@Override
	public boolean release(int decrement)
	{
		return cnt.addAndGet(-decrement) == 0;
	}

	
}
