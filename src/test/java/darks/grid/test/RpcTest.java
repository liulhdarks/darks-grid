package darks.grid.test;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

import org.junit.Test;

import darks.grid.GridRuntime;
import darks.grid.beans.MethodResult;
import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.config.MethodConfig;
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
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
	}

	@Test
	public void testExecuteRpc()
	{
		ResourceLeakDetector.setLevel(Level.PARANOID);
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		RpcExecutor.registerMethod("print", RemoteObject.class, new RemoteObject());
		ThreadUtils.threadSleep(3000);
		MethodResult result = RpcExecutor.callMethod("print", null, new MethodConfig());
		System.out.println(result);
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
		
	}
	
}
