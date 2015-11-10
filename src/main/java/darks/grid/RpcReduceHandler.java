package darks.grid;

import darks.grid.executor.task.rpc.RpcResult;

public interface RpcReduceHandler
{

	public Object reduce(RpcResult result);
	
}
