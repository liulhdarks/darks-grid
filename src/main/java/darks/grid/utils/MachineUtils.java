package darks.grid.utils;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public final class MachineUtils
{
    
    private MachineUtils()
    {
    
    }
    
    public static String getProcessRuntimeName()
    {
        return ManagementFactory.getRuntimeMXBean().getName();
    }
    
    @SuppressWarnings("restriction")
    public static float getPhysicalMemoryUsage()
    {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize();
        long usedMemorySize = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());
        return (float)((double)usedMemorySize / (double)totalMemorySize);
    }
}
