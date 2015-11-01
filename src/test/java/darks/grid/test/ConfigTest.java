package darks.grid.test;

import org.junit.Assert;
import org.junit.Test;

import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;

public class ConfigTest
{

    @Test
    public void testLoadConfig()
    {
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
        Assert.assertNotNull(config);
        System.out.println(config);
    }
    
}
