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
