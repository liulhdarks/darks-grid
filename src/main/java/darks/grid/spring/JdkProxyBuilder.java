package darks.grid.spring;

import java.lang.reflect.Proxy;

import darks.grid.RpcReduceHandler;
import darks.grid.config.MethodConfig;

public class JdkProxyBuilder implements ProxyBuilder
{
	
	private MethodConfig config;
	
	public JdkProxyBuilder(MethodConfig config)
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
