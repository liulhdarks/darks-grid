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
package darks.grid.executor;

import java.io.Serializable;

import darks.grid.RpcReduceHandler;
import darks.grid.balance.GridBalance;
import darks.grid.balance.RollPolingBalance;

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
    
    private int timeout = 0;
    
    private transient RpcReduceHandler reducerHandler;
    
    private transient GridBalance balance = new RollPolingBalance();
    
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

    public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
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
		return "ExecuteConfig [callType=" + callType + ", responseType=" + responseType
				+ ", timeout=" + timeout + "]";
	}

}
