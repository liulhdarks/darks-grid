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
package darks.grid.utils;


import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

public final class MachineUtils
{
    
	private static final Logger log = LoggerFactory.getLogger(MachineUtils.class);
	
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
			log.error(e.getMessage(), e);
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
			log.error(e.getMessage(), e);
			return null;
		}
    }
}
