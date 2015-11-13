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

import darks.grid.network.GridSession;

public class GridNodeStatus
{
	
	public static final int INVALID = 0;
	
	public static final int ACTIVE = 1;
	
	public static final int INACTIVE = 2;
	
	public static String valueOf(GridNode node)
	{
		GridSession session = node.getSession();
		if (session == null)
			return "INVALID";
		if (node.isAlive())
			return "ALIVE";
		if (session.isActive())
			return "ACTIVE";
		return "INACTIVE";
	}

}
