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

import darks.grid.GridException;

public class GridNodeType
{
	
	public static final int TYPE_LOCAL = 1;
	private static final String TYPE_LOCAL_STR = "L";
	
	public static final int TYPE_REMOTE = 2;
	private static final String TYPE_REMOTE_STR = "R";
	
	public static String valueOf(int type)
	{
		switch (type)
		{
		case TYPE_LOCAL:
			return TYPE_LOCAL_STR;
		case TYPE_REMOTE:
			return TYPE_REMOTE_STR;
		default:
			throw new GridException("Invalid grid node type " + type);
		}
	}
}
