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

package darks.grid.master;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.meta.MasterMeta;
import darks.grid.config.GridConfiguration;
import darks.grid.manager.GridManager;

public class GridMasterManager implements GridManager
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMasterManager.class);

	private ElectionStrategy strategy = new ModeratorElectionStrategy();
	
	private Lock lock = new ReentrantLock();
	

	@Override
	public boolean initialize(GridConfiguration config)
	{
		if (GridRuntime.config().isAutoMaster())
		{
			GridRuntime.components().setupComponent(new MasterChecker());
		}
		return true;
	}

	@Override
	public void destroy()
	{
		
	}
	
	public boolean checkMaster()
	{
		if (!GridRuntime.config().isAutoMaster())
			return false;
		if (!lock.tryLock())
			return false;
		try
		{
			return strategy.checkElect() != null;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public boolean electMaster()
	{
		if (!lock.tryLock())
			return false;
		try
		{
			String nodeId = strategy.elect();
			return nodeId != null;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public boolean electSelfMaster(boolean force)
	{
		lock.lock();
		try
		{
			MasterMeta meta = new MasterMeta(GridRuntime.getLocalId(), GridRuntime.context().getServerAddress());
			return GridRuntime.events().publishAll(GridEvent.CONFIRM_MASTER, meta);
		}
		finally
		{
			lock.unlock();
		}
	}
	
}
