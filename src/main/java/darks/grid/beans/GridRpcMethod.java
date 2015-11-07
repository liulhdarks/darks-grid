package darks.grid.beans;

import java.io.Serializable;
import java.lang.reflect.Method;

public class GridRpcMethod implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8392036669739608404L;

	String methodName;
	
	Class<?> targetClass;
	
	Object targetObject;
	
	Method method;
	
	public GridRpcMethod()
	{
		
	}

	public GridRpcMethod(String methodName, Class<?> targetClass, Object targetObject, Method method)
	{
		super();
		this.methodName = methodName;
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.method = method;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public Class<?> getTargetClass()
	{
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass)
	{
		this.targetClass = targetClass;
	}

	public Object getTargetObject()
	{
		return targetObject;
	}

	public void setTargetObject(Object targetObject)
	{
		this.targetObject = targetObject;
	}
	

	public Method getMethod()
    {
        return method;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    @Override
	public String toString()
	{
		return "GridRpcBean [methodName=" + methodName + ", targetClass=" + targetClass
				+ ", targetObject=" + targetObject + "]";
	}

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
        result = prime * result + ((targetObject == null) ? 0 : targetObject.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GridRpcMethod other = (GridRpcMethod)obj;
        if (method == null)
        {
            if (other.method != null)
                return false;
        }
        else if (!method.equals(other.method))
            return false;
        if (methodName == null)
        {
            if (other.methodName != null)
                return false;
        }
        else if (!methodName.equals(other.methodName))
            return false;
        if (targetClass == null)
        {
            if (other.targetClass != null)
                return false;
        }
        else if (!targetClass.equals(other.targetClass))
            return false;
        if (targetObject == null)
        {
            if (other.targetObject != null)
                return false;
        }
        else if (!targetObject.equals(other.targetObject))
            return false;
        return true;
    }
	
		
}
