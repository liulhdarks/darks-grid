/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.spring;

import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import darks.grid.RpcReduceHandler;
import darks.grid.balance.GridBalance;
import darks.grid.balance.RollPolingBalance;
import darks.grid.executor.ExecuteConfig.CallType;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.task.TaskResultListener;
import darks.grid.utils.ReflectUtils;

public class GridRpcSpringConsumerBean<T> implements FactoryBean<T>, InitializingBean
{

    private Class<T> target;
    
    private String proxy = ProxyBuilder.JDK_PROXY_TYPE;
    
    private Class<? extends RpcReduceHandler> reducer;
    
    private Class<? extends GridBalance> balance = RollPolingBalance.class;
    
    private Class<? extends TaskResultListener> taskResultListener;
    
    private RpcProxyBean proxyBean = new RpcProxyBean();
    
    @Override
    public void afterPropertiesSet()
        throws Exception
    {
        if (!ProxyBuilder.JDK_PROXY_TYPE.equals(proxy) 
        		&& !ProxyBuilder.JAVASSIST_PROXY_TYPE.equals(proxy))
            proxy = ProxyBuilder.JDK_PROXY_TYPE;
    	if (reducer != null)
    		proxyBean.getConfig().setReducerHandler(ReflectUtils.newInstance(reducer));
    	if (balance != null)
    		proxyBean.getConfig().setBalance(ReflectUtils.newInstance(balance));
    	if (taskResultListener != null)
    		proxyBean.setTaskResultListener(ReflectUtils.newInstance(taskResultListener));
    }

    @Override
    public T getObject() throws Exception
    {
        if (ProxyBuilder.JDK_PROXY_TYPE.equals(proxy))
        {
            ProxyBuilder builder = new JdkProxyBuilder(proxyBean);
            return (T)builder.build(target);
        }
        else  if (ProxyBuilder.JAVASSIST_PROXY_TYPE.equals(proxy))
        {
            ProxyBuilder builder = new JavassistProxyBuilder(proxyBean);
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
    	proxyBean.getConfig().setCallType(CallType.valueOf(callType.toUpperCase()));
    }

    public void setResponseType(String responseType)
    {
    	proxyBean.getConfig().setResponseType(ResponseType.valueOf(responseType.toUpperCase()));
    }

    public void setTimeout(int timeout)
    {
    	proxyBean.getConfig().setTimeout(timeout);
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
		return proxyBean.getTargetMethods();
	}

	public void setTargetMethods(Set<String> targetMethods)
	{
		proxyBean.setTargetMethods(targetMethods);
	}

	public Class<? extends TaskResultListener> getTaskResultListener()
	{
		return taskResultListener;
	}

	public void setTaskResultListener(Class<? extends TaskResultListener> taskResultListener)
	{
		this.taskResultListener = taskResultListener;
	}
	
	public boolean isAsyncInvoke()
	{
		return proxyBean.isAsyncInvoke();
	}

	public void setAsyncInvoke(boolean asyncInvoke)
	{
		this.proxyBean.setAsyncInvoke(asyncInvoke);
	}
}
