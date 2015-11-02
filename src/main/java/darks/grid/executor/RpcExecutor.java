package darks.grid.executor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import darks.grid.beans.GridRpcBean;
import darks.grid.beans.MethodRequest;
import darks.grid.beans.MethodResponse;
import darks.grid.utils.ReflectUtils;


public class RpcExecutor extends GridExecutor
{
	
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
	
	public static boolean callMethod(String methodName, CallType callType)
	{
		return true;
	}
	
	public static boolean executeMethod(MethodRequest request)
	{
		MethodResponse rep = new MethodResponse();
		String methodName = request.getMethodName();
		GridRpcBean bean = rpcMap.get(methodName);
		if (bean == null)
			return false;
		Object obj = null;
		obj = bean.getTargetObject();
		if (obj == null)
		{
			if (bean.getTargetClass() == null)
			{
				return false;
			}
			obj = ReflectUtils.newInstance(bean.getTargetClass());
		}
		if (obj == null)
			return false;
		Class<?>[] paramsTypes = ReflectUtils.getObjectClasses(request.getParams());
		Method method = ReflectUtils.getDeepMethod(obj.getClass(), methodName, paramsTypes);
		Object retObj = ReflectUtils.invokeMethod(obj, method, request.getParams().toArray());
		
		return false;
	}
}
