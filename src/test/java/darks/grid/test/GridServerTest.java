package darks.grid.test;

import org.junit.Test;

import darks.grid.GridContext;

public class GridServerTest
{

	@Test
	public void testStartup()
	{
		GridContext.initialize();
		while(true)
		{
			try
			{
				Thread.sleep(10000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
