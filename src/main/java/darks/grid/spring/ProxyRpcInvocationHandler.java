package darks.grid.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.TaskResultListener;
import darks.grid.executor.task.rpc.RpcResult;
import darks.grid.utils.ReflectUtils;

public class ProxyRpcInvocationHandler implements InvocationHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(ProxyRpcInvocationHandler.class);
	
	ExecuteConfig config = null;
	
	Set<String> targetMethods = null;
	
	TaskResultListener taskResultListener = null;
	
	boolean asyncInvoke = false;
	
	public ProxyRpcInvocationHandler(RpcProxyBean proxyBean)
	{
		config = proxyBean.getConfig();
		targetMethods = proxyBean.getTargetMethods();
		taskResultListener = proxyBean.getTaskResultListener();
		asyncInvoke = proxyBean.isAsyncInvoke();
	} 
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
    	if (!matchTargetMethod(method.getName()))
    		return method.invoke(proxy, args);
        String methodName = ReflectUtils.getMethodUniqueName(method);
        if (asyncInvoke)
        {
        	RpcExecutor.asyncCallMethod(methodName, args, method.getParameterTypes(), config, taskResultListener);
        	return null;
        }
        RpcResult result = RpcExecutor.callMethod(methodName, args, method.getParameterTypes(), config);
        if (!result.isSuccess())
        {
        	log.error("Fail to invoke method " + methodName + ". Cause " + result.getErrorMessage());
        }
        Object finalResult = null;
        if (config.getReducerHandler() != null)
        {
        	finalResult = config.getReducerHandler().reduce(result);
        }
        else
        {
        	if (method.getReturnType().equals(Object.class))
        	{
        		finalResult = result.getResult();
        	}
        	else
        	{
        		List<Object> list = result.getResult();
        		finalResult = (list == null || list.isEmpty()) ? null : list.get(0);
        	}
        }
        if (taskResultListener != null)
        	taskResultListener.handle(finalResult);
        return finalResult;
    }
    
    private boolean matchTargetMethod(String methodName)
    {
    	boolean ret = false;
    	if (targetMethods == null 
    			|| targetMethods.isEmpty() 
    			|| targetMethods.contains(methodName))
    	{
    		ret = true;
    	}
    	return ret;
    }
    
}
