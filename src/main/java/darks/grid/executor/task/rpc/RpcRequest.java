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
import java.util.Arrays;

public class RpcRequest implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8107745899221499625L;

	private String uniqueName;
	
	private Object[] params;

    private Class<?>[] types;
	
	public RpcRequest()
	{
		
	}



    public RpcRequest(String uniqueName, Object[] params, Class<?>[] types)
    {
        super();
        this.uniqueName = uniqueName;
        this.params = params;
        this.types = types;
    }

    
    
    public String getUniqueName()
	{
		return uniqueName;
	}



	public void setUniqueName(String uniqueName)
	{
		this.uniqueName = uniqueName;
	}



	public Object[] getParams()
    {
        return params;
    }

    public void setParams(Object[] params)
    {
        this.params = params;
    }

    public Class<?>[] getTypes()
    {
        return types;
    }

    public void setTypes(Class<?>[] types)
    {
        this.types = types;
    }


    @Override
    public String toString()
    {
        return "RpcRequest [uniqueName=" + uniqueName + ", params=" + Arrays.toString(params) + ", types="
            + Arrays.toString(types) + "]";
    }

}
