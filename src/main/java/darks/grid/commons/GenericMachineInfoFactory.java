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

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

import darks.grid.beans.MachineInfo;
import darks.grid.beans.NodeHealth;
import darks.grid.utils.MachineUtils;
import darks.grid.utils.OSUtils;
import darks.grid.utils.ReflectUtils;

@SuppressWarnings("restriction")
public class GenericMachineInfoFactory extends MachineInfoFactory
{
	private static final Logger log = LoggerFactory.getLogger(GenericMachineInfoFactory.class);

	public GenericMachineInfoFactory()
	{
	    log.info("Create generic machine info factory");
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
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		info.setUsedPhysicalMemoryUsage(MachineUtils.getPhysicalMemoryUsage());
		info.setUsedMaxMemoryUsage((float) ((double) (maxMemory - freeMemory) / (double) maxMemory));
		info.setUsedTotalMemoryUsage((float) ((double) (totalMemory - freeMemory) / (double) totalMemory));
		updateProcessCpuUsage(osmxb, info);
		updateSystemCpuUsage(osmxb, info);
		info.setHealthyScore(NodeHealth.evaluateLocal(info));
		return info;
	}

	private void updateProcessCpuUsage(OperatingSystemMXBean osmxb, MachineInfo info)
	{
        Method method = ReflectUtils.getDeepMethod(OperatingSystemMXBean.class, "getProcessCpuLoad");
		if (method != null)
		{
			Object ret = ReflectUtils.invokeMethod(osmxb, method);
			if (ret != null)
			{
				info.setProcessCpuUsage(((Double) ret).floatValue());
			}
		}
		else
		{
		    if (OSUtils.isWindows())
		        info.setProcessCpuUsage(MachineUtils.getCpuUsageForWindows());
		    else
                info.setProcessCpuUsage(MachineUtils.getCpuUsageForLinux());
		}
	}

	private void updateSystemCpuUsage(OperatingSystemMXBean osmxb, MachineInfo info)
	{
		Method method = ReflectUtils.getDeepMethod(OperatingSystemMXBean.class, "getSystemCpuLoad");
		if (method != null)
		{
			Object ret = ReflectUtils.invokeMethod(osmxb, method);
			if (ret != null)
			{
				info.setSystemCpuUsage(((Double) ret).floatValue());
			}
		}
        else
        {
            if (OSUtils.isWindows())
                info.setSystemCpuUsage(MachineUtils.getCpuUsageForWindows());
            else
                info.setSystemCpuUsage(MachineUtils.getCpuUsageForLinux());
        }
	}
}
