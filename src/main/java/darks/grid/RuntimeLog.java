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
	
	private boolean nodesInfo = true;
	
	private boolean taskInfo = true;
	
	private boolean execJobInfo = true;
	
	private boolean jvmInfo = true;

	@Override
	protected void execute() throws Exception
	{
		StringBuilder buf = new StringBuilder();
		buf.append("\n=============================================================\n");
		if (nodesInfo)
		{
			buf.append(GridRuntime.nodes().getNodesInfo());
			buf.append("-------------------------------------------------------------\n");
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
			buf.append(' ').append("Avg-Delay:").append(GridStatistic.getAvgDelay())
				.append(' ').append("Max-Delay:").append(GridStatistic.getMaxDelay())
				.append(' ').append("Avg-LDelay:").append(GridStatistic.getAvgLocalDelay())
				.append(' ').append("Max-LDelay:").append(GridStatistic.getMaxLocalDelay());
			buf.append('\n');
			buf.append("-------------------------------------------------------------\n");
		}
		if (taskInfo)
		{
			long taskCount = GridStatistic.getTaskCount();
			String info = GridRuntime.tasks().toSimgleString().trim();
			buf.append("Tasks(").append(taskCount).append("):\n");
			if (!"".equals(info))
				buf.append(info).append('\n');
			buf.append("-------------------------------------------------------------\n");
		}
		if (execJobInfo)
		{
			long jobCount = GridStatistic.getJobCount();
			String info = GridRuntime.jobs().toExecuteJobsString().trim();
			buf.append("Jobs(").append(jobCount).append("):\n");
			if (!"".equals(info))
				buf.append(info).append('\n');
		}
		buf.append("=============================================================\n");
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

}
