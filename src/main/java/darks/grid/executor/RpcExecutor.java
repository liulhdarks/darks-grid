package darks.grid.executor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridRpcBean;
import darks.grid.beans.MethodResult;
import darks.grid.config.MethodConfig;
import darks.grid.config.MethodConfig.ResponseType;
import darks.grid.executor.task.RpcTask;
import darks.grid.executor.task.TaskResultListener;
import darks.grid.executor.task.rpc.MethodJob;
import darks.grid.executor.task.rpc.MethodJobReply;
import darks.grid.executor.task.rpc.MethodRequest;
import darks.grid.utils.ReflectUtils;
import darks.grid.utils.ThreadUtils;


public class RpcExecutor extends GridExecutor
{
    
    private static final Logger log = LoggerFactory.getLogger(RpcExecutor.class);
	
	static Map<String, GridRpcBean> rpcMap = new ConcurrentHashMap<>();

	public static boolean registerMethod(String methodName, Class<?> targetClass, Object targetObject)
	{
		if (!rpcMap.containsKey(methodName))
		{
			GridRpcBean bean = new GridRpcBean(methodName, targetClass, targetObject);
			rpcMap.put(methodName, bean);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static MethodResult callMethod(String methodName, Object[] params, MethodConfig config)
	{
	    if (config == null)
	        config = new MethodConfig();
	    config.fixType();
	    MethodRequest request = new MethodRequest(methodName, params, config);
	    RpcTask task = new RpcTask(request);
	    FutureTask<MethodResult> future = GridRuntime.tasks().executeTask(task);
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
            return MethodResult.fail("Fail to call method " + methodName + ". Cause " + e.getMessage());
        }
	}
    
    public static void asyncCallMethod(String methodName, Object[] params, MethodConfig config, 
                        TaskResultListener<MethodResult> listenr)
    {
        if (config == null)
            config = new MethodConfig();
        config.fixType();
        MethodRequest request = new MethodRequest(methodName, params, config);
        RpcTask task = new RpcTask(request, listenr);
        ThreadUtils.submitTask(task);
    }
	
	public static MethodJobReply executeMethod(MethodJob job)
	{
		MethodJobReply rep = new MethodJobReply(job);
		String methodName = job.getMethodName();
		GridRpcBean bean = rpcMap.get(methodName);
		if (bean == null)
			return rep.failed(MethodJobReply.ERR_NO_METHOD, "Cannot find method " + methodName);
		Object obj = null;
		obj = bean.getTargetObject();
		if (obj == null)
		{
			if (bean.getTargetClass() == null)
			{
			    return rep.failed(MethodJobReply.ERR_INVALID_OBJANDCLASS, 
			        "Invalid target object and class which is null.");
			}
			obj = ReflectUtils.newInstance(bean.getTargetClass());
	        if (obj == null)
	            return rep.failed(MethodJobReply.ERR_INSTANCE_CLASS_FAIL, "Fail to instance class " + bean.getTargetClass());
		}
		try
        {
	        Class<?>[] paramsTypes = ReflectUtils.getObjectClasses(job.getParams());
	        Method method = ReflectUtils.getDeepMethod(obj.getClass(), methodName, paramsTypes);
	        if (method == null)
                return rep.failed(MethodJobReply.ERR_GET_CLASS_METHOD, 
                    "Fail to get deep method " + methodName + " from " + obj.getClass() + " [" + paramsTypes + "]");
	        if (!method.isAccessible())
	        	method.setAccessible(true);
	        Object retObj = ReflectUtils.invokeMethod(obj, method, job.getParams());
	        rep.setResult(retObj);
	        return rep;
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return rep.failed(MethodJobReply.ERR_INVOKE_EXCEPTION, 
                "Fail to invoke method " + methodName + ". Cause " + e.getMessage());
        }
	}
}
