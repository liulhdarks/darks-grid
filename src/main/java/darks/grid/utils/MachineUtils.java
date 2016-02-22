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


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

public final class MachineUtils
{
    
	private static final Logger log = LoggerFactory.getLogger(MachineUtils.class);

    private static final int CPUTIME = 500;

    private static final int PERCENT = 100;

    private static final int FAULTLENGTH = 10;
	
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
    
    public static float getCpuUsageForLinux()
    {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        Process process = null;
        try
        {
            process = Runtime.getRuntime().exec("top -b -n 1");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            float sumUsage = 0;
            boolean flag = false;
            int cpuIndex = -1;
            int cmdIndex = -1;
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                if ("".equals(line))
                    continue;
                if (!flag && line.indexOf("PID") >= 0 && line.indexOf("CPU") >= 0)
                {
                    flag = true;
                    String[] captions = line.split("[\\s\\t]+");
                    for (int i = 0; i < captions.length; i++)
                    {
                        String caption = captions[i];
                        if ("%CPU".equals(caption))
                            cpuIndex = i;
                        if ("COMMAND".equals(caption))
                            cmdIndex = i;
                    }
                }
                else if (flag && cpuIndex >=0 && cmdIndex >= 0)
                {
                    String[] datas = line.split("[\\s\\t]+");
                    String cpuInfo = datas[cpuIndex];
                    String cmdInfo = datas[cmdIndex].toLowerCase().trim();
                    if (cmdInfo.startsWith("top"))
                        continue;
                    float radio = Float.parseFloat(cpuInfo);
                    sumUsage += radio;
                }
            }
            if (cpuIndex < 0)
                return -1;
            return sumUsage / 100.f;
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeStream(is);
            IOUtils.closeStream(isr);
            IOUtils.closeStream(reader);
            if (process != null)
                process.destroy();
        }
        return -1;
    }

    public static float getCpuUsageForWindows()
    {
        try
        {
            String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readWindowsCpuInfo(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readWindowsCpuInfo(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null)
            {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return (float) (PERCENT * (busytime)) / (float) (busytime + idletime);
            }
            else
            {
                return 0.0f;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return 0.0f;
        }
    }

    private static long[] readWindowsCpuInfo(Process proc)
    {
        long[] retn = new long[2];
        try
        {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH)
            {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null)
            {
                if (line.length() < wocidx)
                {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = line.substring(capidx, cmdidx - 1).trim();
                String cmd = line.substring(cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0)
                {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process") || caption.equals("System"))
                {
                    idletime += Long.valueOf(line.substring(kmtidx, rocidx - 1).trim()).longValue();
                    idletime += Long.valueOf(line.substring(umtidx, wocidx - 1).trim()).longValue();
                    continue;
                }

                kneltime += Long.valueOf(line.substring(kmtidx, rocidx - 1).trim()).longValue();
                usertime += Long.valueOf(line.substring(umtidx, wocidx - 1).trim()).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        finally
        {
            IOUtils.closeStream(proc.getErrorStream());
            IOUtils.closeStream(proc.getInputStream());
            proc.destroy();
        }
        return null;
    }
}
