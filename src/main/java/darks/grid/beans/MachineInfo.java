package darks.grid.beans;

import java.io.Serializable;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import darks.grid.utils.MachineUtils;

public class MachineInfo implements Serializable
{

    private static final long serialVersionUID = 1080482398147164650L;
    
    private float usedPhysicalMemoryUsage;
    
    private float processCpuUsage;
    
    private float systemCpuUsage;
    
    private float usedMaxMemoryUsage;
    
    private float usedTotalMemoryUsage;

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
    }

    public float getUsedPhysicalMemoryUsage()
    {
        return usedPhysicalMemoryUsage;
    }

    public void setUsedPhysicalMemoryUsage(float usedPhysicalMemoryUsage)
    {
        this.usedPhysicalMemoryUsage = usedPhysicalMemoryUsage;
    }

    public float getProcessCpuUsage()
    {
        return processCpuUsage;
    }

    public void setProcessCpuUsage(float processCpuUsage)
    {
        this.processCpuUsage = processCpuUsage;
    }

    public float getSystemCpuUsage()
    {
        return systemCpuUsage;
    }

    public void setSystemCpuUsage(float systemCpuUsage)
    {
        this.systemCpuUsage = systemCpuUsage;
    }

    public float getUsedMaxMemoryUsage()
    {
        return usedMaxMemoryUsage;
    }

    public void setUsedMaxMemoryUsage(float usedMaxMemoryUsage)
    {
        this.usedMaxMemoryUsage = usedMaxMemoryUsage;
    }

    public float getUsedTotalMemoryUsage()
    {
        return usedTotalMemoryUsage;
    }

    public void setUsedTotalMemoryUsage(float usedTotalMemoryUsage)
    {
        this.usedTotalMemoryUsage = usedTotalMemoryUsage;
    }

    @Override
    public String toString()
    {
        return "MachineInfo [usedPhysicalMemoryUsage=" + usedPhysicalMemoryUsage + ", processCpuUsage="
            + processCpuUsage + ", systemCpuUsage=" + systemCpuUsage + ", usedMaxMemoryUsage=" + usedMaxMemoryUsage
            + ", usedTotalMemoryUsage=" + usedTotalMemoryUsage + "]";
    }
    
}
