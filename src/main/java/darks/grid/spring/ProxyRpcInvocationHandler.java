package darks.grid.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.RpcReduceHandler;
import darks.grid.beans.MethodResult;
import darks.grid.config.MethodConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.utils.ReflectUtils;

public class ProxyRpcInvocationHandler implements InvocationHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(ProxyRpcInvocationHandler.class);
	

	private RpcReduceHandler reducer;
	
	public ProxyRpcInvocationHandler()
	{
		
	}
	
	public ProxyRpcInvocationHandler(RpcReduceHandler reducer)
	{
		this.reducer = reducer;
	} 
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        String methodName = ReflectUtils.getMethodUniqueName(method);
        MethodResult result = RpcExecutor.callMethod(methodName, args, new MethodConfig());
        if (!result.isSuccess())
        {
        	log.error("Fail to invoke method " + methodName + ". Cause " + result.getErrorMessage());
        }
        if (reducer != null)
        {
        	return reducer.reduce(result);
        }
        else
        {
        	if (method.getReturnType().isAssignableFrom(List.class))
        	{
                return result.getResult();
        	}
        	else
        	{
        		List<Object> list = result.getResult();
        		return (list == null || list.isEmpty()) ? null : list.get(0);
        	}
        }
    }
    
}
