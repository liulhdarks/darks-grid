package darks.grid.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class GridRpcFactoryBean<T> implements FactoryBean<T>, InitializingBean
{

    private Class<T> target;
    
    private String proxy = "jdk";
    
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
            ProxyBuilder builder = new JdkProxyBuilder();
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
        return false;
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

    
}
