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

public class TimerObject<T>
{

	T object;
	
	long timestamp;
	
	long expireTime;

	public TimerObject(T object, long expireTime)
	{
		super();
		this.object = object;
		this.expireTime = expireTime;
	}

	public TimerObject(T object, long timestamp, long expireTime)
	{
		super();
		this.object = object;
		this.timestamp = timestamp;
		this.expireTime = expireTime;
	}
	
	public boolean isTimeout()
	{
		long st = System.currentTimeMillis() - timestamp;
		return st > expireTime;
	}

	public T getObject()
	{
		return object;
	}

	public void setObject(T object)
	{
		this.object = object;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	

	public long getExpireTime()
	{
		return expireTime;
	}

	public void setExpireTime(long expireTime)
	{
		this.expireTime = expireTime;
	}

	@Override
	public String toString()
	{
		return "TimerObject [object=" + object + ", timestamp=" + timestamp + ", expireTime="
				+ expireTime + "]";
	}
	
}
