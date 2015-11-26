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
package darks.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridComponent;
import darks.grid.utils.GridStatistic;
import darks.grid.utils.MachineUtils;
import darks.grid.utils.StringUtils;

public class RuntimeLog extends GridComponent
{

	private static final long serialVersionUID = 8692793078604660506L;
	
	private static final Logger log = LoggerFactory.getLogger(RuntimeLog.class);
	
	private static final String HEAD_SEP = "\n==========================Darks Grid V1.0.0====================================\n";
	
	private static final String INFO_SEP = "-------------------------------------------------------------------------------\n";
	
	private static final String END_SEP = "===============================================================================\n";
	
	private boolean nodesInfo = true;
	
	private boolean taskInfo = true;
	
	private boolean execJobInfo = true;
	
	private boolean jvmInfo = true;
	
	private boolean statInfo = true;
	
	private boolean eventInfo = true;

	@Override
	protected void execute() throws Exception
	{
		StringBuilder buf = new StringBuilder();
		buf.append(HEAD_SEP);
		if (nodesInfo)
		{
			buf.append(GridRuntime.nodes().getNodesInfo());
			buf.append(INFO_SEP);
		}
		if (jvmInfo)
		{
			Runtime jvmRuntime = Runtime.getRuntime();
			buf.append("Direct-mem:").append(StringUtils.memorySize(MachineUtils.getReservedDirectMemory()))
				.append('/').append(StringUtils.memorySize(MachineUtils.getMaxDirectMemory())).append('\t')
				.append("Heap-men:").append(StringUtils.memorySize(jvmRuntime.totalMemory() - jvmRuntime.freeMemory()))
				.append('/').append(StringUtils.memorySize(jvmRuntime.totalMemory()));
			if (jvmRuntime.totalMemory() != jvmRuntime.maxMemory())
				buf.append('/').append(StringUtils.memorySize(jvmRuntime.maxMemory()));
			buf.append('\n');
			buf.append(INFO_SEP);
		}
		if (statInfo)
		{
			buf.append("Avg-Delay:").append(GridStatistic.getAvgDelay())
				.append(' ').append("Max-Delay:").append(GridStatistic.getMaxDelay())
				.append(' ').append("Avg-LDelay:").append(GridStatistic.getAvgLocalDelay())
				.append(' ').append("Max-LDelay:").append(GridStatistic.getMaxLocalDelay());
			buf.append('\n');
			buf.append("Avg-JobW-Delay:").append(GridStatistic.getAvgJobWaitDelay())
				.append(' ').append("Max-JobW-Delay:").append(GridStatistic.getMaxJobWaitDelay())
				.append(' ').append("Cur-JobW-Count:").append(GridStatistic.getCurJobWaitCount())
				.append(' ').append("Run-Job-Count:").append(GridRuntime.jobs().getRunningJobsCount());
			buf.append('\n');
			buf.append(INFO_SEP);
		}
		if (eventInfo)
		{
			buf.append(GridRuntime.events().getEventsInfo()).append('\n');
			buf.append(INFO_SEP);
		}
		if (taskInfo)
		{
			long taskCount = GridStatistic.getTaskCount();
			String info = GridRuntime.tasks().toSimgleString().trim();
			buf.append("Tasks(").append(taskCount).append("):\n");
			if (!"".equals(info))
				buf.append(info).append('\n');
			buf.append(INFO_SEP);
		}
		if (execJobInfo)
		{
			long jobCount = GridStatistic.getJobCount();
			String info = GridRuntime.jobs().toExecuteJobsString().trim();
			buf.append("Jobs(").append(jobCount).append("):\n");
			if (!"".equals(info))
				buf.append(info).append('\n');
		}
		buf.append(END_SEP);
		log.info(buf.toString());
	}

	public boolean isTaskInfo()
	{
		return taskInfo;
	}

	public void setTaskInfo(boolean taskInfo)
	{
		this.taskInfo = taskInfo;
	}

	public boolean isExecJobInfo()
	{
		return execJobInfo;
	}

	public void setExecJobInfo(boolean execJobInfo)
	{
		this.execJobInfo = execJobInfo;
	}

	public boolean isJvmInfo()
	{
		return jvmInfo;
	}

	public void setJvmInfo(boolean jvmInfo)
	{
		this.jvmInfo = jvmInfo;
	}

	public boolean isNodesInfo()
	{
		return nodesInfo;
	}

	public void setNodesInfo(boolean nodesInfo)
	{
		this.nodesInfo = nodesInfo;
	}

	public boolean isStatInfo()
	{
		return statInfo;
	}

	public void setStatInfo(boolean statInfo)
	{
		this.statInfo = statInfo;
	}

	public boolean isEventInfo()
	{
		return eventInfo;
	}

	public void setEventInfo(boolean eventInfo)
	{
		this.eventInfo = eventInfo;
	}

}
