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

import java.util.Arrays;

import darks.grid.executor.job.GridJobAdapter;

public class GridRpcJob extends GridJobAdapter
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1781136302155242623L;

	private String uniqueName;
	
	private Object[] params;

    private Class<?>[] types;
	
	public GridRpcJob()
	{
		setFailRedo(false);
	}
	
	public GridRpcJob(RpcRequest request)
	{
		this.uniqueName = request.getUniqueName();
		this.params = request.getParams();
        this.types = request.getTypes();
		setFailRedo(false);
	}
	
	
	@Override
    public Object execute()
    {
	    return RpcMethodInvoker.invoke(this);
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
        return "GridRpcJob [uniqueName=" + uniqueName + ", params=" + Arrays.toString(params) + ", types="
            + Arrays.toString(types) + "]";
    }
    
}
