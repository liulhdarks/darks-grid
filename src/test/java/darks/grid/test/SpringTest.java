package darks.grid.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import darks.grid.test.spring.RpcComputer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class SpringTest extends AbstractJUnit4SpringContextTests
{

    @Resource
    RpcComputer rpcComputer;
    
    @Test
    public void testSpring()
    {
        rpcComputer.add(1, 2, null, new int[]{1});
    }
}
