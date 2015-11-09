package darks.grid.spring;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import darks.grid.GridException;
import darks.grid.executor.RpcExecutor;
import darks.grid.utils.ReflectUtils;

public class GridRpcSpringProviderBean implements ApplicationContextAware, InitializingBean
{

	private ApplicationContext ctx;
	
	private Class<?> serviceInterface;
	
	private Object target;
	
	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (target == null || serviceInterface == null)
		{
			throw new GridException("Invalid target or serviceInterface config.");
		}
		Method[] methods = serviceInterface.getMethods();
		for (Method method : methods)
		{
//	        String methodName = ReflectUtils.getMethodUniqueName(method);
	        RpcExecutor.registerMethod(method, target.getClass(), target);
//			RpcExecutor.registerMethod(methodName, method.getName(), target.getClass(), target);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.ctx = applicationContext;
	}

	public Class<?> getServiceInterface()
	{
		return serviceInterface;
	}

	public void setServiceInterface(Class<?> serviceInterface)
	{
		this.serviceInterface = serviceInterface;
	}

	public Object getTarget()
	{
		return target;
	}

	public void setTarget(Object target)
	{
		this.target = target;
	}

}