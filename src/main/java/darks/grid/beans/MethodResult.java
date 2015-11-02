package darks.grid.beans;

import java.io.Serializable;
import java.util.List;

public class MethodResult implements Serializable, Cloneable
{
    private static final long serialVersionUID = 747642688611619579L;

    private List<Object> result;
    
    private String errorMessage;
    
    private boolean success = true;
    
    public MethodResult()
    {
        
    }
    
    public static MethodResult fail(String error)
    {
        MethodResult result = new MethodResult();
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
    
}
