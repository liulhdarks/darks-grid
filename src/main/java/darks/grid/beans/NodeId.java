package darks.grid.beans;

import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

import darks.grid.GridRuntime;
import darks.grid.GridException;
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
			InetSocketAddress ipAddress = GridRuntime.network().getBindAddress();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(macBytes);
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
