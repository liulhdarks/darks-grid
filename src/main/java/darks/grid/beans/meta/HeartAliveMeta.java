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
package darks.grid.beans.meta;

import darks.grid.GridContext;

public class HeartAliveMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2008358585638132743L;
	
	private String nodeId;
	
	private GridContext context;
	
	private long timestamp;

	private long pingDelay;

	public HeartAliveMeta()
	{
		
	}
	
	

	public HeartAliveMeta(String nodeId, GridContext context)
	{
		super();
		this.nodeId = nodeId;
		this.context = context;
	}



	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public GridContext context()
	{
		return context;
	}

	public void setContext(GridContext context)
	{
		this.context = context;
	}

	public GridContext getContext()
	{
		return context;
	}


	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public long getPingDelay()
	{
		return pingDelay;
	}

	public void setPingDelay(long pingDelay)
	{
		this.pingDelay = pingDelay;
	}

	@Override
	public String toString()
	{
		return "HeartAliveMeta [nodeId=" + nodeId + ", context=" + context + ", timestamp="
				+ timestamp + ", pingDelay=" + pingDelay + "]";
	}

	
}
