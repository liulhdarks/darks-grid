package darks.grid.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import darks.grid.RpcReduceHandler;
import darks.grid.utils.ReflectUtils;

public class GridRpcSpringConsumerBean<T> implements FactoryBean<T>, InitializingBean
{

    private Class<T> target;
    
    private String proxy = "jdk";
    
    private Class<? extends RpcReduceHandler> reducer;
    
    @Override
    public void afterPropertiesSet()
        throws Exception
    {
        if (!"jdk".equals(proxy))
            proxy = "jdk";
    }

    @Override
    public T getObject() throws Exception
    {
        if ("jdk".equals(proxy))
        {
        	RpcReduceHandler handler = null;
        	if (reducer != null)
        		handler = ReflectUtils.newInstance(reducer);
            ProxyBuilder builder = new JdkProxyBuilder(handler);
            return (T)builder.build(target);
        }
        return null;
    }

    @Override
    public Class<?> getObjectType()
    {
        return target;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }

    public Class<T> getTarget()
    {
        return target;
    }

    public void setTarget(Class<T> target)
    {
        this.target = target;
    }

    public String getProxy()
    {
        return proxy;
    }

    public void setProxy(String proxy)
    {
        this.proxy = proxy;
    }

	public Class<? extends RpcReduceHandler> getReducer()
	{
		return reducer;
	}

	public void setReducer(Class<? extends RpcReduceHandler> reducer)
	{
		this.reducer = reducer;
	}

}
