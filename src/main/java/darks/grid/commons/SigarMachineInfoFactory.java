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

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.MachineInfo;
import darks.grid.beans.NodeHealth;

public class SigarMachineInfoFactory extends MachineInfoFactory
{
	private static final Logger log = LoggerFactory.getLogger(SigarMachineInfoFactory.class);

    public SigarMachineInfoFactory()
    {
        log.info("Create sigar machine info factory");
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
	    Sigar sigar = new Sigar();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		info.setUsedMaxMemoryUsage((float) ((double) (maxMemory - freeMemory) / (double) maxMemory));
		info.setUsedTotalMemoryUsage((float) ((double) (totalMemory - freeMemory) / (double) totalMemory));
        Mem memory = null;
        try {
            memory = sigar.getMem();
            info.setUsedPhysicalMemoryUsage((float)((double) memory.getUsed() / (double)memory.getTotal()));
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
        }
		updateProcessCpuUsage(sigar, info);
		updateSystemCpuUsage(sigar, info);
		info.setHealthyScore(NodeHealth.evaluateLocal(info));
		return info;
	}

	private void updateProcessCpuUsage(Sigar sigar, MachineInfo info)
	{
	    try {
	        long pid = sigar.getPid();
	        ProcCpu cpu = sigar.getProcCpu(pid);
	        info.setProcessCpuUsage((float) cpu.getPercent());
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
        }
	}
	
	private void updateSystemCpuUsage(Sigar sigar, MachineInfo info)
	{
        try {
            CpuPerc perc = sigar.getCpuPerc();
            info.setSystemCpuUsage((float) perc.getCombined());
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
        }
	}
}
