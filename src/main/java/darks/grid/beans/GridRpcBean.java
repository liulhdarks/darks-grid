package darks.grid.beans;

import java.io.Serializable;

public class GridRpcBean implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8392036669739608404L;

	String methodName;
	
	Class<?> targetClass;
	
	Object targetObject;
	
	public GridRpcBean()
	{
		
	}

	public GridRpcBean(String methodName, Class<?> targetClass, Object targetObject)
	{
		super();
		this.methodName = methodName;
		this.targetClass = targetClass;
		this.targetObject = targetObject;
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

	@Override
	public String toString()
	{
		return "GridRpcBean [methodName=" + methodName + ", targetClass=" + targetClass
				+ ", targetObject=" + targetObject + "]";
	}
	
		
}
