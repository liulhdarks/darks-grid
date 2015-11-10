package darks.grid.executor.task.rpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.task.mapred.MapReduceTaskAdapter;

public class GridRpcTask extends MapReduceTaskAdapter<RpcRequest, RpcResult>
{
     
    public GridRpcTask()
    {
        super(TaskType.RPC);
    }

    @Override
    public Collection<? extends GridJob> split(int jobsCount, RpcRequest request)
    {
        List<GridRpcJob> jobs = new ArrayList<GridRpcJob>(jobsCount);
        for (int i = 0; i < jobsCount; i++)
        {
            GridRpcJob job = new GridRpcJob(request);
            jobs.add(job);
        }
        return jobs;
    }

    @Override
    public RpcResult reduce(List<JobResult> results)
    {
        StringBuilder errorBuf = new StringBuilder();
        RpcResult result = new RpcResult();
        List<Object> objs = new ArrayList<>(results.size());
        for (JobResult jobRet : results)
        {
            if (!jobRet.isSuccess())
            {
                errorBuf.append("Error ").append(jobRet.getErrorCode())
                    .append(" on ").append(jobRet.getNode().getId()).append(" ")
                    .append(jobRet.getErrorMessage()).append('\n');
            }
            else
                objs.add(jobRet.getObject());
        }
        result.setResult(objs);
        if (errorBuf.length() > 0)
        {
            result.setErrorMessage(errorBuf.toString());
            result.setSuccess(false);
        }
        return result;
    }
    
}
