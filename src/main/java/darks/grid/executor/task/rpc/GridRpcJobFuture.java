package darks.grid.executor.task.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import darks.grid.beans.JobStatus;
import darks.grid.beans.JobStatus.JobStatusType;
import darks.grid.beans.MethodResult;
import darks.grid.executor.task.GridJobFuture;

public class GridRpcJobFuture extends GridJobFuture<MethodResult>
{
	
	

	@Override
	public MethodResult get()
	{
		StringBuilder errorBuf = new StringBuilder();
		MethodResult result = new MethodResult();
		List<Object> objs = new ArrayList<>(statusMap.size());
		for (Entry<String, JobStatus> entry : statusMap.entrySet())
		{
			JobStatus status = entry.getValue();
			if (status.getStatusType() == JobStatusType.FAIL)
			{
				errorBuf.append("Error ").append(status.getErrorCode())
					.append(" on ").append(status.getNode().getId()).append(" ")
					.append(status.getErrorMessage()).append('\n');
			}
			else
				objs.add(status.getResult());
		}
		if (errorBuf.length() > 0)
		{
			result.setErrorMessage(errorBuf.toString());
			result.setSuccess(false);
		}
		return result;
	}

	@Override
	public List<MethodResult> getList()
	{
		return null;
	}

	
}
