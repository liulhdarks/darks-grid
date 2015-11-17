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

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class GridFuture<V>
{

	public abstract boolean isSuccess();

	public abstract boolean await();
	
	public abstract boolean await(int timeout, TimeUnit unit);
	
	public V get()
	{
		List<V> list = getList();
		if (list != null && !list.isEmpty())
			return list.get(0);
		else
			return null;
	}
	
	public abstract List<V> getList();
}
