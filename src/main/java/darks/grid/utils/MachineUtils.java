package darks.grid.utils;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

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
    
    public static String getProcessId()
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }
    
    @SuppressWarnings("restriction")
    public static float getPhysicalMemoryUsage()
    {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize();
        long usedMemorySize = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());
        return (float)((double)usedMemorySize / (double)totalMemorySize);
    }
    
    public static Long getMaxDirectMemory()
    {
    	try
		{
        	Class<?> c = Class.forName("java.nio.Bits");
        	Field maxMemory = c.getDeclaredField("maxMemory");
        	maxMemory.setAccessible(true);
        	return (Long) maxMemory.get(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }
    
    public static Long getReservedDirectMemory()
    {
    	try
		{
        	Class<?> c = Class.forName("java.nio.Bits");
        	Field reservedMemory = c.getDeclaredField("reservedMemory");
        	reservedMemory.setAccessible(true);
        	return (Long) reservedMemory.get(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }
}
