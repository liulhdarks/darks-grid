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

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class GridEvent implements Serializable, Cloneable
{

    public static final String CONNECT_ACTIVE = "connect_active";
	public static final String NODE_JOIN = "node_join";
	public static final String NODE_LEAVE = "node_leave";
	public static final String MERGE_NODES = "merge_nodes";
	public static final String CONFIRM_MASTER = "confirm_master";
	
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

	@SuppressWarnings("unchecked")
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
