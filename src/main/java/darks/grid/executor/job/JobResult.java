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
package darks.grid.executor.job;

import darks.grid.beans.GridNode;

public class JobResult
{

    private Object object;
    
    private String errorMessage;
    
    private int errorCode;
    
    private boolean success;
    
    private GridNode node;
    
    public JobResult(GridNode node)
    {
        this.node = node;
    }
    

    public Object getObject()
    {
        return object;
    }



    public void setObject(Object object)
    {
        this.object = object;
    }



    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }


    public boolean isSuccess()
    {
        return success;
    }


    public void setSuccess(boolean success)
    {
        this.success = success;
    }


    public GridNode getNode()
    {
        return node;
    }


    public void setNode(GridNode node)
    {
        this.node = node;
    }


    @Override
    public String toString()
    {
        return "JobResult [object=" + object + ", errorMessage=" + errorMessage + ", errorCode=" + errorCode
            + ", success=" + success + ", node=" + node + "]";
    }
    
    
}
