package darks.grid.executor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridException;
import darks.grid.GridRuntime;
import darks.grid.beans.GridRpcMethod;
import darks.grid.beans.MethodResult;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.task.GridJobReply;
import darks.grid.executor.task.TaskResultListener;
import darks.grid.executor.task.rpc.GridRpcJob;
import darks.grid.executor.task.rpc.GridRpcTask;
import darks.grid.executor.task.rpc.RpcRequest;
import darks.grid.utils.ReflectUtils;


public class RpcExecutor extends GridExecutor
{
    
    private static final Logger log = LoggerFactory.getLogger(RpcExecutor.class);
	
	static Map<String, GridRpcMethod> rpcMap = new ConcurrentHashMap<>();

    public static boolean registerMethod(Method method, Class<?> targetClass, Object targetObject)
    {
        String uniqueName = ReflectUtils.getMethodUniqueName(method);
        return registerMethod(uniqueName, method.getName(), targetClass, targetObject, method);
    }
    
    public static boolean registerMethod(String methodName, Class<?>[] types, Class<?> targetClass, Object targetObject)
    {
        Class<?> clazz = targetObject == null ? targetClass : targetObject.getClass();
        Method method = ReflectUtils.getDeepMethod(clazz, methodName, types);
        if (method == null)
            throw new GridException("Cannot find method " + methodName + " " + Arrays.toString(types));
        String uniqueName = ReflectUtils.getMethodUniqueName(method);
        return registerMethod(uniqueName, methodName, targetClass, targetObject, method);
    }

	public static boolean registerMethod(String uniqueName, Class<?> targetClass, Object targetObject)
	{
		return registerMethod(uniqueName, uniqueName, targetClass, targetObject, null);
	}
    
    public static boolean registerMethod(String uniqueName, String methodName, Class<?> targetClass, Object targetObject)
    {
        return registerMethod(uniqueName, methodName, targetClass, targetObject, null);
    }
	
	public static boolean registerMethod(String uniqueName, String methodName, Class<?> targetClass, 
	            Object targetObject, Method method)
	{
		if (!rpcMap.containsKey(uniqueName))
		{
			GridRpcMethod bean = new GridRpcMethod(methodName, targetClass, targetObject, method);
			rpcMap.put(uniqueName, bean);
			return true;
		}
		else
		{
			return false;
		}
	}

    public static MethodResult callMethod(String uniqueName, Object[] params, ExecuteConfig config) {
        Class<?>[] types = ReflectUtils.getObjectClasses(params);
        return callMethod(uniqueName, params, types, config);
    }

    public static MethodResult callMethod(String uniqueName, Object[] params, Class<?>[] types, ExecuteConfig config)
	{
	    if (config == null)
	        config = new ExecuteConfig();
	    config.fixType();
	    RpcRequest request = new RpcRequest(uniqueName, params, types);
	    GridRpcTask task = new GridRpcTask();
	    FutureTask<MethodResult> future = GridRuntime.tasks().executeMapReduceTask(task, request, config, null);
	    if (config.getResponseType() == ResponseType.NONE)
	        return new MethodResult();
	    try
        {
	        if (config.getTimeoutSeconds() <= 0)
	            return future.get();
	        else
	            return future.get(config.getTimeoutSeconds(), TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return MethodResult.fail("Fail to call method " + uniqueName + ". Cause " + e.getMessage());
        }
	}

    public static void asyncCallMethod(String uniqueName, Object[] params, ExecuteConfig config,
                                       TaskResultListener<MethodResult> listener)
    {
        Class<?>[] types = ReflectUtils.getObjectClasses(params);
        asyncCallMethod(uniqueName, params, types, config, listener);
    }
    
    public static void asyncCallMethod(String uniqueName, Object[] params, Class<?>[] types, ExecuteConfig config,
                        TaskResultListener<MethodResult> listener)
    {
        if (config == null)
            config = new ExecuteConfig();
        config.fixType();
        RpcRequest request = new RpcRequest(uniqueName, params, types);
        GridRpcTask task = new GridRpcTask();
        //TODO listener
        GridRuntime.tasks().executeMapReduceTask(task, request, config, listener);
    }

    public static GridRpcMethod getRpcMethod(String uniqueName)
    {
        return rpcMap.get(uniqueName);
    }
	
}
