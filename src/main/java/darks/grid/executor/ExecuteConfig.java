package darks.grid.executor;

import java.io.Serializable;

import darks.grid.RpcReduceHandler;
import darks.grid.balance.GridBalance;

public class ExecuteConfig implements Serializable
{
    
    private static final long serialVersionUID = -7677264908208421584L;

    public enum CallType
    {
        ALL, SINGLE
    }
    
    public enum ResponseType
    {
        NONE, ALL, SINGLE
    }
    
    private CallType callType = CallType.ALL;
    
    private ResponseType responseType = ResponseType.ALL;
    
    private int timeoutSeconds = 0;
    
    private transient RpcReduceHandler reducerHandler;
    
    private transient GridBalance balance;
    
    public ExecuteConfig()
    {
        
    }
    
    public void fixType()
    {
//        if (responseType != ResponseType.NONE)
//        {
//            if (callType == CallType.ALL)
//                responseType = ResponseType.ALL;
//            else
//                responseType = ResponseType.SINGLE;
//        }
    }

    public CallType getCallType()
    {
        return callType;
    }

    public void setCallType(CallType callType)
    {
        this.callType = callType;
    }

    public ResponseType getResponseType()
    {
        return responseType;
    }

    public void setResponseType(ResponseType responseType)
    {
        this.responseType = responseType;
    }

    public int getTimeoutSeconds()
    {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds)
    {
        this.timeoutSeconds = timeoutSeconds;
    }
    

    public RpcReduceHandler getReducerHandler()
    {
        return reducerHandler;
    }

    public void setReducerHandler(RpcReduceHandler reducerHandler)
    {
        this.reducerHandler = reducerHandler;
    }

    public GridBalance getBalance()
    {
        return balance;
    }

    public void setBalance(GridBalance balance)
    {
        this.balance = balance;
    }

    @Override
    public String toString()
    {
        return "ExecuteConfig [callType=" + callType + ", responseType=" + responseType + ", timeoutSeconds="
            + timeoutSeconds + "]";
    }

}
