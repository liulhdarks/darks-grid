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
package darks.grid.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.MachineInfo;
import darks.grid.beans.NodeHealth;

public class BlankMachineInfoFactory extends MachineInfoFactory
{
	private static final Logger log = LoggerFactory.getLogger(BlankMachineInfoFactory.class);


    public BlankMachineInfoFactory()
    {
        log.info("Create blank machine info factory");
    }
	
    @Override
	public MachineInfo createMachineInfo()
	{
		MachineInfo info = new MachineInfo();
		return updateMachineInfo(info);
	}

	@Override
	public MachineInfo updateMachineInfo(MachineInfo info)
	{
		long maxMemory = Runtime.getRuntime().maxMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		info.setUsedMaxMemoryUsage((float) ((double) (maxMemory - freeMemory) / (double) maxMemory));
		info.setUsedTotalMemoryUsage((float) ((double) (totalMemory - freeMemory) / (double) totalMemory));
		info.setProcessCpuUsage(-1f);
		info.setSystemCpuUsage(-1f);
		info.setHealthyScore(NodeHealth.evaluateLocal(info));
		return info;
	}
	
}
