package darks.grid.test;

import org.junit.Test;

import darks.grid.GridRuntime;
import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.utils.ThreadUtils;

public class GridServerTest
{

	@Test
	public void testStartup()
	{
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}
	
}
