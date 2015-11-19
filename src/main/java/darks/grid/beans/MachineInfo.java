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

import java.io.Serializable;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import darks.grid.utils.MachineUtils;

@SuppressWarnings("restriction")
public class MachineInfo implements Serializable
{

    private static final long serialVersionUID = 1080482398147164650L;
    
    private float usedPhysicalMemoryUsage;
    
    private float processCpuUsage;
    
    private float systemCpuUsage;
    
    private float usedMaxMemoryUsage;
    
    private float usedTotalMemoryUsage;
    
    private int healthyScore = 100;

    public MachineInfo()
    {
        update();
    }
    
    public synchronized void update()
    {
        usedPhysicalMemoryUsage = MachineUtils.getPhysicalMemoryUsage();
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();  
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();   
        long freeMemory = Runtime.getRuntime().freeMemory(); 
        usedMaxMemoryUsage = (float)((double) (maxMemory - freeMemory) / (double) maxMemory);
        usedTotalMemoryUsage = (float)((double) (totalMemory - freeMemory) / (double) totalMemory);
        processCpuUsage = (float) osmxb.getProcessCpuLoad();
        systemCpuUsage = (float) osmxb.getSystemCpuLoad();
        healthyScore = NodeHealth.evaluateLocal(this);
    }

    public synchronized float getUsedPhysicalMemoryUsage()
    {
        return usedPhysicalMemoryUsage;
    }

    public synchronized void setUsedPhysicalMemoryUsage(float usedPhysicalMemoryUsage)
    {
        this.usedPhysicalMemoryUsage = usedPhysicalMemoryUsage;
    }

    public synchronized float getProcessCpuUsage()
    {
        return processCpuUsage;
    }

    public synchronized void setProcessCpuUsage(float processCpuUsage)
    {
        this.processCpuUsage = processCpuUsage;
    }

    public synchronized float getSystemCpuUsage()
    {
        return systemCpuUsage;
    }

    public synchronized void setSystemCpuUsage(float systemCpuUsage)
    {
        this.systemCpuUsage = systemCpuUsage;
    }

    public synchronized float getUsedMaxMemoryUsage()
    {
        return usedMaxMemoryUsage;
    }

    public synchronized void setUsedMaxMemoryUsage(float usedMaxMemoryUsage)
    {
        this.usedMaxMemoryUsage = usedMaxMemoryUsage;
    }

    public synchronized float getUsedTotalMemoryUsage()
    {
        return usedTotalMemoryUsage;
    }

    public synchronized void setUsedTotalMemoryUsage(float usedTotalMemoryUsage)
    {
        this.usedTotalMemoryUsage = usedTotalMemoryUsage;
    }

    public synchronized int getHealthyScore()
	{
		return healthyScore;
	}

	public synchronized void setHealthyScore(int healthyScore)
	{
		this.healthyScore = healthyScore;
	}

	@Override
	public String toString()
	{
		return "MachineInfo [usedPhysicalMemoryUsage=" + usedPhysicalMemoryUsage
				+ ", processCpuUsage=" + processCpuUsage + ", systemCpuUsage=" + systemCpuUsage
				+ ", usedMaxMemoryUsage=" + usedMaxMemoryUsage + ", usedTotalMemoryUsage="
				+ usedTotalMemoryUsage + ", healthyScore=" + healthyScore + "]";
	}
    
}
