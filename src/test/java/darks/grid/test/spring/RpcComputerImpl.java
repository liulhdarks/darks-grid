package darks.grid.test.spring;


import java.util.Random;

public class RpcComputerImpl implements RpcComputer
{
    Random  rand = new Random(System.currentTimeMillis());

	@Override
	public int add(int a, int b)
	{
		return a + b;
	}

    @Override
    public int rand(int base)
    {
        return rand.nextInt(base);
    }

}
