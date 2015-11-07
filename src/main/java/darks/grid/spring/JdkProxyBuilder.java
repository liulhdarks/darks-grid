package darks.grid.spring;

import java.lang.reflect.Proxy;

import darks.grid.RpcReduceHandler;
import darks.grid.executor.ExecuteConfig;

public class JdkProxyBuilder implements ProxyBuilder
{
	
	private ExecuteConfig config;
	
	public JdkProxyBuilder(ExecuteConfig config)
	{
		this.config = config;
	}
    
    @Override
    public Object build(Class<?> interfaceClass)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),   
            new Class<?>[]{interfaceClass}, new ProxyRpcInvocationHandler(config)); 
    }
    
}
