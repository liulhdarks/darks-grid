package darks.grid.test;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

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
		ResourceLeakDetector.setLevel(Level.PARANOID);
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}
	
}
