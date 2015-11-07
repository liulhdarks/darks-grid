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
