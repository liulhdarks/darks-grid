package darks.grid.spring;

import java.lang.reflect.Proxy;

public class JdkProxyBuilder implements ProxyBuilder
{
	
	private RpcProxyBean proxyBean;
	
	public JdkProxyBuilder(RpcProxyBean proxyBean)
	{
		this.proxyBean = proxyBean;
	}
    
    @Override
    public Object build(Class<?> interfaceClass)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),   
            new Class<?>[]{interfaceClass}, new ProxyRpcInvocationHandler(proxyBean)); 
    }
    
}
