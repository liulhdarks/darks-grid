package darks.grid.executor.task;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.balance.GridBalance;
import darks.grid.balance.RollPolingBalance;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.JobStatus;
import darks.grid.beans.JobStatus.JobStatusType;
import darks.grid.beans.MethodResult;
import darks.grid.config.MethodConfig.CallType;
import darks.grid.executor.task.rpc.GridRpcJobFuture;
import darks.grid.executor.task.rpc.MethodJob;
import darks.grid.executor.task.rpc.MethodJobReply;
import darks.grid.executor.task.rpc.MethodRequest;

public class RpcTask extends GridTask<MethodResult>
{
	
	private static final Logger log = LoggerFactory.getLogger(RpcTask.class);

    private MethodRequest request;
    
    private TaskResultListener<MethodResult> listener;
    
    private GridRpcJobFuture future = new GridRpcJobFuture();
    
    public RpcTask(MethodRequest request)
    {
        super(TaskType.RPC);
        this.request = request;
    }
    
    public RpcTask(MethodRequest request, TaskResultListener<MethodResult> listener)
    {
        super(TaskType.RPC);
        this.request = request;
        this.listener = listener;
    }
    
    @Override
    protected MethodResult execute()
    {
        CallType callType = request.getMethodConfig().getCallType();
        if (callType == CallType.ALL)
        {
            for (Entry<String, GridNode> entry : GridRuntime.nodes().getNodesMap().entrySet())
            {
                GridNode node = entry.getValue();
                if (!node.isAlive())
                    continue;
                executeTaskOnNode(node);
            }
        }
        else if (callType == CallType.SINGLE)
        {
        	GridBalance balance = new RollPolingBalance();
        	GridNode node = balance.getBalanceNode();
        	if (node != null && node.isAlive())
                executeTaskOnNode(node);
        }
        future.await();
        MethodResult ret = future.get();
        if (listener != null)
            return listener.handle(ret);
        else
        	return ret;
    }

    private void executeTaskOnNode(GridNode node)
    {
    	MethodJob job = new MethodJob(request);
    	job.setTaskId(getId());
    	JobStatus status = new JobStatus(job, node);
    	if (node.isLocal())
    	{
    		log.info("Ignore local node for task " + getId());
    	}
    	else
    	{
        	future.addJobStatus(status);
            GridMessage message = new GridMessage(job, GridMessage.MSG_RPC_REQUEST);
            boolean ret = node.sendSyncMessage(message);
            if (ret)
            	status.setStatusType(JobStatusType.DOING);
            else
            	future.removeJobStatus(job.getJobId());
    	}
    }
    
    public void replyJob(MethodJobReply reply)
    {
    	future.replyStatus(reply);
    }
    
}
