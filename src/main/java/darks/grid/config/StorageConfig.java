package darks.grid.config;

import java.io.File;


public class StorageConfig
{

	private String rootDir;
	
	public StorageConfig()
	{
		
	}

	public String getRootDir()
	{
		return rootDir;
	}

	public void setRootDir(String rootDir)
	{
		this.rootDir = rootDir;
	}
	
	public File getRootDirFile()
	{
		if (rootDir == null || "".equals(rootDir.trim()))
			rootDir = System.getProperty("java.io.tmpdir");
		if (rootDir == null || "".equals(rootDir.trim()))
			rootDir = "/";
		File dir = new File(rootDir);
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	@Override
	public String toString()
	{
		return "StorageConfig [rootDir=" + rootDir + "]";
	}
	
	
}
