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

package darks.grid.config;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import darks.grid.GridException;
import darks.grid.master.MasterTask;
import darks.grid.utils.ParamsUtils;


public class MasterConfig
{

	private boolean autoMaster = false;
	
	private int minSlaveNum = 0;
	
	private Map<String, MasterTaskConfig> taskMaps = new HashMap<String, MasterTaskConfig>();
	
	public void addTaskConfig(MasterTaskConfig taskConfig)
	{
		taskMaps.put(taskConfig.getTaskName(), taskConfig);
	}
	
	public boolean isAutoMaster()
	{
		return autoMaster;
	}

	public void setAutoMaster(boolean autoMaster)
	{
		this.autoMaster = autoMaster;
	}
	

	public Map<String, MasterTaskConfig> getTaskMaps()
	{
		return taskMaps;
	}

	public int getMinSlaveNum()
	{
		return minSlaveNum;
	}

	public void setMinSlaveNum(int minSlaveNum)
	{
		this.minSlaveNum = minSlaveNum;
	}


	@Override
	public String toString()
	{
		return "MasterConfig [autoMaster=" + autoMaster + ", minSlaveNum="
				+ minSlaveNum + ", taskMaps=" + taskMaps + "]";
	}



	public static class MasterTaskConfig
	{
		Class<? extends MasterTask> taskClass;
		
		String taskName = null;
		
    	String taskClassName = null;
		
		long interval = 1000;
		
		boolean singleUse = false;
		
		@SuppressWarnings("unchecked")
		public void setTaskClass(String className)
		{
			try
			{
				taskClass = (Class<? extends MasterTask>) Class.forName(className);
			}
			catch (Exception e)
			{
				throw new GridException("Fail to add master task " + className + ". Cause " + e.getMessage(), e);
			}
		}
		
		public void parse(Element el)
		{
        	if (el.hasAttribute("name"))
        		taskName = el.getAttribute("name");
        	if (el.hasAttribute("class"))
        		taskClassName = el.getAttribute("class");
        	else
        		throw new GridException("Invalid master task config " + el);
        	if (taskClassName == null || "".equals(taskClassName.trim()))
        		throw new GridException("Invalid master task className:" + taskClassName);
        	if (taskName == null || "".equals(taskName.trim()))
        		taskName = taskClassName;
			if (el.hasAttribute("interval"))
			{
        		String strInterval = el.getAttribute("interval");
        		interval = ParamsUtils.parseTime(strInterval);
        		if (interval < 0)
            		throw new GridException("Invalid master task interval:" + interval);
			}
            if (el.hasAttribute("single_use"))
            {
                String strSingleUse = el.getAttribute("single_use");
                singleUse = Boolean.parseBoolean(strSingleUse);
            }
			setTaskClass(taskClassName);
		}

		public Class<? extends MasterTask> getTaskClass()
		{
			return taskClass;
		}

		public void setTaskClass(Class<? extends MasterTask> taskClass)
		{
			this.taskClass = taskClass;
		}

		public String getTaskName()
		{
			return taskName;
		}

		public void setTaskName(String taskName)
		{
			this.taskName = taskName;
		}

		public String getTaskClassName()
		{
			return taskClassName;
		}

		public void setTaskClassName(String taskClassName)
		{
			this.taskClassName = taskClassName;
		}

		public long getInterval()
		{
			return interval;
		}

		public void setInterval(long interval)
		{
			this.interval = interval;
		}

        public boolean isSingleUse() {
            return singleUse;
        }

        public void setSingleUse(boolean singleUse) {
            this.singleUse = singleUse;
        }
		
		
	}
}
