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
package darks.grid.config;

public class EventsConfig
{

	private boolean useBlockQueue = true;
	
	private int blockQueueMaxNumber = Integer.MAX_VALUE;
	
	private int eventConsumerNumber = Runtime.getRuntime().availableProcessors();
	
	public EventsConfig()
	{
		
	}

	public boolean isUseBlockQueue()
	{
		return useBlockQueue;
	}

	public void setUseBlockQueue(boolean useBlockQueue)
	{
		this.useBlockQueue = useBlockQueue;
	}

	public int getBlockQueueMaxNumber()
	{
		return blockQueueMaxNumber;
	}

	public void setBlockQueueMaxNumber(int blockQueueMaxNumber)
	{
		this.blockQueueMaxNumber = blockQueueMaxNumber;
	}

	public int getEventConsumerNumber()
	{
		return eventConsumerNumber;
	}

	public void setEventConsumerNumber(int eventConsumerNumber)
	{
		this.eventConsumerNumber = eventConsumerNumber;
	}

	@Override
	public String toString()
	{
		return "EventsConfig [useBlockQueue=" + useBlockQueue + ", blockQueueMaxNumber="
				+ blockQueueMaxNumber + ", eventConsumerNumber=" + eventConsumerNumber + "]";
	}
	
}
