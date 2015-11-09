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
