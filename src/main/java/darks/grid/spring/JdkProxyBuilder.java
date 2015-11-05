package darks.grid.spring;

import java.lang.reflect.Proxy;

public class JdkProxyBuilder implements ProxyBuilder
{
    
    @Override
    public Object build(Class<?> interfaceClass)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),   
            new Class<?>[]{interfaceClass}, new ProxyInvocationHandler()); 
    }
    
}
