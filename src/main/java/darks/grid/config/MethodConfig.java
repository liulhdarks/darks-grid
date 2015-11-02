package darks.grid.config;

import java.io.Serializable;

public class MethodConfig implements Serializable
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
    
    CallType callType = CallType.ALL;
    
    ResponseType responseType = ResponseType.ALL;
    
    int timeoutSeconds = 0;
    
    public MethodConfig()
    {
        
    }
    
    public void fixType()
    {
        if (responseType != ResponseType.NONE)
        {
            if (callType == CallType.ALL)
                responseType = ResponseType.ALL;
            else
                responseType = ResponseType.SINGLE;
        }
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

    @Override
    public String toString()
    {
        return "MethodConfig [callType=" + callType + ", responseType=" + responseType + ", timeoutSeconds="
            + timeoutSeconds + "]";
    }
    
}
