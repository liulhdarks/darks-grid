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
package darks.grid.executor.task;

import darks.grid.executor.job.GridJob;
import darks.grid.utils.UUIDUtils;

public abstract class GridTask
{

    public enum TaskType
    {
        RPC, MAPRED
    }
    
    private String id = UUIDUtils.newUUID();
    
    private TaskType taskType;
    
    public GridTask(TaskType taskType)
    {
        this.id = UUIDUtils.newUUID();
        this.taskType = taskType;
    }
    
    protected boolean failRedo(GridJob job)
	{
		return true;
	}

    public String getId()
    {
        return id;
    }

    public TaskType getTaskType()
    {
        return taskType;
    }
    
}
