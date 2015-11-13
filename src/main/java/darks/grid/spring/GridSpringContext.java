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

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GridSpringContext
{

	private static ApplicationContext context;

	public static ApplicationContext getContext()
	{
		return context;
	}

	public static void setContext(ApplicationContext context)
	{
		GridSpringContext.context = context;
	}
	
	public static boolean loadContext(ServletContext ctx)
	{
		context = WebApplicationContextUtils.getWebApplicationContext(ctx);
		return context != null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name)
	{
		if (context == null)
			return null;
		return (T)context.getBean(name);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType)
	{
		if (context == null)
			return null;
		return (T)context.getBean(name, requiredType);
	}
	
	public static <T> T getBean(Class<T> requiredType)
	{
		if (context == null)
			return null;
		return (T)context.getBean(requiredType);
	}
	
	public static boolean containsBean(String name)
	{
		if (context == null)
			return false;
		return context.containsBean(name);
	}
}
