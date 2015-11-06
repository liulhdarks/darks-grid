package darks.grid.spring;

import java.lang.reflect.Proxy;

import darks.grid.RpcReduceHandler;

public class JdkProxyBuilder implements ProxyBuilder
{
	
	private RpcReduceHandler reducer;
	
	public JdkProxyBuilder()
	{
		
	}
	
	public JdkProxyBuilder(RpcReduceHandler reducer)
	{
		this.reducer = reducer;
	}
    
    @Override
    public Object build(Class<?> interfaceClass)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),   
            new Class<?>[]{interfaceClass}, new ProxyRpcInvocationHandler(reducer)); 
    }
    
}
