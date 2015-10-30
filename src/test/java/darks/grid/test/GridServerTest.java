package darks.grid.test;

import org.junit.Test;

import darks.grid.GridConfiguration;
import darks.grid.GridRuntime;
import darks.grid.utils.ThreadUtils;

public class GridServerTest
{

	@Test
	public void testStartup()
	{
		GridConfiguration config = new GridConfiguration();
		config.setListenPort(7800);
		GridRuntime.initialize(config);
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}

	@Test
	public void testStartupNode2()
	{
		GridConfiguration config = new GridConfiguration();
		config.setListenPort(7800);
		GridRuntime.initialize(config);
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}
	
}
