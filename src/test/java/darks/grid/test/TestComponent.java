package darks.grid.test;

import darks.grid.network.discovery.GridDiscovery;

public class TestComponent extends GridDiscovery
{

	@Override
	public void findNodes()
	{
		System.out.println("do TestComponent");
	}

}
