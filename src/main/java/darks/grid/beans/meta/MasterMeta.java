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

import java.net.InetSocketAddress;

public class MasterMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7521512429952965854L;

	private String nodeId;
	
	private InetSocketAddress address;
	
	public MasterMeta()
	{
		
	}

	public MasterMeta(String nodeId, InetSocketAddress address)
	{
		super();
		this.nodeId = nodeId;
		this.address = address;
	}

	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public InetSocketAddress getAddress()
	{
		return address;
	}

	public void setAddress(InetSocketAddress address)
	{
		this.address = address;
	}

	@Override
	public String toString()
	{
		return "MasterMeta [nodeId=" + nodeId + ", address=" + address + "]";
	}
}
