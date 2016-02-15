package darks.grid.test.spring;

import darks.grid.annotations.RpcMethod;
import darks.grid.executor.ExecuteConfig.CallType;
import darks.grid.executor.ExecuteConfig.ResponseType;


public interface RpcComputer
{

    @RpcMethod(callType=CallType.ALL, responseType=ResponseType.SINGLE, reducer=RpcComputerReduceHandler.class)
    public int add(int a, int b);

    public int rand(int base);
    
    @RpcMethod(callType=CallType.OTHERS, responseType=ResponseType.NONE)
    public void print(int v);
}
