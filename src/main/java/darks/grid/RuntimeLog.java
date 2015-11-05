package darks.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridComponent;
import darks.grid.utils.MachineUtils;

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
			buf.append("Direct Memory:").append(MachineUtils.getReservedDirectMemory())
				.append('/').append(MachineUtils.getMaxDirectMemory()).append('\n');
			buf.append("-------------------------------------------------------------\n");
		}
		if (taskInfo)
		{
			buf.append(GridRuntime.tasks().toSimgleString()).append('\n');
			buf.append("-------------------------------------------------------------\n");
		}
		if (execJobInfo)
		{
			buf.append(GridRuntime.jobs().toExecuteJobsString()).append('\n');
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
