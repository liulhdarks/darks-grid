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


public class JoinNodeMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3103532011798509755L;

	private String nodeId;
	
	private GridContext nodeContext;
	
	public JoinNodeMeta()
	{
		
	}
	

	public JoinNodeMeta(String nodeId, GridContext nodeContext)
	{
		this.nodeId = nodeId;
		this.nodeContext = nodeContext;
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
		return nodeContext;
	}


	public void setNodeContext(GridContext nodeContext)
	{
		this.nodeContext = nodeContext;
	}


	@Override
	public String toString()
	{
		return "JoinNodeMeta [nodeId=" + nodeId + ", nodeContext=" + nodeContext + "]";
	}

}
