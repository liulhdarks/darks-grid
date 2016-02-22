/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.commons;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridConstant;
import darks.grid.beans.MachineInfo;
import darks.grid.config.GridConfiguration;
import darks.grid.utils.ReflectUtils;

public abstract class MachineInfoFactory
{
    
    private static final Logger log = LoggerFactory.getLogger(MachineInfoFactory.class);

	public abstract MachineInfo createMachineInfo();
	
	public abstract MachineInfo updateMachineInfo(MachineInfo info);
	
	public static MachineInfoFactory buildFactory(GridConfiguration config)
	{
	    String customFactory = config.getConstantConfig().getConstant(GridConstant.CFG_MACHINE_FACTORY);
	    if (customFactory != null && !"".equals(customFactory.trim()))
	    {
	        try {
	            MachineInfoFactory factory = (MachineInfoFactory) ReflectUtils.newInstance(Class.forName(customFactory));
	            if (factory != null)
	                return factory;
	        } catch (Exception e) {
                log.error("Fail to instance custom machine factory. Cause " + e.getMessage(), e);
            }
	    }
	    if (isGenericJdk7Valid())
	        return new GenericMachineInfoFactory();
	    if (isSigarValid())
	        return new SigarMachineInfoFactory();
        if (isGenericValid())
            return new GenericMachineInfoFactory();
        return new BlankMachineInfoFactory();
	}
    
    private static boolean isGenericValid() {
        try {
            return Class.forName("com.sun.management.OperatingSystemMXBean") != null;
        } catch (Exception e) {
            return false;
        }
    } 
    
    private static boolean isGenericJdk7Valid() {
        try {
            Class<?> clazz = Class.forName("com.sun.management.OperatingSystemMXBean");
            if (clazz != null)
            {
                Method method = ReflectUtils.getDeepMethod(clazz, "getProcessCpuLoad");
                return method != null;
            }
        } catch (Exception e) {
        }
        return false;
    }
	
    private static boolean isSigarValid() {
        try {
            return Class.forName("org.hyperic.sigar.CpuPerc") != null
                    && Class.forName("org.hyperic.sigar.ProcCpu") != null;
        } catch (Exception e) {
            return false;
        }
    }
}
