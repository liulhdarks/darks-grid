package darks.grid.spring;

import java.lang.reflect.Proxy;
import java.util.Set;

import darks.grid.executor.ExecuteConfig;

public class JdkProxyBuilder implements ProxyBuilder
{
	
	private ExecuteConfig config;
	
	private Set<String> targetMethods;
	
	public JdkProxyBuilder(ExecuteConfig config, Set<String> targetMethods)
	{
		this.config = config;
		this.targetMethods = targetMethods;
	}
    
    @Override
    public Object build(Class<?> interfaceClass)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),   
            new Class<?>[]{interfaceClass}, new ProxyRpcInvocationHandler(config, targetMethods)); 
    }
    
}
