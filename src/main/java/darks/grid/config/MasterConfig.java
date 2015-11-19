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

import darks.grid.GridException;
import darks.grid.master.MasterTask;


public class MasterConfig
{

	private boolean autoMaster = false;
	
	private Integer minSlaveNum = 0;
	
	private Map<String, Class<? extends MasterTask>> taskClasses = new HashMap<>();
	
	public void addTaskClass(String name, String className)
	{
		try
		{
			@SuppressWarnings("unchecked")
			Class<? extends MasterTask> taskClass = (Class<? extends MasterTask>) Class.forName(className);
			if (taskClass != null)
			{
				taskClasses.put(name, taskClass);
			}
		}
		catch (Exception e)
		{
			throw new GridException("Fail to add master task " + className + ". Cause " + e.getMessage(), e);
		}
	}
	
	public boolean isAutoMaster()
	{
		return autoMaster;
	}

	public void setAutoMaster(boolean autoMaster)
	{
		this.autoMaster = autoMaster;
	}

	public Map<String, Class<? extends MasterTask>> getTaskClasses()
	{
		return taskClasses;
	}

	public Integer getMinSlaveNum()
	{
		return minSlaveNum;
	}

	public void setMinSlaveNum(Integer minSlaveNum)
	{
		this.minSlaveNum = minSlaveNum;
	}

	@Override
	public String toString()
	{
		return "MasterConfig [autoMaster=" + autoMaster + ", minSlaveNum=" + minSlaveNum
				+ ", taskClasses=" + taskClasses + "]";
	}


}
