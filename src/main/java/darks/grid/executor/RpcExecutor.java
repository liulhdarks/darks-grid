/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.executor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridException;
import darks.grid.GridRuntime;
import darks.grid.beans.GridRpcMethod;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.task.TaskResultListener;
import darks.grid.executor.task.rpc.GridRpcTask;
import darks.grid.executor.task.rpc.RpcRequest;
import darks.grid.executor.task.rpc.RpcResult;
import darks.grid.master.ElectRpcInvoker;
import darks.grid.utils.ReflectUtils;


public class RpcExecutor extends GridExecutor
{
    
    private static final Logger log = LoggerFactory.getLogger(RpcExecutor.class);
	
	static Map<String, GridRpcMethod> rpcMap = new ConcurrentHashMap<String, GridRpcMethod>();
	
	public static void registerSystemMethod()
	{
		ElectRpcInvoker elect = new ElectRpcInvoker();
		registerMethod("getNodeStatus", new Class<?>[0], elect.getClass(), elect);
	}

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
			log.info("Register method unique " + uniqueName);
			GridRpcMethod bean = new GridRpcMethod(methodName, targetClass, targetObject, method);
			rpcMap.put(uniqueName, bean);
			return true;
		}
		else
		{
			return false;
		}
	}

    public static RpcResult callMethod(Class<?> targetClass, String methodName, Object[] params, ExecuteConfig config) 
    {
    	Class<?>[] types = ReflectUtils.getObjectClasses(params);
    	Method method = ReflectUtils.getDeepMethod(targetClass, methodName, types);
        if (method == null)
            throw new GridException("Cannot find method " + methodName + " " + Arrays.toString(types));
        String uniqueName = ReflectUtils.getMethodUniqueName(method);
        return callMethod(uniqueName, params, types, config);
    }

    public static RpcResult callMethod(String uniqueName, Object[] params, ExecuteConfig config) 
    {
        Class<?>[] types = ReflectUtils.getObjectClasses(params);
        return callMethod(uniqueName, params, types, config);
    }

    public static RpcResult callMethod(String uniqueName, Object[] params, Class<?>[] types, ExecuteConfig config)
	{
	    if (config == null)
	        config = new ExecuteConfig();
	    RpcRequest request = new RpcRequest(uniqueName, params, types);
	    GridRpcTask task = new GridRpcTask();
	    FutureTask<RpcResult> future = GridRuntime.tasks().executeMapReduceTask(task, request, config, null);
	    if (config.getResponseType() == ResponseType.NONE)
	        return new RpcResult();
	    try
        {
	        RpcResult result = null;
	        if (config.getTimeout() <= 0)
	            result = future.get();
	        else
	            result = future.get(config.getTimeout(), TimeUnit.MILLISECONDS);
	        if (result == null)
	            result = RpcResult.fail("Fail to call method " + uniqueName + ". Cause null result.");
	        return result;
        }
        catch (TimeoutException e)
        {
            return RpcResult.fail("Fail to call method " + uniqueName + ". Cause timeout " + config.getTimeout());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return RpcResult.fail("Fail to call method " + uniqueName + ". Cause " + e.getMessage());
        }
	}

    public static void asyncCallMethod(String uniqueName, Object[] params, ExecuteConfig config,
                                       TaskResultListener listener)
    {
        Class<?>[] types = ReflectUtils.getObjectClasses(params);
        asyncCallMethod(uniqueName, params, types, config, listener);
    }
    
    public static void asyncCallMethod(String uniqueName, Object[] params, Class<?>[] types, ExecuteConfig config,
                        TaskResultListener listener)
    {
        if (config == null)
            config = new ExecuteConfig();
        RpcRequest request = new RpcRequest(uniqueName, params, types);
        GridRpcTask task = new GridRpcTask();
        GridRuntime.tasks().executeMapReduceTask(task, request, config, listener);
    }

    public static GridRpcMethod getRpcMethod(String uniqueName)
    {
        return rpcMap.get(uniqueName);
    }
	
}
