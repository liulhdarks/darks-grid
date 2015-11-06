package darks.grid.test.spring;


public class RpcComputerImpl implements RpcComputer
{

	@Override
	public int add(int a, int b)
	{
		return a + b;
	}

}
