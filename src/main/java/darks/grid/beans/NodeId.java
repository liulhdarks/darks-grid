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
package darks.grid.beans;

import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;

import darks.grid.GridException;
import darks.grid.GridRuntime;
import darks.grid.utils.BytesUtils;
import darks.grid.utils.NetworkUtils;

public final class NodeId
{

	private NodeId()
	{
		
	}
	
	public static String localId()
	{
		if (GridRuntime.network() == null)
			throw new GridException("Grid network hasn't been inited. Invalid local id.");
		try
		{
			byte[] macBytes = NetworkUtils.getMacBytes();
			String proccessId = ManagementFactory.getRuntimeMXBean().getName();  
			GridAddress ipAddress = GridRuntime.network().getBindAddress();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (macBytes != null)
			    baos.write(macBytes);
			if (proccessId != null)
			    baos.write(proccessId.getBytes());
			baos.write(ipAddress.toString().getBytes());
			byte[] bytes = baos.toByteArray();
			return BytesUtils.md5(bytes);
		}
		catch (Exception e)
		{
			throw new GridException("Fail to get local id. Cause " + e.getMessage(), e);
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		System.out.println(localId());  
	}
}
