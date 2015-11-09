package darks.grid.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.MethodResult;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.utils.ReflectUtils;

public class ProxyRpcInvocationHandler implements InvocationHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(ProxyRpcInvocationHandler.class);
	

	private ExecuteConfig config;
	
	private Set<String> targetMethods;
	
	private Map<String, Boolean> cacheMatch = new ConcurrentHashMap<String, Boolean>();
	
	public ProxyRpcInvocationHandler(ExecuteConfig config, Set<String> targetMethods)
	{
		this.config = config;
		this.targetMethods = targetMethods;
	} 
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
    	if (!matchTargetMethod(method.getName()))
    		return method.invoke(proxy, args);
        String methodName = ReflectUtils.getMethodUniqueName(method);
        MethodResult result = RpcExecutor.callMethod(methodName, args, method.getParameterTypes(), config);
        if (!result.isSuccess())
        {
        	log.error("Fail to invoke method " + methodName + ". Cause " + result.getErrorMessage());
        }
        if (config.getReducerHandler() != null)
        {
        	return config.getReducerHandler().reduce(result);
        }
        else
        {
        	if (method.getReturnType().equals(Object.class))
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
    
    private boolean matchTargetMethod(String methodName)
    {
    	Boolean oldRet = cacheMatch.get(methodName);
    	if (oldRet != null)
    		return oldRet;
    	boolean ret = false;
    	if (targetMethods == null 
    			|| targetMethods.isEmpty() 
    			|| targetMethods.contains(methodName))
    	{
    		ret = true;
    	}
    	else
    	{
    		//TODO regex
    	}
    	cacheMatch.put(methodName, ret);
    	return ret;
    }
    
}
