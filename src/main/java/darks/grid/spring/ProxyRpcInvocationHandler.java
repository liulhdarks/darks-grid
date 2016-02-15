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
package darks.grid.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.RpcReduceHandler;
import darks.grid.annotations.GridAnnotationParser;
import darks.grid.annotations.GridClassAnnotation;
import darks.grid.annotations.GridMethodAnnotation;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.TaskResultListener;
import darks.grid.executor.task.rpc.RpcResult;
import darks.grid.utils.ReflectUtils;

public class ProxyRpcInvocationHandler implements InvocationHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(ProxyRpcInvocationHandler.class);
	
	private static RpcReduceHandler defaultReducer = new DefaultRpcReduceHandler();
	
	ExecuteConfig config = null;
	
	Set<String> targetMethods = null;
	
	TaskResultListener taskResultListener = null;
	
	boolean asyncInvoke = false;
	
	GridClassAnnotation classAnnotations;
	
	public ProxyRpcInvocationHandler(Class<?> interfaceClass, RpcProxyBean proxyBean)
	{
		config = proxyBean.getConfig();
		targetMethods = proxyBean.getTargetMethods();
		taskResultListener = proxyBean.getTaskResultListener();
		asyncInvoke = proxyBean.isAsyncInvoke();
		classAnnotations = GridAnnotationParser.parse(interfaceClass);
	} 
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
    	if (!matchTargetMethod(method.getName()))
    		return method.invoke(proxy, args);
        String methodName = ReflectUtils.getMethodUniqueName(method);
        ExecuteConfig methodConfig = config;
        if (classAnnotations != null) 
        {
            GridMethodAnnotation methodAnnotation = classAnnotations.getMethodAnnotation(methodName);
            if (methodAnnotation != null)
            {
                methodConfig = methodAnnotation.getMethodConfig();
            }
        }
        if (asyncInvoke)
        {
        	RpcExecutor.asyncCallMethod(methodName, args, method.getParameterTypes(), methodConfig, taskResultListener);
        	return null;
        }
        RpcResult result = RpcExecutor.callMethod(methodName, args, method.getParameterTypes(), methodConfig);
        if (!result.isSuccess())
        {
        	log.error("Fail to invoke method " + methodName + ". Cause " + result.getErrorMessage());
        }
        RpcReduceHandler reducer = methodConfig.getReducerHandler() == null ? defaultReducer : methodConfig.getReducerHandler();
        Object finalResult = reducer.reduce(proxy, method, args, result);
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
