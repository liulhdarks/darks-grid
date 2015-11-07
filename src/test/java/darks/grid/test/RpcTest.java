package darks.grid.test;

import java.util.Random;
import java.util.Scanner;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

import org.junit.Test;

import darks.grid.GridRuntime;
import darks.grid.beans.MethodResult;
import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.RpcExecutor;
import darks.grid.utils.MachineUtils;
import darks.grid.utils.ThreadUtils;

public class RpcTest
{

	@Test
	public void testStartRpcNode()
	{
		ResourceLeakDetector.setLevel(Level.PARANOID);
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		RpcExecutor.registerMethod("print", RemoteObject.class, new RemoteObject());
		RpcExecutor.registerMethod("add", RemoteObject.class, new RemoteObject());
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}

	@Test
	public void testExecuteRpc()
	{
		Random rand = new Random(System.currentTimeMillis());
		ResourceLeakDetector.setLevel(Level.PARANOID);
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		RpcExecutor.registerMethod("print", RemoteObject.class, new RemoteObject());
		RpcExecutor.registerMethod("add", RemoteObject.class, new RemoteObject());
		ThreadUtils.threadSleep(3000);
		System.out.println("ready scanner");
		Scanner scan = new Scanner(System.in);
		while (scan.hasNext())
		{
			String cmd = scan.next();
			MethodResult result = null;
			if ("print".equals(cmd))
			{
				result = RpcExecutor.callMethod("print", null, new ExecuteConfig());
				System.out.println(result);
			}
			else if (cmd.startsWith("add"))
			{
				int a = scan.nextInt();
				int b = scan.nextInt();
				result = RpcExecutor.callMethod("add", new Object[]{a, b}, new ExecuteConfig());
				System.out.println(result);
			}
			else if (cmd.startsWith("loop_add"))
			{
				int n = scan.nextInt();
				for (int i = 1; i <= n; i++)
				{
					int a = rand.nextInt(100);
					int b = rand.nextInt(100);
					long st = System.currentTimeMillis();
					result = RpcExecutor.callMethod("add", new Object[]{a, b}, new ExecuteConfig());
					System.out.println("==========>" + i + " " + result + " cost:" + (System.currentTimeMillis() - st));
					ThreadUtils.threadSleep(10);
				}
			}
		}
		scan.close();
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}

	@Test
	public void testExecuteMultiThreadRpc()
	{
		Random rand = new Random(System.currentTimeMillis());
		ResourceLeakDetector.setLevel(Level.PARANOID);
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		RpcExecutor.registerMethod("print", RemoteObject.class, new RemoteObject());
		RpcExecutor.registerMethod("add", RemoteObject.class, new RemoteObject());
		ThreadUtils.threadSleep(5000);
		for (int i = 1; i <= 100; i++)
		{
			int a = rand.nextInt(100);
			int b = rand.nextInt(100);
			long st = System.currentTimeMillis();
			MethodResult result = RpcExecutor.callMethod("add", new Object[]{a, b}, new ExecuteConfig());
			System.out.println("==========>" + i + " " + result + " cost:" + (System.currentTimeMillis() - st));
			ThreadUtils.threadSleep(10);
		}
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}
	
	class RemoteObject
	{
		public RemoteObject()
		{
			
		}
		
		public String print()
		{
			System.out.println("Execute print " + MachineUtils.getProcessId());
			return MachineUtils.getProcessId();
		}
		
		public Integer add(Integer a, Integer b)
		{
			int ret = a + b;
			System.out.println("Execute add " + a + " + " + b + " = " + ret);
			return ret;
		}
		
	}
	
}
