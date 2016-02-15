package darks.grid.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import darks.grid.GridRuntime;
import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.test.spring.RpcComputer;
import darks.grid.utils.ThreadUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class SpringTest extends AbstractJUnit4SpringContextTests
{

    @Resource
    RpcComputer rpcComputer;

    @Test
    public void testSpringStartup()
    {
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
    }
    
    @Test
    public void testSpring()
    {
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		GridRuntime.initialize(config);
		ThreadUtils.threadSleep(3000);
        for (int i = 0; i < 5; i++)
        {
//            int ret = rpcComputer.rand(100);
//            Integer ret = rpcComputer.add(5, 2);
            rpcComputer.print(5);
//            System.out.println("result:" + ret);
        }
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
    }
    
    @Test
    public void testSpringThread()
    {
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
        GridRuntime.initialize(config);
        ThreadUtils.threadSleep(3000);
        for (int i = 0; i < 5; i++)
        {
            new Thread()
            {
                public void run()
                {
                    long sum = 0;
                    long count = 0;
                    while (!isInterrupted())
                    {
                        for (int i = 1; i <= 100; i++)
                        {
                            count++;
                            long st = System.currentTimeMillis();
                            int ret = rpcComputer.rand(100);
                            sum += System.currentTimeMillis() - st;
                            float avgCost = (float) sum / (float) count;
                            System.out.println(i + " " + getName() + " result:" + ret + " cost:" + avgCost);
                            ThreadUtils.threadSleep(5);
                        }
                        ThreadUtils.threadSleep(5000);
                    }
                }
            }.start();
        }
        while(true)
        {
            ThreadUtils.threadSleep(10000);
        }
    }
}
