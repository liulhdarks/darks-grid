package darks.grid.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import darks.grid.GridRuntime;
import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.spring.GridSpringContext;

public class GridContextListener implements ServletContextListener
{

	private static final String DEFAULT_CONFIG_PATH = "/grid-config.xml";
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		GridRuntime.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent ctx)
	{
		if (isSpringValid())
			GridSpringContext.loadContext(ctx.getServletContext());
		String configPath = ctx.getServletContext().getInitParameter("configPath");
		if (configPath == null || "".equals(configPath.trim()))
			configPath = DEFAULT_CONFIG_PATH;
		else
			configPath = configPath.trim();
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream(configPath));
        GridRuntime.initialize(config);
	}

	private boolean isSpringValid()
	{
		try
		{
			Class<?> clazz1 = Class.forName("org.springframework.context.ApplicationContext");
			Class<?> clazz2 = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
			return clazz1 != null && clazz2 != null;
		}
		catch (ClassNotFoundException e)
		{
			return false;
		}
	}
}
