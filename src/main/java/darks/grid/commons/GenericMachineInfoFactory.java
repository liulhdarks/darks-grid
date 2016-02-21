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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

import darks.grid.beans.MachineInfo;
import darks.grid.beans.NodeHealth;
import darks.grid.utils.BytesUtils;
import darks.grid.utils.IOUtils;
import darks.grid.utils.MachineUtils;
import darks.grid.utils.ReflectUtils;

public class GenericMachineInfoFactory extends MachineInfoFactory
{
	private static final Logger log = LoggerFactory.getLogger(GenericMachineInfoFactory.class);

	private static final int CPUTIME = 5000;

	private static final int PERCENT = 100;

	private static final int FAULTLENGTH = 10;

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
				info.setProcessCpuUsage((Float) ret);
			}
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
				info.setSystemCpuUsage((Float) ret);
			}
		}
	}

	private static float getCpuUsageForLinux()
	{
		String version = System.getProperty("os.version");
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		try
		{
			Process process = Runtime.getRuntime().exec("top -b -n 1");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);

			if ("2.4".equals(version))
			{
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();

				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				String user = tokenStat.nextToken();
				tokenStat.nextToken();
				String system = tokenStat.nextToken();
				tokenStat.nextToken();
				String nice = tokenStat.nextToken();

				System.out.println(user + " , " + system + " , " + nice);

				user = user.substring(0, user.indexOf("%"));
				system = system.substring(0, system.indexOf("%"));
				nice = nice.substring(0, nice.indexOf("%"));

				float userUsage = new Float(user).floatValue();
				float systemUsage = new Float(system).floatValue();
				float niceUsage = new Float(nice).floatValue();

				return (userUsage + systemUsage + niceUsage) / 100;
			}
			else
			{
				brStat.readLine();
				brStat.readLine();

				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				String cpuUsage = tokenStat.nextToken();
				Float usage = new Float(cpuUsage.substring(0, cpuUsage.indexOf("%")));
				return (1 - usage.floatValue() / 100);
			}

		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		finally
		{
			IOUtils.closeStream(is);
			IOUtils.closeStream(isr);
			IOUtils.closeStream(brStat);
		}
		return 0;
	}

	private double getCpuRatioForWindows()
	{
		try
		{
			String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,"
					+ "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			// 取进程信息
			long[] c0 = readCpuInfo(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpuInfo(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null)
			{
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return Double.valueOf(PERCENT * (busytime) / (busytime + idletime)).doubleValue();
			}
			else
			{
				return 0.0;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return 0.0;
		}
	}

	/** */
	/**
	 * 读取CPU信息.
	 * 
	 * @param proc
	 * @return
	 * @author amg * Creation date: 2008-4-25 - 下午06:10:14
	 */
	private long[] readCpuInfo(Process proc)
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
				String caption = BytesUtils.subString(line, capidx, cmdidx - 1).trim();
				String cmd = BytesUtils.subString(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0)
				{
					continue;
				}
				// log.info("line="+line);
				if (caption.equals("System Idle Process") || caption.equals("System"))
				{
					idletime += Long.valueOf(BytesUtils.subString(line, kmtidx, rocidx - 1).trim()).longValue();
					idletime += Long.valueOf(BytesUtils.subString(line, umtidx, wocidx - 1).trim()).longValue();
					continue;
				}

				kneltime += Long.valueOf(BytesUtils.subString(line, kmtidx, rocidx - 1).trim()).longValue();
				usertime += Long.valueOf(BytesUtils.subString(line, umtidx, wocidx - 1).trim()).longValue();
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
