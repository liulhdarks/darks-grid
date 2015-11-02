package darks.grid.executor.task;

import java.util.Map.Entry;

import darks.grid.GridRuntime;
import darks.grid.beans.GridNode;
import darks.grid.beans.MethodResult;
import darks.grid.beans.meta.MethodRequest;
import darks.grid.config.MethodConfig.CallType;

public class RpcTask extends GridTask<MethodResult>
{

    private MethodRequest request;
    
    private TaskResultListener<MethodResult> listener;
    
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
                //TODO
            }
        }
        
        //TODO
        MethodResult ret = null;
        if (listener != null)
            listener.handle(ret);
        return ret;
    }

}
