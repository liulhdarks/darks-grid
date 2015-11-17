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

package darks.grid.manager;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import darks.grid.config.GridConfiguration;
import darks.grid.utils.FileUtils;
import darks.grid.utils.StringUtils;

public class GridStorageManager implements GridManager
{
	
	private static final String FILE_HISTORY_NODES = "history_nodes.dat";

	private File rootDir;
	
	private GridConfiguration config;
	
	private File historyNodesFile;
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
		this.config = config;
		rootDir = config.getStorageConfig().getRootDirFile();
		if (!rootDir.exists())
			rootDir.mkdirs();
		historyNodesFile = new File(rootDir, FILE_HISTORY_NODES);
		return true;
	}

	@Override
	public void destroy()
	{

	}
	
	public synchronized void cacheHistoryNodes(InetSocketAddress address)
	{
		boolean cached = config.getNetworkConfig().isCacheHistoryNodes();
		if (!cached)
			return;
		String addr = StringUtils.stringBuffer(address.getAddress().getHostAddress(), ':', address.getPort());
		FileUtils.appendLine(historyNodesFile, addr);
	}
	
	public synchronized Collection<InetSocketAddress> getCacheHistoryNodes()
	{
		if (historyNodesFile.exists())
		{
			List<String> addresses = FileUtils.readLineToList(historyNodesFile);
			Set<InetSocketAddress> result = new HashSet<>(addresses.size());
			for (String addr : addresses)
			{
				String[] datas = addr.split(":");
				result.add(new InetSocketAddress(datas[0], Integer.parseInt(datas[1])));
			}
			return result;
		}
		else
			return new ArrayList<>(0);
	}

}
