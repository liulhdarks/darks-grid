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

import java.io.Serializable;

import darks.grid.utils.UUIDUtils;

public abstract class BaseMeta implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3863318610098206811L;
	
	private String metaId = UUIDUtils.newUUID();

	public BaseMeta()
	{
		
	}

	public String getMetaId()
	{
		return metaId;
	}

	public void setMetaId(String metaId)
	{
		this.metaId = metaId;
	}
	
	
}
