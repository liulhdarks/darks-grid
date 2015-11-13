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

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import darks.grid.GridException;
import darks.grid.executor.RpcExecutor;

public class GridRpcSpringProviderBean implements ApplicationContextAware, InitializingBean
{

	private ApplicationContext ctx;
	
	private Class<?> serviceInterface;
	
	private Object target;
	
	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (target == null || serviceInterface == null)
		{
			throw new GridException("Invalid target or serviceInterface config.");
		}
		Method[] methods = serviceInterface.getMethods();
		for (Method method : methods)
		{
	        RpcExecutor.registerMethod(method, target.getClass(), target);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.ctx = applicationContext;
	}

	public Class<?> getServiceInterface()
	{
		return serviceInterface;
	}

	public void setServiceInterface(Class<?> serviceInterface)
	{
		this.serviceInterface = serviceInterface;
	}

	public Object getTarget()
	{
		return target;
	}

	public void setTarget(Object target)
	{
		this.target = target;
	}

}
