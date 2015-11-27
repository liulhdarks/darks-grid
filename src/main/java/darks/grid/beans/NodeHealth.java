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

import darks.grid.GridRuntime;
import darks.grid.utils.GridStatistic;

public final class NodeHealth
{
	
	private static final long MAX_PING_DELAY = 1000; 
	
	private static final long MAX_NETWORK_DELAY = 3000; 
	
	private static final float PROP_CPU = 0.3f;
	
	private static final float PROP_MEN = 0.2f;
	
	private static final float PROP_REMOTE = 0.15f;
	
	private static final float PROP_LOCAL = 0.05f;
	
	private static final float PROP_PING = 0.3f;

	private NodeHealth()
	{
		
	}
	
	public static int evaluateLocal(MachineInfo info)
	{
		long pingDelaySum = 0;
		int remoteNodeSize = 0;
		for (GridNode node : GridRuntime.nodes().getNodesList())
		{
			if (!node.isLocal())
			{
				remoteNodeSize++;
				pingDelaySum += node.getPingDelay();
			}
		}
		long maxPingDelay = MAX_PING_DELAY * remoteNodeSize;
		float pingScore = (maxPingDelay == 0 || maxPingDelay <= pingDelaySum) ? 0 :
					(((float) (maxPingDelay - pingDelaySum) / (float) maxPingDelay) * 100);
		long remoteDelay = GridStatistic.getAvgDelay();
		long localDelay = GridStatistic.getAvgLocalDelay();
		float remoteScore = MAX_NETWORK_DELAY <= remoteDelay ? 0 :
				(((float) (MAX_NETWORK_DELAY - remoteDelay) / (float) MAX_NETWORK_DELAY) * 100);
		float localScore = MAX_NETWORK_DELAY <= localDelay ? 0 :
				(((float) (MAX_NETWORK_DELAY - localDelay) / (float) MAX_NETWORK_DELAY) * 100);
		float cpuUsage = info.getSystemCpuUsage();
		if (cpuUsage < 0) cpuUsage = 0.f;
		float cpuScore = 100.f - cpuUsage * 100.f;
		float memUsage = info.getUsedTotalMemoryUsage();
		if (memUsage < 0) memUsage = 0.f;
		float menScore = 100.f - memUsage * 100.f;
		int score = (int) (cpuScore * PROP_CPU + menScore * PROP_MEN + pingScore * PROP_PING +
				remoteScore * PROP_REMOTE + localScore * PROP_LOCAL);
		return score;
	}
	
}
