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

package darks.grid.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils
{
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
	

	private FileUtils()
	{
		
	}
	
	public static boolean appendLine(File file, String content)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(content + "\n");
			writer.flush();
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
		finally
		{
			IOUtils.closeStream(writer);
		}
	}
	
	public static List<String> readLineToList(File file)
	{
		List<String> result = new LinkedList<String>();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if ("".equals(line))
					continue;
				result.add(line);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		} 
		finally
		{
			IOUtils.closeStream(reader);
		}
		return result;
	}
}
