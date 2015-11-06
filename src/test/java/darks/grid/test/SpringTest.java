package darks.grid.test;

import java.util.List;

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
		int ret = rpcComputer.add(1, 2);
		System.out.println(ret);
		while(true)
		{
			ThreadUtils.threadSleep(10000);
		}
    }
}
