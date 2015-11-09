package darks.grid.spring;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import darks.grid.RpcReduceHandler;
import darks.grid.balance.GridBalance;
import darks.grid.balance.RollPolingBalance;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.ExecuteConfig.CallType;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.utils.ReflectUtils;

public class GridRpcSpringConsumerBean<T> implements FactoryBean<T>, InitializingBean
{

    private Class<T> target;
    
    private String proxy = "jdk";
    
    private Class<? extends RpcReduceHandler> reducer;
    
    private ExecuteConfig config = new ExecuteConfig();
    
    private Class<? extends GridBalance> balance = RollPolingBalance.class;
    
    private Set<String> targetMethods = new HashSet<>();
    
    @Override
    public void afterPropertiesSet()
        throws Exception
    {
        if (!"jdk".equals(proxy))
            proxy = "jdk";
    	if (reducer != null)
    	    config.setReducerHandler(ReflectUtils.newInstance(reducer));
    	if (balance != null)
    	    config.setBalance(ReflectUtils.newInstance(balance));
    }

    @Override
    public T getObject() throws Exception
    {
        if ("jdk".equals(proxy))
        {
            ProxyBuilder builder = new JdkProxyBuilder(config, targetMethods);
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

    public void setCallType(String callType)
    {
        config.setCallType(CallType.valueOf(callType.toUpperCase()));
    }

    public void setResponseType(String responseType)
    {
        config.setResponseType(ResponseType.valueOf(responseType.toUpperCase()));
    }

    public void setTimeoutSeconds(int timeoutSeconds)
    {
        config.setTimeoutSeconds(timeoutSeconds);
    }

	public Class<? extends GridBalance> getBalance()
	{
		return balance;
	}

	public void setBalance(Class<? extends GridBalance> balance)
	{
		this.balance = balance;
	}

	public Set<String> getTargetMethods()
	{
		return targetMethods;
	}

	public void setTargetMethods(Set<String> targetMethods)
	{
		this.targetMethods = targetMethods;
	}
}
