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
package darks.grid.executor.task.rpc;

import java.io.Serializable;
import java.util.List;

public class RpcResult implements Serializable, Cloneable
{
    private static final long serialVersionUID = 747642688611619579L;

    private List<Object> result;
    
    private String errorMessage;
    
    private boolean success = true;
    
    public RpcResult()
    {
        
    }
    
    public RpcResult(List<Object> result)
    {
        this.result = result;
    }
    
    public static RpcResult fail(String error)
    {
        RpcResult result = new RpcResult();
        result.errorMessage = error;
        result.success = false;
        return result;
    }


    public List<Object> getResult()
    {
        return result;
    }


    public void setResult(List<Object> result)
    {
        this.result = result;
    }


    public String getErrorMessage()
    {
        return errorMessage;
    }


    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    public boolean isSuccess()
    {
        return success;
    }


    public void setSuccess(boolean success)
    {
        this.success = success;
    }

	@Override
	public String toString()
	{
		return "MethodResult [result=" + result + ", errorMessage=" + errorMessage + ", success="
				+ success + "]";
	}
    
}
