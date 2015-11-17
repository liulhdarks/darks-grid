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

package darks.grid.master;

import java.util.List;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.rpc.RpcResult;


public class ModeratorElectionStrategy extends ElectionStrategy
{

	@Override
	public void elect()
	{
		requestHealthyScore();
	}

	private void requestHealthyScore()
	{
		ExecuteConfig config = new ExecuteConfig();
		RpcResult result = RpcExecutor.callMethod(ElectRpcInvoker.class, "computeHealthy", null, config);
		List<Object> list = result.getResult();
		List<String> nodeIds = result.getNodeIds();
		if (list == null || list.isEmpty())
		{
			
		}
		else
		{
			for (int i = 0; i < list.size(); i++)
			{
				Integer score = (Integer)list.get(i);
				score = score == null ? 0 : score;
				String nodeId = nodeIds.get(i);
				System.out.println(nodeId + " " + score);
			}
		}
	}
}
