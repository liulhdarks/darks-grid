package darks.grid.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import darks.grid.beans.MethodResult;
import darks.grid.config.MethodConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.utils.ReflectUtils;

public class ProxyInvocationHandler implements InvocationHandler
{
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        String methodName = ReflectUtils.getMethodUniqueName(method);
        System.out.println(methodName);
//        MethodResult result = RpcExecutor.callMethod("add", new Object[]{a, b}, new MethodConfig());
        return null;
    }
    
}
